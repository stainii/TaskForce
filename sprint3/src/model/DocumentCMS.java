package model;

import java.awt.image.BufferedImage;
import java.sql.Date;

import controllers.Databank;


public class DocumentCMS
{	
	private int id;
	private String status;
	private boolean verwijderd;
	private Model m;
	private int erfgoedId;
	private Date datum;
	private String opmerkingen; 
	private String tekst; 
	private String type; 
	private String redenAfwijzing;
	private BufferedImage image;
	

	//deze constructor wordt gebruikt bij het inladen van de databank
	public DocumentCMS(int id, String status, Date datum, boolean verwijderd, String opmerkingen, String tekst, String type, int erfgoedId,String redenAfwijzing, Model m)
	{
		this.m = m;
		this.id = id;
		this.status = status;
		this.verwijderd = verwijderd;
		this.setErfgoedId(erfgoedId);
		this.datum = datum;
		this.opmerkingen = opmerkingen;
		this.tekst = tekst; 
		this.type = type;
		this.redenAfwijzing = redenAfwijzing;
	}
	
	//deze constructor wordt gebruikt hij het toevoegen van een nieuw document aan een erfgoed
	public DocumentCMS(Erfgoed e, Model m, Databank d)
	{
		this.m = m;
		this.id = -1;		//moet later uit databank ingelezen worden
		this.status = "Nog niet beoordeeld";
		this.verwijderd = false;
		this.setErfgoedId(e.getId());
		this.datum = new Date(d.getDatabankTijd().getTime());
		this.opmerkingen = "";
		this.tekst = "";
		this.type = "Onbekend";
		this.redenAfwijzing = "";
	}


	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}
	
	public Erfgoed getErfgoed()
	{
		for (Erfgoed e: m.getErfgoed())
		{
			if (e.getId() == this.erfgoedId)
				return e;
		}
		return null;
	}
	
	public Burger getEigenaar()
	{
		return getErfgoed().getEigenaar();
	}
	
	public Date getDatum()
	{
		return datum;
	}

	public boolean isVerwijderd() {
		return verwijderd;
	}
	
	public String getOpmerkingen() {
		return opmerkingen;
		
	}


	public String getTekst() {
		return tekst;
	}


	public String getType() {
		return type;
	}
	
	public int getErfgoedId() {
		return erfgoedId;
	}	

	public BufferedImage getImage() {
		return image;
	}
	
	public String getRedenAfwijzing() {
		return redenAfwijzing;
	}




	// setters
	public void setId(int id)
	{
		this.id = id;
		m.notifyListeners();
	}

	public void setStatus(String status)
	{
		this.status = status;
		m.notifyListeners();
	}
	
	public void setVerwijderd(boolean verwijderd)
	{
		this.verwijderd = verwijderd;
		m.notifyListeners();		
	}

	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
		m.notifyListeners();
	}


	public void setTekst(String tekst)
	{
		this.tekst = tekst;
		m.notifyListeners();
	}


	public void setType(String type)
	{
		this.type = type;
		m.notifyListeners();
	}


	public void setErfgoedId(int erfgoedId)
	{
		this.erfgoedId = erfgoedId;
		m.notifyListeners();
	}
	
	public void setDatum(Date datum)
	{
		this.datum = datum;
		m.notifyListeners();
	}
	

	public void setImage(BufferedImage image) 
	{
		this.image = image;
		//geen notify nodig
	}
	
	public void setRedenAfwijzing(String redenAfwijzing)
	{
		this.redenAfwijzing = redenAfwijzing;
		m.notifyListeners();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + erfgoedId;
		result = prime * result + id;
		result = prime * result
				+ ((opmerkingen == null) ? 0 : opmerkingen.hashCode());
		result = prime * result
				+ ((redenAfwijzing == null) ? 0 : redenAfwijzing.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tekst == null) ? 0 : tekst.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (verwijderd ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentCMS other = (DocumentCMS) obj;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (erfgoedId != other.erfgoedId)
			return false;
		if (id != other.id)
			return false;
		if (opmerkingen == null) {
			if (other.opmerkingen != null)
				return false;
		} else if (!opmerkingen.equals(other.opmerkingen))
			return false;
		if (redenAfwijzing == null) {
			if (other.redenAfwijzing != null)
				return false;
		} else if (!redenAfwijzing.equals(other.redenAfwijzing))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tekst == null) {
			if (other.tekst != null)
				return false;
		} else if (!tekst.equals(other.tekst))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (verwijderd != other.verwijderd)
			return false;
		return true;
	}
}