package guiElementen;

import harsh.p.raval.lightbox.LightBox;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controllers.Databank;
import controllers.Login;

import views.OverzichtContent;
import views.OverzichtView;

import model.Instellingen;
import model.Model;

@SuppressWarnings("serial")
public class InstelView extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/background_instelviewLang.png"));
	private Image background = backgroundIcon.getImage();
	private JPanel instelPanel;
	private JLabel bewerken,opslaan,close,wachtwoordBtn;
	private Model m;
	private JFrame f;
	private OverzichtView v;
	private Databank d;
	private JLabelFactory jLabelFactory;
	private LightBox box;
	private JTextField nieuweRedenTxt, emailOutTxt, poortTxt, userTxt;
	private JPasswordField pwdTxt,oudW, nieuwW1,nieuwW2;
	private DefaultListModel redenModel;
	private JList standaardRedenen;
	private ArrayList<String> reden;
	private boolean inPanel = false;
	String nieuweReden = "Typ hier een nieuwe reden...";
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public InstelView(Model model,JFrame frame,OverzichtView view,Databank data)
	{
		this.m = model;
		this.f = frame;
		this.v = view;
		this.d = data;
		reden = new ArrayList<String>();
		
		jLabelFactory = new JLabelFactory();
		
		setSize(new Dimension(510,570));
		setOpaque(false);
		setLayout(null);
		setBackground(Color.GRAY);
	
		// Absolute positionering 
		JLabel titel = jLabelFactory.getTitel("Instellingen voor " + m.getBeheerder().getVoornaam());
		Dimension sizeTitel = titel.getPreferredSize();
		titel.setBounds(20,5,sizeTitel.width,sizeTitel.height);
		
		close = new JLabel();
		close.addMouseListener(new CloseListener());
		close.setIcon(new ImageIcon(getClass().getResource("imgs/close.png")));
		Dimension sizeClose = close.getPreferredSize();
		close.setBounds(482, 3, sizeClose.width, sizeClose.height);
		
		JLabel overzicht = jLabelFactory.getMenuTitel("Overzicht");
		Dimension sizeOverzicht = overzicht.getPreferredSize();
		overzicht.setBounds(10, 35, sizeOverzicht.width, sizeOverzicht.height);
		
		JLabel ikWil = jLabelFactory.getNormaleTekst("Bij het opstarten wil ik ");
		Dimension sizeIkWil = ikWil.getPreferredSize();
		ikWil.setBounds(15, 55, sizeIkWil.width, sizeIkWil.height);
		
		//eerste groep radiobuttons
		ImageIcon selected = new ImageIcon(getClass().getResource("imgs/radiobutton_selected.png"));
		ImageIcon notSelected = new ImageIcon(getClass().getResource("imgs/radiobutton_normal.png"));
		ImageIcon hover = new ImageIcon(getClass().getResource("imgs/radiobutton_hover.png"));
		
		// Erfgoed
		JRadioButton erfgoed = new JRadioButton("Erfgoeden");
		erfgoed.setForeground(Color.white);
		erfgoed.setOpaque(false);
		erfgoed.setSelectedIcon(selected);		
		erfgoed.setIcon(notSelected);
		erfgoed.setRolloverIcon(hover);
				
		erfgoed.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setTypeContent("Erfgoed"); //Scherm laten veranderen in gekozen keuze
				m.getBeheerder().setTypeContent(m.getBeheerder().getId(),"Erfgoed" );
				d.updateInstellingen("Erfgoed", m.getInstellingenId("TypeContent"));
			}
		});
		Dimension sizeErfgoed = erfgoed.getPreferredSize();
		erfgoed.setBounds(16, 75, sizeErfgoed.width, sizeErfgoed.height);
		
		// Documenten
		JRadioButton documenten = new JRadioButton("Documenten");
		documenten.setForeground(Color.white);
		documenten.setOpaque(false);
		documenten.setSelectedIcon(selected);		
		documenten.setIcon(notSelected);
		documenten.setRolloverIcon(hover);
		documenten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				v.getContent().setTypeContent("Documenten");
				m.getBeheerder().setTypeContent(m.getBeheerder().getId(),"Documenten" );
				d.updateInstellingen("Documenten", m.getInstellingenId("TypeContent"));
			}
		});
		Dimension sizeDocumenten = documenten.getPreferredSize();
		documenten.setBounds(16, 95,sizeDocumenten.width, sizeDocumenten.height);
		
		ButtonGroup group1 = new ButtonGroup();
		group1.add(erfgoed);
		group1.add(documenten);
		
		// in een
		JLabel inEen = jLabelFactory.getNormaleTekst("in een");
		Dimension sizeInEen = inEen.getPreferredSize();
		inEen.setBounds(190, 89, sizeInEen.width, sizeInEen.height);
		
		// tweede groep radiobuttons
		JRadioButton tegel = new JRadioButton("lijst van tegels");
		tegel.setForeground(Color.white);
		tegel.setOpaque(false);
		tegel.setSelectedIcon(selected);		
		tegel.setIcon(notSelected);
		tegel.setRolloverIcon(hover);
		
		tegel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setView("TegelView");
				m.getBeheerder().setView(m.getBeheerder().getId(),"TegelView" );
				d.updateInstellingen("TegelView", m.getInstellingenId("View"));
			}
		});
		Dimension sizeTegel = tegel.getPreferredSize();
		tegel.setBounds(290, 75, sizeTegel.width, sizeTegel.height);
		
		JRadioButton tabel = new JRadioButton("tabel");
		tabel.setForeground(Color.white);
		tabel.setOpaque(false);
		tabel.setSelectedIcon(selected);		
		tabel.setIcon(notSelected);
		tabel.setRolloverIcon(hover);
		
		tabel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				v.getContent().setView("LijstView");
				m.getBeheerder().setView(m.getBeheerder().getId(),"LijstView" );
				d.updateInstellingen("LijstView", m.getInstellingenId("View"));
			}
		});
		Dimension sizeTabel = tabel.getPreferredSize();
		tabel.setBounds(290, 95, sizeTabel.width, sizeTabel.height);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(tegel);
		group2.add(tabel);
		
		// zien
		JLabel zien = jLabelFactory.getNormaleTekst("zien.");
		Dimension sizeZien = zien.getPreferredSize();
		zien.setBounds(440, 89, sizeZien.width, sizeZien.height);
		
		// titel documenten
		JLabel documentenLbl = jLabelFactory.getMenuTitel("Document");
		Dimension sizeDocumentenLbl = documentenLbl.getPreferredSize();
		documentenLbl.setBounds(10, 130, sizeDocumentenLbl.width, sizeDocumentenLbl.height);
		
		JLabel reden = jLabelFactory.getNormaleTekst("Standaard redenen voor afwijzing");
		Dimension sizeStandaardReden = reden.getPreferredSize();
		reden.setBounds(15, 150, sizeStandaardReden.width, sizeStandaardReden.height);
		
		// Standaardreden
		redenModel = new DefaultListModel();
		for(int i=0;i<m.getStandaardReden().size();i++)
		{
			redenModel.addElement(m.getStandaardReden().get(i));
		}
		
		standaardRedenen = new JList(redenModel);
		standaardRedenen.setLayoutOrientation(JList.VERTICAL);
		standaardRedenen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane redenScroll = new JScrollPane(standaardRedenen);
		redenScroll.setPreferredSize(new Dimension(150,130));
		redenScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		redenScroll.setBounds(35,170,redenScroll.getPreferredSize().width,redenScroll.getPreferredSize().height);
		
		// Textfield voor nieuwe reden
		nieuweRedenTxt = new JTextField(nieuweReden);
		nieuweRedenTxt.setForeground(Color.gray);
		nieuweRedenTxt.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				nieuweRedenTxt.setText("");
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if(nieuweRedenTxt.getText().equals(nieuweReden))
					nieuweRedenTxt.setForeground(Color.gray);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(nieuweRedenTxt.getText().equals(nieuweReden))
					nieuweRedenTxt.setForeground(Color.black);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		nieuweRedenTxt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				nieuweRedenTxt.setForeground(Color.gray);
				if(nieuweRedenTxt.getText().isEmpty())
				{
					nieuweRedenTxt.setText(nieuweReden);
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				nieuweRedenTxt.setForeground(Color.black);
				if(nieuweRedenTxt.getText().equals(nieuweReden));
				{
					nieuweRedenTxt.setText("");
					nieuweRedenTxt.setColumns(14);
				}
			}
		});

		Dimension sizeNieuweRedenTxt = nieuweRedenTxt.getPreferredSize();
		nieuweRedenTxt.setBounds(220, 190, sizeNieuweRedenTxt.width, sizeNieuweRedenTxt.height);
		
		// Toevoegen nieuwe reden 
		final JLabel nieuweRedenBtn = new JLabel();
		nieuweRedenBtn.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegen.png")));
		nieuweRedenBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!nieuweRedenTxt.getText().equals(nieuweReden))
				{
					m.toevoegenInstelling(new Instellingen(0,"StandaardReden",nieuweRedenTxt.getText(),m.getBeheerder().getId()));
					d.voegInstellingToe("StandaardReden",nieuweRedenTxt.getText() , m.getBeheerder().getId());		//toevoegen aan databank
					redenModel.addElement(nieuweRedenTxt.getText());			// toevoegen aan JList
					nieuweRedenTxt.setText(nieuweReden);
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				nieuweRedenBtn.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		Dimension sizeNieuweRedenBtn = nieuweRedenBtn.getPreferredSize();
		nieuweRedenBtn.setBounds(240,215,sizeNieuweRedenBtn.width,sizeNieuweRedenBtn.height);

		// verwijderen reden
		final JLabel verwijderenBtn = new JLabel();
		verwijderenBtn.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		verwijderenBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
					
					m.setInstelling(standaardRedenen.getSelectedValue().toString());
					d.verwijderStandaardReden(m.getInstelling());
					m.verwijderStandaardReden(standaardRedenen.getSelectedValue().toString());
					redenModel.remove(standaardRedenen.getSelectedIndex());
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				verwijderenBtn.setCursor(hand);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		Dimension sizeVerwijderenBtn = verwijderenBtn.getPreferredSize();
		verwijderenBtn.setBounds(240, 250, sizeVerwijderenBtn.width, sizeVerwijderenBtn.height);
		
		//Wachtwoord
		JLabel wachtwoordLbl = jLabelFactory.getMenuTitel("Wachtwoord wijzigen");
		Dimension sizeWachtwoord = wachtwoordLbl.getPreferredSize();
		wachtwoordLbl.setBounds(10,310,sizeWachtwoord.width,sizeWachtwoord.height);
		
		JLabel oudwLbl = jLabelFactory.getNormaleTekst("Oud wachtwoord");
		Dimension sizeoudwLbl = oudwLbl.getPreferredSize();
		oudwLbl.setBounds(20,330,sizeoudwLbl.width,sizeoudwLbl.height);
		
		oudW = new JPasswordField();
		oudW.addKeyListener(new OudwachtwoordListener());
		oudW.setColumns(15);
		Dimension sizeOudW = oudW.getPreferredSize();
		oudW.setBounds(140,330,sizeOudW.width,sizeOudW.height);
		
		JLabel nieuwW1Lbl = jLabelFactory.getNormaleTekst("Nieuw wachtwoord");
		Dimension sizenieuwW1Lbl = nieuwW1Lbl.getPreferredSize();
		nieuwW1Lbl.setBounds(20,352,sizenieuwW1Lbl.width,sizenieuwW1Lbl.height);
		
		nieuwW1 = new JPasswordField();
		nieuwW1.setColumns(15);
		nieuwW1.setEnabled(false);
		Dimension sizenieuwW1 = nieuwW1.getPreferredSize();
		nieuwW1.setBounds(140,352,sizenieuwW1.width,sizenieuwW1.height);
		
		JLabel herhaalLbl = jLabelFactory.getNormaleTekst("Herhaal wachtwoord");
		Dimension sizeherhaalLbl = herhaalLbl.getPreferredSize();
		herhaalLbl.setBounds(20,374,sizeherhaalLbl.width,sizeherhaalLbl.height);
		
		nieuwW2 = new JPasswordField();
		nieuwW2.setColumns(15);
		nieuwW2.setEnabled(false);
		Dimension sizenieuwW2 = nieuwW2.getPreferredSize();
		nieuwW2.setBounds(140,374,sizenieuwW2.width,sizenieuwW2.height);
		
		wachtwoordBtn = new JLabel();
		wachtwoordBtn.addMouseListener(new WachtwoordOpslaanListener());
		wachtwoordBtn.setVisible(false);
		wachtwoordBtn.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		Dimension sizeWbtn = wachtwoordBtn.getPreferredSize();
		wachtwoordBtn.setBounds(320,350, sizeWbtn.width, sizeWbtn.height);
		
		
		oudW.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if(oudW.getText().isEmpty())
				{
					nieuwW1.setEnabled(false);
					nieuwW2.setEnabled(false);
					wachtwoordBtn.setVisible(false);
				}	
			}
		});
		
		// Email
		JLabel emailLbl = jLabelFactory.getMenuTitel("E-mail voorkeuren");
		Dimension sizeEmailLbl = emailLbl.getPreferredSize();
		emailLbl.setBounds(10, 400, sizeEmailLbl.width, sizeEmailLbl.height);
		
		JLabel emailzin = jLabelFactory.getNormaleTekst("Geef hieronder uw E-mail instellingen");
		Dimension sizeEmailzin = emailzin.getPreferredSize();
		emailzin.setBounds(15,420,sizeEmailzin.width,sizeEmailzin.height);
		
		JLabel emailOut = jLabelFactory.getNormaleTekst("Email-Out (smtp)");
		Dimension sizeEmailOut = emailOut.getPreferredSize();
		emailOut.setBounds(20,452,sizeEmailOut.width,sizeEmailOut.height);
		
		JLabel poortLbl = jLabelFactory.getNormaleTekst("Poort");
		Dimension sizePoortLbl = poortLbl.getPreferredSize();
		poortLbl.setBounds(20,475,sizePoortLbl.width,sizePoortLbl.height);
		
		emailOutTxt = new JTextField();
		emailOutTxt.setColumns(15);
		Dimension sizeEmailOutTxt = emailOutTxt.getPreferredSize();
		emailOutTxt.setBounds(130, 450, sizeEmailOutTxt.width, sizeEmailOutTxt.height);
		
		poortTxt = new JTextField();
		poortTxt.setColumns(15);
		Dimension sizePoortTxt = poortTxt.getPreferredSize();
		poortTxt.setBounds(130, 475, sizePoortTxt.width, sizePoortTxt.height);
		
		JLabel userLbl =  jLabelFactory.getNormaleTekst("Gebruikersnaam");
		Dimension sizeUserLbl = userLbl.getPreferredSize();
		userLbl.setBounds(20,500,sizeUserLbl.width,sizeUserLbl.height);
		
		userTxt = new JTextField();
		userTxt.setColumns(15);
		Dimension sizeUserTxt = userTxt.getPreferredSize();
		userTxt.setBounds(130,500,sizeUserTxt.width,sizeUserTxt.height);
		
		JLabel pwdLbl =  jLabelFactory.getNormaleTekst("Wachtwoord");
		Dimension sizePwdLbl = pwdLbl.getPreferredSize();
		pwdLbl.setBounds(20,525,sizePwdLbl.width,sizePwdLbl.height);
		
		pwdTxt = new JPasswordField();
		pwdTxt.setColumns(15);
		Dimension sizePwdTxt = pwdTxt.getPreferredSize();
		pwdTxt.setBounds(130,525,sizePwdTxt.width,sizePwdTxt.height);
		
		
		// bewerken button
		bewerken = new JLabel();
		bewerken.addMouseListener(new BewerkenListener());
		bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
		Dimension sizeBewerken = bewerken.getPreferredSize();
		bewerken.setBounds(320, 480, sizeBewerken.width, sizeBewerken.height);
		
		opslaan = new JLabel();
		opslaan.addMouseListener(new OpslaanListener());
		opslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		Dimension sizeOpslaan = opslaan.getPreferredSize();
		opslaan.setBounds(320,480, sizeOpslaan.width,sizeOpslaan.height);
		opslaan.setVisible(false);
		
		
		// Jtextfields bij email niet editable zetten !! Dit gebeurt pas wanneer er op "Bewerken" wordt geklikt
		emailOutTxt.setEnabled(false);
		poortTxt.setEnabled(false);
		userTxt.setEnabled(false);
		pwdTxt.setEnabled(false);
		
		emailOutTxt.setText(m.getEmailVoorkeur("EmailOut"));
		poortTxt.setText(m.getEmailVoorkeur("EmailPoort"));
		userTxt.setText(m.getEmailVoorkeur("EmailGebruikernaam"));
		pwdTxt.setText(m.getEmailVoorkeur("EmailWachtwoord"));
		
		
		//__Radiobuttons selected of niet
		if(m.getBeheerder().getTypeContent().equals(""))
		{
			erfgoed.setSelected(false);
			documenten.setSelected(false);
		}
		if(m.getBeheerder().getTypeContent().equals("Erfgoed"))
			erfgoed.setSelected(true);
		if(m.getBeheerder().getTypeContent().equals("Documenten"))
			documenten.setSelected(true);
		
		if(m.getBeheerder().getView().equals(""))
		{
			tegel.setSelected(false);
			tabel.setSelected(false);
		}
		if(m.getBeheerder().getView().equals("TegelView"))
			tegel.setSelected(true);
		if(m.getBeheerder().getView().equals("LijstView"))
			tabel.setSelected(true);
		
		// alles toevoegen aan InstelView panel
		add(titel);
		add(close);
		add(overzicht);
		add(ikWil);
		add(erfgoed);
		add(documenten);
		add(inEen);
		add(tegel);
		add(tabel);
		add(zien);
		add(documentenLbl);
		add(reden);
		add(redenScroll);
		add(nieuweRedenTxt);
		add(nieuweRedenBtn);
		add(verwijderenBtn);
		add(wachtwoordLbl);
		add(oudwLbl);
		add(oudW);
		add(nieuwW1Lbl);
		add(nieuwW1);
		add(herhaalLbl);
		add(nieuwW2);
		add(wachtwoordBtn);
		add(emailLbl);
		add(emailzin);
		add(emailOut);
		add(emailOutTxt);
		add(poortLbl);
		add(poortTxt);
		add(bewerken);
		add(opslaan);
		add(userLbl);
		add(userTxt);
		add(pwdLbl);
		add(pwdTxt);
		
		// LightBox maken met nodige listeners om te sluiten
		box = new LightBox();
		box.createLightBoxEffect(f,this);
		box.grabFocus();
		box.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 27)			// 27 is de keycode voor Esc
					box.closeLightBox(f, getInstelView());
			}
		});
		box.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(!inPanel)
					box.closeLightBox(f,getInstelView());
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		f.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				//Lightbox wegsmijten en een nieuwe maken bij het resizen.
				box.closeLightBox(f, getInstelView());
				box.createLightBoxEffect(f, getInstelView());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		
		// listener voor InstelView 
		this.addMouseListener(new InstelViewListener());
	}
	
	public JPanel getInstelPanel()
	{
		return instelPanel;
	}
	
	public JPanel getInstelView()		// gebruiken in private klassen hieronder
	{
		return this;
	}
	
	private class BewerkenListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			int resultaat = JOptionPane.showConfirmDialog(null,"Bent u zeker dat u wijzigingen wilt aanbrengen?\n" +
					"Foutieve gegevens kunnen ervoor zorgen dat het mailsysteem niet meer werkt!","Bewerken",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if(resultaat == JOptionPane.YES_OPTION)
			{
				emailOutTxt.setEnabled(true);
				poortTxt.setEnabled(true);
				userTxt.setEnabled(true);
				pwdTxt.setEnabled(true);
				
				bewerken.setVisible(false);
				opslaan.setVisible(true);

			}
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			bewerken.setCursor(hand);
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	}
	
	private class OpslaanListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			
			m.setEmailVoorkeur(emailOutTxt.getText(), "EmailOut");
			m.setEmailVoorkeur(poortTxt.getText(), "EmailPoort");
			m.setEmailVoorkeur(userTxt.getText(), "EmailGebruikernaam");
			m.setEmailVoorkeur(pwdTxt.getText(), "EmailWachtwoord");
				
			d.updateInstellingen(emailOutTxt.getText(),m.getInstellingenId("EmailOut"));
			d.updateInstellingen(poortTxt.getText(),m.getInstellingenId("EmailPoort"));
			d.updateInstellingen(userTxt.getText(),m.getInstellingenId("EmailGebruikernaam"));
			d.updateInstellingen(pwdTxt.getText(),m.getInstellingenId("EmailWachtwoord"));
			
			emailOutTxt.setEnabled(false);
			poortTxt.setEnabled(false);
			userTxt.setEnabled(false);
			pwdTxt.setEnabled(false);
			
			opslaan.setVisible(false);
			bewerken.setVisible(true);
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			bewerken.setCursor(hand);
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	}
	
	private class CloseListener implements MouseListener
	{
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
				box.closeLightBox(f,getInstelView());
		}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			close.setCursor(hand);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
	}
	
	private class OudwachtwoordListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e) {
			nieuwW1.setEnabled(true);
			nieuwW2.setEnabled(true);
			wachtwoordBtn.setVisible(true);
		}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}
		
	}
	
	private class WachtwoordOpslaanListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			wachtwoordBtn.setCursor(hand);
		}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {

			try {
				if(Login.convert(oudW.getText()).equals(m.getBeheerder().getWachtwoord()))
				{
					if(nieuwW1.getText().equals(nieuwW2.getText()))
					{
						m.getBeheerder().setWachtwoord(Login.convert(nieuwW2.getText()));
						d.updateBeheerdersDatabank(m.getBeheerder());

					}
					else
					{
						JOptionPane.showMessageDialog(null, "Wachtwoorden komen niet overeen", "Fout wachtwoord!",JOptionPane.ERROR_MESSAGE);
						oudW.setText("");
						nieuwW1.setText("");
						nieuwW2.setText("");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Verkeerde wachtwoord", "Fout wachtwoord!",JOptionPane.ERROR_MESSAGE);
					nieuwW1.setText("");
					nieuwW2.setText("");
				}
					
				
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}	
			finally
			{
				wachtwoordBtn.setVisible(false);
				oudW.setText("");
				nieuwW1.setEnabled(false);
				nieuwW2.setEnabled(false);
				nieuwW1.setText("");
				nieuwW2.setText("");
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	
	// MouseListener voor InstelView
	
	private class InstelViewListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			inPanel = true;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			inPanel = false;
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
}
