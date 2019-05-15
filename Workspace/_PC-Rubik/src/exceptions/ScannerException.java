package exceptions;

/**
 * <b>Gestion des exceptions de la class Scanner</b>
 * 
 * @see objects.Scanner
 * 
 * @author	Loïc.Wermeille
 * @since	24.03.2016
 * @version 1.0
 */
public class ScannerException extends Exception {
	private static final long serialVersionUID = 1;
	
	/**
	 * Gestion de l'erreur
	 * @param number Numéro de l'erreur
	 * @param message Signification du message d'erreur
	 */
	public ScannerException(int number, String message){
		super(number + " : " +message);
	}
}
