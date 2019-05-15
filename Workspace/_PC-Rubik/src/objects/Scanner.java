package objects;
 
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
 
import org.kociemba.twophase.Tools;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
 
import exceptions.RobotException;
import exceptions.ScannerException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
 
/**
 * Classe permettant de scanner les différentes faces d'un Rubik's cube
 * @author  Loïc Wermeille
 * @since   24.03.2016
 * @version 1.0
 */
public class Scanner {
     
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive;
     
    private ImageView displayArea;
     
    /**
     * Constructeur du scanner
     * @param displayFrame Zone pour afficher l'image scannée
     */
    public Scanner(ImageView displayFrame){
        displayArea = displayFrame;
    }
     
    /**
     * @return cameraActive
     */
    public boolean cameraActive(){
        return cameraActive;
    }
     
    /**
     * Méthode de démarrage de la saisie de données depuis la caméra 
     * @throws ScannerException
     */
    private void start() throws ScannerException{
        if(!cameraActive)
        {
            // start the video capture
            this.capture.open(1);	//0 ou 1 en fonction du port utilisé pour la caméra
             
            // is the video stream available?
            if (this.capture.isOpened())
            {
                this.cameraActive = true;
                 
                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {
                     
                    @Override
                    public void run()
                    {
                        DataScanned data = grabFrame();
                        Image imageToShow = data.dataImg;
                        displayArea.setImage(imageToShow);
                    }
                };
                 
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                 
            }
            else
            {
                // log the error
                System.err.println("Failed to open the camera connection...");
                throw new ScannerException(1, "Erreur lors de la connexion");
            }
        }
    }
     
    /**
     *  Méthode de fin de la saisie de données depuis la caméra 
     * @throws ScannerException
     */
    private void stop() throws ScannerException{
        if(cameraActive)
        {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            //this.cameraButton.setText("Start Camera");
             
            // stop the timer
            try
            {
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                // log the exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
                throw new ScannerException(1, "Erreur lors de la fermeture de la connection");
            }
             
            // release the camera
            this.capture.release();
        }
    }
     
    /**
     * Méthode gérant le scanage d'une face d'un rbik's cube
     * 
     * @return les données scannées
     * @throws ScannerException Exception spécifique au scanner
     */
    public ColorCube[][] scan() throws ScannerException{
        if(!cameraActive)
        {
            try{
                start();
            }catch(ScannerException e){
                throw new ScannerException(2, "Probleme lors du demarrage de la connection \n "+e.toString());
            }
        }
         
        //Stockage intermédiaire des données dans un rubik's cube
        Rubik rubikAboutToScan = new Rubik();
        recogniseFace(1, rubikAboutToScan);
         
        if(cameraActive)
        {
            try{
                stop();
            }catch(ScannerException e){
                throw new ScannerException(2, "Probleme lors de l'arret de la connection \n "+e.toString());
            }
        }
         
        return rubikAboutToScan.getValuesFace(1);
    }
     
    /**
     * Méthode gérant le scanage complet d'un rubik's cube
     * 
     * @param rubikAboutToScan  Rubik's cube à scanner
     * @param robotToUse        Robot à disposition
     * @throws ScannerException Exception spécifique au scanner
     */
    public void scan(Rubik rubikAboutToScan, Robot robotToUse) throws ScannerException{
        /* ORDRE DES FACES LORS DU SCANNAGE:
         *   6
         *  3412
         *   5
         *
         * VALEURS ORDRES ENVOYÉS:
         *  nombre à trois chiffre:
         *  centaine    -> axe (1=parallèle, 2 perpendiculaire)
         *  dizaine     -> pince (1=première, 2=deuxième, 4=les deux)
         *  unité       -> sens rotation (0=horraire, 1=trygo, 2=1/2 tour)
         *
         * PROCÉDURE DE SCANNAGE
         *  1.  scannage face 1
         *  2.  rotate 90° axe perpendiculaire -> ordre: 241
         *  3.  scannage face 2
         *  4.  rotate 90° axe perpendiculaire -> ordre: 241
         *  5.  scannage face 3
         *  6.  rotate 90° axe perpendiculaire -> ordre: 241
         *  7.  scannage face 4
         *  8.  rotate 90° axe parallèle -> ordre: 140
         *  9.  scannage face 5
         *  10. rotate 180° axe parallèle -> ordre: 142
         *  11. scannage face 6
         */
         
        if(!cameraActive)
        {
            try{
                start();
            }catch(ScannerException e){
                throw new ScannerException(2, "Probleme lors du démarrage de la connection \n "+e.toString());
            }
        }
         
        try {
            //Mise en place de la caméra
            int[][] order = new int[2][2];
            order[0][0]=2;
            order[0][1]=37;
            order[1][0]=0;
            order[1][1]=0;
             
            robotToUse.executeRobotInstructions(order);
             
            recogniseFace(4, rubikAboutToScan);
            robotToUse.execute(240);
            recogniseFace(5, rubikAboutToScan);
            robotToUse.execute(240);
            recogniseFace(2, rubikAboutToScan);
            robotToUse.execute(240);
            recogniseFace(3, rubikAboutToScan);
            robotToUse.execute(141);
            recogniseFace(6, rubikAboutToScan);
            robotToUse.execute(142);
            recogniseFace(1, rubikAboutToScan);
 
            //Déplacement de la caméra
            order[0][1]=38;
            robotToUse.executeRobotInstructions(order);
             
        } catch (RobotException e) {
            throw new ScannerException(2, "Probleme avec le robot lors du scannage \n "+e.toString());
        }
        
        System.out.println(rubikAboutToScan.toString());
        System.out.println(rubikAboutToScan.listColorToSolve());
        
        int test = Tools.verify(rubikAboutToScan.listColorToSolve());
        if(test != 0){
            throw new ScannerException(22, "Un probleme a eu lieu pendant le scannage et le rubik's cube est incorrecte \n ");
        }/**/
         
        if(cameraActive)																													///changement, avant: if(!cameraActive)
        {
            try{
                stop();
            }catch(ScannerException e){
                throw new ScannerException(2, "Probleme lors de l'arret de la connection \n "+e.toString());
            }
        }
    }
     
    /**
     * Méthode gérant le scanage du rubik
     * 
     * @param numFace Numéro de la face à scanner
     * @param theRubik  Rubik's cube à compléter
     * @throws ScannerException Exception spécifique au scanner
     */
    private void recogniseFace(int numFace, Rubik theRubik) throws ScannerException{
        /*
         * ETAPES DE RESOLUTIONS
         *  1. Vérification que la capture de frame est en route
         *  2. Capture d'une frame ou plus
         *  3. Vérification que toutes les données (culeurs) soient correctes
         *  4. Completion du rubik's cube 
         */
         
        if(!cameraActive)
        {
            try{
                start();
            }catch(ScannerException e){
                throw new ScannerException(2, "Probleme lors du demarrage de la connection \n "+e.toString());
            }
        }
         
        //Préparation des données
        ColorCube[][] colors = null;
        DataScanned data = null;
         
        int n = 2;
        //Boucle jusqu'à ce que deux lots de données soient identique
        for(int i=0;i<n;i++){
            //Attente de la connection
            while(grabFrame()==null){}
             
            data = grabFrame();
            if(colors == null){
                colors = data.dataColorCube;
            }else{
                for(int j=0;j<3;j++){
                    for(int k=0;k<3;k++){
                        if(colors[j][k] != data.dataColorCube[j][k]||colors[j][k]==ColorCube.black){
                            System.out.println("Données différentes");
                            i=0;
                        }
                    }
                }
                colors = data.dataColorCube;
            }
        }
        //Modification du rubik's cube
        theRubik.setValuesFace(numFace, colors);
 
        Image imageToShow = data.dataImg;
         
        //Stockage de l'image
        File dir = new File ("c:\\Cubix");
        dir.mkdirs();
         
        Imgcodecs.imwrite( "c:\\Cubix\\"+numFace+".jpg", data.dataMat);
         
        displayArea.setImage(imageToShow);
    }
     
    /**
     * Get a frame from the opened video stream (if any)
     * 
     * @return the {@link Image} to show
     */
    private DataScanned grabFrame()
    {
        // init everything
        Mat frame = new Mat();
        DataScanned data = null;
         
        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);
                 
                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    Imgproc.medianBlur(frame, frame, 25);
                    //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
                     
                    data = Analyse.squareAdding(frame);
                    Mat temp = data.dataMat;
                    //Imgproc.cvtColor(temp, temp, Imgproc.COLOR_HSV2BGR);
                     
                    data.dataImg = mat2Image(temp);
                }
                 
            }
            catch (Exception e)
            {
                // log the (full) error
                System.err.print("ERROR");
                e.printStackTrace();
            }
        }
         
        return data;
    }
     
    /**
     * Convert a {@link Mat} object (OpenCV) in the corresponding {@link Image}
     * for JavaFX
     * 
     * @param frame
     *            the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    private Image mat2Image(Mat frame)
    {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer, according to the PNG format
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}