package p3;

public class Truck implements Runnable {
	private Storage storage;
	private boolean running = false;
	private Thread thread;
	private int itemLimit = 12;
	private double weightLimit = 12.50, volumeLimit = 22.50;
	private String items = "";
	private GUISemaphore gui;
	private FoodItem food = new FoodItem(volumeLimit, volumeLimit, items);
	
	public Truck(GUISemaphore gui, Storage storage) {
		this.gui = gui;
		this.storage = storage;
	}

	/**
	 * Returnerar max volym.
	 * @return
	 */
	public double getVolumeLimit() {
		return volumeLimit;
	}

	/**
	 * Returnerar max vikt.
	 * @return
	 */
	public double getWeightLimit() {
		return weightLimit;
	}

	/**
	 * Returnerar max varor.
	 * @return
	 */
	
	public int getItemLimit() {
		return itemLimit;
	}

	/**
	 * Metoden h�mtar ett FoodItem fr�n storage och ifall varan
	 * inte �verstiger weightLimit eller volumeLimit och inte n�r
	 * max antal varor, l�ggs den in i truck och uppdaterar
	 * progressbar.
	 * Ifall den �verstiger eller n�t max antal varor, anropas
	 * metoden leave.
	 * Processen loopas.
	 */
	public void run() {
		while (running) {
			if (storage.getStorageCounter() != 0) {
				try {
					food = storage.getItem();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (itemLimit == 0) {
					gui.setTruckStatus("Truck limited by items");
					gui.fullTruck("Truck delivering food");
					leave();
				}
				else if ((weightLimit - food.getWeight()) < 0) {
					gui.setTruckStatus("Truck limited by weight");
					gui.fullTruck("Truck delivering food");
					leave();
				}

				else if ((volumeLimit - food.getVolume()) < 0) {
					gui.setTruckStatus("Truck limited by volume");
					gui.fullTruck("Truck delivering food");
					leave();
				} else {
					setCargo(food.getName());
					weightLimit = weightLimit - food.getWeight();
					volumeLimit = volumeLimit - food.getVolume();
					itemLimit--;
					storage.decrementCounter();
					gui.cargoList();
				}
				try {
					thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gui.setProgressbar(storage.getStorageCounter());
				gui.setTruckStatus("New Truck: Loading");
			} else {
				gui.setTruckStatus("New Truck: Waiting");
			}
		}
	}

	/**
	 * Tr�den sover n�r den truck �r full, och s�tter
	 * ordinare values p� items,volume och weight.
	 */
	public void leave() {
		try {
			thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemLimit = 10;
		weightLimit = 12.50;
		volumeLimit = 22.50;
		gui.fullTruck("");
		items = "";
	}

	/**
	 * Sparar de h�mtade varorna.
	 * @param item
	 */
	public void setCargo(String item) {
		items += item + "\n";
	}

	/**
	 * Returnerar de h�mtade varorna.
	 */
	public String getCargo() {
		return items;
	}

	/**
	 * S�tter ig�ng tr�den.
	 */
	public void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}

	/**
	 * Stoppar tr�den.
	 */
	public void stop() {
		running = false;
	}
}
