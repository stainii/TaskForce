package guiElementen;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JComboBox defaultRedenenCbx;
	private Model m;
	
	public RedenAfwijzing(DocumentController cont,Model model)
	{		
		this.m = model;
		//deze default redenen moeten uit settings gehaald worden
				
		setLayout(new GridBagLayout());
		GridBagConstraints c =new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		JLabel l = new JLabelFactory().getNormaleTekst("Reden afwijzing: ");
		add(l, c);
		
		c.gridx = 2;
		redenTxt = new JTextField(20);
		add(redenTxt,c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(new JLabelFactory().getNormaleTekst("Of kies reden: "),c);
		
		c.gridx =2;
		c.gridy = 2;
		c.gridwidth = 1;
		defaultRedenenCbx = new JComboBox(m.getStandaardReden().toArray());
		add(defaultRedenenCbx, c);
		
		defaultRedenenCbx.setMaximumRowCount(3);
		defaultRedenenCbx.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (defaultRedenenCbx.getSelectedIndex() !=-1)
				{
					redenTxt.setText(defaultRedenenCbx.getSelectedItem().toString());
				}
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