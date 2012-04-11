package administrator.adminPanels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Model;

@SuppressWarnings("serial")
public class InstelView extends JPanel
{
	private JPanel instelPanel,beheerder;
	private JLabel instellingenMin,plus;
	private Model m;
	
	public InstelView(JPanel bh, JLabel pl, Model model)
	{
		this.beheerder = bh;
		this.plus = pl;
		this.m = model;
		
		instelPanel = new JPanel();
		instelPanel.setLayout(new GridBagLayout());
		
		instellingenMin = new JLabel("Instellingen");
		instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
		instellingenMin.addMouseListener(new MinListener());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,5,5);		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		//titel
		c.gridx = 1;
		c.gridy = 2;
		JLabel titel = new JLabel("Instellingen voor "+m.getBeheerder().getNaam());
		titel.setFont(new Font("Comic Sans", Font.BOLD, 16));
		instelPanel.add(titel,c);
		
		c.gridx = 2;
		c.gridy = 1;
		instelPanel.add(instellingenMin,c);
	}
	
	public JPanel getInstelPanel()
	{
		return instelPanel;
	}
	
	private class MinListener implements MouseListener
	{
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			instelPanel.setVisible(false);
			beheerder.setVisible(true);
			plus.setVisible(true);	
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_min.png")));
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			instellingenMin.setIcon(new ImageIcon(getClass().getResource("../../views/imgs/toevoegenIco_hover_min.png")));
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {}
	}
}
