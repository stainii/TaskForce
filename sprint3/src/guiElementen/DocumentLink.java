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

import controllers.DocumentController;

/** Deze klasse wordt gebruikt om de tekst van een document met type Tekst in DocumentView weer te geven **/

@SuppressWarnings("serial")
public class DocumentLink extends JPanel implements DocumentMedia
{
	private JTextField tekst;
	private DocumentController controller;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	public DocumentLink(DocumentController c)
	{
		this.controller = c;
		setOpaque(false);
		
		tekst = new MooiTextField("", "http://www.voorbeeld.be");
		tekst.setText(c.getOrigineelDocument().getTekst());
		
		tekst.setColumns(30);
		tekst.setEditable(false);
		tekst.setBorder(null);
		tekst.setOpaque(false);
		tekst.setForeground(Color.WHITE);
		
		tekst.addMouseListener(new MouseListener()
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
				if (!tekst.getText().equals("") && !tekst.isEditable())
					tekst.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					if (!tekst.getText().equals("") && !tekst.isEditable())
						Runtime.getRuntime().exec("cmd.exe /c start " + tekst.getText());
				}
				catch (IOException ioe)
				{
					JOptionPane.showMessageDialog(null, "Kan de website niet openen.", "Fout bij het openen van de website", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints con= new GridBagConstraints();
		con.anchor = GridBagConstraints.FIRST_LINE_START;
		con.weightx=1.0;
		con.weighty=1.0;
		con.fill = GridBagConstraints.NONE;
		
		con.gridy=1;
		add(tekst,con);
	}

	@Override
	public void setEditable(boolean b)
	{
		tekst.setEditable(b);
		if (b)
		{
			tekst.setBorder(new JTextArea().getBorder());
			tekst.setOpaque(true);
			tekst.setForeground(Color.BLACK);
		}
		else
		{
			controller.getVoorlopigDocument().setTekst(tekst.getText());
			controller.getVoorlopigDocument().setExtensieDocument("url");
			tekst.setBorder(null);
			tekst.setOpaque(false);
			tekst.setForeground(Color.WHITE);
		}
	}

	@Override
	public void quit() {}
}
