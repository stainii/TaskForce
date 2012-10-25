package guiElementen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import controllers.Databank;
import model.DocumentCMS;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSMovie;
import de.humatic.dsj.SwingMovieController;

/** Deze klasse wordt gebruikt om een video in DocumentView weer te geven*/

@SuppressWarnings("serial")
public class DocumentVideo extends JPanel implements DocumentMedia, PropertyChangeListener
{
	private DSMovie movie;
	private JLabel laden;
	private DocumentCMS doc;
	
	public DocumentVideo(DocumentCMS doc, Databank databank)
	{
		this.doc = doc;
		setPreferredSize(new Dimension(400,300));
		setBackground(Color.BLACK);
		
		setLayout(new BorderLayout());
		
		laden = new JLabelFactory().getLadenTekst("Laden...");
		add(laden, BorderLayout.NORTH);		
		
		new VideoLaden(this).execute();
	}

	@Override
	public void setEditable(boolean b)
	{
		
	}	

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {}

	@Override
	public void quit()
	{
		if (movie != null)
		{
			movie.stop();
			movie.dispose();
		}
	}
	
	@SuppressWarnings("rawtypes")
	class VideoLaden extends SwingWorker
	{
		private PropertyChangeListener prop;
		
		public VideoLaden(PropertyChangeListener prop)
		{
			this.prop = prop;
		}

		@Override
		protected Object doInBackground() throws Exception
		{			
			//temp hier verwijderd
			movie = new DSMovie(doc.getPad(), DSFiltergraph.DD7, prop);
			add(movie.asComponent(),BorderLayout.CENTER);
			add(new SwingMovieController(movie),BorderLayout.SOUTH);

			return null;
		}

		@Override
		protected void done()
		{
			laden.setVisible(false);
			super.done();
		}
		
		
	}
}