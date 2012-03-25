package model;

import java.sql.Timestamp;

public class Actie
{
	private String actie;
	private Timestamp datum;
	private DocumentCMS document;
	
	public Actie(DocumentCMS document,String actie, Timestamp datum)
	{
		this.actie = actie;
		this.datum = datum;
		this.document = document;
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
}
