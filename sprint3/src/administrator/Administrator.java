package administrator;

import guiElementen.JLabelFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import views.Start;
import model.Model;
import controllers.Databank;
import controllers.MD5;

/**
 * Deze klasse is het administratorpaneel. Deze view zal te zien zijn wanneer een administrator zich aanmeldt. 
 * Het bevat 3 panelen, het BeheerderPanel, AdminPanel en het BurgerPanel. 
 */

@SuppressWarnings("serial")
public class Administrator extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/ladenBackground.jpg"));
	private Image background = backgroundIcon.getImage();
		
	private Model m;
	private Databank d;
	private JFrame frame;
	private static String gebruiker;
	private JLabel uitlogLbl;
	private JPanel welkomPnl;
	private JTabbedPane tab;
	private BeheerderPanel beheerderPnl;
	private AdminPanel adminPnl;
	private BurgerPanel gebruikerPnl;
		
	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, frame.getWidth(), frame.getHeight(), this);
	}
	
	public Administrator(BufferedImage image)
	{
		m = new Model();
		d = new Databank(m);
				
		d.getBeheerdersEnBurgersUitDatabank();		// haalt beheerders en burgers uit databank en steekt ze in ArrayList<Beheerder> , ArrayList<Burger>
		
		welkomPnl = new JPanel();	
		welkomPnl.setSize(400,400);
		welkomPnl.setOpaque(false);
		welkomPnl.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.HORIZONTAL;

		uitlogLbl = new JLabelFactory().getUitloggenTekst("Uitloggen");
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
		uitlogLbl.addMouseListener(new UitlogListener());
		
		beheerderPnl = new BeheerderPanel(m,d);		// new BeheerderPanel
		gebruikerPnl = new BurgerPanel(m,d);
		adminPnl = new AdminPanel(m,d,getGebruiker());
		
		tab = new JTabbedPane();
		tab.addTab("Beheerders", beheerderPnl.getBeheerPanel());
		tab.addTab("Gebruikers", gebruikerPnl.getBurgerPanel());
		tab.addTab("Admins", adminPnl.getAdminPanel());
		tab.setPreferredSize(new Dimension(500,300));
		
		c.gridx = 1;
		c.gridy = 1;
		welkomPnl.add(new JLabelFactory().getTitel("Welkom administrator!" /*+ getGebruiker()*/+ "                                   "),c);
		
		c.gridx = 2;
		c.gridy = 1;
		welkomPnl.add(uitlogLbl,c);
		
		
		
		add(welkomPnl);
		add(tab,BorderLayout.CENTER);
		frame = new JFrame("Administratorpaneel");
		frame.add(this);
		frame.setSize(600,400);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(image);
	}
	
	public String getGebruiker() {
		return gebruiker;
	}
	public static void setGebruiker(String g)
	{
		gebruiker = g;
	}
	
	private class UitlogListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
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
}
