package objects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import exceptions.RobotException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

/**
 * <b>Class gérant la communication avec 1 brique NXT</b> <br>
 * Details du fonctionnement de la connexion<br>
 * PC NXT Legende<br>
 * |------------------&gt;| 1. Connection USB<br>
 * | |<br>
 * |&lt;------------------| 2. Validation connexion<br>
 * |------------------&gt;| 3. Envoie premier ordre<br>
 * |&lt;------------------| 4. Confirmation nb<br>
 * | |<br>
 * |------------------&gt;| 5. Envoie order 1<br>
 * |&lt;------------------| 6. Confirmation ordres 1 après fin de celui-ci<br>
 * |&lt;----------------&gt;| 7. Envoie des autres ordres et confirmation
 * ATTENTION, DERNIER ORDRE NEGATIF POUR SIGNALER FIN<br>
 * | |<br>
 * |---------X---------| 8. Fermetures des connexions<br>
 * 
 * @author Loïc Wermeille
 * @version 1.0
 */
public class NXT {

	// Definition des parametres de base
	NXTConnector conn = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	String name = "";
	NXTInfo mySelf = null;
	boolean inited = false;

	/**
	 * Fonction initialisant le connexion vers un nxt défini et testant la connexion
	 * 
	 * @param nxtInfo
	 *            Identifiant du nxt
	 * @throws RobotException
	 *             Exception spécifique au robot
	 */
	public void init(NXTInfo nxtInfo) throws RobotException {
		name = nxtInfo.name;
		mySelf = nxtInfo;
		if (nxtInfo != null)
			inited = true;
		test();
	}

	/**
	 * Fonction établisant la connexion entre le pc et le nxt
	 * 
	 * @throws RobotException
	 *             Exception spécifique au robot
	 */
	private void connect() throws RobotException {
		if (!inited) {
			throw new RobotException(0, "NXT non initialise");
		}

		conn = new NXTConnector();

		// Ajout du listener pour pour la console
		conn.addLogListener(new NXTCommLogListener() {
			public void logEvent(String message) {
				System.out.println("USBSend Log.listener: " + message);
			}

			public void logEvent(Throwable throwable) {
				System.out.println("USBSend Log.listener - stack trace: ");
				throwable.printStackTrace();
			}
		});

		// Connection et vérification de celle-ci
		if (!conn.connectTo(mySelf, NXTComm.PACKET)) {
			System.err.println("No NXT found using USB");
			throw new RobotException(10, "Fail to connect to any NXT");
		}
		System.out.println(name + " connected");

		// Configuration des données
		dis = new DataInputStream(conn.getInputStream());
		dos = new DataOutputStream(conn.getOutputStream());
	}

	/**
	 * Méthode d'envoie d'instructions au NXT
	 * 
	 * @param instructions
	 *            Liste des instructions a envoyer
	 * @throws RobotException
	 *             Exception spécifique au robot
	 */
	public void send(List<Integer> instructions) throws RobotException {
		connect();

		int t = instructions.size();

		// Modification de la derniere instruction en négatif pour signifier la fin de
		// la communication
		instructions.set(t - 1, -instructions.get(t - 1));

		// Bouclage sur la liste des instructions et envoie
		for (int i = 0; i <= t - 1; i++) {
			try {
				System.out.println("Sending " + instructions.get(i));
				dos.writeInt(instructions.get(i));
				dos.flush();

			} catch (IOException ioe) {
				throw new RobotException(2, "IO Exception writing bytes:" + ioe.getMessage());
			}

			try {
				System.out.println("Received " + dis.readInt());
			} catch (IOException ioe) {
				throw new RobotException(3, "IO Exception reading bytes:" + ioe.getMessage());
			}
		}

		close();
	}

	/**
	 * Test de l'envoie et de la reception entre le pc et le nxt
	 * 
	 * @throws RobotException
	 *             Exception spécifique au robot
	 */
	public void test() throws RobotException {
		connect();

		// Envoie de 100 chiffres bidon en commençant par 9 pour signifier le test de
		// communication
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("Sending " + (i * 30000));
				dos.writeInt((i * 30000));
				dos.flush();

			} catch (IOException ioe) {
				throw new RobotException(2, "IO Exception writing bytes:" + ioe.getMessage());
			}

			try {
				System.out.println("Received " + dis.readInt());
			} catch (IOException ioe) {
				throw new RobotException(3, "IO Exception reading bytes:" + ioe.getMessage());
			}
		}

		// Fermeture de la connexion du côte Robot en envoyant un nombre negatif
		try {
			System.out.println("Sending " + 10 * 30000);
			dos.writeInt(-(10 * 30000));
			dos.flush();

		} catch (IOException ioe) {
			throw new RobotException(2, "IO Exception writing bytes:" + ioe.getMessage());
		}

		try {
			System.out.println("Received " + dis.readInt());
		} catch (IOException ioe) {
			throw new RobotException(3, "IO Exception reading bytes:" + ioe.getMessage());
		}

		// Fermeture de la connexion
		close();
	}

	/**
	 * Fermeture de la connexion
	 * 
	 * @throws RobotException
	 *             Exception de type robot
	 */
	private void close() throws RobotException {
		try {
			dis.close();
			dos.close();
			conn.close();
			conn = null;
		} catch (IOException ioe) {
			throw new RobotException(4, "IOException closing connection:" + ioe.getMessage());
		}
	}
}
