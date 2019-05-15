package objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.RobotException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

/**
 * <b>Classe gérant les différents NXT ainsi que leurs moteurs respectifs</b>
 * @author	Loïc Wermeille
 * @since	24.03.2016
 * @version	2.0
 */
public class Robot {
	
	//Occurence 0 = null -> place pour 4 nxt de l'occurence 1 a 4
	private NXT[] nxts= new NXT[5];
	
	/**	
	 * Methode permettant de connecter les différents NXT
	 * @throws RobotException Exception spécifique au robot
	 */
	public void connect() throws RobotException {
		/** Modèle de données v.2:
		 *   quatre NXT avec trois sorties
		 *   
		 *   PLAN ROTATIONS et système de codage
		 *   NXT 1  -> 1.x
		 *   port 2 -> x.2
		 *   	 2.1
		 *   1.1	 1.2
		 *   	 2.2
		 *   
		 *   PLANS PINCES
		 *   	 4.1
		 *   3.1	 3.2
		 *   	 4.2
		 *   
		 *   RÉSUMÉ PAR NXT
		 *   NXT1
		 *   	port 1 -> rotate gauche
		 *   	port 2 -> rotate droite
		 *   	port 3 -> gestion camera
		 *   NXT2
		 *   	port 1 -> rotate arrière
		 *   	port 2 -> rotate avant
		 *   	port 3
		 *   NXT3
		 *   	port 1 -> pince gauche
		 *   	port 2 -> pince droite
		 *   	port 3 -> élévation Rubick
		 *   
		 *   NXT4
		 *   	port 1 -> pince arrière
		 *   	port 2 -> pince avant
		 *   	port 3 -> rotation plateforme centrale
		 *   
		 */
		
		int nbBrick = 0;
		NXTInfo[] nxtInfo = null; 
		NXTConnector nxtConnector = new NXTConnector();
		nxtInfo = nxtConnector.search(null,null,NXTCommFactory.USB);
		System.out.println(Arrays.toString(nxtInfo));
		int lasBrick = 0;
		//nxt = nxtInfo[0];
		//System.out.println(nxtInfo[0].name + nxtInfo[0].deviceAddress);
		//System.out.println(nxtInfo[1].name + nxtInfo[1].deviceAddress);
		
		//Attribution des robots a chaque slot
		for(int i=0; i<=3; i++){
			try {
				System.out.println(nxtInfo[i].name);
				//liste nom et correspondance adresses
				// NXT1 - 0016530D7EF5
				// NXT2 - 0016530E9254
				// NXT3 - 0016530D38E4
				// NXT4 - 0016531798DB
				switch(nxtInfo[i].deviceAddress){
					case "0016530D7EF5":
						nxts[1] = new NXT();
						nxts[1].init(nxtInfo[i]);
						nbBrick++;
						lasBrick = 1;
						break;
					case "0016530E9254":
						nxts[2] = new NXT();
						nxts[2].init(nxtInfo[i]);
						nbBrick++;
						lasBrick=2;
						break;
					case "0016530D38E4":
						nxts[3] = new NXT();
						nxts[3].init(nxtInfo[i]);
						nbBrick++;
						lasBrick=3;
						break;
					case "0016531798DB":
						nxts[4] = new NXT();
						nxts[4].init(nxtInfo[i]);
						nbBrick++;
						lasBrick=4;
						break;
					default:
						System.out.println("Robot inconnu");
						throw new RobotException(7,"Robot inconnu");
				}
			} catch(Exception e){
				System.out.println(e.toString()+" - "+nbBrick);
				if(nbBrick < 4)
					throw new RobotException(6,"Fail to connect to all NXT");
			}	
		}
		
		// Positionnement des 4 pinces
		// Code 996 et 997 pour NXT 1 et 2
		// Code 998 et 999 pour NXT 3 et 4
		// Code différent du aux différents usages
		List<Integer> init = new ArrayList<Integer>(0);
		if(lasBrick!=1){
			init.add(996);
			nxts[1].send(init);
			init.set(0,997);
			nxts[2].send(init);
		}else{
			init.add(997);
			nxts[2].send(init);
			init.set(0,996);
			nxts[1].send(init);
		}
		init.set(0,998);
		nxts[3].send(init);
		init.set(0,999);
		nxts[4].send(init);
	}
	
	/**
	 * Fonction intermédiaire entre les ordres standars et les instructions complexes robots. 
	 * @param instructions Ordre sous forme de chaine 
	 * @throws RobotException Exception spécifique au robot
	 */
	public void execute(String instructions) throws RobotException{
		executeRobotInstructions(Convertor.convertMovement(instructions));
	}
	/**
	 * Fonction intermédiaire entre les ordres standars et les instructions complexes robots. 
	 * @param order Ordre sous forme d'entier 
	 * @throws RobotException Exception spécifique au robot
	 */
	public void execute(int order) throws RobotException{
		executeRobotInstructions(Convertor.convertOrder(order));
	}
	
	/**
	 * Optimisation de l'envoie des données aux différents NXT en limitant le nb de connexions
	 * @param orders Listes des ordres dans un tableau
	 * @throws RobotException Exception de type Robot
	 */
	public void executeRobotInstructions(int[][] orders) throws RobotException{
		boolean endInstructions = false;
		int indice=0;
		System.out.println("Instructions:");
		while(!endInstructions){
			if(orders[indice][0]==0)
				endInstructions=true;
			else if(orders[indice][1]!=1){
				//orders[indice][1] valant 1 = instructions supprimé lors de l'optimisation
				boolean maximise = true;
				ArrayList<Integer> ordersAboutToBeSend = new ArrayList<Integer>(0);
				ordersAboutToBeSend.add(orders[indice][1]);
				System.out.print(orders[indice][0]+""+orders[indice][1]+"-");
				
				//Groupement des instructions par brique nxt
				while(maximise){
					if(orders[indice+1][0]==orders[indice][0]){
						indice++;
						ordersAboutToBeSend.add(orders[indice][1]);
						System.out.print(orders[indice][0]+""+orders[indice][1]+"-");
					}else
						maximise = false;
				}
				
				System.out.print("\n");
				//int order = orders[indice][1];
				
				if(orders[indice][0]>4)
					throw new RobotException(1,"Num nxt inconnu");
				
				nxts[orders[indice][0]].send(ordersAboutToBeSend);
				
			}
			indice++;
		}
	}
}