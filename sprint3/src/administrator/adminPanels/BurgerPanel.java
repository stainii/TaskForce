package administrator.adminPanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import controllers.Databank;
import model.Beheerder;
import model.Burger;
import model.Model;

@SuppressWarnings("serial")
public class BurgerPanel extends JPanel
{
	private Model m;
	private Databank d;
	private JPanel allesPanel, burgerPanel;
	private JList burgerList;
	private DefaultListModel burgerModel;
	private JLabel gebruikersnaam, voornaam, familienaam, email;
	private JTextField gebruikerTxt, naamTxt, familienaamTxt, emailTxt;
	
	public BurgerPanel(Model model, Databank data)
	{
		this.m = model;
		this.d = data;
		
		allesPanel = new JPanel();		// bevat de 2 panels
		
		burgerPanel = new JPanel();
		burgerPanel.setLayout(new GridBagLayout());
		
		burgerModel = new DefaultListModel();
		
		for(Burger b : m.getBurgers())
		{
			burgerModel.addElement(b.getNaam());
		}
		
		burgerList = new JList(burgerModel);
		burgerList.setLayoutOrientation(JList.VERTICAL);
		burgerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane adminScroll = new JScrollPane(burgerList);
		adminScroll.setPreferredSize(new Dimension(150,100));
		adminScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		burgerList.addListSelectionListener(new ListListener());
		
		gebruikersnaam = new JLabel("Gebruikersnaam");
		voornaam = new JLabel("Voornaam");
		familienaam = new JLabel("Familienaam");
		email = new JLabel("E-mail");
		
		gebruikerTxt = new JTextField();
		naamTxt = new JTextField();
		familienaamTxt = new JTextField();
		emailTxt = new JTextField();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 1;
		c.gridy = 1;
		burgerPanel.add(gebruikersnaam,c);
		
		c.gridx = 2;
		c.gridy = 1;
		burgerPanel.add(gebruikerTxt,c);
		
		c.gridx = 1;
		c.gridy = 2;
		burgerPanel.add(voornaam,c);
		
		c.gridx = 2;
		c.gridy = 2;
		burgerPanel.add(naamTxt,c);
		
		c.gridx = 1;
		c.gridy = 3;
		burgerPanel.add(familienaam,c);
		
		c.gridx = 2;
		c.gridy = 3;
		burgerPanel.add(familienaamTxt,c);
		
		c.gridx = 1;
		c.gridy = 4;
		burgerPanel.add(email,c);
		
		c.gridx = 2;
		c.gridy = 4;
		burgerPanel.add(emailTxt,c);
		
		
		allesPanel.add(adminScroll,BorderLayout.LINE_START);
		allesPanel.add(burgerPanel,BorderLayout.CENTER);
	}
	
	public JPanel getBurgerPanel()
	{
		return allesPanel;
	}
	
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			if(burgerList.getSelectedIndex() == -1)
			{
				gebruikerTxt.setText("");
				naamTxt.setText("");
				familienaamTxt.setText("");
				emailTxt.setText("");
			}
			else
			{
				gebruikerTxt.setEditable(false);
				naamTxt.setEditable(false);
				familienaamTxt.setEditable(false);
				emailTxt.setEditable(false);
				
				for(Burger b : m.getBurgers())
				{
					if(burgerList.getSelectedValue().equals(b.getNaam()))
					{
						m.setBurger(burgerList.getSelectedValue().toString());
						
						gebruikerTxt.setText(b.getGebruikersnaam());
						naamTxt.setText(b.getNaam());
						familienaamTxt.setText(b.getFamilienaam());
						emailTxt.setText(b.getEmail());			
					}
				}
			}
		}
		
	}
}
