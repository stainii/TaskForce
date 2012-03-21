package guiElementen;

/**Interface de types Document in DocumentView. Voorbeelden zijn afbeelding, video, tekst, ... **/
public interface DocumentMedia
{
	void setEditable(boolean b);	//maakt bepaalde velden zichtbaar of bewerkbaar. Bv. bij tekst: veld bewerkbaar, bij afbeelding: "kies een een nieuwe afbeelding"-tekst zichtbaar.
	void quit();	//wat moet er gebeuren als er wordt teruggekeerd naar een ander scherm, of als het programma wordt afgesloten? Bv. bij video moet de video gestopt en disposet worden.
}
