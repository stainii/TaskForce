package controllers;

import java.sql.Date;

import javax.swing.JOptionPane;

import model.DocumentCMS;
import model.Erfgoed;
import model.Model;

/** Controller dat aanpassingen van een stuk erfgoed regelt. Zelfde manier van werken als DocumentController */

public class ErfgoedController
{
	private Erfgoed voorlopigErfgoed;
	private Erfgoed origineelErfgoed;
	private Databank d;
	private Model m;
	
	public ErfgoedController(Model m, Databank d, Erfgoed e)
	{
		this.origineelErfgoed = e;
		this.voorlopigErfgoed = new Erfgoed(e.getId(), e.getNaam(), e.getPostcode(), e.getDeelgemeente(),
				e.getStraat(), e.getHuisnr(), e.getOmschrijving(), e.getTypeErfgoed(), e.getKenmerken(),
				e.getGeschiedenis(), e.getNuttigeInfo(), e.getLink(), e.getBurgerId(), m);
		this.m = m;
		this.d = d;
	}
	
	
	public Erfgoed getVoorlopigErfgoed()
	{
		return voorlopigErfgoed;
	}


	public Erfgoed getOrigineelErfgoed()
	{
		return origineelErfgoed;
	}


	public void verwijder()
	{
		//d.verwijderErfgoed(origineelErfgoed);		
		m.verwijderErfgoed(origineelErfgoed);
	}
	
	public void update()
	{
		/*origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());
		origineelDocument.setLaatsteWijziging(voorlopigDocument);*/
		
		origineelErfgoed.setBurgerId(voorlopigErfgoed.getBurgerId());
		origineelErfgoed.setDeelgemeente(voorlopigErfgoed.getDeelgemeente());
		origineelErfgoed.setGeschiedenis(voorlopigErfgoed.getGeschiedenis());
		origineelErfgoed.setHuisnr(voorlopigErfgoed.getHuisnr());
		origineelErfgoed.setKenmerken(voorlopigErfgoed.getKenmerken());
		origineelErfgoed.setLink(voorlopigErfgoed.getLink());
		origineelErfgoed.setNaam(voorlopigErfgoed.getNaam());
		origineelErfgoed.setNuttigeInfo(voorlopigErfgoed.getNuttigeInfo());
		origineelErfgoed.setOmschrijving(voorlopigErfgoed.getOmschrijving());
		origineelErfgoed.setPostcode(voorlopigErfgoed.getPostcode());
		origineelErfgoed.setStraat(voorlopigErfgoed.getStraat());
		origineelErfgoed.setTypeErfgoed(voorlopigErfgoed.getTypeErfgoed());
		
		//d.updateErfgoed(origineelErfgoed);
		m.bewerkErfgoed(origineelErfgoed);
		
	}
	public void toevoegen()
	{
		origineelErfgoed.setBurgerId(voorlopigErfgoed.getBurgerId());
		origineelErfgoed.setDeelgemeente(voorlopigErfgoed.getDeelgemeente());
		origineelErfgoed.setGeschiedenis(voorlopigErfgoed.getGeschiedenis());
		origineelErfgoed.setHuisnr(voorlopigErfgoed.getHuisnr());
		origineelErfgoed.setKenmerken(voorlopigErfgoed.getKenmerken());
		origineelErfgoed.setLink(voorlopigErfgoed.getLink());
		origineelErfgoed.setNaam(voorlopigErfgoed.getNaam());
		origineelErfgoed.setNuttigeInfo(voorlopigErfgoed.getNuttigeInfo());
		origineelErfgoed.setOmschrijving(voorlopigErfgoed.getOmschrijving());
		origineelErfgoed.setPostcode(voorlopigErfgoed.getPostcode());
		origineelErfgoed.setStraat(voorlopigErfgoed.getStraat());
		origineelErfgoed.setTypeErfgoed(voorlopigErfgoed.getTypeErfgoed());

		/*origineelDocument.setDatumGewijzigd(d.getDatabankTijd());
		voorlopigDocument.setDatumGewijzigd(d.getDatabankTijd());*/
		
		//origineelErfgoed.setId(d.toevoegenErfgoed(origineelErfgoed));
		m.toevoegenErfgoed(origineelErfgoed);
	}
}