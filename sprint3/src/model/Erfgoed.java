package model;

import java.util.ArrayList;


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
	private int burgerId;
	
	public Erfgoed(int id, String naam, String postcode, String deelgemeente,
			String straat, String huisnr, String omschrijving,
			String typeErfgoed, String kenmerken, String geschiedenis,
			String nuttigeInfo, String link, int burgerId, Model m)
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
		this.burgerId = burgerId;
	}
	
	public Erfgoed(Model m)
	{
		this (-1, "","","","","","","","","","","",m.getBeheerder().getId(),m);
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

	public String getTypeErfgoed() {
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
	
	public int getBurgerId()
	{
		return burgerId;
	}
	public Burger getEigenaar()
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

	public void setNuttigeInfo(String nuttigeInfo) {
		this.nuttigeInfo = nuttigeInfo;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setBurgerId(int burgerId)
	{
		this.burgerId = burgerId;
		m.notifyListeners();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deelgemeente == null) ? 0 : deelgemeente.hashCode());
		result = prime * result + ((huisnr == null) ? 0 : huisnr.hashCode());
		result = prime * result
				+ ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((straat == null) ? 0 : straat.hashCode());
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
		if (deelgemeente == null) {
			if (other.deelgemeente != null)
				return false;
		} else if (!deelgemeente.equals(other.deelgemeente))
			return false;
		if (huisnr == null) {
			if (other.huisnr != null)
				return false;
		} else if (!huisnr.equals(other.huisnr))
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
		return true;
	}
}