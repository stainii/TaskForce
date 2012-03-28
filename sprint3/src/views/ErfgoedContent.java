package views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import controllers.Databank;
import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ErfgoedContent(Model m, Databank db, Hoofd h, Erfgoed e)
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
		
		JTextField titel = new JTextField(20);
		tekstvakken.add(titel);
		titel.setText(erfgoed.getNaam());
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
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
		add(new JLabelFactory().getNormaleTekst(erfgoed.getEigenaar().getGebruikersnaam() + " - " + erfgoed.getEigenaar().getNaam()),c);
		
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
		JTextField straat = new JTextField(25);
		tekstvakken.add(straat);
		straat.setText(erfgoed.getStraat());
		straat.setEditable(false);
		straat.setBorder(null);
		straat.setOpaque(false);
		straat.setForeground(Color.WHITE);
		add(straat,c);
		
		c.gridx=4;
		c.gridwidth=2;
		JTextField huisNr = new JTextField(4);
		tekstvakken.add(huisNr);
		huisNr.setText(erfgoed.getHuisnr());
		huisNr.setEditable(false);
		huisNr.setBorder(null);
		huisNr.setOpaque(false);
		huisNr.setForeground(Color.WHITE);
		add(huisNr,c);
		
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=1;
		JTextField postcode = new JTextField(4);
		tekstvakken.add(postcode);
		postcode.setText(erfgoed.getPostcode());
		postcode.setEditable(false);
		postcode.setBorder(null);
		postcode.setOpaque(false);
		postcode.setForeground(Color.WHITE);
		add(postcode,c);
		
		c.gridx=2;
		c.gridwidth = 5;
		JTextField deelgemeente = new JTextField(25);
		tekstvakken.add(deelgemeente);
		deelgemeente.setText(erfgoed.getDeelgemeente());
		deelgemeente.setEditable(false);
		deelgemeente.setBorder(null);
		deelgemeente.setOpaque(false);
		deelgemeente.setForeground(Color.WHITE);
		add(deelgemeente,c);
		
		c.gridx=1;
		c.gridy=7;
		c.gridwidth = 6;
		JTextArea omschrijving = new JTextArea(3,30);
		tekstvakken.add(omschrijving);
		omschrijving.setText(erfgoed.getOmschrijving());
		omschrijving.setEditable(false);
		omschrijving.setBorder(null);
		omschrijving.setOpaque(false);
		omschrijving.setForeground(Color.WHITE);
		add(omschrijving,c);
		
		
		//nuttige info
		c.gridx=1;
		c.gridy=8;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Nuttige info:"),c);
		
		c.gridx=1;
		c.gridy=9;
		c.gridwidth = 6;
		JTextArea nuttigeInfo = new JTextArea(5,30);
		tekstvakken.add(nuttigeInfo);
		nuttigeInfo.setText(erfgoed.getNuttigeInfo());
		nuttigeInfo.setEditable(false);
		nuttigeInfo.setBorder(null);
		nuttigeInfo.setOpaque(false);
		nuttigeInfo.setForeground(Color.WHITE);
		add(nuttigeInfo,c);
		
		//kenmerken
		c.gridx=1;
		c.gridy=10;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Kenmerken:"),c);
				
		c.gridx=1;
		c.gridy=11;
		c.gridwidth = 6;
		JTextArea kenmerken = new JTextArea(5,30);
		tekstvakken.add(kenmerken);
		kenmerken.setText(erfgoed.getKenmerken());
		kenmerken.setEditable(false);
		kenmerken.setBorder(null);
		kenmerken.setOpaque(false);
		kenmerken.setForeground(Color.WHITE);
		add(kenmerken,c);
		
		//geschiedenis
		c.gridx=1;
		c.gridy=12;
		c.gridwidth=6;
		add(new JLabelFactory().getMenuTitel("Geschiedenis:"),c);
				
		c.gridx=1;
		c.gridy=13;
		c.gridwidth = 6;
		JTextArea geschiedenis = new JTextArea(5,30);
		tekstvakken.add(geschiedenis);
		geschiedenis.setText(erfgoed.getGeschiedenis());
		geschiedenis.setEditable(false);
		geschiedenis.setBorder(null);
		geschiedenis.setOpaque(false);
		geschiedenis.setForeground(Color.WHITE);
		add(geschiedenis,c);
		
		
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
		for (DocumentCMS doc: erfgoed.getDocumenten())
		{
			x++;
			if (x>5)
			{
				x=1;
				y++;
			}
			
			c.gridx=x;
			c.gridy=y;
			documentenPanel.add(new DocumentThumbnail(model, databank, doc, hoofd, null), c);
		}
		documentenPanel.setBackground(Color.BLACK);
		
		c.gridx=7;
		c.gridy=3;
		c.gridwidth=2;
		c.gridheight=10;
		c.fill = GridBagConstraints.VERTICAL;
		add(documentenPanel,c);				
	}
	
	public String[] bewerken()
	{
		String[] s = new String[tekstvakken.size()+1];
		
		if (tekstvakken.get(0).isEditable())
		{
			for (int i = 0; i<tekstvakken.size(); i++)
			{
				tekstvakken.get(i).setEditable(false);
				s[i] = tekstvakken.get(i).getText();
				tekstvakken.get(i).setOpaque(false);
				tekstvakken.get(i).setForeground(Color.WHITE);
			}
			s[tekstvakken.size()] = type.getSelectedItem().toString();
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
		}
		
		return s;
	}

}