package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**Maakt een fiche van een erfgoed in PDF-formaat **/

public class PdfMaker 
{ 
	private Erfgoed erfgoed;
	private Font titelFont = new Font(Font.FontFamily.HELVETICA,18,Font.BOLD);
	private Font tussentitelFont = new Font(Font.FontFamily.HELVETICA,14,Font.NORMAL);
	private Font normaleFont = new Font(Font.FontFamily.HELVETICA,10,Font.NORMAL);
	private Model model;
	
	public PdfMaker(Erfgoed e, Model m, File f)
	{
		this.erfgoed = e;
		this.model = m;
		
		try
		{
			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(f));
			doc.open();
			metaData(doc);
			header(doc);
			content(doc);
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
	
	private void metaData(Document doc)
	{
		doc.addTitle(erfgoed.getNaam());
		doc.addSubject("Informatie over " + erfgoed.getNaam());
		doc.addKeywords("Erfgoed");
		doc.addAuthor("Erfgoedbank Gemeente Herzele");
		doc.addCreator("Erfgoedbank Gemeente Herzele");
	}
	
	private void header(Document doc) throws DocumentException
	{
		Paragraph titel = new Paragraph();
		titel.add(new Paragraph(erfgoed.getNaam(), titelFont));
		legeLijn(titel,1);
		titel.add(new Paragraph("Document opgesteld door " + model.getBeheerder().getNaam() + " op " + new Date()));
		legeLijn(titel,1);
		doc.add(titel);
	}
	
	public void content(Document doc) throws DocumentException, MalformedURLException, IOException
	{
		//informatie over erfgoed
		Paragraph e = new Paragraph();
		PdfPTable tabel = new PdfPTable(2);
		tabel.setWidthPercentage(100);
		tabel.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tabel.addCell(erfgoed.getTypeErfgoed());
		tabel.addCell(erfgoed.getBurger()!=null?erfgoed.getBurger().getNaam():erfgoed.getBeheerder().getNaam());
		tabel.addCell(erfgoed.getStraat() + " " + erfgoed.getHuisnr() + " \n"
						+ erfgoed.getPostcode() + " " + erfgoed.getDeelgemeente());
		tabel.addCell(erfgoed.getNuttigeInfo());
		tabel.addCell(erfgoed.getKenmerken());
		tabel.addCell(erfgoed.getGeschiedenis());
	
		e.add(tabel);
		legeLijn(e,2);
		doc.add(e);
		
		//alle documenten
		com.itextpdf.text.Image image = null;
			
		for(DocumentCMS c : erfgoed.getDocumenten())
		{
			Paragraph linkerkant = new Paragraph();
			Paragraph rechterkant = new Paragraph();
			
			linkerkant.add(new Paragraph(c.getTitel(), tussentitelFont));	
			linkerkant.add(new Paragraph((c.getBurger()!=null?c.getBurger().getNaam():c.getBeheerder().getNaam()), normaleFont));
			linkerkant.add(new Paragraph("Datum ingediend: " + c.getDatumToegevoegd().toString(), normaleFont));
			linkerkant.add(new Paragraph("Laatste wijziging: " + c.getDatumGewijzigd(), normaleFont));
			linkerkant.add(new Paragraph("Opmerkingen: " + c.getOpmerkingen(), normaleFont));
			
			
			PdfPTable docTabel = new PdfPTable(2);
			docTabel.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			docTabel.setWidthPercentage(100);
			docTabel.addCell(linkerkant);
			
			
			
			//rechterkant
			if (c.getTypeDocument().equals("Afbeelding"))
			{
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
				PdfPCell cel = new PdfPCell(image,false);
				cel.setBorder(Rectangle.NO_BORDER);
				docTabel.addCell(cel);
			}
			else if (c.getTypeDocument().equals("Tekst"))
			{
				PdfPCell cel = new PdfPCell(new Paragraph(c.getTekst(), normaleFont));
				cel.setBorder(Rectangle.NO_BORDER);
				docTabel.addCell(cel);
			}
			
			legeLijn(linkerkant,2);
			legeLijn(rechterkant,2);
			
			doc.add(docTabel);
		}
	}

	private static void legeLijn(Paragraph paragraaf, int hoeveelLijnen) {
		for (int i = 0; i < hoeveelLijnen; i++) {
			paragraaf.add(new Paragraph(" "));
		}
	}

}
