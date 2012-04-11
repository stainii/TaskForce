package administrator.adminPanels;

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

import administrator.Administrator;
import controllers.Databank;
import controllers.Login;
import model.Beheerder;
import model.Model;

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
	
	public AdminPanel(Model model , Databank data,String g)
	{
		this.m=model;
		this.d=data;
		this.gebruiker = g;
		
		allesPanel = new JPanel();		// bevat de 2 panels
		
		adminPanel = new JPanel();
		adminPanel.setLayout(new GridBagLayout());
		
		adminModel = new DefaultListModel();
		
		for(Beheerder b : m.getBeheerders())
		{
			if(b.isAdmin()==true)
				adminModel.addElement(b.getVoornaam());
		}
		
		adminList = new JList(adminModel);
		adminList.setLayoutOrientation(JList.VERTICAL);
		adminList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane adminScroll = new JScrollPane(adminList);
		adminScroll.setPreferredSize(new Dimension(150,100));
		adminScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		adminList.addListSelectionListener(new ListListener());
		
		naamLbl = new JLabel("Naam");
		pw1Lbl = new JLabel("Wachtwoord");
		pw2Lbl = new JLabel("Bevestiging wachtwoord");
		emailLbl = new JLabel("E-mail");
		
		naamTxt = new JTextField();
		password1Txt = new JPasswordField();
		password2Txt = new JPasswordField();
		emailTxt = new JTextField();
		
		toevoegen = new JLabel("Voeg admin toe");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new ToevoegenListener());
		
		toevoegenOpslaan = new JLabel();
		toevoegenOpslaan.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/opslaan.png")));
		toevoegenOpslaan.addMouseListener(new ToevoegenOpslaanListener());
		
		verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/verwijderen.png")));
		verwijderen.addMouseListener(new VerwijderenListener());
		
		bewerken = new JLabel();
		bewerken.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/bewerken.png")));
		bewerken.addMouseListener(new BewerkenListener());
		
		verwijderen.setVisible(false);
		bewerken.setVisible(false);
		
		bewerkenOpslaan = new JLabel();
		bewerkenOpslaan.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/opslaan.png")));
		bewerkenOpslaan.addMouseListener(new BewerkenOpslaanListener());
		
		annuleren = new JLabel();
		annuleren.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/annuleren.png")));
		annuleren.addMouseListener(new AnnulerenListener());
		
		naamTxt.setEditable(false);
		emailTxt.setEditable(false);
		password1Txt.setEditable(false);
		
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
			if(adminList.getSelectedValue().equals(b.getVoornaam()))
			{
				if(b.isAdmin() == true)
				{
					m.setBeheerder(adminList.getSelectedValue().toString());
					index = adminList.getSelectedIndex();
					naamTxt.setText(b.getVoornaam());
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
			toevoegen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			toevoegen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
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
		@Override
		public void mousePressed(MouseEvent arg0) {

			//controle of alle velden zijn ingevuld.
			if(naamTxt.getText().isEmpty() || password1Txt.getText().isEmpty() ||password2Txt.getText().isEmpty() || emailTxt.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Alle velden moeten ingevuld zijn!" ,"Velden zijn leeg",JOptionPane.ERROR_MESSAGE);
			else
			{
				if(password1Txt.getText().equals(password2Txt.getText()))
					try {
						d.voegBeheerderToeAanDatabank(naamTxt.getText(),naamTxt.getText(),Login.convert(password2Txt.getText()),emailTxt.getText(),false,false,false,false,true);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
				{
					JOptionPane.showMessageDialog(null,"Wachtwoorden komen niet overeen" ,"Fout",JOptionPane.ERROR_MESSAGE);
					password2Txt.setText("");
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
						adminModel.addElement(b.getVoornaam());
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
				int resultaat = JOptionPane.showConfirmDialog(null, "Wilt u " +m.getBeheerder().getVoornaam().toString()+
						" verwijderen?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
								
				if(resultaat == JOptionPane.YES_OPTION)
				{
					d.deleteBeheerder(m.getBeheerder().getId());			// verwijderen uit databank
					m.verwijderBeheerder(m.getBeheerder().getId());			// verwijderen uit ArrayList<Beheerder>
					
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
		@Override
		public void mousePressed(MouseEvent arg0) {
			
			m.getBeheerder().setVoornaam(naamTxt.getText());
			try 
			{
				/*
				 * Beetje smerig opgelost: Voor veiligheid gaan we MD5 niet terug naar String converteren ( wat denk ik zelfs niet mogelijk is)
				 * krijgt het password1Txt een defaultweergave van "wachtwoord" Bij het bewerken wordt er dan eerst gekeken of het textfield zijn 
				 * waarde veranderd is? Als het textfield nog steeds de waarde "wachtwoord" bevat gaat hij niets doen en dus ook het wachtwoord 
				 * van de Beheerder(admin) NIET veranderen. Is het wel veranderd gaat hij het wachtwoord wel veranderen en encrypteren.
				 */
				if(!password1Txt.getText().equals("wachtwoord"))
					m.getBeheerder().setWachtwoord(Login.convert(password1Txt.getText()));
					
			} catch (NoSuchAlgorithmException e) {e.printStackTrace();
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
			
			m.getBeheerder().setEmail(emailTxt.getText());
			
			try {
				d.updateBeheerdersDatabank(m.getBeheerder());
			} catch (NoSuchAlgorithmException e) {e.printStackTrace();
			} catch (UnsupportedEncodingException e) {e.printStackTrace();
			}
			
			adminModel.removeAllElements();
			
			for(Beheerder b : m.getBeheerders())
			{
				if(b.isAdmin()==true)
					adminModel.addElement(b.getVoornaam());
			}
			adminList.setSelectedIndex(index);			//zorgt ervoor dat na het "hertekenen" terug op de geselecteerde index staat
			
			naamTxt.setEditable(false);
			password1Txt.setEditable(false);
			emailTxt.setEditable(false);
			verwijderen.setVisible(true);
			
			bewerken.setVisible(true);
			bewerkenOpslaan.setVisible(false);
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {}		
	}
	
	private class AnnulerenListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
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
}
