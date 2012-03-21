package controllers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login 
{
	/**Controleert de login **/
	
	private final static String connectie = "jdbc:odbc:projecten2";
	
	public static boolean controleerLogin(String gebruikersnaam, String wachtwoord) 
	{
		Connection c = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		boolean correct = false;
		
		try
		{
			c = DriverManager.getConnection(connectie);
			s = c.prepareStatement("SELECT COUNT (*) FROM Beheerder WHERE Gebruikersnaam=? AND Wachtwoord=?");
			s.setString(1, gebruikersnaam);
			s.setString(2, convert(wachtwoord));
			rs = s.executeQuery();
			
			rs.next();
			if (rs.getInt(1) == 1)		//als er exact 1 persoon is met de combinatie gebruikersnaam-passwoord dan mag hij inloggen
				correct =true;			//dit getal is 0 als de combinatie niet klopt. Het zou nooit meer dan 1 mogen zijn, als dit 
										//toch het geval is dan is er een dubbele gebruikersnaam (met hetzelfde wachtwoord), wat niet kan.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			
			
		}
		return correct;
	}
	
	
	/**Converteert geëncrypteerde data naar tekst **/
	private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
	/**Converteert tekst naar geëncrypteerde data **/
	public static String convert(String text) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
	
}
