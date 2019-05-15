package objects;

/**
 * class permettant d'effectuer une liste de mouvement sur un Rubik logique
 * @author	Loïc Wermeille
 * @since	30.12.2015
 * @version	1.0
 */
public class SolveAI {
	
	/**
	 * Fonction exécutant une liste d'opérations sur un cube
	 * @param rubik rubik's cube à résoudre
	 * @param instructions instruction en format chaîne
	 */
	public static void resoudCube(Rubik rubik, String instructions){
		for(int i=0; i<=instructions.length()-1;i++){
			//Récupération de la face
			int face = 0;
			String toTest = Character.toString(instructions.charAt(i));
			switch(toTest){
				case "U": face=1; break;
				case "L": face=2; break;
				case "F": face=3; break;
				case "R": face=4; break;
				case "B": face=5; break;
				case "D": face=6; break;
				case ".": face=9; break;
				default: System.out.println("Error algo Movement part 1");
			}
			
			if(face==9)
				return;
			
			//Récupération du type de mouvement
			i++;
			int rotation = 0;
			toTest = Character.toString(instructions.charAt(i));
			switch(toTest){
				case " ": rotation++; break;
				case "'": rotation--; i+=1; break;
				case "2": rotation=2; i+=1; break;
				default: System.out.println("Error algo Movement part 2");
			}
			
			//Effectuation du mouvement
			rubik.turn(face, rotation);
		}
	}
}