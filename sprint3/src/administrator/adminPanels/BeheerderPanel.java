package administrator.adminPanels;

import java.awt.Dimension;
import java.awt.Graphics;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controllers.Databank;
import controllers.Login;
import model.Beheerder;
import model.Model;

public class BeheerderPanel extends JPanel
{
	private Model m;
	private Databank d;
	private JList beheerderList;
	private DefaultListModel beheerderModel;
	private JPanel beheerderPnl;
	private JTextField naamTxt, achternaamTxt, emailTxt;
	private JLabel rechten, instellingen, opslaan,wijzigen,verwijderen,naamLbl,achternaamLbl,emailLbl,toevoegen;
	
	@Override
	public void paint(Graphics g) 	
	{
		super.paintComponent(g);
	}
	
	public BeheerderPanel(Model model, Databank data )
	{
		this.m=model;
		this.d=data;
		
		beheerderPnl = new JPanel();
		beheerderPnl.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(5,5,5,5);
		
		beheerderModel = new DefaultListModel();
	
		for(Beheerder b : m.getBeheerders())
		{
			beheerderModel.addElement(b.getNaam());
		}
		
		naamTxt = new JTextField();
		achternaamTxt = new JTextField();
		emailTxt = new JTextField();
		
		naamTxt.setEditable(false);
		achternaamTxt.setEditable(false);
		emailTxt.setEditable(false);
		
		rechten = new JLabel("Rechten");
		rechten.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		instellingen = new JLabel("Instellingen");
		instellingen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		verwijderen = new JLabel("Verwijderen");
		wijzigen = new JLabel("Wijzigen");
		
		opslaan = new JLabel();
		opslaan.setIcon(new ImageIcon(getClass().getResource("../../guiElementen/imgs/opslaan.png")));
		opslaan.addMouseListener(new OpslaanListener());
		
		beheerderList = new JList(beheerderModel);
		beheerderList.setPreferredSize(new Dimension(100,150));
		
		beheerderList.setLayoutOrientation(JList.VERTICAL);
		beheerderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
					naamTxt.setEditable(false);
					achternaamTxt.setEditable(false);		// als men op Toevoegen klikt de tekstvakken terug on editable zetten
					emailTxt.setEditable(false);
					
					rechten.setVisible(true);
					verwijderen.setVisible(true);
					instellingen.setVisible(true);
					wijzigen.setVisible(true);
					opslaan.setVisible(false	);
					
					for(Beheerder b : m.getBeheerders())
					{
						if(beheerderList.getSelectedValue().equals(b.getNaam()))
						{
							m.setBeheerder(beheerderList.getSelectedValue().toString());
							naamTxt.setText(m.getBeheerder().getNaam());
							achternaamTxt.setText(m.getBeheerder().getAchternaam());
							emailTxt.setText(m.getBeheerder().getEmail());
						}
					}
				}
					
			}
		});
		
		JScrollPane beheerderScroll = new JScrollPane(beheerderList);
		beheerderScroll.setPreferredSize(new Dimension(150,200));
		
		naamLbl = new JLabel("Naam");
		achternaamLbl = new JLabel("Achternaam");
		emailLbl = new JLabel("E-mail");
		
		toevoegen = new JLabel("Voeg beheerder toe");
		toevoegen.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco.png")));
		toevoegen.addMouseListener(new ToevoegenListener());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
				
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 3;
		c.gridy = 1;
		beheerderPnl.add(toevoegen,c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 7;
		beheerderPnl.add(beheerderScroll,c);
				
		c.gridx = 2;
		c.gridy = 2;	
		c.gridheight = 1;
		beheerderPnl.add(naamLbl,c);
		
		c.gridx = 3;
		c.gridy = 2;
		beheerderPnl.add(naamTxt,c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridheight = 1;
		beheerderPnl.add(achternaamLbl,c);
		
		c.gridx = 3;
		c.gridy = 3;
		beheerderPnl.add(achternaamTxt,c);
		
		c.gridx = 2;
		c.gridy = 4;
		c.gridheight = 1;
		beheerderPnl.add(emailLbl,c);
		
		c.gridx = 3;
		c.gridy = 4;
		beheerderPnl.add(emailTxt,c);
		
		c.gridx = 2;
		c.gridy = 5;
		beheerderPnl.add(rechten,c);
		
		c.gridx = 3;
		c.gridy = 5;
		instellingen.setVisible(true);
		beheerderPnl.add(instellingen,c);
		
		c.gridx = 3;
		c.gridy = 5;
		opslaan.setVisible(false);
		beheerderPnl.add(opslaan,c);
		
		c.gridx = 2;
		c.gridy = 6;
		beheerderPnl.add(verwijderen,c);		// deze laten veranderen naar Annuleren bij wijziging
		
		c.gridx = 3;
		c.gridy = 6;
		beheerderPnl.add(wijzigen,c);
	}
	
	public JPanel getBeheerPanel()
	{
		return beheerderPnl;
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
			
			naamTxt.setText("");
			achternaamTxt.setText("");
			emailTxt.setText("");
			
			naamTxt.setEditable(true);
			achternaamTxt.setEditable(true);
			emailTxt.setEditable(true);
			
			rechten.setVisible(false);
			verwijderen.setVisible(false);
			wijzigen.setVisible(false);
			instellingen.setVisible(false);
			opslaan.setVisible(true);
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	private class OpslaanListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			//controle of alle velden zijn ingevuld.
			if(naamTxt.getText().isEmpty() || achternaamTxt.getText().isEmpty() || emailTxt.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Beide velden moeten ingevuld zijn!" ,"Velden zijn leeg",JOptionPane.ERROR_MESSAGE);
			else
			{
				d.voegBeheerderToeAanDatabank(naamTxt.getText(),achternaamTxt.getText(),"test",emailTxt.getText(),true,true,true,true);
				
				naamTxt.setText("");
				achternaamTxt.setText("");
				emailTxt.setText("");
								
				beheerderModel.removeAllElements();
				for(Beheerder b : m.getBeheerders())
				{
					beheerderModel.addElement(b.getNaam());
				}

			}
				
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
