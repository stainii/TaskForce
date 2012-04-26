package views;

/** De grijze doorzichtige balk, aan de rechterkant van het scherm in document-view.
 *   Hier worden andere documenten van het geselecteerde erfgoed getoond en extra opties gegeven zoals
 *   een document toevoegen aan het erfgoed, het maken van een pdf, ...
 **/

import guiElementen.JLabelFactory;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Databank;
import controllers.ErfgoedController;
import controllers.PdfMaker;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

@SuppressWarnings("serial")
public class ErfgoedMenu extends JPanel
{	
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/transparantGrijs.png"));
	private Image background = backgroundIcon.getImage();
	
	private JLabel toevoegen, pdf;
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private Erfgoed erfgoed; 
	private JLabel overzicht, bewerken, verwijderen, nieuwOpslaan;
	private ErfgoedContent content;
	private ErfgoedController controller;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);	

	@Override
	protected void paintComponent(Graphics g)
	{		
		//achtergrond tekenen
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public ErfgoedMenu(Model m, Databank d, Erfgoed e, Hoofd h, ErfgoedContent c)
	{
		this.model = m;
		this.databank = d;
		this.hoofd = h;
		this.erfgoed = e;
		this.content = c;
		controller = new ErfgoedController(m, d, e);
		
		setOpaque(false);
		setPreferredSize(new Dimension(200,0));
		setBorder(new EmptyBorder(15, 10, 0, 0) );
		
		FlowLayout f =new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
				
		//overzicht button
		overzicht =new JLabelFactory().getMenuTitel("Overzicht");
		overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
		overzicht.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) 
			{
				overzicht.setIcon(new ImageIcon(getClass().getResource("imgs/terug_hover.png")));
				overzicht.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				hoofd.laadOverzicht();
			}
		});
		
		add(overzicht);
		
		if (erfgoed.getId() != -1)//het is geen nieuw erfgoed
		{
			//toevoegbutton
			toevoegen = new JLabelFactory().getMenuTitel("Document toevoegen");
			toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
			toevoegen.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e)
				{
					toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
				}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
					toevoegen.setCursor(hand);
				}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(erfgoed,model, databank, model.getBeheerder().getId()),hoofd));
				}
			});
			
			if(m.getBeheerder().KanToevoegen() == false)
				toevoegen.setVisible(false);
			
			add(toevoegen);
		
			// pdf button
			pdf = new JLabelFactory().getMenuTitel("Pdf maken");
			pdf.setIcon(new ImageIcon(getClass().getResource("imgs/pdf_zwartwit.png")));
			pdf.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e)
				{
					pdf.setIcon(new ImageIcon(getClass().getResource("imgs/pdf_zwartwit.png")));
				}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					pdf.setIcon(new ImageIcon(getClass().getResource("imgs/pdf.png")));
					pdf.setCursor(hand);
				}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					
					File file = new File("");
					
					int resul = chooser.showSaveDialog(hoofd);
					if(resul == JFileChooser.APPROVE_OPTION)
					{
						file = chooser.getSelectedFile();
					}
					
					new PdfMaker(erfgoed, model, file);
					
					try
					{
						Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ file.getAbsolutePath());
					}
					catch (IOException ioe){}
				}
			});
			add(pdf);
			
			
			verwijderen = new JLabel();
			verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
			verwijderen.addMouseListener(new MouseListener()
			{		
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					verwijderen.setCursor(hand);
				}
				
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (JOptionPane.showConfirmDialog(null, "Als u dit erfgoed verwijderd worden alle bijhorende documenten ook verwijderd. Weet u zeker dat u dit wilt doen?", "Opgelet!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					{
						controller.verwijder();
						hoofd.laadOverzicht();
					}
				}
			});
			
			if(m.getBeheerder().KanVerwijderen() == false)
				verwijderen.setVisible(false);
			add(verwijderen);
			
			bewerken = new JLabel();
			bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
			bewerken.addMouseListener(new MouseListener()
			{	
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e){}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					bewerken.setCursor(hand);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					String[] s = content.bewerken();
					if (s!=null)
					{
						if (s[0]==null)
							bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
						else
						{
							controller.getVoorlopigErfgoed().setNaam(s[0]);
							controller.getVoorlopigErfgoed().setStraat(s[1]);
							controller.getVoorlopigErfgoed().setHuisnr(s[2]);
							controller.getVoorlopigErfgoed().setOmschrijving(s[3]);
							controller.getVoorlopigErfgoed().setNuttigeInfo(s[4]);
							controller.getVoorlopigErfgoed().setKenmerken(s[5]);
							controller.getVoorlopigErfgoed().setGeschiedenis(s[6]);
							controller.getVoorlopigErfgoed().setTypeErfgoed(s[7]);
							controller.getVoorlopigErfgoed().setPostcode(s[8]);
							controller.getVoorlopigErfgoed().setDeelgemeente(s[9]);
							controller.update();
							bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
						}
					}
				}
			});
			
			if(m.getBeheerder().KanWijzigen() == false)
				bewerken.setVisible(false);
			add(bewerken);
		}
		else //het is een nieuw erfgoed
		{
			nieuwOpslaan = new JLabel();
			nieuwOpslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
			nieuwOpslaan.addMouseListener(new MouseListener()
			{	
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e){}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					nieuwOpslaan.setCursor(hand);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					String[] s = content.bewerken();
					if (s!=null)
					{
						controller.getVoorlopigErfgoed().setNaam(s[0]);
						controller.getVoorlopigErfgoed().setStraat(s[1]);
						controller.getVoorlopigErfgoed().setHuisnr(s[2]);
						controller.getVoorlopigErfgoed().setOmschrijving(s[3]);
						controller.getVoorlopigErfgoed().setNuttigeInfo(s[4]);
						controller.getVoorlopigErfgoed().setKenmerken(s[5]);
						controller.getVoorlopigErfgoed().setGeschiedenis(s[6]);
						controller.getVoorlopigErfgoed().setTypeErfgoed(s[7]);
						controller.getVoorlopigErfgoed().setPostcode(s[8]);
						controller.getVoorlopigErfgoed().setDeelgemeente(s[9]);
						controller.toevoegen();
						
						hoofd.setContentPaneel(new ErfgoedView(model,databank,erfgoed,hoofd));
					}
				}
			});
			add(nieuwOpslaan);
		}
	}
}
