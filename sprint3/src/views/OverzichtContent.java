package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import guiElementen.JLabelFactory;
import guiElementen.RijDocument;
import guiElementen.RijErfgoed;
import guiElementen.TegelDocument;
import guiElementen.TegelErfgoed;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import controllers.Databank;
import controllers.OverzichtDocumentenController;
import controllers.OverzichtErfgoedController;
import model.Erfgoed;
import model.Model;

/**Toont alle tegels of rijen. Is de linkerkant van OverzichtView.
 * 
 * 
 * Systeem:
 * 		1) De te tonen view (tegelView of lijstView) wordt bijgehouden in de String view
 * 		2) We hebben 2 panelen: docPanel en scrollPanel. DocPanel bevat de tegels/rijen, scrollPanel
 * 		   zorgt voor de nummering van de pagina's.
 * 		3) De methode bepaalAantalOpScherm() berekent hoeveel tegels/rijen er op 1 pagina kunnnen. De methode
 * 		   bepaalAantalPaginas() berekent op basis daarvan hoeveel pagina's er nodig zullen zijn om de tegels/rijen
 * 		   te kunnen weergeven.
 * 		4) De content die geladen moet worden zit in de OverzichtController controller, het attribuut inTeLaden.
 * 		   Deze ArrayList wordt gevuld door OverzichtMenu. In deze klasse moeten we gewoon deze ArrayList inlezen,
 * 		   niet de gegevens aanpassen etc.
 * 		5) Tijd om content te tonen! Dit doen we met de methode toonContent(aantalTegels). Deze roept ook de methode
 * 		   toonScrollbar(aantalTegels) aan.
 * 			5a) toonScrollbar zorgt voor het juist afhandelen van de paginanummering. Er zijn een hoop situaties
 * 				waar rekening mee gehouden moet worden. Bv. als het scherm vergroot, en er dus meer tegels op het scherm
 * 				kunnen, dan moet je nog steeds op de juiste pagina belanden, ongeveer op het punt waar je zat voor de vergroting.
 * 			 	Op de minimumgrootte van het scherm kunnen nrs 1-30 getoond worden. Zijn er meer dan 30 pagina's, dan
 * 				moet pagina 1->9 ... huidigePag-2 -> huidigePag+2 .. max-9 -> max getoond worden. Maar als de huidige pagina
 * 				tot een van de eerste 9 of laatste 9 behoort, moet hij 1->9 ... max-9 -> max tonen.
 * 			5b) toonContent mag nu de rest van het werk doen. Hij zal alle tegels/rijen die op het scherm staan verwijderen
 * 				en de nieuwe erop zetten. Hij begint vanaf de ondergrens (ingesteld door toonScrollbar) en gaat door tot
 * 				het aantal tegels/rijen dat op het scherm kunnen, of tot de documenten op zijn (laatste pagina).
 * 		6) Iedere keer als er iets verandert in het model, of als het scherm van grootte verandert, wordt toonContent()
 * 		   opnieuw uitgevoerd. Als het scherm van grootte verandert wordt ook opnieuw gekeken hoeveel tegels/rijen er
 *         op het scherm kunnen. 
 *      7) Als typeContent "documenten" is, dan worden tegels/rijen voor documenten ingeladen. Anders worden tegels/rijen voor
 *         erfgoed ingeladen.
 * **/

@SuppressWarnings("serial")
public class OverzichtContent extends JPanel implements ComponentListener, ChangeListener
{
	private OverzichtDocumentenController controller1;
	private OverzichtErfgoedController controller2;
	private JPanel docPanel, scrollPanel;
	private JLabel documentenTitel, erfgoedTitel, documentToevoegen, erfgoedToevoegen;
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private int ondergrens;
	private int huidigePagina;
	private String view;
	private String typeContent;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);	

	public OverzichtContent(Model m, Databank d, Hoofd h, OverzichtDocumentenController c1, OverzichtErfgoedController c2)
	{
		this.model = m;
		this.databank = d;
		this.hoofd = h;
		this.controller1 = c1;
		this.controller2 = c2;
		this.view = "TegelView";
		this.typeContent = m.getBeheerder().getTypeContent();
		
		
		addComponentListener(this);
				
		controller1.addListener(this);
		controller2.addListener(this);
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		//titels en toevoegenlbl
		JPanel bovenkant = new JPanel(new GridBagLayout());
		bovenkant.setOpaque(false);
		
		JPanel typeContentKiezer = new JPanel();
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		typeContentKiezer.setLayout(f);
		typeContentKiezer.setOpaque(false);
		
		documentenTitel = new JLabelFactory().getTitel("   Documenten");
		documentenTitel.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (typeContent.equals("Documenten"))
					((JLabel)e.getSource()).setForeground(Color.WHITE);
				else
					((JLabel)e.getSource()).setForeground(new Color(120,120,120));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				documentenTitel.setCursor(hand);
				if (typeContent.equals("Erfgoed"))
					((JLabel)e.getSource()).setForeground(new Color(200,200,200));
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				setTypeContent("Documenten");
				documentenTitel.setForeground(Color.WHITE);
				erfgoedTitel.setForeground(new Color(120,120,120));
			}
		});
		
		erfgoedTitel = new JLabelFactory().getTitel(" Erfgoed");
		erfgoedTitel.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (typeContent.equals("Erfgoed"))
					((JLabel)e.getSource()).setForeground(Color.WHITE);
				else
					((JLabel)e.getSource()).setForeground(new Color(120,120,120));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				erfgoedTitel.setCursor(hand);
				if (typeContent.equals("Documenten"))
					((JLabel)e.getSource()).setForeground(new Color(200,200,200));
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				setTypeContent("Erfgoed");
				erfgoedTitel.setForeground(Color.WHITE);
				documentenTitel.setForeground(new Color(120,120,120));
			}
		});
		
		if (typeContent.equals("Documenten"))
			erfgoedTitel.setForeground(new Color(120,120,120));			
		else
			documentenTitel.setForeground(new Color(120,120,120));
		
		typeContentKiezer.add(erfgoedTitel, BorderLayout.NORTH);
		typeContentKiezer.add(documentenTitel, BorderLayout.NORTH);
		
		erfgoedToevoegen = new JLabelFactory().getMenuTitel("Erfgoed toevoegen");
		erfgoedToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		erfgoedToevoegen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				erfgoedToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				erfgoedToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
				erfgoedToevoegen.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
				hoofd.setContentPaneel(new ErfgoedView(model,databank,new Erfgoed(model),hoofd));
			}
		});
		
		documentToevoegen = new JLabelFactory().getMenuTitel("Document toevoegen");
		documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		documentToevoegen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
				documentToevoegen.setCursor(hand);
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				setTypeContent("Erfgoed");
				documentenTitel.setForeground(new Color(120,120,120));
				erfgoedTitel.setForeground(Color.WHITE);
				documentToevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
			}
		});
		documentToevoegen.setVisible(false);
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		bovenkant.add(typeContentKiezer, c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0,10,0,10);
		bovenkant.add(erfgoedToevoegen, c);
		
		c.gridx = 2;
		bovenkant.add(documentToevoegen, c);
		
		add(bovenkant,BorderLayout.NORTH);
		
		
		//paneel met de documenten
		docPanel = new JPanel();
		docPanel.setOpaque(false);
		add(docPanel, BorderLayout.CENTER);
		
		FlowLayout fl =new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		docPanel.setLayout(fl);
		setVisible(true);
		
		//nummering
		scrollPanel = new JPanel();
		scrollPanel.setLayout(new FlowLayout());
		scrollPanel.setOpaque(false);
		scrollPanel.setPreferredSize(new Dimension(0,22));
		add(scrollPanel, BorderLayout.SOUTH);
			
		if(model.getBeheerder().getView().equals(""))		//defaultwaarde wanneer er geen instellingen zijn
			setView("TegelView");
		if(model.getBeheerder().getView().equals("TegelView"))
			setView("TegelView");
		if(model.getBeheerder().getView().equals("LijstView"))
			setView("LijstView");
		
		//TypeContent instellingen
		if(model.getBeheerder().getTypeContent().equals(""))
			setTypeContent("Erfgoed");
		if(model.getBeheerder().getTypeContent().equals("Erfgoed"))
		{
			setTypeContent("Erfgoed");
		}
		if(model.getBeheerder().getTypeContent().equals("Documenten"))
		{
			setTypeContent("Documenten");
		}
	}
	public String getTypeContent() {
		return typeContent;
	}
	public void setTypeContent(String string)
	{
		typeContent = string;
		model.notifyListeners();
		
		if(string.equals("Erfgoed"))
		{
			erfgoedTitel.setForeground(Color.WHITE);
			documentenTitel.setForeground(new Color(120,120,120));
		}
		else if(string.equals("Documenten"))
		{
			documentenTitel.setForeground(Color.WHITE);
			erfgoedTitel.setForeground(new Color(120,120,120));
		}
		
		if (model.getBeheerder().KanToevoegen())
		{
			if (string.equals("Documenten"))
			{
				documentToevoegen.setVisible(true);
				erfgoedToevoegen.setVisible(false);
			}
			else
			{
				documentToevoegen.setVisible(false);
				erfgoedToevoegen.setVisible(true);
			}
		}
	}
	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
		model.notifyListeners();
	}

	public int getOndergrens() {
		return ondergrens;
	}
	public void setOndergrens(int ondergrens) {
		this.ondergrens = ondergrens;
	}
	public void toonContent(int aantalTegels)
	{
		toonScrollbar(aantalTegels);
		if (aantalTegels >0)
		{
			docPanel.setVisible(false);
			docPanel.removeAll();
			
			if (view.equals("TegelView"))
			{
				FlowLayout f = new FlowLayout();
				f.setAlignment(FlowLayout.LEFT);
				docPanel.setLayout(f);
			}
			else
			{
				docPanel.setLayout(new GridLayout(bepaalAantalOpScherm(),1));
			}
			
			
			if (typeContent.equals("Documenten"))
			{
				for (int i=ondergrens;i<ondergrens+aantalTegels && i<controller1.getInTeLaden().size() ;i++)
				{
					if (view.equals("TegelView"))
					{
						docPanel.add(new TegelDocument(model,databank,controller1.getInTeLaden().get(i),hoofd));
					}
					else if (view.equals("LijstView"))
					{
						docPanel.add(new RijDocument(model,databank,controller1.getInTeLaden().get(i),hoofd));
					}
				}
			}
			else
			{
				for (int i=ondergrens;i<ondergrens+aantalTegels && i<controller2.getInTeLaden().size() ;i++)
				{
					if (view.equals("TegelView"))
					{
						docPanel.add(new TegelErfgoed(model,databank,controller2.getInTeLaden().get(i),hoofd));
					}
					else if (view.equals("LijstView"))
					{
						docPanel.add(new RijErfgoed(model,databank,controller2.getInTeLaden().get(i),hoofd));
					}
				}				
			}
			
			docPanel.setVisible(true);
		}
	}
	
	public void toonScrollbar(int aantalTegels)
	{
		if (aantalTegels >0 )
		{
			//als er gefilterd wordt, en het aantal pagina's vermindert, dan moet de ondergrens vermindert worden
			double aantalPaginas = bepaalAantalPaginas();
			
			while(ondergrens>=aantalPaginas*aantalTegels)		//ondergrens is de eerste tegel die getoond wordt
				ondergrens-=aantalTegels;
			if (ondergrens<0)	//dit kan alleen als er GEEN documenten in de databank zitten
				ondergrens = 0;
			
			scrollPanel.removeAll();
			
			if (aantalPaginas<=30)
			{
				for (int i=1;i<=aantalPaginas;i++)
				{
					JLabel l = new JLabelFactory().getPaginaNummer(i);
					if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
					{
						l.setForeground(Color.RED);
					}
					else 
					{
						l.addMouseListener(new MouseListener()
						{
							@Override
							public void mouseReleased(MouseEvent arg0) {	}
							
							@Override
							public void mousePressed(MouseEvent arg0) {}
							
							@Override
							public void mouseExited(MouseEvent e)
							{
								((JLabel)(e.getSource())).setForeground(Color.GRAY);
							}
							
							@Override
							public void mouseEntered(MouseEvent e)
							{
								((JLabel)(e.getSource())).setForeground(Color.ORANGE);
							}
							
							@Override
							public void mouseClicked(MouseEvent e)
							{
								setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
							}
						});				
					}
					scrollPanel.add(l);
				}
			}
			
			
			else
			{
				if (huidigePagina<=8 || huidigePagina>=aantalPaginas-7)
				{
					for (int i=1;i<=10;i++)
					{
						JLabel l = new JLabelFactory().getPaginaNummer(i);
						if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
						{
							l.setForeground(Color.RED);
						}
						else 
						{
							l.addMouseListener(new MouseListener()
							{
								@Override
								public void mouseReleased(MouseEvent arg0) {	}
								
								@Override
								public void mousePressed(MouseEvent arg0) {}
								
								@Override
								public void mouseExited(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.GRAY);
								}
								
								@Override
								public void mouseEntered(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.ORANGE);
								}
								
								@Override
								public void mouseClicked(MouseEvent e)
								{
									setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
								}
							});				
						}
						scrollPanel.add(l);
					}
					
					scrollPanel.add(new JLabelFactory().getPaginaNummerBlank());
					
					for (int i=(int) (aantalPaginas-9);i<=aantalPaginas;i++)
					{
						JLabel l = new JLabelFactory().getPaginaNummer(i);
						if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
						{
							l.setForeground(Color.RED);
						}
						else 
						{
							l.addMouseListener(new MouseListener()
							{
								@Override
								public void mouseReleased(MouseEvent arg0) {	}
								
								@Override
								public void mousePressed(MouseEvent arg0) {}
								
								@Override
								public void mouseExited(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.GRAY);
								}
								
								@Override
								public void mouseEntered(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.ORANGE);
								}
								
								@Override
								public void mouseClicked(MouseEvent e)
								{
									setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
								}
							});				
						}
						scrollPanel.add(l);
					}
					
				}

				else
				{	
					for (int i=1;i<=5;i++)
					{
						JLabel l = new JLabelFactory().getPaginaNummer(i);
						if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
						{
							l.setForeground(Color.RED);
						}
						else 
						{
							l.addMouseListener(new MouseListener()
							{
								@Override
								public void mouseReleased(MouseEvent arg0) {	}
								
								@Override
								public void mousePressed(MouseEvent arg0) {}
								
								@Override
								public void mouseExited(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.GRAY);
								}
								
								@Override
								public void mouseEntered(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.ORANGE);
								}
								
								@Override
								public void mouseClicked(MouseEvent e)
								{
									setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
								}
							});				
						}
						scrollPanel.add(l);
					}
					
					scrollPanel.add(new JLabelFactory().getPaginaNummerBlank());
					
					for (int i=(huidigePagina-2);i<=(huidigePagina+2);i++)
					{
						JLabel l = new JLabelFactory().getPaginaNummer(i);
						if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
						{
							l.setForeground(Color.RED);
						}
						else 
						{
							l.addMouseListener(new MouseListener()
							{
								@Override
								public void mouseReleased(MouseEvent arg0) {	}
								
								@Override
								public void mousePressed(MouseEvent arg0) {}
								
								@Override
								public void mouseExited(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.GRAY);
								}
								
								@Override
								public void mouseEntered(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.ORANGE);
								}
								
								@Override
								public void mouseClicked(MouseEvent e)
								{
									setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
								}
							});				
						}
						scrollPanel.add(l);
					}
					
					scrollPanel.add(new JLabelFactory().getPaginaNummerBlank());
					
					for (int i=(int)(aantalPaginas-4);i<=aantalPaginas;i++)
					{
						JLabel l = new JLabelFactory().getPaginaNummer(i);
						if (ondergrens/aantalTegels+1 == i)	//als het de huidige pagina is
						{
							l.setForeground(Color.RED);
						}
						else 
						{
							l.addMouseListener(new MouseListener()
							{
								@Override
								public void mouseReleased(MouseEvent arg0) {	}
								
								@Override
								public void mousePressed(MouseEvent arg0) {}
								
								@Override
								public void mouseExited(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.GRAY);
								}
								
								@Override
								public void mouseEntered(MouseEvent e)
								{
									((JLabel)(e.getSource())).setForeground(Color.ORANGE);
								}
								
								@Override
								public void mouseClicked(MouseEvent e)
								{
									setPagina(Integer.parseInt(((JLabel) (e.getSource())).getText()));
								}
							});				
						}
						scrollPanel.add(l);
					}
				}
				
			}
		}
	}
	
	public void setPagina(int paginaNr)
	{
		ondergrens = (paginaNr-1) * bepaalAantalOpScherm();
		huidigePagina = paginaNr;
		stateChanged(new ChangeEvent(this));
	}
	
	public int bepaalAantalPaginas()
	{
		if (typeContent.equals("Documenten"))
			return (int)(Math.ceil(((double)(controller1.getInTeLaden().size())) / ((double)bepaalAantalOpScherm())));
		else
			return (int)(Math.ceil(((double)(controller2.getInTeLaden().size())) / ((double)bepaalAantalOpScherm())));
	}
	
	public int bepaalAantalOpScherm()
	{
		return (view.equals("TegelView") ? (int) ( (double)(this.getWidth() / 300) * (double)(this.getHeight() / 150)) : (int) (this.getHeight() / 50) - 1);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {	}

	@Override
	public void componentMoved(ComponentEvent arg0) {	}

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		//als het scherm van grootte veranderd, wordt het aantal mogelijke tegels op 1 scherm berekent en de content herladen
		
		//bepaal de nieuwe ondergrens
		int aantalTegels = bepaalAantalOpScherm();
		ondergrens = huidigePagina*aantalTegels;
		
		toonContent(bepaalAantalOpScherm());
	}

	@Override
	public void componentShown(ComponentEvent arg0)	{	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		toonContent(bepaalAantalOpScherm());
	}
}