package speeltuin;

import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**Deze klasse laat een stem horen die boodschappen kan zeggen adhv geluidssamples.
 * Het oorspronkelijke idee was om bij het opstarten een stem te laten zeggen hoeveel
 * onbeoordeelde bestanden er waren. Deze functie is (professioneler) overgenomen door
 * de System Tray */

public class Stainii
{
	ArrayList<AudioInputStream> files = new ArrayList<AudioInputStream>();
	
	  
	public Stainii()
	{
		try
		{
				loadSamples();
		}
		catch (UnsupportedAudioFileException e)
		{
				e.printStackTrace();
		}
		catch (IOException e)
		{
				e.printStackTrace();
		}
	}
	public void speak(int aantal)
	{
		try
		{
			say(0);	//Welkom, u heeft
			if (aantal == 0)
			{
				say(3);		//geen
				say(1); 	//nog niet beoordeelde documenten
			}
			if (aantal == 1)
			{
				say(4);		//1
				say(2); 	//nog niet beoordeeld document
			}
			say(1); 	//nog niet beoordeelde documenten
			
				
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	public void say(int i) throws LineUnavailableException, IOException
	{
		/*
		 * Speak sample 
		 */
		byte[] buffer = new byte[4096]; 
		
		AudioInputStream f = files.get(i);
		AudioFormat format = f.getFormat(); 
	    SourceDataLine line = AudioSystem.getSourceDataLine(format); 
	    line.open(format); 
	    line.start(); 
	     while (f.available() > 0)
	     { 
	    	 int len = f.read(buffer); 
	         line.write(buffer, 0, len); 
	     }
	     line.drain();
	     line.close();
	}
		
	public void loadSamples() throws UnsupportedAudioFileException, IOException
	{
		/*
		 * Alle mogelijke samples laden
		 */
		files.add(AudioSystem.getAudioInputStream(getClass().getResource("resources/deel1.wav")));
		files.add(AudioSystem.getAudioInputStream(getClass().getResource("resources/deel2.wav")));
		files.add(AudioSystem.getAudioInputStream(getClass().getResource("resources/deel2b.wav")));
		files.add(AudioSystem.getAudioInputStream(getClass().getResource("resources/geen.wav")));
	}
	
	public static void main (String args[])
	{
		Stainii s = new Stainii();
		s.speak(0);
	}
}
