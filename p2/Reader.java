package p2;

import java.util.Random;

public class Reader implements Runnable {
	private GUIMutex guiMutex;
	private CharacterBuffer buffer;
	private boolean synced = false, running = true;
	private Thread thread;
	private Random rand = new Random();
	private int i = 0, size = 0;

	public Reader(GUIMutex guiMutex, CharacterBuffer buffer) {
		this.guiMutex = guiMutex;
		this.buffer = buffer;
	}

	/**
	 * Först kollar den ifall man valt synkad eller osynkad.
	 * Vid synkad läser den en bokstav i taget tills hela texten är läst.
	 * Vid osynkad läser den hela tiden tills hela texten har skrivits.
	 * Sover slumpmässig tid.
	 */
	
	public void run() {
		i = 0;
		while (running) {
			if (synced) {
				guiMutex.setReaderLog(buffer.getSync());
				i++;
				if(i == size) {
					guiMutex.compareTexts();
					guiMutex.setTexts();
				}
			} else if (!synced) {
				guiMutex.setReaderLog(buffer.getAsync());
			}
			try {
				thread.sleep(rand.nextInt(1500) + 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Startar reader.
	 * @param size
	 * @param synced
	 */
	
	public void startReading(int size, boolean synced) {
		thread = new Thread(this);
		this.synced = synced;
		this.size = size;
		running = true;
		thread.start();
	}


	/**
	 * Stoppar reader.
	 */
	
	public void stopReading() {
		running = false;
	}
}
