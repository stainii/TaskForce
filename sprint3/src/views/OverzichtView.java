package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import controllers.Databank;
import controllers.OverzichtController;

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
		
		OverzichtController c = new OverzichtController(m,d);
		content = new OverzichtContent(m, d, h, c);
		menu = new OverzichtMenu(m, d, c, content, h);
		
		add(content, BorderLayout.CENTER);
		add(menu,BorderLayout.EAST);
	}
	public void refresh()	//vernieuwt alle tegels/rijen zodat de doorgevoerde wijzigingen zichtbaar worden
	{
		content.stateChanged(new ChangeEvent(this));		
	}
}
