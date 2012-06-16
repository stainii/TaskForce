package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import guiElementen.Beoordeling;
import guiElementen.Contact;
import guiElementen.DocumentAndere;
import guiElementen.DocumentLink;
import guiElementen.DocumentMedia;
import guiElementen.DocumentTekst;
import guiElementen.DocumentVideo;
import guiElementen.JLabelFactory;
import guiElementen.DocumentAfbeelding;
import guiElementen.MooiTextField;
import guiElementen.MooiTextArea;
import guiElementen.TypeKiezer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import systemTray.InSystemTray;
import controllers.Databank;
import controllers.DocumentController;
import model.DocumentCMS;
import model.Model;
import guiElementen.JTextFieldLimit;

/**De linkerkant van DocumentView. Toont alle details van een document en haar erfgoed. Geeft ook de
 * mogelijkheid om het document te bewerken of verwijderen */

@SuppressWarnings("serial")
public class DocumentContent extends JPanel
{
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private DocumentCMS document;
	private Beoordeling beoordeling;
	private DocumentMedia media;
	private DocumentController controller;
	private ArrayList<JTextComponent> tekstvakken;
	private JLabel aard;
	private JPanel documentPanel;
	private JComboBox aardBox;
	
	public DocumentContent(Model m, Databank db, Hoofd h, DocumentCMS doc, InSystemTray tray)
	{
		this.model = m;
		this.databank = db;
		this.hoofd = h;
		this.controller = new DocumentController(m,db,doc, tray);		
		this.document = doc;
		
		tekstvakken = new ArrayList<JTextComponent>();
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		documentPanel = new JPanel(new GridBagLayout());
		documentPanel.setOpaque(false);
		add(documentPanel, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();
		
		//document titel
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(2,3,2,3);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		
		MooiTextField titel = new MooiTextField("","Titel",new JLabelFactory().getTitel("").getFont());
		titel.setDocument(new JTextFieldLimit(50));
		titel.setText(document.getTitel());
		tekstvakken.add(titel);
		titel.setColumns(20);
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
		documentPanel.add(titel,c);
		
		//naam eigenaar
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		if (doc.getBurger()!=null)
			documentPanel.add(new Contact(doc.getBurger()),c);
		else
			documentPanel.add(new Contact(doc.getBeheerder()),c);
		
		//aard
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		documentPanel.add(new JLabelFactory().getNormaleTekst("Aard: "),c);
		
		String[] a = {"Affiche", "Andere","Brochure","Film", "Foto", "Kaart", "Postkaart", "Prent", "Tekening"};
		aardBox = new JComboBox(a);
		//aardBox.setEnabled(false);
		aardBox.setSelectedItem(doc.getAard());
		aardBox.setVisible(false);
		
		aard = new JLabelFactory().getNormaleTekst(doc.getAard());
		aard.setVisible(true);
		
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 2;
		
		documentPanel.add(aardBox,c);
		documentPanel.add(aard,c);
		
		//datum toegevoegd
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		documentPanel.add(new JLabelFactory().getNormaleTekst("Datum ingediend: "), c);
		
		c.gridx = 2;
		JTextField datumToegevoegd = new JTextField(20);
		datumToegevoegd.setText(document.getDatumToegevoegd().toString().substring(0,19));
		datumToegevoegd.setEditable(false);
		datumToegevoegd.setBorder(null);
		datumToegevoegd.setOpaque(false);
		datumToegevoegd.setForeground(Color.WHITE);
		documentPanel.add(datumToegevoegd, c);
		
		//datum gewijzigd
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		documentPanel.add(new JLabelFactory().getNormaleTekst("Laatste geaccep. wijz.: "), c);
				
		c.gridx = 2;
		JTextField datumGewijzigd = new JTextField(20);
		datumGewijzigd.setText(document.getDatumToegevoegd().toString().substring(0,19));
		datumGewijzigd.setEditable(false);
		datumGewijzigd.setBorder(null);
		datumGewijzigd.setOpaque(false);
		datumGewijzigd.setForeground(Color.WHITE);
		documentPanel.add(datumGewijzigd, c);
		
		//opmerkingen
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 2;
		JPanel opmerkingenPnl = new JPanel();
		opmerkingenPnl.setOpaque(false);
		opmerkingenPnl.setBorder(new JLabelFactory().getFieldSet("Opmerkingen"));
		documentPanel.add(opmerkingenPnl, c);
		
		MooiTextArea opmerkingen = new MooiTextArea(document.getOpmerkingen(),"Opmerkingen",new JLabelFactory().getItalic("").getFont());
		tekstvakken.add(opmerkingen);
		opmerkingen.setRows(5);
		opmerkingen.setColumns(28);
		opmerkingen.setFont(new JLabelFactory().getItalic("").getFont());
		opmerkingen.setEditable(false);
		opmerkingen.setOpaque(false);
		opmerkingen.setBorder(null);
		opmerkingen.setForeground(Color.WHITE);
		opmerkingen.setLineWrap(true);
		opmerkingen.setWrapStyleWord(true);
		JScrollPane opmerkingenScroll = new JScrollPane(opmerkingen);
		opmerkingenScroll.getViewport().setOpaque(false);
		opmerkingenScroll.getViewport().setBorder(null);
		opmerkingenScroll.setBorder(null);
		opmerkingenScroll.setOpaque(false);
		
		opmerkingenPnl.add(opmerkingenScroll);
		
		//media
		c.gridx = 3;
		c.gridy = 1;
		c.gridheight = 9;
		c.gridwidth = 2;
		
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
			media = new DocumentVideo(controller.getOrigineelDocument(),databank);
		}
		else if (doc.getTypeDocument().equals("Link"))
		{
			media = new DocumentLink(controller);
		}
		else if (doc.getTypeDocument().equals("Andere"))
		{
			media = new DocumentAndere(controller.getOrigineelDocument(),databank);	
		}
		else
		{
			//het is een nieuw document
			media = new TypeKiezer(controller, this, databank);
			setEditable(true);
			
			titel.setOpaque(true);
			titel.setEditable(true);
			titel.setForeground(Color.black);
			titel.setBorder(new JTextField().getBorder());
			
			opmerkingen.setOpaque(true);
			opmerkingen.setEditable(true);
			opmerkingen.setForeground(Color.black);
			opmerkingen.setBorder(new JTextArea().getBorder());
		}
		documentPanel.add((Component) media,c);
		
		//wijziging
		c.gridx = 3;
		c.gridy = 12;
		c.gridheight = 1;
		
		//beoordeling
		c.gridx = 3;
		c.gridy = 13;
		c.gridheight = 1;
		beoordeling = new Beoordeling(databank,model,document,hoofd,this, controller,tray);
		documentPanel.add(beoordeling,c);
	}
	
	//retourneert alle tekstvakken die bewerkbaar mogen gezet worden door (o.a.) Beoordeling
	public ArrayList<JTextComponent> getTekstvakken()
	{
		aardBox.setVisible(true);
		aard.setVisible(false);
		return tekstvakken;
	}
	public JComboBox getComboBox()
	{
		return aardBox;		
	}
	
	public void setEditable(boolean b)
	{
		media.setEditable(b);
		aard.setEnabled(b);
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
		c.gridheight= 9;
		
		setVisible(false); 
		documentPanel.remove((JPanel)media);
		media = dm; 
		documentPanel.add((JPanel)media,c);
		setVisible(true);
		
	}
}