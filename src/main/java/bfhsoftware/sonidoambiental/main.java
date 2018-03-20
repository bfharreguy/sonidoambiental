/*
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

public class main {

    public static boolean isandroid() {
        try {
            Class.forName("bfhsoftware.sonidoambiental.Sonidoambiental");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void main(final String... args) {
        //subir el volumen al mango y sacar silencio
        audio.setMasterOutputVolume(1);
        audio.setMasterOutputMute(false);
        //iniciar sucesos o logging
        susesos iniciarsusesos = new susesos();
        System.out.println(System.getProperty("os.name"));
        Thread hacer = new Thread(new tareasprogramadas());
        //hacer.setDaemon(true);
        hacer.start();

        basededatos prueba = new basededatos();
        prueba.verificarbasededatos();
        comunicacion con = new comunicacion();

        con.verificarbasededatos();
        con.comprobardirectorios();

        servidorhttp http = new servidorhttp(con.directoriodemusica);
        Thread servidorweb = new Thread(http);
        //servidorweb.setDaemon(true);
        servidorweb.start();


        Runnable sistemaprincipal = reproductor.getInstance();
        Thread t = new Thread(sistemaprincipal);
        //t.setDaemon(true);
        t.start();

        con.verificarmusica();
        con = null;
       /*while (true){
            if (!hacer.isAlive()){
                hacer.start();}
           System.out.println("dale");
            if (!servidorweb.isAlive()){
                servidorweb.start();}
            if (!t.isAlive()){
                t.start();}
           System.out.println(servidorweb.isAlive());

        }*/
    }


}


