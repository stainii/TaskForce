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
	
	public DocumentTekst(DocumentController c)
	{
		this.controller = c;
		setOpaque(false);
		
		tekst = new MooiTextArea("", "Tekst");
		tekst.setText(c.getOrigineelDocument().getTekst());
		tekst.setRows(20);
		tekst.setColumns(30);
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
			controller.getVoorlopigDocument().setExtensieDocument("txt");
			tekst.setBorder(null);
			tekst.setOpaque(false);
			tekst.setForeground(Color.WHITE);
		}
	}

	@Override
	public void quit() {}
}
