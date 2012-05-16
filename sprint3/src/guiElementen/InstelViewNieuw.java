package guiElementen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import harsh.p.raval.lightbox.LightBox;
import java.awt.Image;
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
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import model.Instellingen;
import model.Model;
import views.OverzichtView;
import controllers.Databank;
import controllers.MD5;

@SuppressWarnings("serial")
public class InstelViewNieuw extends JPanel
{
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/background_instelviewNieuw.png"));
	private Image background = backgroundIcon.getImage();
	private JPanel opstartviewPanel,standaardredenPanel,wachtwoordPanel,emailPanel;
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
	
	public InstelViewNieuw(Model model,JFrame frame,OverzichtView view,Databank data)
	{
		this.m = model;
		this.f = frame;
		this.v = view;
		this.d = data;
		
		jLabelFactory = new JLabelFactory();
		
		setSize(new Dimension(510,645));
		setOpaque(false);
		setLayout(new GridBagLayout());
		//setBackground(Color.GRAY);
		
		// Panels		
		opstartviewPanel = new JPanel();
		opstartviewPanel.setLayout(null);
		opstartviewPanel.setBorder(jLabelFactory.getFieldSet("Overzicht"));
		opstartviewPanel.setOpaque(false);
		opstartviewPanel.setPreferredSize(new Dimension(500,100));
		
		standaardredenPanel = new JPanel();
		standaardredenPanel.setLayout(null);
		standaardredenPanel.setBorder(jLabelFactory.getFieldSet("Documenten"));
		standaardredenPanel.setOpaque(false);
		standaardredenPanel.setPreferredSize(new Dimension(500,200));
		
		wachtwoordPanel = new JPanel();
		wachtwoordPanel.setLayout(null);
		wachtwoordPanel.setBorder(jLabelFactory.getFieldSet("Wachtwoord wijzigen"));
		wachtwoordPanel.setOpaque(false);
		wachtwoordPanel.setPreferredSize(new Dimension(500,120));
		
		emailPanel = new JPanel();
		emailPanel.setLayout(null);
		emailPanel.setBorder(jLabelFactory.getFieldSet("E-mail voorkeuren"));
		emailPanel.setOpaque(false);
		emailPanel.setPreferredSize(new Dimension(500,180));
		
		
		close = new JLabel();
		close.addMouseListener(new CloseListener());
		close.setIcon(new ImageIcon(getClass().getResource("imgs/close.png")));
	/*	Dimension sizeClose = close.getPreferredSize();
		close.setBounds(470, 15, sizeClose.width, sizeClose.height);*/
	
		
		//Overzicht_________________________________________________________________________________________
		JLabel ikWil = jLabelFactory.getNormaleTekst("Bij het opstarten wil ik ");
		Dimension sizeIkWil = ikWil.getPreferredSize();
		ikWil.setBounds(15, 15, sizeIkWil.width, sizeIkWil.height);
		
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
		erfgoed.setBounds(16, 35, sizeErfgoed.width, sizeErfgoed.height);
		
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
		documenten.setBounds(16, 55,sizeDocumenten.width, sizeDocumenten.height);
		
		ButtonGroup group1 = new ButtonGroup();
		group1.add(erfgoed);
		group1.add(documenten);
		
		// in een
		JLabel inEen = jLabelFactory.getNormaleTekst("in een");
		Dimension sizeInEen = inEen.getPreferredSize();
		inEen.setBounds(160, 45, sizeInEen.width, sizeInEen.height);
		
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
		tegel.setBounds(260, 35, sizeTegel.width, sizeTegel.height);
		
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
		tabel.setBounds(260, 55, sizeTabel.width, sizeTabel.height);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(tegel);
		group2.add(tabel);
		
		// zien
		JLabel zien = jLabelFactory.getNormaleTekst("zien.");
		Dimension sizeZien = zien.getPreferredSize();
		zien.setBounds(440, 45, sizeZien.width, sizeZien.height);
				
		// Standaardreden_________________________________________________________________________________________
		
		JLabel reden = jLabelFactory.getNormaleTekst("Standaard redenen voor afwijzing");
		Dimension sizeStandaardReden = reden.getPreferredSize();
		reden.setBounds(15, 20, sizeStandaardReden.width, sizeStandaardReden.height);
		
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
		redenScroll.setBounds(35,40,redenScroll.getPreferredSize().width,redenScroll.getPreferredSize().height);
		
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
		nieuweRedenTxt.setBounds(240, 60, sizeNieuweRedenTxt.width, sizeNieuweRedenTxt.height);
		
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
		nieuweRedenBtn.setBounds(260,85,sizeNieuweRedenBtn.width,sizeNieuweRedenBtn.height);

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
		verwijderenBtn.setBounds(260, 120, sizeVerwijderenBtn.width, sizeVerwijderenBtn.height);
		
		//Wachtwoord_________________________________________________________________________________________
		
		JLabel oudwLbl = jLabelFactory.getNormaleTekst("Oud wachtwoord");
		Dimension sizeoudwLbl = oudwLbl.getPreferredSize();
		oudwLbl.setBounds(20,30,sizeoudwLbl.width,sizeoudwLbl.height);
		
		oudW = new JPasswordField();
		oudW.addKeyListener(new OudwachtwoordListener());
		oudW.setColumns(15);
		Dimension sizeOudW = oudW.getPreferredSize();
		oudW.setBounds(140,30,sizeOudW.width,sizeOudW.height);
		
		JLabel nieuwW1Lbl = jLabelFactory.getNormaleTekst("Nieuw wachtwoord");
		Dimension sizenieuwW1Lbl = nieuwW1Lbl.getPreferredSize();
		nieuwW1Lbl.setBounds(20,52,sizenieuwW1Lbl.width,sizenieuwW1Lbl.height);
		
		nieuwW1 = new JPasswordField();
		nieuwW1.setColumns(15);
		nieuwW1.setEnabled(false);
		Dimension sizenieuwW1 = nieuwW1.getPreferredSize();
		nieuwW1.setBounds(140,52,sizenieuwW1.width,sizenieuwW1.height);
		
		JLabel herhaalLbl = jLabelFactory.getNormaleTekst("Herhaal wachtwoord");
		Dimension sizeherhaalLbl = herhaalLbl.getPreferredSize();
		herhaalLbl.setBounds(20,74,sizeherhaalLbl.width,sizeherhaalLbl.height);
		
		nieuwW2 = new JPasswordField();
		nieuwW2.setColumns(15);
		nieuwW2.setEnabled(false);
		Dimension sizenieuwW2 = nieuwW2.getPreferredSize();
		nieuwW2.setBounds(140,74,sizenieuwW2.width,sizenieuwW2.height);
		
		wachtwoordBtn = new JLabel();
		wachtwoordBtn.addMouseListener(new WachtwoordOpslaanListener());
		wachtwoordBtn.setVisible(false);
		wachtwoordBtn.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		Dimension sizeWbtn = wachtwoordBtn.getPreferredSize();
		wachtwoordBtn.setBounds(320,50, sizeWbtn.width, sizeWbtn.height);
		
		
		oudW.addFocusListener(new FocusAdapter() {
			
			@SuppressWarnings("deprecation")
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
		
		// Email voorkeuren_________________________________________________________________________________________
		JLabel emailLbl = jLabelFactory.getMenuTitel("E-mail voorkeuren");
		Dimension sizeEmailLbl = emailLbl.getPreferredSize();
		emailLbl.setBounds(10, 10, sizeEmailLbl.width, sizeEmailLbl.height);
		
		JLabel emailzin = jLabelFactory.getNormaleTekst("Geef hieronder uw E-mail instellingen");
		Dimension sizeEmailzin = emailzin.getPreferredSize();
		emailzin.setBounds(15,30,sizeEmailzin.width,sizeEmailzin.height);
		
		JLabel emailOut = jLabelFactory.getNormaleTekst("Email-Out (smtp)");
		Dimension sizeEmailOut = emailOut.getPreferredSize();
		emailOut.setBounds(20,62,sizeEmailOut.width,sizeEmailOut.height);
		
		JLabel poortLbl = jLabelFactory.getNormaleTekst("Poort");
		Dimension sizePoortLbl = poortLbl.getPreferredSize();
		poortLbl.setBounds(20,85,sizePoortLbl.width,sizePoortLbl.height);
		
		emailOutTxt = new JTextField();
		emailOutTxt.setColumns(15);
		Dimension sizeEmailOutTxt = emailOutTxt.getPreferredSize();
		emailOutTxt.setBounds(130, 60, sizeEmailOutTxt.width, sizeEmailOutTxt.height);
		
		poortTxt = new JTextField();
		poortTxt.setColumns(15);
		Dimension sizePoortTxt = poortTxt.getPreferredSize();
		poortTxt.setBounds(130, 85, sizePoortTxt.width, sizePoortTxt.height);
		
		JLabel userLbl =  jLabelFactory.getNormaleTekst("Gebruikersnaam");
		Dimension sizeUserLbl = userLbl.getPreferredSize();
		userLbl.setBounds(20,110,sizeUserLbl.width,sizeUserLbl.height);
		
		userTxt = new JTextField();
		userTxt.setColumns(15);
		Dimension sizeUserTxt = userTxt.getPreferredSize();
		userTxt.setBounds(130,110,sizeUserTxt.width,sizeUserTxt.height);
		
		JLabel pwdLbl =  jLabelFactory.getNormaleTekst("Wachtwoord");
		Dimension sizePwdLbl = pwdLbl.getPreferredSize();
		pwdLbl.setBounds(20,135,sizePwdLbl.width,sizePwdLbl.height);
		
		pwdTxt = new JPasswordField();
		pwdTxt.setColumns(15);
		Dimension sizePwdTxt = pwdTxt.getPreferredSize();
		pwdTxt.setBounds(130,135,sizePwdTxt.width,sizePwdTxt.height);
		
		
		// bewerken button
		bewerken = new JLabel();
		bewerken.addMouseListener(new BewerkenListener());
		bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
		Dimension sizeBewerken = bewerken.getPreferredSize();
		bewerken.setBounds(320, 90, sizeBewerken.width, sizeBewerken.height);
		
		opslaan = new JLabel();
		opslaan.addMouseListener(new OpslaanListener());
		opslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		Dimension sizeOpslaan = opslaan.getPreferredSize();
		opslaan.setBounds(320,90, sizeOpslaan.width,sizeOpslaan.height);
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
		
		// Adden aan gerelateerde panel met Absolute positionering
		opstartviewPanel.add(ikWil);		
		opstartviewPanel.add(erfgoed);
		opstartviewPanel.add(documenten);
		opstartviewPanel.add(inEen);
		opstartviewPanel.add(tegel);
		opstartviewPanel.add(tabel);
		opstartviewPanel.add(zien);
		
		standaardredenPanel.add(reden);
		standaardredenPanel.add(redenScroll);
		standaardredenPanel.add(nieuweRedenTxt);
		standaardredenPanel.add(nieuweRedenBtn);
		standaardredenPanel.add(verwijderenBtn);
		
		wachtwoordPanel.add(oudwLbl);
		wachtwoordPanel.add(oudW);
		wachtwoordPanel.add(nieuwW1Lbl);
		wachtwoordPanel.add(nieuwW1);
		wachtwoordPanel.add(herhaalLbl);
		wachtwoordPanel.add(nieuwW2);
		wachtwoordPanel.add(wachtwoordBtn);
		
		emailPanel.add(emailzin);
		emailPanel.add(emailOut);
		emailPanel.add(emailOutTxt);
		emailPanel.add(poortLbl);
		emailPanel.add(poortTxt);
		emailPanel.add(bewerken);
		emailPanel.add(opslaan);
		emailPanel.add(userLbl);
		emailPanel.add(userTxt);
		emailPanel.add(pwdLbl);
		emailPanel.add(pwdTxt);
		
		
		GridBagConstraints c = new GridBagConstraints();
			
		c.gridx = 1;
		c.gridy = 1;
		add(jLabelFactory.getTitel("Instellingen voor " + m.getBeheerder().getVoornaam()),c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(opstartviewPanel,c);
		
		c.gridx = 1;
		c.gridy = 3;
		add(standaardredenPanel, c);
		
		c.gridx = 1;
		c.gridy = 4;
		add(wachtwoordPanel,c);
		
		c.gridx = 1;
		c.gridy = 5;
		add(emailPanel,c);
		
		
		//Lightbox
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
				//box.createLightBoxEffect(f, getInstelView());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		
		// listener voor InstelView 
		this.addMouseListener(new InstelViewListener());
		
		
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
		
		@SuppressWarnings("deprecation")
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

		@SuppressWarnings("deprecation")
		@Override
		public void mousePressed(MouseEvent e) {

			try {
				if(MD5.convert(oudW.getText()).equals(m.getBeheerder().getWachtwoord()))
				{
					if(nieuwW1.getText().equals(nieuwW2.getText()))
					{
						m.getBeheerder().setWachtwoord(MD5.convert(nieuwW2.getText()));
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
					JOptionPane.showMessageDialog(null, "Verkeerd wachtwoord", "Fout wachtwoord!",JOptionPane.ERROR_MESSAGE);
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
