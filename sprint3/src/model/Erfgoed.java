package model;

import java.util.ArrayList;


public class Erfgoed
{
	private Model m;
	private int id;
	private String naam;
	private int burgerId;
	private String plaats;
	
	public Erfgoed(int id, String naam, int burgerId, String plaats, Model m)
	{
		this.m = m;
		this.id = id;
		this.naam = naam;
		this.plaats = plaats;
		this.setBurgerId(burgerId);
	}
	
	public int getId()
	{
		return id;
	}
	public String getNaam()
	{
		return naam;
	}
	public Burger getEigenaar()
	{
		ArrayList<Burger> burgers = m.getBurgers();
		for (Burger b: burgers)
		{
			ArrayList<Erfgoed> erfgoed = b.getErfgoed();
			for (Erfgoed e: erfgoed)
			{
				if (e.equals(this))
				{
					return b;
				}
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
	
	public String getPlaats()
	{
		return plaats;
	}
	public int getBurgerId()
	{
		return burgerId;
	}
	
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
	public void setBurgerId(int burgerId)
	{
		this.burgerId = burgerId;
		m.notifyListeners();
	}
	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
		m.notifyListeners();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + burgerId;
		result = prime * result + id;
		result = prime * result + ((naam == null) ? 0 : naam.hashCode());
		result = prime * result + ((plaats == null) ? 0 : plaats.hashCode());
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
		if (burgerId != other.burgerId)
			return false;
		if (id != other.id)
			return false;
		if (naam == null) {
			if (other.naam != null)
				return false;
		} else if (!naam.equals(other.naam))
			return false;
		if (plaats == null) {
			if (other.plaats != null)
				return false;
		} else if (!plaats.equals(other.plaats))
			return false;
		return true;
	}

}