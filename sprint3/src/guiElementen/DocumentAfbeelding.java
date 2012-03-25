package guiElementen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controllers.Databank;
import controllers.DocumentController;

/** Deze klasse wordt gebruikt om een afbeelding van een document  in DocumentView weer te geven
 *  Als je op de afbeelding klikt krijg je hem fullscreen te zien.
 *  Je kan ook de afbeelding wijzigen en hem als lokale kopie opslaan.**/

@SuppressWarnings("serial")
public class DocumentAfbeelding extends JPanel implements DocumentMedia
{
	private DocumentController controller;
	private Databank databank;
	private boolean wijziging;
	private Afbeelding afb;
	private JLabel kiesAndereAfbeelding; 
	
	public DocumentAfbeelding(DocumentController con, Databank d, boolean w)
	{
		this.controller = con;
		this.databank = d;
		this.wijziging = w;
		
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth= 2;
		c.weighty=0;
		c.fill = GridBagConstraints.BOTH;
		
		if (!wijziging)
		{
			afb = new Afbeelding(controller.getVoorlopigDocument(),350,300,d);
			afb.addMouseListener(new MouseListener()
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
					new FullScreenAfbeelding(controller.getVoorlopigDocument(),databank);				
				}
			});
		}
		else
		{
			afb = new Afbeelding(controller.getOrigineelDocument().getLaatsteWijziging(),350,300,d);
			afb.addMouseListener(new MouseListener()
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
					new FullScreenAfbeelding(controller.getOrigineelDocument().getLaatsteWijziging(),databank);				
				}
			});
		}
		add(afb, c);
		
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth= 1;
		c.weighty = 2;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		kiesAndereAfbeelding = new JLabelFactory().getNormaleTekst("Kies een andere afbeelding   ");
		kiesAndereAfbeelding.setVisible(false);
		kiesAndereAfbeelding.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{	
				
				BufferedImage image;
				try
				{
					//chooser openen
					JFileChooser chooser = new JFileChooser();
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					{
						//afbeelding lezen en in voorlopig document steken
						image = ImageIO.read(chooser.getSelectedFile());
						controller.getVoorlopigDocument().setImage(image);
						
						//gui veranderen: de oude afbeelding wegdoen en de nieuwe afbeelding tonen
						setVisible(false);
						remove(afb);
						afb = new Afbeelding(controller.getVoorlopigDocument(),350,300,databank);
						
						GridBagConstraints c = new GridBagConstraints();
						c.gridx = 1;
						c.gridy = 1;
						c.gridwidth= 2;
						c.weighty=0;
						c.fill = GridBagConstraints.BOTH;
						add(afb, c);
						
						setVisible(true);
					}
				} catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null, "Het geselecteerde bestand is geen geldige afbeelding!", "Bestand corrupt of geen afbeelding", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		add(kiesAndereAfbeelding, c);
		
		c.gridx = 2;
		JLabel kopieOpslaan = new JLabelFactory().getNormaleTekst("   Kopie opslaan");
		kopieOpslaan.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
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
					JFileChooser chooser = new JFileChooser();
					if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
					{
						if (!wijziging)
							ImageIO.write(controller.getVoorlopigDocument().getImage(),"jpg", chooser.getSelectedFile());
						else
							ImageIO.write(controller.getOrigineelDocument().getLaatsteWijziging().getImage(),"jpg", chooser.getSelectedFile());
					}
				}
				catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null,"Er is een fout gebeurd bij het opslaan.", "Fout bij het opslaan", JFileChooser.ERROR_OPTION);
				}
				
			}
		});
		add(kopieOpslaan, c);
		
	}

	public void setEditable(boolean b)
	{
		kiesAndereAfbeelding.setVisible(b);
	}

	@Override
	public void quit() {}
}