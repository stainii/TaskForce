package guiElementen;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import views.DocumentContent;
import views.DocumentView;
import views.Hoofd;
import model.DocumentCMS;
import model.Model;
import controllers.Databank;
import controllers.DocumentController;
import controllers.mail.AfgekeurdMail;
import controllers.mail.GoedkeurMail;
import controllers.mail.MailSchool;
import controllers.mail.MailThuis;

/** Deel van de GUI waarin je een document goedkeurt of afkeurt. **/

@SuppressWarnings("serial")
public class Beoordeling extends JPanel 
{
	private JLabel nogNietBeoordeeld, goedkeuren, afwijzen, bewerken, verwijderen;
	private Hoofd hoofd;
	private Model model;
	private Databank databank;
	private DocumentContent documentContent;
	private DocumentController controller;
	private MailThuis mail;
	private ExecutorService ex;
	private RedenAfwijzing redenAfwijzing;
	
	public Beoordeling(Databank d, Model m , DocumentCMS doc, Hoofd h, DocumentContent dc, DocumentController cont)
	{
		this.hoofd = h;
		this.documentContent= dc;
		this.controller = cont;
		this.model = m;
		this.databank = d;
		
		//multithreading om de mail in achtergrond te versturen
		ex = Executors.newFixedThreadPool(1);
		
		nogNietBeoordeeld = new JLabel();
		goedkeuren = new JLabel();
		afwijzen = new JLabel();
		bewerken = new JLabel();
		verwijderen = new JLabel();
		
		nogNietBeoordeeld.setIcon(new ImageIcon(getClass().getResource("imgs/nogNietBeoordeeld.png")));
		goedkeuren.setIcon(new ImageIcon(getClass().getResource("imgs/goedkeuren.png")));
		afwijzen.setIcon(new ImageIcon(getClass().getResource("imgs/afkeuren.png")));
		bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
		verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		
		if(controller.getOrigineelDocument().getStatus().equals("Goedgekeurd"))
		{
			nogNietBeoordeeld.setVisible(false);
			goedkeuren.setIcon(new ImageIcon(getClass().getResource("imgs/goedgekeurd.png")));
		}
		if(controller.getOrigineelDocument().getStatus().equals("Afgekeurd"))
		{
			nogNietBeoordeeld.setVisible(false);
			afwijzen.setIcon(new ImageIcon(getClass().getResource("imgs/afgekeurd.png")));
		}
		if (doc.getTypeDocument().equals("Onbekend"))
		{
			nogNietBeoordeeld.setVisible(false);
			goedkeuren.setVisible(false);
			afwijzen.setVisible(false);
		}
		
		//als document afgekeurd is/wordt, verschijnt het panel RedenAfwijzing
		redenAfwijzing = new RedenAfwijzing(controller);
		redenAfwijzing.setReden(doc.getRedenAfwijzing());
		if (controller.getOrigineelDocument().getStatus().equals("Nog niet beoordeeld") || controller.getOrigineelDocument().getStatus().equals("Goedgekeurd"))
			redenAfwijzing.setVisible(false);
		else
			redenAfwijzing.setEditable(false);
		
		//als het type nog niet bepaald is (bij het maken van een nieuw document)
		if (doc.getTypeDocument().equals("Onbekend"))
		{
			verwijderen.setVisible(false);		
		}
		else
		{
			verwijderen.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					controller.verwijder();
					hoofd.laadOverzicht();
				}
			});
		}
		
		if (doc.getTypeDocument().equals("Onbekend"))
		{
			bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
			bewerken.addMouseListener(new MouseListener()
			{	
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e){}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{					
					controller.getVoorlopigDocument().setOpmerkingen(documentContent.getTekstvakken().get(2).getText());
					documentContent.setEditable(false);
					controller.toevoegen();
					//maak een nieuw DocumentView van het net gemaakte document
					hoofd.setContentPaneel(new DocumentView(model,databank,controller.getOrigineelDocument(),hoofd));
				}
			});
		}
		else
		{
			bewerken.addMouseListener(new MouseListener()
			{	
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e){}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					//Overloop alle tekstvakken van DocumentContent en maak ze bewerkbaar (bewerken).
					//Zijn ze al bewerkbaar? Sla dan hun inhoud op (opslaan).
					if (documentContent.getTekstvakken().get(0).isEditable())
					{
						for(int i = 0; i<documentContent.getTekstvakken().size();i++)
						{
							JTextComponent t= documentContent.getTekstvakken().get(i);
							t.setOpaque(false);
							t.setEditable(false);
							t.setForeground(Color.white);
							t.setBorder(null);
							
							switch (i)
							{
								case 0: controller.getVoorlopigDocument().setTitel(t.getText()); break;
								case 1: controller.getVoorlopigDocument().setOpmerkingen(t.getText()); break; 
							}
							
						}
						
						documentContent.setEditable(false);
						controller.update();
						hoofd.setContentPaneel(new DocumentView(model, databank, controller.getOrigineelDocument(), hoofd));
					}
					else
					{
						for(JTextComponent t : documentContent.getTekstvakken())
						{
							t.setOpaque(true);
							t.setEditable(true);
							t.setForeground(Color.black);
							t.setBorder(new JTextField().getBorder());
							t.setFocusable(true);
						}
						documentContent.setEditable(true);
						bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
					}
				}
			});
		}
		goedkeuren.addMouseListener(new MouseListener()
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
			public void mouseClicked(MouseEvent arg0)
			{
					nogNietBeoordeeld.setVisible(false);
					goedkeuren.setIcon(new ImageIcon(getClass().getResource("imgs/goedgekeurd.png")));
					afwijzen.setIcon(new ImageIcon(getClass().getResource("imgs/afkeuren.png")));
					
					controller.goedkeuren();

					//mail versturen
					mail = new MailThuis(controller.getOrigineelDocument().getEigenaar().getEmail(),"Document is goedgekeurd", controller.getOrigineelDocument(), new GoedkeurMail(controller.getOrigineelDocument()));
					ex.execute(mail);
					
					redenAfwijzing.setVisible(false);
			}
		});
		
		afwijzen.addMouseListener(new MouseListener()
		{	
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0){}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
					if (redenAfwijzing.isVisible())
					{
						nogNietBeoordeeld.setVisible(false);
						goedkeuren.setIcon(new ImageIcon(getClass().getResource("imgs/goedkeuren.png")));
						afwijzen.setIcon(new ImageIcon(getClass().getResource("imgs/afgekeurd.png")));
					
						controller.afkeuren(redenAfwijzing.getReden());
					
						mail = new MailThuis(controller.getOrigineelDocument().getEigenaar().getEmail(),"Document is afgekeurd", controller.getOrigineelDocument(), new AfgekeurdMail(controller.getOrigineelDocument(),redenAfwijzing));
						ex.execute(mail);
						
						redenAfwijzing.setEditable(false);
					}
					else
					{
						//nog vragen om een reden, vooraleer het document af te keuren
						nogNietBeoordeeld.setVisible(false);
						goedkeuren.setIcon(new ImageIcon(getClass().getResource("imgs/goedkeuren.png")));
						afwijzen.setIcon(new ImageIcon(getClass().getResource("imgs/afkeurenBevestigen.png")));
						redenAfwijzing.setVisible(true);
						redenAfwijzing.setEditable(true);
					}
			}
		});
		
		
		//alles adden aan de layout
		setLayout(new GridBagLayout());
		setOpaque(false);
		setBorder(new EmptyBorder(10,10,10,10));
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy=0;
		c.gridwidth=3;
		c.gridheight=1;
		add(nogNietBeoordeeld,c);
		
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=2;
		add(goedkeuren,c);
		
		c.gridx=1;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=2;
		add(afwijzen,c);
		
		c.gridx=2;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		add(bewerken,c);
		
		c.gridx=2;
		c.gridy=2;
		c.gridwidth=1;
		c.gridheight=1;
		add(verwijderen,c);
		
		c.gridx=0;
		c.gridy=3;
		c.gridwidth=3;
		c.gridheight=1;
		add(redenAfwijzing,c);
	}
}
