/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.util.Timer;
import java.util.TimerTask;
//import java.util.logging.Level;
//import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class reproductor implements Runnable {
    
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
        if (!reproduciendo ){
           //System.out.println("bfhsoftware.sonidoambiental.reproductor.reproducir()");
        String proximotema = com.proximotema();
        empezar(proximotema);
        }
    }
    public void empezar (String proximotema){
        //reproduciendo = true;
        System.out.println("intenta reproducir");  
    }
    public void parar(){
        reproduciendo = false;
    }
    public void pausa(){
        reproduciendo = false;
        pausado = true;
    }
    //public abstract void empezar()
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
    public reproductor (){
        System.out.println("bfhsoftware.sonidoambiental.reproductor.<init>()");
    }
    reproductor getInstance() {
        System.out.println("bfhsoftware.sonidoambiental.reproductor.getInstance()");
        if (main.isandroid()) {
            return new reproducirAndroid();
        } else {
            //System.out.println("bfhsoftware.sonidoambiental.reproductor.getInstance()");
            return new ReproducirJava();
        }
    }
    @Override
    public void run (){
        
        if (verificador ==null) {
            
            verificador = new Timer();}
        verificador.scheduleAtFixedRate(controlado, 0, 1000);
        //el error esta a continuacion, un bucle que vuelve loca a la memoria
        
        while (!muerto && ordendereproducir) {
            reproducir();
             //System.out.println( "reproducir");
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
    
    
}

 