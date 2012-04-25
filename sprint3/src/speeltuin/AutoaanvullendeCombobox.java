package speeltuin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import model.Gemeenten;
import model.Model;

public class AutoaanvullendeCombobox extends JComboBox
{
	
	private Model m;
	private JTextField txt = new JTextField(); 
	private ArrayList<String> gemeente = new ArrayList<String>();
	private boolean hide_flag;
	private JComboBox combo = new JComboBox();
	
	public AutoaanvullendeCombobox(Model model)
	{
		this.m = model;
		combo = this;
		setEditable(true);
		txt = (JTextField) this.getEditor().getEditorComponent();
		
		txt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

				EventQueue.invokeLater(new Runnable() 
				{
					@Override
					public void run() {
						String tekst = txt.getText();
						
						if(tekst.length()==0)
						{
							getAutoCombo().hidePopup();
							setModel(new DefaultComboBoxModel(gemeente.toArray()),"");
						}
						else
						{
							DefaultComboBoxModel m = getSuggestedModel(gemeente,tekst);
							
							if(m.getSize()==0 || hide_flag) 
            				{
            					getAutoCombo().hidePopup();
            					hide_flag = false;
            				}
            				else
            				{
            					setModel(m,tekst);
            					getAutoCombo().showPopup();
            				}
						}
					}
					
				});
			}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				String tekst = txt.getText();
				int code = e.getKeyCode();
				
				if(code == KeyEvent.VK_ENTER)
				{
					
				}
				else if(code==KeyEvent.VK_ESCAPE) 
				{
		            hide_flag = true; 
		        }
				else if(code==KeyEvent.VK_RIGHT) 
				{
		            for(int i=0;i<gemeente.size();i++) 
		            {
		                String str =  gemeente.get(i);
		            
		                if(str.startsWith(tekst)) 
		                {
		                    getAutoCombo().setSelectedIndex(-1);
		                    txt.setText(str);
		                    return;
		                }
		            }
		        }
			}

			@Override
			public void keyReleased(KeyEvent e) {}
		});
		
		for(Gemeenten g : m.getGemeenten())
		{
			gemeente.add(g.getGemeente());
		}
		
		setModel(new DefaultComboBoxModel(gemeente.toArray()), "");
		
		this.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXX");
		
	}
	
	public JComboBox getAutoCombo()
	{
		return this;
	}
	
	private static DefaultComboBoxModel getSuggestedModel(ArrayList<String> list, String text) 
	{
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) 
        {
            if(s.startsWith(text)) 
            	m.addElement(s);
        }
        return m;
    }
    private void setModel(DefaultComboBoxModel mdl, String str) 
    {
        getAutoCombo().setModel(mdl);
        getAutoCombo().setSelectedIndex(-1);
        txt.setText(str);
    }
	
}
