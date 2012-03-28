package controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import model.Actie;
import model.Beheerder;
import model.Burger;
import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

public class Databank
{	
	private Model m;
	private final String online = "jdbc:sqlserver://Projecten2.mssql.somee.com;database=Projecten2;user=JDBC;password=TaskForceB2";
	private final String offline = "jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc";
	private final String connectie = offline; 
	
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
				documenten.add(new DocumentCMS(rs.getInt("DocumentId"),rs.getString("DocumentTitel").trim(), rs.getString("StatusDocument").trim(),rs.getTimestamp("DatumToegevoegd"),rs.getBoolean("Obsolete"), rs.getString("Opmerkingen"), rs.getString("Tekst"), rs.getString("TypeDocument").trim(),rs.getInt("ErfgoedId"),rs.getString("RedenAfwijzing"), rs.getTimestamp("DatumLaatsteWijziging"), rs.getInt("MediaId"),m));
			}
			
			//burgers laden
			rs = s.executeQuery("SELECT * FROM Burger");
			
			while (rs.next())
			{
				burgers.add(new Burger(rs.getInt("BurgerId"),rs.getString("Gebruikersnaam"),rs.getString("Voornaam"),rs.getString("Familienaam"), rs.getString("Email"),m));
			}
			
			//erfgoed laden
			rs = s.executeQuery("SELECT * FROM Erfgoed");
			
			while (rs.next())
			{
				erfgoed.add(new Erfgoed(rs.getInt("ErfgoedId"),rs.getString("Naam"), rs.getString("Postcode"), rs.getString("Deelgemeente"), rs.getString("Straat"),
						rs.getString("Huisnr"), rs.getString("Omschrijving"), rs.getString("TypeErfgoed"), rs.getString("Kenmerken"), rs.getString("Geschiedenis"),
						rs.getString("NuttigeInfo"), rs.getString("Link"), rs.getInt("BurgerId"), m));
			}
			
			
			//laatste wijziging inladen
			for (DocumentCMS doc : documenten)
			{
				s2 = c.prepareStatement("SELECT TOP 1 * FROM Document WHERE WijzigingStatus='Nog niet beoordeeld' AND WijzigingVanDocument=? ORDER BY DatumLaatsteWijziging DESC");
				s2.setInt(1, doc.getId());
				rs = s2.executeQuery();
				if (rs.next())
				{
					doc.setLaatsteWijziging(new DocumentCMS(rs.getInt("DocumentId"),rs.getString("DocumentTitel").trim(), rs.getString("StatusDocument").trim(),rs.getTimestamp("DatumToegevoegd"),rs.getBoolean("Obsolete"), rs.getString("Opmerkingen"), rs.getString("Tekst"), rs.getString("TypeDocument").trim(),rs.getInt("ErfgoedId"),rs.getString("RedenAfwijzing"), rs.getTimestamp("DatumLaatsteWijziging"), rs.getInt("MediaId"),m));
				}
			}
			
			//beheerders (en admins) laden
			rs = s.executeQuery("SELECT * FROM Beheerder");
			
			while (rs.next())
			{				
				beheerders.add(new Beheerder(rs.getInt("BeheerderId"), rs.getString("Gebruikersnaam").trim(), rs.getBoolean("KanBeoordelen"), rs.getBoolean("KanWijzigen"), rs.getBoolean("KanVerwijderen"), rs.getBoolean("KanToevoegen"), m));
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
				ImageIO.write(doc.getImage(), "jpg", afbStream);
				
				s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
				s.setBlob(1,afbBlob);
				s.executeUpdate();
				
				s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			
			s = c.prepareStatement("INSERT INTO Document(DocumentTitel, StatusDocument,DatumToegevoegd,Obsolete,Opmerkingen,Tekst,TypeDocument,RedenAfwijzing, DatumLaatsteWijziging, WijzigingStatus, ErfgoedId, MediaId) VALUES (?,?,?,?,?,?,?,?,?,'Actief', ?,?)");
			
			s.setString(1, doc.getTitel());
			s.setString(2, doc.getStatus());
			s.setTimestamp(3, doc.getDatumToegevoegd());
			s.setBoolean(4,doc.isVerwijderd());
			s.setString(5, doc.getOpmerkingen());
			s.setString(6, doc.getTekst());
			s.setString(7, doc.getTypeDocument());
			s.setString(8, doc.getRedenAfwijzing());
			s.setTimestamp(9, doc.getDatumGewijzigd());
			s.setInt(10,doc.getErfgoedId());
			if (doc.getTypeDocument().equals("Tekst"))
					s.setNull(11, Types.INTEGER);
			else 
				s.setInt(11, doc.getMediaId());
			s.executeUpdate();
			
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT DocumentId FROM Document ORDER BY DocumentId DESC"));
			rs.next();
			id = rs.getInt("DocumentId");
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, id);
			s.setString(2,"Toegevoegd");
			s.setString(3, m.getBeheerder().getNaam());
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
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, doc.getId());
			s.setString(2,"Verwijderd");
			s.setString(3, m.getBeheerder().getNaam());
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
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE Erfgoed SET Obsolete = 1 WHERE ErfgoedId=?");
			s.setInt(1, erf.getId());
			s.executeUpdate();
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, erf.getId());
			s.setString(2,"Verwijderd");
			s.setString(3, m.getBeheerder().getNaam());
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
	
	
	public void verwijderenOngedaanMaken(DocumentCMS doc)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE Document SET Obsolete = 0 WHERE DocumentId=? OR WijzigingVanDocument=?");
			s.setInt(1, doc.getId());
			s.setInt(2, doc.getId());
			s.executeUpdate();
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, doc.getId());
			s.setString(2,"Toegevoegd");
			s.setString(3, m.getBeheerder().getNaam());
			s.executeUpdate();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het ongedaan maken van het verwijderen van een document!", "Databank fout!",JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het ongedaan maken van het verwijderen van een document, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public void updateDocument(DocumentCMS doc)
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
				ImageIO.write(doc.getImage(), "jpg", afbStream);
				
				s = c.prepareStatement("INSERT INTO Media(BLOB) VALUES (?)");
				s.setBlob(1,afbBlob);
				s.executeUpdate();
				
				s2 = c.createStatement();
				rs = s2.executeQuery(("SELECT MediaId FROM MEDIA ORDER BY MediaId DESC"));
				rs.next();
				doc.setMediaId(rs.getInt("MediaId"));
			}
			
			s = c.prepareStatement("INSERT INTO Document(DocumentTitel, StatusDocument,DatumToegevoegd,Obsolete,Opmerkingen,Tekst,TypeDocument,RedenAfwijzing, DatumLaatsteWijziging, WijzigingStatus, ErfgoedId, MediaId, WijzigingVanDocument) VALUES (?,?,?,?,?,?,?,?,?,'Nog niet beoordeeld', ?,?,?)");
			
			s.setString(1, doc.getTitel());
			s.setString(2, doc.getStatus());
			s.setTimestamp(3, doc.getDatumToegevoegd());
			s.setBoolean(4,doc.isVerwijderd());
			s.setString(5, doc.getOpmerkingen());
			s.setString(6, doc.getTekst());
			s.setString(7, doc.getTypeDocument());
			s.setString(8, doc.getRedenAfwijzing());
			s.setTimestamp(9, doc.getDatumGewijzigd());
			s.setInt(10,doc.getErfgoedId());
			s.setInt(11, doc.getMediaId());
			s.setInt(12, doc.getId());
			if (doc.getTypeDocument().equals("Tekst"))
					s.setNull(11, Types.INTEGER);
			else 
				s.setInt(11, doc.getMediaId());
			s.executeUpdate();
			
			
			s2 = c.createStatement();
			rs = s2.executeQuery(("SELECT DocumentId FROM Document ORDER BY DocumentId DESC"));
			rs.next();
			id = rs.getInt("DocumentId");
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, id);
			s.setString(2,"Gewijzigd");
			s.setString(3, m.getBeheerder().getNaam());
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
			
			s = c.prepareStatement("INSERT INTO Logboek (DocumentId, Actie, Gebruikersnaam, GebruikerRol) VALUES (?,?,?,'Beheerder')");
			s.setInt(1, doc.getId());
			
			if (goedgekeurd)
				s.setString(2,"Goedgekeurd");
			else
				s.setString(2,"Afgekeurd");
			
			s.setString(3, m.getBeheerder().getNaam());
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
	
	public BufferedImage getBlob(int docId)
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
				else
					System.out.println("Afbeelding niet gevonden");
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
	

	public Timestamp getDatabankTijd()	//omdat de systeemtijd verschillend kan zijn van de databanktijd,
	{									//gebruiken we steeds de databanktijd
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		Timestamp t = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT CURRENT_TIMESTAMP;");			
			rs = s.executeQuery();
		
			rs.next();
			t = rs.getTimestamp(1);
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het ophalen van de tijd)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het ophalen van de tijd, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return t;
	}
	public Timestamp getTijdLaatsteRijLogboek()		//voor het synchronisatiesysteem
	{
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		Timestamp t = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT TOP 1 DatumTijd FROM LOGBOEK WHERE Gebruikersnaam <> ? AND GebruikerRol = 'Beheerder' ORDER BY DatumTijd DESC;");
			s.setString(1, m.getBeheerder().getNaam());
			
			rs = s.executeQuery();
		
			if (rs.next())
				t = rs.getTimestamp(1);

		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het controleren van het logboek)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
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
		return t;
	}
	
	public ArrayList<Actie> getActies()
	{
		ArrayList<Actie> acties = new ArrayList<Actie>();
		
		Connection c = null;
		PreparedStatement s = null, s2 = null;
		ResultSet rs = null, rs2 = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT * FROM LOGBOEK WHERE Gebruikersnaam = ? AND GebruikerRol = 'Beheerder' ORDER BY DatumTijd DESC;");
			s.setString(1, m.getBeheerder().getNaam());
			
			rs = s.executeQuery();
		
			while (rs.next())
			{
				int documentId = rs.getInt("DocumentId");
				s2 = c.prepareStatement("SELECT * FROM DOCUMENT WHERE DocumentId = ?;");
				s2.setInt(1, documentId);
				rs2 = s2.executeQuery();
				DocumentCMS doc = null;
				if (rs2.next())
				{
					doc = new DocumentCMS(documentId,rs2.getString("DocumentTitel").trim(), rs2.getString("StatusDocument").trim(),rs2.getTimestamp("DatumToegevoegd"),rs2.getBoolean("Obsolete"), rs2.getString("Opmerkingen"), rs2.getString("Tekst"), rs2.getString("TypeDocument").trim(),rs2.getInt("ErfgoedId"),rs2.getString("RedenAfwijzing"), rs2.getTimestamp("DatumLaatsteWijziging"), rs2.getInt("MediaId"),m);
				}
				
				acties.add(new Actie(doc,rs.getString("Actie"),rs.getTimestamp("DatumTijd")));
			}
				

		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het ophalen van het logboek)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het ophalen van het logboek, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		
		return acties;
	}
	
	public String synchroniseerModel(Timestamp tijd)	//synchroniseert het model. Houdt rekenening met de
	{													//aanpassingen in het logboek vanaf de meegegeven tijd.
		int aantalWijzigingen = 0;
		int aantalVerwijderd = 0;
		int aantalToegevoegd = 0;
		
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try
		{
			
			//query uitvoeren
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT logboek.Actie, document.*, erfgoed.* FROM LOGBOEK logboek, DOCUMENT document, ERFGOED erfgoed WHERE logboek.DocumentId = document.DocumentId AND erfgoed.ErfgoedId = document.ErfgoedId AND DatumTijd> ? AND Gebruikersnaam <> ? AND GebruikerRol = 'Beheerder';");
			
			s.setTimestamp(1,tijd);
			s.setString(2, m.getBeheerder().getNaam());
			
			rs = s.executeQuery();
			
			while (rs.next())
			{
				String actie = rs.getString("Actie").trim();
				if (actie.equals("Gewijzigd") || actie.equals("Goedgekeurd") || actie.equals("Afgekeurd"))
				{
					aantalWijzigingen++;
					
					DocumentCMS doc = new DocumentCMS(rs.getInt("DocumentId"),rs.getString("DocumentTitel"),rs.getString("StatusDocument").trim(),rs.getTimestamp("DatumToegevoegd"),rs.getBoolean("Obsolete"), rs.getString("Opmerkingen"), rs.getString("Tekst"), rs.getString("TypeDocument").trim(),rs.getInt("ErfgoedId"),rs.getString("RedenAfwijzing"), rs.getTimestamp("DatumLaatsteWijziging"), rs.getInt("MediaId"),m);
					doc.setImage(null);	//hierdoor wordt de afbeelding opnieuw ingeladen (moest ze upgedate zijn...)
					m.bewerkDocument(doc); 
					m.bewerkErfgoed(new Erfgoed(rs.getInt("ErfgoedId"), rs.getString("Naam"), rs.getString("Postcode"), rs.getString("Deelgemeente"), rs.getString("Straat"),
									rs.getString("Huisnr"), rs.getString("Omschrijving"), rs.getString("TypeErfgoed"), rs.getString("Kenmerken"), rs.getString("Geschiedenis"),
									rs.getString("NuttigeInfo"), rs.getString("Link"), rs.getInt("BurgerId"),m));
				}
				else if (actie.equals("Verwijderd"))
				{
					aantalVerwijderd++;
					m.verwijderDocument(rs.getInt("DocumentId"));
				}
				
				else if (actie.equals("Toegevoegd"))
				{
					aantalToegevoegd++;
					m.toevoegenDocument(new DocumentCMS(rs.getInt("DocumentId"),rs.getString("DocumentTitel"),rs.getString("StatusDocument").trim(),rs.getTimestamp("DatumToegevoegd"),rs.getBoolean("Obsolete"),rs.getString("Opmerkingen"),rs.getString("Tekst"),rs.getString("TypeDocument").trim(),rs.getInt("ErfgoedId"),rs.getString("RedenAfwijzing"),rs.getTimestamp("DatumLaatsteWijziging"),rs.getInt("MediaId"),m));
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
				if (s !=null)
					s.close();
				if (c!=null)
					c.close();
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Fout bij het verbinden met de databank! (bij het synchroniseren, het sluiten van de verbinding)", "Databank fout!",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		return "Er zijn " + aantalWijzigingen + " documenten gewijzigd, " + aantalVerwijderd + " documenten verwijderd en " + aantalToegevoegd + " documenten toegevoegd.";
	}
}