package guiElementen;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *	Deze klasse zorgt ervoor dat er een limiet aan tekens kan gegeven worden aan tekstveld.
 *  Deze klasse wordt in veel andere klassen gebruikt waar een JTextField aanwezig is.
 */

@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument
{
	  private int limit;
	  public JTextFieldLimit(int limit) {
	    super();
	    this.limit = limit;
	  }

	  JTextFieldLimit(int limit, boolean upper) {
	    super();
	    this.limit = limit;
	  }

	  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str, attr);
	    }
	  }
}
