package speeltuin;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GroeiendeWindow extends JFrame
{
	JPanel p = new JPanel();
	JFrame t = this;
	int x = 1;
	int y= 1;
	
	public GroeiendeWindow()
	{
		p.setBackground(Color.BLACK);
		add(p);
	}
	
	public void start() {
        new Timer().scheduleAtFixedRate(new GameLoop(),0,10);
        this.setVisible(true);
        this.setSize(x,y);
    }
	
	private class GameLoop extends TimerTask
	{
        @Override
        public void run()
        {
        	x++;
        	y++;
        	
            t.setSize(x,y);
            p.repaint();
        }    
    }
    
    public static void main(String[] args) {
        new GroeiendeWindow().start();
    }

}
