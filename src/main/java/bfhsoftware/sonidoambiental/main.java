/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

/*import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.sun.net.httpserver.*;
import com.sun.swing.internal.plaf.metal.resources.metal;

import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
//aca termina
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import java.util.concurrent.ThreadLocalRandom;
*/
import java.io.*;
public class main {

    private static boolean isandroid() {
         try {
        Class.forName("main");
        return true;
    } catch(ClassNotFoundException e) {
        return false;
    }
    } 
    public static void main(final String... args) throws IOException {
            System.out.println(System.getProperty("os.name"));
            if (isandroid()){
                System.out.println("es android");
            }
           /*  Properties p = System.getProperties();
    Enumeration keys = p.keys();
    while(keys.hasMoreElements()) {
    String key = (String) keys.nextElement();
    String value = (String) p.get(key);
    System.out.println(key + " >>>> " + value);
   }*/
       comunicacion con = new comunicacion();
       reproductor sistemaprincipal = new reproductor();
        Thread t = new Thread(sistemaprincipal);
        t.start();
        con.verificaralbum();
        con.verificarmusica();
        con = null;
        
        
        servidorhttp http = new servidorhttp();
       http.ejecutame();
           
    }

    
    
}


