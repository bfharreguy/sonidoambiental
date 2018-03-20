/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

//import static bfhsoftware.sonidoambiental.reproductor.pausado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author bfhsoftware
 */
public class ReproducirJava extends reproductor {
    private static final Logger log = Logger.getLogger(susesos.class.getName());
    final JFXPanel fxPanel = new JFXPanel();
    private Boolean pasardetema = false;
    private Media musica = null;
    static MediaPlayer reproductor = null;

    public ReproducirJava() {
        /* Llamamos al constructor padre */
        super();
    }

    @Override
    public void empezar(String proximotema) {
        /* Llamamos a la implementación común de comienzo */
        super.empezar(proximotema);
        if ((reproductor == null) || pasardetema) {
            if (pasardetema)
                reproductor.dispose();
            pasardetema=false;
            if (proximotema.equals("")) {
                ordendereproducir = false;
                reproduciendo = false;
            } else {
                musica = new Media(new File(proximotema).toURI().toString());

                reproductor = new MediaPlayer(musica);
                if (reproductor != null) {
                    //reproductor.setPlayBackListener(escuchar);
                    reproductor.play();
                    temaactual = com.Ultimotema();
                    reproductor.setOnStopped(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Se detiene la reproduccion por stop");
                            reproduciendo = false;
                        }
                    });
                    reproductor.setOnHalted(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Se detiene la reproduccion por halted");
                            revivir();
                        }
                    });
                    reproductor.setOnError(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Se detiene la reproduccion por un error");
                            revivir();
                        }
                    });
                    reproductor.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Intentar reproducir proximo tema");
                            proximotema();
                            return;
                        }
                    });
                } else {
                    reproduciendo = false;
                    ordendereproducir = false;

                }
            }
        }


        reproduciendo = (reproductor != null);
    }

    private void revivir() {
        reproductor = null;
        reproduciendo = false;
        ordendereproducir = com.verificarcanciones().sinarchivos;
        if (ordendereproducir)
            proximotema();
        return;
    }

    @Override
    public void parar() {
        /* Llamamos a la implementación común de parada */
        super.parar();
        reproduciendo = false;
        reproductor.stop();
        reproductor = null;

        /* Implementamos aquí el método en java se */
    }

    @Override
    public void pausa() {
        /* Llamamos a la implementación común de pausa */
        super.pausa();
        reproduciendo = false;
        if (reproductor != null)
            reproductor.stop();
    }

    private void reiniciar() {
        reproduciendo = false;
        if (reproductor != null)
            reproductor.stop();
        /*if (!(musica == null))
            musica = null;*/
    }

    public void proximotema() {
        reiniciar();
        pasardetema=true   ;
        reproducir();
    }
}