package model;

public class Gemeenten 
{
	private int postcode;
	private String gemeente;
	
	public Gemeenten(int p , String g)
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
