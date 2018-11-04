package p5;

import java.io.IOException;
import java.net.Socket;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * Barry Al-Jawari
 * @author Barry-PC
 *
 */
public class Client implements Runnable {
	private Socket socket;
	private JFrame frame;
	private JTextField txt;
	private JButton btnSend;
	private JTextArea lstMsg;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	/**
	 * Startar clienten.
	 */
	public void run() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Multi Chat Client");
		initializeGUI();
		frame.setVisible(true);
		frame.setResizable(false);
		connect();
		new ServerListener().run();
	}

	/**
	 * Initierar GUIet.
	 */
	private void initializeGUI() {
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
				sendMessage(txt.getText());
			}
		});
	}

	/**
	 * Ansultar till servern och skapar stream.
	 * 
	 */
	public void connect() {
		try {
			socket = new Socket("localhost", 50000);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		showMessage("Connected");
	}

	/**
	 * Skriver meddelande till server.
	 */
	public void sendMessage(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		showMessage(message);
	}

	/**
	 * Skriver ut meddelande på textfield.
	 */
	public void showMessage(String message) {
		lstMsg.append(message + "\n");
	}

	/**
	 * Clienten kommer att börja att lyssna efter meddelanden från servern.
	 *
	 */
	public class ServerListener implements Runnable {
		public void run() {
			while (true) {
				try {
					String message = (String) input.readObject();
					showMessage(message);
				} catch (IOException io) {

				} catch (ClassNotFoundException classex) {

				}
			}
		}
	}

	public static void main(String[] args) {
		Client cl = new Client();
		cl.run();
	}
}
