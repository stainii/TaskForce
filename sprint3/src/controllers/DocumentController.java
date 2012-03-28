package controllers;

import java.sql.Date;

import javax.swing.JOptionPane;

import model.DocumentCMS;
import model.Model;

/** Controller dat aanpassingen van het document regelt. Er wordt gewerkt met 2 documenten: een origineel
 * document (dat tevens het document in het model is, door referentiële integriteit) en een voorlopig document
 * Het idee is dat wijzigingen eerst in een voorlopig document worden toegepast, en uiteindelijk (bij het 
 * klikken op opslaan) ook doorgevoerd worden in het originele document.
 * Deze manier van werken staat toe dat meerdere panelen eenzelfde document kunnen aanpassen. */

public class DocumentController
{
	private DocumentCMS voorlopigDocument;
	private DocumentCMS origineelDocument;
	private Databank d;
	private Model m;
	
	public DocumentController(Model m, Databank d, DocumentCMS doc)
	{
		this.origineelDocument = doc;
		this.voorlopigDocument = new DocumentCMS(doc.getId(), doc.getTitel(), doc.getStatus(), doc.getDatumToegevoegd(), doc.isVerwijderd(), doc.getOpmerkingen(), doc.getTekst(), doc.getTypeDocument(), doc.getErfgoedId(),doc.getRedenAfwijzing(), doc.getDatumGewijzigd(), doc.getMediaId(), doc.getBurgerId(), m);
		//niet voorlopigDocument = origineelDoc, want dan heb je referentiële integriteit
		voorlopigDocument.setImage(doc.getImage());
		this.m = m;
		this.d = d;
	}
	
	public DocumentCMS getOrigineelDocument()
	{
		return origineelDocument;
	}
	public DocumentCMS getVoorlopigDocument()
	{
		return voorlopigDocument;
	}
	
	public void verwijder()
	{
		d.verwijderDocument(origineelDocument);
		m.verwijderDocument(origineelDocument);
	}
	
	public void update()
	{
		origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());
		origineelDocument.setLaatsteWijziging(voorlopigDocument);
		
		d.updateDocument(voorlopigDocument);
		JOptionPane.showMessageDialog(null, "Uw wijzigingen werden opgeslagen. Deze worden zichtbaar wanneer de gebruiker deze goedkeurt.", "Wijzigingen doorgevoerd", JOptionPane.INFORMATION_MESSAGE);
	}
	public void toevoegen()
	{
		origineelDocument.setId(voorlopigDocument.getId());
		origineelDocument.setTitel(voorlopigDocument.getTitel());
		origineelDocument.setStatus(voorlopigDocument.getStatus());
		origineelDocument.setDatumToegevoegd(voorlopigDocument.getDatumToegevoegd());
		origineelDocument.setVerwijderd(voorlopigDocument.isVerwijderd());
		origineelDocument.setOpmerkingen(voorlopigDocument.getOpmerkingen());
		origineelDocument.setTekst(voorlopigDocument.getTekst());
		origineelDocument.setTypeDocument(voorlopigDocument.getTypeDocument());
		origineelDocument.setErfgoedId(voorlopigDocument.getErfgoedId());
		origineelDocument.setImage(voorlopigDocument.getImage());
		origineelDocument.setRedenAfwijzing(voorlopigDocument.getRedenAfwijzing());
		origineelDocument.setMediaId(voorlopigDocument.getMediaId());
		origineelDocument.setBurgerId(voorlopigDocument.getBurgerId());
		
		origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());
		
		origineelDocument.setId(d.toevoegenDocument(origineelDocument));
		m.toevoegenDocument(origineelDocument);
	}
	
	public void goedkeuren()
	{
		origineelDocument.setStatus("Goedgekeurd");
		voorlopigDocument.setStatus("Goedgekeurd");
		origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());
		
		d.beoordeelDocument(origineelDocument,true);
	}
	public void afkeuren(String reden)
	{
		origineelDocument.setStatus("Afgekeurd");
		origineelDocument.setRedenAfwijzing(reden);
		voorlopigDocument.setStatus("Afgekeurd");
		voorlopigDocument.setRedenAfwijzing(reden);
		origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());
		
		d.beoordeelDocument(origineelDocument,false);
	}
}