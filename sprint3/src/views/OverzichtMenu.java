package views;

/** De grijze doorzichtige balk aan de rechterkant van het overzichtscherm.
 *	Filtert en sorteert de documenten in samenwerking met de DocumentController.
 **/

import guiElementen.JLabelFactory;
import guiElementen.OverzichtKiezer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controllers.OverzichtDocumentenController;
import controllers.OverzichtErfgoedController;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

@SuppressWarnings("serial")
public class OverzichtMenu extends JPanel implements ChangeListener
{	
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/transparantGrijs.png"));
	private Image background = backgroundIcon.getImage();
	
	private JCheckBox goedgekeurd, afgekeurd, nietBeoordeeld/*, afbeelding, tekst, video*/;
	private JRadioButton burger, erfgoed, datum, typeDoc;
	private JTextField zoekTxt;
	private JLabel zoekBtn, filteren;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	private OverzichtDocumentenController c1;
	private OverzichtErfgoedController c2;
	private OverzichtContent linkerscherm;
	private Model model;

	@Override
	protected void paintComponent(Graphics g)
	{		
		//achtergrond tekenen
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public OverzichtMenu(Model m, OverzichtDocumentenController controller1, OverzichtErfgoedController controller2, OverzichtContent content)
	{
		this.c1 = controller1;
		this.c2 = controller2;
		this.model = m;
		this.linkerscherm = content;
		
		m.addListener(this);
		setOpaque(false);
		setPreferredSize(new Dimension(200,0));	//de hoogte maakt niet uit, wordt overschreven door layoutmanager.
		setBorder(new EmptyBorder(15, 10, 20, 20) );
		
		FlowLayout f =new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
		
		//switch tussen tegel- of lijstview 
		add(new OverzichtKiezer(linkerscherm, controller1));
		
		//zoeken
		add(new JLabelFactory().getMenuTitel("    Zoekfunctie"));

		zoekTxt = new JTextField("zoeken...");
		zoekTxt.setOpaque(true);
		zoekTxt.setBorder(null);
		zoekTxt.setColumns(12);
		zoekTxt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				zoekBtn.setIcon(new ImageIcon(getClass().getResource("imgs/zoek.png")));
				stateChanged(new ChangeEvent(this));
				goedgekeurd.grabFocus();
			}
		});
		zoekTxt.addKeyListener(new KeyListener()
		{	
			@Override
			public void keyTyped(KeyEvent arg0){}
			
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				stateChanged(new ChangeEvent(this));
			}
			
			@Override
			public void keyPressed(KeyEvent arg0){}
		});
		zoekTxt.addFocusListener(new FocusListener()
		{	
			@Override
			public void focusLost(FocusEvent e)
			{
				zoekBtn.setIcon(new ImageIcon(getClass().getResource("imgs/zoek.png")));
				stateChanged(new ChangeEvent(this));
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				zoekTxt.setText("");
				zoekBtn.setIcon(new ImageIcon(getClass().getResource("imgs/zoek_hover.png")));
				stateChanged(new ChangeEvent(this));
			}
		});
		add(zoekTxt);
				
		zoekBtn = new JLabel();
		zoekBtn.setIcon(new ImageIcon(getClass().getResource("imgs/zoek.png")));
		zoekBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {	}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e){}
			
			@Override
			public void mouseEntered(MouseEvent e){
				zoekBtn.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				stateChanged(new ChangeEvent(this));				
			}
		});
		add(zoekBtn);

		
		//filteren
		filteren = new JLabelFactory().getMenuTitel("    Filter             ");
		add(filteren);
	
		goedgekeurd = new JCheckBox("Goedgekeurd");
		afgekeurd = new JCheckBox("Afgekeurd");
		nietBeoordeeld = new JCheckBox("Nog niet beoordeeld");

		goedgekeurd.setOpaque(false);
		afgekeurd.setOpaque(false);
		nietBeoordeeld.setOpaque(false);
	
		goedgekeurd.setForeground(Color.white);
		afgekeurd.setForeground(Color.white);
		nietBeoordeeld.setForeground(Color.white);		
		
		goedgekeurd.setSelected(false);
		afgekeurd.setSelected(false);
		nietBeoordeeld.setSelected(true);
		
		goedgekeurd.addChangeListener(this);
		afgekeurd.addChangeListener(this); 
		nietBeoordeeld.addChangeListener(this); 
		
		goedgekeurd.setCursor(hand);
		afgekeurd.setCursor(hand);
		nietBeoordeeld.setCursor(hand);
	
		add(goedgekeurd);
		add(afgekeurd);
		add(nietBeoordeeld);
		
		//sorteren
		add(new JLabelFactory().getMenuTitel("    Sorteren             "));
		
		burger = new JRadioButton("Auteur         ");
		erfgoed = new JRadioButton("Naam erfgoed     ");
		datum = new JRadioButton("Laatst toegevoegd");
		typeDoc = new JRadioButton("Type     ");
		
		burger.setForeground(Color.white);
		erfgoed.setForeground(Color.white);
		datum.setForeground(Color.white);
		typeDoc.setForeground(Color.white);
		
		burger.setOpaque(false);
		erfgoed.setOpaque(false);
		datum.setOpaque(false);
		datum.setSelected(true);
		typeDoc.setOpaque(false);
		
		ImageIcon selected = new ImageIcon(getClass().getResource("imgs/radiobutton_selected.png"));
		ImageIcon notSelected = new ImageIcon(getClass().getResource("imgs/radiobutton_normal.png"));
		ImageIcon hover = new ImageIcon(getClass().getResource("imgs/radiobutton_hover.png"));
		
		burger.setSelectedIcon(selected);
		erfgoed.setSelectedIcon(selected);
		datum.setSelectedIcon(selected);
		typeDoc.setSelectedIcon(selected);
		
		burger.setIcon(notSelected);
		erfgoed.setIcon(notSelected);
		datum.setIcon(notSelected);
		typeDoc.setIcon(notSelected);

		burger.setRolloverIcon(hover);
		erfgoed.setRolloverIcon(hover);
		datum.setRolloverIcon(hover);
		typeDoc.setRolloverIcon(hover);
		
		burger.addChangeListener(this);
		erfgoed.addChangeListener(this);
		datum.addChangeListener(this);
		typeDoc.addChangeListener(this);
		
		burger.setCursor(hand);
		erfgoed.setCursor(hand);
		datum.setCursor(hand);
		typeDoc.setCursor(hand);
		
		ButtonGroup group = new ButtonGroup();
		group.add(burger);
		group.add(erfgoed);
		group.add(datum);
		group.add(typeDoc);
		
		add(burger);
		add(erfgoed);
		add(datum);
		add(typeDoc);
		
		goedgekeurd.grabFocus(); 	//zodat het zoekveld niet direct een rood icoontje heeft
		
		veranderType();
		stateChanged(new ChangeEvent(this));
	}

	
	public void veranderType()
	{
		if (linkerscherm.getTypeContent().equals("Erfgoed"))
		{
			goedgekeurd.setVisible(false);
			afgekeurd.setVisible(false);
			nietBeoordeeld.setVisible(false);
			filteren.setVisible(false);
		}
		else
		{
			goedgekeurd.setVisible(true);
			afgekeurd.setVisible(true);
			nietBeoordeeld.setVisible(true);
			filteren.setVisible(true);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{	
		veranderType();
		
		//werkt samen met de controller om te filteren en het resultaat te sorteren
		ArrayList<DocumentCMS> gefilterd1 = new ArrayList<DocumentCMS>();
		ArrayList<Erfgoed> gefilterd2 = model.getErfgoed();
		
		if (goedgekeurd.isSelected())
			gefilterd1.addAll(c1.filterOpStatus("Goedgekeurd"));
		if (afgekeurd.isSelected())
			gefilterd1.addAll(c1.filterOpStatus("Afgekeurd"));
		if (nietBeoordeeld.isSelected())
			gefilterd1.addAll(c1.filterOpStatus("Nog niet beoordeeld"));
		
		if (!zoekTxt.getText().equals("") && !zoekTxt.getText().equals("zoeken..."))
		{
			gefilterd1 = c1.zoek(zoekTxt.getText(), gefilterd1);
			gefilterd2 = c2.zoek(zoekTxt.getText(), gefilterd2);
		}
		
		c1.setInTeLaden(gefilterd1);
		c2.setInTeLaden(gefilterd2);
		
		if (burger.isSelected())
		{
			c1.sorteerOpBurger();
			c2.sorteerOpBurger();
		}
		else if (erfgoed.isSelected())
		{
			c1.sorteerOpErfgoed();
			c2.sorteerOpErfgoed();
		}
		else if (datum.isSelected())
		{
			c1.sorteerOpLaatstToegevoegd();
			c2.sorteerOpLaatstToegevoegd();
		}
		else if (typeDoc.isSelected())
		{
			c1.sorteerOpType();
			c2.sorteerOpType();
		}
	}
}