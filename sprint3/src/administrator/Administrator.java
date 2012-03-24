package administrator;

import guiElementen.JLabelFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.Login;

public class Administrator extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/ladenBackground.jpg"));
	private Image background = backgroundIcon.getImage();
		
	private final static String connectie = "jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc";
	private JFrame frame;
	private JPanel admin;	
	
	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, frame.getWidth(), frame.getHeight(), this);
	}
	
	public Administrator()
	{
		JLabel welkom = new JLabelFactory().getMenuTitel("Welkom");
		admin = new JPanel();
		admin.setLayout(new FlowLayout());
		admin.add(welkom,BorderLayout.NORTH);
		
		frame = new JFrame();
		frame.add(admin);
		frame.setResizable(true);
		frame.setSize(600,400);
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static boolean isAdministrator(String gebruikersnaam, String wachtwoord) 
	{
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
}
