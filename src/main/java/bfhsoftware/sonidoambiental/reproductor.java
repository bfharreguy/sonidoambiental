/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import android.media.MediaPlayer;
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
public abstract class reproductor implements Runnable {
    protected static  boolean pausado = false;
    protected static String temaactual ;
    protected static Integer posiciondepausa;
    protected static boolean ordendereproducir =true;
    
    //private final static Logger LOGGER = Logger.getLogger("reproduccion");
    comunicacion com = new comunicacion();
    
    volatile boolean muerto = false;
    volatile boolean reproduciendo = false;
    //PlaybackListener escuchar = playbackFinished();
    public void reproducir(){
        
    }
    void empezar (String proximotema){
        reproduciendo = true;
    }
    static Timer verificador;
    static TimerTask controlado = new TimerTask()
    {
        @Override
        public void run()
        {
            //System.err.println("si");
        }
    };
    /* public void reiniciar(){
    
    }*/
    
    
    @Override
    public void run (){
        if (verificador ==null) {
            
            verificador = new Timer();}
        verificador.scheduleAtFixedRate(controlado, 0, 1000);
        //el error esta a continuacion, un bucle que vuelve loca a la memoria
        
        while (!muerto && ordendereproducir) {
            reproducir();
            // System.out.println( "reproducir");
        }
    }
    
    public String cancion(){
        return temaactual;
    }
    public String estado (){
        if (reproduciendo) {
            return "Detenido";
        } else {
            return "Reproduciendo";
        }
    }
    reproductor getInstance() {
        if (main.isandroid()) {
            return new reproducirAndroid();
        } else {
            return new reproducirJava();
        }
    }
    
}
public class reproducirAndroid extends reproductor {
    private static MediaPlayer mp;
    public reproducirAndroid() {
        /* Llamamos al constructor padre */
        super();
    }
    
    @Override
    public void empezar() {
        /* Llamamos a la implementación común de comienzo */
        super.empezar();
        
        /* Implementamos aquí el método en android */
    }
    
    @Override
    public void parar() {
        /* Llamamos a la implementación común de parada */
        super.parar();
        /* Implementamos aquí el método en android */
    }
    
    @Override
    public void pausa() {
        /* Llamamos a la implementación común de pausa */
        super.pausa();
        /* Implementamos aquí el método en android */
    }
    
    /* No es necesario implementar getNumCancion ni getPosicion */
}
 public class reproducirJava extends reproductor {
    PlaybackListener escuchar = new escuchaevento();
    static AdvancedPlayer reproductor;
    public reproducirJava() {
        /* Llamamos al constructor padre */
        super();
        try {
            if ((reproductor == null) && (! pausado)) {
                //System.out.println("proximotema:''");
                do{
                    try {
                        String proximotema = com.proximotema();
                        //      System.out.println("proximotema:'" + proximotema + "'");
                        if (proximotema.equals("")){
                            ordendereproducir = false;
                            reproduciendo = false;
                            System.out.println("tratando de detener");
                            break;
                            
                        }else{
                            reproductor = new AdvancedPlayer(new FileInputStream(proximotema));
                            temaactual = com.Ultimotema();
                        }
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(reproductor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (reproductor == null && ordendereproducir);
                
                if (reproductor != null){
                    reproductor.setPlayBackListener(escuchar);
                    reproductor.play();
                    reproduciendo = true;}
                // System.out.println(reproductor.toString() + "algo");
            } else if(reproductor != null && pausado && ordendereproducir){
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
    public void empezar() {
        /* Llamamos a la implementación común de comienzo */
        super.empezar(proximotema);
        try {
            if ((reproductor == null) && (! pausado)) {
                reproductor = new AdvancedPlayer(new FileInputStream(proximotema));
            }
            /* Implementamos aquí el método en java se */
        }
        
        @Override
        public void parar() {
        /* Llamamos a la implementación común de parada */
        super.parar();
        /* Implementamos aquí el método en java se */
    }
        
        @Override
        public void pausa() {
        /* Llamamos a la implementación común de pausa */
        super.pausa();
        /* Implementamos aquí el método en java se */
    }
        private void reiniciar(){
            reproduciendo = false;
            if (reproductor != null)
                reproductor.close();
            reproductor = null;
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
        
        /* No es necesario implementar getNumCancion ni getPosicion */
}