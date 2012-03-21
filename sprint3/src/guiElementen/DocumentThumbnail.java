package guiElementen;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.Databank;

import views.DocumentContent;
import views.DocumentView;
import views.Hoofd;

import model.DocumentCMS;
import model.Model;

/** In het menu rechts van het scherm in DocumentView worden er thumbnails van andere documenten
 *  van het erfgoed weergegeven. Deze klasse is zo één thumbnail.
 */

@SuppressWarnings("serial")
public class DocumentThumbnail extends JPanel implements MouseListener
{
	private Model model;
	private Databank databank;
	private DocumentCMS document;
	private Hoofd hoofd;
	private DocumentContent content;
	
	public DocumentThumbnail(Model m, Databank d, DocumentCMS doc, Hoofd h, DocumentContent cont)
	{
		this.model = m;
		this.databank = d;
		this.document = doc;
		this.hoofd = h;
		this.content = cont;
		
		setOpaque(false);
		setPreferredSize(new Dimension(85,85));
		
		if (doc.getType().equals("Afbeelding"))
		{
			Afbeelding afb = new Afbeelding(doc,85,85,databank);
			afb.addMouseListener(this);
			add(afb);
		}
		else if (doc.getType().equals("Video"))
		{
			JLabel video = new JLabel();
			video.setIcon(new ImageIcon(getClass().getResource("../views/imgs/video.png")));
			video.addMouseListener(this);
			add(video);
			
		}
		else
		{
			JLabel tekst = new JLabel();
			tekst.setIcon(new ImageIcon(getClass().getResource("../views/imgs/tekst.png")));
			tekst.addMouseListener(this);
			add(tekst);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0){}
	
	@Override
	public void mousePressed(MouseEvent arg0){}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		content.quit();
		hoofd.setContentPaneel(new DocumentView(model,databank,document,hoofd));
	}

}
