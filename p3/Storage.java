package p3;

import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Storage {
	private LinkedList<FoodItem> buffer = new LinkedList<FoodItem>();
	private boolean isFull = false;
	private int storageLimit = 50, storageCounter = 0;
	private Semaphore writerSema = new Semaphore(50);
	private Semaphore readerSema = new Semaphore(0);
	private Lock lock = new ReentrantLock();

	
	/**
	 * Minskar writerSemaphore med 1 och l�gger in en vara.
	 * �kar readerSemaphore med 1.
	 * @param food
	 * @throws InterruptedException
	 */
	public void add(FoodItem food) throws InterruptedException {
		writerSema.acquire();
		lock.lock();
		buffer.add(food);
		incrementCounter();
		lock.unlock();
		readerSema.release();
	}

	/**
	 * Minskar readerSemaphore med 1 och h�mtar ut en vara.
	 * �kar writerSemaphore med 1.
	 * @return
	 * @throws InterruptedException
	 */
	public FoodItem getItem() throws InterruptedException {
		readerSema.acquire();
		lock.lock();
		FoodItem fi = buffer.remove();
		lock.unlock();
		writerSema.release();
		return fi;
	}

	/**
	 * �kar storage med 1.
	 */
	public void incrementCounter() {
		storageCounter++;
	}

	/**
	 * Minskar storage med 1.
	 */
	public void decrementCounter() {
		storageCounter--;
	}

	/**
	 * Returnerar antal items.
	 * @return
	 */
	public int getStorageCounter() {
		return storageCounter;
	}

	/**
	 * Kollar ifall storage �r full
	 * @return
	 */
	public boolean isFull() {
		if (storageCounter == storageLimit) {
			isFull = true;
		} else {
			isFull = false;
		}
		return isFull;
	}
}