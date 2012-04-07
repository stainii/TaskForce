package administrator.adminPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import controllers.Databank;
import model.Beheerder;
import model.Model;

public class BeheerderPanel extends JPanel
{
	private Model m;
	private Databank d;
	private JList beheerderList;
	private DefaultListModel beheerderModel;
	private JPanel allesPanel,beheerderPnl,rechtenPnl;
	private JTextField naamTxt, achternaamTxt, emailTxt;
	private JLabel rechtenPlus,rechtenMin ,instellingen, toevoegenOpslaan,bewerken,verwijderen,naamLbl,achternaamLbl,emailLbl
		,toevoegen,bewerkenOpslaan,instellingenMin,instellingenPlus;
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
		achternaamTxt.setEditable(false);
		emailTxt.setEditable(false);
		
		rechtenPlus = new JLabel("Rechten");
		rechtenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		rechtenPlus.addMouseListener(new RechtenPlusListener());
		
		rechtenMin = new JLabel("Rechten");
		rechtenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
		rechtenMin.addMouseListener(new RechtenMinListener());		// andere !!! 
				
		verwijderen = new JLabel();
		verwijderen.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/verwijderen.png")));
		verwijderen.addMouseListener(new VerwijderenListener());
		
		bewerken = new JLabel();
		bewerken.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/bewerken.png")));
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
		toevoegenOpslaan.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/opslaan.png")));
		toevoegenOpslaan.addMouseListener(new ToevoegenOpslaanListener());
		
		bewerkenOpslaan = new JLabel();
		bewerkenOpslaan.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/opslaan.png")));
		bewerkenOpslaan.addMouseListener(new BewerkenOpslaanListener());
		
		instellingenMin = new JLabel("Instellingen");
		instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
		instellingenMin.addMouseListener(new InstellingenMinListener());
		
		instellingenPlus = new JLabel("Instellingen");
		instellingenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		instellingenPlus.addMouseListener(new InstellingenPlusListener());
		
		beheerderModel = new DefaultListModel();
		
		for(Beheerder b : m.getBeheerders())			// BeheerderModel vullen met Beheerders uit de ArrayList<Beheerder>
		{
			if(b.isAdmin()==false)
				beheerderModel.addElement(b.getNaam());
		}
		
		beheerderList = new JList(beheerderModel);
		beheerderList.setLayoutOrientation(JList.VERTICAL);
		beheerderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane beheerderScroll = new JScrollPane(beheerderList);
		beheerderScroll.setPreferredSize(new Dimension(150,200));
		beheerderScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		beheerderList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(beheerderList.getSelectedIndex() == -1)
				{
					naamTxt.setText("");
					achternaamTxt.setText("");
					emailTxt.setText("");

				}
				else
				{
					index = beheerderList.getSelectedIndex();
					naamTxt.setEditable(false);
					achternaamTxt.setEditable(false);		// als men op Toevoegen klikt de tekstvakken terug on editable zetten
					emailTxt.setEditable(false);
					
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
						if(beheerderList.getSelectedValue().equals(b.getNaam()))
						{
							m.setBeheerder(beheerderList.getSelectedValue().toString());
							naamTxt.setText(m.getBeheerder().getNaam());
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
		});
						
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
		toevoegen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new ToevoegenListener());
		
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
		instellingenPlus.setVisible(true);
		beheerderPnl.add(instellingenPlus,c);
		
		c.gridx = 2;
		c.gridy = 5;
		instellingenMin.setVisible(false);
		beheerderPnl.add(instellingenMin,c);
		
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
	}
	
	public JPanel getBeheerPanel()
	{
		return allesPanel;
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
			instellingenPlus.setVisible(false);
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
				d.voegBeheerderToeAanDatabank(naamTxt.getText(),achternaamTxt.getText(),"test",emailTxt.getText(),true,true,true,true,false);
				d.getBeheerdersEnBurgersUitDatabank();
				
				naamTxt.setText("");
				achternaamTxt.setText("");
				emailTxt.setText("");
								
				beheerderModel.removeAllElements();
				for(Beheerder b : m.getBeheerders())
				{
					if(b.isAdmin()==false)
						beheerderModel.addElement(b.getNaam());
				}
				
				beheerderList.setSelectedIndex(beheerderList.getLastVisibleIndex());
			}
			
			rechtenPlus.setVisible(true);
				
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
		public void mousePressed(MouseEvent arg0) {
			
			m.getBeheerder().setNaam(naamTxt.getText());
			m.getBeheerder().setAchternaam(achternaamTxt.getText());
			m.getBeheerder().setEmail(emailTxt.getText());
			m.getBeheerder().setKanBeoordelen(beoordelenCb.isSelected());
			m.getBeheerder().setKanToevoegen(toevoegenCb.isSelected());
			m.getBeheerder().setKanVerwijderen(verwijderenCb.isSelected());
			m.getBeheerder().setKanWijzigen(wijzigenCb.isSelected());
			
			d.updateBeheerdersDatabank(m.getBeheerder());
			
			beheerderModel.removeAllElements();
			
			for(Beheerder b : m.getBeheerders())
			{
				if(b.isAdmin()==false)
					beheerderModel.addElement(b.getNaam());
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
				int resultaat = JOptionPane.showConfirmDialog(beheerderPnl, "Wilt u " +m.getBeheerder().getNaam().toString()+
						" verwijderen?","Verwijderen",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				
				if(resultaat == JOptionPane.YES_OPTION)
				{
					d.deleteBeheerder(m.getBeheerder().getId());			// verwijderen uit databank
					m.verwijderBeheerder(m.getBeheerder().getId());			// verwijderen uit ArrayList<Beheerder>
					
					beheerderModel.remove(beheerderList.getSelectedIndex());
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class RechtenPlusListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			rechtenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			rechtenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
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
			rechtenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover_min.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			rechtenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
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
	
	private class InstellingenPlusListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			instellingenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			instellingenPlus.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			instellingenMin.setVisible(true);
			instellingenPlus.setVisible(false);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}	
	}
	
	private class InstellingenMinListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover_min.png")));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			instellingenMin.setVisible(false);
			instellingenPlus.setVisible(true);
		}	
	}
}
