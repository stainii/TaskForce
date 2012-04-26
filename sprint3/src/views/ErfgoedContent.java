package views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import guiElementen.AutoaanvullendeCombobox;
import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;
import guiElementen.MooiTextField;
import guiElementen.MooiTextArea;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import sun.java2d.loops.MaskFill;

import controllers.Databank;
import model.DocumentCMS;
import model.Erfgoed;
import model.Gemeenten;
import model.Model;
import guiElementen.JTextFieldLimit;

/**De linkerkant van DocumentView. Toont alle details van een document en haar erfgoed. Geeft ook de
 * mogelijkheid om het document te bewerken of verwijderen */

@SuppressWarnings("serial")
public class ErfgoedContent extends JPanel
{
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private Erfgoed erfgoed;
	private ArrayList<JTextComponent> tekstvakken;
	@SuppressWarnings("rawtypes")
	private JComboBox type;
	private String[] types;
	private AutoaanvullendeCombobox deelgemeente,postcode;
	private JTextField postcodeTxt,deelgemeenteTxt;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ErfgoedContent(final Model m, Databank db, Hoofd h, Erfgoed e)
	{
		this.model = m;
		this.databank = db;
		this.hoofd = h;
		this.erfgoed = e;
		
		tekstvakken = new ArrayList<JTextComponent>();
				
		types = new String[5];
		types[0] = "Landschap";
		types[1] = "Dorpsgezicht";
		types[2] = "Gebouw";
		types[3] = "Archeologische site";
		types[4] = "Andere";
		
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		/**LINKERKANT**/
		//erfgoed titel
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(2,3,2,3);
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridwidth = 6;
		
		MooiTextField titel = new MooiTextField("","Titel",new JLabelFactory().getTitel("").getFont());
		titel.setDocument(new JTextFieldLimit(40));
		titel.setText(erfgoed.getNaam());
		tekstvakken.add(titel);
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
		titel.setColumns(20);
		add(titel,c);
		
		c.gridx=1;
		c.gridy=2;
		c.gridwidth=2;
		type = new JComboBox(types);
		type.setEnabled(false);
		UIManager.put("ComboBox.disabledForeground", Color.BLACK);	//maakt de tekst van de JComboBox zwart.
		
		//het juiste type selecteren
		for (int i=0; i<types.length; i++)
		{
			if (erfgoed.getTypeErfgoed().equals(types[i]))
			{
				type.setSelectedIndex(i);
			}
		}
		
		add(type,c);
		
		//naam eigenaar
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 4;
		add(new JLabelFactory().getNormaleTekst(erfgoed.getBurger()!=null?erfgoed.getBurger().getGebruikersnaam() + " - " + erfgoed.getBurger().getNaam():erfgoed.getBeheerder().getNaam()),c);
		
		//beetje ruimte
		c.gridx=1;
		c.gridy=3;
		c.gridwidth=6;
		add(new JLabel("   "),c);
		
		//locatie
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Locatie: "),c);
		
		c.gridx=1;
		c.gridy=5;
		c.gridwidth=3;
		MooiTextField straat = new MooiTextField("","Straat");
		straat.setDocument(new JTextFieldLimit(50));
		straat.setText(erfgoed.getStraat());
		tekstvakken.add(straat);
		straat.setColumns(25);
		straat.setEditable(false);
		straat.setBorder(null);
		straat.setOpaque(false);
		straat.setForeground(Color.WHITE);
		add(straat,c);
		
		c.gridx=4;
		c.gridwidth=2;
		MooiTextField huisNr = new MooiTextField("","Nr");
		huisNr.setDocument(new JTextFieldLimit(10));
		huisNr.setText(erfgoed.getHuisnr());
		tekstvakken.add(huisNr);
		huisNr.setColumns(4);
		huisNr.setEditable(false);
		huisNr.setBorder(null);
		huisNr.setOpaque(false);
		huisNr.setForeground(Color.WHITE);
		add(huisNr,c);
		
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=1;
		
		ArrayList<Integer> post = new ArrayList<Integer>();

		for(Gemeenten g : m.getGemeenten())
		{
			if(!post.contains(g.getPostcode()))
				post.add(g.getPostcode());	
		}
	
		postcode = new AutoaanvullendeCombobox(post,"Integer", "Postcode",new JFormattedTextField());
		postcode.setSelectedItem(erfgoed.getPostcode());
		postcode.setEnabled(false);
		postcode.setVisible(false);
		String p = "Postcode";
		
		if(postcode.getSelectedItem().equals(""))
		postcode.getEditor().setItem(p);
		
		postcodeTxt = new JTextField();
		postcodeTxt.setEditable(false);
		postcodeTxt.setBorder(null);
		postcodeTxt.setOpaque(false);
		postcodeTxt.setForeground(Color.white);
		postcodeTxt.setVisible(true);
		postcodeTxt.setText(erfgoed.getPostcode());
		
		add(postcode,c);
		add(postcodeTxt,c);
		
		postcode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				for(Gemeenten g : m.getGemeenten())
				{
					if(postcode.getSelectedItem() == null)
					{
						DefaultComboBoxModel m = new DefaultComboBoxModel();
						for(String s : model.getNaamGemeenten())
						{
							m.addElement(s);
						}
						deelgemeente.setModel(m);
						deelgemeente.setSelectedItem("");
						return;
					}
					else if(postcode.getSelectedItem().equals(g.getPostcode()))
						if(postcode.getSelectedItem().equals(9550))
						{
							DefaultComboBoxModel m = new DefaultComboBoxModel();
							for(String s : model.getNaamGemeentenZonderAndereGemeenten(Integer.parseInt(postcode.getSelectedItem().toString())))
							{
								m.addElement(s);
							}
							
							deelgemeente.setModel(m);
							deelgemeente.setSelectedItem("-- Kies een gemeente --");
						}
						else
							deelgemeente.setSelectedItem(g.getGemeente());
				}
			}
		});		
		
		c.gridx=2;
		c.gridwidth = 5;
				
		deelgemeente = new AutoaanvullendeCombobox(model.getNaamGemeenten(),"String", "Deelgemeente",new JTextField());
		deelgemeente.setSelectedItem(erfgoed.getDeelgemeente());
		deelgemeente.setEnabled(false);
		deelgemeente.setVisible(false);
		if(deelgemeente.getSelectedItem().equals(""))
			deelgemeente.getEditor().setItem("Deelgemeente");
		
		deelgemeenteTxt = new JTextField();
		deelgemeenteTxt.setEditable(false);
		deelgemeenteTxt.setBorder(null);
		deelgemeenteTxt.setOpaque(false);
		deelgemeenteTxt.setForeground(Color.white);
		deelgemeenteTxt.setVisible(true);
		deelgemeenteTxt.setText(erfgoed.getDeelgemeente());

		add(deelgemeente,c);
		add(deelgemeenteTxt,c);
		
		
		c.gridx=1;
		c.gridy=7;
		c.gridwidth = 6;
		MooiTextArea omschrijving = new MooiTextArea(erfgoed.getOmschrijving(),"Omschrijving");
		tekstvakken.add(omschrijving);
		omschrijving.setRows(3);
		omschrijving.setColumns(30);
		omschrijving.setEditable(false);
		omschrijving.setOpaque(false);
		omschrijving.setBorder(null);
		omschrijving.setForeground(Color.WHITE);
		omschrijving.setLineWrap(true);
		JScrollPane omschrijvingScroll = new JScrollPane(omschrijving);
		omschrijvingScroll.setBorder(null);
		omschrijvingScroll.getViewport().setBorder(null);
		omschrijvingScroll.setOpaque(false);
		omschrijvingScroll.getViewport().setOpaque(false);
		add(omschrijvingScroll,c);
		
		
		//nuttige info
		c.gridx=1;
		c.gridy=8;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Nuttige info:"),c);
		
		c.gridx=1;
		c.gridy=9;
		c.gridwidth = 6;
		MooiTextArea nuttigeInfo = new MooiTextArea(erfgoed.getNuttigeInfo(), "Nuttige info");
		tekstvakken.add(nuttigeInfo);
		nuttigeInfo.setRows(5);
		nuttigeInfo.setColumns(30);
		nuttigeInfo.setEditable(false);
		nuttigeInfo.setOpaque(false);
		nuttigeInfo.setBorder(null);
		nuttigeInfo.setForeground(Color.WHITE);
		nuttigeInfo.setLineWrap(true);
		JScrollPane nuttigeInfoScroll = new JScrollPane(nuttigeInfo);
		nuttigeInfoScroll.setBorder(null);
		nuttigeInfoScroll.getViewport().setBorder(null);
		nuttigeInfoScroll.setOpaque(false);
		nuttigeInfoScroll.getViewport().setOpaque(false);
		add(nuttigeInfoScroll,c);
		
		//kenmerken
		c.gridx=1;
		c.gridy=10;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Kenmerken:"),c);
				
		c.gridx=1;
		c.gridy=11;
		c.gridwidth = 6;
		MooiTextArea kenmerken = new MooiTextArea(erfgoed.getKenmerken(), "Kenmerken");
		tekstvakken.add(kenmerken);
		kenmerken.setRows(5);
		kenmerken.setColumns(30);
		kenmerken.setEditable(false);
		kenmerken.setBorder(null);
		kenmerken.setOpaque(false);
		kenmerken.setForeground(Color.WHITE);
		kenmerken.setLineWrap(true);
		JScrollPane kenmerkenScroll = new JScrollPane(kenmerken);
		kenmerkenScroll.setBorder(null);
		kenmerkenScroll.getViewport().setBorder(null);
		kenmerkenScroll.setOpaque(false);
		kenmerkenScroll.getViewport().setOpaque(false);
		add(kenmerkenScroll,c);
		
		//geschiedenis
		c.gridx=1;
		c.gridy=12;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Geschiedenis:"),c);
				
		c.gridx=1;
		c.gridy=13;
		c.gridwidth = 6;
		MooiTextArea geschiedenis = new MooiTextArea(erfgoed.getGeschiedenis(),"Geschiedenis");
		tekstvakken.add(geschiedenis);
		geschiedenis.setRows(5);
		geschiedenis.setColumns(30);
		geschiedenis.setEditable(false);
		geschiedenis.setBorder(null);
		geschiedenis.setOpaque(false);
		geschiedenis.setForeground(Color.WHITE);
		geschiedenis.setLineWrap(true);
		JScrollPane geschiedenisScroll = new JScrollPane(geschiedenis);
		geschiedenisScroll.setBorder(null);
		geschiedenisScroll.getViewport().setBorder(null);
		geschiedenisScroll.setOpaque(false);
		geschiedenisScroll.getViewport().setOpaque(false);
		add(geschiedenisScroll,c);
		
		/**RECHTERKANT**/
		c.gridx=7;
		c.gridy=2;
		c.gridwidth=2;
		add(new JLabelFactory().getMenuTitel("Documenten"),c);
		
		JPanel documentenPanel = new JPanel(new GridBagLayout());
		documentenPanel.setOpaque(false);
		
		int x = 0;
		int y = 1;
		c.gridwidth=1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		for (DocumentCMS doc: erfgoed.getDocumenten())
		{
			x++;
			if (x>4)
			{
				x=1;
				y++;
			}
			
			c.gridx=x;
			c.gridy=y;
			JPanel l =new DocumentThumbnail(model, databank, doc, hoofd, null);
			documentenPanel.add(l, c);
		}
		
		c.gridx=7;
		c.gridy=3;
		c.gridwidth=2;
		c.gridheight=11;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.VERTICAL;
		add(documentenPanel,c);	
		
		
		//als het een nieuw erfgoed is (m.a.w. een leeg erfgoed), dan worden alles editable gezet
		if (tekstvakken.get(0).getText().equals(""))
			bewerken();
	}
	
	public String[] bewerken()
	{
		String[] s = new String[tekstvakken.size()+3];
		
		if (tekstvakken.get(0).isEditable())
		{
			postcode.setVisible(false);
			postcodeTxt.setVisible(true);
			
			deelgemeente.setVisible(false);
			deelgemeenteTxt.setVisible(true);
			
			if (tekstvakken.get(0).getText().equals("") && tekstvakken.get(4).getText().equals(""))
			{
				JOptionPane.showMessageDialog(null,"Gelieve een titel en een deelgemeente in te vullen", "Foute invoer", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if (tekstvakken.get(0).getText().equals(""))
			{
				JOptionPane.showMessageDialog(null,"Gelieve een titel in te vullen", "Foute invoer", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if (tekstvakken.get(4).getText().equals(""))
			{
				JOptionPane.showMessageDialog(null,"Gelieve een deelgemeente in te vullen", "Foute invoer", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			for (int i = 0; i<tekstvakken.size(); i++)
			{
				tekstvakken.get(i).setEditable(false);
				s[i] = tekstvakken.get(i).getText();
				tekstvakken.get(i).setOpaque(false);
				tekstvakken.get(i).setForeground(Color.WHITE);
			}
			s[tekstvakken.size()] = type.getSelectedItem().toString();
			s[tekstvakken.size()+1] = postcode.getSelectedItem().toString();
			s[tekstvakken.size()+2] = deelgemeente.getSelectedItem().toString();
			type.setEnabled(false);
			
		}
		else
		{
			for (int i = 0; i<tekstvakken.size(); i++)
			{
				tekstvakken.get(i).setEditable(true);
				tekstvakken.get(i).setOpaque(true);
				tekstvakken.get(i).setForeground(Color.BLACK);
			}
			type.setEnabled(true);
			postcode.setEnabled(true);
			postcode.setVisible(true);
			postcodeTxt.setVisible(false);
			deelgemeente.setEnabled(true);
			deelgemeente.setVisible(true);
			deelgemeenteTxt.setVisible(false);
		}
		
		return s;
	}
}