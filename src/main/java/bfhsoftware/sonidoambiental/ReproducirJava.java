/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

//import static bfhsoftware.sonidoambiental.reproductor.pausado;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 *
 * @author bfhsoftware
 */
public class ReproducirJava extends reproductor {
    PlaybackListener escuchar = new escuchaevento();
    static AdvancedPlayer reproductor=null;
    public ReproducirJava() {
        /* Llamamos al constructor padre */
        super();
        /* try {
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
        }*/
    }
    
    @Override
    public void empezar(String proximotema) {
        /* Llamamos a la implementación común de comienzo */
        super.empezar(proximotema);
         System.out.println("bfhsoftware.sonidoambiental.ReproducirJava.empezar()");
        if ((reproductor == null)){
           
            try {
                if (proximotema.equals("")){
                    ordendereproducir = false;
                    reproduciendo = false;
                    System.out.println("sin archivos para reproducir...");
                }else{
                    reproductor = new AdvancedPlayer(new FileInputStream(proximotema));
                    reproductor.setPlayBackListener(escuchar);
                    temaactual = com.Ultimotema();
                    
                }
                
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
                Logger.getLogger(reproductor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                System.out.println(ex);
                Logger.getLogger(ReproducirJava.class.getName()).log(Level.SEVERE, null, ex);
            }}
        reproduciendo = (reproductor != null);
        
        /* } while (reproductor == null && ordendereproducir);
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
        reproductor.stop();
        reproductor = null;
        /* Implementamos aquí el método en java se */
    }
    
    @Override
    public void pausa() {
        /* Llamamos a la implementación común de pausa */
        super.pausa();
        reproductor.stop();
        pausado=true;
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
            System.out.println("bfhsoftware.sonidoambiental.ReproducirJava.escuchaevento.playbackFinished()");
            parar();
            reproducir();
        }
    }
    
    /* No es necesario implementar getNumCancion ni getPosicion */
}