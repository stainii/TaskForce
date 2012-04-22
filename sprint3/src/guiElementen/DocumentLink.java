package guiElementen;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controllers.DocumentController;

/** Deze klasse wordt gebruikt om de tekst van een document met type Tekst in DocumentView weer te geven **/

@SuppressWarnings("serial")
public class DocumentLink extends JPanel implements DocumentMedia
{
	private JTextField tekst;
	private JLabel wijziging;
	private DocumentController controller;
	
	public DocumentLink(DocumentController c)
	{
		this.controller = c;
		setOpaque(false);
		
		
		tekst = new MooiTextField("", "http://www.voorbeeld.be");
		tekst.setText(c.getOrigineelDocument().getTekst());
		
		wijziging = new JLabelFactory().getWijziging("");
		if(c.getOrigineelDocument().getLaatsteWijziging()!=null)
			wijziging.setText("Wijziging: " + c.getOrigineelDocument().getLaatsteWijziging().getTekst());
		
		tekst.setColumns(30);
		tekst.setEditable(false);
		tekst.setBorder(null);
		tekst.setOpaque(false);
		tekst.setForeground(Color.WHITE);
		
		setLayout(new GridBagLayout());
		GridBagConstraints con= new GridBagConstraints();
		con.anchor = GridBagConstraints.FIRST_LINE_START;
		con.weightx=1.0;
		con.weighty=1.0;
		con.fill = GridBagConstraints.NONE;
		
		con.gridy=1;
		add(tekst,con);
		con.gridy=2;
		add(wijziging,con);
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
