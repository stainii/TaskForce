package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class Erfgoed
{
	private Model m;
	private int id;
	private String naam;
	private String postcode;
	private String deelgemeente; 
	private String straat; 
	private String huisnr; 
	private String omschrijving;
	private String typeErfgoed;
	private String kenmerken; 
	private String geschiedenis;
	private String nuttigeInfo;
	private String link;
	private Timestamp datumToegevoegd;
	private boolean verwijderd; 
	private int burgerId;
	private int beheerderId;
	
	public Erfgoed(int id, String naam, String postcode, String deelgemeente,
			String straat, String huisnr, String omschrijving,
			String typeErfgoed, String kenmerken, String geschiedenis,
			String nuttigeInfo, String link, Timestamp datumToegevoegd, boolean verwijderd, int burgerId, int beheerderId, Model m)
	{
		this.m = m;
		this.id = id;
		this.naam = naam;
		this.postcode = postcode; 
		this.deelgemeente = deelgemeente;
		this.straat = straat;
		this.huisnr = huisnr;
		this.omschrijving = omschrijving; 
		this.typeErfgoed = typeErfgoed; 
		this.kenmerken = kenmerken; 
		this.geschiedenis = geschiedenis; 
		this.nuttigeInfo = nuttigeInfo;
		this.link = link;
		this.datumToegevoegd = datumToegevoegd;
		this.verwijderd = verwijderd;
		this.burgerId = burgerId;
		this.beheerderId = beheerderId;
	}
	
	public Erfgoed(Model m)
	{
		this (-1, "","","","","","","","","","","",new Timestamp(new Date().getTime()), false,0,m.getBeheerder().getId(),m);
	}
	
	// ------- getters ------------
	

	public int getId()
	{
		return id;
	}
	public String getNaam()
	{
		return naam;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getDeelgemeente() {
		return deelgemeente;
	}

	public String getStraat() {
		return straat;
	}

	public String getHuisnr() {
		return huisnr;
	}

	public String getOmschrijving() {
		return omschrijving;
	}

	public String getTypeErfgoed()
	{
		return typeErfgoed;
	}

	public String getKenmerken() {
		return kenmerken;
	}

	public String getGeschiedenis() {
		return geschiedenis;
	}

	public String getNuttigeInfo() {
		return nuttigeInfo;
	}
	
	public String getLink() {
		return link;
	}
	public Timestamp getDatumToegevoegd()
	{
		return datumToegevoegd;
	}
	
	public boolean isVerwijderd() {
		return verwijderd;
	}
	
	public int getBurgerId()
	{
		return burgerId;
	}
	public int getBeheerderId()
	{
		return beheerderId;
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
	
	public ArrayList<DocumentCMS> getDocumenten()
	{
			ArrayList<DocumentCMS> returnArray = new ArrayList<DocumentCMS>();
			for (DocumentCMS d: m.getDocumenten())
			{
				if (d.getErfgoedId() == this.id)
				{
					returnArray.add(d);
				}
			}
			return returnArray;
	}
	
	public int getAantalDocumentenMetStatus(String status)
	{
		int aantal = 0;
		for (DocumentCMS d: m.getDocumenten())
		{
			if (d.getErfgoedId() == this.id && d.getStatus().equals(status))
			{
				aantal++;
			}
		}
		return aantal;
	}
	

	// -------- setters -------------
	
	public void setId(int id)
	{
		this.id = id;
		m.notifyListeners();
	}
	public void setNaam(String naam)
	{
		this.naam = naam;
		m.notifyListeners();
	}
	
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setDeelgemeente(String deelgemeente) {
		this.deelgemeente = deelgemeente;
	}

	public void setStraat(String straat) {
		this.straat = straat;
	}

	public void setHuisnr(String huisnr) {
		this.huisnr = huisnr;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public void setTypeErfgoed(String typeErfgoed) {
		this.typeErfgoed = typeErfgoed;
	}

	public void setKenmerken(String kenmerken) {
		this.kenmerken = kenmerken;
	}

	public void setGeschiedenis(String geschiedenis) {
		this.geschiedenis = geschiedenis;
	}

	public void setNuttigeInfo(String nuttigeInfo)
	{
		this.nuttigeInfo = nuttigeInfo;
	}

	public void setLink(String link) 
	{
		this.link = link;
	}
	
	public void setDatumToegevoegd(Timestamp datumToegevoegd) 
	{
		this.datumToegevoegd = datumToegevoegd;
	}
	
	public void setVerwijderd(boolean verwijderd)
	{
		this.verwijderd = verwijderd;
	}

	public void setBurgerId(int burgerId)
	{
		this.burgerId = burgerId;
		m.notifyListeners();
	}
	public void setBeheerderId(int beheerderId)
	{
		this.beheerderId = beheerderId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + beheerderId;
		result = prime * result + burgerId;
		result = prime * result
				+ ((datumToegevoegd == null) ? 0 : datumToegevoegd.hashCode());
		result = prime * result
				+ ((deelgemeente == null) ? 0 : deelgemeente.hashCode());
		result = prime * result
				+ ((geschiedenis == null) ? 0 : geschiedenis.hashCode());
		result = prime * result + ((huisnr == null) ? 0 : huisnr.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((kenmerken == null) ? 0 : kenmerken.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((naam == null) ? 0 : naam.hashCode());
		result = prime * result
				+ ((nuttigeInfo == null) ? 0 : nuttigeInfo.hashCode());
		result = prime * result
				+ ((omschrijving == null) ? 0 : omschrijving.hashCode());
		result = prime * result
				+ ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((straat == null) ? 0 : straat.hashCode());
		result = prime * result
				+ ((typeErfgoed == null) ? 0 : typeErfgoed.hashCode());
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
		Erfgoed other = (Erfgoed) obj;
		if (beheerderId != other.beheerderId)
			return false;
		if (burgerId != other.burgerId)
			return false;
		if (datumToegevoegd == null) {
			if (other.datumToegevoegd != null)
				return false;
		} else if (!datumToegevoegd.equals(other.datumToegevoegd))
			return false;
		if (deelgemeente == null) {
			if (other.deelgemeente != null)
				return false;
		} else if (!deelgemeente.equals(other.deelgemeente))
			return false;
		if (geschiedenis == null) {
			if (other.geschiedenis != null)
				return false;
		} else if (!geschiedenis.equals(other.geschiedenis))
			return false;
		if (huisnr == null) {
			if (other.huisnr != null)
				return false;
		} else if (!huisnr.equals(other.huisnr))
			return false;
		if (id != other.id)
			return false;
		if (kenmerken == null) {
			if (other.kenmerken != null)
				return false;
		} else if (!kenmerken.equals(other.kenmerken))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (naam == null) {
			if (other.naam != null)
				return false;
		} else if (!naam.equals(other.naam))
			return false;
		if (nuttigeInfo == null) {
			if (other.nuttigeInfo != null)
				return false;
		} else if (!nuttigeInfo.equals(other.nuttigeInfo))
			return false;
		if (omschrijving == null) {
			if (other.omschrijving != null)
				return false;
		} else if (!omschrijving.equals(other.omschrijving))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (straat == null) {
			if (other.straat != null)
				return false;
		} else if (!straat.equals(other.straat))
			return false;
		if (typeErfgoed == null) {
			if (other.typeErfgoed != null)
				return false;
		} else if (!typeErfgoed.equals(other.typeErfgoed))
			return false;
		if (verwijderd != other.verwijderd)
			return false;
		return true;
	}
}