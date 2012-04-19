package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Erfgoed;
import model.Model;

public class OverzichtErfgoedController
{
	
	/**Overzicht controller is de controller met functies specifiek voor OverzichtContent.
	 * Het houdt bij wat er ingeladen moet worden op het scherm. Dat is een gefilterde en
	 * gesorteerde versie van het model. Het sorteren moet NA het filteren gebeuren.
	 * Verwittigt OverzichtContent als er iets verandert.*/

	private Model m;
	private Databank d;
	private ArrayList<Erfgoed> inTeLaden;
	private ArrayList<ChangeListener> listeners;
	
	public OverzichtErfgoedController(Model m, Databank d)
	{	
		this.m = m;
		this.d = d;
		listeners = new ArrayList<ChangeListener>();
		
		inTeLaden = m.getErfgoed();
		sorteerOpLaatstToegevoegd();
	}
	
	
	
	
	public void sorteerOpBurger()
	{
		Collections.sort(inTeLaden, new Comparator<Erfgoed>()
		{
			@Override
			public int compare(Erfgoed e1, Erfgoed e2)
			{
				return (e1.getBurger()!=null?e1.getBurger().getNaam():e1.getBeheerder().getNaam()).compareTo((e2.getBurger()!=null?e2.getBurger().getNaam():e2.getBeheerder().getNaam()));
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpErfgoed()
	{
		Collections.sort(inTeLaden, new Comparator<Erfgoed>()
		{
			@Override
			public int compare(Erfgoed e1, Erfgoed e2)
			{
				return e1.getNaam().compareTo(e2.getNaam());
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpLaatstToegevoegd()
	{
		Collections.sort(inTeLaden, new Comparator<Erfgoed>()
		{
			@Override
			public int compare(Erfgoed e1, Erfgoed e2)
			{
				try
				{
					return -e1.getDatumToegevoegd().compareTo(e2.getDatumToegevoegd());					
				}
				catch(IndexOutOfBoundsException i)
				{
					return -1;
				}
				
			}
		});
		notifyListeners();
	}
	
	public void sorteerOpType()
	{
		Collections.sort(inTeLaden, new Comparator<Erfgoed>()
		{
			@Override
			public int compare(Erfgoed e1, Erfgoed e2)
			{
				return e1.getTypeErfgoed().compareTo(e2.getTypeErfgoed());
			}
		});
		notifyListeners();
	}
	
	public ArrayList<Erfgoed> zoek(String s, ArrayList<Erfgoed> erfgoed)
	{
		ArrayList<Erfgoed> resultaat = new ArrayList<Erfgoed>();
		
		for (Erfgoed e: erfgoed)
		{
			if (e.getNaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getDeelgemeente().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getGeschiedenis().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getHuisnr()!= null && e.getHuisnr().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getKenmerken().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getLink().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getNuttigeInfo().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getOmschrijving().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getPostcode().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getStraat().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getTypeErfgoed().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if (e.getBurger()!=null && e.getBurger().getGebruikersnaam().toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if ((e.getBurger()!=null?e.getBurger().getNaam():e.getBeheerder().getNaam()).toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
			else if ((e.getBurger()!=null?e.getBurger().getEmail():e.getBeheerder().getEmail()).toLowerCase().contains(s.toLowerCase()))
				resultaat.add(e);
		}
		
		return resultaat;
	}
	
	public ArrayList<Erfgoed> getInTeLaden()
	{
		return inTeLaden;
	}

	public void setInTeLaden(ArrayList<Erfgoed> inTeLaden)
	{
		this.inTeLaden = inTeLaden;
	}
	
	public void verwijderErfgoed(Erfgoed e)
	{
		m.verwijderErfgoed(e);
		d.verwijderErfgoed(e);
	}
	
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
		for(ChangeListener l : listeners)
		{
			l.stateChanged(e);
		}
	}
}