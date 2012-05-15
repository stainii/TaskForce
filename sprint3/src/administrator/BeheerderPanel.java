package administrator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import controllers.mail.MailThuis;
import model.Beheerder;
import model.Model;
import controllers.mail.SoortMail;
import controllers.mail.NieuweBeheerderMail;
import controllers.WachtwoordGenerator;
import controllers.MD5;

/**
 * Panel waar beheerders worden weergegeven en kunnen worden beheerd.
 */

@SuppressWarnings("serial")
public class BeheerderPanel extends JPanel
{
	private Model m;
	private Databank d;
	private JList beheerderList;
	private DefaultListModel beheerderModel;
	private JPanel allesPanel,beheerderPnl,rechtenPnl;
	private JTextField naamTxt, achternaamTxt, emailTxt;
	private JLabel rechtenPlus,rechtenMin , toevoegenOpslaan,bewerken,verwijderen,naamLbl,achternaamLbl,emailLbl
		,toevoegen,bewerkenOpslaan;
	private JCheckBox wijzigenCb, toevoegenCb, verwijderenCb, beoordelenCb;
	private int index;
	
	public BeheerderPanel(Model model, Databank data )
	{
		this.m=model;
		this.d=data;
		
		allesPanel = new JPanel();
	
		beheerderPnl = new JPanel();
		beheerderPnl.setLayout(new GridBagLayout());
				
		naamTxt = new JTextField();
		achternaamTxt = new JTextField();
		emailTxt = new JTextField();
		
		naamTxt.setEditable(false);
		naamTxt.setColumns(17);
		naamTxt.setDocument(new JTextFieldLimit(20));
		achternaamTxt.setEditable(false);
		achternaamTxt.setColumns(17);
		achternaamTxt.setDocument(new JTextFieldLimit(20));
		emailTxt.setEditable(false);
		emailTxt.setColumns(17);
		emailTxt.setDocument(new JTextFieldLimit(60));
		
		rechtenPlus = new JLabel("Rechten");
		rechtenPlus.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		rechtenPlus.addMouseListener(new RechtenPlusListener());
		
		rechtenMin = new JLabel("Rechten");
		rechtenMin.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_min.png")));
		rechtenMin.addMouseListener(new RechtenMinListener());		
				
		verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("imgs/verwijderen.png")));
		verwijderen.addMouseListener(new VerwijderenListener());
		
		bewerken = new JLabel();
		bewerken.setIcon(new ImageIcon(getClass().getResource("imgs/bewerken.png")));
		bewerken.addMouseListener(new BewerkenListener());
		
		verwijderen.setVisible(false);
		bewerken.setVisible(false);
		
		wijzigenCb = new JCheckBox("Wijzigen");
		toevoegenCb = new JCheckBox("Toevoegen");
		verwijderenCb = new JCheckBox("Verwijderen");
		beoordelenCb = new JCheckBox("Beoordelen");
				
		wijzigenCb.setEnabled(false);
		toevoegenCb.setEnabled(false);
		verwijderenCb.setEnabled(false);
		beoordelenCb.setEnabled(false);
		
		toevoegenOpslaan = new JLabel();
		toevoegenOpslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		toevoegenOpslaan.addMouseListener(new ToevoegenOpslaanListener());
		
		bewerkenOpslaan = new JLabel();
		bewerkenOpslaan.setIcon(new ImageIcon(getClass().getResource("imgs/opslaan.png")));
		bewerkenOpslaan.addMouseListener(new BewerkenOpslaanListener());
						
		rechtenPnl = new JPanel();
		rechtenPnl.setLayout(new GridLayout(4,1));
		
		rechtenPnl.add(wijzigenCb);
		rechtenPnl.add(toevoegenCb);
		rechtenPnl.add(beoordelenCb);
		rechtenPnl.add(verwijderenCb);
		
		naamLbl = new JLabel("Naam");
		achternaamLbl = new JLabel("Achternaam");
		emailLbl = new JLabel("E-mail");
		
		toevoegen = new JLabel("Voeg beheerder toe");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new ToevoegenListener());
		
		beheerderModel = new DefaultListModel();
		
		for(Beheerder b : m.getBeheerders())			// BeheerderModel vullen met Beheerders uit de ArrayList<Beheerder>
		{
			if(b.isAdmin()==false)
				beheerderModel.addElement(b.getVoornaam());
		}
		
		beheerderList = new JList(beheerderModel);
		beheerderList.setLayoutOrientation(JList.VERTICAL);
		beheerderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane beheerderScroll = new JScrollPane(beheerderList);
		beheerderScroll.setPreferredSize(new Dimension(150,200));
		beheerderScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		beheerderList.addListSelectionListener(new ListListener());
		beheerderList.setSelectedIndex(0);
		m.setBeheerder(beheerderList.getSelectedValue().toString());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 2;
		c.gridy = 1;
		beheerderPnl.add(toevoegen,c);
						
		c.gridx = 1;
		c.gridy = 2;	
		c.gridheight = 1;
		beheerderPnl.add(naamLbl,c);
		
		c.gridx = 2;
		c.gridy = 2;
		beheerderPnl.add(naamTxt,c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridheight = 1;
		beheerderPnl.add(achternaamLbl,c);
		
		c.gridx = 2;
		c.gridy = 3;
		beheerderPnl.add(achternaamTxt,c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridheight = 1;
		beheerderPnl.add(emailLbl,c);
		
		c.gridx = 2;
		c.gridy = 4;
		beheerderPnl.add(emailTxt,c);
		
		c.gridx = 1;
		c.gridy = 5;
		beheerderPnl.add(rechtenPlus,c);
		
		c.gridx = 1;
		c.gridy = 5;
		rechtenMin.setVisible(false);
		beheerderPnl.add(rechtenMin,c);
				
		c.gridx = 2;
		c.gridy = 5;
		toevoegenOpslaan.setVisible(false);
		beheerderPnl.add(toevoegenOpslaan,c);
		
		c.gridx = 1;
		c.gridy = 6;
		beheerderPnl.add(verwijderen,c);		// deze laten veranderen naar Annuleren bij wijziging
		
		c.gridx = 1;
		c.gridy = 6;
		rechtenPnl.setVisible(false);
		beheerderPnl.add(rechtenPnl,c);
				
		c.gridx = 2;
		c.gridy = 6;
		beheerderPnl.add(bewerken,c);
		
		c.gridx = 2;
		c.gridy = 6;
		bewerkenOpslaan.setVisible(false);
		beheerderPnl.add(bewerkenOpslaan,c);
		
		allesPanel.add(beheerderScroll,BorderLayout.LINE_START);
		allesPanel.add(beheerderPnl,BorderLayout.CENTER);
		verwijderen.setVisible(true);
	}
	
	public JPanel getBeheerPanel()
	{
		return allesPanel;
	}
	
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (beheerderList.getSelectedIndex()!=-1)
			{
				naamTxt.setEditable(false);
				achternaamTxt.setEditable(false);	
				emailTxt.setEditable(false);
				
				rechtenPlus.setVisible(true);
				verwijderen.setVisible(true);
				bewerken.setVisible(true);
				
				if(rechtenPnl.isVisible())
					verwijderen.setVisible(false);
				else
					verwijderen.setVisible(true);
				
				bewerken.setVisible(true);
				toevoegenOpslaan.setVisible(false);
				
				for(Beheerder b : m.getBeheerders())
				{
					if(beheerderList.getSelectedValue().equals(b.getVoornaam()))
					{
						m.setBeheerder(beheerderList.getSelectedValue().toString());
						naamTxt.setText(m.getBeheerder().getVoornaam());
						achternaamTxt.setText(m.getBeheerder().getAchternaam());
						emailTxt.setText(m.getBeheerder().getEmail());
						
						wijzigenCb.setSelected(m.getBeheerder().KanWijzigen());
						toevoegenCb.setSelected(m.getBeheerder().KanToevoegen());
						beoordelenCb.setSelected(m.getBeheerder().KanBeoordelen());
						verwijderenCb.setSelected(m.getBeheerder().KanVerwijderen());
					}
				}
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
			
			beheerderList.setSelectedIndex(0);
			
			rechtenPnl.setVisible(false);
			naamTxt.setText("");
			achternaamTxt.setText("");
			emailTxt.setText("");
			
			naamTxt.setEditable(true);
			achternaamTxt.setEditable(true);
			emailTxt.setEditable(true);
			
			rechtenPlus.setVisible(false);
			verwijderen.setVisible(false);
			bewerken.setVisible(false);
			toevoegenOpslaan.setVisible(true);
			
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
			if(naamTxt.getText().isEmpty() || achternaamTxt.getText().isEmpty() || emailTxt.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Alle velden moeten ingevuld zijn!" ,"Velden zijn leeg",JOptionPane.ERROR_MESSAGE);
			else
			{
				boolean dubbel = false;
				for (Beheerder b: m.getBeheerders())
				{
					if (b.getVoornaam().equalsIgnoreCase(naamTxt.getText()))
						dubbel = true;
				}
				
				if (!dubbel)
				{
					String wachtwoord = WachtwoordGenerator.randomstring();
					
					int id = 0;
					try
					{
						id = d.voegBeheerderToeAanDatabank(naamTxt.getText(),achternaamTxt.getText(),MD5.convert(wachtwoord),emailTxt.getText(),true,true,true,true,false);
					}
					catch (NoSuchAlgorithmException e)
					{
						e.printStackTrace();
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
					d.getBeheerdersEnBurgersUitDatabank();
					
					//mail sturen
					Beheerder beheerder = null;
					for (Beheerder b: m.getBeheerders())
					{
						if (b.getId() == id)
							beheerder = b;
					}
					if (beheerder!=null)
					{
						
						SoortMail smail = new NieuweBeheerderMail(beheerder, wachtwoord);
						MailThuis mail = new MailThuis(beheerder.getEmail(), "Nieuwe beheerder", smail ,m);
						ExecutorService ex = Executors.newFixedThreadPool(1);; 
						ex.execute(mail);
					}				
					JOptionPane.showMessageDialog(null, "De beheerder zal op het opgegeven emailadres een mail krijgen waarin het wachtwoord te vinden is.");
					
					naamTxt.setText("");
					achternaamTxt.setText("");
					emailTxt.setText("");
									
					beheerderModel.removeAllElements();
					for(Beheerder b : m.getBeheerders())
					{
						if(b.isAdmin()==false)
							beheerderModel.addElement(b.getVoornaam());
					}
					
					beheerderList.setSelectedIndex(beheerderList.getLastVisibleIndex());
					rechtenPlus.setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Er bestaat reeds een beheerder met deze gebruikersnaam." ,"Fout",JOptionPane.ERROR_MESSAGE);
				}
				
			}
				
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {}		
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
			achternaamTxt.setEditable(true);
			emailTxt.setEditable(true);
			
			wijzigenCb.setEnabled(true);
			toevoegenCb.setEnabled(true);
			verwijderenCb.setEnabled(true);
			beoordelenCb.setEnabled(true);
			
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
		public void mousePressed(MouseEvent arg0)
		{
			boolean dubbel = false;
			for (Beheerder b: m.getBeheerders())
			{
				if (b.getVoornaam().equalsIgnoreCase(naamTxt.getText()) && b.getId()!=m.getBeheerder().getId())
					dubbel = true;
			}
			
			if (!dubbel)
			{
				m.getBeheerder().setVoornaam(naamTxt.getText());
				m.getBeheerder().setAchternaam(achternaamTxt.getText());
				m.getBeheerder().setEmail(emailTxt.getText());
				m.getBeheerder().setKanBeoordelen(beoordelenCb.isSelected());
				m.getBeheerder().setKanToevoegen(toevoegenCb.isSelected());
				m.getBeheerder().setKanVerwijderen(verwijderenCb.isSelected());
				m.getBeheerder().setKanWijzigen(wijzigenCb.isSelected());
				
				try {
					d.updateBeheerdersDatabank(m.getBeheerder());
				} catch (NoSuchAlgorithmException e) {e.printStackTrace();
				} catch (UnsupportedEncodingException e) {e.printStackTrace();
				}
				
				beheerderModel.removeAllElements();
				
				for(Beheerder b : m.getBeheerders())
				{
					if(b.isAdmin()==false)
						beheerderModel.addElement(b.getVoornaam());
				}
				beheerderList.setSelectedIndex(index);			//zorgt ervoor dat na het "hertekenen" terug op de geselecteerde index staat
				
				naamTxt.setEditable(false);
				achternaamTxt.setEditable(false);
				emailTxt.setEditable(false);
				
				wijzigenCb.setEnabled(false);
				toevoegenCb.setEnabled(false);
				verwijderenCb.setEnabled(false);
				beoordelenCb.setEnabled(false);
				
				bewerken.setVisible(true);
				bewerkenOpslaan.setVisible(false);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Er bestaat reeds een beheerder met deze gebruikersnaam." ,"Fout",JOptionPane.ERROR_MESSAGE);
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
			if(verwijderen.isEnabled() == true)
			{
				int resultaat = JOptionPane.showConfirmDialog(beheerderPnl, "Wilt u " +m.getBeheerder().getVoornaam().toString()+
						" verwijderen?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				
				if(resultaat == JOptionPane.YES_OPTION)
				{
					d.deleteBeheerder(m.getBeheerder().getId());			// verwijderen uit databank
					m.verwijderBeheerder(m.getBeheerder().getId());			// verwijderen uit ArrayList<Beheerder>
					
					beheerderModel.remove(beheerderList.getSelectedIndex());
					
					naamTxt.setText("");
					achternaamTxt.setText("");
					emailTxt.setText("");
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	private class RechtenPlusListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			rechtenPlus.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			rechtenPlus.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			rechtenMin.setVisible(true);
			rechtenPlus.setVisible(false);
			verwijderen.setVisible(false);
			rechtenPnl.setVisible(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	private class RechtenMinListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			rechtenMin.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_hover_min.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			rechtenMin.setIcon(new ImageIcon(getClass().getResource("imgs/toevoegenIco_min.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			rechtenMin.setVisible(false);
			rechtenPlus.setVisible(true);
			rechtenPnl.setVisible(false);
			
			if(!(beheerderList.getSelectedIndex() == -1))
				verwijderen.setVisible(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
}
class JTextFieldLimit extends PlainDocument {
	  private int limit;
	  JTextFieldLimit(int limit) {
	    super();
	    this.limit = limit;
	  }

	  JTextFieldLimit(int limit, boolean upper) {
	    super();
	    this.limit = limit;
	  }

	  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str, attr);
	    }
	  }
	}