
package p5;

import java.io.IOException;

/**
 * Barry Al-Jawari
 * @author Barry-PC
 *
 */

public class Main 
{
	public static void main(String[] args) throws IOException
	{
		//GUIFrame test = new GUIFrame();
		//GUIMutex test = new GUIMutex();
		//GUISemaphore test = new GUISemaphore();
		//GUIMonitor test = new GUIMonitor();
		GUIChat test = new GUIChat();
		test.Start();
	}

}
