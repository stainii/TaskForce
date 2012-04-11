package controllers.mail;

import controllers.WachtwoordGenerator;

public class WachtwoordMail implements SoortMail 
{
	private String wachtwoord;
	public WachtwoordMail()
	{
		wachtwoord = WachtwoordGenerator.randomstring();
	}
	
	@Override
	public String getMail() 
	{	
		return "Beste ...\n" + "Er werd een nieuw wachtwoord aangemaakt voor uw account. Als u een gepersonaliseerd wachtwoord wilt"
				+" moet u contact opnemen met de administrator en het onderstaand wachtwoord en het nieuwe wachtwoord melden."
				+ "\n\n Uw gegevens:"
				+ "Naam: \n"
				+"Wachtwoord: " + wachtwoord;
	}	
	public String getWachtwoord(){
		return wachtwoord;
	}
}
