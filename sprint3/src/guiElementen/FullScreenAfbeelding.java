package guiElementen;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JWindow;

import model.DocumentCMS;

import controllers.Databank;

/**Toont een afbeelding fullscreen. Het is eigenlijk een JWindow (JFrame zonder titelbalk) dat de grootte
 * van het hele scherm heeft */

@SuppressWarnings("serial")
public class FullScreenAfbeelding extends JWindow implements MouseListener
{
	public FullScreenAfbeelding(DocumentCMS doc, Databank d)
	{
		//als er op een toets geduwd wordt, sluit dit venster af
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher()
		{	
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				setVisible(false);
				dispose();
				return false;
			}
		});
		
		//stel de grootte in op de schermgrootte, zodat het fullscreen lijkt
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim);
		setVisible(true);
		
		//Maak de achtergrond zwart (kan niet rechtstreeks in een JWindow, er is een tussen-panel voor nodig
		JPanel p = new JPanel();
		p.setBackground(Color.BLACK);
		add(p);
		
		p.add(new Afbeelding(doc, (int)dim.getWidth(), (int)dim.getHeight(),d));
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		setVisible(false);
		dispose();		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
