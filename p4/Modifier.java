package p4;

/**
 * 
 * @author Barry Al-Jawari
 *
 */

public class Modifier implements Runnable {
	private BoundedBuffer buffer;
	private Thread thread;
	private String textFind, textReplace;
	private int size;

	public Modifier(BoundedBuffer buffer, int size, String textFind, String textReplace) {
		this.buffer = buffer;
		this.size = size;
		this.textFind = textFind;
		this.textReplace = textReplace;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * När Modifier startas(tråden). så skickar den in 2 argumenter. Ordet som ska ändras och det nya ordet. 
	 */
	public void run() {
		while (size != 0) {
			try {
				buffer.modify(textFind, textReplace);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			size--;
		}
	}
}
