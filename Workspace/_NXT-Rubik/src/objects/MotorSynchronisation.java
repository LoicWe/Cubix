package objects;

import lejos.nxt.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * <b>AppNXT est la class gerant la synchronisation de deux moteurs</b>
 * <p>
 * Le programme se compose d'une boucle principale attendant une connection du PC via USB.
 * Les instructions reçues sont traitees dans la class OrderManager afin d'effectuer les differents ordres
 * </p>
 * 
 * @see OrderManager
 * 
 * @author	Loïc Wermeille
 * @version	1.0
 */
public class MotorSynchronisation {
	protected DifferentialPilot pilot;
	protected RegulatedMotor motor1;
	protected RegulatedMotor motor2;
	
	private static final int SPEED_PILOT = 3;
	private static final int SPEED_PILOT_ALIGNMENT = 1;
	
	private static final double ROTATION_ALIGNMENT_PILOT = 0.8;
	private static final double ROTATION_ALIGNMENT_HALF_TURN_PILOT = 1.8;

	/**
	 * Constructeur
	 * @param motor1 moteur a synchroniser numero 1
	 * @param motor2 moteur a synchroniser numero 2
	 */
	public MotorSynchronisation(RegulatedMotor motor1, RegulatedMotor motor2){
		this.motor1 = motor1;
		this.motor2 = motor2;
		pilot = new DifferentialPilot(1f, 1f, this.motor1, this.motor2);
	}
	
	/**
	 * Effectue une rotation synchroniser sur deux moteurs
	 * @param rotation type de rotation a effectuer 
	 */
	public void syncRotate(int rotation) {
		pilot.setTravelSpeed(SPEED_PILOT);
		switch(rotation){
		case 0:
			pilot.travel(ROTATION_ALIGNMENT_PILOT);
			pilot.setTravelSpeed(SPEED_PILOT_ALIGNMENT);
			pilot.forward();
			while(OrderManager.CAPTOR_A.getRawColor().getRed()<OrderManager.CPT_1_COLOR_GREY){
			}
			pilot.stop();
			break;
		case 1:
			pilot.travel(-ROTATION_ALIGNMENT_PILOT);
			pilot.setTravelSpeed(SPEED_PILOT_ALIGNMENT);
			pilot.backward();
			while(OrderManager.CAPTOR_A.getRawColor().getRed()<OrderManager.CPT_1_COLOR_GREY){
			}
			pilot.stop();
//			pilot.travel(0.3);
			break;
		case 2:
			pilot.travel(ROTATION_ALIGNMENT_HALF_TURN_PILOT);
			pilot.setTravelSpeed(SPEED_PILOT_ALIGNMENT);
			pilot.forward();
			while(OrderManager.CAPTOR_A.getRawColor().getRed()<OrderManager.CPT_1_COLOR_GREY){
			}
			pilot.stop();
			break;
		case 3:
			pilot.travel(-1);
			pilot.stop();
			break;
		case 4:
			pilot.travel(1.2);
			pilot.stop();
			break;
		}
		pilot.setTravelSpeed(SPEED_PILOT);
		LCD.drawString("fin sync pilot",0,6);
	}
}
