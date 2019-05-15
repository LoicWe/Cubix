package exceptions;

/**
 * <b>Gestion des exceptions de la class Robot</b>
 * 
 * @see objects.Robot
 * 
 * @author	Loïc.Wermeille
 * @since	23.03.2016
 * @version 1.0
 */
public class RobotException extends Exception {
	private static final long serialVersionUID = 1;
	
	/**
	 * Gestion de l'erreur
	 * @param number Numero de l'erreur
	 * @param message Signification du message d'erreur
	 */
	public RobotException(int number, String message){
		super(number + " : " +message);
	}
}
