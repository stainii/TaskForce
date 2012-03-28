package speeltuin;

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import views.DocumentView;
import views.Hoofd;


import controllers.Databank;

import model.Burger;
import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

public class ErfgoedPanel extends JPanel
{	
	private Model model;
	private Databank databank;
	private Erfgoed erfgoed;
	private Burger burger;
	private Hoofd hoofd;
	private String[] types;
	private JLabel toevoegen, pdf, groei, verklein;

	public ErfgoedPanel(Model m, Databank d, Erfgoed e, Hoofd h)
	{
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		this.model = m;
		this.databank = d;
		this.erfgoed = e;
		this.hoofd = h;
		
		types = new String[5];
		types[0] = "Landschappen";
		types[1] = "Dorpsgezichten";
		types[2] = "Gebouwen";
		types[3] = "Archeologische sites";
		types[4] = "Andere";
		
		//toonGroot();
	}
	
	public void toonKlein()
	{
		removeAll();
		setPreferredSize(new Dimension(1000,210));
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
		//tekstvakken.add(titel);
		titel.setText(erfgoed.getNaam());
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
		add(new JLabelFactory().getNormaleTekst(erfgoed.getEigenaar().getGebruikersnaam() + " - " + erfgoed.getEigenaar().getNaam()),c);
				
		//locatie
		JPanel locatie = new JPanel(new GridBagLayout());
		locatie.setOpaque(false);
		
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=6;
		locatie.add(new JLabelFactory().getMenuTitel("Locatie: "),c);
		
		c.gridx=1;
		c.gridy=5;
		c.gridwidth=3;
		JTextField straat = new JTextField(25);
		//tekstvakken.add(plaats);
		straat.setText(erfgoed.getStraat());
		straat.setEditable(false);
		straat.setBorder(null);
		straat.setOpaque(false);
		straat.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		straat.setFocusable(false);
		locatie.add(straat,c);
		
		c.gridx=4;
		c.gridwidth=2;
		JTextField huisNr = new JTextField(4);
		//tekstvakken.add(plaats);
		huisNr.setText(erfgoed.getHuisnr());
		huisNr.setEditable(false);
		huisNr.setBorder(null);
		huisNr.setOpaque(false);
		huisNr.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		huisNr.setFocusable(false);
		locatie.add(huisNr,c);
		
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=1;
		JTextField postcode = new JTextField(4);
		//tekstvakken.add(plaats);
		postcode.setText(erfgoed.getPostcode());
		postcode.setEditable(false);
		postcode.setBorder(null);
		postcode.setOpaque(false);
		postcode.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		postcode.setFocusable(false);
		locatie.add(postcode,c);
		
		c.gridx=2;
		c.gridwidth = 5;
		JTextField deelgemeente = new JTextField(25);
		//tekstvakken.add(plaats);
		deelgemeente.setText(erfgoed.getDeelgemeente());
		deelgemeente.setEditable(false);
		deelgemeente.setBorder(null);
		deelgemeente.setOpaque(false);
		deelgemeente.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		deelgemeente.setFocusable(false);
		locatie.add(deelgemeente,c);
		
		c.gridx=1;
		c.gridy=7;
		c.gridwidth = 6;
		JTextArea omschrijving = new JTextArea(3,30);
		//tekstvakken.add(plaats);
		omschrijving.setText(erfgoed.getOmschrijving());
		omschrijving.setEditable(false);
		omschrijving.setBorder(null);
		omschrijving.setOpaque(false);
		omschrijving.setForeground(Color.WHITE);
		//plaats.addFocusListener(this);
		omschrijving.setFocusable(false);
		locatie.add(omschrijving,c);
		
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=6;
		c.gridheight=5;
		add(locatie,c);
		
		
		/**RECHTERKANT**/
		c.gridx=7;
		c.gridy=1;
		c.gridwidth=1;
		add(new JLabelFactory().getMenuTitel("Documenten"),c);
		
		JPanel documentenPanel = new JPanel(new GridBagLayout());
		documentenPanel.setOpaque(false);
		
		int x = 0;
		c.gridy=1;
		for (DocumentCMS doc: erfgoed.getDocumenten())
		{
			if (x<6)
			{
				x++;			
				c.gridx=x;
				documentenPanel.add(new DocumentThumbnail(model, databank, doc, null, null), c);
			}
		}
		
		c.gridx=7;
		c.gridy=2;
		c.gridwidth=2;
		c.gridheight=4;
		c.fill = GridBagConstraints.VERTICAL;
		add(documentenPanel,c);
		
		//toevoegbutton
		c.gridx=7;
		c.gridy=7;
		c.gridwidth=1;
		c.gridheight=1;
		toevoegen = new JLabelFactory().getMenuTitel("Document toevoegen");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new MouseListener() {
					
		@Override
		public void mouseReleased(MouseEvent e) {}
			
		@Override
		public void mousePressed(MouseEvent e) {}
			
		@Override
		public void mouseExited(MouseEvent e)
		{
			toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
		}
			
		@Override
		public void mouseEntered(MouseEvent e)
		{
			toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco_hover.png")));
		}
			
		@Override
		public void mouseClicked(MouseEvent e)
		{
			//content.quit();
				
			hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(erfgoed,model, databank, burger),hoofd));
		}
		});
		add(toevoegen,c);
				
		//pdf button
		c.gridx=8;
		c.gridy=7;
		c.gridwidth=1;
		c.gridheight=1;
		
		pdf = new JLabelFactory().getMenuTitel("Pdf maken");
		pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf_zwartwit.png")));
		pdf.addMouseListener(new MouseListener() {
					
			@Override
			public void mouseReleased(MouseEvent e) {}
					
			@Override
			public void mousePressed(MouseEvent e) {}
					
			@Override
			public void mouseExited(MouseEvent e)
			{
				pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf_zwartwit.png")));
			}
					
			@Override
			public void mouseEntered(MouseEvent e)
			{
				pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf.png")));
			}
					
			@Override
			public void mouseClicked(MouseEvent e)
			{			
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				File file = new File("");
						
				int resul = chooser.showSaveDialog(null);
				if(resul == JFileChooser.APPROVE_OPTION)
				{
					file = chooser.getSelectedFile();
				}
				
				//new PdfMaker(erfgoed, model, file);
				
				try
				{
					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ file.getAbsolutePath());
				}
				catch (IOException ioe){}
			}
		});
		add(pdf,c);
		
		
		//expandbutton
		c.gridx=1;
		c.gridy=100;
		c.gridwidth=100;
		c.gridheight=1;
		c.anchor =  GridBagConstraints.CENTER;
		groei = new JLabel();
		groei.setIcon(new ImageIcon(getClass().getResource("../views/imgs/groei.png")));
		groei.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent e){}
			
			@Override
			public void mousePressed(MouseEvent e){}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				groei();
			}
		});
		add(groei, c);
	}
	
	public void toonGroot()
	{
		removeAll();
		setPreferredSize(new Dimension(1000,500));
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
		//tekstvakken.add(titel);
		titel.setText(erfgoed.getNaam());
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
		//tekstvakken.add(plaats);
		straat.setText(erfgoed.getStraat());
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
		huisNr.setText(erfgoed.getHuisnr());
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
		postcode.setText(erfgoed.getPostcode());
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
		deelgemeente.setText(erfgoed.getDeelgemeente());
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
		omschrijving.setText(erfgoed.getOmschrijving());
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
		nuttigeInfo.setText(erfgoed.getNuttigeInfo());
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
		kenmerken.setText(erfgoed.getKenmerken());
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
		geschiedenis.setText(erfgoed.getGeschiedenis());
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
			documentenPanel.add(new DocumentThumbnail(model, databank, doc, null, null), c);
		}
		
		c.gridx=7;
		c.gridy=3;
		c.gridwidth=2;
		c.gridheight=10;
		c.fill = GridBagConstraints.VERTICAL;
		add(documentenPanel,c);
		
		//toevoegbutton
		c.gridx=7;
		c.gridy=11;
		c.gridwidth=1;
		c.gridheight=1;
			toevoegen = new JLabelFactory().getMenuTitel("Document toevoegen");
			toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
			toevoegen.addMouseListener(new MouseListener() {
						
			@Override
			public void mouseReleased(MouseEvent e) {}
				
			@Override
			public void mousePressed(MouseEvent e) {}
				
			@Override
			public void mouseExited(MouseEvent e)
			{
				toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco.png")));
			}
				
			@Override
			public void mouseEntered(MouseEvent e)
			{
				toevoegen.setIcon(new ImageIcon(getClass().getResource("../views/imgs/toevoegenIco_hover.png")));
			}
				
			@Override
			public void mouseClicked(MouseEvent e)
			{
				//content.quit();
					
				hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(erfgoed,model, databank, burger),hoofd));
			}
			});
			add(toevoegen,c);
					
			//pdf button
			c.gridx=8;
			c.gridy=11;
			c.gridwidth=1;
			c.gridheight=1;
			
			pdf = new JLabelFactory().getMenuTitel("Pdf maken");
			pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf_zwartwit.png")));
			pdf.addMouseListener(new MouseListener() {
						
				@Override
				public void mouseReleased(MouseEvent e) {}
						
				@Override
				public void mousePressed(MouseEvent e) {}
						
				@Override
				public void mouseExited(MouseEvent e)
				{
					pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf_zwartwit.png")));
				}
						
				@Override
				public void mouseEntered(MouseEvent e)
				{
					pdf.setIcon(new ImageIcon(getClass().getResource("../views/imgs/pdf.png")));
				}
						
				@Override
				public void mouseClicked(MouseEvent e)
				{			
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					
					File file = new File("");
							
					int resul = chooser.showSaveDialog(null);
					if(resul == JFileChooser.APPROVE_OPTION)
					{
						file = chooser.getSelectedFile();
					}
					
					//new PdfMaker(erfgoed, model, file);
					
					try
					{
						Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ file.getAbsolutePath());
					}
					catch (IOException ioe){}
				}
			});
			add(pdf,c);
		
		
		//verkleinbutton
		c.gridx=1;
		c.gridy=100;
		c.gridwidth=100;
		c.gridheight=1;
		c.anchor =  GridBagConstraints.CENTER;
		verklein = new JLabel();
		verklein.setIcon(new ImageIcon(getClass().getResource("../views/imgs/verklein.png")));
		verklein.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent e){}
			
			@Override
			public void mousePressed(MouseEvent e){}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				verklein();
			}
		});
		add(verklein, c);
		
	}
	
	public void groei()
	{
		setPreferredSize(new Dimension(1000,600));
		toonGroot();
	}
	public void verklein()
	{
		setPreferredSize(new Dimension(1000,210));
		toonKlein();
	}
	
	public static void main(String args[])
	{
		JFrame f = new JFrame();
		Model m = new Model();
		Databank d = new Databank(m);
		d.laadDatabank();
		
		f.add(new ErfgoedPanel(m,d, m.getErfgoed().get(0),null));
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
}
