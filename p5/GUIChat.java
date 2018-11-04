package p5;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

/**
 * The GUI for assignment 5
 */
public class GUIChat {
	/**
	 * These are the components you need to handle. You have to add listeners
	 * and/or code
	 */
	private JFrame frame; // The Main window
	private JTextField txt; // Input for text to send
	private JButton btnSend; // Send text in txt
	private JTextArea lstMsg; // The logger listbox
	private Server server;

	/**
	 * Constructor
	 */
	public GUIChat() {

	}

	/**
	 * Starts the application
	 * 
	 * @throws IOException
	 */
	public void Start() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Multi Chat Server"); // Change to "Multi Chat Server" on
												// server part and vice versa
		InitializeGUI(); // Fill in components
		frame.setVisible(true);
		frame.setResizable(false); // Prevent user from change size
		startConnection();
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI() {
		txt = new JTextField();
		txt.setBounds(13, 13, 177, 23);
		frame.add(txt);
		btnSend = new JButton("Send");
		btnSend.setBounds(197, 13, 75, 23);
		frame.add(btnSend);
		lstMsg = new JTextArea();
		lstMsg.setEditable(false);
		JScrollPane pane = new JScrollPane(lstMsg);
		pane.setBounds(12, 51, 260, 199);
		pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.add(pane);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.sendMessage(txt.getText());
			}
		});
	}

	/**
	 * Startar servern.
	 * @throws IOException
	 */
	public void startConnection() throws IOException {
		server = new Server(this,50000);
		server.start();
	}

	/**
	 * Skriver ut meddelandet på textfield.
	 * @param message
	 */
	public void showMessage(String message) {
		lstMsg.append(message + "\n");
	}
}
