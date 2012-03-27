package model;

public class Beheerder 
{
	private int id;
	private String naam;
	private boolean KanBeoordelen, KanWijzigen, KanVerwijderen;
	private Model m;
	
	public Beheerder(int id, String n, boolean kb, boolean kw, boolean kv, Model model)
	{
		this.id=id;
		this.naam=n;
		this.KanBeoordelen = kb;
		this.KanWijzigen = kw;
		this.KanVerwijderen = kv;
		this.m = model;
	}

	public int getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public boolean isKanBeoordelen() {
		return KanBeoordelen;
	}

	public boolean isKanWijzigen() {
		return KanWijzigen;
	}

	public boolean isKanVerwijderen() {
		return KanVerwijderen;
	}

	public void setId(int id) {
		this.id = id;
		m.notifyListeners();
	}

	public void setNaam(String naam) {
		this.naam = naam;
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
	
	
}
