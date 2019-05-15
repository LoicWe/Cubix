package objects;
 
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
 
/**
 * @author Loïc Wermeille
 *
 */
public class Analyse {
    /**
     * @param image Matrice contenant l'image à traiter
     * @return L'image avec les nouvelles données
     */
    public static DataScanned squareAdding(Mat image){
         
        //Récupération de la listes des points pour les rubik's cube
        int width = (int) image.size().width;
        int height = (int) image.size().height;
        Point[] tablePoints = getListPoints(width, height);
         
        //Variables de boucles
        double[] colour = new double[3];

        colour[0]=0;
        colour[1]=0;
        colour[2]=0;
        
        Mat subMate1;
        ColorCube[][] colors = new ColorCube[3][3];
         
        for(int k=0; k<17;k+=2){
            //Récupération de chaque carré l'un après l'autre
            subMate1 = image.submat(new Rect(tablePoints[k],tablePoints[k+1]));
             
            //Variables temp pour la seconde boucle
            double r=0,g=0,b=0;
            int nb=0;
            //Parcours de tous les pixels
            for(int i=0; i<subMate1.width();i++){
                for(int j=0;j<subMate1.height();j++){
                    colour = subMate1.get(j,i);
                    
                    r+=colour[2];
                    g+=colour[1];
                    b+=colour[0];
                    nb++;
                }
            }
            
            //Moyenne de la couleur
            colour[0]=b/nb;
            colour[1]=g/nb;
            colour[2]=r/nb;
            
            //Stokage de la couleur au point (0,0)
            subMate1.put(0,0,colour);
            
            //Conversion en HSV
            Imgproc.cvtColor(subMate1, subMate1, Imgproc.COLOR_BGR2HSV);
            
            //Récupération en HSV
            colour = subMate1.get(0,0);
            
            //Détection de la couleur
            ColorCube color = detectColor(colour[0], colour[1], colour[2]);
            
            //Reconversion en RGB
            Imgproc.cvtColor(subMate1, subMate1, Imgproc.COLOR_HSV2BGR);
            
            //Ajout du carré de sélection sur le cube
            Imgproc.rectangle(image,
                    tablePoints[k],
                    tablePoints[k+1], 
                    color.getColorBGR());
            
            int i = (k/2)/3;
            int j = (k/2)%3;
            colors[i][j] = color;
        }
        return new DataScanned(image,colors);
    }
     
     
     
    /**
     * Transforme une couleur hsv en un énuméré de type couleur
     * @param hue teinte de la couleur
     * @param saturation saturation de la couleur
     * @param value luminosité de la couleur
     * @return un énuméré de type ColorCube de la couleur détecté
     */
    public static ColorCube detectColor(double hue, double saturation, double value){
        /*
        Couleur blanche --> 255  255 255
        Couleur rouge   --> 255  0   0
        Couleur vert    --> 0    255 0
        Couleur jaune   --> 255  255 0
        Couleur orange  -->  255 128 0
        Couleur bleu    -->  0   0   255
         
        Couleur blanche --> x    0   240
        Couleur rouge   --> 0
        Couleur vert    --> 80
        Couleur jaune   --> 40
        Couleur orange  -->  20
        Couleur bleu    -->  160
         
        0   64   128   192   255
        /**/
         
        //System.out.println(h+" \t"+s+"  \t"+v);
         
        /************************* V.2 *************************/
        //Blanc
        if(saturation<60&&value>85||saturation<70&&value>180||saturation<20&&value>=40||saturation<80&&value>90){
            //System.out.println("White");
            return ColorCube.white;
        }
        // Red
        if(hue>170||hue<=3||hue<=14&&(saturation+value<200)){
            //System.out.println("Red");
            return ColorCube.red;
        }
        //Orange
        if(hue<=13&&!(hue<=3||hue<=14&&(saturation+value<200))){
            //System.out.println("Orange");
            return ColorCube.orange;
        }
 
        //Yellow
        if(hue>=15&&hue<35){
            //System.out.println("Yellow");
            return ColorCube.yellow;
        }
        //Green
        if(hue>35&&hue<75){
            //System.out.println("Green");
            return ColorCube.green;
        }
 
        //Blue
        if(hue>95&&hue<160){
            //System.out.println("Blue");
            return ColorCube.blue;
        }
         
        return ColorCube.black;
    }
     
    /**
     * Calcul des 18 points principaux du rubik's cube dans l'image
     * @param width Largeur de l'image
     * @param height Hauteur de l'image
     * @return
     */
    private static Point[] getListPoints(int width, int height){
        int y = height;
        int x = height;
        int z = (width-x)/2;
         
        int x1, x2, x3, x4, x5, x6, x7, x8;
        int y1, y2, y3, y4, y5, y6, y7, y8;
         
        x1=z+x/12;
        x2=z+x/6+x/24;
        x3=z+x/3-x/12;
        x4=x1+x/3;
        x5=x3+x/3;
        x6=x1+2*x/3;
        x7=2*z+x-x2;
        x8=x3+2*x/3;
         
        y1=y/12;
        y2=y/6+y/24;
        y3=y/3-y/12;
        y4=y1+y/3;
        y5=y3+y/3;
        y6=y1+2*y/3;
        y7=y-y2;
        y8=y3+2*y/3;
         
        Point[] tablePoints = new Point[18];
        // carré 1
        tablePoints[17] = new Point(x1,y1);
        tablePoints[16] = new Point(x3,y3);
        // carré 2
        tablePoints[15] = new Point(x4,y2);
        tablePoints[14] = new Point(x5,y3);
        // carré 3
        tablePoints[13] = new Point(x6,y1);
        tablePoints[12] = new Point(x8,y3);
        // carré 4
        tablePoints[11] = new Point(x2,y4);
        tablePoints[10] = new Point(x3,y5);
        // carré 5
        tablePoints[9] = new Point(x4,y4);
        tablePoints[8] = new Point(x5,y5);
        // carré 6
        tablePoints[7] = new Point(x6,y4);
        tablePoints[6] = new Point(x7,y5);
        // carré 7
        tablePoints[5] = new Point(x1,y6);
        tablePoints[4] = new Point(x3,y8);
        // carré 8
        tablePoints[3] = new Point(x4,y6);
        tablePoints[2] = new Point(x5,y7);
        // carré 9
        tablePoints[1] = new Point(x6,y6);
        tablePoints[0] = new Point(x8,y8);
         
        return tablePoints;
    }
}