package p4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The GUI for assignment 4
 */
public class GUIMonitor {
	/**
	 * These are the components you need to handle. You have to add listeners
	 * and/or code
	 */
	private JFrame frame; // The Main window
	private JMenu fileMenu; // The menu
	private JMenuItem openItem; // File - open
	private JMenuItem saveItem; // File - save as
	private JMenuItem exitItem; // File - exit
	private JTextField txtFind; // Input string to find
	private JTextField txtReplace; // Input string to replace
	private JCheckBox chkNotify; // User notification choise
	private JLabel lblInfo; // Hidden after file selected
	private JButton btnCreate; // Start copying
	private JButton btnClear; // Removes dest. file and removes marks
	private JLabel lblChanges; // Label telling number of replacements
	private JTextPane txtPaneSource, txtPaneDest;
	private BoundedBuffer buffer;
	private String text = "";
	private Writer writer;
	private Reader reader;
	private Modifier modifier;
	private File selectedFile;
	private LinkedList<String> list = new LinkedList<String>();
	private int count = 0;

	/**
	 * Constructor
	 */
	public GUIMonitor() {
	}

	/**
	 * Starts the application
	 */
	public void Start() {
		frame = new JFrame();
		frame.setBounds(0, 0, 714, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Text File Copier - with Find and Replace");
		InitializeGUI(); // Fill in components
		frame.setVisible(true);
		frame.setResizable(false); // Prevent user from change size
		frame.setLocationRelativeTo(null); // Start middle screen
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI() {
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open Source File");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		saveItem = new JMenuItem("Save Destination File As");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		saveItem.setEnabled(false);
		exitItem = new JMenuItem("Exit");
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		bar.add(fileMenu);

		JPanel pnlFind = new JPanel();
		pnlFind.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Find and Replace"));
		pnlFind.setBounds(12, 32, 436, 122);
		pnlFind.setLayout(null);
		frame.add(pnlFind);
		JLabel lab1 = new JLabel("Find:");
		lab1.setBounds(7, 30, 80, 13);
		pnlFind.add(lab1);
		JLabel lab2 = new JLabel("Replace with:");
		lab2.setBounds(7, 63, 80, 13);
		pnlFind.add(lab2);

		txtFind = new JTextField();
		txtFind.setBounds(88, 23, 327, 20);
		pnlFind.add(txtFind);
		txtReplace = new JTextField();
		txtReplace.setBounds(88, 60, 327, 20);
		pnlFind.add(txtReplace);
		chkNotify = new JCheckBox("Notify user on every match");
		chkNotify.setBounds(88, 87, 180, 17);
		pnlFind.add(chkNotify);

		lblInfo = new JLabel("Select Source File..");
		lblInfo.setBounds(485, 42, 120, 13);
		frame.add(lblInfo);

		btnCreate = new JButton("Copy to Destination");
		btnCreate.setBounds(465, 119, 230, 23);
		frame.add(btnCreate);
		btnCreate.setEnabled(false);
		btnClear = new JButton("Clear dest. and remove marks");
		btnClear.setBounds(465, 151, 230, 23);
		frame.add(btnClear);
		btnClear.setEnabled(false);

		lblChanges = new JLabel("No. of Replacements:");
		lblChanges.setBounds(279, 161, 200, 13);
		frame.add(lblChanges);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 170, 653, 359);
		frame.add(tabbedPane);
		txtPaneSource = new JTextPane();
		JScrollPane scrollSource = new JScrollPane(txtPaneSource);
		tabbedPane.addTab("Source", null, scrollSource, null);
		txtPaneDest = new JTextPane();
		JScrollPane scrollDest = new JScrollPane(txtPaneDest);
		tabbedPane.addTab("Destination", null, scrollDest, null);

		btnClear.setEnabled(false);

		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					chooseTextFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				saveItem.setEnabled(true);
				btnCreate.setEnabled(true);
			}
		});

		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writer = new Writer(buffer, list);
				modifier = new Modifier(buffer, count , txtFind.getText(), txtReplace
						.getText());
				reader = new Reader(buffer, count);
				btnClear.setEnabled(true);
				btnCreate.setEnabled(false);
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDestinationText("");
				buffer.clearBuffer();
				btnCreate.setEnabled(true);
				btnClear.setEnabled(false);
				count = 0;
			}
		});
	}

	/**
	 * Metod som låter användaren att kunna spara den slutgiltiga texten.
	 */
	public void save() {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
		int actionDialog = chooser.showSaveDialog(chooser);
		if (actionDialog == JFileChooser.APPROVE_OPTION) {
			File fileName = new File(chooser.getSelectedFile() + "");
			if (fileName == null)
				return;
			if (fileName.exists()) {
				actionDialog = JOptionPane.showConfirmDialog(chooser,
						"Replace existing file?");
				if (actionDialog == JOptionPane.NO_OPTION)
					return;
			}
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fileName
						+ ".txt"));
				out.write(txtPaneDest.getText());
				out.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Metod som låter användaren att välja en text som sedan skriver ut i source.
	 * @throws IOException
	 */
	public void chooseTextFile() throws IOException {
		text = "";
		int i = 0;
		String userDir = System.getProperty("user.home");
		JFileChooser fileChooser = new JFileChooser(userDir + "/Desktop");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT",
				"txt");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			lblInfo.setText(selectedFile.getName());
			String line;
			BufferedReader in = new BufferedReader(new FileReader(selectedFile));
			while ((line = in.readLine()) != null) {
				if (i == 0) {
					text += line;
					i++;
				} else {
					text += "\n" + line;
				}
				list.add(line);
				count++;
			}
			in.close();
		}
		setSourceText(text);
		buffer = new BoundedBuffer(txtPaneDest);
	}

	/**
	 * Skriver texten i source textpane.
	 * @param str
	 */
	public void setSourceText(String str) {
		txtPaneSource.setText(str);
	}

	/**
	 * Skriver texten i destination textpane.
	 * @param str
	 */
	public void setDestinationText(String str) {
		txtPaneDest.setText(str);
	}

}