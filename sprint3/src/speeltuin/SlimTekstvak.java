package speeltuin;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**Een tekstvak dat auto-aanvult, zoals in Google **/

@SuppressWarnings("serial")
public class SlimTekstvak extends JTextField
{
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(200,200,200));
		g2.setComposite(makeComposite(0.5f));
		g2.drawString("Een test", 2, 15);
	}
	private AlphaComposite makeComposite(float alpha) {
		  int type = AlphaComposite.SRC_OVER;
		  return(AlphaComposite.getInstance(type, alpha));
		 }
	
	public SlimTekstvak(String s)
	{
		super(s);
	}

	public static void main(String args[])
	{
		JFrame f = new JFrame();
		f.add(new SlimTekstvak(""));
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
