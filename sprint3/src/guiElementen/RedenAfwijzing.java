package guiElementen;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Model;

import controllers.DocumentController;

/** Hoort bij Beoordeling. Als er afgewezen is wordt dit zichtbaar. Deze klasse toont de reden
 * of laat de reden bewerken. Als je de reden bewerkt krijg je een keuzelijstje te zien met standaard-
 * redenen. Die standaardredenen kunnen per beheerder gewijzigd worden in zijn settings.
 */

@SuppressWarnings("serial")
public class RedenAfwijzing extends JPanel
{
	private JTextField redenTxt;
	@SuppressWarnings("rawtypes")
	private JComboBox defaultRedenenCbx;
	private Model m;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedenAfwijzing(DocumentController cont,Model model)
	{		
		this.m = model;
		//deze default redenen moeten uit settings gehaald worden
				
		setLayout(new GridBagLayout());
		GridBagConstraints c =new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		JLabel l = new JLabelFactory().getNormaleTekst("   Reden afwijzing: ");
		add(l, c);
		
		c.gridx = 2;
		redenTxt = new JTextField(20);
		add(redenTxt,c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		defaultRedenenCbx = new JComboBox(m.getStandaardReden().toArray());
		add(defaultRedenenCbx, c);
		
		redenTxt.addKeyListener(new KeyListener()
		{	
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0){}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				defaultRedenenCbx.setSelectedIndex(0);
			}
		});
		
		defaultRedenenCbx.setMaximumRowCount(3);
		defaultRedenenCbx.addItemListener(new ItemListener()
		{	
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (((JComboBox)(e.getSource())).getSelectedIndex() !=0) 
					redenTxt.setText(((JComboBox)(e.getSource())).getSelectedItem().toString());
			}
		});
		
		setOpaque(false);
	}
	
	public String getReden()
	{
		if (redenTxt != null)
			return redenTxt.getText();
		else
			return null;
	}
	
	public void setReden(String s)
	{
		redenTxt.setText(s);
	}
	
	public void setEditable(boolean b)
	{
		if (b)
		{
			redenTxt.setEditable(true);
			defaultRedenenCbx.setVisible(true);
			redenTxt.setOpaque(true);
			redenTxt.setBorder(new JTextField().getBorder());
			redenTxt.setForeground(Color.BLACK);
		}
		else
		{
			redenTxt.setEditable(false);
			defaultRedenenCbx.setVisible(false);
			redenTxt.setOpaque(false);
			redenTxt.setBorder(null);
			redenTxt.setForeground(Color.WHITE);
		}
	}
		

}