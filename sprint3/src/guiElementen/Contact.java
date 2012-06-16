package guiElementen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Beheerder;
import model.Burger;

import controllers.DocumentController;

/** Deze klasse toont de naam van de eigenaar van een document
 * en als je er op klikt wordt het emailprogramma geopend,
 * zodat je hem/haar een mail kan sturen.*/

@SuppressWarnings("serial")
public class Contact extends JLabel
{
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	private Burger burger;
	private Beheerder beheerder;
	
	public Contact(Burger b)
	{
		this.burger = b;
		setText(b.getGebruikersnaam() + " (klik om te contacteren)");
		setForeground(Color.WHITE);
		setFont(new JLabelFactory().getNormaleTekst("").getFont());
		
		
		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					Runtime.getRuntime().exec("cmd.exe /c start " + "mailto:"+burger.getEmail());
				}
				catch (IOException ioe)
				{
					JOptionPane.showMessageDialog(null, "Kan het emailprogramma niet openen.", "Fout bij het openen van uw emailprogramma.\nHet emailadres van de eigenaar is " + burger.getEmail(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public Contact(Beheerder b)
	{
		this.beheerder = b;
		setText(b.getGebruikersnaam() + " (klik om te contacteren)");
		setForeground(Color.WHITE);
		setFont(new JLabelFactory().getNormaleTekst("").getFont());
		
		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					Runtime.getRuntime().exec("cmd.exe /c start " + "mailto:"+beheerder.getEmail());
				}
				catch (IOException ioe)
				{
					JOptionPane.showMessageDialog(null, "Kan het emailprogramma niet openen.", "Fout bij het openen van uw emailprogramma.\nHet emailadres van de eigenaar is " + beheerder.getEmail(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
