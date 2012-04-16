package guiElementen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (document!=null)
		{
			if (document.getStatus().equals("Afgekeurd"))
					g2.setColor(Color.RED);
			else if (document.getStatus().equals("Goedgekeurd"))
					g2.setColor(Color.GREEN);
			else
				g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, 85, 3);
		}
			
	}

	private Model model;
	private Databank databank;
	private DocumentCMS document;
	private Hoofd hoofd;
	private DocumentContent content;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	public DocumentThumbnail(Model m, Databank d, DocumentCMS doc, Hoofd h, DocumentContent cont)
	{
		this.model = m;
		this.databank = d;
		this.document = doc;
		this.hoofd = h;
		this.content = cont;
		
		setOpaque(false);
		setPreferredSize(new Dimension(85,85));
		
		if (doc.getTypeDocument().equals("Afbeelding"))
		{
			Afbeelding afb = new Afbeelding(doc,85,85,databank);
			afb.addMouseListener(this);
			add(afb);
		}
		else if (doc.getTypeDocument().equals("Video"))
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
	public void mouseEntered(MouseEvent arg0) {
		this.setCursor(hand);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (content!=null)		//is nul als je van erfgoedfiche naar hier komt
			content.quit();
		hoofd.setContentPaneel(new DocumentView(model,databank,document,hoofd));
	}

}
