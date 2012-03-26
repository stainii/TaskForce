package guiElementen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Databank;

import views.DocumentView;
import views.Hoofd;
import views.Start;

import model.Erfgoed;
import model.Model;

/**Dit is tegel in de tegelview.
 * Toont info over een erfgoed en is aanklikbaar **/

@SuppressWarnings("serial")
public class TegelErfgoed extends JPanel implements MouseListener
{
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("../views/imgs/folder.png"));
	private Image background = backgroundIcon.getImage();
	
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private Erfgoed erfgoed;
	

	@Override
	protected void paintComponent(Graphics g)		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public TegelErfgoed(Model m, Databank d, Erfgoed e, Hoofd h)
	{
		this.model = m;
		this.data = d;
		this.erfgoed = e;
		this.hoofd = h;
		
		setOpaque(false);
		setBorder(new EmptyBorder(10,10,10,10) );
		setPreferredSize(new Dimension(300,160));
		
		addMouseListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(12,0,0,0);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 4;
		add(new JLabelFactory().getTegelTitel(e.getNaam()),c);
		
		c.insets = new Insets(0,0,0,0);
		c.gridy = 2;
		c.gridwidth = 4;
		add(new JLabelFactory().getTegelTekst40(e.getTypeErfgoed() + " in " + e.getDeelgemeente()),c);
		
		c.gridx = 1;
		c.gridy = 3;
		add(new JLabelFactory().getTegelTekst40(e.getEigenaar().getNaam()),c);
		
		c.insets = new Insets(15,0,0,0);
		c.gridx = 1;
		c.gridy = 4;
		add(new JLabelFactory().getTegelTekst40("bevat " + e.getDocumenten().size() + " documenten"),c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.insets = new Insets(5,10,0,10);
		add(new JLabelFactory().getNogNietBeoordeeld(e.getAantalDocumentenMetStatus("Nog niet beoordeeld") + ""),c);
		
		c.gridx = 2;
		c.gridy = 5;
		add(new JLabelFactory().getGoedgekeurd(e.getAantalDocumentenMetStatus("Goedgekeurd") + ""),c);
		
		c.gridx = 3;
		c.gridy = 5;
		add(new JLabelFactory().getAfgekeurd(e.getAantalDocumentenMetStatus("Afgekeurd") + ""),c);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		hoofd.setContentPaneel(new DocumentView(model,data,erfgoed.getDocumenten().get(0),hoofd));		
	}

	@Override
	public void mouseEntered(MouseEvent arg0){}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
