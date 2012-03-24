package guiElementen;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.DocumentController;

/** Deze klasse wordt gebruikt om de tekst van een document met type Tekst in DocumentView weer te geven **/

@SuppressWarnings("serial")
public class DocumentTekst extends JPanel implements DocumentMedia
{
	private JTextArea tekst;
	private DocumentController controller;
	
	public DocumentTekst(DocumentController c, boolean wijziging)
	{
		this.controller = c;
		setOpaque(false);
		
		tekst = new JTextArea(20,30);
		if (!wijziging)
			tekst.setText(c.getOrigineelDocument().getTekst());
		else
			tekst.setText(c.getOrigineelDocument().getLaatsteWijziging().getTekst());
		tekst.setEditable(false);
		tekst.setBorder(null);
		tekst.setOpaque(false);
		tekst.setForeground(Color.WHITE);
		tekst.setLineWrap(true);
		
		JScrollPane scroll = new JScrollPane(tekst);
		scroll.getViewport().setOpaque(false);
		scroll.getViewport().setBorder(null);
		scroll.setOpaque(false);
		scroll.setBorder(null);
		
		add(scroll);
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
			tekst.setBorder(null);
			tekst.setOpaque(false);
			tekst.setForeground(Color.WHITE);
		}
	}

	@Override
	public void quit() {}
}
