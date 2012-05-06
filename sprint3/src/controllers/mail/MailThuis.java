package controllers.mail;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import model.Model;

/**De instellingen voor het versturen van een mail buiten school*/

public class MailThuis implements Runnable
{
	private String aan;
	private String onderwerp;
	private SoortMail mail;
	private Model m;
	
	private static String host;		// "smtp.gmail.com"
	private static int port;		// 587
	private static String user; 		//= "task.forceb2@gmail.com";
	private static String pwd; 		//="azertyb2";
	
	public MailThuis(String aan, String onderwerp, SoortMail mail, Model model)
	{
		this.aan=aan;
		this.onderwerp=onderwerp;
		this.mail=mail;
		this.m = model;
		
		if (!m.getEmailVoorkeur("EmailOut").equals(""))
		{
			host = m.getEmailVoorkeur("EmailOut");
			port = Integer.parseInt(m.getEmailVoorkeur("EmailPoort"));
			user = m.getEmailVoorkeur("EmailGebruikernaam");
			pwd = m.getEmailVoorkeur("EmailWachtwoord");
		}
		else
		{
			host = "smtp.gmail.com";
			port = 587;
			user = "task.forceb2@gmail.com";
			pwd = "azertyb2";
		}
		
	}
		
	public void sendMail()
	{
		Properties pr = new Properties();
		pr.put("mail.transport.protocol", "smtp");
		pr.put("mail.smtp.host",host);
		pr.put("mail.smtp.auth","true");
		pr.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getDefaultInstance(pr);
		//session.setDebug(true);		// mag op einde weg = is om te debuggen 
		
		try{
			Transport trans = session.getTransport();
		
			MimeMessage message = new MimeMessage(session);
			message.setSubject(onderwerp);
			message.setContent(mail.getMail(),"text/html");
		
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(aan));
			
		
			trans.connect(host,port,user,pwd);
		
			trans.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			trans.close();
		}
		/*catch(MessagingException ex)
		{
			throw new RuntimeException(ex);
		}*/
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "De mailfunctie werkt via de servers van GMail.\nHet lijkt dat deze echter geblokkeerd wordt op het huidig netwerk.\nIn de finale versie zal dit systeem werken met jullie eigen serverinstellingen.", "Fout bij het versturen van een mail", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		sendMail();
	}
}
