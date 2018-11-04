package p4;

import java.util.LinkedList;

/**
 * 
 * @author Barry Al-jawari
 *
 */
public class Writer implements Runnable {
	private BoundedBuffer buffer;
	private Thread thread;
	private LinkedList<String> list = new LinkedList<String>();

	public Writer(BoundedBuffer buffer, LinkedList<String> list) {
		this.buffer = buffer;
		this.list = list;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Om det går att skriva ut en rad så skriv det till bufferten. 
	 */
	public void run() {
		while (!list.isEmpty()) {
			buffer.write(list.removeFirst());
		}
	}
}
