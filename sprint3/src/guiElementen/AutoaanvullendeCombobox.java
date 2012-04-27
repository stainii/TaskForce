package guiElementen;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

@SuppressWarnings("serial")
public class AutoaanvullendeCombobox<T> extends JComboBox  
{
	private JTextComponent txt; 
	private ArrayList<T> lijst;
	private boolean hide_flag;
	private String object;
	private String defaultTekst;
	private Font font;
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (((JTextComponent) txt).getText().equals("") && isEditable())
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(font);
			g2.setColor(new Color(180,180,180));
			g2.drawString(defaultTekst, 2, font.getSize());
		}
		else
		{
			setBackground(Color.WHITE);
		}
	}
	
	public AutoaanvullendeCombobox(ArrayList<T> l, String ob, String defaultTekst,JTextComponent soort, int max)
	{
		this.defaultTekst = defaultTekst;
		this.lijst = l;
		this.object = ob;
		this.font = getFont();
		
		setEditable(true);
		
		txt = soort; 
		txt = (JTextField)this.getEditor().getEditorComponent();
		
		
		txt.setDocument(new JTextFieldLimit(max));
		

			txt.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					if(txt.getText().equals("Postcode"))
						txt.setText("");
					if(txt.getText().equals("Deelgemeente"))
						txt.setText("");
				}
			});
			
			txt.addFocusListener(new FocusAdapter() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					if(object.equals("Integer"))
					{
						if(txt.getText().equals(""))
							txt.setText("Postcode");
					}
					if(object.equals("String"))
					{
						if(txt.getText().equals(""))
							txt.setText("Deelgemeente");
					}
					
				}
			});
		

		txt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}

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
			public void keyReleased(KeyEvent e) {
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
