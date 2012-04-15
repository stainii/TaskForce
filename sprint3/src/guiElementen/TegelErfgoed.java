package guiElementen;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Databank;

import views.DocumentView;
import views.ErfgoedView;
import views.Hoofd;

import model.DocumentCMS;
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
		setPreferredSize(new Dimension(300,152));
		
		addMouseListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(22,0,0,0);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 6;
		add(new JLabelFactory().getTegelTitel(e.getNaam()),c);
		
		c.insets = new Insets(0,0,0,0);
		c.gridy = 2;
		c.gridwidth = 6;
		add(new JLabelFactory().getTegelTekst40(e.getTypeErfgoed() + " in " + e.getDeelgemeente()),c);
		
		c.gridx = 1;
		c.gridy = 3;
		add(new JLabelFactory().getTegelTekst40(e.getEigenaar().getNaam()),c);
		
		c.gridx = 1;
		c.gridy = 4;
		add(new JLabelFactory().getTegelTekst40("bevat " + e.getDocumenten().size() + (e.getDocumenten().size()==1?" document":" documenten")),c);
		
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		c.insets = new Insets(0,10,0,10);
		add(new JLabelFactory().getNogNietBeoordeeld(e.getAantalDocumentenMetStatus("Nog niet beoordeeld") + ""),c);
		
		c.gridx = 3;
		c.gridy = 5;
		add(new JLabelFactory().getGoedgekeurd(e.getAantalDocumentenMetStatus("Goedgekeurd") + ""),c);
		
		c.gridx = 5;
		c.gridy = 5;
		add(new JLabelFactory().getAfgekeurd(e.getAantalDocumentenMetStatus("Afgekeurd") + ""),c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 3;
		JLabel documentToevoegen = new JLabel();
		documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/documentToevoegen.png")));
		documentToevoegen.addMouseListener(new MouseListener() {
			
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
				hoofd.setContentPaneel(new DocumentView(model, data, new DocumentCMS(erfgoed,model,data, model.getBeheerder().getId()), hoofd));
			}
		});
		if(m.getBeheerder().KanToevoegen() == false) // mag beheerder toevoegen?
			documentToevoegen.setVisible(false);
		add(documentToevoegen,c);
		
		c.gridx = 4;
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
				if (JOptionPane.showConfirmDialog(null, "Als u dit erfgoed verwijderd worden alle bijhorende documenten ook verwijderd. Weet u zeker dat u dit wilt doen?", "Opgelet!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					model.verwijderErfgoed(erfgoed);
					data.verwijderErfgoed(erfgoed);
				}
			}
		});
		if(m.getBeheerder().KanVerwijderen() == false) // mag beheerder verwijderen?
			verwijderen.setVisible(false);
		
		add(verwijderen,c);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		hoofd.setContentPaneel(new ErfgoedView(model,data,erfgoed,hoofd));
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
