package views;
import guiElementen.JLabelFactory;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Model;


/** De rode header bovenaan het programma. Bevat loguit-functie en later eventueel menu-opties**/


@SuppressWarnings("serial")
public class Header extends JPanel implements MouseListener
{
	//rode achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/header.png"));
	private Image background = backgroundIcon.getImage();
	
	//logo linksboven
	private ImageIcon logoIcon = new ImageIcon(getClass().getResource("imgs/logoHeader.png"));
	private Image logo = logoIcon.getImage();
	
	private JLabel inlogLbl, uitlogLbl;
	private JFrame frame;

	@Override
	protected void paintComponent(Graphics g) 		//headerachtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		if(logo != null)
			g.drawImage(logo, 8,0,88,110,this);
	}
	
	public Header(Model m, JFrame f)
	{
		this.frame =f;
		setOpaque(false);
		setPreferredSize(new Dimension(1000,156));	//de breedte maakt niet uit, wordt overridden door de layoutmanager
		
		inlogLbl = new JLabelFactory().getBeheerderLogin(m.getBeheerder().getVoornaam());
		uitlogLbl = new JLabelFactory().getUitloggenTekst("Uitloggen");
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
		uitlogLbl.addMouseListener(this);
		
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel p = new JPanel();
		p.setOpaque(false);
		
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(20,15,5,15);
		c.gridx = 1;
		c.gridy = 1;		
		p.add(inlogLbl, c);
		
		c.insets = new Insets(0,15,0,15);
		c.gridx = 1;
		c.gridy = 2;
		p.add(uitlogLbl,c);
		add(p);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen_hover.png")));
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		String[] args = new String[1];	//er wordt een argument meegegeven met de main-methode. Dit zorgt ervoor
		Start.main(args);				//dat de system tray niet opnieuw gemaakt wordt
		frame.dispose();
	}
}