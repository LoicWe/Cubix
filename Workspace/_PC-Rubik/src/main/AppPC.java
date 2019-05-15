package main;

import java.io.IOException;	

import org.opencv.core.Core;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**<
 * <b>AppPC est la classe principale permettant la résolution d'un rubik's cube</b>
 * <p>
 * La classe permet d'utiliser la bibliothèque org.kociemba.twophase pour résoudre logiquemenet le rubik's cube.
 * Elle communique avec les différentes briques NXT afin de controler le robot et résoudre physiquement le rubik's cube.
 * Cette classe est utilisable à travers une interface graphique.
 * </p>
 * 
 * @author	Loïc.Wermeille
 * @since	20.12.2015
 * @version 1.0
 */
public class AppPC extends Application {
	
	private Stage primaryStage;
    private BorderPane rootLayout;
	
    /**
	 * Méthode permettant de gérer la création de la fenêtre
	 * @param primaryStage Fenêtre principale
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Rubik's cube Solver v1.0 - Loïc Wermeille");

        initRootLayout();
	}
	
	/**
	 * Fonction de personalisation de la fenêtre avec ajout du gestionnaire d'événement dans la fenêtre
	 * @see view.FXController
	 */
	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppPC.class.getResource("/view/DisplayJFX.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            
            // Feuille CSS
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            //Affichage de la fenêtre
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Méthode principale d'entrée du programme qui lance l'interface graphique
	 * @param args Valeurs d'entrée du programme
	 */
	public static void main(String[] args) {
		// load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//Création de l'affichage
		launch(args);
	}
}
