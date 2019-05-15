package objects;

import exceptions.RobotException;

/**
 * <b>Class static permettant de convertir des ordres humain en ordres robots</b>
 * <p>Convertor peut convertir les différents types d'ordres suivant en ordre pour le robot:</p>
 * <ul>
 * <li>Ordres format Humain (L U2 B' R2 F U' F')</li>
 * <li>ordres spécifiques au robot [axe,moteur,rotation]</li>
 * </ul>
 * <p>Les ordres générées sont une série d'instructions compréhensible et spécifique pour l'objet robot</p>
 * 
 * @see Robot
 * 
 * @author	Loïc Wermeille
 * @since	24.03.2016
 * @version	1.0
 */
public final class Convertor {
	
	private static int indice = 0;
	private static int[][] orders = null;
	
	/**
	 * Méthode retournant des instructions spécifiques pour l'objet robot
	 * @param listOperation Tableau[x][2] -&gt; colonne 1 = face, colonne 2 = sens rotation
	 * @return Liste des instructions codées dans un tableau de int à deux dimensions int[nMoteur][Ordre]
	 * @throws RobotException Exceptions spécifiques à au robot 
	 */
	public static int[][] convertMovement(String listOperation) throws RobotException{
		
		boolean upAtTop = true;
		indice = 0;
		orders = new int[200][2];
		
		for(int i=0; i <= listOperation.length()-1;i++){
			int order = 0;
			//Lecture du mouvement
			switch(listOperation.charAt(i)){
				case 'U':
					if(upAtTop){
						assistedRotationInstructions(140);
						upAtTop = false;
					}
					order=220;
					break;
				case 'D':
					if(upAtTop){
						assistedRotationInstructions(140);
						upAtTop = false;
					}
					order=210;
					break;
				case 'L':
					order=110;
					break;
				case 'R':
					order=120;
					break;
				case 'B':
					if(!upAtTop){
						assistedRotationInstructions(141);
						upAtTop = true;
					}
					order=210;
					break;
				case 'F':
					if(!upAtTop){
						assistedRotationInstructions(141);
						upAtTop = true;
					}
					order=220;
					break;
			}
			//Prochain caractère
			i++;
			
			//Lecture de la rotation
			switch(listOperation.charAt(i)){
			case '2':
				order+=2;
				i++;
				break;
			case '\'':
				order+=1;
				i++;
			}
			assistedRotationInstructions(order);
		}
		maximise();
		return orders;
	}
	/**
	 * Méthode retournant des instructions spécifiques pour l'objet robot
	 * @param order
	 * 					ordre spécifique pour le robot
	 * @return 
	 * 					liste des instructions codées dans un tableau de int à deux dimensions int[nMoteur][Ordre]
	 * @throws RobotException
	 * 					exceptions spécifiques à au robot 
	 */
	public static int[][] convertOrder(int order) throws RobotException{
		indice = 0;
		orders = new int[10][2];
		assistedRotationInstructions(order);
		return orders;
	}
	
	/**
	 * Fonction transformant un ordre[axe,moteur,rotation] en une série d'instructions géré par l'objet Robot<br>
	 *
	 * FORMAT DE CODAGE DE ORDRE:<br>
	 *	nombre à trois chiffre:<br>
	 *	centaine	-> axe (1=parallèle, 2 perpendiculaire)<br>
	 *	dizaine		-> pince (1=première, 2=deuxième, 4=les deux)<br>
	 *	unité		-> sens rotation (0=horraire, 1=trygo, 2=1/2 tour)
	 *
	 * @param order Ordre à exécuter
	 * @throws RobotException Exception spécifique au robot
	 */
	private static void assistedRotationInstructions(int order) throws RobotException{
		int axe = order/100;
		int unites = order%10;
		int dizaines = (order-axe*100-unites)/10;
		int orderNXT = order-axe*100;
		
		//Ouverture des pinces en cas de rotation du cube
		if(dizaines==4){
			if(axe==1){
				orders[indice][0]=4;
				orders[indice][1]=43;
			}else{
				orders[indice][0]=3;
				orders[indice][1]=43;
			}
			indice++;
		}
		
		// Execution de l'ordre
		switch(axe){
		case 1:
			orders[indice][0]=1;
			orders[indice][1]=orderNXT;
			break;
		case 2:
			orders[indice][0]=2;
			orders[indice][1]=orderNXT;
			break;
		default:
			throw new RobotException(10, "Num nxt inconnu");
		}
		indice++;
		
		//exit en cas de non besoin de remettre les pinces justes
		if(unites==2&&dizaines!=4)
			return;
		
		//Fermeture des pinces en cas de rotation complète
		if(dizaines==4){
			if(axe==1){
				orders[indice][0]=4;
				orders[indice][1]=44;
			}else{
				orders[indice][0]=3;
				orders[indice][1]=44;
			}
			indice++;
			
			if(unites==2)
				return;
		}
		
		//Ouverture des pinces à remettre en place
		if(axe==1){
			orders[indice][0]=3;
			orders[indice][1]=dizaines*10+3;
		}else{
			orders[indice][0]=4;
			orders[indice][1]=dizaines*10+3;
		}
		indice++;
		
		//Inversion du sens de rotation
		unites= (-(unites*2-1)+1)/2;
		orderNXT=dizaines*10+unites;
		if(orderNXT==8){
			System.out.println("Error #8888888888888888");
			System.out.println("Unite : "+unites);
			System.out.println("Order : "+orderNXT);
		}
		//Rotation en sens inverse
		switch(axe){
		case 1:
			orders[indice][0]=1;
			orders[indice][1]=orderNXT;
			break;
		case 2:
			orders[indice][0]=2;
			orders[indice][1]=orderNXT;
			break;
		case 3:
			orders[indice][0]=3;
			orders[indice][1]=orderNXT;
			break;
		default:
			throw new RobotException(10, "Num nxt inconnu");
		}
		indice++;
		
		//Fermeture de la pince
		if(axe==1){
			orders[indice][0]=3;
			orders[indice][1]=dizaines*10+4;
		}else{
			orders[indice][0]=4;
			orders[indice][1]=dizaines*10+4;
		}
		indice++;
	}
	
	/**
	 * Fonction d'optimisation des instructions<br>
	 * Suppression des opérations inverses qui se suivent
	 */
	private static void maximise(){
		//Optimisation d'une fermeture de pince suivi d'une ouverture de celle-ci
		for(int i=1;i<=indice;i++){
			if(orders[i-1][0]==orders[i][0]){
				if(orders[i-1][1]-orders[i][1]<2&&orders[i][1]-orders[i-1][1]<2){
					System.out.println("Erreur de répétition détecté et corrigé :"+orders[i-1][0]+orders[i][1]+"-"+orders[i-1][0]+orders[i-1][1]);
					orders[i][0]=1;
					orders[i][1]=1;
					orders[i-1][0]=1;
					orders[i-1][1]=1;
				}
			}
		}
	}
}
