package speeltuin;

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controllers.Databank;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

public class ErfgoedPanel extends JPanel
{	
	public ErfgoedPanel(Model m, Databank d, Erfgoed e)
	{
		setBackground(Color.GRAY);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		String[] types = new String[5];
		types[0] = "Landschappen";
		types[1] = "Dorpsgezichten";
		types[2] = "Gebouwen";
		types[3] = "Archeologische sites";
		types[4] = "Andere";

		
		
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
		//tekstvakken.add(titel);
		titel.setText(e.getNaam());
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
		//titel.addFocusListener(this);
		titel.setFocusable(false);
		add(titel,c);
		
		c.gridx=1;
		c.gridy=2;
		c.gridwidth=2;
		JComboBox type = new JComboBox(types);
		type.setEnabled(false);
		add(type,c);
		
		//naam eigenaar
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 4;
		add(new JLabelFactory().getNormaleTekst(e.getEigenaar().getGebruikersnaam() + " - " + e.getEigenaar().getNaam()),c);
		
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
		//tekstvakken.add(plaats);
		straat.setText(e.getStraat());
		straat.setEditable(false);
		straat.setBorder(null);
		straat.setOpaque(false);
		straat.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		straat.setFocusable(false);
		add(straat,c);
		
		c.gridx=4;
		c.gridwidth=2;
		JTextField huisNr = new JTextField(4);
		//tekstvakken.add(plaats);
		huisNr.setText(e.getHuisnr());
		huisNr.setEditable(false);
		huisNr.setBorder(null);
		huisNr.setOpaque(false);
		huisNr.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		huisNr.setFocusable(false);
		add(huisNr,c);
		
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=1;
		JTextField postcode = new JTextField(4);
		//tekstvakken.add(plaats);
		postcode.setText(e.getPostcode());
		postcode.setEditable(false);
		postcode.setBorder(null);
		postcode.setOpaque(false);
		postcode.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		postcode.setFocusable(false);
		add(postcode,c);
		
		c.gridx=2;
		c.gridwidth = 5;
		JTextField deelgemeente = new JTextField(25);
		//tekstvakken.add(plaats);
		deelgemeente.setText(e.getDeelgemeente());
		deelgemeente.setEditable(false);
		deelgemeente.setBorder(null);
		deelgemeente.setOpaque(false);
		deelgemeente.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		deelgemeente.setFocusable(false);
		add(deelgemeente,c);
		
		c.gridx=1;
		c.gridy=7;
		c.gridwidth = 6;
		JTextArea omschrijving = new JTextArea(3,30);
		//tekstvakken.add(plaats);
		omschrijving.setText(e.getOmschrijving());
		omschrijving.setEditable(false);
		omschrijving.setBorder(null);
		omschrijving.setOpaque(false);
		omschrijving.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		omschrijving.setFocusable(false);
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
		//tekstvakken.add(plaats);
		nuttigeInfo.setText(e.getNuttigeInfo());
		nuttigeInfo.setEditable(false);
		nuttigeInfo.setBorder(null);
		nuttigeInfo.setOpaque(false);
		nuttigeInfo.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		nuttigeInfo.setFocusable(false);
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
		//tekstvakken.add(plaats);
		kenmerken.setText(e.getKenmerken());
		kenmerken.setEditable(false);
		kenmerken.setBorder(null);
		kenmerken.setOpaque(false);
		kenmerken.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		kenmerken.setFocusable(false);
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
		//tekstvakken.add(plaats);
		geschiedenis.setText(e.getGeschiedenis());
		geschiedenis.setEditable(false);
		geschiedenis.setBorder(null);
		geschiedenis.setOpaque(false);
		geschiedenis.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		geschiedenis.setFocusable(false);
		add(geschiedenis,c);
		
		
		/**RECHTERKANT**/
		c.gridx=7;
		c.gridy=2;
		c.gridwidth=1;
		add(new JLabelFactory().getMenuTitel("Documenten"),c);
		
		JPanel documentenPanel = new JPanel(new GridBagLayout());
		documentenPanel.setOpaque(false);
		
		int x = 0;
		int y = 1;
		for (DocumentCMS doc: e.getDocumenten())
		{
			x++;
			if (x>3)
			{
				x=1;
				y++;
			}
			
			c.gridx=x;
			c.gridy=y;
			documentenPanel.add(new DocumentThumbnail(m, d, doc, null, null), c);
		}
		
		c.gridx=7;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=100;	//even overdrijven
		c.fill = GridBagConstraints.VERTICAL;
		add(documentenPanel,c);
	}
	
	public static void main(String args[])
	{
		Model m = new Model();
		Databank d = new Databank(m);
		d.laadDatabank();
		
		JFrame f = new JFrame();
		f.add(new ErfgoedPanel(m, d, m.getErfgoed().get(0)));
		
		f.setSize(1024,600);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
