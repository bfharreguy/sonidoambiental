/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.Toast;

/**
 *
 * @author bfhsoftware
 */
public class ReproductorAndroid extends reproductor{
    private static MediaPlayer reproductor = null;
    OnCompletionListener escuchar = sedetienelamusica();
    public ReproductorAndroid() {
        super();
    }
    
    @Override
    public void empezar(String proximotema) {        
        super.empezar(proximotema);
        //
        if ((reproductor == null)){
            try {
                if (proximotema.equals("")){                    
                    System.out.println(proximotema);
                    //Environment.getExternalStorageDirectory().getPath()+
                    //Uri myUri = Uri.parse(proximotema);
                    reproductor = new MediaPlayer();
                    //reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    //reproductor.setDataSource(proximotema);
                   /* android.content.ContextWrapper
                            android.content.ContextWrapper*/
                    //reproductor.setDataSource(getApplicationContext(), myUri);
                    
                   // reproductor.setDataSource(getActivity().getApplicationContext(),Uri.parse(proximotema)); 
                   reproductor.setDataSource(proximotema);
                    //reproductor = MediaPlayer.create(this, myUri);
                    reproductor.prepare(); // might take long! (for buffering, etc)                                        
                    reproductor.setOnCompletionListener(escuchar);
                    reproductor.start();
                    System.out.println("deberia estar reproduciendo");
                }
            } catch(Exception e ) {
                e.printStackTrace();
                System.out.println("error al reproducir");
                System.out.println(e.getMessage());        
            }
        }
        reproduciendo = (reproductor != null);
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
    
    private OnCompletionListener sedetienelamusica() {
        reiniciar();
        reproducir();
        return null;
    }
    private void reiniciar(){
        reproduciendo = false;
        if (reproductor != null)
            reproductor.stop();
        reproductor = null;
    }
    public static void displayExceptionMessage(Context context, String msg)
{
    Toast.makeText(context, msg , Toast.LENGTH_LONG).show();
}
/*
    private Context getApplicationContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
