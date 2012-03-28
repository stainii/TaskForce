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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import views.Start;

import model.Beheerder;
import model.Burger;
import model.Erfgoed;
import model.Model;

import controllers.Databank;
import controllers.Login;


public class Administrator extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/ladenBackground.jpg"));
	private Image background = backgroundIcon.getImage();
		
	private final static String connectie = "jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc";
	private Model m;
	private Databank d;
	private JFrame frame;
	private JLabel uitlogLbl,toevoegenAdminLbl,toevoegenBeheerderLbl,opslaan;
	private JLabel opslaanNieuweBh, nieuweBh, terugBh;
	private JCheckBox wijzigen, toevoegen, verwijderen, beoordelen;
	private JCheckBox wijzigenNieuweBh, toevoegenNieuweBh, verwijderenNieuweBh, beoordelenNieuweBh;
	private JComboBox beheerderCB;
	private JTextField naamTxt, wachtwoordTxt;
	private Beheerder geselecteerdeBeheerder;
	private static String gebruiker;
	private JPanel startpanel,toevoegBeheerderPanel;
	
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
		d = new Databank(m);
				
		d.getBeheerdersUitDatabank();		// haalt beheerders uit databank en steekt ze in ArrayList<Beheerder> 
		
		startpanel = new JPanel();
		startpanel.setOpaque(false);
		
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
		beoordelen = new JCheckBox("Beoordelen");
		
		wijzigen.setForeground(Color.white);
		toevoegen.setForeground(Color.white);
		verwijderen.setForeground(Color.white);
		beoordelen.setForeground(Color.white);
		
		wijzigen.setOpaque(false);
		toevoegen.setOpaque(false);
		verwijderen.setOpaque(false);
		beoordelen.setOpaque(false);
		
		beheerderCB = new JComboBox();
		beheerderCB.addItem("<<Geen beheerder geselecteerd>>");
		beheerderCB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{		
				for(Beheerder b : m.getBeheerders())
				{
					if(beheerderCB.getSelectedItem().equals(b.getNaam()))
					{
						m.setBeheerder(beheerderCB.getSelectedItem().toString());
						geselecteerdeBeheerder = b;
						opslaan.setVisible(true);
						wijzigen.setSelected(b.KanWijzigen());
						toevoegen.setSelected(b.KanToevoegen());
						beoordelen.setSelected(b.KanBeoordelen());
						verwijderen.setSelected(b.KanVerwijderen());
					}
					else if(beheerderCB.getSelectedIndex() == 0)
					{
						wijzigen.setSelected(false);
						toevoegen.setSelected(false);
						beoordelen.setSelected(false);
						verwijderen.setSelected(false);
						opslaan.setVisible(false);
					}
				}
			}
		});
		
		opslaan = new JLabel();
		opslaan.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/opslaan.png")));
		opslaan.addMouseListener(new OpslaanListener());
		
		for(Beheerder s : m.getBeheerders())		//overloopt de ArrayList en vult de JComboBox met de namen
		{
			beheerderCB.addItem(s.getNaam());
		}
		
		//_____StartPanel_________________________________________________________**
		startpanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		
		c.gridx =1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		startpanel.add(uitlogLbl,c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		startpanel.add(toevoegenAdminLbl,c);
		
		c.gridx = 2;
		c.gridy = 2;
		startpanel.add(toevoegenBeheerderLbl,c);
		
		c.gridx = 1;
		c.gridy = 2;
		startpanel.add(new JLabelFactory().getTitel("Welkom " + getGebruiker()),c);
		
		c.gridx = 1; 
		c.gridy = 3;
		startpanel.add(new JLabelFactory().getItalic("<html><u>Kies een beheerder: </u></html>"),c);
		
		c.gridx = 1; 
		c.gridy = 4;
		startpanel.add(beheerderCB,c);
		
		c.gridx = 1;
		c.gridy = 5;
		startpanel.add(new JLabelFactory().getItalic("<html><u>Wat mag deze gebruiker: </u></html>"),c);
		
		c.gridx = 1;
		c.gridy = 6;
		startpanel.add(wijzigen,c);

		c.gridx = 1;
		c.gridy = 7;
		startpanel.add(toevoegen,c);
		
		c.gridx = 1;
		c.gridy = 8;
		startpanel.add(beoordelen,c);
		
		c.gridx = 1;
		c.gridy = 9;
		startpanel.add(verwijderen,c);
		
		c.gridx = 2;
		c.gridy = 9;
		opslaan.setVisible(false);
		startpanel.add(opslaan, c);
		
		add(startpanel);
		//_______________________________________________________**
		
		//________________ToevoegPanel__________________________**
		toevoegBeheerderPanel = new JPanel();
		toevoegBeheerderPanel.setLayout(new GridBagLayout());
		toevoegBeheerderPanel.setOpaque(false);
		
		wijzigenNieuweBh = new JCheckBox("Wijzigen");
		toevoegenNieuweBh = new JCheckBox("Toevoegen");
		verwijderenNieuweBh = new JCheckBox("Verwijderen");
		beoordelenNieuweBh = new JCheckBox("Beoordelen");
		
		wijzigenNieuweBh.setOpaque(false);
		toevoegenNieuweBh.setOpaque(false);
		verwijderenNieuweBh.setOpaque(false);
		beoordelenNieuweBh.setOpaque(false);
		
		wijzigenNieuweBh.setForeground(Color.white);
		toevoegenNieuweBh.setForeground(Color.white);
		verwijderenNieuweBh.setForeground(Color.white);
		beoordelenNieuweBh.setForeground(Color.white);
		
		naamTxt = new JTextField();
		wachtwoordTxt = new JTextField();
				
		opslaanNieuweBh = new JLabel();
		opslaanNieuweBh.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/opslaan.png")));
		opslaanNieuweBh.addMouseListener(new NieuweBeheerderListener());
 		
		nieuweBh = new JLabel("Toegevoegd");
		nieuweBh.setForeground(Color.white);
		
		terugBh =new JLabelFactory().getMenuTitel("Terug");
		terugBh.setIcon(new ImageIcon(getClass().getResource("../views/imgs/terug.png")));
		terugBh.addMouseListener(new TerugListener());
		
		c.gridx = 1;
		c.gridy = 2;
		toevoegBeheerderPanel.add(new JLabelFactory().getTitel("Beheerder toevoegen"),c);
		
		c.gridx = 1; 
		c.gridy = 3;
		toevoegBeheerderPanel.add(new JLabelFactory().getItalic("<html><u>Geef een nieuwe beheerder: </u></html>"),c);
				
		c.gridx = 1;
		c.gridy = 4;
		toevoegBeheerderPanel.add(naamTxt,c);
		
		c.gridx = 1;
		c.gridy = 5;
		toevoegBeheerderPanel.add(wachtwoordTxt,c);
		
		c.gridx = 1; 
		c.gridy = 6;
		toevoegBeheerderPanel.add(new JLabelFactory().getItalic("<html><u>Wat mag deze beheerder: </u></html>"),c);
		
		c.gridx = 1;
		c.gridy = 7;
		toevoegBeheerderPanel.add(wijzigenNieuweBh,c);
		
		c.gridx = 1;
		c.gridy = 8;
		toevoegBeheerderPanel.add(toevoegenNieuweBh,c);
		
		c.gridx = 1;
		c.gridy = 9;
		toevoegBeheerderPanel.add(beoordelenNieuweBh,c);
		
		c.gridx = 1;
		c.gridy = 10;
		toevoegBeheerderPanel.add(verwijderenNieuweBh,c);
		
		c.gridx = 1;
		c.gridy = 11;
		toevoegBeheerderPanel.add(terugBh,c);
		
		c.gridx = 2;
		c.gridy = 11;
		toevoegBeheerderPanel.add(opslaanNieuweBh,c);
		
		c.gridx = 2;
		c.gridy = 12;
		nieuweBh.setVisible(false);
		toevoegBeheerderPanel.add(nieuweBh,c);
		
		toevoegBeheerderPanel.setVisible(false);
		add(toevoegBeheerderPanel);
		//____________________***
		
		frame = new JFrame("Administratorpaneel");
		frame.add(this);
		frame.setSize(450,400);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}
	
	public String getGebruiker() {
		return gebruiker;
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
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/uitloggen_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("../views/imgs/uitloggen.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			frame.dispose();
			String[] args = new String[0];
			Start.main(args);	
		}

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
		public void mousePressed(MouseEvent e) 
		{
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	private class ToevoegenBeheerderListenener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

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

			startpanel.setVisible(false);
			toevoegBeheerderPanel.setVisible(true);		
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	private class OpslaanListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			m.getBeheerder().setKanWijzigen(wijzigen.isSelected());
			m.getBeheerder().setKanToevoegen(toevoegen.isSelected());
			m.getBeheerder().setKanVerwijderen(verwijderen.isSelected());
			m.getBeheerder().setKanBeoordelen(beoordelen.isSelected());
				
			d.updateBeheerdersDatabank(geselecteerdeBeheerder);	
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	private class NieuweBeheerderListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent e) 
		{
			if(naamTxt.getText().isEmpty() || wachtwoordTxt.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Beide velden moeten ingevuld zijn!" ,"Velden zijn leeg",JOptionPane.ERROR_MESSAGE);
			else
			{
				try {
					d.voegBeheerderToeAanDatabank(naamTxt.getText(), Login.convert(wachtwoordTxt.getText()), beoordelenNieuweBh.isSelected(), wijzigenNieuweBh.isSelected(), verwijderenNieuweBh.isSelected(), toevoegenNieuweBh.isSelected());
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				naamTxt.setText("");
				wachtwoordTxt.setText("");
				beoordelenNieuweBh.setSelected(false);
				wijzigenNieuweBh.setSelected(false);
				verwijderenNieuweBh.setSelected(false);
				toevoegenNieuweBh.setSelected(false);
				nieuweBh.setVisible(true);
				
				beheerderCB.removeAll();
				
				for(Beheerder s : m.getBeheerders())		//overloopt de ArrayList en vult de JComboBox met de namen
				{
					beheerderCB.addItem(s.getNaam());
				}
			}	
		}	
		@Override
		public void mouseExited(MouseEvent arg0) {}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	}
	
	private class TerugListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0){}
		
		@Override
		public void mousePressed(MouseEvent arg0) {}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			terugBh.setIcon(new ImageIcon(getClass().getResource("../views/imgs/terug.png")));
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) 
		{
			terugBh.setIcon(new ImageIcon(getClass().getResource("../views/imgs/terug_hover.png")));
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			toevoegBeheerderPanel.setVisible(false);
			startpanel.setVisible(true);
		}
	}
}
