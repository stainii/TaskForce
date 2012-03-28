package views;

/** De grijze doorzichtige balk, aan de rechterkant van het scherm in document-view.
 *   Hier worden andere documenten van het geselecteerde erfgoed getoond en extra opties gegeven zoals
 *   een document toevoegen aan het erfgoed, het maken van een pdf, ...
 **/

import guiElementen.DocumentThumbnail;
import guiElementen.JLabelFactory;

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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controllers.Databank;
import controllers.PdfMaker;

import model.DocumentCMS;
import model.Model;

@SuppressWarnings("serial")
public class DocumentMenu extends JPanel
{	
	//achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/transparantGrijs.png"));
	private Image background = backgroundIcon.getImage();
	
	private JLabel toevoegen, pdf;
	private Model model;
	private Databank databank;
	private Hoofd hoofd;
	private DocumentCMS document;
	private DocumentContent content;
	private JLabel overzicht, erfgoed;
	

	@Override
	protected void paintComponent(Graphics g)
	{		
		//achtergrond tekenen
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public DocumentMenu(Model m, Databank d, DocumentCMS doc, Hoofd h, DocumentContent cont)
	{
		this.model = m;
		this.databank = d;
		this.hoofd = h;
		this.document = doc;
		this.content = cont;
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
				content.quit();
				hoofd.laadOverzicht();
			}
		});
		
		add(overzicht);
		
		//terug button
		erfgoed =new JLabelFactory().getMenuTitel("Bekijk erfgoedfiche");
		//terug.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
		erfgoed.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0){}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				//terug.setIcon(new ImageIcon(getClass().getResource("imgs/terug.png")));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) 
			{
				//terug.setIcon(new ImageIcon(getClass().getResource("imgs/terug_hover.png")));
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				content.quit();
				hoofd.setContentPaneel(new ErfgoedView(model,databank,document.getErfgoed(),hoofd));
			}
		});
		add(erfgoed);
		
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
				content.quit();
				hoofd.setContentPaneel(new DocumentView(model,databank,new DocumentCMS(document.getErfgoed(),model, databank),hoofd));
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
				
				new PdfMaker(document.getErfgoed(), model, file);
				
				try
				{
					Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+ file.getAbsolutePath());
				}
				catch (IOException ioe){}
			}
		});
		add(pdf);
				
		//toon andere documenten van het erfgoed
		add(new JLabelFactory().getMenuTitel("Andere documenten"));
	
		boolean gevonden = false;
		for (DocumentCMS document : doc.getErfgoed().getDocumenten())
		{
			if (!doc.equals(document))
			{
				add(new DocumentThumbnail(model, d, document, hoofd, content));
				gevonden = true;
			}
		}
		
		if (!gevonden)
			add(new JLabelFactory().getNormaleTekst("<html>Er zijn geen andere documenten<br />voor dit erfgoed opgeladen</html>"));
	}
}
