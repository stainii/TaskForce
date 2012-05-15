package systemTray;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
				String[] args = new String[1];	//er wordt een argument meegegeven met de main-methode. Dit doen we zodat er geen nieuwe System Tray meer gemaakt wordt
				Start.main(args);
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