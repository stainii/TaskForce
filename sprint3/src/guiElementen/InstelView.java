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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controllers.Databank;

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
	private Databank d;
	private JLabelFactory jLabelFactory;
	private LightBox box;
	private JTextField nieuweRedenTxt;
	private DefaultListModel redenModel;
	private JList standaardRedenen;
	private ArrayList<String> reden;
	private boolean inPanel = false;
	
	public InstelView(Model model,JFrame frame,OverzichtView view,Databank data)
	{
		this.m = model;
		this.f = frame;
		this.v = view;
		this.d = data;
		reden = new ArrayList<String>();
		
		jLabelFactory = new JLabelFactory();
		
		setSize(new Dimension(550,300));
		setLayout(new GridBagLayout());
		setBackground(Color.GRAY);
		
		
		// deze lus gaat de ArrayList vullen met de standaardreden afhankelijk welke gebruiker het is.
		// Dit staat hier omdat ik steeds een foutmelding kreeg als ik de arraylist declareerde in het model?? 
		// Foutmelding was : java.lang.OutOfMemoryError: Java heap space ( dit misschien eens navragen aan Van Impe? )
		for(Instellingen i : m.getInstellingen())					
		{
			if(i.getBeheerderId() == m.getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("StandaardReden"))
				{
					reden.add(i.getInstellingenWaarde());
				}
			}
		}
		m.setStandaardReden(reden);		//ArrayList<String> reden gaat geset worden in model.
		
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
		add(jLabelFactory.getTitel("Instellingen voor " + m.getBeheerder().getVoornaam()),c);
		
		
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
				m.getBeheerder().setTypeContent(m.getBeheerder().getId(),"Erfgoed" );
				d.updateInstellingen("Erfgoed", m.getInstellingenId("TypeContent"));
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
				m.getBeheerder().setTypeContent(m.getBeheerder().getId(),"Documenten" );
				d.updateInstellingen("Documenten", m.getInstellingenId("TypeContent"));
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
		JRadioButton tegel = new JRadioButton("Tegels");
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
				m.getBeheerder().setView(m.getBeheerder().getId(),"TegelView" );
				d.updateInstellingen("TegelView", m.getInstellingenId("View"));
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
		
		tabel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setView("LijstView");
				m.getBeheerder().setView(m.getBeheerder().getId(),"LijstView" );
				d.updateInstellingen("LijstView", m.getInstellingenId("View"));
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
		
		redenModel = new DefaultListModel();
		for(int i=0;i<m.getStandaardReden().size();i++)
		{
			redenModel.addElement(m.getStandaardReden().get(i));
		}
		
		standaardRedenen = new JList(redenModel);
		standaardRedenen.setLayoutOrientation(JList.VERTICAL);
		standaardRedenen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane redenScroll = new JScrollPane(standaardRedenen);
		redenScroll.setPreferredSize(new Dimension(100,50));
		redenScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		standaardRedenen.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				//m.setInstelling(standaardRedenen.getSelectedValue().toString());
			}
		});
		add(standaardRedenen,c);
				
		c.gridx = 2;
		c.gridy = 10;
		c.gridheight = 1;
		final String nieuweReden = "Typ hier een nieuwe reden...";
		nieuweRedenTxt = new JTextField(nieuweReden);
		nieuweRedenTxt.setForeground(Color.gray);
		nieuweRedenTxt.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {
				nieuweRedenTxt.setForeground(Color.gray);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				nieuweRedenTxt.setForeground(Color.black);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		nieuweRedenTxt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				nieuweRedenTxt.setForeground(Color.gray);
				if(nieuweRedenTxt.getText().isEmpty())
				{
					nieuweRedenTxt.setText(nieuweReden);
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				nieuweRedenTxt.setForeground(Color.black);
				if(nieuweRedenTxt.getText().equals(nieuweReden));
				{
					nieuweRedenTxt.setText("");
					nieuweRedenTxt.setColumns(14);
				}
			}
		});
		add(nieuweRedenTxt,c);
		
		c.gridx = 3;
		c.gridy = 10;
		JButton nieuweRedenBtn = new JButton("Toevoegen");
		add(nieuweRedenBtn,c);
		nieuweRedenBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!nieuweRedenTxt.getText().equals(nieuweReden))
				{
					m.toevoegenInstelling(new Instellingen(0,"StandaardReden",nieuweRedenTxt.getText(),m.getBeheerder().getId()));
					d.voegInstellingToe("StandaardReden",nieuweRedenTxt.getText() , m.getBeheerder().getId());		//toevoegen aan databank
					redenModel.addElement(nieuweRedenTxt.getText());			// toevoegen aan JList
					nieuweRedenTxt.setText(nieuweReden);
				}
			}
		});
		
		c.gridx = 2;
		c.gridy = 11;
		JButton verwijderenBtn = new JButton("Verwijder geselecteerde reden");
		verwijderenBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				m.setInstelling(standaardRedenen.getSelectedValue().toString());
				d.verwijderStandaardReden(m.getInstelling());
				m.verwijderStandaardReden(standaardRedenen.getSelectedValue().toString());
				redenModel.remove(standaardRedenen.getSelectedIndex());
			}
		});
		add(verwijderenBtn, c);
		
		c.gridx = 3;
		c.gridy = 11;
		add(annuleren,c);
		
		//__Radiobuttons selected of niet
		if(m.getBeheerder().getTypeContent().equals(""))
		{
			erfgoed.setSelected(false);
			documenten.setSelected(false);
		}
		if(m.getBeheerder().getTypeContent().equals("Erfgoed"))
			erfgoed.setSelected(true);
		if(m.getBeheerder().getTypeContent().equals("Documenten"))
			documenten.setSelected(true);
		
		if(m.getBeheerder().getView().equals(""))
		{
			tegel.setSelected(false);
			tabel.setSelected(false);
		}
		if(m.getBeheerder().getView().equals("TegelView"))
			tegel.setSelected(true);
		if(m.getBeheerder().getView().equals("LijstView"))
			tabel.setSelected(true);
		
		
		box = new LightBox();
		box.createLightBoxEffect(f,this);
		box.grabFocus();
		box.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 27)			// 27 is de keycode voor Esc
					box.closeLightBox(f, getInstelView());
			}
		});
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {

			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				inPanel = false;
				System.out.println(inPanel);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				inPanel = true;
				System.out.println(inPanel);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(inPanel = false)
					System.out.println("jep");
			}
		});
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
