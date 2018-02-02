/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

import android.media.MediaPlayer;

/**
 *
 * @author bfhsoftware
 */
public class reproducirAndroid extends reproductor {
    private static MediaPlayer mp;
    public reproducirAndroid() {
        /* Llamamos al constructor padre */
        super();
    }
    
    @Override
    public void empezar(String proximotema) {
        /* Llamamos a la implementación común de comienzo */
        super.empezar(proximotema);
        
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