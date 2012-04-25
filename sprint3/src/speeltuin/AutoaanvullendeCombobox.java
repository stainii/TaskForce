package speeltuin;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AutoaanvullendeCombobox<T> extends JComboBox  
{
	private JTextField txt = new JTextField(); 
	private ArrayList<T> lijst;
	private boolean hide_flag;
	private String object;
	
	public AutoaanvullendeCombobox(ArrayList<T> l, String ob)
	{
		this.lijst = l;
		this.object = ob;
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
							setModel(new DefaultComboBoxModel(lijst.toArray()),"");
						}
						else
						{
							DefaultComboBoxModel m = getSuggestedModel(lijst,tekst,object);
							
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
		            for(int i=0;i<lijst.size();i++) 
		            {
		                String str =  (String) lijst.get(i);
		            
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
		
		setModel(new DefaultComboBoxModel(lijst.toArray()), "");
		
		if(ob.equals("Integer"))
		{
			this.setPrototypeDisplayValue("XXXX");
		}
		else
			this.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXX");
	}
	
	public JComboBox getAutoCombo()
	{
		return this;
	}
	
	private DefaultComboBoxModel getSuggestedModel(ArrayList<T> list, String text,String welkObject) 
	{
        DefaultComboBoxModel m = new DefaultComboBoxModel();
       
    	if( welkObject.equals("Integer"))
    	{
    		for(T s : list)
    		{
    			if( ((Integer) s).toString().startsWith(text) )
    				m.addElement(s);
    		}
    		return m;
    	}
    	else
    	{
            for(T s: list) 
            {
            	if(((String) s).startsWith(text)) 
                	m.addElement(s);
            }
            return m;
    	}
    }
    private void setModel(DefaultComboBoxModel mdl, String str) 
    {
        getAutoCombo().setModel(mdl);
        getAutoCombo().setSelectedIndex(-1);
        txt.setText(str);
    }
	
}
