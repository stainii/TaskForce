package model;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import controllers.Databank;


public class DocumentCMS
{	
	private int id;
	private String titel;
	private String status;
	private boolean verwijderd;
	private Model m;
	private int erfgoedId;
	private Timestamp datumToegevoegd;
	private Timestamp datumGewijzigd;
	private String opmerkingen; 
	private String tekst; 
	private String typeDocument; 
	private String extensieDocument;
	private String redenAfwijzing;
	private BufferedImage image;
	private int mediaId;
	private String temp;
	
	private int burgerId;
	private int beheerderId;
	private String aard;

	//deze constructor wordt gebruikt bij het inladen van de databank
	public DocumentCMS(int id, String titel, String status, Timestamp datumToegevoegd, boolean verwijderd, String opmerkingen, String tekst, String type, String extensie, int erfgoedId, String redenAfwijzing, Timestamp datumWijziging, int mediaId, int burgerId, int beheerderId,String aard, Model m)
	{
		this.m = m;
		this.id = id;
		this.titel = titel;
		this.status = status;
		this.verwijderd = verwijderd;
		this.setErfgoedId(erfgoedId);
		this.datumToegevoegd = datumToegevoegd;
		this.datumGewijzigd = datumWijziging;
		this.opmerkingen = opmerkingen;
		this.tekst = tekst; 
		this.typeDocument = type;
		this.setExtensieDocument(extensie);
		this.redenAfwijzing = redenAfwijzing;
		this.mediaId = mediaId;
		this.burgerId = burgerId;
		this.beheerderId = beheerderId;
		this.aard = aard;
	}
	
	//deze constructor wordt gebruikt hij het toevoegen van een nieuw document aan een erfgoed
	public DocumentCMS(Erfgoed e, Model m, Databank d, int beheerderId)
	{
		this.m = m;
		this.id = -1;		//moet later uit databank ingelezen worden
		this.titel ="";
		this.status = "Goedgekeurd";
		this.verwijderd = false;
		this.erfgoedId = e.getId();
		this.datumToegevoegd = new Timestamp(new Date().getTime());;
		this.datumGewijzigd = new Timestamp(new Date().getTime());;
		this.opmerkingen = "";
		this.tekst = "";
		this.typeDocument = "Onbekend";
		this.setExtensieDocument("Onbekend");
		this.redenAfwijzing = "";
		this.beheerderId = beheerderId;
		this.aard = "";
	}


	public int getBurgerId()
	{
		return burgerId;
	}
	
	public int getBeheerderId()
	{
		return beheerderId;
	}

	public int getId()
	{
		return id;
	}
	
	public String getTitel()
	{
		return titel;
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
	
	public Burger getBurger()
	{
		ArrayList<Burger> burgers = m.getBurgers();
		for (Burger b: burgers)
		{
			if (b.getId()==burgerId)
			{
				return b;
			}
		}
		return null;
	}
	public Beheerder getBeheerder()
	{
		ArrayList<Beheerder> beheerder = m.getBeheerders();
		for (Beheerder b: beheerder)
		{
			if (b.getId()==beheerderId)
			{
				return b;
			}
		}
		return null;
	}
	
	public Timestamp getDatumToegevoegd()
	{
		return datumToegevoegd;
	}
	public Timestamp getDatumGewijzigd()
	{
		return datumGewijzigd;
	}

	public boolean isVerwijderd()
	{
		return verwijderd;
	}
	
	public String getOpmerkingen()
	{
		return opmerkingen;
	}


	public String getTekst()
	{
		return tekst;
	}


	public String getTypeDocument()
	{
		return typeDocument;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public String getRedenAfwijzing()
	{
		return redenAfwijzing;
	}
	
	public int getErfgoedId()
	{
		return erfgoedId;
	}	

	public int getMediaId()
	{
		return mediaId;
	}
	
	public String getAard() {
		return aard;
	}


	// setters
	public void setBurgerId(int burgerId)
	{
		this.burgerId = burgerId;
	}
	public void setBeheerderId(int beheerderId)
	{
		this.beheerderId = beheerderId;
	}
	
	public void setId(int id)
	{
		this.id = id;
		m.notifyListeners();
	}
	
	public void setTitel(String titel)
	{
		this.titel = titel;
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

	public void setTypeDocument(String type)
	{
		this.typeDocument = type;
		m.notifyListeners();
	}
	
	public void setDatumToegevoegd(Timestamp datum)
	{
		this.datumToegevoegd = datum;
		m.notifyListeners();
	}
	public void setDatumGewijzigd(Timestamp datum)
	{
		this.datumGewijzigd = datum;
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
	
	public void setErfgoedId(int erfgoedId)
	{
		this.erfgoedId = erfgoedId;
		m.notifyListeners();
	}
	
	public void setMediaId(int mediaId)
	{
		this.mediaId = mediaId;
	}

	public void setAard(String aard) {
		this.aard = aard;
	}
	
	public String getExtensieDocument() {
		return extensieDocument;
	}

	public void setExtensieDocument(String extensieDocument) {
		this.extensieDocument = extensieDocument;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datumGewijzigd == null) ? 0 : datumGewijzigd.hashCode());
		result = prime * result
				+ ((datumToegevoegd == null) ? 0 : datumToegevoegd.hashCode());
		result = prime * result + erfgoedId;
		result = prime * result + id;
		result = prime * result + mediaId;
		result = prime * result
				+ ((opmerkingen == null) ? 0 : opmerkingen.hashCode());
		result = prime * result
				+ ((redenAfwijzing == null) ? 0 : redenAfwijzing.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tekst == null) ? 0 : tekst.hashCode());
		result = prime * result + ((titel == null) ? 0 : titel.hashCode());
		result = prime * result
				+ ((typeDocument == null) ? 0 : typeDocument.hashCode());
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
		if (datumGewijzigd == null) {
			if (other.datumGewijzigd != null)
				return false;
		} else if (!datumGewijzigd.equals(other.datumGewijzigd))
			return false;
		if (datumToegevoegd == null) {
			if (other.datumToegevoegd != null)
				return false;
		} else if (!datumToegevoegd.equals(other.datumToegevoegd))
			return false;
		if (erfgoedId != other.erfgoedId)
			return false;
		if (id != other.id)
			return false;
		if (mediaId != other.mediaId)
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
		if (titel == null) {
			if (other.titel != null)
				return false;
		} else if (!titel.equals(other.titel))
			return false;
		if (typeDocument == null) {
			if (other.typeDocument != null)
				return false;
		} else if (!typeDocument.equals(other.typeDocument))
			return false;
		return true;
	}

}