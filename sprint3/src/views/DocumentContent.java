package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import guiElementen.Beoordeling;
import guiElementen.DocumentMedia;
import guiElementen.DocumentTekst;
import guiElementen.DocumentVideo;
import guiElementen.JLabelFactory;
import guiElementen.DocumentAfbeelding;
import guiElementen.TypeKiezer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import controllers.Databank;
import controllers.DocumentController;
import model.DocumentCMS;
import model.Model;

/**De linkerkant van DocumentView. Toont alle details van een document en haar erfgoed. Geeft ook de
 * mogelijkheid om het document te bewerken of verwijderen */

@SuppressWarnings("serial")
public class DocumentContent extends JPanel implements FocusListener
{
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private DocumentCMS document;
	private Beoordeling beoordeling;
	private DocumentMedia media;
	private DocumentController controller;
	private ArrayList<JTextComponent> tekstvakken;
	private JLabel waarschuwing;
	
	public DocumentContent(Model m, Databank db, Hoofd h, DocumentCMS doc)
	{
		this.model = m;
		this.databank = db;
		this.hoofd = h;
		this.controller = new DocumentController(m,db,doc);		
		this.document = doc;
		
		tekstvakken = new ArrayList<JTextComponent>();
		
		setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//erfgoed titel
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(2,3,2,3);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		
		JTextField titel = new JTextField(20);
		tekstvakken.add(titel);
		titel.setText(document.getErfgoed().getNaam());
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
		titel.addFocusListener(this);
		titel.setFocusable(false);
		add(titel,c);
		
		
		//naam eigenaar
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 4;
		add(new JLabelFactory().getNormaleTekst(doc.getEigenaar().getGebruikersnaam() + " - " + doc.getEigenaar().getNaam()),c);
		
		//beetje ruimte
		c.gridy =3;
		add(new JLabel("   "),c);
		
		//locatie
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=3;
		add(new JLabelFactory().getNormaleTekst("Locatie: "),c);
		
		c.gridx=2;
		JTextField plaats = new JTextField(20);
		tekstvakken.add(plaats);
		plaats.setText(document.getErfgoed().getDeelgemeente());
		plaats.setEditable(false);
		plaats.setBorder(null);
		plaats.setOpaque(false);
		plaats.setForeground(Color.WHITE);
		plaats.addFocusListener(this);
		plaats.setFocusable(false);
		add(plaats,c);
		
		//datum
		c.gridx = 1;
		c.gridy =5;
		c.gridwidth = 1;
		add(new JLabelFactory().getNormaleTekst("Datum ingediend: "), c);
		
		c.gridx = 2;
		JTextField datum = new JTextField(20);
		//tekstvakken.add(datum);			//moet niet, de datum moet niet aangepast worden
		datum.setText("" + document.getDatum());
		datum.setEditable(false);
		datum.setBorder(null);
		datum.setOpaque(false);
		datum.setForeground(Color.WHITE);
		datum.addFocusListener(this);
		datum.setFocusable(false);
		add(datum, c);
		
		//opmerkingen
		c.gridx=1;
		c.gridy =6;
		c.gridwidth = 2;
		add(new JLabelFactory().getNormaleTekst("Opmerkingen: "), c);
		
		c.gridx=1;
		c.gridy = 7 ;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.VERTICAL;
		
		JTextArea opmerkingen = new JTextArea(10,30);
		tekstvakken.add(opmerkingen);
		opmerkingen.setDocument(new CustomDocument(opmerkingen));	//zorgt voor de limiet van 255 tekens
		opmerkingen.setText(document.getOpmerkingen());
		opmerkingen.setFont(new JLabelFactory().getItalic("").getFont());
		opmerkingen.setEditable(false);
		opmerkingen.setBorder(null);
		opmerkingen.setOpaque(false);
		opmerkingen.setForeground(Color.WHITE);
		opmerkingen.setLineWrap(true);
		opmerkingen.grabFocus(); 		//nodig zodat de waarschuwing niet automatisch verschijnt
		
		add(opmerkingen, c);
		
		//media
		c.gridx = 3;
		c.gridy =1;
		c.gridheight= 7;
		
		if (doc.getTypeDocument().equals("Afbeelding"))
		{
			media = new DocumentAfbeelding(controller,databank);
		}
		else if (doc.getTypeDocument().equals("Tekst"))
		{
			media = new DocumentTekst(controller); 
		}
		else if (doc.getTypeDocument().equals("Video"))
		{
			media = new DocumentVideo();
		}
		else
		{
			//het is een nieuw document
			media = new TypeKiezer(controller, this, databank);
			setEditable(true);
			
			opmerkingen.setOpaque(true);
			opmerkingen.setEditable(true);
			opmerkingen.setForeground(Color.black);
			opmerkingen.setBorder(new JTextField().getBorder());			
		}
		add((Component) media,c);
		
		//beoordeling
		c.gridx = 3;
		c.gridy = 12;
		c.gridheight = 1;
		beoordeling = new Beoordeling(databank,model,document,hoofd,this, controller);
		add(beoordeling,c);
		
		//waarschuwing voor het wijzigen van velden in erfgoed
		c.gridy = 13;
		c.gridwidth = 3;
		waarschuwing = new JLabelFactory().getWaarschuwing("<html><center>Opgelet: wijzigingen in dit tekstvak gelden<br />voor alle documenten in dit erfgoed!</center></html>");
		waarschuwing.setVisible(false);
		add(waarschuwing, c);
	}
	
	//retourneert alle tekstvakken die bewerkbaar mogen gezet worden door (o.a.) Beoordeling
	public ArrayList<JTextComponent> getTekstvakken()
	{
		return tekstvakken;
	}
	
	public void setEditable(boolean b)
	{
		media.setEditable(b);
	}
	public void quit()
	{
		media.quit();
	}
	
	public void setMedia(DocumentMedia dm)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy =1;
		c.gridheight= 7;
		
		setVisible(false); 
		remove((JPanel)media);
		media = dm; 
		add((JPanel)media,c);
		setVisible(true);
		
	}
	
	//zorgt ervoor dat opmerkingen niet meer dan 255 karakters kan bevatten
	public static class CustomDocument extends PlainDocument
	{
		private static int MAX_LENGTH = 255;
		private JTextArea tField;
		public CustomDocument(JTextArea field)
		{
			tField = field;
		}

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			if(str == null || tField.getText().length() > MAX_LENGTH) 
				return;
			super.insertString(offs, str, a);
		}	
	}

	@Override
	public void focusGained(FocusEvent arg0)		//als een veld van erfgoed bewerkt wordt, moet de gebruiker
	{												//gewaarschuwd worden dat zijn wijziging toegepast wordt in
		waarschuwing.setVisible(true);				//alle documenten voor dat erfgoed
		
	}
	@Override
	public void focusLost(FocusEvent arg0)
	{
		waarschuwing.setVisible(false);
	}
}
