package guiElementen;
/**
 * Deze klasse is een JPanel waar een afbeelding in geladen wordt.
 * De afbeelding wordt in een aparte thread (AfbeeldingWorker) uit de databank gehaald.
 * Daarna wordt ze geschaald, zodat ze mooi in de layout past.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import model.DocumentCMS;

import controllers.Databank;

@SuppressWarnings("serial")
public class Afbeelding extends JPanel
{
	private JLabel label;
	private ImageIcon afb;
	private Databank db;
	private int destWidth, destHeight;
	
	public Afbeelding(DocumentCMS doc , int destWidth, int destHeight, Databank data)
	{
		this.destHeight = destHeight;
		this.destWidth = destWidth;
		this.db=data;
		
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.CENTER);
		setLayout(f);
		
		setMinimumSize(new Dimension(destWidth,destHeight));
		setMaximumSize(new Dimension(destWidth,destHeight));
		setOpaque(false);
		
		label = new JLabelFactory().getLadenTekst("Laden...");
				
		add(label);
		
		try
		{
			new AfbeeldingWorker(label, doc, destWidth, destHeight).execute();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}	
	
	
	private class AfbeeldingWorker extends SwingWorker<Void, Void>
	{
		private BufferedImage image;
		private JLabel label;
		private DocumentCMS doc;
		
		public AfbeeldingWorker(JLabel label, DocumentCMS document, int destWidth, int destHeight) throws IOException
		{
			this.label = label;
			this.doc = document;
		}

		@Override
		protected Void doInBackground() throws IOException
		{
			//als er nog geen afbeelding voor het document is opgeslagen, haal het uit de databank en sla het op
			if (doc.getImage() == null)
			{
				image = db.getBlobImage(doc.getId());
				doc.setImage(image);
			}
			
			afb = new ImageIcon(doc.getImage());
			double schaal;	//schaal berekenen
			if ( (double)(destWidth)/afb.getIconWidth()<(double)(destHeight)/afb.getIconHeight())
			{
				schaal = (double)(destWidth)/afb.getIconWidth();				
			}
			else
			{
				schaal = (double)(destHeight)/afb.getIconHeight();
			}
			
			
			label.setIcon(scale(afb.getImage(),schaal));	//schalen en adden aan label
			label.setText("");	//"laden"-tekst wegdoen
			
			return null;
		}
		
		@Override
		protected void done()
		{
			super.done();
		}
	}
	
	//geweldige methode, gevonden op http://www.java-forums.org/java-2d/14407-imageicon-size.html
	//schaalt de afbeelding volgens een % (tussen 0-1)
	private ImageIcon scale(Image src, double scale) {
        int w = (int)(scale*src.getWidth(this));
        int h = (int)(scale*src.getHeight(this));
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dst = new BufferedImage(w, h, type);
        Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, w, h, this);
        g2.dispose();
        return new ImageIcon(dst);
    }	
}