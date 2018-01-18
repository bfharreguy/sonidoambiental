/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
//import java.util.logging.Level;
//import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class reproductor implements Runnable {
    private static  boolean pausado = false;
    private static String temaactual ;
    private static Integer posiciondepausa;
    private static boolean ordendereproducir =true;
    
    //private final static Logger LOGGER = Logger.getLogger("reproduccion");
    comunicacion com = new comunicacion();
    volatile boolean muerto = false;
    volatile boolean reproduciendo = false;
    //PlaybackListener escuchar = playbackFinished();
    PlaybackListener escuchar = new escuchaevento();
    static AdvancedPlayer reproductor;
    static Timer verificador;
    static TimerTask controlado = new TimerTask()
    {
        @Override
        public void run()
        {
            System.err.println("si");
        }
    };
    public void reiniciar(){
        reproduciendo = false;
        if (reproductor != null)
            reproductor.close();
        reproductor = null;
    }
    public void reproducir(){
        try {
            if ((reproductor == null) || (! pausado)) {
                do{
                    try {
                        String proximotema = com.proximotema();
                        if (proximotema.equals("")){
                            ordendereproducir = false;
                            reproduciendo = false;
                            break;
                            
                        }else{
                            reproductor = new AdvancedPlayer(new FileInputStream(proximotema));
                            temaactual = com.Ultimotema();
                        }
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(reproductor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (reproductor == null || !ordendereproducir);
                if (reproductor != null){
                    reproductor.setPlayBackListener(escuchar);
                    reproductor.play();
                    reproduciendo = true;}
                // System.out.println(reproductor.toString() + "algo");
            } else if(reproductor != null || pausado || ordendereproducir){
                if (posiciondepausa != 0) {
                    reproductor.play(posiciondepausa, Integer.MAX_VALUE);
                    //reproductor.play();
                    reproduciendo = true;
                } else {
                    reproductor.play();
                    reproduciendo = true;
                }
            }
        }
        catch (JavaLayerException ex) {
            Logger.getLogger(reproductor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run (){
        if (verificador ==null) {
            
            verificador = new Timer();}
        verificador.scheduleAtFixedRate(controlado, 0, 1000);
        while (!muerto || !ordendereproducir) {
            reproducir();
            System.out.println( "reproducir");
        }
    }
    
    public String cancion(){
        return temaactual;
    }
    public String estado (){
        if (reproductor == null) {
            return "Detenido";
        } else {
            return "Reproduciendo";
        }
    }
    class escuchaevento extends PlaybackListener {
        @Override
        public void playbackStarted(PlaybackEvent event){
            //temaactual = event.getSource().toString();
        }
        
        @Override
        public void playbackFinished(PlaybackEvent event) {
            reiniciar();
            reproducir();
            
        }
    }
}
