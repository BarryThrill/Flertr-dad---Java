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
	 * H�r s� g�r writern in och kollar ifall status det �r tomt (empty), OM det �r det. is�fall skriver den in
	 * en str�ng p� den positionen och s�tter status till new. Annars s� kommer den v�nta.
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
	 * H�r s� g�r den in i Modifier och kollar ifall status �r ny (new),OM det �r det. is�fall g�r den in och
	 * kollar ifall n�t ord ska �ndras och s�tter status till checked. Annars s� kommer den v�nta.
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
	 * H�r s� g�r den in i readern och kollar ifall status �r markerat (checked), OM det �r det. is�fall g�r den in och
	 * l�ser str�ngen och s�tter status till empty, Annars s� kommer den v�nta.
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
	 * Metoden h�r s�tter alla positioner till empty status
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
	 * Markerar de nya orden s� att man kan se var �ndringar har skett.
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
	 * Metod som nollst�ller alla pointers och texten som ska skrivas �ver till destination textpane.
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
