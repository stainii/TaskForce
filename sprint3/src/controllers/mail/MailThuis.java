package controllers.mail;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**De instellingen voor het versturen van een mail buiten school*/

public class MailThuis implements Runnable
{
	private String aan;
	private String onderwerp;
	private SoortMail mail;
	
	private static final String host = "smtp.gmail.com";
	private static final int port =587;
	private static final String user = "task.forceb2@gmail.com";
	private static final String pwd ="azertyb2";
	
	public MailThuis(String aan, String onderwerp, SoortMail mail)
	{
		this.aan=aan;
		this.onderwerp=onderwerp;
		this.mail=mail;
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
		catch(MessagingException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void run() {
		sendMail();
	}
}
