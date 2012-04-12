package model;

public class Instellingen 
{
	private int instellingId;
	private String instellingenSleutel;
	private String instellingenWaarde;
	private int beheerderId;
	
	public Instellingen(int inid, String is, String iw, int bid)
	{
		this.instellingId = inid;
		this.instellingenSleutel = is;
		this.instellingenWaarde = iw;
		this.beheerderId = bid;
	}

	public int getInstellingId(){
		return instellingId;
	}
	public String getInstellingenSleutel() {
		return instellingenSleutel;
	}

	public String getInstellingenWaarde() {
		return instellingenWaarde;
	}

	public int getBeheerderId() {
		return beheerderId;
	}

	public void setInstellingId(int instellingId){
		this.instellingId = instellingId;
	}
	public void setInstellingenSleutel(String instellingenSleutel) {
		this.instellingenSleutel = instellingenSleutel;
	}

	public void setInstellingenWaarde(String instellingenWaarde) {
		this.instellingenWaarde = instellingenWaarde;
	}

	public void setBeheerderId(int beheerderId) {
		this.beheerderId = beheerderId;
	}
}
