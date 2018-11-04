package p3;

import java.util.Random;

public class Factory implements Runnable {
	private FoodItem[] foodBuffer = new FoodItem[20];
	private Storage storage;
	private GUISemaphore gui;
	private boolean running = false;
	private String whatFactory = "";
	private Thread thread;
	
	/**
	 * Konstruktur för Factory med värderna gui och storage
	 * @param gui
	 * @param storage
	 */
	public Factory(GUISemaphore gui, Storage storage) {
		this.gui = gui;
		this.storage = storage;
	}
	
	/**
	 * Värderna för Fooditem där vi anger weight, volume och name
	 */
	public void InitFoodItems() {
		foodBuffer[0] = new FoodItem(1.1, 0.5, "Milk");
		foodBuffer[1] = new FoodItem(0.6, 0.4, "Cream");
		foodBuffer[2] = new FoodItem(1.1, 0.5, "Yoghurt");
		foodBuffer[3] = new FoodItem(2.34, 0.66, "Butter");
		foodBuffer[4] = new FoodItem(3.4, 1.2, "Flower");
		foodBuffer[5] = new FoodItem(1.55, 0.27, "Salt");
		foodBuffer[6] = new FoodItem(0.6, 0.19, "Almonds");
		foodBuffer[7] = new FoodItem(1.98, 0.75, "Bread");
		foodBuffer[8] = new FoodItem(1.4, 0.5, "Donuts");
		foodBuffer[9] = new FoodItem(1.3, 1.5, "Jam");
		foodBuffer[10] = new FoodItem(4.1, 2.5, "Ham");
		foodBuffer[11] = new FoodItem(3.5, 3.9, "Chicken");
		foodBuffer[12] = new FoodItem(0.87, 0.55, "Salat");
		foodBuffer[13] = new FoodItem(2.46, 0.29, "Orange");
		foodBuffer[14] = new FoodItem(2.44, 0.4, "Apple");
		foodBuffer[15] = new FoodItem(1.3, 0.77, "Pear");
		foodBuffer[16] = new FoodItem(2.98, 2.0, "Soda");
		foodBuffer[17] = new FoodItem(3.74, 1.5, "Beer");
		foodBuffer[18] = new FoodItem(2.0, 1.38, "HotDogs");
		foodBuffer[19] = new FoodItem(3.7, 1.8, "Sugar");
	}

	/**
	 * Metoden har en random funktion där den väljer i från foodBuffer och sedan kollar den om storage är ej full så kollar den om A
	 * fungerar så fortsätter den tills den är full. Samma sak med B. (Dessutom lägger den in till storage med storage.add) 
	 * 
	 */
	public void run() {
		Random rand = new Random();
		while (running) {
			int number = rand.nextInt(20);
			try {
				if (!storage.isFull()) {
					if (whatFactory == "A") {
						gui.producerAStatus("Working");
					} else {
						gui.producerBStatus("Working");
					}
					storage.add(foodBuffer[number]);
				} else {
					if (whatFactory == "A") {
						gui.producerAStatus("Waiting");
					} else {
						gui.producerBStatus("Waiting");
					}
				}
				gui.setProgressbar(storage.getStorageCounter());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Startar tråden
	 * @param str
	 */

	public void start(String str) {
		InitFoodItems();
		this.whatFactory = str;
		thread = new Thread(this);
		running = true;
		thread.start();
	}	
	
	/**
	 * Stoppa tråden
	 */

	public void stop() {
		running = false;
	}
}