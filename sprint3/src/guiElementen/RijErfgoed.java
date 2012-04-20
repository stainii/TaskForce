package guiElementen;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;
import views.DocumentView;
import views.ErfgoedView;
import views.Hoofd;
import controllers.Databank;


@SuppressWarnings("serial")
public class RijErfgoed extends JPanel implements MouseListener
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/lijst.png"));
	private Image backgroundTegel = backgroundIcon.getImage();
	private Model model;
	private Databank data;
	private Hoofd hoofd;
	private Erfgoed erfgoed;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
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
		add(new JLabelFactory().getTegelTekst(e.getBurger()!=null?e.getBurger().getNaam():e.getBeheerder().getNaam()));
		
		JPanel documenten = new JPanel(new FlowLayout());
		documenten.setBorder(new EmptyBorder(10,0,0,0));
		documenten.setOpaque(false);
		documenten.add(new JLabelFactory().getNogNietBeoordeeld(e.getAantalDocumentenMetStatus("Nog niet beoordeeld") + ""));
		documenten.add(new JLabelFactory().getGoedgekeurd(e.getAantalDocumentenMetStatus("Goedgekeurd") + ""));
		documenten.add(new JLabelFactory().getAfgekeurd(e.getAantalDocumentenMetStatus("Afgekeurd") + ""));
		add(documenten);
		
		final JLabel documentToevoegen = new JLabel();
		documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/documentToevoegen.png")));
		documentToevoegen.addMouseListener(new MouseListener() {
			
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
				documentToevoegen.setCursor(hand);
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
				hoofd.setContentPaneel(new DocumentView(model, data, new DocumentCMS(erfgoed,model,data, model.getBeheerder().getId()), hoofd));
			}
		});
		if(m.getBeheerder().KanToevoegen() == false) // mag beheerder toevoegen?
			documentToevoegen.setVisible(false);
		
		add(documentToevoegen);
		
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
				if (JOptionPane.showConfirmDialog(null, "Als u dit erfgoed verwijderd worden alle bijhorende documenten ook verwijderd. Weet u zeker dat u dit wilt doen?", "Opgelet!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					model.verwijderErfgoed(erfgoed);
					data.verwijderErfgoed(erfgoed);
				}
			}
		});
		if(m.getBeheerder().KanVerwijderen() == false) // mag beheerder verwijderen?
			verwijderen.setVisible(false);
		
		add(verwijderen);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		hoofd.setContentPaneel(new ErfgoedView(model,data,erfgoed,hoofd));
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