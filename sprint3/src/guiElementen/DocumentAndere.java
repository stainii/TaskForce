package guiElementen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import controllers.Databank;

import model.DocumentCMS;

import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSMovie;
import de.humatic.dsj.SwingMovieController;

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
	
	class BestandLaden extends SwingWorker
	{
		@Override
		protected Object doInBackground() throws Exception
		{			
			if (doc.getTemp()==null || doc.getTemp().equals(""))
			{
				doc.setTemp(databank.getBlobFile(doc.getId()));
			}
			
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
						Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ doc.getTemp());
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