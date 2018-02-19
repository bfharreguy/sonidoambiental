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
        //iniciar sucesos o logging
        susesos iniciarsusesos = new susesos();
        System.out.println(System.getProperty("os.name"));
        Thread hacer = new Thread(new tareasprogramadas());
        hacer.start();
        basededatos prueba = new basededatos();
        prueba.verificarbasededatos();
        comunicacion con = new comunicacion();

        con.verificaralbum();
        con.comprobardirectorios();

        servidorhttp http = new servidorhttp(con.directoriodemusica);
        Thread servidorweb = new Thread(http);
        servidorweb.start();

        Runnable sistemaprincipal = reproductor.getInstance();
        Thread t = new Thread(sistemaprincipal);
        t.start();
        con.verificarmusica();
        con = null;

    }


}


