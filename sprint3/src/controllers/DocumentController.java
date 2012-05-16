package controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import systemTray.InSystemTray;

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
	private ExecutorService ex;
	private InSystemTray tray;
	
	public DocumentController(Model m, Databank d, DocumentCMS doc, InSystemTray tray)
	{
		this.origineelDocument = doc;
		this.voorlopigDocument = new DocumentCMS(doc.getId(), doc.getTitel(), doc.getStatus(), doc.getDatumToegevoegd(), doc.isVerwijderd(), doc.getOpmerkingen(), doc.getTekst(), doc.getTypeDocument(), doc.getExtensieDocument(), doc.getErfgoedId(),doc.getRedenAfwijzing(), doc.getDatumGewijzigd(), doc.getMediaId(), doc.getBurgerId(), doc.getBeheerderId(),doc.getAard(), m);
		//niet voorlopigDocument = origineelDoc, want dan heb je referentiële integriteit
		voorlopigDocument.setImage(doc.getImage());
		voorlopigDocument.setTemp(doc.getTemp());
		this.m = m;
		this.d = d;
		ex = Executors.newCachedThreadPool();
		this.tray = tray;
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
		origineelDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		voorlopigDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		origineelDocument.setLaatsteWijziging(voorlopigDocument);
		ex.execute(new Updaten());
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
		origineelDocument.setExtensieDocument(voorlopigDocument.getExtensieDocument());
		origineelDocument.setErfgoedId(voorlopigDocument.getErfgoedId());
		origineelDocument.setImage(voorlopigDocument.getImage());
		origineelDocument.setRedenAfwijzing(voorlopigDocument.getRedenAfwijzing());
		origineelDocument.setMediaId(voorlopigDocument.getMediaId());
		origineelDocument.setBurgerId(voorlopigDocument.getBurgerId());
		origineelDocument.setBeheerderId(voorlopigDocument.getBeheerderId());
		origineelDocument.setAard(voorlopigDocument.getAard());
		origineelDocument.setTemp(voorlopigDocument.getTemp());
		
		origineelDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		voorlopigDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		
		ex.execute(new Toevoegen());
	}
	
	public void goedkeuren()
	{
		origineelDocument.setStatus("Goedgekeurd");
		voorlopigDocument.setStatus("Goedgekeurd");
		origineelDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		voorlopigDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		ex.execute(new Beoordelen(true));		
	}
	public void afkeuren(String reden)
	{
		origineelDocument.setStatus("Afgekeurd");
		origineelDocument.setRedenAfwijzing(reden);
		voorlopigDocument.setStatus("Afgekeurd");
		voorlopigDocument.setRedenAfwijzing(reden);
		origineelDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		voorlopigDocument.setDatumGewijzigd(new Timestamp(new Date().getTime()));
		ex.execute(new Beoordelen(false));
	}
	
	private class Toevoegen extends SwingWorker<Void,Void>
	{

		@Override
		protected Void doInBackground()
		{
			tray.zegIets("Bezig met het toevoegen van uw document");
			origineelDocument.setId(d.toevoegenDocument(origineelDocument));
			m.toevoegenDocument(origineelDocument);
			tray.zegIets("Uw document is toegevoegd");
			return null;
		}
	}
	private class Updaten extends SwingWorker<Void,Void>
	{

		@Override
		protected Void doInBackground()
		{
			tray.zegIets("Bezig met het wijzigen van het " + voorlopigDocument.getTitel());
			if (voorlopigDocument.getBeheerder()!=null && voorlopigDocument.getBeheerder().equals(m.getBeheerder()))	//als de beheerder de eigenaar is van het document
			{
				d.updateDocument(voorlopigDocument,true);
				origineelDocument.setAard(voorlopigDocument.getAard());
				origineelDocument.setExtensieDocument(voorlopigDocument.getExtensieDocument());
				origineelDocument.setImage(voorlopigDocument.getImage());
				origineelDocument.setMediaId(voorlopigDocument.getMediaId());
				origineelDocument.setOpmerkingen(voorlopigDocument.getOpmerkingen());
				origineelDocument.setRedenAfwijzing(voorlopigDocument.getRedenAfwijzing());
				origineelDocument.setStatus(voorlopigDocument.getStatus());
				origineelDocument.setTekst(voorlopigDocument.getTekst());
				origineelDocument.setTemp(voorlopigDocument.getTemp());
				origineelDocument.setTitel(voorlopigDocument.getTitel());
				origineelDocument.setVerwijderd(voorlopigDocument.isVerwijderd());
				origineelDocument.setLaatsteWijziging(null);	
				tray.zegIets("Uw document "+ voorlopigDocument.getTitel()+" is gewijzigd");		
			}
			else
			{
				voorlopigDocument.setId(d.updateDocument(voorlopigDocument,false));
				origineelDocument.setLaatsteWijziging(voorlopigDocument);
				tray.zegIets("Uw document "+ voorlopigDocument.getTitel()+" is gewijzigd");		
				JOptionPane.showMessageDialog(null, "Uw wijzigingen werden opgeslagen. Deze worden zichtbaar wanneer de gebruiker deze goedkeurt.", "Wijzigingen doorgevoerd", JOptionPane.INFORMATION_MESSAGE);
			}
			
			return null;
		}
	}
	private class Beoordelen extends SwingWorker<Void,Void>
	{
		private boolean goedkeuren;
		
		public Beoordelen(boolean goedkeuren)
		{
			this.goedkeuren = goedkeuren;
		}
		@Override
		protected Void doInBackground()
		{
			d.beoordeelDocument(origineelDocument,goedkeuren);
			return null;
		}
	}
}