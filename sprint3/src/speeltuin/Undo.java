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
import javax.swing.JScrollPane;
import controllers.Databank;
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
			if (a.getDocument()!=null)
				panel.add(new JLabel(a.getDocument().getTitel() + " (document)"),c);
			else
				panel.add(new JLabel(a.getErfgoed().getNaam() + " (erfgoed)"),c);
			
			c.gridx = 2;
			panel.add(new JLabel(a.getActie()),c);
			
			c.gridx = 3;
			panel.add(new JLabel(a.getDatum().toString().substring(0,19)),c);
			
			c.gridx = 4;
			JButton button = new JButton("Ongedaan maken");
			
			if (a.getActie().equals("Verwijderd"))
			{
				button.addActionListener(new VerwijderdActionListener(a));
			}
			else if (a.getActie().equals("Toegevoegd"))
			{
				button.addActionListener(new ToegevoegdActionListener(a));
			}
			else if (a.getActie().equals("Gewijzigd"))
			{
				button.setVisible(false);
			}
			else if (a.getActie().equals("Afgekeurd"))
			{
				button.addActionListener(new BeoordelingActionListener(a,true));
			}
			else if (a.getActie().equals("Goedgekeurd"))
			{
				button.addActionListener(new BeoordelingActionListener(a,false));
			}
			
			panel.add(button,c);
			y++;
		}
		
		panel.setVisible(true);
		JScrollPane scroll = new JScrollPane(panel);
		add(scroll);
	}
	
	public static void main(String[] args)
	{
		Model m = new Model();
		Databank d = new Databank(m);
		d.laadDatabank();
		m.setBeheerder("Stijn");
		
        JFrame f = new Undo(m,d);
        f.setSize(600,400);
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
			if (actie.getDocument()!=null)
				databank.verwijderenOngedaanMaken(actie.getDocument());
			else
				databank.verwijderenOngedaanMaken(actie.getErfgoed());
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
			if (actie.getDocument()!=null)
				databank.verwijderDocument(actie.getDocument());
			else
				databank.verwijderErfgoed(actie.getErfgoed());
			leesUitDatabank();
		}
	}
	class BeoordelingActionListener implements ActionListener
	{
		private Actie actie;
		private boolean goedgekeurd;
		
		public BeoordelingActionListener(Actie a, boolean goedgekeurd)
		{
			this.actie = a;
			this.goedgekeurd = goedgekeurd;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			databank.beoordeelDocument(actie.getDocument(),goedgekeurd);
			leesUitDatabank();
		}
	}
}