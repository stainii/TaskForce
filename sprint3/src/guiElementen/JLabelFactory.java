package guiElementen;

/** Om overal gelijkaardige fonts te gebruiken, en om 1 bepaald type font in 1 klap te kunnen veranderen
 *   gebruiken we een "JLabel-fabriek". Deze maakt voor ons de font voor de gewone tekst, voor de titel, ...
 */
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JLabelFactory
{
	public JLabel getNormaleTekst(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.PLAIN, 12));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public JLabel getLadenTekst(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.CENTER_BASELINE, 13));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public JLabel getTegelTekst(String s)
	{
		if (s.length()>20)
			s = s.substring(0,17) + "...";
		
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 13));
		label.setForeground(Color.WHITE);
		return label;
	}
	public JLabel getTegelTekst40(String s)
	{	
		if (s.length()>40)
			s = s.substring(0,37) + "...";
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 13));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	
	public JLabel getTegelTitel(String s)
	{
		if (s.length()>18)
			s = s.substring(0,15) + "...";
		
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 15));
		label.setForeground(new Color(50,50,50));
		return label;
	}
	
	
	public JLabel getPaginaNummer(int s)
	{
		JLabel label = new JLabel(s + "");
		label.setFont(new Font("Comic Sans", Font.CENTER_BASELINE, 16));
		label.setForeground(Color.GRAY);
		return label;
	}
	public JLabel getPaginaNummerBlank()
	{
		JLabel label = new JLabel("...");
		label.setFont(new Font("Comic Sans", Font.CENTER_BASELINE, 16));
		label.setForeground(Color.GRAY);
		return label;
	}
	
	public JLabel getGoedgekeurd(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 13));
		label.setForeground(new Color(0,200,0));
		label.setIcon(new ImageIcon(getClass().getResource("../views/imgs/goedgekeurdIco.png")));
		return label;
	}
	public JLabel getAfgekeurd(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 13));
		label.setForeground(new Color(230,0,0));
		label.setIcon(new ImageIcon(getClass().getResource("../views/imgs/afgekeurdIco.png")));
		return label;
	}
	public JLabel getNogNietBeoordeeld(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 13));
		label.setForeground(Color.WHITE);
		label.setIcon(new ImageIcon(getClass().getResource("../views/imgs/nogNietBeoordeeldIco.png")));
		return label;
	}
	
	public JLabel getTitel(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 20));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public JLabel getBeheerderLogin(String s)
	{
		JLabel label = new JLabel("Welkom " + s);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 20));
		label.setForeground(Color.WHITE);
		return label;
	}
	public JLabel getUitloggenTekst(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 12));
		label.setForeground(Color.YELLOW);
		return label;
	}
	
	public JLabel getMenuTitel(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.BOLD, 15));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public JLabel getItalic(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 12));
		label.setForeground(Color.WHITE);
		return label;
	}
	
	public JLabel getWijziging(String s)
	{
		JLabel label = new JLabel(s);
		label.setFont(new Font("Comic Sans", Font.ITALIC, 12));
		label.setForeground(Color.RED);
		return label;
	}
}
