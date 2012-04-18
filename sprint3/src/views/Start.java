package views;

/** Deze klasse start het programma en is het loginvenster. Het volgende panel wordt geladen door de
 *  klasse Laden, die ook in dit bestand staat */


import guiElementen.JLabelFactory;

import java.awt.Color;
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

import administrator.Administrator;

import systemTray.InSystemTray;

import controllers.Databank;
import controllers.Login;
import controllers.mail.MailThuis;
import controllers.mail.WachtwoordMail;

import model.Beheerder;
import model.Model;
@SuppressWarnings("serial")
public class Start extends JPanel implements ActionListener
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
	private boolean sysTrayAlIngeladen;	//is de system tray al ingeladen? zo ja, dan moet hij niet opnieuw gemaakt worden
	
	@Override
	protected void paintComponent(Graphics g) 		//achtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public Start(JFrame fra, boolean sysTrayAlIngeladen)
	{
		this.frame = fra;
		this.sysTrayAlIngeladen = sysTrayAlIngeladen;
		
		m = new Model();
		d = new Databank(m);
		d.getBeheerdersEnBurgersUitDatabank();
		
		setBorder(new EmptyBorder(20,0,0,0) );
		FlowLayout f =new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		setLayout(f);
		
		ex = Executors.newFixedThreadPool(1);
		
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
		
		inloggenBtn.addActionListener(this);
		gebruikersnaamTxt.addActionListener(this);
		wachtwoordTxt.addActionListener(this);
		
	}
	
	public static void main(String args[])
	{
		JFrame f = new JFrame();
		f.setTitle("Herzele erfgoed CMS");
		f.setSize(new Dimension(500,300));
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		
		boolean sysTrayAlIngeladen =  (args.length>0);
		
		f.add(new Start(f, sysTrayAlIngeladen));
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e)		//wordt uitgevoerd bij het klikken op inloggen of op enter duwen in een van de tekstvakken
	{
		login.setVisible(false);
		voortgang.setVisible(true);
		repaint();
		
		if(Administrator.isAdministrator(gebruikersnaamTxt.getText(), wachtwoordTxt.getText()))
		{
			voortgang.setVisible(false);
			logo.setVisible(false);
			frame.dispose();
			new Administrator();		// maakt een nieuw JFrame aan! Moet nog vervangen worden zodat enkel JPanel vervangen wordt binnen DIT frame	
		}
		
		else if (Login.controleerLogin(gebruikersnaamTxt.getText(), wachtwoordTxt.getText()))
		{
			new Laden(frame, voortgangLbl, gebruikersnaamTxt.getText(), sysTrayAlIngeladen,m,d).execute();	//laadt het programma in					
		}
		else
		{
			wachtwoordVergeten.setVisible(true);
			wachtwoordTxt.setText("");
			JOptionPane.showMessageDialog(null, "U bent geen beheerder en niet gemachtigd om dit programma te gebruiken!","Beheerder niet gevonden of wachtwoord foutief",JOptionPane.ERROR_MESSAGE);
			login.setVisible(true);
			voortgang.setVisible(false);
			wachtwoordTxt.grabFocus();
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
			int resultaat = JOptionPane.showConfirmDialog(null,"Om de veiligheid te blijven garanderen wordt het oud wachtwoord verwijderd \nen wordt er een nieuw " +
					"wachtwoord gestuurd naar de gebruiker.\nBent u hiermee akkoord?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if(resultaat == JOptionPane.YES_OPTION)
			{
				WachtwoordMail wachtwoord = new WachtwoordMail();
				
				for(Beheerder b : m.getBeheerders())
				{
					if(gebruikersnaamTxt.getText().equalsIgnoreCase(b.getVoornaam()))
					{
						b.setWachtwoord(wachtwoord.getWachtwoord());
						try {
							d.updateBeheerdersDatabank(b);
						} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();} catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
						
						mail = new MailThuis(b.getEmail(), "Nieuw wachtwoord", wachtwoord,m);
						ex.execute(mail);
					}
				}	
						
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}


class Laden extends SwingWorker<Void,Void>
{
	private JFrame frame;
	private String beheerderNaam;
	private JLabel laden;
	private InSystemTray systemTray;
	private boolean sysTrayAlIngeladen;
	private Model m;
	private Databank d;
	
	public Laden(JFrame f, JLabel laden, String beheerderNaam, boolean sysTrayAlIngeladen, Model model , Databank data)
	{
		this.frame =f;
		this.laden = laden;
		this.beheerderNaam = beheerderNaam;
		this.sysTrayAlIngeladen = sysTrayAlIngeladen;
		this.m = model;
		this.d = data;
	}
	

	@Override
	protected Void doInBackground() throws Exception
	{
		//databank inladen in model
		laden.setText("Bezig met laden databank...");
		d.laadDatabank();
		m.setBeheerder(beheerderNaam);
		
		//Interface (GUI) maken en eigenschappen instellen
		laden.setText("Bezig met laden interface...");
		JFrame f = new JFrame();
		f.setTitle("Herzele Erfgoed CMS");
		f.add(new Hoofd(m, d, f));
		f.setSize(new Dimension(1005,720));
		f.setMinimumSize(new Dimension(1005,700));
		f.setLocationRelativeTo(null);
		
		//system tray inladen
		if (!sysTrayAlIngeladen)
		{
			systemTray = new InSystemTray(m,d);
			f.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					systemTray.zegHallo();	//boodschap weergeven dat de system tray nog werkt
					((JFrame)(e.getSource())).dispose();	//het frame vernietigen
				}
			});
		}
		
		f.setVisible(true);
		
		//het inlogvenster verbergen en daarna vernietigen
		frame.setVisible(false);
		frame.dispose();
		return null;
	}
}