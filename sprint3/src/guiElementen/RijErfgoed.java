package guiElementen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Erfgoed;
import model.Model;
import views.DocumentView;
import views.Hoofd;
import controllers.Databank;


@SuppressWarnings("serial")
public class RijErfgoed extends JPanel implements MouseListener
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/lijst.png"));
	private Image backgroundTegel = backgroundIcon.getImage();
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private Erfgoed erfgoed;
	
	/**Dit is een rij in de lijstview, vergelijkbaar met een Tegel in de tegelview.
	 * Toont info over een document en is aanklikbaar **/
	
	@Override
	protected void paintComponent(Graphics g)	//achtergrond tekenen
	{
		super.paintComponent(g);
		if (backgroundTegel != null)
			g.drawImage(backgroundTegel, 0, 0, getWidth(), getHeight(), this);
	}
	
	public RijErfgoed(Model m, Databank d, Erfgoed e, Hoofd h)
	{
		this.model = m;
		this.data = d;
		this.erfgoed = e;
		this.hoofd = h;
		
		setOpaque(false);
		setBorder(new EmptyBorder(0,10,0,10) );
		setPreferredSize(new Dimension(780,50));
		
		addMouseListener(this);
		
		setLayout(new GridLayout(1,8));
		
		add(new JLabelFactory().getTegelTitel(e.getNaam()));
		add(new JLabelFactory().getTegelTekst(e.getTypeErfgoed()));
		add(new JLabelFactory().getTegelTekst(e.getDeelgemeente()));
		add(new JLabelFactory().getTegelTekst(e.getEigenaar().getNaam()));
		add(new JLabelFactory().getTegelTekst(e.getDocumenten().size() + " documenten"));
		
		JPanel documenten = new JPanel(new FlowLayout());
		documenten.setBorder(new EmptyBorder(10,0,0,0));
		documenten.setOpaque(false);
		documenten.add(new JLabelFactory().getNogNietBeoordeeld(e.getAantalDocumentenMetStatus("Nog niet beoordeeld") + ""));
		documenten.add(new JLabelFactory().getGoedgekeurd(e.getAantalDocumentenMetStatus("Goedgekeurd") + ""));
		documenten.add(new JLabelFactory().getAfgekeurd(e.getAantalDocumentenMetStatus("Afgekeurd") + ""));
		add(documenten);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		hoofd.setContentPaneel(new DocumentView(model,data,erfgoed.getDocumenten().get(0),hoofd));
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