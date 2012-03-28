package views;

/** De grijze doorzichtige balk, aan de rechterkant van het scherm in document-view.
 *   Hier worden andere documenten van het geselecteerde erfgoed getoond en extra opties gegeven zoals
 *   een document toevoegen aan het erfgoed, het maken van een pdf, ...
 **/

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

import java.awt.Color;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

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
	private JLabel overzicht, bewerken, verwijderen;
	private ErfgoedContent content;
	private ErfgoedController controller;
	

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
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				hoofd.laadOverzicht();
			}
		});
		
		add(overzicht);
		
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
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(erfgoed,model, databank),hoofd));
			}
		});
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
		verwijderen.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/verwijderen.png")));
		verwijderen.addMouseListener(new MouseListener()
		{		
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
				if (JOptionPane.showConfirmDialog(null, "Als u dit erfgoed verwijderd worden alle bijhorende documenten ook verwijderd. Weet u zeker dat u dit wilt doen?", "Opgelet!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				{
					controller.verwijder();
					hoofd.laadOverzicht();
				}
			}
		});
		add(verwijderen);
		
		bewerken = new JLabel();
		bewerken.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/bewerken.png")));
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
				String[] s = content.bewerken();
				if (s[0]==null)
					bewerken.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/opslaan.png")));
				else
				{
					controller.getVoorlopigErfgoed().setNaam(s[0]);
					controller.getVoorlopigErfgoed().setStraat(s[1]);
					controller.getVoorlopigErfgoed().setHuisnr(s[2]);
					controller.getVoorlopigErfgoed().setPostcode(s[3]);
					controller.getVoorlopigErfgoed().setDeelgemeente(s[4]);
					controller.getVoorlopigErfgoed().setOmschrijving(s[5]);
					controller.getVoorlopigErfgoed().setNuttigeInfo(s[6]);
					controller.getVoorlopigErfgoed().setKenmerken(s[7]);
					controller.getVoorlopigErfgoed().setGeschiedenis(s[8]);
					controller.getVoorlopigErfgoed().setTypeErfgoed(s[9]);
					controller.update();
					bewerken.setIcon(new ImageIcon(getClass().getResource("../guiElementen/imgs/bewerken.png")));					
				}
			}
		});
		add(bewerken);
	}
}