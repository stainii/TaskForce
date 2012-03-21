package speeltuin;
/*

Database Programming with JDBC and Java, Second Edition
By George Reese
ISBN: 1-56592-616-1

Publisher: O'Reilly

*/


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;

/**
 * Example 4.2.
 */
public class TestVideoBLOB 
{
	private JFileChooser chooser;
	
  public static void main(String args[])
  {
	  TestVideoBLOB t = new TestVideoBLOB();
	  t.schrijven();
	  t.lezen();  
  }
  
  public void lezen()
  {
	  try
	  {
		  Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc");
	      
	      chooser.showOpenDialog(null);
	      File f = chooser.getSelectedFile();
	      
	      PreparedStatement stmt;
	
	      if (!f.exists())
	      {
	        // if the file does not exist
	        // retrieve it from the database and write it to the named file
	        ResultSet rs;
	
	        stmt = con.prepareStatement("SELECT BLOB FROM DOCUMENT WHERE DocumentId = 1");
	        rs = stmt.executeQuery();
	        
	        rs.next();
	        
	        Blob b = rs.getBlob("BLOB");
	        
	        FileOutputStream os = new FileOutputStream(f);
	        System.out.println("Bezig met schrijven");
	        os.write(b.getBytes(0, (int) b.length()));
	        System.out.println("Klaar");
	        os.flush();
	        os.close();
	      }
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
    }
    
    public void schrijven()
    {
    	Connection con = null;
        chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        
        PreparedStatement stmt;
        try
        {
        	con = DriverManager.getConnection("jdbc:sqlserver://localhost;database=Projecten2;user=JDBC;password=jdbc");
	        
        	
	        /*FileInputStream fis = new FileInputStream(f);
	        byte[] tmp = new byte[1024];
	        byte[] data = null;
	        int sz, len = 0;
	        
	        System.out.println("Start lezen");
	        while ((sz = fis.read(tmp)) != -1) {
	          if (data == null) {
	            len = sz;
	            data = tmp;
	          } else {
	            byte[] narr;
	            int nlen;
	
	            nlen = len + sz;
	            narr = new byte[nlen];
	            System.arraycopy(data, 0, narr, 0, len);
	            System.arraycopy(tmp, 0, narr, len, sz);
	            data = narr;
	            len = nlen;
	          }
	        }
	        if (len != data.length) {
	          byte[] narr = new byte[len];
	
	          System.arraycopy(data, 0, narr, 0, len);
	          data = narr;
	        }*/
        	
        	FileInputStream data = new FileInputStream(f);
	        
	        System.out.println("Start wegschrijven");
	        stmt = con.prepareStatement("UPDATE Document SET BLOB = ?, TypeDocument='Video' WHERE DocumentId=1");
	        stmt.setObject(1, data);
	        stmt.executeUpdate();
	        System.out.println("Stop wegschrijven");
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}