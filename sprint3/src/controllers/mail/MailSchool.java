package controllers.mail;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.DocumentCMS;

/**De instellingen voor het versturen van een mail in school*/

public class MailSchool implements Runnable
{
	private DocumentCMS document;
	private String aan;
	private String onderwerp;
	private SoortMail mail;
	
	private static final String host = "mail-out.hogent.be";
	private static final int port =25;
	private static final String user = "095599kl";
	
	private static final String pwd ="+ypbej!";
	
	public MailSchool(String aan, String onderwerp, DocumentCMS doc, SoortMail mail)
	{
		this.aan=aan;
		this.onderwerp=onderwerp;
		this.document=doc;
		this.mail=mail;
	}

	public void sendMail()
	{
		Properties pr = new Properties();
		pr.put("mail.transport.protocol", "smtp");
		pr.put("mail.smtp.host",host);
		pr.put("mail.smtp.auth","true");
		
		Session session = Session.getDefaultInstance(pr);
		//session.setDebug(true);		// mag op einde weg = is om te debuggen 
		
		try{
			Transport trans = session.getTransport();
		
			MimeMessage message = new MimeMessage(session);
			message.setSubject(onderwerp);
			message.setContent(mail.getMail(),"text/html");
		
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(aan));
			
		
			trans.connect(host,port,user,pwd);
			//trans.connect(host,user);
		
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
