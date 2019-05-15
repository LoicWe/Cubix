package objects;

import org.opencv.core.Scalar;

/**
 * <b>Enuméré représentant les couleurs dans le rubik's cube</b>
 * 
 * @author	Loïc Wermeille
 * @since	30.12.2015
 * @version	1.0
 */

public enum ColorCube {
	/** Blue*/
	blue	(1,"U",new Scalar(120,255,255)	,new Scalar(255,0,0),"Blue"),
	/** Orange*/
	orange	(2,"L",new Scalar(15,255,255)	,new Scalar(0,140,255),"Orange"),
	/** White*/
	white	(3,"F",new Scalar(0,0,255)		,new Scalar(255,255,255),"White"),
	/** Red*/
	red		(4,"R",new Scalar(0,255,255)	,new Scalar(0,0,255),"Red"),
	/** Yellow*/
	yellow	(5,"B",new Scalar(30,255,255)	,new Scalar(0,255,255),"Yellow"),
	/** Green*/
	green	(6,"D",new Scalar(50,255,255)	,new Scalar(0,255,0),"Green"),
	/** Black*/
	black	(7,"E",new Scalar(0,0,0)		,new Scalar(0,0,0),"Black");
	
	//Valeur stockant l'id de la couleur
	private int idColor = 0;
	private String posColor = "";
	private Scalar hsvColor = new Scalar(0,0,0);
	private Scalar bgrColor = new Scalar(0,0,0);
	private String strColor = "";
	
	/**
	 * Constructeur autoParamétré (code numérique et code caractere)
	 * 
	 * @param value Identifiant de la couleur
	 * @param colorSet Position de la couleur dans le rubik's cube
	 */
	ColorCube(int value, String colorSet, Scalar colorHSV, Scalar colorRGB, String nameColor){
		idColor = value;
		posColor = colorSet;
		hsvColor = colorHSV;
		bgrColor = colorRGB;
		strColor = nameColor;
	}
	
	/**
	 * Accède au nom de la couleur
	 * @return la valeur sous forme de chaine de caractère
	 */
	public String toString(){
		return strColor;
	}
	
	/**
	 * Accède à identifiant de la couleur
	 * @return L'identifiant de la couleur
	 */
	public int getValue(){
		return this.idColor;
	}
	
	/**
	 * Accède à la position de la couleur sous forme de lettre
	 * @return Le caractère de position de la couleur de la rubik's cube
	 */
	public String colorCubeToColorString(){
		return posColor;
	}
	
	/**
	 * Accède à la couleur hsv correspondante
	 * @return la valeur hsv de la couleur dans un objet Scalar
	 */
	public Scalar getColorHsv(){
		return hsvColor;
	}
	
	/**
	 * Accède à la couleur hsv correspondante
	 * @return la valeur rgb de la couleur dans un objet Scalar
	 */
	public Scalar getColorBGR() {
		return bgrColor;
	}
	
	/**
	 * Méthode permettant de retourner une couleur par rapport à un identifiant
	 * @param id Identifiant de la couleur
	 * @return La couleur correspondante à l'identifiant
	 */
	public static ColorCube getColor(int id){
		switch (id){
			case 1: return blue;
			case 2: return orange;
			case 3: return white; 
			case 4: return red;
			case 5: return yellow;
			case 6: return green; 
			default: return black;
		}
	}
}