package objects;

/**
 * <b>Class définisant un objet de type rubik's cube</b>
 * <p>
 * Les données du rubik's cube sont stocké dans un tableau de couleurs (colorRubik)
 * 
 * Numérotations des faces:<br>
 *	-1--<br>
 *	2345<br>
 *	-6--<br>
 *
 * Aperçu du tableau de valeurs:<br>
 *	- - - 1 1 1 - - - - - -<br>
 *	- - - 1 1 1 - - - - - -<br>
 *	- - - 1 1 1 - - - - - -<br>
 *	2 2 2 3 3 3 4 4 4 5 5 5<br>
 *	2 2 2 3 3 3 4 4 4 5 5 5<br>
 *	2 2 2 3 3 3 4 4 4 5 5 5<br>
 *	- - - 6 6 6 - - - - - -<br>
 *	- - - 6 6 6 - - - - - -<br>
 *	- - - 6 6 6 - - - - - -<br>
 * </p>
 * 
 * @see ColorCube
 * 
 * @author	Loïc Wermeille
 * @since	30.01.2016
 * @version	2.0
 */
public class Rubik{
	// tableau des valeurs des faces
	private ColorCube values[][] = new ColorCube [9][12];
	
	/**
	 * Constructeur
	 */
	public Rubik() {
		//Initailisation des valeurs
		initValues();
	}
	
	/**
	 * Initialisation des faces du Rubik's cube
	 */
	private void initValues() {
		//Face 1, 6 et faces noirs
		//Remplissage des cases vides avec une couleur noir afin de pouvoir détecter les erreurs
		int colorValue = 1;
		int colorInverse = 6;
		ColorCube colorFace = ColorCube.getColor(colorValue);
		ColorCube colorFaceOppose = ColorCube.getColor(colorInverse);
		//Boucle sur les 9 carrés de la face
		for(int j=0; j<=2; j++){
			for(int k=0; k<=2; k++){
				values[j][3+k]=colorFace;
				values[j][k]=ColorCube.black;
				values[j][6+k]=ColorCube.black;
				values[j][9+k]=ColorCube.black;
				values[6+j][3+k]=colorFaceOppose;
				values[6+j][k]=ColorCube.black;
				values[6+j][6+k]=ColorCube.black;
				values[6+j][9+k]=ColorCube.black;
			}
		}
		colorValue++;
		colorFace = ColorCube.getColor(colorValue);
		
		//Boucle sur les faces 2,3,4,5
		for(int i=0;i<=3;i++){
			//Boucle sur les 9 carrés de la face
			for(int j=0; j<=2; j++){
				for(int k=0; k<=2; k++){
					values[3+j][i*3+k]=colorFace;
				}
			}
			colorValue++;
			colorFace = ColorCube.getColor(colorValue);
		}
	}
	
	/**
	 * Méthode permettant de vérifier si toutes les faces sont identiques
	 * @return Vrai ou faux si toutes les faces sont correctes
	 */
	public boolean check(){
		//Boucle par face
		for(int i=0;i<=2;i++){
			for(int j=0;j<=3;j++){
				//Vérification des 9 carrés de chaque face
				ColorCube colorFace = values[i*3][j*3];
				for(int k=0; k<=2;k++){
					for(int l=0; l<=2;l++){
						if(colorFace!=values[i*3+k][j*3+l]){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Fonction permettant de tourner une face
	 * @param face Numéro de la face à tourner
	 * @param rotation Indice de la rotation, 1=1/4 de tour(sens horaire), 2=1/2 tour, -1=1/4 de tour(sens trigonométrique)
	 */
	public void turn(int face, int rotation){
		//tableau de couleurs de 5x5 contenant toutes les faces é déplacer
		ColorCube newFaceValues[][] = new ColorCube[5][5];
		// Initialisation du tableau
		for(int i=0;i<=4;i++){
			for(int j=0; j<=4;j++){
				newFaceValues[i][j]=ColorCube.black;
			}
		}
		
		int line = 0;
		int column = 0;
		
		// Choix de la face à tourner
		switch(face){
		case 1:
			line = 0;
			column = 1;
			
			// Dessus
			newFaceValues[0][1]=values[3][11];
			newFaceValues[0][2]=values[3][10];
			newFaceValues[0][3]=values[3][9];
			
			// Gauche
			newFaceValues[1][0]=values[3][0];
			newFaceValues[2][0]=values[3][1];
			newFaceValues[3][0]=values[3][2];

			// Droite
			newFaceValues[1][4]=values[3][8];
			newFaceValues[2][4]=values[3][7];
			newFaceValues[3][4]=values[3][6];

			// Dessous
			newFaceValues[4][1]=values[3][3];
			newFaceValues[4][2]=values[3][4];
			newFaceValues[4][3]=values[3][5];
			
			break;
		
		case 2:
			line = 1;
			column = 0;
			
			// Dessus
			newFaceValues[0][1]=values[0][3];
			newFaceValues[0][2]=values[1][3];
			newFaceValues[0][3]=values[2][3];
			
			// Gauche
			newFaceValues[1][0]=values[3][11];
			newFaceValues[2][0]=values[4][11];
			newFaceValues[3][0]=values[5][11];

			// Droite
			newFaceValues[1][4]=values[3][3];
			newFaceValues[2][4]=values[4][3];
			newFaceValues[3][4]=values[5][3];

			// Dessous
			newFaceValues[4][1]=values[8][3];
			newFaceValues[4][2]=values[7][3];
			newFaceValues[4][3]=values[6][3];
			break;
			
		case 3:
			line = 1;
			column = 1;
			
			// Dessus
			newFaceValues[0][1]=values[2][3];
			newFaceValues[0][2]=values[2][4];
			newFaceValues[0][3]=values[2][5];
			
			// Gauche
			newFaceValues[1][0]=values[3][2];
			newFaceValues[2][0]=values[4][2];
			newFaceValues[3][0]=values[5][2];

			// Droite
			newFaceValues[1][4]=values[3][6];
			newFaceValues[2][4]=values[4][6];
			newFaceValues[3][4]=values[5][6];

			// Dessous
			newFaceValues[4][1]=values[6][3];
			newFaceValues[4][2]=values[6][4];
			newFaceValues[4][3]=values[6][5];
			break;
			
		case 4:
			line = 1;
			column = 2;
			
			// Dessus
			newFaceValues[0][1]=values[2][5];
			newFaceValues[0][2]=values[1][5];
			newFaceValues[0][3]=values[0][5];
			
			// Gauche
			newFaceValues[1][0]=values[3][5];
			newFaceValues[2][0]=values[4][5];
			newFaceValues[3][0]=values[5][5];

			// Droite
			newFaceValues[1][4]=values[3][9];
			newFaceValues[2][4]=values[4][9];
			newFaceValues[3][4]=values[5][9];

			// Dessous
			newFaceValues[4][1]=values[6][5];
			newFaceValues[4][2]=values[7][5];
			newFaceValues[4][3]=values[8][5];
			break;
			
		case 5:
			line = 1;
			column = 3;
			
			// Dessus
			newFaceValues[0][1]=values[0][5];
			newFaceValues[0][2]=values[0][4];
			newFaceValues[0][3]=values[0][3];
			
			// Gauche
			newFaceValues[1][0]=values[3][8];
			newFaceValues[2][0]=values[4][8];
			newFaceValues[3][0]=values[5][8];

			// Droite
			newFaceValues[1][4]=values[3][0];
			newFaceValues[2][4]=values[4][0];
			newFaceValues[3][4]=values[5][0];

			// Dessous
			newFaceValues[4][1]=values[8][5];
			newFaceValues[4][2]=values[8][4];
			newFaceValues[4][3]=values[8][3];
			break;

		case 6:
			line = 2;
			column = 1;
			
			// Dessus
			newFaceValues[0][1]=values[5][3];
			newFaceValues[0][2]=values[5][4];
			newFaceValues[0][3]=values[5][5];
			
			// Gauche
			newFaceValues[1][0]=values[5][2];
			newFaceValues[2][0]=values[5][1];
			newFaceValues[3][0]=values[5][0];

			// Droite
			newFaceValues[1][4]=values[5][6];
			newFaceValues[2][4]=values[5][7];
			newFaceValues[3][4]=values[5][8];

			// Dessous
			newFaceValues[4][1]=values[5][11];
			newFaceValues[4][2]=values[5][10];
			newFaceValues[4][3]=values[5][9];
			break;
			
		default:
			break;
		}
		
		// Copie de la face dans le tableau des valeurs
		for(int i=1;i<=3;i++){
			for(int j=1; j<=3;j++){
				newFaceValues[i][j]=values[line*3+i-1][column*3+j-1];
			}
		}

		ColorCube valueFaceTemp1[][] = new ColorCube[5][5];
		if(rotation>=1){
			// Pivotage de la face dans un tableau temporaire(sens horaire)
			for(int i=0;i<=4;i++){
				for(int j=0; j<=4;j++){
					valueFaceTemp1[j][(i-2)*(-1)+2]=newFaceValues[i][j];
				}
			}
			
			newFaceValues=valueFaceTemp1;
			if(rotation==2){
				ColorCube valueFaceTemp2[][] = new ColorCube[5][5];
				// Pivotage de la face dans un tableau temporaire(sens horaire)
				for(int i=0;i<=4;i++){
					for(int j=0; j<=4;j++){
						valueFaceTemp2[j][(i-2)*(-1)+2]=newFaceValues[i][j];
					}
				}
				newFaceValues=valueFaceTemp2;
			}
		}else{
			// Pivotage de la face dans un tableau temporaire(sens trigonométrique)
			for(int i=0;i<=4;i++){
				for(int j=0; j<=4;j++){
					valueFaceTemp1[i][j]=newFaceValues[j][(i-2)*(-1)+2];
				}
			}
			newFaceValues=valueFaceTemp1;
		}
		
		// Copie de la face dans le tableau des valeurs
		for(int i=0;i<=2;i++){
			for(int j=0; j<=2;j++){
				values[line*3+i][column*3+j]=newFaceValues[i+1][j+1];
			}
		}
		
		switch(face){
		case 1:
			// Dessus
			values[3][11]=newFaceValues[0][1];
			values[3][10]=newFaceValues[0][2];
			values[3][9]=newFaceValues[0][3];
            
			// Gauche
			values[3][0]=newFaceValues[1][0];
			values[3][1]=newFaceValues[2][0];
			values[3][2]=newFaceValues[3][0];
            
			// Droite
			values[3][8]=newFaceValues[1][4];
			values[3][7]=newFaceValues[2][4];
			values[3][6]=newFaceValues[3][4];
            
			// Dessous
			values[3][3]=newFaceValues[4][1];
			values[3][4]=newFaceValues[4][2];
			values[3][5]=newFaceValues[4][3];
			
			break;
		
		case 2:
			// Dessus
			values[0][3]=newFaceValues[0][1];
			values[1][3]=newFaceValues[0][2];
			values[2][3]=newFaceValues[0][3];
			
			// Gauche
			values[3][11]=newFaceValues[1][0];
			values[4][11]=newFaceValues[2][0];
			values[5][11]=newFaceValues[3][0];
            
			// Droite
			values[3][3]=newFaceValues[1][4];
			values[4][3]=newFaceValues[2][4];
			values[5][3]=newFaceValues[3][4];
            
			// Dessous
			values[8][3]=newFaceValues[4][1];
			values[7][3]=newFaceValues[4][2];
			values[6][3]=newFaceValues[4][3];
			break;
			
		case 3:
			// Dessus
			values[2][3]=newFaceValues[0][1];
			values[2][4]=newFaceValues[0][2];
			values[2][5]=newFaceValues[0][3];
			
			// Gauche
			values[3][2]=newFaceValues[1][0];
			values[4][2]=newFaceValues[2][0];
			values[5][2]=newFaceValues[3][0];
            
			// Droite
			values[3][6]=newFaceValues[1][4];
			values[4][6]=newFaceValues[2][4];
			values[5][6]=newFaceValues[3][4];
            
			// Dessous
			values[6][3]=newFaceValues[4][1];
			values[6][4]=newFaceValues[4][2];
			values[6][5]=newFaceValues[4][3];
			break;
			
		case 4:
			// Dessus
			values[2][5]=newFaceValues[0][1];
			values[1][5]=newFaceValues[0][2];
			values[0][5]=newFaceValues[0][3];
			
			// Gauche
			values[3][5]=newFaceValues[1][0];
			values[4][5]=newFaceValues[2][0];
			values[5][5]=newFaceValues[3][0];
            
			// Droite
			values[3][9]=newFaceValues[1][4];
			values[4][9]=newFaceValues[2][4];
			values[5][9]=newFaceValues[3][4];
            
			// Dessous
			values[6][5]=newFaceValues[4][1];
			values[7][5]=newFaceValues[4][2];
			values[8][5]=newFaceValues[4][3];
			break;
			
		case 5:
			// Dessus
			values[0][5]=newFaceValues[0][1];
			values[0][4]=newFaceValues[0][2];
			values[0][3]=newFaceValues[0][3];
			
			// Gauche
			values[3][8]=newFaceValues[1][0];
			values[4][8]=newFaceValues[2][0];
			values[5][8]=newFaceValues[3][0];
            
			// Droite
			values[3][0]=newFaceValues[1][4];
			values[4][0]=newFaceValues[2][4];
			values[5][0]=newFaceValues[3][4];
            
			// Dessous
			values[8][5]=newFaceValues[4][1];
			values[8][4]=newFaceValues[4][2];
			values[8][3]=newFaceValues[4][3];
			break;

		case 6:
			// Dessus
			values[5][3] =newFaceValues[0][1];
			values[5][4] =newFaceValues[0][2];
			values[5][5] =newFaceValues[0][3];
			
			// Gauche
			values[5][2] =newFaceValues[1][0];
			values[5][1] =newFaceValues[2][0];
			values[5][0] =newFaceValues[3][0];
            
			// Droite
			values[5][6] =newFaceValues[1][4];
			values[5][7] =newFaceValues[2][4];
			values[5][8] =newFaceValues[3][4];
            
			// Dessous
			values[5][11]=newFaceValues[4][1];
			values[5][10]=newFaceValues[4][2];
			values[5][9] =newFaceValues[4][3];
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Fonction permettant de retourner le tableau de valeur des faces
	 * @return Le tableau des couleurs constituant le rubik's cube
	 */
	public ColorCube[][] getValues(){
		return(values);
	}
	
	/**
	 * Fonction retournant un tableau contenant les différentes couleurs d'une face
	 * @param face L'identifiant de la face à retourner
	 * @return Les valeurs d'une face
	 */
	public ColorCube[][] getValuesFace(int face){
		//Création de la variable qui sera retournée
		ColorCube[][] valuesFace = new ColorCube[3][3];
		
		if(face>1 && face<6){
			//Face 2,3,4 et 5 -> respectivement valeurs 0,1,2,3 dans l'algo
			face-=2;
			// Copie de la face dans le tableau des valeurs
			for(int i=0;i<=2;i++){
				for(int j=0; j<=2;j++){
					valuesFace[i][j] = values[3+i][face*3+j];
				}
			}
		} else {
			//Face 1 et 6 -> respectivement valeurs 0 et 2 dans l'algo
			face--;
			face/=5;
			face*=2;
			// Copie de la face dans le tableau des valeurs
			for(int i=0;i<=2;i++){
				for(int j=0; j<=2;j++){
					valuesFace[i][j] = values[face*3+i][3+j];
				}
			}
		}
		
		return(valuesFace);
	}
	
	/**
	 * Paramètres les valeurs du cube
	 * @param newValues Nouvelles valeurs pour la face
	 */
	public void setValues(ColorCube[][] newValues){
		values = newValues;
	}
	
	/**
	 * Paramètres les valeurs du cube pour une face
	 * @param face Numéro de la face à changer
	 * @param newValuesFace Nouvelles valeurs pour la face
	 */
	public void setValuesFace(int face, ColorCube[][] newValuesFace){
		if(face>1 && face<6){
			//Face 2,3,4 et 5 -> respectivement valeurs 0,1,2,3 dans l'algo
			face-=2;
			// Copie de la face dans le tableau des valeurs
			for(int i=0;i<=2;i++){
				for(int j=0; j<=2;j++){
					values[3+i][face*3+j] = newValuesFace[i][j];
				}
			}
		} else {
			//Face 1 et 6 -> respectivement valeurs 0 et 2 dans l'algo
			face--;
			face/=5;
			face*=2;
			// Copie de la face dans le tableau des valeurs
			for(int i=0;i<=2;i++){
				for(int j=0; j<=2;j++){
					values[face*3+i][3+j] = newValuesFace[i][j];
				}
			}
		}
	}
	
	/**
	 * Fonction permettant de mélanger le cube aléatoirement.
	 * @param nbMovement Nombre de mouvements aléatoire à effectuer
	 */
	public void mix(int nbMovement){
		for(int i=nbMovement;i>=0;i--){
			this.turn((int)(1+Math.random()*6),1);
		}
	}
	
	/**
	 * Méthode privé la matrice de conversion des couleurs pour l'algorythme de Kociemba
	 * @return Tableau de conversion de couleurs
	 */
	private ColorCube[] matrixOfConversion(){
		/*
		blue	(1,"U"),
		orange	(2,"L"),
		white	(3,"F"),
		red		(4,"R"),
		yellow	(5,"B"),
		green	(6,"D"),
		black	(7,"E");
		*/
		ColorCube[] mConvert = new ColorCube[7];
		
		/*
		 * Vérification dans le cube que le blanc est en haut et sinon convertir la couleur à la place en blanc
		 * donc convert[cube.color.valeur] = 
		 */
		
		
		//Création du tableau de valeurs
		for (int i = 1; i<7;i++){
			//System.out.println(i+" - "+ColorCube.getColor(i)+" - "+getValuesFace(i)[1][1].getValue());
			mConvert[getValuesFace(i)[1][1].getValue()] = ColorCube.getColor(i);
			//System.out.println(ColorCube.getColor(i)+"\t\t"+getValuesFace(i)[1][1].getValue()+"\t\t"+convert[getValuesFace(i)[1][1].getValue()]+"\t\t"+getValuesFace(i)[1][1]);
			//System.out.println(mConvert[getValuesFace(i)[1][1].getValue()]);
		}
		
		return mConvert;
	}
	
	/**
	 * Méthode permettant de retourner les valeurs du cube pour l'algo de résolution 
	 * @return String des différentes valeurs des faces
	 */
	public String listColorToSolve(){
		
		ColorCube[] mConvert = matrixOfConversion();
		
		String theCubeInString = new String();
		// Attention ordre d'ajout important, ne pas changer
		theCubeInString += listFaceColor(1,mConvert);
		theCubeInString += listFaceColor(4,mConvert);
		theCubeInString += listFaceColor(3,mConvert);
		theCubeInString += listFaceColor(6,mConvert);
		theCubeInString += listFaceColor(2,mConvert);
		theCubeInString += listFaceColor(5,mConvert);
		
		return theCubeInString;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public ColorCube[][] toColorCube(int data[][]){
		ColorCube temp[][] = new ColorCube[3][3];
		for(int i=0;i<=2;i++){
			for(int j=0;j<=2;j++){
				temp[i][j]=ColorCube.getColor(data[i][j]);
			}
		}
		
		return temp;
	}
	
	/**
	 * Méthode privé retournant les valeurs d'une face en format chaine
	 * @param face Numéro de la face
	 * @return Valeurs de la face en format string
	 */
	private String listFaceColor(int face, ColorCube[] matrixOfConversion){
		ColorCube[][] valuesFace = getValuesFace(face);
		String listeColor = new String();
		for(int i=0; i<=2; i++){
			for(int j=0; j<=2; j++){
				//System.out.println(i+" - "+j+" - "+valuesFace[i][j].getValue()+" - "+matrixOfConversion[valuesFace[i][j].getValue()]);
				listeColor += matrixOfConversion[valuesFace[i][j].getValue()].colorCubeToColorString();
			}
		}
		return listeColor;
	}
	

	public void setTemp(){
		/*
		777565777777
		777624777777
		777254777777
		446326512131
		341563124514
		236323112155
		777464777777
		777232777777
		777663777777
		*/
		int[][] data = new int[3][3];
		ColorCube temp[][];
		
		//1
		data = new int[][]{
			{5,6,5},
			{6,5,4},
			{2,5,4}
			};
		temp = toColorCube(data);
		this.setValuesFace(1,temp);
		
		//2
		data = new int[][]{
			{4,4,6},
			{3,4,1},
			{2,3,6}
			};
		temp = toColorCube(data);
		this.setValuesFace(2,temp);
		
		//3
		data = new int[][]{
			{3,2,6},
			{5,6,3},
			{3,2,3}
			};
		temp = toColorCube(data);
		this.setValuesFace(3,temp);
		
		//4
		data = new int[][]{
			{5,1,2},
			{1,2,4},
			{1,1,2}
			};
		temp = toColorCube(data);
		this.setValuesFace(4,temp);
		
		//5
		data = new int[][]{
			{1,3,1},
			{5,1,4},
			{1,5,5}
			};
		temp = toColorCube(data);
		this.setValuesFace(5,temp);
		
		//6
		data = new int[][]{
			{4,6,4},
			{2,3,2},
			{6,6,3}
			};
		temp = toColorCube(data);
		this.setValuesFace(6,temp);
	}
	
	/**
	 * Méthode permettant de faire une rotation à un cube
	 * 
	 * Informations supplémentaires sur l'axe de rotation:<br>
	 * Axe 1: axe passant par face1+6<br>
	 * Axe 2: axe passant par face3+5<br>
	 * Axe 3: axe passant pas face2+4<br>
	 * <br>
	 * @param axe l'axe de rotation du cube<br>
	 * 				Axe 1: axe passant par face1+6<br>
	 * 				Axe 2: axe passant par face3+5<br>
	 * 				Axe 3: axe passant pas face2+4
	 * @param sens le sens de rotation
	 */
	public void rotateComplete(int axe, int sens){
		int j=1;
		int l=1;
		switch(sens){
		case 2:
			l=2;
			break;
		case -1:
			l=3;
			break;
		}
		
		switch (axe){
		case 1:
			for(int m=0;m<l;m++){
				this.turn(1,-1);
				this.turn(6,1);
				
				for(int k=j;k>0;k--){
					//save of the data
					ColorCube[] save = new ColorCube[15];
					for(int i=0;i<=11;i++){
						save[i+3]=values[4][i];
					}
					for(int i=0;i<=2;i++){
						values[4][i]=save[12+i];
					}
					for(int i=3;i<=11;i++){
						values[4][i]=save[i];
					}
				}
			}
			break;
		case 2:
			for(int m=0;m<l;m++){
				this.turn(2,1);
				this.turn(4,-1);
				
				for(int k=j;k>0;k--){
					//save of the data
					ColorCube[] save = new ColorCube[15];
					//face 1/3/6 saving
					for(int i=0;i<=8;i++){
						save[i+3]=values[i][4];
					}
					//face 5 saving
					for(int i=0;i<=2;i++){
						int inv = i;
						inv--;
						inv*=-1;
						inv++;
						save[i+12]=values[3+inv][10];
					}
					System.out.print("\n");
					//face1 setting
					for(int i=0;i<=2;i++){
						values[i][4]=save[12+i];
					}
					//face 3/5/6 setting
					for(int i=3;i<=8;i++){
						values[i][4]=save[i];
					}
					//face 6 setting
					for(int i=9;i<=11;i++){
						int inv = i;
						inv-=10;
						inv*=-1;
						inv++;
						values[3+inv][10]=save[i];
					}
				}
			}
			break;
		case 3:
			for(int m=0;m<l;m++){
				this.turn(3,1);
				this.turn(5,-1);
				j=3;
				
				for(int k=j;k>0;k--){
					//save of the data
					ColorCube[] save = new ColorCube[15];
					//face 1/6 saving
					for(int i=0;i<=2;i++){
						int inv = i;
						inv--;
						inv*=-1;
						inv++;
						save[i+3]=values[1][3+i];
						save[i+9]=values[7][3+inv];
					}
					//face 3/5 saving
					for(int i=0;i<=2;i++){
						int inv = i;
						inv--;
						inv*=-1;
						inv++;
						save[i+6]=values[3+i][7];
						save[i+12]=values[3+inv][1];
					}
					for (int zut =0;zut<=14;zut++)
						System.out.print(save[zut]);
					System.out.print("\n");
					//face1+6 setting
					for(int i=0;i<=2;i++){
						int inv = i;
						inv--;
						inv*=-1;
						inv++;
						values[1][3+i]=save[i+6];
						values[7][3+inv]=save[i+12];
					}
					//face 2/4 setting
					for(int i=0;i<=2;i++){
						int inv = i;
						inv--;
						inv*=-1;
						inv++;
						values[3+i][7]=save[i+9];
						values[3+inv][1]=save[i+3];
					}
				}
			}
			break;
		}
	}
	
	/**
	 * Fonction retournant le rubik's cube sous forme de texte
	 */
	public String toString(){
		StringBuilder line = new StringBuilder();
		//Affichage de toute le tableau
		for(int i=0;i<=8;i++){
			for(int j=0;j<=11;j++){
				line.append(values[i][j].getValue());
			}
			line.append("\n");
		}
		return line.toString();
	}
}
