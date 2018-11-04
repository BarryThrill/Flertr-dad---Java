package p2;

public class CharacterBuffer {
	private char buffer = ' ';
	private boolean async = true;
	
	/**
	 * Den ger char en bokstav och v�cker tr�den som v�ntar p� bokstaven.
	 * Ifall bokstaven inte har h�mtats, s� v�ntar den tills den har h�mtats och d�
	 * v�cks denna tr�d som ger char en ny bokstav.
	 * Anv�nder en boolean f�r att veta ifall bokstaven har h�mtats eller inte.
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
	 * Den h�mtar en bokstav och v�cker tr�den som s�tter bokstaven.
	 * Ifall det inte finns en ny bokstav, s� v�ntar den tills det finns en ny bokstav
	 * att h�mta och d� v�cks denna tr�d som h�mtar den.
	 * Anv�nder en boolean f�r att veta ifall det finns bokstav att h�mta eller inte.
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
	 * S�tter en ny bokstav.
	 * @param obj
	 */
	
	public void putAsync(char obj) {
		buffer = obj;
	}

	/**
	 * H�mtar bokstaven.
	 * @return
	 */
	
	public char getAsync() {
		return buffer;
	}

	/**
	 * Nollst�ller allt
	 */
	
	public void clear() {
		buffer = ' ';
		async = true;
	}
}