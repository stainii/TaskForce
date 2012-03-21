package model;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**Bevat alle documenten, erfgoed, burgers **/

public class Model
{
	private ArrayList<Burger> burgers;
	private ArrayList<Erfgoed> erfgoed;
	private ArrayList<DocumentCMS> documenten;
	private ArrayList<ChangeListener> listeners;
	private String beheerder;
	

	public Model()
	{
		listeners = new ArrayList<ChangeListener>();
		burgers = new ArrayList<Burger>();
		erfgoed = new ArrayList<Erfgoed>();
		documenten = new ArrayList<DocumentCMS>();
	}
	
	public ArrayList<Burger> getBurgers()
	{
		return burgers;
	}
	public ArrayList<Erfgoed> getErfgoed()
	{
		return erfgoed;
	}
	public ArrayList<DocumentCMS> getDocumenten()
	{
		return documenten;
	}

	public void setBurgers(ArrayList<Burger> burgers)
	{
		this.burgers = burgers;
		notifyListeners();
	}
	public void setErfgoed(ArrayList<Erfgoed> erfgoed)
	{
		this.erfgoed = erfgoed;
		notifyListeners();
	}

	public void setDocumenten(ArrayList<DocumentCMS> documenten)
	{
		this.documenten = documenten;
		notifyListeners();
	}
	
	public void toevoegenDocument(DocumentCMS d)
	{
		documenten.add(d);
		notifyListeners();
	}
	
	public void verwijderDocument(DocumentCMS d)
	{
		documenten.remove(d);
		notifyListeners();
	}
	
	public void verwijderDocument(int id)		//deze methode verwijdert het document op basis van zijn databankId
	{
		for (int i=0; i<getDocumenten().size();i++)
		{
			if (getDocumenten().get(i).getId() == id)
			{
				getDocumenten().remove(i);
			}
		}
		notifyListeners();
	}
	
	public void bewerkDocument(DocumentCMS d, Erfgoed e)
	{
		//overloop de array list van documenten
		for(int i =0; i<documenten.size(); i++)
		{
			DocumentCMS doc = documenten.get(i);
			if(doc.getId()==d.getId())
			{
				documenten.set(i,d);
				i = documenten.size();
			}
		}
		
		//overloop de array list van erfgoed
		for(int i =0; i<erfgoed.size(); i++)
		{
			Erfgoed erf = erfgoed.get(i);
			if(erf.getId()==e.getId())
			{
				erfgoed.set(i,e);
				i = erfgoed.size();
			}
		}
		
		notifyListeners();
	}
	
	public String getBeheerder() 
	{
		return beheerder;
	}

	public void setBeheerder(String beheerder) 
	{
		this.beheerder = beheerder;
	}
	
	//Listeners
	public void addListener(ChangeListener l)
	{
		listeners.add(l);
	}
	public void removeListener(ChangeListener l)
	{
		listeners.remove(l);
	}
	
	public void notifyListeners()
	{
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener l: listeners)
		{
			l.stateChanged(e);
		}
	}
}