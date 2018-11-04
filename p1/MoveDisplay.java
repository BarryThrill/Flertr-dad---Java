package guis;

import java.util.Random;

public class MoveDisplay implements Runnable {
	private GUIFrame gui;
	private boolean moving = false;
	private Thread thread;

	/**
	 * Konstruktor f�r f� information fr�n GUIFrame.
	 */
	public MoveDisplay(GUIFrame gui) {
		this.gui = gui;
	}
	/**
	 * Metoden f�r MoveDisplay
	 */
	public void run() {
		Random rand = new Random();
		while (moving) { //G�ra s� att x och Y axel f�r random v�rde som g�r att det g�r randomly. 
			int x = rand.nextInt(150) + 1;
			int y = rand.nextInt(150) + 1;
			gui.moveDisplay(x, y);
			try {
				Thread.sleep(1000); //Att varje sekund kommer bilden att f� nya x,y v�rden vilket g�r det r�r sig random. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Starta thread
	 */
	public void startThread() {
		moving = true;
		thread = new Thread(this);
		thread.start(); //Starta thread
		System.out.println("Display Thread start");
	}
	/**
	 * Stoppa thread
	 */
	public void stopThread() {
		moving = false;
		thread = null; //Stoppa thread
		System.out.println("Display Thread stopp");
	}
}
