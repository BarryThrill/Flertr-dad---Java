package guis;

import java.util.Random;

public class MoveDisplay implements Runnable {
	private GUIFrame gui;
	private boolean moving = false;
	private Thread thread;

	/**
	 * Konstruktor för få information från GUIFrame.
	 */
	public MoveDisplay(GUIFrame gui) {
		this.gui = gui;
	}
	/**
	 * Metoden för MoveDisplay
	 */
	public void run() {
		Random rand = new Random();
		while (moving) { //Göra så att x och Y axel får random värde som gör att det går randomly. 
			int x = rand.nextInt(150) + 1;
			int y = rand.nextInt(150) + 1;
			gui.moveDisplay(x, y);
			try {
				Thread.sleep(1000); //Att varje sekund kommer bilden att få nya x,y värden vilket gör det rör sig random. 
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
