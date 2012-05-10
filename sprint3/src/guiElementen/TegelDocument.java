package guiElementen;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Databank;

import views.DocumentView;
import views.Hoofd;

import model.DocumentCMS;
import model.Model;

/**Dit is tegel in de tegelview.
 * Toont info over een document en is aanklikbaar **/

@SuppressWarnings("serial")
public class TegelDocument extends JPanel implements MouseListener
{
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/backgroundTile.png"));
	private Image background = backgroundIcon.getImage();
	
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private DocumentCMS document;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	

	@Override
	protected void paintComponent(Graphics g)		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public TegelDocument(Model m, Databank d, DocumentCMS doc, Hoofd h)
	{
		this.model = m;
		this.data = d;
		this.document = doc;
		this.hoofd = h;
		
		setOpaque(false);
		setBorder(new EmptyBorder(10,10,10,10) );
		setPreferredSize(new Dimension(300,152));
		
		addMouseListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 7;		
		if (document.getTypeDocument().equals("Afbeelding"))
		{
			add(new Afbeelding(document,100,100,data), c);
		}
		else if (document.getTypeDocument().equals("Video"))
		{
			JLabel icoon = new JLabel();
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/video.png")));
			add(icoon,c);
		}
		else if (document.getTypeDocument().equals("Tekst"))
		{
			JLabel icoon = new JLabel();
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/tekst.png")));
			add(icoon,c);
		}
		else if (document.getTypeDocument().equals("Link"))
		{
			JLabel icoon = new JLabel();
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/link.png")));
			add(icoon,c);
		}
		else
		{
			JLabel icoon = new JLabel();
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/onbekendType.png")));
			add(icoon,c);
		}
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		add(new JLabelFactory().getTegelTitel(doc.getTitel()),c);
		c.gridy = 2;
		add(new JLabelFactory().getTegelTekst("Voor " + doc.getErfgoed().getNaam()),c);
		//c.gridy = 3;
		//add(new JLabelFactory().getTegelTekst(doc.getBurger()!=null?doc.getBurger().getNaam():doc.getBeheerder().getNaam()),c);
		c.gridy = 4;
		add(new JLabelFactory().getTegelTekst(doc.getErfgoed().getDeelgemeente()),c);
		//c.gridy = 5;
		//add(new JLabelFactory().getTegelTekst(doc.getDatumToegevoegd().toString().substring(0, 10)), c);
		
		c.gridy = 6;
		if (doc.getStatus().equals("Goedgekeurd"))
			add(new JLabelFactory().getGoedgekeurd(doc.getStatus()),c);
		else if (doc.getStatus().equals("Afgekeurd"))
			add(new JLabelFactory().getAfgekeurd(doc.getStatus()),c);
		else
			add(new JLabelFactory().getNogNietBeoordeeld(doc.getStatus()),c);
				
		c.gridy = 7;
		
		final JLabel verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		verwijderen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				backgroundIcon = new ImageIcon(getClass().getResource("imgs/backgroundTile_hover.png"));
				background = backgroundIcon.getImage();
				repaint();	
				verwijderen.setCursor(hand);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				backgroundIcon = new ImageIcon(getClass().getResource("imgs/backgroundTile.png"));
				background = backgroundIcon.getImage();
				repaint();	
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				model.verwijderDocument(document);
				data.verwijderDocument(document);
				repaint();
			}
		});
		if(m.getBeheerder().KanVerwijderen() == false) // mag beheerder verwijderen?
			verwijderen.setVisible(false);
		
		add(verwijderen,c);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		hoofd.setContentPaneel(new DocumentView(model,data,document,hoofd));		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		backgroundIcon = new ImageIcon(getClass().getResource("imgs/backgroundTile_hover.png"));
		background = backgroundIcon.getImage();
		repaint();	
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		backgroundIcon = new ImageIcon(getClass().getResource("imgs/backgroundTile.png"));
		background = backgroundIcon.getImage();
		repaint();	
	}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
