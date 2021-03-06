package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import systemTray.InSystemTray;

import controllers.Databank;

import model.DocumentCMS;
import model.Model;

/** De view waar de details van een document worden gegeven. Deze klasse bevat de linkerkant (de content) en
 * het menu rechts. */

@SuppressWarnings("serial")
public class DocumentView extends JPanel
{
	private DocumentContent content;
	private DocumentMenu menu;
	
	public DocumentView(Model m, Databank d, DocumentCMS doc, Hoofd h, InSystemTray tray)
	{
		setOpaque(false);		
		setLayout(new BorderLayout());
		
		content = new DocumentContent(m, d, h, doc, tray);
		menu = new DocumentMenu(m,d,doc,h, content, tray);
		
		add(content, BorderLayout.CENTER);
		add(menu,BorderLayout.EAST);
	}
	public void quit()
	{
		content.quit();
	}

}
