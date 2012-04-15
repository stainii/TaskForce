package guiElementen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MooiTextArea extends JTextArea
{
	private String defaultTekst;
	private Font font;
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (getText().equals("") && isEditable())
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(font);
			g2.setColor(new Color(180,180,180));
			g2.drawString(defaultTekst, 2, font.getSize());
		}
		else
		{
			setBackground(Color.WHITE);
		}
	}
	
	public MooiTextArea(String s, String defaultTekst, Font font)
	{
		super(s);
		this.defaultTekst = defaultTekst;
		this.font = font;
	}
	public MooiTextArea(String s, String defaultTekst)
	{
		super(s);
		this.defaultTekst = defaultTekst;
		this.font = getFont();
	}
	
	public void waarschuwing()
	{
		setBackground(new Color(155,0,0));
	}
}