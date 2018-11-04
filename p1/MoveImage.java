package guis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * The assignment 1 by Barry Al-Jawari
 */
public class MoveImage implements Runnable {
	private GUIFrame gui;
	private boolean image = true;
	private boolean imageMoving = false;
	private Thread thread;
	
	/**
	 * Konstruktor f�r f� information fr�n GUIFrame.
	 */
	public MoveImage(GUIFrame gui) {
		this.gui = gui;
	}
	
	/**
	 * Metod f�r MoveImage (Vart man kan v�lja bild) 
	 */
	public void run() {
		if (!imageMoving) {
			String userDir = System.getProperty("user.home");
			JFileChooser fileChooser = new JFileChooser(userDir + "/Desktop");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"JPG & PNG Images", "jpg", "png");
			fileChooser.setFileFilter(filter);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile().getAbsoluteFile();
				try {
					BufferedImage image = ImageIO.read(selectedFile);
					gui.setImage(image);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			imageMoving = true;
		}
		Random rand = new Random();
		while (image) { // Att g�ra s� bilden f�r random v�rde f�r x,y som g�r att bilden r�r sig randomly. 
			int x = rand.nextInt(100) + 1;
			int y = rand.nextInt(100) + 1;
			gui.moveImage(x, y);
			try {
				Thread.sleep(1000); //Kommer att flytta p� sig randomly vart sekund. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Starta thread
	 */
	public void startThread() {
		image = true;
		thread = new Thread(this);
		thread.start();  //Starta thread
		System.out.println("Image Thread start");
	}
	/**
	 * Stoppa thread
	 */
	public void stopThread() {
		image = false;
		thread = null; //Stoppa thread
		System.out.println("Image Thread stopp");
	}
}