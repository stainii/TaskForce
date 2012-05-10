package model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import controllers.MD5;

public class Beheerder 
{
	private int id;
	private String voornaam,achternaam,email;
	private String wachtwoord;
	private boolean KanToevoegen,KanBeoordelen, KanWijzigen, KanVerwijderen,isAdmin;
	private Model m;
	
	public Beheerder(int id, String n, String a,String w,String em, boolean kb ,boolean kw, boolean kv, boolean kt,boolean isAdmin, Model model)
	{
		this.id=id;
		this.voornaam=n;
		this.achternaam=a;
		this.wachtwoord = w;
		this.email=em;
		this.KanToevoegen = kt;
		this.KanBeoordelen = kb;
		this.KanWijzigen = kw;
		this.KanVerwijderen = kv;
		this.isAdmin = isAdmin;
		this.m = model;
	}
	
	public int getId() {
		return id;
	}

	public String getVoornaam() {
		return voornaam;
	}
	public String getAchternaam(){
		return achternaam;
	}
	public String getNaam()
	{
		return voornaam + " " + achternaam;
	}
	public String getWachtwoord()  
	{
		return wachtwoord;
	}
	public String getEmail(){
		return email;
	}
	public boolean KanToevoegen()
	{
		return KanToevoegen;
	}
	public boolean KanBeoordelen() {
		return KanBeoordelen;
	}

	public boolean KanWijzigen() {
		return KanWijzigen;
	}

	public boolean KanVerwijderen() {
		return KanVerwijderen;
	}
	public boolean isAdmin(){
		return isAdmin;
	}
	
	public String getView()
	{
		for(Instellingen i : m.getInstellingen())
		{
			if(i.getBeheerderId()==m.getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("View"))
				{
					if(i.getInstellingenWaarde().equals("LijstView"))
						return "LijstView";
					else if(i.getInstellingenWaarde().equals("TegelView"))
						return "TegelView";
				}
			}
		}
		return "";
	}
	
	public String getTypeContent()
	{
		for(Instellingen i : m.getInstellingen())
		{
			if(i.getBeheerderId()==m.getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("TypeContent"))
				{
					if(i.getInstellingenWaarde().equals("Erfgoed"))
						return "Erfgoed";
					else if(i.getInstellingenWaarde().equals("Documenten"))
						return "Documenten";
				}
			}
		}
		return "";
	}

	public void setId(int id) {
		this.id = id;
		m.notifyListeners();
	}

	public void setVoornaam(String naam) {
		this.voornaam = naam;
		m.notifyListeners();
	}
	public void setAchternaam(String achternaam){
		this.achternaam = achternaam;
		m.notifyListeners();
	}
	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setKanToevoegen(boolean kanToevoegen){
		KanToevoegen = kanToevoegen;
		m.notifyListeners();
	}
	
	public void setKanBeoordelen(boolean kanBeoordelen) {
		KanBeoordelen = kanBeoordelen;
		m.notifyListeners();
	}

	public void setKanWijzigen(boolean kanWijzigen) {
		KanWijzigen = kanWijzigen;
		m.notifyListeners();
	}

	public void setKanVerwijderen(boolean kanVerwijderen) {
		KanVerwijderen = kanVerwijderen;
		m.notifyListeners();
	}
	public void setIsAdmin(boolean isAdmin){
		this.isAdmin = isAdmin;
		m.notifyListeners();
	}
	
	public void setView(int id, String view)
	{
		for(Instellingen i : m.getInstellingen())
		{
			if(i.getBeheerderId() == id)
			{
				if(i.getInstellingenSleutel().equals("View"))
					i.setInstellingenWaarde(view);
			}			
		}
		m.notifyListeners();
	}
	
	public void setTypeContent(int id, String typeContent)
	{
		for(Instellingen i : m.getInstellingen())
		{
			if(i.getBeheerderId() == id)
				if(i.getInstellingenSleutel().equals("TypeContent"))
					i.setInstellingenWaarde(typeContent);
		}
		m.notifyListeners();
	}
	
	
}
