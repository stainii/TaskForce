package guiElementen;

import harsh.p.raval.lightbox.LightBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import views.OverzichtContent;
import views.OverzichtView;

import model.Instellingen;
import model.Model;

@SuppressWarnings("serial")
public class InstelView extends JPanel 
{

	private JPanel instelPanel;
	private JLabel annuleren;
	private Model m;
	private JFrame f;
	private OverzichtView v;
	private JLabelFactory jLabelFactory;
	private LightBox box;
	
	public InstelView(Model model,JFrame frame,OverzichtView view)
	{
		this.m = model;
		this.f = frame;
		this.v = view;
		
		jLabelFactory = new JLabelFactory();
		
		setSize(new Dimension(550,300));
		setLayout(new GridBagLayout());
		setBackground(Color.GRAY);
		
		annuleren = new JLabel();
		annuleren.setIcon(new ImageIcon(getClass().getResource("imgs/annuleren.png")));
		annuleren.addMouseListener(new AnnulerenListener());
	
		GridBagConstraints c = new GridBagConstraints();

		//titel
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridx = 1;
		c.gridwidth = 6;
		add(jLabelFactory.getTitel("Instellingen voor " + m.getBeheerder().getNaam()),c);
		
		
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
		erfgoed.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setTypeContent("Erfgoed"); //Scherm laten veranderen in gekozen keuze
				
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
		documenten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				v.getContent().setTypeContent("Documenten");
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
		tegel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setView("TegelView");
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
		
		for(Instellingen i : m.getInstellingen())
		{
			if(i.getBeheerderId() == m.getBeheerder().getId())
			{
				System.out.println(i.getInstellingenSleutel());
			}
		}
		tabel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setView("LijstView");
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
		
		c.gridx = 3;
		c.gridy = 11;
		add(annuleren,c);
		
		box = new LightBox();
		box.createLightBoxEffect(f,this);

	}
	
	public JPanel getInstelPanel()
	{
		return instelPanel;
	}
	
	public JPanel getInstelView()		// gebruiken in private klassen hieronder
	{
		return this;
	}
	
	private class AnnulerenListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			box.closeLightBox(f, getInstelView());
				
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	}
}
