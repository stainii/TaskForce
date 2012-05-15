package guiElementen;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;

import views.OverzichtContent;
import controllers.OverzichtDocumentenController;

/** Zorgt voor de keuze tussen tabelview en lijstview in het overzichtscherm**/

@SuppressWarnings("serial")
public class OverzichtKiezer extends JPanel
{
	private JLabel lijst, tegel;
	private OverzichtContent content;
	private OverzichtDocumentenController controller;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	public OverzichtKiezer(OverzichtContent conte, OverzichtDocumentenController contr)
	{
		this.content = conte;
		this.controller = contr;
		
		lijst = new JLabel();
		tegel = new JLabel();
		
		lijst.setIcon(new ImageIcon(getClass().getResource("imgs/tabelview.png")));
		tegel.setIcon(new ImageIcon(getClass().getResource("imgs/tegelview.png")));
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		setBorder(new EmptyBorder(10,10,10,10));
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 10, 0, 0);
		
		c.gridx=1;
		c.gridy=1;
		add(tegel,c);
		
		c.gridx = 2;
		c.gridy = 1;
		add(lijst,c);
		
		lijst.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				lijst.setIcon(new ImageIcon(getClass().getResource("imgs/tabelview.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				lijst.setIcon(new ImageIcon(getClass().getResource("imgs/tabelview_hover.png")));
				lijst.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				content.setView("LijstView");
				content.stateChanged(new ChangeEvent(this));
				bepaalNieuweOndergrens();
			}
		});
		
		tegel.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent e){}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				tegel.setIcon(new ImageIcon(getClass().getResource("imgs/tegelview.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				tegel.setIcon(new ImageIcon(getClass().getResource("imgs/tegelview_hover.png")));
				tegel.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				content.setView("TegelView");
				content.stateChanged(new ChangeEvent(this));
				bepaalNieuweOndergrens();
			}
		});
	}
	
	/**Bij het wisselen van view moet ongeveer dezelfde content getoond worden. De pagina mag dus niet plots
	 * 0 worden. Als de ondergrens (eerste tegel die op het scherm staat) kleiner is dan het aantal tegels die
	 * op het scherm kunnen dan is de pagina 0 (1). Hetzelfde geldt bij het maximum. Anders bepalen we de huidige
	 * pagina door de ondergrens te delen door het aantal documenten dat op 1 pagina kan.*/
	public void bepaalNieuweOndergrens()
	{
		int ondergrens = content.getOndergrens();
		int aantalOpScherm = content.bepaalAantalOpScherm();
		
		if (ondergrens<aantalOpScherm)
		{
			content.setPagina(0);
		}
		else if (ondergrens>controller.getInTeLaden().size() - aantalOpScherm)
		{
			content.setPagina(content.bepaalAantalPaginas());
		}
		else
		{
			content.setPagina((int) (Math.ceil(ondergrens/aantalOpScherm))+1);
		}
	}
}