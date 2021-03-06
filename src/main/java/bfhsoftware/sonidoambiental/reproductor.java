/*
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * @author Usuario
 */
public class reproductor implements Runnable {
    protected static Long tiempointerno = (System.currentTimeMillis() / 1000L);
    protected static boolean pausado = false;
    protected static String temaactual = "";
    protected static Integer posiciondepausa;
    protected static boolean ordendereproducir = true;
    protected static boolean publicidadactivada = true;
    protected static String nombrepublicidadactual = "";
    public static Boolean iniciarreproduccion = false;
    //private final static Logger LOGGER = Logger.getLogger("reproduccion");
    comunicacion com = new comunicacion();

    static boolean muerto = false;
    static boolean reproduciendo = false;

    //PlaybackListener escuchar = playbackFinished();
    public void reproducir() {
        if (!reproduciendo) {
            //subir el volumen al mango y sacar silencio
            audio.setMasterOutputVolume(1);
            audio.setMasterOutputMute(false);
            //System.out.println("bfhsoftware.sonidoambiental.reproductor.reproducir()");
            String proximotema = com.proximotema();
            temaactual = (Paths.get(proximotema)).getFileName().toString();
            empezar(proximotema);
            //System.out.println("Reproduciendo: "+proximotema);
        }
    }

    public void empezar(String proximotema) {
/*        if (proximotema.equals("")){

        }*/
        //reproduciendo = true;       
    }

    public void parar() {
        reproduciendo = false;
        ordendereproducir = false;
        pausado = false;
    }

    public void pausa() {
        reproduciendo = false;
        pausado = true;
    }

    //public abstract void empezar()
    static Timer verificador;
    TimerTask controlado = new TimerTask() {
        @Override
        public void run() {
            //System.err.println("si");
            //System.out.println(System.currentTimeMillis() / 1000L);
            comunicacion com = new comunicacion();
            if ((((System.currentTimeMillis() / 1000L) - (tiempointerno)) > 20) || iniciarreproduccion) {
                tiempointerno = (System.currentTimeMillis() / 1000L);
                iniciarreproduccion=false;
                if (!reproduciendo)
                    if (!com.verificarcanciones().sinarchivos) {
                        if (ordendereproducir) {
                            reproducir();
                            //System.out.println( "reproducir");
                        }
                    }
            }
            if ((((System.currentTimeMillis() / 1000L) - (Long.valueOf((String) com.opcion("ultimavezqueseverificaalbum", Objects.toString((System.currentTimeMillis() / 1000L), null))).longValue())) > 900)) {
               /* if (verificardirectorio )
                    System.out.println("verificar directorio por subida de archivos");*/
                com.verificarmusica();
                System.out.println(reproduciendo);
                if (!reproduciendo) {
                    ordendereproducir = true;
                }
            }
        }
    };
       //private static reproductor ourInstance = new reproductor();

    static reproductor getInstance() {

        //System.out.println("bfhsoftware.sonidoambiental.reproductor.getInstance()");
        if (main.isandroid()) {
            //System.out.println("reproducir android");
            //return new Class.forName("bfhsoftware.sonidoambiental.Sonidoambiental");
//reproducirAndroid();
            return new ReproductorAndroid();
        } else {
            //System.out.println("reproducir java");
            //System.out.println("bfhsoftware.sonidoambiental.reproductor.getInstance()");
            return new ReproducirJava();
        }
    }

    @Override
    public void run() {
        if (verificador == null) {
            verificador = new Timer();
            verificador.scheduleAtFixedRate(controlado, 0, 1000);
        }
    }

    public String cancion() {
        return temaactual;
    }

    public String estado() {
        if (reproduciendo) {
            return "Detenido";
        } else {
            return "Reproduciendo";
        }
    }


}

 