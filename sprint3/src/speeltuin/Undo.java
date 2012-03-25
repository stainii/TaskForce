package speeltuin;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.Databank;

import model.Actie;
import model.DocumentCMS;
import model.Model;

@SuppressWarnings("serial")
public class Undo extends JFrame
{
	private Model model;
	private Databank databank;
	private JPanel panel;
	
	public Undo(Model m, Databank d)
	{
		this.model = m;
		this.databank = d;
		
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		add(panel);
		
		leesUitDatabank();		

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void leesUitDatabank()
	{
		panel.setVisible(false);
		panel.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);
		int y = 1;
		
		ArrayList<Actie> acties = databank.getActies();		
		for (Actie a: acties)
		{			
			c.gridy = y;
			
			c.gridx = 1;
			panel.add(new JLabel(a.getDocument().getTitel()),c);
			
			c.gridx = 2;
			panel.add(new JLabel(a.getActie()),c);
			
			c.gridx = 3;
			panel.add(new JLabel(a.getDatum().toString()),c);
			
			c.gridx = 4;
			JButton button = new JButton("Ongedaan maken");
			
			if (a.getActie().equals("Verwijderd"))
				button.addActionListener(new VerwijderdActionListener(a));
			else if (a.getActie().equals("Toegevoegd"))
				button.addActionListener(new ToegevoegdActionListener(a));
			
			panel.add(button,c);
			y++;
		}
		
		panel.setVisible(true);
		pack();
	}
	
	public static void main(String[] args)
	{
		Model m = new Model();
		Databank d = new Databank(m);
		d.laadDatabank();
		m.setBeheerder("Stijn");
		
        new Undo(m,d);
    }
	
	class VerwijderdActionListener implements ActionListener
	{
		private Actie actie;
		public VerwijderdActionListener(Actie a)
		{
			this.actie = a;
		}
		

		@Override
		public void actionPerformed(ActionEvent e)
		{
			databank.verwijderenOngedaanMaken(actie.getDocument());
			leesUitDatabank();
		}
	}
	
	class ToegevoegdActionListener implements ActionListener
	{
		private Actie actie;
		public ToegevoegdActionListener(Actie a)
		{
			this.actie = a;
		}
		

		@Override
		public void actionPerformed(ActionEvent e)
		{
			databank.verwijderDocument(actie.getDocument());
			leesUitDatabank();
		}
	}
}