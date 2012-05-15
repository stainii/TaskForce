package controllers.mail;

import model.Beheerder;


/**Verstuurt een mail naar de nieuwe beheerder **/

public class NieuweBeheerderMail implements SoortMail
{
	private Beheerder beheerder;
	private String wachtwoord;
	
	public NieuweBeheerderMail(Beheerder b, String w)
	{
		this.beheerder = b;
		this.wachtwoord = w;
	}
	public String getMail()
	{
		String nieuweBeheerderMail = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
				"<html><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><style type=\"text/css\">" +
				"/* Client-specific Styles */"+
				"#outlook a { padding: 0; }	/* Force Outlook to provide a \"view in browser\" button. */" +
				"body { width: 100% !important; }" +
				".ReadMsgBody { width: 100%; }" + 
				".ExternalClass { width: 100%; display:block !important; } /* Force Hotmail to display emails at full width */" +
				"/* Reset Styles */"+ 
				"/* Add 100px so mobile switch bar doesn't cover street address. */" +
				"html, body { background-color: #c7c7c7; margin: 0; padding: 0; }" +
				"img { height: auto; line-height: 100%; outline: none; text-decoration: none; display: block;}" +
				"br, strong br, b br, em br, i br { line-height:100%; }" +
				"h1, h2, h3, h4, h5, h6 { line-height: 100% !important; -webkit-font-smoothing: antialiased; }" +
				"h1 a, h2 a, h3 a, h4 a, h5 a, h6 a { color: blue !important; }" +
				"h1 a:active, h2 a:active,  h3 a:active, h4 a:active, h5 a:active, h6 a:active {color: red !important; }" +
				"/* Preferably not the same color as the normal header link color.  There is limited support for psuedo classes in email clients, this was added just for good measure. */" +
				"h1 a:visited, h2 a:visited,  h3 a:visited, h4 a:visited, h5 a:visited, h6 a:visited { color: purple !important; }" +
				"/* Preferably not the same color as the normal header link color. There is limited support for psuedo classes in email clients, this was added just for good measure. */" + 
				"table td, table tr { border-collapse: collapse; }" +
				".yshortcuts, .yshortcuts a, .yshortcuts a:link,.yshortcuts a:visited, .yshortcuts a:hover, .yshortcuts a span {" +
				"color: black; text-decoration: none !important; border-bottom: none !important; background: none !important;" +
				"}	/* Body text color for the New Yahoo.  This example sets the font of Yahoo's Shortcuts to black. */" +
				"/* This most probably won't work in all email clients. Don't include <code _tmplitem=\"385\" > blocks in email. */" +
				"code { white-space: normal;word-break: break-all;}" +
				"#background-table { background-color: #c7c7c7; }" +
				"/* Webkit Elements */" +
				"#top-bar { border-radius:6px 6px 0px 0px; -moz-border-radius: 6px 6px 0px 0px; -webkit-border-radius:6px 6px 0px 0px; -webkit-font-smoothing: antialiased; background-color: #2E2E2E; color: #888888; }" +
				"#top-bar a { font-weight: bold; color: #eeeeee; text-decoration: none;}" +
				"#footer { border-radius:0px 0px 6px 6px; -moz-border-radius: 0px 0px 6px 6px; -webkit-border-radius:0px 0px 6px 6px; -webkit-font-smoothing: antialiased; }" +
				"/* Fonts and Content */" +
				"body { font-family: Helvetica Neue, Arial, Helvetica, Geneva, sans-serif; }" +
				".header-content, .footer-content-left, .footer-content-right { -webkit-text-size-adjust: none; -ms-text-size-adjust: none; }" +
				"/* Prevent Webkit and Windows Mobile platforms from changing default font sizes on header and footer. */" +
				".header-content { font-size: 12px; color: #888888; }" +
				".header-content a { font-weight: bold; color: #eeeeee; text-decoration: none; }" +
				"#headline p { color: #eeeeee; font-family: Helvetica Neue, Arial, Helvetica, Geneva, sans-serif; font-size: 36px; text-align: center; margin-top:0px; margin-bottom:30px; }" +
				"#headline p a { color: #eeeeee; text-decoration: none; }" +
				".article-title { font-size: 18px; line-height:24px; color: #b0b0b0; font-weight:bold; margin-top:0px; margin-bottom:18px; font-family: Helvetica Neue, Arial, Helvetica, Geneva, sans-serif; }" +
				".article-title a { color: #b0b0b0; text-decoration: none; }" +
				".article-title.with-meta {margin-bottom: 0;}" +
				".article-meta { font-size: 13px; line-height: 20px; color: #ccc; font-weight: bold; margin-top: 0;}" +
				".article-content { font-size: 13px; line-height: 18px; color: #444444; margin-top: 0px; margin-bottom: 18px; font-family: Helvetica Neue, Arial, Helvetica, Geneva, sans-serif; }" +
				".article-content a { color: #2f82de; font-weight:bold; text-decoration:none; }" +
				".article-content img { max-width: 100% }" +
				".article-content ol, .article-content ul { margin-top:0px; margin-bottom:18px; margin-left:19px; padding:0; }" +
				".article-content li { font-size: 13px; line-height: 18px; color: #444444; }" +
				".article-content li a { color: #2f82de; text-decoration:underline; }" +
				".article-content p {margin-bottom: 15px;}" +
				".footer-content-left { font-size: 12px; line-height: 15px; color: #888888; margin-top: 0px; margin-bottom: 15px; }" +
				".footer-content-left a { color: #eeeeee; font-weight: bold; text-decoration: none; }" +
				".footer-content-right { font-size: 11px; line-height: 16px; color: #888888; margin-top: 0px; margin-bottom: 15px; }" +
				".footer-content-right a { color: #eeeeee; font-weight: bold; text-decoration: none; }" +
				"#footer { background-color: #000000; color: #888888; }" +
				"#footer a { color: #eeeeee; text-decoration: none; font-weight: bold; }" +
				"#permission-reminder { white-space: pre-wrap; }" +
				"#street-address { color: #ffffff; white-space: pre-wrap; }" +
				"</style>" +

				"<!--[if gte mso 9]>" +
				"<style _tmplitem=\"385\" >" +
				".article-content ol, " +
				".article-content ul {margin: 0 0 0 24px;padding: 0;list-style-position: inside;}"+
				"</style>" +
				"<![endif]-->" +
				"</head>" +
				"<body>" +
					"<table id=\"background-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
				"<tbody>" +
					"<tr>" +
					"<td align=\"center\" bgcolor=\"#c7c7c7\">" +
				"<table class=\"w640\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"640\">" +
				"<tbody><tr><td class=\"w640\" height=\"20\" width=\"640\"></td></tr>" +
				"<tr>" +
				"<td class=\"w640\" width=\"640\">" +
				"<table id=\"top-bar\" class=\"w640\" bgcolor=\"#000000\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"640\">" +
				"<tbody><tr>" +					/*Hierboven staat achtergrondkleur titel balk */
				"<td class=\"w15\" width=\"15\"></td>" +
				"<td class=\"w325\" align=\"left\" valign=\"middle\" width=\"350\">" +
				"<table class=\"w325\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"350\">" +
				" <tbody><tr><td class=\"w325\" height=\"8\" width=\"350\"></td></tr>" +
				" </tbody></table>" +
				"<table class=\"w325\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"350\">" +
				"<tbody><tr><td class=\"w325\" height=\"8\" width=\"350\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"<td class=\"w255\" align=\"right\" valign=\"middle\" width=\"255\">" +
				"<table class=\"w255\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"255\">" +
				"<tbody><tr><td class=\"w255\" height=\"8\" width=\"255\"></td></tr>" +
				"</tbody></table>" +
	
				" <table class=\"w255\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"255\">" +
				" <tbody><tr><td class=\"w255\" height=\"8\" width=\"255\"></td></tr>" +
				" </tbody></table>" +
				"</td>" +
				" <td class=\"w15\" width=\"15\"></td>" +
				"</tr>" +
				"</tbody></table>" +
				" </td>" +
				"</tr>" +
				"<tr>" +
				" <td id=\"header\" class=\"w640\" align=\"center\" bgcolor=\"#00da15\" width=\"640\">" +
				"<table class=\"w640\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"640\">" +
				"<tbody><tr><td class=\"w30\" width=\"30\"></td><td class=\"w580\" height=\"30\" width=\"580\"></td><td class=\"w30\" width=\"30\"></td></tr>" +
				"<tr>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"<td class=\"w580\" width=\"580\"><div id=\"headline\" align=\"center\"><p><strong>Welkom!</strong></p></div></td>" +
				" <td class=\"w30\" width=\"30\"></td>" +
				"</tr>" +
				"</tbody></table>" +
				"</td>" +
				"</tr>" +
				"<tr><td class=\"w640\" bgcolor=\"#ffffff\" height=\"30\" width=\"640\"></td></tr>" +
				"<tr id=\"simple-content-row\"><td class=\"w640\" bgcolor=\"#ffffff\" width=\"640\">" +
				"<table class=\"w640\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"640\">" +
				"<tbody><tr>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"<td class=\"w580\" width=\"580\">" +
				"<repeater>" +
				"<layout label=\"Text only\">" +
				"<table class=\"w580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"580\">" +
				"<tbody><tr>" +
				"<td class=\"w580\" width=\"580\">" +
				"<p class=\"article-title\" align=\"left\"><singleline label=\"Title\">" +

				// 1ste titel		
				"<strong>Welkom op de erfgoedbank van Herzele</strong>"+
				"</singleline></p>" +
				"<div class=\"article-content\" align=\"left\">" +
				"<multiline label=\"Description\">" +
				
				// Tekst
				"<p>Beste " + beheerder.getNaam() + ",</p>"+
				"<p>U bent successvol geregistreerd als beheerder voor de erfgoedbank van Herzele. " +
				"U kan zich met uw inloggegevens, die u onderaan deze mail vindt, inloggen op het CMS. " +
				"Daarna kan u eventueel een nieuw wachtwoord instellen in het instellingen-menu.</p> "+
				"<p>Wij danken u vriendelijk voor uw medewerking.</p>"+
				"Met vriendelijke groet" +
				"<p>Gemeente Herzele</p>"+
				"</multiline>" +
				"</div>" +
				"</td>" +
				"</tr>" +

				"<tr><td class=\"w580\" height=\"10\" width=\"580\"></td></tr>" +
				"</tbody></table>" +
				"</layout>" +
				"<layout label=\"Text with right-aligned image\">" +
				"<table class=\"w580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"580\">" +
				"<tbody><tr>" +
		
				"</tr>" +
				"<tr><td class=\"w580\" height=\"10\" width=\"580\"></td></tr>" +
				"</tbody></table>" +
				"</layout>" +
				"<layout label=\"Text with left-aligned image\">" +
				"<table class=\"w580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"580\">" +
				"<tbody><tr>" +
				"<td class=\"w580\" width=\"580\">" +
				"<p class=\"article-title\" align=\"left\"><singleline label=\"Title\">" +
				
				// 2de titel			
				"<strong>"+"Uw inloggegevens"+"</strong>" +
				"</singleline></p>" +
				"<table>"+
					"<tr>"+
						"<td>Gebruikersnaam:</td>" +
						"<td>" + beheerder.getVoornaam() +"</td>"+
						
					"</tr>" +
					"<tr>" +
						"<td>Wachtwoord:</td>" +
						"<td>" + wachtwoord + "</td>"+
					"</tr>"+
				"</table>" +
				"</multiline>" +
				"</div>" +
				"</td>" +
				"</tr>" +
				"<tr><td class=\"w580\" height=\"10\" width=\"580\"></td></tr>" +
				"</tbody></table>" +
				"</layout>" +
				"<layout label=\"Two columns\">" +
				"<table class=\"w580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"580\">" +
				"<tbody><tr>" +
				"<td class=\"w275\" valign=\"top\" width=\"275\">" +
				"<table class=\"w275\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"275\">" +
				"<tbody>" +
	
				"<tr><td class=\"w275\" height=\"10\" width=\"275\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"<td class=\"w275\" valign=\"top\" width=\"275\">" +
				"<table class=\"w275\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"275\">" +
				"<tbody>" +
			
				"<tr><td class=\"w275\" height=\"10\" width=\"275\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"</tr>" +
				"</tbody></table>" +
				"</layout>" +
				"<layout label=\"Image gallery\">" +
				"<table class=\"w580\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"580\">" +
				"<tbody><tr>" +
				"<td class=\"w180\" valign=\"top\" width=\"180\">" +
				"<table class=\"w180\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"180\">" +
				"<tbody><tr>" +
				
				"</tr>" +
				"<tr><td class=\"w180\" height=\"10\" width=\"180\"></td></tr>" +
				"</tbody></table>" + 
				"</td>" +
				"<td width=\"20\"></td>" +
				"<td class=\"w180\" valign=\"top\" width=\"180\">" +
				"<table class=\"w180\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"180\">" +
				"<tbody><tr>" +
			
				"</tr>" +
				"<tr><td class=\"w180\" height=\"10\" width=\"180\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"<td width=\"20\"></td>" +
				"<td class=\"w180\" valign=\"top\" width=\"180\">" +
				"<table class=\"w180\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"180\">" +
				"<tbody><tr>" +
			
				"</tr>" +
				"<tr><td class=\"w180\" height=\"10\" width=\"180\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"</tr>" +
				"</tbody></table>" +
				"</layout>" +
				"</repeater>" +
				"</td>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"</tr>" +
				"</tbody></table>" +
				"</td></tr>"+
				"<tr><td class=\"w640\" bgcolor=\"#ffffff\" height=\"15\" width=\"640\"></td></tr>" +
				"<tr>" +
				"<td class=\"w640\" width=\"640\">" +
				"<table id=\"footer\" class=\"w640\" bgcolor=\"#000000\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"640\">" +
				"<tbody><tr><td class=\"w30\" width=\"30\"></td><td class=\"w580 h0\" height=\"30\" width=\"360\"></td><td class=\"w0\" width=\"60\"></td><td class=\"w0\" width=\"160\"></td><td class=\"w30\" width=\"30\"></td></tr>" +
				"<tr>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"<td class=\"w580\" valign=\"top\" width=\"360\">" +
				"<span class=\"hide\"><p id=\"permission-reminder\" class=\"footer-content-left\" align=\"left\">&nbsp;</p></span>" +
				"<p class=\"footer-content-left\" align=\"left\"><preferences>Task-Force</preferences></p>" +
				"</td>" +
				"<td class=\"hide w0\" width=\"60\"></td>" +
				"<td class=\"hide w0\" valign=\"top\" width=\"160\">" +
				"<p id=\"street-address\" class=\"footer-content-right\" align=\"right\">&nbsp;</p>" +
				"</td>" +
				"<td class=\"w30\" width=\"30\"></td>" +
				"</tr>" +
				"<tr><td class=\"w30\" width=\"30\"></td><td class=\"w580 h0\" height=\"15\" width=\"360\"></td><td class=\"w0\" width=\"60\"></td><td class=\"w0\" width=\"160\"></td><td class=\"w30\" width=\"30\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"</tr>" +
				"<tr><td class=\"w640\" height=\"60\" width=\"640\"></td></tr>" +
				"</tbody></table>" +
				"</td>" +
				"</tr>" +
				"</tbody></table></body></html>"
				;
		return nieuweBeheerderMail;
	}
}