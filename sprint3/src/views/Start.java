package views;

import guiElementen.JLabelFactory;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import systemTray.InSystemTray;

import administrator.Administrator;

import model.Beheerder;
import model.Model;
import controllers.Databank;
import controllers.MD5;
import controllers.mail.MailThuis;
import controllers.mail.WachtwoordMail;

/**
 *	Het inlogvenster en het laden van de applicatie.
 */

@SuppressWarnings("serial")
public class Start extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/ladenBackground.jpg"));
	private Image background = backgroundIcon.getImage();
	private JPasswordField wachtwoordTxt;
	private JTextField gebruikersnaamTxt;
	private JPanel voortgang, login, wachtwoordVergetenPnl;
	private JLabel voortgangLbl, logo, wachtwoordVergeten;
	private JFrame frame;
	private MailThuis mail;
	private ExecutorService ex;
	private Databank d;
	private Model m;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public Start(JFrame fra)
	{
		this.frame = fra;
		
		m = new Model();
		d = new Databank(m);
		
		setBorder(new EmptyBorder(20,0,0,0) );
		FlowLayout f =new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
		
		logo = new JLabel();
		logo.setIcon(new ImageIcon(getClass().getResource("imgs/logo.png")));
		add(logo);
		
		login = new JPanel();
		login.setOpaque(false);
		add(login);
		
		voortgang = new JPanel();
		voortgang.setOpaque(false);
		voortgang.setVisible(false);
		add(voortgang);
		
		voortgangLbl = new JLabelFactory().getMenuTitel("Bezig met laden");
		voortgang.add(voortgangLbl);
		
		wachtwoordVergetenPnl = new JPanel();
		wachtwoordVergetenPnl.setOpaque(false);
		add(wachtwoordVergetenPnl);
		
		//layout login
		login.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		c.gridx =1;
		c.gridy = 1;
		c.weightx = 1;
		login.add(new JLabelFactory().getMenuTitel("Gebruikersnaam"),c);
		
		c.gridx =1;
		c.gridy = 2;
		login.add(new JLabelFactory().getMenuTitel("Wachtwoord"),c);
		
		c.gridx =2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		gebruikersnaamTxt = new JTextField(15);
		login.add(gebruikersnaamTxt,c);
		
		c.gridx =2;
		c.gridy = 2;
		wachtwoordTxt = new JPasswordField(15);
		login.add(wachtwoordTxt,c);
		
		c.gridx = 3;
		c.gridy = 3;
		wachtwoordVergeten = new JLabelFactory().getItalic("<html><u>Wachtwoord vergeten?</u></html>");
		wachtwoordVergeten.setVisible(false);
		login.add(wachtwoordVergeten,c);
		wachtwoordVergeten.addMouseListener(new WachtwoordVergetenListener());
		
		c.gridx = 3;
		c.gridy = 4;
		c.fill = GridBagConstraints.NONE;
		JButton inloggenBtn = new JButton("    Inloggen    ");
		login.add(inloggenBtn,c);
		
		inloggenBtn.addActionListener(new InloggenListener());
		gebruikersnaamTxt.addActionListener(new InloggenListener());
		wachtwoordTxt.addActionListener(new InloggenListener());
	}
	
	public static void main(String args[])
	{
		JFrame f = new JFrame();
		f.setTitle("Herzele Erfgoedbank CMS");
		f.setSize(new Dimension(500,300));
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.add(new Start(f));
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private class InloggenListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			ex = Executors.newFixedThreadPool(1);
			ex.execute(new Inloggen());
		}					
	}
	private class WachtwoordVergetenListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			wachtwoordVergeten.setCursor(hand);
		}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			
			String beheerderDatWiltInloggen = gebruikersnaamTxt.getText();
			boolean isEenBeheerder = false;
			Object[] opties = {"Ja", "Nee"};
			
			for(Beheerder s : m.getBeheerders())
			{
				if(s.getGebruikersnaam().equals(beheerderDatWiltInloggen))
					isEenBeheerder = true;
			}
			
			if(isEenBeheerder)
			{
				int resultaat = JOptionPane.showOptionDialog(null,"Om de veiligheid te blijven garanderen wordt het oud wachtwoord verwijderd \nen wordt er een nieuw " +
						"wachtwoord gestuurd naar de gebruiker.\nBent u hiermee akkoord?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, opties, opties[0]);
				
				if(resultaat == JOptionPane.YES_OPTION)
				{				
					m.setBeheerder(gebruikersnaamTxt.getText());
					WachtwoordMail wachtwoord = new WachtwoordMail(m.getBeheerder());
					
					for(Beheerder b : m.getBeheerders())
					{
						if(gebruikersnaamTxt.getText().equalsIgnoreCase(b.getGebruikersnaam()))
						{
							m.setBeheerder(gebruikersnaamTxt.getText());
							
							try {
								b.setWachtwoord(MD5.convert(wachtwoord.getWachtwoord()));
								d.updateBeheerdersDatabank(b);
							} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();} catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
							
							mail = new MailThuis(b.getEmail(), "Nieuw wachtwoord", wachtwoord,m);
							ex.execute(mail);
						}
					}	
							
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Er kan geen nieuw wachtwoord worden verstuurd omdat de ingevulde beheerder niet werd teruggevonden." +
						"\nControleer of de naam juist is geschreven.","Beheerder niet gevonden",JOptionPane.ERROR_MESSAGE);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	private class Inloggen extends SwingWorker<Void,Void>
	{
		private Hoofd hoofd;
		private InSystemTray systemTray;

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground() throws Exception
		{
			login.setVisible(false);
			voortgang.setVisible(true);
			
			/*INLOGGEN*/
			voortgangLbl.setText("Bezig met inloggen...");
			d.getBeheerdersEnBurgersUitDatabank();
			d.getInstellingen();
			
			if(d.isAdministrator(gebruikersnaamTxt.getText(), wachtwoordTxt.getText()))
			{
				voortgang.setVisible(false);
				logo.setVisible(false);
				new Administrator();		// maakt een nieuw JFrame aan! Moet nog vervangen worden zodat enkel JPanel vervangen wordt binnen DIT frame
				frame.dispose();
			}
			
			else if (d.controleerLogin(gebruikersnaamTxt.getText(), wachtwoordTxt.getText()))
			{
				//LADEN
				//databank inladen in model
				voortgangLbl.setText("Bezig met laden databank...");
				d.laadDatabank();
				m.setBeheerder(gebruikersnaamTxt.getText());
				
				//Interface (GUI) maken en eigenschappen instellen
				voortgangLbl.setText("Bezig met laden interface...");				
				systemTray = new InSystemTray(m,d);
				JFrame f = new JFrame();
				f.setTitle("Herzele Erfgoed CMS");
				hoofd = new Hoofd(m, d, f, systemTray);
				f.add(hoofd);
				f.setSize(new Dimension(1005,720));
				f.setMinimumSize(new Dimension(1005,700));
				f.setLocationRelativeTo(null);
				f.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e)
					{
						hoofd.quit();	//video's uitzetten
						systemTray.zegHallo();	//boodschap weergeven dat de system tray nog werkt
						((JFrame)(e.getSource())).dispose();	//het frame vernietigen
						
					}
				});
				f.setVisible(true);
				
				//het inlogvenster verbergen en daarna vernietigen
				frame.setVisible(false);
				frame.dispose();
			}
			else
			{
				wachtwoordVergeten.setVisible(true);
				wachtwoordTxt.setText("");
				JOptionPane.showMessageDialog(null, "Er werd geen overeenkomst gevonden van gebruikersnaam en wachtwoord.\nGelieve de juiste gegevens in te vullen.","Beheerder niet gevonden of wachtwoord foutief",JOptionPane.ERROR_MESSAGE);
				login.setVisible(true);
				voortgang.setVisible(false);
				wachtwoordTxt.grabFocus();
			}
			return null;
		}
	}
}