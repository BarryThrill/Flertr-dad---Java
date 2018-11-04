package guis;

import jaco.mp3.player.MP3Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.sound.sampled.*;

/**
 * The GUI for assignment 1 by Barry Al-Jawari
 */
public class GUIFrame {


	private JFrame frame; 
	private JButton btnOpen; 
	private JButton btnPlay; 
	private JButton btnStop; 
	private JButton btnDisplay; 
	private JButton btnDStop; 
	private JButton btnTriangle;
	private JButton btnTStop; 
	private JLabel lblPlaying; 
	private JLabel lblPlayURL;
	private JPanel pnlMove; 
	private JPanel pnlRotate; 
	private JLabel movingDisplay;
	private JLabel movingImage;
	private MP3Player mp3_player;
	private boolean playing = false;
	private MoveDisplay moveDisplay;
	private MoveImage moveImage;

	
	public void Start() {
		frame = new JFrame();
		frame.setBounds(0, 0, 494, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Multiple Thread Demonstrator");
		InitializeGUI();
		frame.setVisible(true);
		frame.setResizable(false); 
		frame.setLocationRelativeTo(null); 
		moveDisplay = new MoveDisplay(this);
		moveImage = new MoveImage(this);
	}

	
	public void InitializeGUI() {
	
		JPanel pnlSound = new JPanel();
		Border b1 = BorderFactory.createTitledBorder("Music Player");
		pnlSound.setBorder(b1);
		pnlSound.setBounds(12, 12, 450, 100);
		pnlSound.setLayout(null);

		
		btnOpen = new JButton("Open");
		btnOpen.setBounds(6, 71, 75, 23);
		pnlSound.add(btnOpen);

		btnPlay = new JButton("Play");
		btnPlay.setBounds(88, 71, 75, 23);
		pnlSound.add(btnPlay);

		btnStop = new JButton("Stop");
		btnStop.setBounds(169, 71, 75, 23);
		pnlSound.add(btnStop);

		lblPlaying = new JLabel("Nothing playing right now.", JLabel.CENTER);
		lblPlaying.setFont(new Font("Serif", Font.BOLD, 20));
		lblPlaying.setBounds(20, 10, 400, 30);
		pnlSound.add(lblPlaying);

		lblPlayURL = new JLabel("ONLY .MP3 FILES");
		lblPlayURL.setBounds(10, 44, 450, 13);
		pnlSound.add(lblPlayURL);
		frame.add(pnlSound);

		JPanel pnlDisplay = new JPanel();
		Border b2 = BorderFactory.createTitledBorder("Display Thread");
		pnlDisplay.setBorder(b2);
		pnlDisplay.setBounds(12, 118, 222, 269);
		pnlDisplay.setLayout(null);

		btnDisplay = new JButton("Start Display");
		btnDisplay.setBounds(10, 226, 121, 23);
		pnlDisplay.add(btnDisplay);

		btnDStop = new JButton("Stop");
		btnDStop.setBounds(135, 226, 75, 23);
		pnlDisplay.add(btnDStop);

		pnlMove = new JPanel();
		pnlMove.setBounds(10, 19, 200, 200);
		Border b21 = BorderFactory.createLineBorder(Color.black);
		pnlMove.setBorder(b21);
		pnlDisplay.add(pnlMove);
		frame.add(pnlDisplay);

		JPanel pnlTriangle = new JPanel();
		Border b3 = BorderFactory.createTitledBorder("Image Thread");
		pnlTriangle.setBorder(b3);
		pnlTriangle.setBounds(240, 118, 222, 269);
		pnlTriangle.setLayout(null);

		btnTriangle = new JButton("Start Image");
		btnTriangle.setBounds(10, 226, 121, 23);
		pnlTriangle.add(btnTriangle);

		btnTStop = new JButton("Stop");
		btnTStop.setBounds(135, 226, 75, 23);
		pnlTriangle.add(btnTStop);

		pnlRotate = new JPanel();
		pnlRotate.setBounds(10, 19, 200, 200);
		Border b31 = BorderFactory.createLineBorder(Color.black);
		pnlRotate.setBorder(b31);
		pnlTriangle.add(pnlRotate);
		frame.add(pnlTriangle);
		
		btnPlay.setEnabled(false);
		btnStop.setEnabled(false);
		
		/**
		 * Knapp för musik-stop
		 */

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnStop) {
					mp3_player.stop();
					lblPlaying.setText("There's no song playing right now!");
					btnPlay.setEnabled(true);
					btnStop.setEnabled(false);
				
				}
			}
		});
		
		/**
		 * Knapp för musik-play
		 */

		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnPlay) {
						mp3_player.play();
						lblPlaying.setText("Music is playing!");
						btnPlay.setEnabled(false);
						btnStop.setEnabled(true);
						playing = true;
					
					}
				}
			
		});
		
		/**
		 * Knapp för musik-open
		 */

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnOpen) {
					try {
						if (playing) {
							mp3_player.stop();
						}
							lblPlaying.setText("There's no song playing right now!");
							lblPlayURL.setText("ONLY .MP3 FILES");
							chooseMusic();
						if(mp3_player != null) {
							btnPlay.setEnabled(true);
							btnStop.setEnabled(false);
						}

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		movingDisplay = new JLabel("Barry Al-Jawari");
		pnlMove.add(movingDisplay);
		btnDStop.setEnabled(false);

		/**
		 * Knapp för Display-Thread (Start Display)
		 */
		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisplay.setEnabled(false);
				btnDStop.setEnabled(true);
				startMoveDisplay();
			}
		});
		
		/**
		 * Knapp för Display-Thread (Stop Display)
		 */

		btnDStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisplay.setEnabled(true);
				btnDStop.setEnabled(false);
				stopMoveDisplay();
			}
		});

		btnTStop.setEnabled(false);
		movingImage = new JLabel(new ImageIcon(""));
		pnlRotate.add(movingImage);
		
		/**
		 * Knapp för Image-Thread (Start Image)
		 */

		btnTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnTriangle.setEnabled(false);
				btnTStop.setEnabled(true);
				startMoveImage();

			}
		});

		/**
		 * Knapp för Image-Thread (Stop Image)
		 */

		btnTStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnTriangle.setEnabled(true);
				btnTStop.setEnabled(false);
				stopMoveImage();
			}
		});
	}
	
	/**
	 * Konstruktor för Starta MoveDisplay thread.
	 */

	public void startMoveDisplay() {
		moveDisplay.startThread();
	}

	/**
	 * Konstruktor för Stoppa MoveDisplay thread.
	 */
	public void stopMoveDisplay() {
		moveDisplay.stopThread();
	}

	/**
	 * Konstruktor för rörelsen för MoveDisplay.
	 */	
	public void moveDisplay(int x, int y) {
		movingDisplay.setLocation(x, y);
	}
	
	/**
	 * Konstruktor för starta MoveImage thread.
	 */

	public void startMoveImage() {
		moveImage.startThread();
	}

	/**
	 * Konstruktor för Stoppa MoveImage thread.
	 */
	public void stopMoveImage() {
		moveImage.stopThread();
	}
	
	/**
	 * Konstruktor för rörelsen för MoveImage.
	 */

	public void moveImage(int x, int y) {
		movingImage.setLocation(x, y);
	}
	
	/**
	 * Konstruktor för MoveImage (Skala ner bilden till 45x45).
	 */

	public void setImage(BufferedImage image) {
		movingImage.setIcon(new ImageIcon(image.getScaledInstance(45, 45, 0)));
	}
	
	/**
	 * Metod för musik..
	 */

	public void chooseMusic() throws IOException {
		String userDir = System.getProperty("user.home");
		JFileChooser fileChooser = new JFileChooser(userDir + "/Desktop");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3",
				"mp3");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			System.out.println(selectedFile.getName());
			mp3_player = new MP3Player(selectedFile); //Tack vare Jako-JAR så kan man använda sig utav denna koden för att få MP3 ljud. 
			lblPlayURL.setText(selectedFile.getName());
		
		}
	}
}
