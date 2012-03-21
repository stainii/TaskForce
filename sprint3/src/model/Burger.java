package model;

import java.util.ArrayList;

public class Burger
{
	private Model m;
	private int id;
	private String gebruikersnaam;
	private String voornaam;
	private String familienaam;
	private String email;
	
	public Burger(int id, String gebruikersnaam, String voornaam, String familienaam, String email, Model m)
	{
		this.m = m;
		this.id = id;
		this.gebruikersnaam = gebruikersnaam;
		this.voornaam = voornaam;
		this.familienaam = familienaam;
		this.email = email;
	}
	
	public int getId()
	{
		return id;
	}
	public String getGebruikersnaam()
	{
		return gebruikersnaam;
	}
	public String getVoornaam()
	{
		return voornaam;
	}
	public String getFamilienaam()
	{
		return familienaam;
	}
	public String getNaam()
	{
		return voornaam + " " + familienaam;
	}
	public String getEmail()
	{
		return email;
	}
	public ArrayList<Erfgoed> getErfgoed()
	{
		ArrayList<Erfgoed> returnArray = new ArrayList<Erfgoed>();
		for (Erfgoed e: m.getErfgoed())
		{
			if (e.getBurgerId() == this.id)
			{
				returnArray.add(e);
			}
		}
		return returnArray;
	}
	
	public void setId(int id)
	{
		this.id = id;
		m.notifyListeners();
	}
	public void setGebruikersnaam(String gebruikersnaam)
	{
		this.gebruikersnaam = gebruikersnaam;
		m.notifyListeners();
	}
	public void setVoornaam(String voornaam)
	{
		this.voornaam = voornaam;
		m.notifyListeners();
	}
	public void setFamilienaam(String familienaam)
	{
		this.familienaam = familienaam;
		m.notifyListeners();
	}
	public void setEmail(String email) throws IllegalArgumentException
	{
		//controle emailadres
		boolean apenstaartje = false;
		boolean puntNaApenstaartje = false;
		for (int i=0;i<email.length();i++)
		{
			if (!apenstaartje)
			{
				if (email.charAt(i) == '@')
					apenstaartje = true;
			}
			else
			{
				if (email.charAt(i) == '.')
					puntNaApenstaartje = true;
			}
		}
		
		if (apenstaartje && puntNaApenstaartje)
			this.email = email;
		else
			throw new IllegalArgumentException("Emailadres is niet geldig.");
		
		m.notifyListeners();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((familienaam == null) ? 0 : familienaam.hashCode());
		result = prime * result
				+ ((gebruikersnaam == null) ? 0 : gebruikersnaam.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((voornaam == null) ? 0 : voornaam.hashCode());
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
		Burger other = (Burger) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (familienaam == null) {
			if (other.familienaam != null)
				return false;
		} else if (!familienaam.equals(other.familienaam))
			return false;
		if (gebruikersnaam == null) {
			if (other.gebruikersnaam != null)
				return false;
		} else if (!gebruikersnaam.equals(other.gebruikersnaam))
			return false;
		if (id != other.id)
			return false;
		if (voornaam == null) {
			if (other.voornaam != null)
				return false;
		} else if (!voornaam.equals(other.voornaam))
			return false;
		return true;
	}
}