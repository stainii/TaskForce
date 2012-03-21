package guiElementen;

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
import views.DocumentView;
import views.Hoofd;
import controllers.Databank;


@SuppressWarnings("serial")
public class Rij extends JPanel implements MouseListener
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/lijst.png"));
	private Image backgroundTegel = backgroundIcon.getImage();
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private DocumentCMS document;
	
	/**Dit is een rij in de lijstview, vergelijkbaar met een Tegel in de tegelview.
	 * Toont info over een document en is aanklikbaar **/
	
	@Override
	protected void paintComponent(Graphics g)	//achtergrond tekenen
	{
		super.paintComponent(g);
		if (backgroundTegel != null)
			g.drawImage(backgroundTegel, 0, 0, getWidth(), getHeight(), this);
	}
	
	public Rij(Model m, Databank d, DocumentCMS doc, Hoofd h)
	{
		this.model = m;
		this.data = d;
		this.document = doc;
		this.hoofd = h;
		
		setOpaque(false);
		setBorder(new EmptyBorder(0,2,0,2) );
		setPreferredSize(new Dimension(780,50));
		
		addMouseListener(this);
		
		setLayout(new GridLayout(1,7));
		
		//afbeelding		
		if (document.getType().equals("Afbeelding"))
		{
			add(new Afbeelding(document,50,50,data));
		}
		else if (document.getType().equals("Video"))
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/video_lijst.png")));
			add(icoon);
		}
		else  if (document.getType().equals("Tekst"))
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/tekst_lijst.png")));
			add(icoon);
		}
		else
		{
			JLabel icoon = new JLabel();
			icoon.setHorizontalAlignment(JLabel.CENTER);
			icoon.setIcon(new ImageIcon(getClass().getResource("imgs/onbekendType_lijst.png")));
			add(icoon);
		}
		
		//tekst
		add(new JLabelFactory().getTegelTitel(document.getErfgoed().getNaam()));
		
		add(new JLabelFactory().getTegelTekst(document.getEigenaar().getNaam()));
		
		add(new JLabelFactory().getTegelTekst(document.getErfgoed().getPlaats()));
		
		add(new JLabelFactory().getTegelTekst(document.getDatum().toString()));
		
		if (document.getStatus().equals("Goedgekeurd"))
			add(new JLabelFactory().getGoedgekeurd(document.getStatus()));
		else if (document.getStatus().equals("Afgekeurd"))
			add(new JLabelFactory().getAfgekeurd(document.getStatus()));
		else
			add(new JLabelFactory().getNogNietBeoordeeld(document.getStatus()));
		
		JLabel verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		verwijderen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				model.verwijderDocument(document);
				data.verwijderDocument(document);
				repaint();
			}
		});
		
		add(verwijderen);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		hoofd.setContentPaneel(new DocumentView(model,data,document,hoofd));
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}