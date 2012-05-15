package speeltuin;

import java.sql.Timestamp;

import model.DocumentCMS;
import model.Erfgoed;

/**
 * 	Deze klasse doet momenteel niets, dit werd gebruikt in het undo-system. Maar dit
 * 	wordt momenteel NIET meer gebruikt.
 */

public class Actie
{
	private String actie;
	private Timestamp datum;
	private DocumentCMS document;
	private Erfgoed erfgoed;
	
	public Actie(DocumentCMS document,String actie, Timestamp datum)
	{
		this.actie = actie;
		this.datum = datum;
		this.document = document;
	}
	public Actie(Erfgoed erfgoed, String actie, Timestamp datum)
	{
		this.actie = actie;
		this.datum = datum;
		this.erfgoed = erfgoed;
	}
	
	
	public String getActie()
	{
		return actie;
	}
	public Timestamp getDatum()
	{
		return datum;
	}
	public DocumentCMS getDocument()
	{
		return document;
	}
	public Erfgoed getErfgoed()
	{
		return erfgoed;
	}
	
	public void setActie(String actie)
	{
		this.actie = actie;
	}
	public void setDatum(Timestamp datum)
	{
		this.datum = datum;
	}
	public void setDocument(DocumentCMS document)
	{
		this.document = document;
	}
	public void setErfgoed(Erfgoed erfgoed)
	{
		this.erfgoed = erfgoed;
	}
}
