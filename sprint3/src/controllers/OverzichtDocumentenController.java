package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.DocumentCMS;
import model.Model;

public class OverzichtDocumentenController
{
	
	/**Overzicht controller is de controller met functies specifiek voor OverzichtContent.
	 * Het houdt bij wat er ingeladen moet worden op het scherm. Dat is een gefilterde en
	 * gesorteerde versie van het model. Het sorteren moet NA het filteren gebeuren.
	 * Verwittigt OverzichtContent als er iets verandert.*/

	private Model m;
	private Databank d;
	private ArrayList<DocumentCMS> inTeLaden;
	private ArrayList<ChangeListener> listeners;
	
	public OverzichtDocumentenController(Model m, Databank d)
	{	
		this.m = m;
		this.d = d;
		listeners = new ArrayList<ChangeListener>();
		
		inTeLaden = m.getDocumenten();
		setInTeLaden(filterOpStatus("Nog niet beoordeeld"));
		sorteerOpLaatstToegevoegd();
	}
	
	
	public ArrayList<DocumentCMS> filterOpStatus(String s)
	{
		ArrayList<DocumentCMS> origineel = m.getDocumenten();
		ArrayList<DocumentCMS> gefilterd = new ArrayList<DocumentCMS>();
		
		for (DocumentCMS d: origineel)
		{
			if (d.getStatus().equals(s))
			{
				gefilterd.add(d);
			}
		}
		
		return gefilterd;
	}
	
	
	public ArrayList<DocumentCMS> filterOpType(String s)
	{
		ArrayList<DocumentCMS> origineel = m.getDocumenten();
		ArrayList<DocumentCMS> gefilterd = new ArrayList<DocumentCMS>();
		
		for (DocumentCMS d: origineel)
		{
			if (d.getTypeDocument().equals(s))
			{
				gefilterd.add(d);
			}
		}
		
		return gefilterd;
	}
	
	public void sorteerOpBurger()
	{
		Collections.sort(inTeLaden, new Comparator<DocumentCMS>()
		{
			@Override
			public int compare(DocumentCMS doc1, DocumentCMS doc2)
			{
				return doc1.getEigenaar().getNaam().compareTo(doc2.getEigenaar().getNaam());
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpErfgoed()
	{
		Collections.sort(inTeLaden, new Comparator<DocumentCMS>()
		{
			@Override
			public int compare(DocumentCMS doc1, DocumentCMS doc2)
			{
				return doc1.getErfgoed().getNaam().compareTo(doc2.getErfgoed().getNaam());
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpLaatstToegevoegd()
	{
		Collections.sort(inTeLaden, new Comparator<DocumentCMS>()
		{
			@Override
			public int compare(DocumentCMS doc1, DocumentCMS doc2)
			{
				return -doc1.getDatumToegevoegd().toString().compareTo(doc2.getDatumToegevoegd().toString());
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpType()
	{
		Collections.sort(inTeLaden, new Comparator<DocumentCMS>()
		{
			@Override
			public int compare(DocumentCMS doc1, DocumentCMS doc2)
			{
				return doc1.getTypeDocument().compareTo(doc2.getTypeDocument());
			}
		});
		notifyListeners();
	}
	
	public ArrayList<DocumentCMS> zoek(String s, ArrayList<DocumentCMS> docs)
	{
		ArrayList<DocumentCMS> resultaat = new ArrayList<DocumentCMS>();
		
		for (DocumentCMS d: docs)
		{
			if (d.getErfgoed().getNaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getDeelgemeente().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getGeschiedenis().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getHuisnr()!= null && d.getErfgoed().getHuisnr().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getKenmerken().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getLink().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getNuttigeInfo().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getOmschrijving().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getPostcode().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getStraat().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getTypeErfgoed().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getEigenaar().getGebruikersnaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getErfgoed().getEigenaar().getNaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getTitel().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getStatus().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getRedenAfwijzing().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getTekst().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getOpmerkingen().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getDatumToegevoegd().toString().toLowerCase().contains(s.toLowerCase()))
					resultaat.add(d);
			else if (d.getDatumGewijzigd().toString().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
			else if (d.getEigenaar().getNaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(d);
		}
		
		return resultaat;
	}
	
	public ArrayList<DocumentCMS> getInTeLaden()
	{
		return inTeLaden;
	}

	public void setInTeLaden(ArrayList<DocumentCMS> inTeLaden)
	{
		this.inTeLaden = inTeLaden;
	}
	
	public void verwijderDocument(DocumentCMS doc)
	{
		d.verwijderDocument(doc);
	}
	
	public void addListener(ChangeListener l)
	{
		listeners.add(l);
	}
	public void removeListener(ChangeListener l)
	{
		listeners.remove(l);
	}
	public void notifyListeners()
	{
		ChangeEvent e = new ChangeEvent(this);
		for(ChangeListener l : listeners)
		{
			l.stateChanged(e);
		}
	}
}