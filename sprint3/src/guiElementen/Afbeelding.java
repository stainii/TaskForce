package guiElementen;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import model.DocumentCMS;

public class Afbeelding extends JPanel
{
	private static final long serialVersionUID = -232650038723831354L;
	private File dest;
	private boolean download;

	/**This class downloads an image and shows it */
	public Afbeelding(DocumentCMS document, int width, int height)
	{
		setPreferredSize(new Dimension(width,height));
		setOpaque(false);

		try
		{
			new DownloadWorker(document,this, width, height).execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void opslaan(String pad)
	{
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try
		{
			in = new BufferedInputStream(new FileInputStream(dest));
			out = new BufferedOutputStream(new FileOutputStream(new File(pad)));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) > 0)
			{
			  out.write(buffer, 0, len);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
				out.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
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

	/*
	 * Kan een bestand downloaden in de achtergrond en tussentijdse updates weergeven.
	 * Merk op dat we hiervoor een subklasse maken van SwingWorker.
	 * De generische parameters stellen resp. het datatype van het resultaat van doInBackground() en het datatype van de tussentijdse updates voor.
	 */
	private class DownloadWorker extends SwingWorker<Long, Integer> {

	    private URL src;
	    private JPanel panel;
	    private JProgressBar progress;
		private int width;
		private int height;
	    
	    /*
	     * Maakt een nieuwe DownloadWorker die een bestand download van de opgegeven URL en opslaat in het opgegeven bestand.
	     */
	    public DownloadWorker(DocumentCMS document, JPanel panel, int width, int height) throws IOException
	    {
	    	this.panel = panel;
	        this.width = width;
	        this.height = height;
	        progress = new JProgressBar(0,100);
	        panel.setVisible(false);
	        panel.add(progress);
	        
	    	if (document.getTemp()==null)
	    	{
	    		download = true;
	    		this.src = new URL(document.getPad());
		        dest = File.createTempFile("tapazz", ".jpg");
		        document.setTemp(dest.getAbsolutePath());
	    	}
	    	else
	    	{
	    		download = false;
	    		dest = new File(document.getTemp());
	    	}
	    	panel.setVisible(true);
	    }
	    
	    /*
	     * Stelt de taak voor die op de achtergrond in een aparte thread moet worden uitgevoerd.
	     * De methode zal het ingelezen aantal bytes teruggeven.
	     */
	    @Override
	    protected Long doInBackground() throws Exception
	    {
	    	if (download)
	    	{
		        BufferedInputStream in = null;
		        BufferedOutputStream out = null;
		        
		        try {
		            // Open een HTTP connectie naar de bron en vraag de grootte van het bestand op.
		            HttpURLConnection conn = (HttpURLConnection)src.openConnection();
		            conn.connect();
		            long size = conn.getContentLength();
		            
		            // De grootte zal -1 zijn indien er een fout opgetreden is.
		            if (size <= 0)
		              return 0L;
		            
		            // Open een gebufferde inputstream en outputstream.
		            in = new BufferedInputStream(conn.getInputStream());
		            out = new BufferedOutputStream(new FileOutputStream(dest), 1024);
		            
		            // Lees het bestand KB per KB tot er geen bytes meer te lezen zijn.
		            // Hou bij hoeveel bytes er in totaal reeds ingelezen zijn.
		            
		            long downloaded = 0L;
		            byte[] data = new byte[1024];
		            
		            int bytesRead = in.read(data, 0, 1024);
		            while (bytesRead > 0) {
		                out.write(data, 0, bytesRead);
		                downloaded += bytesRead;
		                bytesRead = in.read(data, 0, 1024);
		                
		                // We gebruiken de methode publish() van SwingWorker om een tussentijdse update door te geven.
		                // Deze update is hier het percentage van het bestand dat reeds gedownload is.
		                // De tussentijdse update zullen verwerkt worden door de methode process() die we zelf moeten overschrijven.
		                publish((int)(downloaded * 100 / size));
		            }
		            
		            return downloaded;
		        } finally {
		            try {
		                if (in != null)
		                    in.close();
		                if (out != null)
		                    out.close();
		            } catch (IOException ex) {
		                System.err.println("Error while closing streams:");
		                ex.printStackTrace();
		            }
		        }
	    	}
	    	else
	    	{
	    		return null;
	    	}
	    }

	    @Override
	    protected void done()
	    {
	    	panel.setVisible(false);
	    	panel.remove(progress);
	    	
	    	ImageIcon icon = new ImageIcon(dest.getAbsolutePath());
	    	JLabel lbl = new JLabel();
	    	
	    	double schaal;	//schaal berekenen
			if ( (double)(width)/icon.getIconWidth()<(double)(height)/icon.getIconHeight())
			{
				schaal = (double)(width)/icon.getIconWidth();				
			}
			else
			{
				schaal = (double)(height)/icon.getIconHeight();
			}
			
			
			lbl.setIcon(scale(icon.getImage(),schaal));	//schalen en adden aan label
	    	panel.add(lbl);
	    	panel.setVisible(true);
	    }

	    @Override
	    protected void process(List<Integer> chunks)
	    {
	        // In deze methode plaatsen we de code die de tussentijdse updates moet verwerken.
	        // De methoden publish() en process() zijn niet synchroon.
	        // Het is dus mogelijk dat we een lijst van tussentijdse updates ontvangen en niet enkel de meest recente.
	        // Deze methode zal worden uitgevoerd op de event dispatching thread van Swing !
	    	if (chunks !=null)
	    	{
	    		for(int i : chunks)
	    		{
	    			progress.setValue(i);			
	    		}
	    	}
	    }   
	}

}
