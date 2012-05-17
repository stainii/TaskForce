package administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import controllers.Databank;
import controllers.MD5;
import model.Beheerder;
import model.Model;

/**
 * Panel waar alle administrators worden weergegeven en kunnen worden beheerd.
 */

@SuppressWarnings("serial")
public class AdminPanel extends JPanel
{
	private Model m;
	private Databank d;
	private JPanel allesPanel, adminPanel;
	private JList adminList;
	private DefaultListModel adminModel;
	private JLabel naamLbl,emailLbl, pw1Lbl, pw2Lbl,toevoegen,toevoegenOpslaan,verwijderen,bewerken, bewerkenOpslaan,annuleren;
	private JTextField naamTxt, emailTxt;
	private JPasswordField password1Txt, password2Txt;
	private int index;
	private String gebruiker;
	private Beheerder beheerder;
	
	public AdminPanel(Model model , Databank data,String g)
	{
		this.m=model;
		this.d=data;
		this.gebruiker = g;
		
		allesPanel = new JPanel();		// bevat de 2 panels
		
		adminPanel = new JPanel();
		adminPanel.setLayout(new GridBagLayout());
		
		naamLbl = new JLabel("Naam");
		pw1Lbl = new JLabel("Wachtwoord");
		pw2Lbl = new JLabel("Bevestiging wachtwoord");
		emailLbl = new JLabel("E-mail");
		
		naamTxt = new JTextField();
		password1Txt = new JPasswordField();
		password2Txt = new JPasswordField();
		emailTxt = new JTextField();
		
		toevoegen = new JLabel("Voeg admin toe");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new ToevoegenListener());
		
		toevoegenOpslaan = new JLabel();
		toevoegenOpslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		toevoegenOpslaan.addMouseListener(new ToevoegenOpslaanListener());
		
		verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		verwijderen.addMouseListener(new VerwijderenListener());
		
		bewerken = new JLabel();
		bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
		bewerken.addMouseListener(new BewerkenListener());
		
		verwijderen.setVisible(false);
		bewerken.setVisible(false);
		
		bewerkenOpslaan = new JLabel();
		bewerkenOpslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		bewerkenOpslaan.addMouseListener(new BewerkenOpslaanListener());
		
		annuleren = new JLabel();
		annuleren.setIcon(new ImageIcon(getClass().getResource("imgs/annuleren.png")));
		annuleren.addMouseListener(new AnnulerenListener());
		
		
		naamTxt.setEditable(false);
		naamTxt.setColumns(15);
		MaxLengthTextDocument maxLength = new MaxLengthTextDocument();
		maxLength.setMaxChars(20);//50 is a maximum number of character
		naamTxt.setDocument(maxLength);
		
		emailTxt.setEditable(false);
		emailTxt.setColumns(15);
		MaxLengthTextDocument maxLength2 = new MaxLengthTextDocument();
		maxLength2.setMaxChars(60);//50 is a maximum number of character
		emailTxt.setDocument(maxLength2);
		
		password1Txt.setEditable(false);
		password1Txt.setColumns(15);
		MaxLengthTextDocument maxLength3 = new MaxLengthTextDocument();
		maxLength3.setMaxChars(60);//50 is a maximum number of character
		password1Txt.setDocument(maxLength3);
		
		password2Txt.setColumns(15);
		MaxLengthTextDocument maxLength4 = new MaxLengthTextDocument();
		maxLength4.setMaxChars(60);//50 is a maximum number of character
		password2Txt.setDocument(maxLength4);
		
		adminModel = new DefaultListModel();
		
		for(Beheerder b : m.getBeheerders())
		{
			if(b.isAdmin()==true)
				adminModel.addElement(b.getGebruikersnaam());
		}
		
		adminList = new JList(adminModel);
		adminList.setLayoutOrientation(JList.VERTICAL);
		adminList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane adminScroll = new JScrollPane(adminList);
		adminScroll.setPreferredSize(new Dimension(150,100));
		adminScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		adminList.addListSelectionListener(new ListListener());
		adminList.setSelectedIndex(0);
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 2;
		c.gridy = 1;
		adminPanel.add(toevoegen,c);
		
		c.gridx = 1;
		c.gridy = 2;
		adminPanel.add(naamLbl,c);
		
		c.gridx = 2;
		c.gridy = 2;
		adminPanel.add(naamTxt,c);
		
		c.gridx = 1;
		c.gridy = 3;
		adminPanel.add(pw1Lbl,c);
		
		c.gridx = 2;
		c.gridy = 3;
		adminPanel.add(password1Txt,c);
		
		c.gridx = 1;
		c.gridy = 4;
		pw2Lbl.setVisible(false);
		adminPanel.add(pw2Lbl,c);
		
		c.gridx = 2;
		c.gridy = 4;
		password2Txt.setVisible(false);
		adminPanel.add(password2Txt,c);
		
		c.gridx = 1;
		c.gridy = 5;
		adminPanel.add(emailLbl,c);
		
		c.gridx = 2;
		c.gridy = 5;
		adminPanel.add(emailTxt,c);
		
		c.gridx = 1;
		c.gridy = 6;
		adminPanel.add(verwijderen,c);
		
		c.gridx = 1;
		c.gridy = 6;
		annuleren.setVisible(false);
		adminPanel.add(annuleren,c);
		
		c.gridx = 2;
		c.gridy = 6;
		adminPanel.add(bewerken,c);
		
		c.gridx = 2;
		c.gridy = 6;
		bewerkenOpslaan.setVisible(false);
		adminPanel.add(bewerkenOpslaan,c);
		
		c.gridx = 2;
		c.gridy = 6;
		toevoegenOpslaan.setVisible(false);
		adminPanel.add(toevoegenOpslaan,c);

		
		allesPanel.add(adminScroll,BorderLayout.LINE_START);
		allesPanel.add(adminPanel,BorderLayout.CENTER);
		
	}
	
	public JPanel getAdminPanel()
	{
		return allesPanel;
	}
	
	public void VulAdminList()
	{
		for(Beheerder b : m.getBeheerders())
		{
			if(adminList.getSelectedValue().equals(b.getGebruikersnaam()))
			{
				if(b.isAdmin() == true)
				{
					beheerder = b;
					index = adminList.getSelectedIndex();
					naamTxt.setText(b.getGebruikersnaam());
					password1Txt.setText("wachtwoord");			// voor elke admin hetzelfde wachtwoord laten zien ! MD5 string is veel te lang!
					emailTxt.setText(b.getEmail());
				}
			}
		}
	}
	
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			if(adminList.getSelectedIndex() == -1)
			{
				naamTxt.setText("");
				emailTxt.setText("");
				password1Txt.setText("");
				verwijderen.setVisible(false);
				bewerken.setVisible(false);
			}
			else
			{
				naamTxt.setEditable(false);
				emailTxt.setEditable(false);
				password1Txt.setEditable(false);
		
				bewerken.setVisible(true);
				verwijderen.setVisible(true);
				toevoegenOpslaan.setVisible(false);
				
				VulAdminList();
			}
		}
		
	}
	private class ToevoegenListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			bewerken.setVisible(false);
			verwijderen.setVisible(false);
			annuleren.setVisible(true);
			
			naamTxt.setText("");
			password1Txt.setText("");
			pw2Lbl.setVisible(true);
			password2Txt.setVisible(true);
			emailTxt.setText("");
			
			naamTxt.setEditable(true);
			password1Txt.setEditable(true);
			password2Txt.setEditable(true);
			emailTxt.setEditable(true);
			
			toevoegenOpslaan.setVisible(true);
			
			adminList.setSelectedIndex(-1);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	private class ToevoegenOpslaanListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@SuppressWarnings("deprecation")
		@Override
		public void mousePressed(MouseEvent arg0) {

			//controle of alle velden zijn ingevuld.
			if(naamTxt.getText().isEmpty() || password1Txt.getText().isEmpty() ||password2Txt.getText().isEmpty() || emailTxt.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Alle velden moeten ingevuld zijn!" ,"Velden zijn leeg",JOptionPane.ERROR_MESSAGE);
			else
			{
				if(password1Txt.getText().equals(password2Txt.getText()))
				{
					boolean dubbel = false;
					for (Beheerder b: m.getBeheerders())
					{
						if (b.getGebruikersnaam().equalsIgnoreCase(naamTxt.getText()))
							dubbel = true;
					}
					
					if (!dubbel)
					{
						try
						{
							d.voegBeheerderToeAanDatabank(naamTxt.getText(),naamTxt.getText(),MD5.convert(password2Txt.getText()),emailTxt.getText(),false,false,false,false,true);
						}
						catch (NoSuchAlgorithmException e)
						{
							e.printStackTrace();
						}
						catch (UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Er bestaat reeds een administrator met deze gebruikersnaam." ,"Fout",JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Wachtwoorden komen niet overeen" ,"Fout",JOptionPane.ERROR_MESSAGE);
				}
				
				d.getBeheerdersEnBurgersUitDatabank();
				
				naamTxt.setText("");
				password1Txt.setText("");
				password2Txt.setText("");
				emailTxt.setText("");
								
				adminModel.removeAllElements();
				for(Beheerder b : m.getBeheerders())
				{
					if(b.isAdmin()==true)
						adminModel.addElement(b.getGebruikersnaam());
				}
				
				adminList.setSelectedIndex(adminList.getLastVisibleIndex());
				
				pw2Lbl.setVisible(false);
				password2Txt.setVisible(false);
				annuleren.setVisible(false);
			}
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {}		
	}
	private class VerwijderenListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			
			if(gebruiker.equalsIgnoreCase(adminList.getSelectedValue().toString()))
			{
				JOptionPane.showMessageDialog(null,"U kan de huidige administrator niet verwijderen omdat deze is ingelogd." ,"Fout",JOptionPane.ERROR_MESSAGE);
			}
			else if(verwijderen.isEnabled() == true)
			{
				int resultaat = JOptionPane.showConfirmDialog(null, "Wilt u " +beheerder.getGebruikersnaam().toString()+
						" verwijderen?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
								
				if(resultaat == JOptionPane.YES_OPTION)
				{
					d.deleteBeheerder(beheerder.getId());			// verwijderen uit databank
					m.verwijderBeheerder(beheerder.getId());			// verwijderen uit ArrayList<Beheerder>
					
					adminModel.remove(adminList.getSelectedIndex());
					adminList.setSelectedIndex(adminList.getLastVisibleIndex());
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	private class BewerkenListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			naamTxt.setEditable(true);
			password1Txt.setEditable(true);
			emailTxt.setEditable(true);
			verwijderen.setVisible(false);
			
			bewerken.setVisible(false);
			bewerkenOpslaan.setVisible(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	private class BewerkenOpslaanListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@SuppressWarnings("deprecation")
		@Override
		public void mousePressed(MouseEvent e)
		{
			boolean dubbel = false;
			for (Beheerder b: m.getBeheerders())
			{
				if (b.getNaam().equalsIgnoreCase(naamTxt.getText()))
					dubbel = true;
			}
			
			if (!dubbel)
			{
				beheerder.setGebruikersnaam(naamTxt.getText());
				beheerder.setEmail(emailTxt.getText());
				
				if(!password1Txt.getText().equals("wachtwoord"))
				{
					if (!password1Txt.getText().equals(beheerder.getWachtwoord()))
						try {
							beheerder.setWachtwoord(MD5.convert(password1Txt.getText()));
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
				
				try {
					d.updateBeheerdersDatabank(beheerder);
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				adminModel.removeAllElements();
				
				for(Beheerder b : m.getBeheerders())
				{
					if(b.isAdmin()==true)
						adminModel.addElement(b.getGebruikersnaam());
				}
				adminList.setSelectedIndex(index);			//zorgt ervoor dat na het "hertekenen" terug op de geselecteerde index staat
				
				naamTxt.setEditable(false);
				password1Txt.setEditable(false);
				emailTxt.setEditable(false);
				verwijderen.setVisible(true);
				
				bewerken.setVisible(true);
				bewerkenOpslaan.setVisible(false);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Er bestaat reeds een administrator met deze gebruikersnaam." ,"Fout",JOptionPane.ERROR_MESSAGE);
			}
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {}		
	}
	
	private class AnnulerenListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e){}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e)
		{
			naamTxt.setEditable(false);
			emailTxt.setEditable(false);
			password1Txt.setEditable(false);
			pw2Lbl.setVisible(false);
			password2Txt.setVisible(false);
			
			if(adminList.getSelectedIndex() == -1)
			{
				bewerken.setVisible(false);
				verwijderen.setVisible(false);
				annuleren.setVisible(false);
				toevoegenOpslaan.setVisible(false);
			}
			else
			{
				bewerken.setVisible(true);
				verwijderen.setVisible(true);
				toevoegenOpslaan.setVisible(false);
				
				VulAdminList();
			}
				
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}

	public class MaxLengthTextDocument extends PlainDocument	//beperkt aantal tekens in tekstveld
	{
		private static final long serialVersionUID = 1L;
		//Store maximum characters permitted
		private int maxChars;
		
		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException
		{
			if(str != null && (getLength() + str.length() < maxChars))
			{
				super.insertString(offs, str, a);
			}
		}
		
		public int getMaxChars()
		{
			return maxChars;
		}
		public void setMaxChars(int m)
		{
			maxChars = m;
		}
	}

}