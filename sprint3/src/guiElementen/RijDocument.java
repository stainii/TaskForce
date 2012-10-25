package guiElementen;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.DocumentCMS;
import model.Model;
import systemTray.InSystemTray;
import views.DocumentView;
import views.Hoofd;
import controllers.Databank;

/**Dit is een rij in de lijstview, vergelijkbaar met een Tegel in de tegelview.
 * Toont info over een document en is aanklikbaar **/

@SuppressWarnings("serial")
public class RijDocument extends JPanel implements MouseListener
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst.png"));
	private Image backgroundTegel = backgroundIcon.getImage();
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private DocumentCMS document;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	private InSystemTray systray;
	
	@Override
	protected void paintComponent(Graphics g)	//achtergrond tekenen
	{
		super.paintComponent(g);
		if (backgroundTegel != null)
			g.drawImage(backgroundTegel, 0, 0, getWidth(), getHeight(), this);
	}
	
	public RijDocument(Model m, Databank d, DocumentCMS doc, Hoofd h, InSystemTray tray)
	{
		this.model = m;
		this.data = d;
		this.document = doc;
		this.hoofd = h;
		this.systray = tray;
		
		setOpaque(false);
		setBorder(new EmptyBorder(0,2,0,2) );
		setPreferredSize(new Dimension(780,50));
		
		addMouseListener(this);
		
		setLayout(new GridLayout(1,8));
		
		//afbeelding		
		if (document.getTypeDocument().equals("Afbeelding"))
		{
			add(new Afbeelding(document,50,50));
		}
		else if (document.getTypeDocument().equals("Video"))
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/video_lijst.png")));
			add(icoon);
		}
		else  if (document.getTypeDocument().equals("Tekst"))
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/tekst_lijst.png")));
			add(icoon);
		}
		else  if (document.getTypeDocument().equals("Link"))
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/linkLijst.png")));
			add(icoon);
		}
		else
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/onbekendTypeLijst.png")));
			add(icoon);
		}
		
		//tekst
		add(new JLabelFactory().getTegelTitel(document.getTitel()));
		
		add(new JLabelFactory().getTegelTekst("Voor " + document.getErfgoed().getNaam()));
		
		//add(new JLabelFactory().getTegelTekst(document.getBurger()!=null?document.getBurger().getNaam():document.getBeheerder().getNaam()));
		
		add(new JLabelFactory().getTegelTekst(document.getErfgoed().getDeelgemeente()));
		
		//add(new JLabelFactory().getTegelTekst(document.getDatumToegevoegd().toString().substring(0,10)));
		
		if (document.getStatus().equals("Goedgekeurd"))
			add(new JLabelFactory().getGoedgekeurd(document.getStatus()));
		else if (document.getStatus().equals("Afgekeurd"))
			add(new JLabelFactory().getAfgekeurd(document.getStatus()));
		else
			add(new JLabelFactory().getNogNietBeoordeeld(document.getStatus()));
		
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
				backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst_hover.png"));
				backgroundTegel = backgroundIcon.getImage();
				repaint();	
				verwijderen.setCursor(hand);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst.png"));
				backgroundTegel = backgroundIcon.getImage();
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
		if(m.getBeheerder().KanVerwijderen() == false)	//mag beheerder verwijderen?
			verwijderen.setVisible(false);
		
		add(verwijderen);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		hoofd.setContentPaneel(new DocumentView(model,data,document,hoofd,systray));
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst_hover.png"));
		backgroundTegel = backgroundIcon.getImage();
		repaint();	
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst.png"));
		backgroundTegel = backgroundIcon.getImage();
		repaint();	
	}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}