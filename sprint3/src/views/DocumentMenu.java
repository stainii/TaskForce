package views;

/** De grijze doorzichtige balk, aan de rechterkant van het scherm in document-view.
 *   Hier worden andere documenten van het geselecteerde erfgoed getoond en extra opties gegeven zoals
 *   een document toevoegen aan het erfgoed, het maken van een pdf, ...
 **/

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import systemTray.InSystemTray;

import controllers.Databank;

import model.DocumentCMS;
import model.Model;

@SuppressWarnings("serial")
public class DocumentMenu extends JPanel
{	
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/transparantGrijs.png"));
	private Image background = backgroundIcon.getImage();
	
	private JLabel toevoegen;
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private DocumentCMS document;
	private DocumentContent content;
	private JLabel overzicht, erfgoed;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	private InSystemTray systray;
	

	@Override
	protected void paintComponent(Graphics g)
	{		
		//achtergrond tekenen
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public DocumentMenu(Model m, Databank d, DocumentCMS doc, Hoofd h, DocumentContent cont, InSystemTray tray)
	{
		this.model = m;
		this.databank = d;
		this.hoofd = h;
		this.document = doc;
		this.content = cont;
		this.systray = tray;
		setOpaque(false);
		setPreferredSize(new Dimension(200,0));
		setBorder(new EmptyBorder(15, 10, 0, 0) );
		
		FlowLayout f =new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
				
		//overzicht button
		overzicht =new JLabelFactory().getMenuTitel("Overzicht");
		overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
		overzicht.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) 
			{
				overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug_hover.png")));
				overzicht.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				content.quit();
				hoofd.laadOverzicht();
			}
		});
		
		add(overzicht);
		
		erfgoed =new JLabelFactory().getMenuTitel("Bekijk erfgoedfiche");
		erfgoed.setIcon(new ImageIcon(getClass().getResource("imgs/erfgoedpin_zwartwit.png")));
		erfgoed.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				erfgoed.setIcon(new ImageIcon(getClass().getResource("imgs/erfgoedpin_zwartwit.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) 
			{
				erfgoed.setIcon(new ImageIcon(getClass().getResource("imgs/erfgoedpin.png")));
				erfgoed.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				content.quit();
				hoofd.setContentPaneel(new ErfgoedView(model,databank,document.getErfgoed(),hoofd,systray));
			}
		});
		add(erfgoed);
		
		//toevoegbutton
		toevoegen = new JLabelFactory().getMenuTitel("Document toevoegen");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
				toevoegen.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				content.quit();
				hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(document.getErfgoed(),model, databank, model.getBeheerder().getId()),hoofd,systray));
			}
		});
		
		if (m.getBeheerder().KanToevoegen() == false)
			toevoegen.setVisible(false);
		
		add(toevoegen);
				
		//toon andere documenten van het erfgoed
		add(new JLabelFactory().getMenuTitel("Andere documenten"));
	
		boolean gevonden = false;
		for (DocumentCMS document : doc.getErfgoed().getDocumenten())
		{
			if (!doc.equals(document))
			{
				add(new DocumentThumbnail(model, d, document, hoofd, content,tray));
				gevonden = true;
			}
		}
		
		if (!gevonden)
			add(new JLabelFactory().getNormaleTekst("<html>Er zijn geen andere documenten<br />voor dit erfgoed opgeladen</html>"));
	}
}
