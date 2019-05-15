package objects;

import lejos.nxt.ColorSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.RegulatedMotor;

/**
 * OrderManager est la class gerant la synchronisation de deux moteurs
 * 
 * Le programme se compose d'une boucle principale attendant une connection du PC via USB.
 * Les instructions reçues sont traitees dans la class OrderManager afin d'effectuer les differents ordres
 * 
 * 
 * @see main.AppNXT
 * @see MotorSynchronisation
 * 
 * @author	Loic.Wermeille
 * @version	1.0
 */
public class OrderManager {

	/**Constante de couleur noire*/
	public static int CPT_1_COLOR_RED = 440;
	/**Constante de couleur grise*/
	public static int CPT_1_COLOR_GREY = 400;

	/**Constante de couleur noire*/
	public static int CPT_2_COLOR_RED = 400;
	/**Constante de couleur grise*/
	public static int CPT_2_COLOR_GREY = 350;
	
	
	private static final int SPEED_INIT = 150;
	
	private static final int SPEED_WORK = 300; //OLD 360
	
	private static final int SPEED_ALIGNMENT = 150;
	
	/**Constante de rotation de 1/4 de tour*/
	public static final int ROTATION_ALIGNMENT = 70;
	

	/**Constante de rotation de 1/2 tour*/
	public static final int ROTATION_ALIGNMENT_HALF_TURN = 220;

	/**Etat du type de communication en cours*/
	public boolean phaseTestCommunication = false;
	
	
	private boolean rotaterType = false;
	private int axe = 1;
	
	/**
	 * Declaration des variables moteurs
	 */
	private static RegulatedMotor		MOTOR_A		= new NXTRegulatedMotor(MotorPort.A);
	private static RegulatedMotor		MOTOR_B		= new NXTRegulatedMotor(MotorPort.B);
	private static RegulatedMotor		MOTOR_C		= new NXTRegulatedMotor(MotorPort.C);
	
	/**CAPTOR port A*/
	public static ColorSensor			CAPTOR_A	= new ColorSensor(SensorPort.S1);
	/**CAPTOR port B*/
	public static ColorSensor			CAPTOR_B	= new ColorSensor(SensorPort.S2);
	private static TouchSensor			CAPTOR_C	= new TouchSensor(SensorPort.S3);
	/**
	 * Declaration de l'objet de gestion de synchronisation de MOTOR_A et MOTOR_B
	 */
	private static MotorSynchronisation	syncAB		= new MotorSynchronisation(MOTOR_A, MOTOR_B);
	
	/**
	 * Methode permettant de gerer les ordres reçus
	 * Listes des instructions existantes et leurs significations:
	 * ORDRES SPECIAUX:
	 *	996 -> Init deux pinces + camera
	 *	997 -> Init deux pinces
	 *	998 -> Init deux rotateurs + montage cube
	 *	999 -> Init deux rotateurs + tournage cube
	 * 
	 * TYPE ORDER:
	 * 	10->49
	 * 	dizaine
	 * 	1 -> moteur 1
	 * 	2 -> moteur 2
	 * 	3 -> moteur 3
	 * 	4 -> moteur 1+2
	 * 
	 * unite
	 *  0 -> 1/4 tour horaire
	 *  1 -> 1/4 tour trygonometrique
	 *  2 -> 1/2 tour
	 *  3 -> pince open
	 *  4 -> pince close
	 *  5 -> lever cube
	 *  6 -> descendre descendre cube
	 *	7 -> Placer camera
	 *	8 -> Reculer camera
	 *	9 -> Rotation plateforme cube -> 20 secondes	
	 * @param order
	 * 				L'ordre a traiter
	 * @return
	 * 				Vrai si l'ordre indique une fin de programme
	 * 				Faux si l'ordre n'indique pas la fin du programme
	 */
	public boolean manageOrder(int order){
		boolean end = false;
		if (order < 0){
			end = true;
			order = -order;
		}
		
		//Code de test communication
		if(order==0){
			phaseTestCommunication = true;
			return end;
		}
		if(phaseTestCommunication==true)
			return end;
			
		//Code d'initialisation
		if(order>900){
			init(order);
			
			return true;
		}
		
		//Code de moteur
		if(order>=10&&order<=49){
			motor(order);
			
			return end;
		}
		
		return false;
	}
	
	/**
	 * Methode permettant de d'initialiser la position des moteurs
	 * Listes des instructions existantes et leurs significations:
	 * 	996 -> Init deux rotateurs
	 *	997 -> Init deux rotateurs + caméra
	 *	998 -> Init deux pinces + montage cube
	 *	999 -> Init deux pinces + tournage cube
	 * 
	 * @param order
	 * 				L'ordre a traiter
	 */
	protected void init(int code){
		//Modification de la vitesse des moteurs
		MOTOR_A.setSpeed(SPEED_INIT);
		MOTOR_B.setSpeed(SPEED_INIT);
		
		//Verification si initialisation des pinces
		if(code==996 || code==997){
			//modification valeurs luminosité pour capteurs de la brique 2
			if(code==997){
				CPT_1_COLOR_RED = 450;
				CPT_1_COLOR_GREY = 400;
				CPT_2_COLOR_RED = 450;
				CPT_2_COLOR_GREY = 390;
			}
			
			// robot constitue de deux rotateur
			/** INIT ROTATEURS**/
			rotaterType = true;
			MOTOR_A.rotate(ROTATION_ALIGNMENT);
			MOTOR_A.forward();
			while(CAPTOR_A.getRawColor().getRed()<CPT_1_COLOR_GREY){
			}
			MOTOR_A.stop();
			MOTOR_A.rotate(ROTATION_ALIGNMENT_HALF_TURN+ROTATION_ALIGNMENT+30);
			MOTOR_A.forward();
			while(CAPTOR_A.getRawColor().getRed()<CPT_1_COLOR_RED){
			}
			MOTOR_A.stop();
			
			MOTOR_B.rotate(ROTATION_ALIGNMENT);
			MOTOR_B.forward();
			while(CAPTOR_B.getRawColor().getRed()<CPT_2_COLOR_GREY){
			}
			MOTOR_B.stop();
			MOTOR_B.rotate(ROTATION_ALIGNMENT_HALF_TURN+ROTATION_ALIGNMENT+30);
			MOTOR_B.forward();
			while(CAPTOR_B.getRawColor().getRed()<CPT_2_COLOR_RED){
			}
			MOTOR_B.stop();
			
			if(code==997){
				// Init camera
				/** INIT CAMERA**/
				MOTOR_C.backward();
				while(!CAPTOR_C.isPressed()){
				}
				MOTOR_C.stop();
				MOTOR_C.rotate(720);
				
				axe = 2;
			}
		
		}else if(code==998 || code==999){
			// robot constitue de deux pinces
			/** INIT PINCES**/
			
			if(code==998){
				// Init monteur cube en plus
				/** INIT MONTEUR CUBE**/
				/* CODE */
				
			}else{
				// Init tourneur cube en plus
				/** INIT TOURNEUR CUBE**/
				/* CODE */
				
				axe = 2;
			}
		}
		
		//Modification de la vitesse des moteurs
		MOTOR_A.setSpeed(SPEED_WORK);
		MOTOR_B.setSpeed(SPEED_WORK);
	}
	
	/**
	 * Methode permettant de gerer les ordres standards
	 * Listes des instructions existantes et leurs significations:
	 * 
	 * TYPE ORDER:
	 * 	10->49
	 * 	dizaine
	 * 	1 -> moteur 1
	 * 	2 -> moteur 2
	 * 	3 -> moteur 3
	 * 	4 -> moteur 1+2
	 * 
	 * unite
	 *  0 -> 1/4 tour horaire
	 *  1 -> 1/4 tour trygonometrique
	 *  2 -> 1/2 tour
	 *  3 -> pince open
	 *  4 -> pince close
	 *  5 -> lever cube
	 *  6 -> descendre descendre cube
	 *	7 -> Placer camera
	 *	8 -> Reculer camera
	 *	9 -> Rotation plateforme cube -> 20 secondes
	 *
	 * @param order
	 * 				L'ordre a traiter
	 */
	protected void motor(int order){
		MOTOR_A.setSpeed(SPEED_WORK);
		MOTOR_B.setSpeed(SPEED_WORK);
		
		//Code moteur
		int dizaines = order/10;
		int unites = order%10;

		//Variable valeur rotation
		int varDeg = 0;
		
		//Modification de l'ordre pour le moteur deux
		//if(unites<=1&&dizaines==2)
		//	unites= (-(unites*2-1)+1)/2;
		
		//Inversion de l'ordre du sens de rotation selon axe et moteur
		if(dizaines <= 2 && unites<=1 && rotaterType == true){
			if(axe==1&&dizaines==2||axe==2&&dizaines==2){
				unites=(unites-1)*-1;
			}
		}
		
		switch(unites){
		case 0:
			// 1/4 tour horraire
			varDeg = 123;
			break;
		case 1:
			// 1/4 tour trigonometrique
			varDeg = -123;
			break;
		case 2:
			// 1/2 tour
			varDeg = 247;
			break;
		case 3:
			// Pince ouvrir
			varDeg = -180;
			break;
		case 4:
			// Pince fermer
			varDeg = 240;
			break;
		case 5:
			// Monter cube
			break;
		case 6:
			// Descendre cube
			break;
		case 7:
			// Placer camera
			MOTOR_C.backward();
			while(!CAPTOR_C.isPressed()){
			}
			MOTOR_C.stop();
			break;
		case 8:
			// Enlever camera
			MOTOR_C.rotate(720);
			break;
		case 9:
			// Rotation cube
			break;
		}
		
		//Choix moteur
		switch(dizaines){
		case 1:
			//moteur 1
			if(unites <= 2){
				switch(unites){
				case 0:
					MOTOR_A.rotate(ROTATION_ALIGNMENT);
					MOTOR_A.setSpeed(SPEED_ALIGNMENT);
					MOTOR_A.forward();
					while(CAPTOR_A.getRawColor().getRed()<CPT_1_COLOR_GREY){
					}
					MOTOR_A.stop();
					MOTOR_A.setSpeed(SPEED_WORK);
					break;
				case 1:
					MOTOR_A.rotate(-ROTATION_ALIGNMENT);
					MOTOR_A.setSpeed(SPEED_ALIGNMENT);
					MOTOR_A.backward();
					while(CAPTOR_A.getRawColor().getRed()<CPT_1_COLOR_GREY){
					}
					MOTOR_A.stop();
					MOTOR_A.setSpeed(SPEED_WORK);
					break;
				case 2:
					MOTOR_A.rotate(ROTATION_ALIGNMENT_HALF_TURN);
					MOTOR_A.setSpeed(SPEED_ALIGNMENT);
					MOTOR_A.forward();
					while(CAPTOR_A.getRawColor().getRed()<CPT_1_COLOR_GREY){
					}
					MOTOR_A.stop();
					MOTOR_A.setSpeed(SPEED_WORK);
					break;
				}
			}else{
				MOTOR_A.rotate(varDeg);}
			break;
		case 2:
			//moteur 2
			if(unites <= 2){
				switch(unites){
				case 0:
					MOTOR_B.rotate(ROTATION_ALIGNMENT);
					MOTOR_B.setSpeed(SPEED_ALIGNMENT);
					MOTOR_B.forward();
					while(CAPTOR_B.getRawColor().getRed()<CPT_2_COLOR_GREY){
					}
					MOTOR_B.stop();
					MOTOR_B.setSpeed(SPEED_WORK);
					break;
				case 1:
					MOTOR_B.rotate(-ROTATION_ALIGNMENT);
					MOTOR_B.setSpeed(SPEED_ALIGNMENT);
					MOTOR_B.backward();
					while(CAPTOR_B.getRawColor().getRed()<CPT_2_COLOR_GREY){
					}
					MOTOR_B.stop();
					MOTOR_B.setSpeed(SPEED_WORK);
					break;
				case 2:
					MOTOR_B.rotate(ROTATION_ALIGNMENT_HALF_TURN);
					MOTOR_B.setSpeed(SPEED_ALIGNMENT);
					MOTOR_B.forward();
					while(CAPTOR_B.getRawColor().getRed()<CPT_2_COLOR_GREY){
					}
					MOTOR_B.stop();
					MOTOR_B.setSpeed(SPEED_WORK);
					break;
				}
			}else{
				MOTOR_B.rotate(varDeg);
			}
			break;
		case 3:
			//moteur 3
			MOTOR_C.rotate(varDeg);
			break;
		case 4:
			//moteurs 1+2
			syncAB.syncRotate(unites);
			break;
		}
	}
}
