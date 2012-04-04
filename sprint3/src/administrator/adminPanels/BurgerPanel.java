package administrator.adminPanels;

import java.awt.Dimension;
import java.awt.GridBagLayout;
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
	private JLabel gebruikersnaam, voornaam, familienaam;
	private JTextField gebruikerTxt, naamTxt, familienaamTxt;
	
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
	}
	
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
		}
	}
}
