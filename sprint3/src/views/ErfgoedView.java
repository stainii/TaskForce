package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controllers.Databank;

import model.Erfgoed;
import model.Model;

/** De view waar de details van een eerfgoed worden gegeven. Deze klasse bevat de linkerkant (de content) en
 * het menu rechts. */

@SuppressWarnings("serial")
public class ErfgoedView extends JPanel
{
	private ErfgoedContent content;
	private ErfgoedMenu menu;
	
	public ErfgoedView(Model m, Databank d, Erfgoed e, Hoofd h)
	{
		setOpaque(false);		
		setLayout(new BorderLayout());
		
		content = new ErfgoedContent(m, d, h, e);
		menu = new ErfgoedMenu(m, d, e, h, content);
		
		add(content, BorderLayout.CENTER);
		add(menu,BorderLayout.EAST);
	}

}
