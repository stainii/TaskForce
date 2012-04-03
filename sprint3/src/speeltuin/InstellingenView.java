package speeltuin;

import guiElementen.JLabelFactory;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controllers.Databank;

import model.Model;

@SuppressWarnings("serial")
public class InstellingenView extends JPanel
{
	private JLabelFactory jLabelFactory;
	
	public InstellingenView(Model m)
	{
		jLabelFactory = new JLabelFactory();
		
		setLayout(new GridBagLayout());
		setBackground(new Color(200,0,0));
		GridBagConstraints c = new GridBagConstraints();
		
		//titel
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridx = 1;
		c.gridwidth = 6;
		add(jLabelFactory.getTitel("Instellingen voor " /*+ m.getBeheerder().getNaam()*/),c);
		
		
		//overzicht
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(10,0,0,0);
		c.gridwidth = 1;
		add(jLabelFactory.getMenuTitel("Overzicht"),c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(0,0,0,0);
		add(jLabelFactory.getNormaleTekst("Bij het opstarten wil ik "),c);
		
		//eerste groep radiobuttons
		ImageIcon selected = new ImageIcon(getClass().getResource("../views/imgs/radiobutton_selected.png"));
		ImageIcon notSelected = new ImageIcon(getClass().getResource("../views/imgs/radiobutton_normal.png"));
		ImageIcon hover = new ImageIcon(getClass().getResource("../views/imgs/radiobutton_hover.png"));
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridheight = 2;
		JRadioButton erfgoed = new JRadioButton("erfgoed");
		erfgoed.setForeground(Color.white);
		erfgoed.setOpaque(false);
		erfgoed.setSelectedIcon(selected);		
		erfgoed.setIcon(notSelected);
		erfgoed.setRolloverIcon(hover);
		erfgoed.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				
			}
		});
		add(erfgoed, c);
		
		c.gridx = 1;
		c.gridy = 6;
		c.gridheight = 2;
		JRadioButton documenten = new JRadioButton("documenten");
		documenten.setForeground(Color.white);
		documenten.setOpaque(false);
		documenten.setSelectedIcon(selected);		
		documenten.setIcon(notSelected);
		documenten.setRolloverIcon(hover);
		documenten.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				
			}
		});
		add(documenten, c);
		
		ButtonGroup group1 = new ButtonGroup();
		group1.add(erfgoed);
		group1.add(documenten);
		
		
		//tussentekst
		c.gridx = 2;
		c.gridy = 5;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		add(jLabelFactory.getNormaleTekst("in een"),c);		
		
		
		//tweede groep radiobuttons
		c.gridx = 3;
		c.gridy = 4;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		JRadioButton tegel = new JRadioButton("lijst van tegels");
		tegel.setForeground(Color.white);
		tegel.setOpaque(false);
		tegel.setSelectedIcon(selected);		
		tegel.setIcon(notSelected);
		tegel.setRolloverIcon(hover);
		tegel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				
			}
		});
		add(tegel, c);
				
		c.gridx = 3;
		c.gridy = 6;
		c.gridheight = 2;
		JRadioButton tabel = new JRadioButton("tabel");
		tabel.setForeground(Color.white);
		tabel.setOpaque(false);
		tabel.setSelectedIcon(selected);		
		tabel.setIcon(notSelected);
		tabel.setRolloverIcon(hover);
		tabel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				
			}
		});
		add(tabel, c);
				
		ButtonGroup group2 = new ButtonGroup();
		group2.add(tegel);
		group2.add(tabel);
		
		
		//tussentekst
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 4;
		c.gridy = 5;
		c.gridheight = 2;
		add(jLabelFactory.getNormaleTekst("zien."),c);
		
		
		
		
		//documenten
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 1;
		c.gridy = 8;
		c.insets = new Insets(10,0,0,0);
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jLabelFactory.getMenuTitel("Document"),c);
		
		//afwijzing
		c.gridx = 1;
		c.gridy = 9;
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 3;
		add(jLabelFactory.getNormaleTekst("Standaard redenen voor afwijzing"), c);
		
		c.gridx = 1;
		c.gridy = 10;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.insets = new Insets(0,10,0,10);
		ArrayList<String> redenen = new ArrayList<String>();
		redenen.add("test1");
		redenen.add("test2");
		redenen.add("test3");
		JList standaardRedenen = new JList(redenen.toArray());
		add(standaardRedenen,c);
		
		c.gridx = 2;
		c.gridy = 10;
		c.gridheight = 1;
		JTextField nieuweRedenTxt = new JTextField("Typ hier een nieuwe reden...");
		add(nieuweRedenTxt,c);
		
		c.gridx = 3;
		c.gridy = 10;
		JButton nieuweRedenBtn = new JButton("Toevoegen");
		add(nieuweRedenBtn,c);
		
		c.gridx = 2;
		c.gridy = 11;
		JButton verwijderenBtn = new JButton("Verwijder geselecteerde reden");
		add(verwijderenBtn, c);
	}
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		
		Model m = new Model();
		Databank d = new Databank(m);
		d.laadDatabank();
		f.add(new InstellingenView(m));
		
		f.setSize(1000,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
