package systemTray;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import views.Start;
import controllers.Databank;
import model.DocumentCMS;
import model.Model;

public class InSystemTray
{
	/** Dit is de SystemTray modus. Het programma wordt niet afgesloten maar draait in de achtergrond
	 * Als er een nieuw document wordt toegevoegd, verwijderd, gewijzigd, komt er een ballonetje om dit te melden.
	 */
	
	private Model m;
	private Databank d;
	
	private final PopupMenu popUp = new PopupMenu();
    private final TrayIcon trayIcon =	new TrayIcon(createImage("erfgoedpin.png", "tray icon"));
    private final SystemTray tray = SystemTray.getSystemTray();
    private MenuItem openenItem = null;
    private int startId;
    
    public InSystemTray(Model m, Databank d)
	{	
		//controleren of het besturingssysteem wel een SystemTray heeft
		if (!SystemTray.isSupported())
		{
            System.out.println("SystemTray wordt niet ondersteund door uw besturingssysteem");
            return;
        }
		
		this.d = d;
		this.m =m;       
        
        //menu maken
        openenItem = new MenuItem("Openen");
        openenItem.addActionListener(new ActionListener()
        {	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					restartApplication(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
        MenuItem afsluitenItem = new MenuItem("Afsluiten");
        afsluitenItem.addActionListener(new ActionListener()
        {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
        });
       
        //menu toevoegen
        popUp.add(openenItem);
        popUp.add(afsluitenItem);
       
        trayIcon.setPopupMenu(popUp);
       
        //icoon toevoegen
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
        //start het controleren
        start();
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
    		java.util.List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
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
	//maakt een icoon van een afbeelding
    public static Image createImage(String path, String description) {
        URL imageURL = InSystemTray.class.getResource(path);
         
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    
    public void start()
    {
    	//Eerste boodschap: weergeven hoeveel nog niet beoordeelde documenten er zijn
    	int aantalNogNietBeoordeeld = 0;
    	
    	for (DocumentCMS d : m.getDocumenten())
    	{
    		if (d.getStatus().equals("Nog niet beoordeeld"))
    		{
    			aantalNogNietBeoordeeld++;
    		}
    	}
    	
    	if (aantalNogNietBeoordeeld == 0)
    		trayIcon.displayMessage("Welkom bij Herzele Erfgoed CMS", "Welkom, er zijn geen nog niet beoordeelde documenten.", TrayIcon.MessageType.INFO);
    	else if (aantalNogNietBeoordeeld == 1)
    		trayIcon.displayMessage("Welkom bij Herzele Erfgoed CMS", "Welkom, er is 1 nog niet beoordeeld document.", TrayIcon.MessageType.INFO);
    	else
    		trayIcon.displayMessage("Welkom bij Herzele Erfgoed CMS", "Welkom, er zijn " + aantalNogNietBeoordeeld + " nog niet beoordeelde documenten.", TrayIcon.MessageType.INFO);
    	
    	
    	//Het id van de laatste log opslaan
    	startId = d.getIdLaatsteRijLogboek();
    	//om de 4000 ms (4 seconden) een controle uitvoeren
    	new Timer().scheduleAtFixedRate(new Controleren(), 0, 4000);
    	
    }
    
    public void zegHallo()	//wordt aangeroepen als het venster gesloten wordt
    {
    	trayIcon.displayMessage("Task Force", "De CMS draait nog steeds in de achtergrond. U wordt verwittigd als er externe wijzigingen aan de databank worden aangebracht.", TrayIcon.MessageType.INFO);
    }
    public void zegIets(String s)
    {
    	trayIcon.displayMessage("Task Force", s, TrayIcon.MessageType.INFO);
    }
    
    private class Controleren extends TimerTask
    {
        @Override
        public void run()
        {
        	int laatsteId = d.getIdLaatsteRijLogboek();
        	
        	if (laatsteId > startId)	//kijk of er nieuwe id's bijgekomen zijn
        		{
        			trayIcon.displayMessage("Externe wijziging", d.synchroniseerModel(startId), TrayIcon.MessageType.INFO);
        			startId = laatsteId;		//controle uitgevoerd, we beginnen opnieuw te controleren met de huidige tijd        		        		
        		}
        	}
        }  
}