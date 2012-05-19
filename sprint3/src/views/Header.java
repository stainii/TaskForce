package views;

import guiElementen.InstelView;
import guiElementen.InstelView;
import guiElementen.JLabelFactory;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controllers.Databank;
import model.Model;


/** De rode header bovenaan het programma. Bevat loguit-functie en later eventueel menu-opties**/


@SuppressWarnings("serial")
public class Header extends JPanel implements MouseListener
{
	//rode achtergrond
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("imgs/header.png"));
	private Image background = backgroundIcon.getImage();
	
	//logo linksboven
	private ImageIcon logoIcon = new ImageIcon(getClass().getResource("imgs/logoHeader.png"));
	private Image logo = logoIcon.getImage();
	
	private JLabel inlogLbl, uitlogLbl,instellingen;
	private Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	private Model m;
	private JFrame frame;
	private OverzichtView v;
	private Databank d;
	private Hoofd h;

	@Override
	protected void paintComponent(Graphics g) 		//headerachtergrond tekenen
	{
		super.paintComponent(g);
		if (background != null)
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		if(logo != null)
			g.drawImage(logo, 8,0,88,110,this);
	}
	
	public Header(Model model, JFrame f,OverzichtView view, Hoofd h, Databank data)
	{
		this.m = model;
		this.frame =f;
		this.v = view;
		this.d = data;
		this.h = h;
		
		setOpaque(false);
		
		setPreferredSize(new Dimension(1000,135));	//de breedte maakt niet uit, wordt overridden door de layoutmanager
		
		inlogLbl = new JLabelFactory().getBeheerderLogin(m.getBeheerder().getGebruikersnaam());
		uitlogLbl = new JLabelFactory().getUitloggenTekst("Uitloggen");
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
		uitlogLbl.addMouseListener(this);
		
		instellingen = new JLabelFactory().getNormaleTekst("Instellingen");
		instellingen.setIcon(new ImageIcon(getClass().getResource("imgs/instellingen.png")));
		instellingen.addMouseListener(new InstellingenListener());
		
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel p = new JPanel();
		p.setOpaque(false);
		
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(7,15,5,15);
		c.gridx = 2;
		c.gridy = 1;		
		p.add(inlogLbl, c);
		
		c.insets = new Insets(0,13,5,15);
		c.gridx = 2;
		c.gridy = 3;
		p.add(uitlogLbl,c);
		
		c.gridx = 2;
		c.gridy = 2;
		p.add(instellingen,c);
		
		add(p);
		
	}
	
	/** 
	 * Sun property pointing the main class and its arguments. 
	 * Might not be defined on non Hotspot VM implementations.
	 */
	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	/**
	 * Restart the current Java application
	 * @param runBeforeRestart some custom code to be run before restarting
	 * @throws IOException
	 */
	public static void restartApplication(Runnable runBeforeRestart) throws IOException {
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

			// program main and program arguments
			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			// program main is a jar
			if (mainCommand[0].endsWith(".jar")) {
				// if it's a jar, add -jar mainJar
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
				// else it's a .class, add the classpath and mainClass
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			}
			// finally add program arguments
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			// execute the command in a shutdown hook, to be sure that all the
			// resources have been disposed before restarting the application
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			// execute some custom code before restarting
			if (runBeforeRestart!= null) {
				runBeforeRestart.run();
			}
			// exit
			System.exit(0);
		} catch (Exception e) {
			// something went wrong
			throw new IOException("Error while trying to restart the application", e);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen_hover.png")));
		uitlogLbl.setCursor(hand);
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		uitlogLbl.setIcon(new ImageIcon(getClass().getResource("imgs/uitloggen.png")));
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		h.quit();
		try {
			restartApplication(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private class InstellingenListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			instellingen.setIcon(new ImageIcon(getClass().getResource("imgs/instellingen_hover.png")));
			instellingen.setCursor(hand);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			instellingen.setIcon(new ImageIcon(getClass().getResource("imgs/instellingen.png")));
		}

		@Override
		public void mousePressed(MouseEvent e)
		{			
			new InstelView(m,frame,v,d);
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}