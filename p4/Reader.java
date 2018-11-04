package p4;

/**
 * 
 * @author Barry Al-Jawari
 *
 */
public class Reader implements Runnable {
	private BoundedBuffer buffer;
	private Thread thread;
	private int size;
	
	public Reader(BoundedBuffer buffer,int size) {
		this.buffer = buffer;
		this.size = size;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * När reader tråden startas, anropar den read metoden i BoundedBuffer klassen.
	 */
	public void run() {
		while (size != 0) {
			try {
				buffer.read();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			size--;
		}
		buffer.setText();
	}
}