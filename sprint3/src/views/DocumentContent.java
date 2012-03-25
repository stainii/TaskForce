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
	private JLabel wijzigingMedia;
	private JPanel documentPanel;
	
	public DocumentContent(Model m, Databank db, Hoofd h, DocumentCMS doc)
	{
		this.model = m;
		this.databank = db;
		this.hoofd = h;
		this.controller = new DocumentController(m,db,doc);		
		this.document = doc;
		
		tekstvakken = new ArrayList<JTextComponent>();
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		//add(new ErfgoedPanel(model, databank, doc.getErfgoed(), hoofd), BorderLayout.NORTH);
		
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
		
		JTextField titel = new JTextField(20);
		tekstvakken.add(titel);
		titel.setText(document.getTitel());
		titel.setEditable(false);
		titel.setBorder(null);
		titel.setOpaque(false);
		titel.setForeground(Color.WHITE);
		titel.setFont(new JLabelFactory().getTitel("").getFont());
		documentPanel.add(titel,c);
		
		//wijziging titel
		if (document.getLaatsteWijziging() != null && !document.getLaatsteWijziging().getTitel().equals(document.getTitel()))
		{
			c.gridy = 2;
			documentPanel.add(new JLabelFactory().getWijziging("Wijziging: " + document.getLaatsteWijziging().getTitel()), c);
		}
		
		//naam eigenaar
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 4;
		documentPanel.add(new JLabelFactory().getNormaleTekst(doc.getEigenaar().getGebruikersnaam() + " - " + doc.getEigenaar().getNaam()),c);
		
		//beetje ruimte
		c.gridy =4;
		documentPanel.add(new JLabel("   "),c);
		
		//datum toegevoegd
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		documentPanel.add(new JLabelFactory().getNormaleTekst("Datum ingediend: "), c);
		
		c.gridx = 2;
		JTextField datumToegevoegd = new JTextField(20);
		//tekstvakken.add(datum);			//moet niet, de datum moet niet aangepast worden
		datumToegevoegd.setText(document.getDatumToegevoegd().toString().substring(0,19));
		datumToegevoegd.setEditable(false);
		datumToegevoegd.setBorder(null);
		datumToegevoegd.setOpaque(false);
		datumToegevoegd.setForeground(Color.WHITE);
		documentPanel.add(datumToegevoegd, c);
		
		//datum gewijzigd
		c.gridx = 1;
		c.gridy = 6;
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
		c.gridy = 7;
		c.gridwidth = 2;
		documentPanel.add(new JLabelFactory().getNormaleTekst("Opmerkingen: "), c);
		
		c.gridx= 1;
		c.gridy = 8 ;
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
		documentPanel.add(opmerkingen, c);
		
		if (document.getLaatsteWijziging() != null && !document.getLaatsteWijziging().getOpmerkingen().equals(document.getOpmerkingen()))
		{
			c.gridy = 9;
			
			JTextArea opmerkingenWijziging = new JTextArea(5,30);
			opmerkingenWijziging.setDocument(new CustomDocument(opmerkingen));	//zorgt voor de limiet van 255 tekens
			opmerkingenWijziging.setText("Wijziging: " + document.getLaatsteWijziging().getOpmerkingen());
			opmerkingenWijziging.setFont(new JLabelFactory().getWijziging("").getFont());
			opmerkingenWijziging.setEditable(false);
			opmerkingenWijziging.setBorder(null);
			opmerkingenWijziging.setOpaque(false);
			opmerkingenWijziging.setLineWrap(true);
			opmerkingenWijziging.setForeground(Color.RED);
			documentPanel.add(opmerkingenWijziging, c);
		}
		
		//media
		c.gridx = 3;
		c.gridy = 1;
		c.gridheight = 9;
		c.gridwidth = 2;
		
		if (doc.getTypeDocument().equals("Afbeelding"))
		{
			media = new DocumentAfbeelding(controller,databank,false);
		}
		else if (doc.getTypeDocument().equals("Tekst"))
		{
			media = new DocumentTekst(controller,false); 
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
		if (doc.getTypeDocument().equals("Afbeelding"))
		{
			if (document.getLaatsteWijziging() != null && document.getLaatsteWijziging().getMediaId()!=document.getMediaId())
			{
				wijzigingMedia = new JLabelFactory().getWijziging("Bekijk wijziging afbeelding");
				wijzigingMedia.addMouseListener(new MouseListener()
				{
					@Override
					public void mouseReleased(MouseEvent arg0) {}
					
					@Override
					public void mousePressed(MouseEvent arg0) {}
					
					@Override
					public void mouseExited(MouseEvent arg0) {}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {}
					
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if(wijzigingMedia.getText().equals("Bekijk wijziging afbeelding"))
						{
							setMedia(new DocumentAfbeelding(controller,databank,true));
							wijzigingMedia.setText("Bekijk huidige afbeelding");
						}
						else
						{
							setMedia(new DocumentAfbeelding(controller,databank,false));
							wijzigingMedia.setText("Bekijk wijziging afbeelding");
						}
					}
				});
				documentPanel.add(wijzigingMedia,c);
			}
		}
		else if (doc.getTypeDocument().equals("Tekst"))
		{
			if (document.getLaatsteWijziging() != null && !document.getLaatsteWijziging().getTekst().equals(document.getTekst()))
			{
				wijzigingMedia = new JLabelFactory().getWijziging("Bekijk wijziging tekst");
				wijzigingMedia.addMouseListener(new MouseListener()
				{
					@Override
					public void mouseReleased(MouseEvent arg0) {}
					
					@Override
					public void mousePressed(MouseEvent arg0) {}
					
					@Override
					public void mouseExited(MouseEvent arg0) {}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {}
					
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if(wijzigingMedia.getText().equals("Bekijk wijziging tekst"))
						{
							setMedia(new DocumentTekst(controller,true));
							wijzigingMedia.setText("Bekijk huidige tekst");
						}
						else
						{
							setMedia(new DocumentTekst(controller,false));
							wijzigingMedia.setText("Bekijk wijziging tekst");
						}
					}
				});
				documentPanel.add(wijzigingMedia,c);
			} 
		}
		else if (doc.getTypeDocument().equals("Video"))
		{
			if (document.getLaatsteWijziging() != null && document.getLaatsteWijziging().getMediaId()!=document.getMediaId())
			{
				wijzigingMedia = new JLabelFactory().getWijziging("Bekijk wijziging video");
				wijzigingMedia.addMouseListener(new MouseListener()
				{
					@Override
					public void mouseReleased(MouseEvent arg0) {}
					
					@Override
					public void mousePressed(MouseEvent arg0) {}
					
					@Override
					public void mouseExited(MouseEvent arg0) {}
					
					@Override
					public void mouseEntered(MouseEvent arg0) {}
					
					@Override
					public void mouseClicked(MouseEvent e)
					{
						setMedia(new DocumentVideo());						
					}
				});
				add(wijzigingMedia,c);
			}
		}
		
		//beoordeling
		c.gridx = 3;
		c.gridy = 13;
		c.gridheight = 1;
		beoordeling = new Beoordeling(databank,model,document,hoofd,this, controller);
		documentPanel.add(beoordeling,c);
		
		/**
		//vorige volgende knoppen
		c.gridx = 1;
		c.gridy = 13;
		c.gridwidth = 3;
		JPanel vorigeVolgendePanel = new JPanel(new GridLayout(1,2));
		vorigeVolgendePanel.setOpaque(false);
		documentPanel.add(vorigeVolgendePanel,c);
		
		JLabel vorige = new JLabelFactory().getNormaleTekst("Vorige");
		vorige.setHorizontalAlignment(JLabel.LEFT);
		JLabel volgende = new JLabelFactory().getNormaleTekst("Volgende");
		volgende.setHorizontalAlignment(JLabel.RIGHT);
		vorigeVolgendePanel.add(vorige);
		vorigeVolgendePanel.add(volgende);
		**/		
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
		c.gridheight= 9;
		
		setVisible(false); 
		documentPanel.remove((JPanel)media);
		media = dm; 
		documentPanel.add((JPanel)media,c);
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
}