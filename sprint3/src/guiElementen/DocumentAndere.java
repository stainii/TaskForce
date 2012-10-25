package guiElementen;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import controllers.Databank;
import model.DocumentCMS;

/** Deze klasse wordt gebruikt om een video in DocumentView weer te geven*/

@SuppressWarnings("serial")
public class DocumentAndere extends JPanel implements DocumentMedia
{
	private JLabel laden;
	private DocumentCMS doc;
	private Databank databank;
	
	public DocumentAndere(DocumentCMS doc, Databank databank)
	{
		this.doc = doc;
		this.databank = databank;
		setOpaque(false);
		
		laden = new JLabelFactory().getLadenTekst("Laden...");
		add(laden);		
		
		new BestandLaden().execute();
	}

	@Override
	public void setEditable(boolean b)
	{
		
	}
	
	@Override
	public void quit()
	{
		
	}
	
	@SuppressWarnings("rawtypes")
	class BestandLaden extends SwingWorker
	{
		@Override
		protected Object doInBackground() throws Exception
		{	
			//hier heb ik temp verwijderd?
			
			laden.setText("Klik hier om het bestand te openen (" + doc.getExtensieDocument() + ")");
			laden.addMouseListener(new MouseListener()
			{
				
				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
				@Override
				public void mousePressed(MouseEvent arg0) {}
				
				@Override
				public void mouseExited(MouseEvent arg0) {}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					try
					{
						Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ doc.getPad());
					}
					catch (IOException ioe)
					{
						JOptionPane.showMessageDialog(null, "Kan het bestand niet openen.", "Fout bij het openen van het bestand", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			return null;
		}		
	}
}