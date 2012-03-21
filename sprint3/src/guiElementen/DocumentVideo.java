package guiElementen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSMovie;
import de.humatic.dsj.SwingMovieController;

/** Deze klasse wordt gebruikt om een video in DocumentView weer te geven*/

@SuppressWarnings("serial")
public class DocumentVideo extends JPanel implements DocumentMedia, PropertyChangeListener
{
	private DSMovie movie;
	
	public DocumentVideo()
	{
		setPreferredSize(new Dimension(400,300));
		setBackground(Color.BLACK);
		
		JFileChooser fd = new JFileChooser();

		fd.showOpenDialog(null);

		if (fd.getSelectedFile() == null) return;

		movie = new DSMovie(fd.getSelectedFile().getAbsolutePath(), DSFiltergraph.DD7, this);

		setLayout(new BorderLayout());
		add(movie.asComponent(),BorderLayout.CENTER);
		add(new SwingMovieController(movie),BorderLayout.SOUTH);
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
}