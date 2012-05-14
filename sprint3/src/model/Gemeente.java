package model;

public class Gemeente 
{
	private int postcode;
	private String gemeente;
	
	public Gemeente(int p , String g)
	{
		this.postcode = p;
		this.gemeente = g;
	}

	public int getPostcode() {
		return postcode;
	}

	public String getGemeente() {
		return gemeente;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public void setGemeente(String gemeente) {
		this.gemeente = gemeente;
	}
}
