package objects;

import org.opencv.core.Mat;
import javafx.scene.image.Image;

/**
 * @author Loïc Wermeille
 *
 */
public class DataScanned {
	/**
	 * Tableau de couleurs
	 */
	public ColorCube[][] dataColorCube;
	/**
	 * Mat
	 */
	public Mat dataMat;
	/**
	 * Image fini
	 */
	public Image dataImg;
	
	/**
	 * @param frame image de type Mat
	 * @param colors couleur de type ColorCube
	 */
	public DataScanned(final Mat frame, final ColorCube[][] colors) {
		dataColorCube = colors;
		dataMat = frame;
	}
	

}
