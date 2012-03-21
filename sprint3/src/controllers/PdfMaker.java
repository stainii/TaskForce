package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import model.DocumentCMS;
import model.Model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**Maakt een fiche van een erfgoed in PDF-formaat **/

public class PdfMaker 
{ 
	private DocumentCMS document;
	private Font tussentitel = new Font(Font.FontFamily.COURIER,16,Font.ITALIC);
	private Model model;
	
	public PdfMaker(DocumentCMS document, Model m, File f)
	{
		this.document = document;
		this.model = m;
		
		try
		{
			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(f));
			doc.open();
			MetaData(doc);
			Header(doc);
			Content(doc);
			doc.close();
		}
		catch(DocumentException de)
		{
			de.printStackTrace();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
			
	}
	
	private void MetaData(Document doc)
	{
		doc.addTitle("Document Erfgoed");
		doc.addSubject("Informatie over bepaald erfgoed");
		doc.addKeywords("Erfgoed");
		doc.addAuthor("Gemeente Herzele");
		doc.addCreator("Gemeente Herzele");
	}
	
	private void Header(Document doc) throws DocumentException
	{
		Paragraph titel = new Paragraph();
		titel.add(new Paragraph(document.getErfgoed().getNaam(), new Font(Font.FontFamily.COURIER, 22, Font.BOLD )));
		legeLijn(titel,1);
		titel.add(new Paragraph("Document opgesteld door " + model.getBeheerder()+ " op " + new Date()));
		legeLijn(titel,1);
		doc.add(titel);
	}
	
	public void Content(Document doc) throws DocumentException, MalformedURLException, IOException
	{
		com.itextpdf.text.Image image = null;
			
		for(DocumentCMS c : document.getErfgoed().getDocumenten())
		{
			if(c.getTypeDocument().equals("Afbeelding"))
			{

				Paragraph afbeelding = new Paragraph();
				afbeelding.add(new Paragraph("Afbeelding", tussentitel));
				
				Paragraph opmerking = new Paragraph();
				opmerking.add(new Paragraph(c.getOpmerkingen(), new Font(Font.FontFamily.COURIER, 6,Font.ITALIC)));
				
				doc.add(afbeelding);
				float factor;
				
				if (c.getImage().getWidth()>c.getImage().getHeight())
				{
					factor = 300f / c.getImage().getWidth();
				}
				else
				{
					factor = 250f / c.getImage().getHeight();
				}
				image = com.itextpdf.text.Image.getInstance(c.getImage().getScaledInstance((int)(c.getImage().getWidth()*factor), (int)(c.getImage().getHeight()*factor), 0), null);
				
				doc.add(image);
				
				legeLijn(opmerking,2);
				doc.add(opmerking);
			}
			
			if(c.getTypeDocument().equals("Tekst"))
			{
				Paragraph tekst = new Paragraph();
				tekst.add(new Paragraph("Tekst",tussentitel));
				
				legeLijn(tekst,1);
				doc.add(tekst);
				
				Paragraph docTekst = new Paragraph();
				docTekst.add(new Paragraph(c.getTekst()));
				
				doc.add(docTekst);
			}
		}
	}

	private static void legeLijn(Paragraph paragraaf, int hoeveelLijnen) {
		for (int i = 0; i < hoeveelLijnen; i++) {
			paragraaf.add(new Paragraph(" "));
		}
	}

}
