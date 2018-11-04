package p5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Barry Al-Jawari
 * @author Barry-PC
 *
 */

public class Task implements Runnable {
	private GUIChat gui;
	private Socket socket;
	private boolean running = true;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	/**
	 * N�r det skapas en ny task, skapas det �ven stream f�r att lyssna efter meddelanden
	 * fr�n klient och skriva meddelande till dem.
	 */
	public Task(Socket socket, GUIChat gui) {
		this.socket = socket;
		this.gui = gui;
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			gui.showMessage("A new user just connected.");
		} catch (IOException e) {
			gui.showMessage(e.toString());
			return;
		}
	}

	/**
	 * Kommer att vara en o�ndlig loop som lyssnar efter meddelande ifr�n klient.
	 */
	public void run() {
		String userMessage = "";
		while (running) {
			try {
				userMessage = (String) input.readObject();
			} catch (IOException e) {
				System.out.println(e);
				break;
			} catch (ClassNotFoundException e2) {
				System.out.println(e2);
				break;
			}
			gui.showMessage(userMessage);
		}
	}

	/**
	 * En Metod f�r att skicka meddelande till client.
	 * @param message
	 */
	public void sendMessage(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}