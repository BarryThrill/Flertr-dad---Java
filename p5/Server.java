package p5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Barry Al-Jawari
 * @author Barry-PC
 *
 */

public class Server {
	private GUIChat gui = new GUIChat();
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private boolean running = true;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Task task;
	private ArrayList<Task> list = new ArrayList<Task>();
	
	Server(GUIChat gui, int port) {
		this.gui = gui;
		this.port = port;
	}

	/**
	 * Servern kommer att startas och v�nta p� att clienten skall ansluta.
	 * N�r det har anslutas s� skaps ny anslutning p� en ny task f�r varje klient som l�ggas in
	 * i tr�dpoolen med 5 tr�dar och kommer att k�ra. Varje klient l�ggs ocks� till i en lista.
	 */
	public void start() throws IOException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(5);
		serverSocket = new ServerSocket(port);
		while (running) {
			socket = serverSocket.accept();
			task = new Task(socket, gui);
			list.add(task);
			executor.execute(task);
		}
		if (!running) {
			serverSocket.close();
		}
	}

	/**
	 * Kommer att loopa igenom listan f�r att kunna skicka meddelande till
	 * varje klient som �r uppkopplad.
	 */
	public void sendMessage(String message) {
		gui.showMessage(message);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).sendMessage(message);
		}
	}
}
