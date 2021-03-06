package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import administrator.Administrator;

import model.Beheerder;
import model.Burger;
import model.DocumentCMS;
import model.Erfgoed;
import model.Gemeente;
import model.Instellingen;
import model.Model;

public class Databank
{	
	private Model m;
	private final String online = "jdbc:sqlserver://Projecten2.mssql.somee.com;database=Projecten2;user=JDBC;password=TaskForceB2";
	//private final String offline = "jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc";
	private final String connectie = online; 
	
	public Databank(Model m)
	{
		this.m = m;		
	}
	
	public void laadDatabank()	//model vullen met alle nodige gegevens
	{
		ArrayList<DocumentCMS> documenten = new ArrayList<DocumentCMS>();
		ArrayList<Burger> burgers = new ArrayList<Burger>();
		ArrayList<Erfgoed> erfgoed = new ArrayList<Erfgoed>();
		ArrayList<Beheerder> beheerders = new ArrayList<Beheerder>();
		ArrayList<Instellingen> instellingen = new ArrayList<Instellingen>();
		ArrayList<Gemeente> gemeente = new ArrayList<Gemeente>();
		Connection c = null;
		Statement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.createStatement();
			
			//documenten laden
			rs = s.executeQuery("SELECT * FROM Document WHERE Obsolete = 0 AND WijzigingStatus = 'Actief'");
		
			while (rs.next())
			{
				documenten.add(new DocumentCMS(rs.getInt("DocumentId"),rs.getString("DocumentTitel").trim(), rs.getString("StatusDocument").trim(),rs.getTimestamp("DatumToegevoegd"),rs.getBoolean("Obsolete"), rs.getString("Opmerkingen"), rs.getString("Tekst"), rs.getString("TypeDocument").trim(), rs.getString("ExtensieDocument").trim(),rs.getInt("ErfgoedId"),rs.getString("RedenAfwijzing"), rs.getTimestamp("DatumLaatsteWijziging"), rs.getInt("MediaId"), rs.getInt("BurgerId"), rs.getInt("BeheerderId"),rs.getString("Aard"),m));
			}
			
			//burgers laden
			rs = s.executeQuery("SELECT * FROM Burger");
			
			while (rs.next())
			{
				burgers.add(new Burger(rs.getInt("BurgerId"),rs.getString("Gebruikersnaam"),rs.getString("Voornaam"),rs.getString("Familienaam"), rs.getString("Email"),m));
			}
			
			//erfgoed laden
			rs = s.executeQuery("SELECT * FROM Erfgoed WHERE Obsolete = 0");
			
			while (rs.next())
			{
				erfgoed.add(new Erfgoed(rs.getInt("ErfgoedId"),rs.getString("Naam"), rs.getString("Postcode"), rs.getString("Deelgemeente"), rs.getString("Straat"),
						rs.getString("Huisnr"), rs.getString("Omschrijving"), rs.getString("TypeErfgoed"), rs.getString("Kenmerken"), rs.getString("Geschiedenis"),
						rs.getString("NuttigeInfo"), rs.getString("Link"), rs.getTimestamp("DatumToegevoegd"), rs.getBoolean("Obsolete"), rs.getInt("BurgerId"), rs.getInt("BeheerderId"), m));
			}
			
			//beheerders (en admins) laden
			rs = s.executeQuery("SELECT * FROM Beheerder");
			
			while (rs.next())
			{				
				beheerders.add(new Beheerder(rs.getInt("BeheerderId"), rs.getString("Gebruikersnaam").trim(), rs.getString("Achternaam"),
						rs.getString("Wachtwoord"),rs.getString("Email"), rs.getBoolean("KanBeoordelen"), 
						rs.getBoolean("KanWijzigen"), rs.getBoolean("KanVerwijderen"), rs.getBoolean("KanToevoegen"),rs.getBoolean("IsAdministrator"), rs.getBoolean("StuurEmails"), m));
			}
			
			
			rs= s.executeQuery("SELECT * FROM Instellingen");
			
			while(rs.next())
			{
				instellingen.add(new Instellingen(rs.getInt("InstellingId"),rs.getString("InstellingSleutel"),rs.getString("InstellingWaarde"),rs.getInt("BeheerderId")));
			}
			
			rs = s.executeQuery("SELECT * FROM Gemeente");
			while(rs.next())
			{
				gemeente.add(new Gemeente(rs.getInt("Postcode"),rs.getString("Gemeente")));
			}

		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het sluiten van de verbinding met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		m.setDocumenten(documenten);
		m.setErfgoed(erfgoed);
		m.setBurgers(burgers);
		m.setBeheerders(beheerders);
		m.setInstellingen(instellingen);
		m.setGemeente(gemeente);
	}
	
	
	public int toevoegenDocument(DocumentCMS doc)
	{
		Connection c = null;
		PreparedStatement s = null;
		Statement s2 = null;
		ResultSet rs = null;
		int id = -1;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			
			if (doc.getTypeDocument().equals("Afbeelding"))	
			{
				Blob afbBlob = c.createBlob();
				OutputStream afbStream = afbBlob.setBinaryStream(1);
				ImageIO.write(doc.getImage(), doc.getExtensieDocument(), afbStream);
				
				s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
				s.setBlob(1,afbBlob);
				s.executeUpdate();
				
				s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			else if (doc.getTypeDocument().equals("Video") || doc.getTypeDocument().equals("Andere"))
			{
				FileInputStream data = new FileInputStream(doc.getTemp());
		        
		        s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
		        s.setObject(1, data);
		        s.executeUpdate();
		        
		        s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			
			s = c.prepareStatement("INSERT INTO Document(DocumentTitel, StatusDocument,DatumToegevoegd,Obsolete,Opmerkingen,Tekst,TypeDocument,ExtensieDocument,RedenAfwijzing, DatumLaatsteWijziging, WijzigingStatus, ErfgoedId, MediaId, BeheerderId, Aard) VALUES (?,?,?,?,?,?,?,?,?,?,'Actief', ?,?,?,?)");
			
			s.setString(1, doc.getTitel());
			s.setString(2, doc.getStatus());
			s.setTimestamp(3, doc.getDatumToegevoegd());
			s.setBoolean(4,doc.isVerwijderd());
			s.setString(5, doc.getOpmerkingen());
			s.setString(6, doc.getTekst());
			s.setString(7, doc.getTypeDocument());
			s.setString(8, doc.getExtensieDocument());
			s.setString(9, doc.getRedenAfwijzing());
			s.setTimestamp(10, doc.getDatumGewijzigd());
			s.setInt(11,doc.getErfgoedId());
			if (doc.getTypeDocument().equals("Tekst") || doc.getTypeDocument().equals("Link"))
					s.setNull(12, Types.INTEGER);
			else 
				s.setInt(12, doc.getMediaId());
			s.setInt(13,doc.getBeheerderId());
			s.setString(14,doc.getAard());
				
			s.executeUpdate();
			
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT DocumentId FROM Document ORDER BY DocumentId DESC"));
			rs.next();
			id = rs.getInt("DocumentId");
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, id);
			s.setString(2,"Toegevoegd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException sql)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het toevoegen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			sql.printStackTrace();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het toevoegen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (s2!=null)
					s2.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het toevoegen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public int toevoegenErfgoed(Erfgoed e)
	{
		Connection c = null;
		PreparedStatement s = null;
		Statement s2 = null;
		ResultSet rs = null;
		int id = -1;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			
			s = c.prepareStatement("INSERT INTO Erfgoed(Naam, Postcode, Deelgemeente, Straat, Huisnr, Omschrijving, TypeErfgoed, Kenmerken, Geschiedenis, NuttigeInfo, Link, Obsolete, BeheerderId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			s.setString(1, e.getNaam());
			s.setString(2, e.getPostcode());
			s.setString(3, e.getDeelgemeente());
			s.setString(4, e.getStraat());
			s.setString(5, e.getHuisnr());
			s.setString(6, e.getOmschrijving());
			s.setString(7, e.getTypeErfgoed());
			s.setString(8, e.getKenmerken());
			s.setString(9, e.getGeschiedenis());
			s.setString(10,e.getNuttigeInfo());
			s.setString(11,e.getLink());
			s.setBoolean(12,false);
			s.setInt(13,e.getBeheerderId());
			s.executeUpdate();
			
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT ErfgoedId FROM Erfgoed ORDER BY ErfgoedId DESC"));
			rs.next();
			id = rs.getInt("ErfgoedId");
			
			s = c.prepareStatement("INSERT INTO Logboek (ErfgoedId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, id);
			s.setString(2,"Toegevoegd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException sql)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het toevoegen van een erfgoed!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			sql.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (s2!=null)
					s2.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException exc)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het toevoegen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				exc.printStackTrace();
			}
		}
		return id;
	}
	
	public void verwijderDocument(DocumentCMS doc)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE Document SET Obsolete = 1 WHERE DocumentId=? OR WijzigingVanDocument=?");
			s.setInt(1, doc.getId());
			s.setInt(2, doc.getId());
			s.executeUpdate();
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, doc.getId());
			s.setString(2,"Verwijderd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verwijderen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het verwijderen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public void verwijderErfgoed(Erfgoed erf)
	{
		Connection c = null;
		PreparedStatement s = null, s2 = null;
		ResultSet rs = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			
			//erfgoed obsolete maken
			s = c.prepareStatement("UPDATE Erfgoed SET Obsolete = 1 WHERE ErfgoedId=?");
			s.setInt(1, erf.getId());
			s.executeUpdate();
			
			//documenten obsolete maken
			s = c.prepareStatement("UPDATE Document SET Obsolete = 1 WHERE ErfgoedId=?");
			s.setInt(1, erf.getId());
			s.executeUpdate();
			
			//erfgoed in logboek
			s = c.prepareStatement("INSERT INTO Logboek (ErfgoedId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, erf.getId());
			s.setString(2,"Verwijderd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
			
			//documenten in logboek
			s = c.prepareStatement("SELECT DocumentId FROM Document WHERE ErfgoedId=?");
			s.setInt(1, erf.getId());
			rs = s.executeQuery();
			
			while (rs.next())
			{
				s2 = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, BeheerderId) VALUES (?,?,?)");
				s2.setInt(1, rs.getInt("DocumentId"));
				s2.setString(2,"Verwijderd");
				s2.setInt(3, m.getBeheerder().getId());
				s2.executeUpdate();				
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verwijderen van een erfgoed!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de erfgoed! (bij het verwijderen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	
	public int updateDocument(DocumentCMS doc)
	{
		Connection c = null;
		PreparedStatement s = null;
		Statement s2 = null;
		ResultSet rs = null;
		int id = -1;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			
			s = c.prepareStatement("UPDATE DOCUMENT SET WijzigingStatus='Gewijzigd' WHERE WijzigingVanDocument=? AND WijzigingStatus='Actief'");
			s.setInt(1, doc.getId());
			s.executeUpdate();
			
			if (doc.getTypeDocument().equals("Afbeelding"))	
			{
				Blob afbBlob = c.createBlob();
				OutputStream afbStream = afbBlob.setBinaryStream(1);
				ImageIO.write(doc.getImage(), "jpg", afbStream);
				
				s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
				s.setBlob(1,afbBlob);
				s.executeUpdate();
				
				s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			else if (doc.getTypeDocument().equals("Video"))
			{
				FileInputStream data = new FileInputStream(doc.getTemp());
		        
		        s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
		        s.setObject(1, data);
		        s.executeUpdate();
		        
		        s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			
			s = c.prepareStatement("INSERT INTO Document(DocumentTitel, StatusDocument,DatumToegevoegd,Obsolete,Opmerkingen,Tekst,TypeDocument,ExtensieDocument, RedenAfwijzing, DatumLaatsteWijziging, WijzigingStatus, ErfgoedId, MediaId, WijzigingVanDocument, BurgerId, BeheerderId, Aard) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			s.setString(1, doc.getTitel());
			s.setString(2, "Goedgekeurd");		//een wijziging houdt in dat de beheerder vindt dat het document in zijn gewijzigde vorm zichtbaar mag zijn op de site
			s.setTimestamp(3, doc.getDatumToegevoegd());
			s.setBoolean(4,doc.isVerwijderd());
			s.setString(5, doc.getOpmerkingen());
			s.setString(6, doc.getTekst());
			s.setString(7, doc.getTypeDocument());
			s.setString(8, doc.getExtensieDocument());
			s.setString(9, doc.getRedenAfwijzing());
			s.setTimestamp(10, doc.getDatumGewijzigd());
			s.setString(11, "Actief");
			s.setInt(12,doc.getErfgoedId());
			s.setInt(14, doc.getId());
			if (doc.getBurgerId()!=0)
			{
				s.setInt(15,doc.getBurgerId());
				s.setNull(16, Types.INTEGER);
			}
			else
			{
				s.setNull(15, Types.INTEGER);
				s.setInt(16,doc.getBeheerderId());
			}
			if (doc.getTypeDocument().equals("Tekst") || doc.getTypeDocument().equals("Link") )
				s.setNull(13, Types.INTEGER);
			else 
				s.setInt(13, doc.getMediaId());
			s.setString(17,doc.getAard());
			s.executeUpdate();
			
			s = c.prepareStatement("UPDATE DOCUMENT SET WijzigingStatus='Gewijzigd' WHERE DocumentId=?");
			s.setInt(1, doc.getId());
			s.executeUpdate();
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT DocumentId FROM Document ORDER BY DocumentId DESC"));
			rs.next();
			id = rs.getInt("DocumentId");
			
			//doe dit niet, en je krijgt 2 documenten in model (de oude versie blijft)
			doc.setId(id);
						
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, id);
			s.setString(2,"Gewijzigd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException sql)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het wijzigen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			sql.printStackTrace();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het wijzigen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (s2!=null)
					s2.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het wijzigen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	public void updateErfgoed(Erfgoed e)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
						
			s = c.prepareStatement("UPDATE Erfgoed SET Naam = ?, Postcode = ?, Deelgemeente = ?, Straat = ?, Huisnr = ?, Omschrijving = ?, TypeErfgoed = ?, Kenmerken = ?, Geschiedenis = ?, NuttigeInfo = ?, Link = ? WHERE ErfgoedId = ?");
			
			s.setString(1, e.getNaam());
			s.setString(2, e.getPostcode());
			s.setString(3, e.getDeelgemeente());
			s.setString(4, e.getStraat());
			s.setString(5, e.getHuisnr());
			s.setString(6, e.getOmschrijving());
			s.setString(7, e.getTypeErfgoed());
			s.setString(8, e.getKenmerken());
			s.setString(9, e.getGeschiedenis());
			s.setString(10,e.getNuttigeInfo());
			s.setString(11, e.getLink());
			s.setInt(12, e.getId());
			s.executeUpdate();
			
			s = c.prepareStatement("INSERT INTO Logboek (ErfgoedId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, e.getId());
			s.setString(2,"Gewijzigd");
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException sql)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het wijzigen van erfgoed!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			sql.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException ex)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het wijzigen van erfgoed, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}
	
	public void beoordeelDocument(DocumentCMS doc, boolean goedgekeurd)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE DOCUMENT SET StatusDocument = ?, RedenAfwijzing = ? WHERE DocumentId = ?");
			
			if (goedgekeurd)
			{
				s.setString(1, "Goedgekeurd");
				s.setString(2,"");
			}
			else
			{
				s.setString(1, "Afgekeurd");
				s.setString(2,doc.getRedenAfwijzing());
			}
			s.setInt(3, doc.getId());
			s.executeUpdate();
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, BeheerderId) VALUES (?,?,?)");
			s.setInt(1, doc.getId());
			
			if (goedgekeurd)
				s.setString(2,"Goedgekeurd");
			else
				s.setString(2,"Afgekeurd");
			
			s.setInt(3, m.getBeheerder().getId());
			s.executeUpdate();
		}
		catch (SQLException sql)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het wijzigen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			sql.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het beoordelen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getBlobImage(int docId)
	{
		Connection c = null;
		PreparedStatement s = null;
		BufferedImage image = null;
		ResultSet rs = null;

		try
		{
			c = DriverManager.getConnection(connectie);
			
			s = c.prepareStatement("SELECT BLOB FROM MEDIA m, DOCUMENT d WHERE d.MediaId= m.MediaId AND d.DocumentId = ?");
			s.setInt(1, docId);
			
			try
			{
				rs = s.executeQuery();
				
				if(rs.next())
				{
					Blob imageBlob = rs.getBlob("BLOB");		
					InputStream imageBlobStream = imageBlob.getBinaryStream();
					image = ImageIO.read(imageBlobStream);
				}
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het opvragen van een afbeelding!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het opvragen van een afbeelding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return image;
	}
	
	public String getBlobFile(int docId)
	{
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		String pad = "";

		try
		{
			c = DriverManager.getConnection(connectie);
			
			s = c.prepareStatement("SELECT BLOB, ExtensieDocument FROM MEDIA m, DOCUMENT d WHERE d.MediaId= m.MediaId AND d.DocumentId = ?");
			s.setInt(1, docId);
			
			try
			{
				rs = s.executeQuery();
				if(rs.next())
				{
					Blob b = rs.getBlob("BLOB");
				      
					String ext = "." + rs.getString("ExtensieDocument").trim();
					File temp = File.createTempFile("erfgoedbank_", ext);
					temp.deleteOnExit();
					pad = temp.getAbsolutePath();
					
				    FileOutputStream os = new FileOutputStream(temp);
				    os.write(b.getBytes(1, (int) b.length()));
				    os.flush();
				    os.close();
				}
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het opvragen van een bestand!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het opvragen van een bestand)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return pad;
	}
	
	public int getIdLaatsteRijLogboek()		//voor het synchronisatiesysteem
	{
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		int i = -1;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT TOP 1 LogId FROM LOGBOEK WHERE (BeheerderId IS NOT NULL AND BeheerderId <> ?) OR BurgerId IS NOT NULL ORDER BY LogId DESC;");
			s.setInt(1, m.getBeheerder().getId());
			
			rs = s.executeQuery();
		
			if (rs.next())
				i = rs.getInt(1);
		}
		catch (SQLException e)
		{
			System.err.println("Fout by synchroniseren!");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het controleren van het logboek, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return i;
	}
	
	public String synchroniseerModel(int logId)	//synchroniseert het model. Houdt rekenening met de
	{											//aanpassingen in het logboek vanaf het aangegeven id.
		int aantalWijzigingen = 0;
		int aantalVerwijderd = 0;
		int aantalToegevoegd = 0;
		
		Connection c = null;
		PreparedStatement s = null, s2 = null;
		ResultSet rs = null, rs2 = null;
		
		try
		{
				c = DriverManager.getConnection(connectie);
				
				//controle
				s = c.prepareStatement("SELECT LogId, Actie FROM LOGBOEK WHERE LogId > ? AND (BeheerderId IS NULL OR BeheerderId <> ?);");
				
				s.setInt(1,logId);
				s.setInt(2, m.getBeheerder().getId());
				
				rs = s.executeQuery();
				
				while (rs.next())
				{
					s2 = c.prepareStatement("SELECT DocumentId, ErfgoedId FROM LOGBOEK WHERE LogId = ?;");
					s2.setInt(1, rs.getInt("LogId"));
					rs2 = s2.executeQuery();
					rs2.next();
										
					int id = rs2.getInt("DocumentId");
					
					if (id != 0)	//het is een document
					{
						s2 = c.prepareStatement("SELECT * FROM DOCUMENT WHERE DocumentId = ?;");
						s2.setInt(1, id);
						rs2 = s2.executeQuery();
						rs2.next();
						
						String actie = rs.getString("Actie");
						if (actie.equals("Toegevoegd"))
						{
							m.getDocumenten().add(new DocumentCMS(rs2.getInt("DocumentId"),rs2.getString("DocumentTitel").trim(), rs2.getString("StatusDocument").trim(), rs2.getTimestamp("DatumToegevoegd"), rs2.getBoolean("Obsolete"), rs2.getString("Opmerkingen"), rs2.getString("Tekst"), rs2.getString("TypeDocument").trim(),  rs2.getString("ExtensieDocument").trim(), rs2.getInt("ErfgoedId"), rs2.getString("RedenAfwijzing"), rs2.getTimestamp("DatumLaatsteWijziging"), rs2.getInt("MediaId"), rs2.getInt("BurgerId"), rs2.getInt("BeheerderId"),rs2.getString("Aard"),m));
							aantalToegevoegd++;
						}
						else if (actie.equals("Verwijderd"))
						{
							m.verwijderDocument(id);
							aantalVerwijderd++;
						}
						else if (actie.equals("Gewijzigd"))
						{
							m.bewerkDocument(new DocumentCMS(rs2.getInt("DocumentId"),rs2.getString("DocumentTitel").trim(), rs2.getString("StatusDocument").trim(), rs2.getTimestamp("DatumToegevoegd"), rs2.getBoolean("Obsolete"), rs2.getString("Opmerkingen"), rs2.getString("Tekst"), rs2.getString("TypeDocument").trim(),  rs2.getString("ExtensieDocument").trim(), rs2.getInt("ErfgoedId"), rs2.getString("RedenAfwijzing"), rs2.getTimestamp("DatumLaatsteWijziging"), rs2.getInt("MediaId"), rs2.getInt("BurgerId"), rs2.getInt("BeheerderId"),rs2.getString("Aard"),m),rs2.getInt("WijzigingVanDocument"));
							
							aantalWijzigingen++;
						}
						else if (actie.equals("Goedgekeurd"))
						{
							for (DocumentCMS d : m.getDocumenten())
							{
								if (d.getId() == id)
								{
									d.setStatus("Goedgekeurd");
								}
							}
							aantalWijzigingen++;
						}
						else if (actie.equals("Afgekeurd"))
						{
							for (DocumentCMS d : m.getDocumenten())
							{
								if (d.getId() == id)
								{
									d.setStatus("Afgekeurd");
									d.setRedenAfwijzing(rs2.getString("RedenAfwijzing"));
								}
							}
							aantalWijzigingen++;
						}
							
					}
					else	//het is een erfgoed
					{
						id = rs2.getInt("ErfgoedId");
						
						s2 = c.prepareStatement("SELECT * FROM ERFGOED WHERE ErfgoedId = ?;");
						s2.setInt(1, id);
						rs2 = s2.executeQuery();
						rs2.next();
						
						String actie = rs.getString("Actie");
						if (actie.equals("Toegevoegd"))
						{
							m.getErfgoed().add(new Erfgoed(rs2.getInt("ErfgoedId"),rs2.getString("Naam"), rs2.getString("Postcode"), rs2.getString("Deelgemeente"), rs2.getString("Straat"),
									rs2.getString("Huisnr"), rs2.getString("Omschrijving"), rs2.getString("TypeErfgoed"), rs2.getString("Kenmerken"), rs2.getString("Geschiedenis"),
									rs2.getString("NuttigeInfo"), rs2.getString("Link"), rs2.getTimestamp("DatumToegevoegd"), rs2.getBoolean("Obsolete"), rs2.getInt("BurgerId"), rs2.getInt("BeheerderId"), m));
							aantalToegevoegd++;
						}
						else if (actie.equals("Verwijderd"))
						{
							m.verwijderErfgoed(id);
							aantalVerwijderd++;
						}
						else if (actie.equals("Gewijzigd"))
						{
							m.bewerkErfgoed(new Erfgoed(rs2.getInt("ErfgoedId"),rs2.getString("Naam"), rs2.getString("Postcode"), rs2.getString("Deelgemeente"), rs2.getString("Straat"),
									rs2.getString("Huisnr"), rs2.getString("Omschrijving"), rs2.getString("TypeErfgoed"), rs2.getString("Kenmerken"), rs2.getString("Geschiedenis"),
									rs2.getString("NuttigeInfo"), rs2.getString("Link"), rs2.getTimestamp("DatumToegevoegd"), rs2.getBoolean("Obsolete"), rs2.getInt("BurgerId"), rs2.getInt("BeheerderId"),m));
							aantalWijzigingen++;
						}
					}
				}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het synchroniseren)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)
					rs.close();
				if (rs2!=null)
					rs2.close();
				if (s!=null)
					s.close();
				if (s2!=null)
					s2.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het synchroniseren, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		return "Er zijn " + aantalWijzigingen + " documenten/fiches gewijzigd, " + aantalVerwijderd + " documenten/fiches verwijderd en " + aantalToegevoegd + " documenten/fiches toegevoegd.";
	}
	
	//___ Methoden voor klasse Administrator
	
	public void getBeheerdersEnBurgersUitDatabank()			// Deze methode wordt ENKEL door Administrator gebruikt! Enkel de beheerders worden ingeladen, niet de volledige databank moet voor deze klasse ingeladen worden
	{
		ArrayList<Beheerder> beheerders = new ArrayList<Beheerder>();
		ArrayList<Burger> burgers = new ArrayList<Burger>();
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.createStatement();
			
			rs = s.executeQuery("SELECT * FROM Beheerder");		//WHERE IsAdministrator = 0
			
			while (rs.next())
			{				
				beheerders.add(new Beheerder(rs.getInt("BeheerderId"), rs.getString("Gebruikersnaam").trim(), rs.getString("Achternaam"),
						rs.getString("Wachtwoord"),rs.getString("Email"), rs.getBoolean("KanBeoordelen"), 
						rs.getBoolean("KanWijzigen"), rs.getBoolean("KanVerwijderen"), rs.getBoolean("KanToevoegen"),rs.getBoolean("IsAdministrator"), rs.getBoolean("StuurEmails"), m));
			}
			
			m.setBeheerders(beheerders);
			
			rs = s.executeQuery("SELECT * FROM Burger");
			
			while (rs.next())
			{				
				burgers.add(new Burger(rs.getInt("BurgerId"),rs.getString("Gebruikersnaam"),rs.getString("Voornaam"),rs.getString("Familienaam")
						,rs.getString("Email"),m));
			}
			m.setBurgers(burgers);
		
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	public void updateBeheerdersDatabank(Beheerder b) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{	
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE BEHEERDER SET Gebruikersnaam = ?,Achternaam = ?,Wachtwoord = ?,Email = ?,KanBeoordelen = ?, KanWijzigen = ?, KanVerwijderen = ?, " +
					"KanToevoegen = ? WHERE BeheerderId = ?");
			s.setString(1,b.getGebruikersnaam());
			s.setString(2, b.getAchternaam());
			s.setString(3,b.getWachtwoord());
			s.setString(4, b.getEmail());
			s.setBoolean(5, b.KanBeoordelen());
			s.setBoolean(6, b.KanWijzigen());
			s.setBoolean(7, b.KanVerwijderen());
			s.setBoolean(8, b.KanToevoegen());
			s.setInt(9, b.getId());
			s.executeUpdate();
					
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public int voegBeheerderToeAanDatabank(String n,String a ,String w,String em, boolean kb, boolean kw, boolean kv, boolean kt,boolean isAdmin)
	{
		Connection c = null;
		PreparedStatement s = null, s3 =null;
		Statement s2 = null;
		ResultSet rs = null;
		int id=-1;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("INSERT INTO BEHEERDER (Gebruikersnaam,Achternaam,Wachtwoord,Email,KanBeoordelen, KanWijzigen, KanVerwijderen, KanToevoegen,IsAdministrator) VALUES (?,?,?,?,?,?,?,?,?)");
			s.setString(1,n);
			s.setString(2,a);
			s.setString(3,w);
			s.setString(4,em);
			s.setBoolean(5, kb);
			s.setBoolean(6,kw);
			s.setBoolean(7,kv);
			s.setBoolean(8,kt);
			s.setBoolean(9,isAdmin);
			
			s.executeUpdate();	
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT BeheerderId FROM Beheerder ORDER BY BeheerderId DESC"));
			rs.next();
			id = rs.getInt("BeheerderId");
			
			// Default mailvoorkeuren voor nieuwe beheerder 
			voegInstellingToe("EmailOut", "smtp.gmail.com", id);
			voegInstellingToe("EmailPoort", "587", id);
			voegInstellingToe("EmailGebruikernaam", "task.forceb2@gmail.com", id);
			voegInstellingToe("EmailWachtwoord", "azertyb2", id);
			
			m.toevoegenBeheerder(new Beheerder(id,n,a, w,em, kb, kw, kv, kt,isAdmin, true, m));
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het toevoegen van een beheerder)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (rs!=null)	
					rs.close();
				if (s!=null)
					s.close();
				if (s2!=null)
					s2.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het toevoegen van een beheerder, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		
		//standaardinstellingen
		if (id!=-1)
		{
			voegInstellingToe("StandaardReden", "Kwalitieit van document is te laag", id);
			voegInstellingToe("TypeContent", "Erfgoed", id);
			voegInstellingToe("View", "TegelView", id);
		}
		
		return id;
		
	}
	
	public void deleteBeheerder(int id)
	{
		Connection c = null;
		PreparedStatement s = null , s2 = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			
			// instellingen van een beheerder verwijderen
			s = c.prepareStatement("DELETE FROM INSTELLINGEN WHERE BeheerderId = ?");
			s.setInt(1, id);
			s.executeUpdate();
			
			// de beheerder verwijderen
			s2 = c.prepareStatement("DELETE FROM BEHEERDER WHERE BeheerderId = ?");
			s2.setInt(1, id);
			
			s2.executeUpdate();
			getBeheerdersEnBurgersUitDatabank();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try{
				if(s !=null)
					s.close();
				if(s2 != null)
					s2.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het verwijderen van een beheerder, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public boolean isAdministrator(String gebruikersnaam, String wachtwoord) 
	{
		
		Administrator.setGebruiker(gebruikersnaam);
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		boolean isAdmin = false;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT COUNT (*) FROM Beheerder WHERE Gebruikersnaam=? AND Wachtwoord=? AND IsAdministrator = 1");
			s.setString(1, gebruikersnaam);
			s.setString(2, MD5.convert(wachtwoord));
			rs = s.executeQuery();
			
			rs.next();
			if (rs.getInt(1) == 1)
				isAdmin = true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return isAdmin;
	}
	
	//____________instellingen
	
	public void getInstellingen()
	{
		ArrayList<Instellingen> instel = new ArrayList<Instellingen>();
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.createStatement();
			
			rs = s.executeQuery("SELECT * FROM Instellingen");
			
			while (rs.next())
			{				
				instel.add(new Instellingen(rs.getInt("InstellingId"),rs.getString("InstellingSleutel"),rs.getString("InstellingWaarde"),rs.getInt("BeheerderId")));
			}
			
			m.setInstellingen(instel);
		
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void updateInstellingen(String iw, int id)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE INSTELLINGEN SET InstellingWaarde = ? WHERE InstellingId = ?");
			s.setString(1,iw);
			s.setInt(2, id);
			s.executeUpdate();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public void voegInstellingToe(String is, String iw, int id)		//Instellingen i
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("INSERT INTO INSTELLINGEN (InstellingSleutel,InstellingWaarde,BeheerderId) VALUES (?,?,?)");
			s.setString(1, is);
			s.setString(2,iw);
			s.setInt(3, id);
			s.executeUpdate();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void verwijderStandaardReden(Instellingen i )
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("DELETE FROM INSTELLINGEN WHERE InstellingSleutel=? AND InstellingWaarde = ? AND BeheerderId = ?");
			s.setString(1,i.getInstellingenSleutel());
			s.setString(2, i.getInstellingenWaarde());
			s.setInt(3, i.getBeheerderId());
			s.executeUpdate();
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setStuurEmails(Beheerder b)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE BEHEERDER SET StuurEmails =? WHERE BeheerderId = ?");
			s.setBoolean(1,b.isStuurEmails());
			s.setInt(2, b.getId());
			s.executeUpdate();
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (s!=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public boolean controleerLogin(String gebruikersnaam, String wachtwoord) 
	{
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		boolean correct = false;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT COUNT (*) FROM Beheerder WHERE Gebruikersnaam=? AND Wachtwoord=?");
			s.setString(1, gebruikersnaam);
			s.setString(2, MD5.convert(wachtwoord));
			rs = s.executeQuery();
			
			rs.next();
			if (rs.getInt(1) == 1)		//als er exact 1 persoon is met de combinatie gebruikersnaam-passwoord dan mag hij inloggen
				correct =true;			//dit getal is 0 als de combinatie niet klopt. Het zou nooit meer dan 1 mogen zijn, als dit 
										//toch het geval is dan is er een dubbele gebruikersnaam (met hetzelfde wachtwoord), wat niet kan.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			
			
		}
		return correct;
	}
}
