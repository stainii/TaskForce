package guiElementen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import views.DocumentContent;

import controllers.Databank;
import controllers.DocumentController;

/**Zit in DocumentView. Als je een nieuw document toevoegt, moet je een type kiezen: afbeelding, video, tekst, ...
 * Deze klasse geeft je de keuze */

@SuppressWarnings("serial")
public class TypeKiezer extends JPanel implements DocumentMedia
{
	private JLabel afbeeldingToevoegen, tekstvakToevoegen, videoToevoegen;
	private DocumentController controller; 
	private DocumentContent content; 
	private JFileChooser chooser;
	private Databank databank; 
	
	public TypeKiezer(DocumentController dcontroller, DocumentContent dcontent, Databank datab)
	{
		setPreferredSize(new Dimension(300,200));
		setOpaque(false);
		this.controller = dcontroller; 
		this.content = dcontent; 
		this.databank = datab;
		chooser = new JFileChooser();
		
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.CENTER);
		
		//afbeelding toevoegen
		afbeeldingToevoegen = new JLabel();
		afbeeldingToevoegen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					try 
					{
						//lees de afbeelding in en sla ze op in het voorlopige document. Toon daarna de afbeelding.
						controller.getVoorlopigDocument().setImage(ImageIO.read(chooser.getSelectedFile()));
						controller.getVoorlopigDocument().setTypeDocument("Afbeelding");
						content.setMedia(new DocumentAfbeelding(controller,databank));
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		//tekstvak toevoegen
		tekstvakToevoegen = new JLabel();
		tekstvakToevoegen.addMouseListener(new MouseListener() {
			
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
				//zet het documentType op tekst en verander naar de gewone view. In de gewone view, maak het tekstvak bewerkbaar.
				controller.getVoorlopigDocument().setTypeDocument("Tekst");
				DocumentTekst doctekst = new DocumentTekst(controller);
				doctekst.setEditable(true);
			}
		});
		videoToevoegen = new JLabel();
		videoToevoegen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});

		afbeeldingToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/afbeeldingToevoegen.png")));
		videoToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/videoToevoegen.png")));
		tekstvakToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/tekstToevoegen.png")));

		add(afbeeldingToevoegen);
		add(videoToevoegen);
		add(tekstvakToevoegen);
	}

	@Override
	public void setEditable(boolean b)
	{}

	@Override
	public void quit(){}
}
