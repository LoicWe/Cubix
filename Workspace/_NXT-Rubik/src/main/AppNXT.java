package main;

import objects.OrderManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * <b>AppNXT est la classe principale gerant la reception des ordres envoyes par le programme PC-Rubik</b>
 * <p>
 * Le programme se compose d'une boucle principale attendant une connection du PC via USB.
 * Les instructions reçues sont traitees dans la class OrderManager afin d'effectuer les differents ordres
 * </p>	s
 * 
 * @see OrderManager
 * 
 * @author	Loic.Wermeille
 * @version	1.0
 */
public class AppNXT {
	
	/**
	 * Methode principale d'entree du programme qui gere la reception des ordres
	 * @param args Valeurs d'entree du programme
	 */
	public static void main(String[] args) {
		Sound.beepSequenceUp();
		// Definitions des status
		String connected = "Connected";
	    String waiting = "Waiting...";
	    String closing = "Closing...";
	       
	    OrderManager manager = new OrderManager();
	    manager.phaseTestCommunication = false;
		while (true)
		{
			LCD.drawString(waiting,0,0);
			LCD.refresh();
			
			boolean end = false;
	        //BTConnection btc = Bluetooth.waitForConnection();
			USBConnection btc = USB.waitForConnection();
	        
			LCD.clear();
			LCD.drawString(connected,0,0);
			LCD.refresh();	
	
			DataInputStream dis = btc.openDataInputStream();
			DataOutputStream dos = btc.openDataOutputStream();
			
			while(end == false) {
				int order = 0;
				try {
					order = dis.readInt();
					
					if(order>900&&order<1000)
						waiting = "Waiting initialised ...";
					
					//Execution de l'ordre
					end = manager.manageOrder(order);
					
					LCD.drawInt(order,7,0,1);
					LCD.refresh();
					
					dos.writeInt(-order);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				dis.close();
				dos.close();
				Thread.sleep(100); // wait for data to drain
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			LCD.clear();
			LCD.drawString(closing,0,0);
			LCD.refresh();
			btc.close();
			LCD.clear();
			manager.phaseTestCommunication = false;
		}
	}
}