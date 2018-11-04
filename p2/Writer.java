package p2;

import java.util.Random;

public class Writer implements Runnable {
	private GUIMutex guiMutex;
	private CharacterBuffer buffer;
	private boolean synced = false, running = false;
	private Thread thread;
	private String write;
	private Random rand = new Random();

	public Writer(GUIMutex guiMutex, CharacterBuffer buffer) {
		this.guiMutex = guiMutex;
		this.buffer = buffer;
	}

	/**
	 *  Först kollar den ifall man valt synkad eller osynkad.
	 *  Vid synkad skriver den en bokstav i taget och fortsätter
	 *  tills den har skrivit hela texten till bufferten och då avslutar den.
	 *  Vid osynkad skriver den nya bokstäver till bufferten hela tiden
	 *  tills den har skrivit texten och då jämför den och skriver ut
	 *  texterna innan den avslutar.
	 */
	public void run() {
		while (running) {
			for (int i = 0; i <= write.length(); i++) {
				if (synced) {
					if (i == write.length()) {
						running = false;
					} else {
						buffer.putSync(write.charAt(i));
						guiMutex.setWriterLog(write.charAt(i));
					}
				} else if (!synced) {
					if (i == write.length()) {
						guiMutex.compareTexts();
						guiMutex.setTexts();
						running = false;
					} else {
						buffer.putAsync(write.charAt(i));
						guiMutex.setWriterLog(write.charAt(i));
					}
				}

				try {
					Thread.sleep(rand.nextInt(1500) + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Startar writer.
	 * @param write
	 * @param synced
	 */
	public void startWriting(String write, boolean synced) {
		thread = new Thread(this);
		this.write = write;
		this.synced = synced;
		running = true;
		thread.start();
	}

	/**
	 * Stoppar writer.
	 */
	public void stopWriting() {
		running = false;
	}
}