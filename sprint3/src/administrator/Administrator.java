package administrator;

import guiElementen.JLabelFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Beheerder;
import model.Burger;
import model.Erfgoed;
import model.Model;

import controllers.Login;


public class Administrator extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/ladenBackground.jpg"));
	private Image background = backgroundIcon.getImage();
		
	private final static String connectie = "jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc";
	private JFrame frame;
	private JLabel uitlogLbl,toevoegenAdminLbl,toevoegenBeheerderLbl,opslaan;
	private JCheckBox wijzigen, toevoegen, verwijderen;	
	private JComboBox beheerderCB;
	private Beheerder beheerders;
	private int geselecteerdeBeheerder;
	private Model m;
	private static String gebruiker;
	
	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, frame.getWidth(), frame.getHeight(), this);
	}
	
	public Administrator()
	{
		m = new Model();
		//Labels
		uitlogLbl = new JLabelFactory().getUitloggenTekst("Uitloggen");
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/uitloggen.png")));
		uitlogLbl.addMouseListener(new UitlogListener());
		
		toevoegenAdminLbl = new JLabelFactory().getNormaleTekst("Voeg admin toe");
		toevoegenAdminLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		toevoegenAdminLbl.addMouseListener(new ToevoegenAdminListener());
		
		toevoegenBeheerderLbl = new JLabelFactory().getNormaleTekst("Voeg beheerder toe");
		toevoegenBeheerderLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		toevoegenBeheerderLbl.addMouseListener(new ToevoegenBeheerderListenener());
		
		wijzigen = new JCheckBox("Wijzigen");
		toevoegen = new JCheckBox("Toevoegen");
		verwijderen = new JCheckBox("Verwijderen");
		
		wijzigen.setForeground(Color.white);
		toevoegen.setForeground(Color.white);
		verwijderen.setForeground(Color.white);
		
		wijzigen.setOpaque(false);
		toevoegen.setOpaque(false);
		verwijderen.setOpaque(false);
		
		beheerderCB = new JComboBox();
		beheerderCB.addItem("<<Geen beheerder geselecteerd>>");
		beheerderCB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i = 0; i<m.getBeheerderArrayList().size();i++)
				{
					if(beheerderCB.getSelectedItem().equals(m.getBeheerderArrayList().get(i).getNaam()))
					{
						geselecteerdeBeheerder = m.getBeheerderArrayList().get(i).getId();
						opslaan.setVisible(true);
						wijzigen.setSelected(m.getBeheerderArrayList().get(i).isKanWijzigen());
						toevoegen.setSelected(m.getBeheerderArrayList().get(i).isKanBeoordelen());
						verwijderen.setSelected(m.getBeheerderArrayList().get(i).isKanVerwijderen());
					}
					else if(beheerderCB.getSelectedIndex() == 0)
					{
						wijzigen.setSelected(false);
						toevoegen.setSelected(false);
						verwijderen.setSelected(false);
						opslaan.setVisible(false);
					}	
				}
				
			}
		});
		
		opslaan = new JLabelFactory().getTitel("Opslaan");
		opslaan.addMouseListener(new OpslaanListener());
		
		
		getBeheerdersUitDatabank();		// haalt beheerders uit databank en steekt ze in ArrayList<Beheerder> 
		
		
		for(Beheerder s : m.getBeheerderArrayList())		//overloopt de ArrayList en vult de JComboBox met de namen
		{
			beheerderCB.addItem(s.getNaam());
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx =1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(uitlogLbl,c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(toevoegenAdminLbl,c);
		
		c.gridx = 2;
		c.gridy = 2;
		add(toevoegenBeheerderLbl,c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(new JLabelFactory().getTitel("Welkom " + getGebruiker()),c);
		
		c.gridx = 1; 
		c.gridy = 3;
		add(new JLabelFactory().getNormaleTekst("Kies een beheerder: "),c);
		
		c.gridx = 1; 
		c.gridy = 4;
		add(beheerderCB,c);
		
		c.gridx = 1;
		c.gridy = 5;
		add(new JLabelFactory().getNormaleTekst("Wat mag deze gebruiker: "),c);
		
		c.gridx = 1;
		c.gridy = 6;
		add(wijzigen,c);

		c.gridx = 1;
		c.gridy = 7;
		add(toevoegen,c);
		
		c.gridx = 1;
		c.gridy = 8;
		add(verwijderen,c);
		
		c.gridx = 2;
		c.gridy = 8;
		opslaan.setVisible(false);
		add(opslaan, c);
		
		frame = new JFrame("Administratorpaneel");
		frame.add(this);
		frame.setSize(600,400);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}
	
	public String getGebruiker() {
		return gebruiker;
	}

	
	/**
	 * Mogen deze 2 onderstaande functies niet in Databank klasse gestoken worden? Zodat Databank methoden bij elkaar blijven staan?
	 * Of wordt het dan te onoverzichtelijk? 
	 */

	
	
	public void updateBeheerdersDatabank(int b /*, boolean kt, boolean kw, boolean kv*/)
	{
		Connection c = null;
		PreparedStatement s = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("UPDATE Beheerder SET KanBeoordelen = ?, KanWijzigen = ?, KanVerwijderen = ? WHERE BeheerderId = ?");
			s.setBoolean(1, m.getBeheerders().isKanBeoordelen());
			s.setBoolean(2, m.getBeheerders().isKanWijzigen());
			s.setBoolean(3, m.getBeheerders().isKanVerwijderen());
			s.setInt(4, b);
			s.executeUpdate();
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getBeheerdersUitDatabank()
	{
		ArrayList<Beheerder> beheerder = new ArrayList<Beheerder>();
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT * FROM Beheerder WHERE IsAdministrator = 0");
			rs = s.executeQuery();
			
			while(rs.next())
			{
				beheerder.add(new Beheerder(rs.getInt("BeheerderId"),rs.getString("Gebruikersnaam"),rs.getBoolean("KanBeoordelen"),rs.getBoolean("KanWijzigen"),rs.getBoolean("KanVerwijderen"),m));
			}
			m.setBeheerders(beheerder);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isAdministrator(String gebruikersnaam, String wachtwoord) 
	{
		gebruiker = gebruikersnaam;
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		boolean isAdmin = false;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT COUNT (*) FROM Beheerder WHERE Gebruikersnaam=? AND Wachtwoord=? AND IsAdministrator = 1");
			s.setString(1, gebruikersnaam);
			s.setString(2, Login.convert(wachtwoord));
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
	
	private class UitlogListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/uitloggen_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/uitloggen.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	private class ToevoegenAdminListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			toevoegenAdminLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			toevoegenAdminLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	private class OpslaanListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			m.getBeheerders().setKanBeoordelen(toevoegen.isSelected());
			m.getBeheerders().setKanWijzigen(wijzigen.isSelected());
			m.getBeheerders().setKanVerwijderen(verwijderen.isSelected());
				
			updateBeheerdersDatabank(geselecteerdeBeheerder);
			getBeheerdersUitDatabank();	// opnieuw alle gegevens verversen ! 
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class ToevoegenBeheerderListenener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			toevoegenBeheerderLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			toevoegenBeheerderLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
