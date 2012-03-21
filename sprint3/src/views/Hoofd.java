package views;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.Databank;

import model.Model;

/**Het beheerderspaneel waar de beheerder zijn ding kan doen met de documenten
 * Bevat de header en OverzichtContent, m.a.w de rode balk bovenaan en een overzicht van documenten**/

@SuppressWarnings("serial")
public class Hoofd extends JPanel
{
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/background.jpg"));
	private Image background = backgroundIcon.getImage();
	
	private JPanel contentWrapper;	//nodig om de content te kunnen wisselen (switchen van OverzichtView naar DocumentView etc.)
	private JPanel content;
	private OverzichtView ingeladenOverzicht;	// de OverzichtView wordt bijgehouden. Als je vanuit DocumentView terug naar OverzichtView gaat,
												//dan moet je op de pagina komen waar je gebleven was, met filters die je toegepast had. Daarom
												// moeten we de view bijhouden, want als je steeds een nieuwe OverzichtView zou maken bij het
												// terugkeren, dan kom je steeds op pagina 1 terecht met de standaard geselecteerde filters.

	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public Hoofd(Model m, Databank d, JFrame f)
	{		
		setLayout(new BorderLayout());
		add(new Header(m,f),BorderLayout.NORTH);
		
		contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.setOpaque(false);
		 
		add(contentWrapper, BorderLayout.CENTER);
		
		ingeladenOverzicht = new OverzichtView(m, d, this);		// de OverzichtView bijhouden
		content = ingeladenOverzicht;
		contentWrapper.add(content);
		
	}
	
	public void setContentPaneel(JPanel p)	//verandert de content naar een nieuw paneel
	{
		contentWrapper.setVisible(false);
		contentWrapper.remove(content);
		
		if (content.getClass().equals(OverzichtView.class))		//is overzichtview op dit moment ingeladen? bewaar het
		{
			ingeladenOverzicht = (OverzichtView) content;
		}
		
		content = p;	//verander de content in de meegegeven content en zet het zichtbaar
		contentWrapper.add(content);
		contentWrapper.setVisible(true);
	}
	public void laadOverzicht()		//dit verandert de huidige content naar de bijgehouden OverzichtView
	{
		contentWrapper.setVisible(false);
		contentWrapper.remove(content);
		content = ingeladenOverzicht;
		ingeladenOverzicht.refresh();		//de tegels/rijen in de view eens herladen, zodat alle doorgevoerde wijzigingen zichtbaar zijn
		contentWrapper.add(content);
		contentWrapper.setVisible(true);
	}
}
