package model;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**Bevat alle documenten, erfgoed, burgers **/

public class Model
{
	private ArrayList<Burger> burgers;
	private ArrayList<Erfgoed> erfgoed;
	private ArrayList<DocumentCMS> documenten;
	private ArrayList<Beheerder> beheerders;
	private ArrayList<Instellingen> instellingen;
	private ArrayList<Gemeenten> gemeente;
	private ArrayList<ChangeListener> listeners;
	private Beheerder beheerder;
	private Burger burger;
	private ArrayList<String> reden;
	private Instellingen instelling;

	public Model()
	{
		listeners = new ArrayList<ChangeListener>();
		burgers = new ArrayList<Burger>();
		erfgoed = new ArrayList<Erfgoed>();
		beheerders = new ArrayList<Beheerder>();
		documenten = new ArrayList<DocumentCMS>();
		instellingen = new ArrayList<Instellingen>();
		gemeente = new ArrayList<Gemeenten>();
		reden = new ArrayList<String>() ;
	}
	
	//getters
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
	public ArrayList<Beheerder> getBeheerders()
	{	
		return beheerders;
	}
	
	public ArrayList<Instellingen> getInstellingen()
	{
		return instellingen;
	}
	public ArrayList<Gemeenten> getGemeenten()
	{
		return gemeente;
	}
	
	//setters
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
	public void setBeheerders(ArrayList<Beheerder> beheerders)
	{
		this.beheerders = beheerders;
		notifyListeners();
	}
	public void setInstellingen(ArrayList<Instellingen> instel)
	{
		this.instellingen = instel;
		notifyListeners();
	}
	public void setGemeenten(ArrayList<Gemeenten> gemeente)
	{
		this.gemeente = gemeente;
		notifyListeners();
	}

	public void toevoegenDocument(DocumentCMS d)
	{
		documenten.add(d);
		notifyListeners();
	}
	
	public void toevoegenErfgoed(Erfgoed e)
	{
		erfgoed.add(e);
		notifyListeners();
	}
	
	public void verwijderDocument(DocumentCMS d)
	{
		documenten.remove(d);
		notifyListeners();
	}
	
	public void toevoegenBeheerder(Beheerder b)
	{
		beheerders.add(b);
		notifyListeners();
	}
	
	public void toevoegenInstelling(Instellingen i)
	{
		instellingen.add(i);
		notifyListeners();
	}
	
	public void verwijderBeheerder(int id)
	{
		for(int i=0; i<getBeheerders().size();i++)
		{
			if(getBeheerders().get(i).getId() == id)
			{
				getBeheerders().remove(i);
			}
		}
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
	
	public void verwijderErfgoed(Erfgoed e)
	{
		documenten.removeAll(e.getDocumenten());	
		erfgoed.remove(e);
		notifyListeners();
	}
	
	public void verwijderErfgoed(int id)
	{
		for (int i=0; i<getErfgoed().size();i++)
		{
			if (getErfgoed().get(i).getId() == id)
			{
				getErfgoed().remove(i);
			}
		}
		notifyListeners();
	}
	
	public void bewerkDocument(DocumentCMS d)
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
		notifyListeners();
	}
	
	public void bewerkErfgoed(Erfgoed e)
	{
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
	
	// Getter en setter voor String beheerder : Naam dat wordt ingegeven in textfield bij login.
	public Beheerder getBeheerder() 
	{
		try
		{
			return beheerder;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Kan de beheerder niet ophalen!", "Fout beheerder", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	public void setBeheerder(String naam) 
	{
		for (Beheerder b : beheerders)
		{
			if (b.getVoornaam().equalsIgnoreCase(naam))
				beheerder = b;
		}
	}
	
	public Burger getBurger()
	{
		try{
			return burger;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Kan de beheerder niet ophalen!", "Fout beheerder", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
	}
	
	public void setBurger(String naam)
	{
		for (Burger b : burgers)
		{
			if(b.getNaam().equalsIgnoreCase(naam))
				burger = b;
		}
	}
	
	public int getInstellingenId(String instellingSleutel)		// deze methode gaat Id teruggeven van de mee te geven InstellingenSleutel
	{															// Voor EmailOut, EmailPoort, .. kan er maar 1 overeenkomen met de BeheerderId
		for(Instellingen i : getInstellingen())					// omdat er maar 1 instelling van kan zijn. 
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals(instellingSleutel))
					return i.getInstellingId();
			}
		}
		return 0;
	}
	public ArrayList<String> getStandaardReden()		// bevat de ArrayList met de Standaard Redenen van afwijzing
	{
		reden.clear();
		for(Instellingen i : getInstellingen())
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("StandaardReden"))
				{
					reden.add(i.getInstellingenWaarde());
				}
			}
		}
		return reden;
	}
	public void setStandaardReden(ArrayList<String> sr)
	{
		this.reden = sr;
	}
	
	public void toevoegenStandaardReden(String sr)
	{
		getStandaardReden().add(sr);
		notifyListeners();
	}
	
	public void verwijderStandaardReden(String sr)
	{		
		for(Instellingen i : getInstellingen())
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("StandaardReden"))
				{
					if(i.getInstellingenWaarde().equals(sr))
					{
						reden.remove(sr);
					}
				}
			}
		}
	}
	
	public Instellingen getInstelling()
	{
		return instelling;
	}
	
	public void setInstelling(String instellingwaarde)
	{
		for(Instellingen i : getInstellingen())
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals("StandaardReden"))
				{
					if(i.getInstellingenWaarde().equals(instellingwaarde))
						instelling = i;
				}
			}
					
		}
	}
	
	public String getEmailVoorkeur(String instellingSleutel)
	{
		for(Instellingen i : getInstellingen())
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals(instellingSleutel))
					return i.getInstellingenWaarde();

			}
		}
		return "";
	}
	public void setEmailVoorkeur(String instellingWaarde, String instellingSleutel)
	{
		for(Instellingen i : getInstellingen())
		{
			if(i.getBeheerderId() == getBeheerder().getId())
			{
				if(i.getInstellingenSleutel().equals(instellingSleutel))
					i.setInstellingenWaarde(instellingWaarde);
			}
		}
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
