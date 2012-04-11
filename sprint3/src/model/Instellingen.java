package model;

public class Instellingen 
{
	private String instellingenSleutel;
	private String instellingenWaarde;
	private int beheerderId;
	
	public Instellingen(String is, String iw, int bid)
	{
		this.instellingenSleutel = is;
		this.instellingenWaarde = iw;
		this.beheerderId = bid;
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
