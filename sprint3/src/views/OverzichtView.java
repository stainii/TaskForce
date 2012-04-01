package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import controllers.Databank;
import controllers.OverzichtDocumentenController;
import controllers.OverzichtErfgoedController;

import model.Model;

/** De view waar een overzicht van documenten wordt gegeven. Deze klasse bevat de linkerkant (de content) en
 * het menu rechts. */

@SuppressWarnings("serial")
public class OverzichtView extends JPanel
{
	private OverzichtContent content;
	private OverzichtMenu menu;
	
	public OverzichtView(Model m, Databank d, Hoofd h)
	{
		setOpaque(false);		
		setLayout(new BorderLayout());
		
		OverzichtDocumentenController c1 = new OverzichtDocumentenController(m,d);
		OverzichtErfgoedController c2 = new OverzichtErfgoedController(m,d);
		content = new OverzichtContent(m, d, h, c1, c2);
		menu = new OverzichtMenu(m, d, c1, c2, content, h);
		
		add(content, BorderLayout.CENTER);
		add(menu,BorderLayout.EAST);
	}
	public void refresh()	//vernieuwt alle tegels/rijen zodat de doorgevoerde wijzigingen zichtbaar worden
	{
		content.stateChanged(new ChangeEvent(this));		
	}
}
