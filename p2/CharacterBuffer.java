package p2;

public class CharacterBuffer {
	private char buffer = ' ';
	private boolean async = true;
	
	/**
	 * Den ger char en bokstav och väcker tråden som väntar på bokstaven.
	 * Ifall bokstaven inte har hämtats, så väntar den tills den har hämtats och då
	 * väcks denna tråd som ger char en ny bokstav.
	 * Använder en boolean för att veta ifall bokstaven har hämtats eller inte.
	 * @param obj
	 */
	
	public synchronized void putSync(char obj) {
		if (!async) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (async) {
			buffer = obj;
			async = false;
			notify();
		}

	}
	
	/**
	 * Den hämtar en bokstav och väcker tråden som sätter bokstaven.
	 * Ifall det inte finns en ny bokstav, så väntar den tills det finns en ny bokstav
	 * att hämta och då väcks denna tråd som hämtar den.
	 * Använder en boolean för att veta ifall det finns bokstav att hämta eller inte.
	 * @return
	 */
	
	public synchronized char getSync() {
		if(async) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		char current = ' ';
		if (!async) {
			current = buffer;
			async = true;
			notify();
		}
		return current;
	}

	/**
	 * Sätter en ny bokstav.
	 * @param obj
	 */
	
	public void putAsync(char obj) {
		buffer = obj;
	}

	/**
	 * Hämtar bokstaven.
	 * @return
	 */
	
	public char getAsync() {
		return buffer;
	}

	/**
	 * Nollställer allt
	 */
	
	public void clear() {
		buffer = ' ';
		async = true;
	}
}