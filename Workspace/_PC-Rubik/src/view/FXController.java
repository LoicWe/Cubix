package view;

import org.kociemba.twophase.Search;
import org.kociemba.twophase.Tools;

import exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import objects.ColorCube;
import objects.Robot;
import objects.Rubik;
import objects.Scanner;

/**
 * <b>FXController est la classe gérant les entrées et sorties de la fenêtre principale</b>
 * <p>
 * La classe permet l'accès aux différentes fonctionnalitées principales de l'aplication
 * tel que la résolution complète du rubik's cube
 * </p>
 * 
 * @author	Loïc.Wermeille
 * @since	17.10.2016
 * @version 1.0
 */
public class FXController {
	// the FXML TextFlow
	@FXML
	private TextArea txa_log;
	
	// the buttons
	@FXML
	private Button btn_start;
	@FXML
	private Button btn_scan;
	@FXML
	private Button btn_conRobot;
	
	// the FXML image view
	@FXML
	private ImageView currentFrame;
	
	private Scanner scannerFromRobot;
	
	/**
	 * Démarre la résolution complète du rubik's cube
	 * Etapes de résolution:
	 * 	En cas de problèmes, Exception retournant le numéro de l'étape posant problème 
	 * 	100. Connection avec le robot												-> OK
	 * 	200. Rotation du cube et reconnaissance des différentes faces avec caméra	-> OK
	 * 		210. Création object Rubik												-> OK
	 * 		220. Scannage du rubik vie l'objet scanner								-> OK
	 * 	300. Résolution du Rubik avec l'algo de Kociemba							-> OK
	 * 	400. Envoie des ordres au robot qui va gérer son exécution					-> OK
	 *
	 * @param event L'événement capté
	 */
	@FXML
	protected void startApp(ActionEvent event)
	{
		logReset();
		log("Clic");
		/*if(true)
			return;
		
		/** ETAPE 100 **/
		Robot myRobot = new Robot();
		try {
			myRobot.connect();
		} catch (RobotException e) {
			log(e.toString());
			System.exit(100);
		}
		
		
		/** ETAPE 200 **/
		log("étape 200");
		Rubik rubikScanned = new Rubik();
		Scanner myScanner = new Scanner(currentFrame);
		try {
			myScanner.scan(rubikScanned, myRobot);
		} catch (ScannerException e) {
			log(e.toString());
			return;
		}
		log("Scan Completed");
		
		log(rubikScanned.toString());
		System.out.println(rubikScanned.listColorToSolve());
		/** ETAPE 300 **/
		
		
		log("Solutions en 20 mvts:");
		String instructions = Search.solution(rubikScanned.listColorToSolve(), 20, 1, false);
		System.out.println(instructions);
		if(instructions.charAt(0)=='E'){
			log("Solutions en 21 mvts:");
			instructions = Search.solution(rubikScanned.listColorToSolve(), 21, 1, false);
			System.out.println(instructions);
		}/**/

		/** ETAPE 400 **/
		log("Etape de résolution");
		
		try {
			myRobot.execute(instructions);
		} catch (RobotException e) {
			log(e.toString());
		}/**/
	}
	
	/**
	 * @param event Evénement
	 */
	@FXML
	public void scanFace(ActionEvent event){
		
		Rubik cube = new Rubik();
		cube.setTemp();
		

        log(cube.toString());
        log(cube.listColorToSolve());
        
		int test = Tools.verify(cube.listColorToSolve());
        if(test != 0){
            log("Erreur");
        }/**/
		
		/*
		if(true)
			return;
		/**/
		
		//Scannage de la face
		scannerFromRobot = new Scanner(currentFrame);
		ColorCube[][] scannedData = null;
		try {
			scannedData = scannerFromRobot.scan();
		} catch (ScannerException e) {
			log(e.toString());
			return;
		}
		
		//Vérification des connées
		if(scannedData==null){
			log("Erreur lors du Scannage");
			return;
		}
		
		//Affichage des données
		String data = "";
		for(int j=0;j<3;j++){
			for(int k=0;k<3;k++){
				data+=scannedData[j][k].toString()+"  \t";
			}
			data+="\n";
		}
		log(data);
	}
	
	/**
	 * Test de disponibilité des NXTs
	 * @param event
	 */
	@FXML
	protected void connectRobot(ActionEvent event){
		/** ETAPE 100 **/
		Robot robotTest = new Robot();
		try {
			robotTest.connect();
		} catch (RobotException e) {
			log(e.toString());
			System.exit(100);
		}
		
		try {
			robotTest.execute("F ");
		} catch (RobotException e) {
			log(e.toString());
		}
	}
	
	/**
	 * Fonction permettant d'afficher des logs prévu dans la zone à cet effet
	 * @param text Le texte à afficher dans la zone de log
	 */
	public void log(String text){
		//Log d'affichage
		String logs = txa_log.getText();
		logs += text+"\n";
		txa_log.setText(logs);
		
		//Log de la console
		System.out.println(text);
	}
	
	/**
	 * Fonction permettant de réinitialiser la zone de log à vide
	 */
	public void logReset(){
		txa_log.setText("");
	}
}
