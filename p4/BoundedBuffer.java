package p4;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * 
 * @author Barry Al-Jawari
 *
 */
public class BoundedBuffer {
	private String[] buffer = new String[10];
	private Status[] status = new Status[10];
	private JTextPane textPaneDest;
	private int writePos = 0, readPos = 0, modifyPos = 0;
	private String finalText = "";
	private String textReplace = "";
	private LinkedList<String> list = new LinkedList<String>();

	public BoundedBuffer(JTextPane textPaneDest) {
		this.textPaneDest = textPaneDest;
		setAllEmpty();
	}

	public enum Status {
		EMPTY, CHECKED, NEW;
	}

	/**
	 * 
	 * Här så går writern in och kollar ifall status det är tomt (empty), OM det är det. isåfall skriver den in
	 * en sträng på den positionen och sätter status till new. Annars så kommer den vänta.
	 * den.
	 * 
	 * @param line
	 */
	public synchronized void write(String line) {
		while (status[writePos] != Status.EMPTY) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (status[writePos] == Status.EMPTY) {
			buffer[writePos] = line;
			status[writePos] = Status.NEW;
			writePos = (writePos + 1) % buffer.length;
			notifyAll();
		}
	}

	/**
	 * Här så går den in i Modifier och kollar ifall status är ny (new),OM det är det. isåfall går den in och
	 * kollar ifall nåt ord ska ändras och sätter status till checked. Annars så kommer den vänta.
	 * 
	 * @param textFind
	 * @param textReplace
	 * @throws InterruptedException
	 */
	public synchronized void modify(String textFind, String textReplace)
			throws InterruptedException {
		this.textReplace = textReplace;
		while (status[modifyPos] != Status.NEW) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (status[modifyPos] == Status.NEW) {
			if (buffer[modifyPos].contains(textFind)) {
				buffer[modifyPos] = buffer[modifyPos].replace(textFind,
						textReplace);
			}
			status[modifyPos] = Status.CHECKED;
			modifyPos = (modifyPos + 1) % buffer.length;
			notifyAll();
		}
	}

	/**
	 * Här så går den in i readern och kollar ifall status är markerat (checked), OM det är det. isåfall går den in och
	 * läser strängen och sätter status till empty, Annars så kommer den vänta.
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void read() throws InterruptedException {
		while (status[readPos] != Status.CHECKED) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (status[readPos] == Status.CHECKED) {
			list.add(buffer[readPos]);
			status[readPos] = Status.EMPTY;
			readPos = (readPos + 1) % buffer.length;
			notifyAll();
		}
	}

	/**
	 * Metoden här sätter alla positioner till empty status
	 */
	public void setAllEmpty() {
		for (int i = 0; i < status.length; i++) {
			status[i] = Status.EMPTY;
		}
	}

	/**
	 * Skriver ut texten.
	 */
	public void setText() {
		for (int i = 0; i < list.size(); i++) {
			if (i != (list.size() - 1)) {
				finalText = finalText + list.get(i) + "\n";
			} else {
				finalText = finalText + list.get(i);
			}
		}
		textPaneDest.setText(finalText);
		try {
			highlight();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Markerar de nya orden så att man kan se var ändringar har skett.
	 * @throws BadLocationException
	 */
	public void highlight() throws BadLocationException {
		if (!textReplace.equals("")) {
			Document document = textPaneDest.getDocument();
			for (int index = 0; index + textReplace.length() < document
					.getLength(); index++) {
				String match = document.getText(index, textReplace.length());
				if (textReplace.equals(match)) {
					javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(
							Color.YELLOW);
					textPaneDest.getHighlighter().addHighlight(index,
							index + textReplace.length(), highlightPainter);
				}
			}
		}
	}

	/**
	 * Metod som nollställer alla pointers och texten som ska skrivas över till destination textpane.
	 */
	public void clearBuffer() {
		writePos = 0;
		readPos = 0;
		modifyPos = 0;
		finalText = "";
		textReplace = "";
		setAllEmpty();
	}
}
