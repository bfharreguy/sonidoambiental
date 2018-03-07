/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author bfhsoftware
 */
public class basededatos {
    private static Logger log = Logger.getLogger(susesos.class.getName());
        public void verificarbasededatos(){
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"albums\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`nombre` TEXT);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"musica\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombreyruta` TEXT NOT NULL, `anulado` NUMERIC NOT NULL DEFAULT 0, `album` INTEGER, `reproducido` INTEGER NOT NULL DEFAULT 0, `ultimareproduccion` TEXT);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"opciones\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombre`	TEXT, `numero` INTEGER, `texto`	TEXT, `binario` INTEGER);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"reproduccion\" (`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombre` TEXT, `habilitado` INTEGER NOT NULL DEFAULT 1,`esalbummaestro` INTEGER NOT NULL DEFAULT 0);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"usuarios\" ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `usuario` TEXT, `sha1`	TEXT);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS `programas` (`id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`idpublicidad` INTEGER NOT NULL,`desde` TEXT, `hasta` TEXT,`sinrepetir` INTEGER NOT NULL DEFAULT 1, `repeticiondias` INTEGER NOT NULL DEFAULT 0, `repeticionhoras` INTEGER NOT NULL DEFAULT 0, `repeticionminutos` INTEGER NOT NULL DEFAULT 0);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS `publicidad` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombre` TEXT NOT NULL, `habilitado` INTEGER NOT NULL DEFAULT 1,`validodesde` TEXT NOT NULL, `validohasta` INTEGER NOT NULL);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS `programareproducciondias` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `idprogramareproduccion` INTEGER, `diadesemana`INTEGER, `diademes` INTEGER);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS `programareproduccion` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `idpadre` INTEGER NOT NULL, `diadesemana` INTEGER NOT NULL, `horadesde` TEXT NOT NULL, `horahasta` TEXT NOT NULL);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS `contenidodereproduccion` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idreproduccion` INTEGER NOT NULL DEFAULT 0,`idalbum` INTEGER NOT NULL DEFAULT 0);");
        }
    public Connection conexionreal () throws SQLException {
            return conexion();
    }
    //  public PreparedStatement consulta;
    private static Connection con=null;
    Connection conexion() throws SQLException  {
        if (con != null){
            //System.out.println("conexion abierta");
            return con;
        }
        if (main.isandroid()){
             try {
            DriverManager.registerDriver((Driver) Class.forName("org.sqldroid.SQLDroidDriver").newInstance());
        } catch (Exception e) {
                 log.warning("Error al intentar conectar a la base de datos en android: "+ e.getMessage());
            throw new RuntimeException("Failed to register SQLDroidDriver");
        }
        //String jdbcUrl = "jdbc:sqldroid:" + "/data/data/" + getPackageName() + "/my-database.db";
        /*String jdbcUrl = "jdbc:sqldroid:" + "/mnt/sdcard/database.db";
        try {
            con = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            log.warning("Error al intentar conectar a la base de datos en android: "+ e.getMessage());
            throw new RuntimeException(e);
        }*/
            //con = DriverManager.getConnection("jdbc:sqlite:"+ getExternalFilesDir("./") +"database.db");
            //con = DriverManager.getConnection("jdbc:sqlite:/mnt/sdcard/database.db");
            System.out.println("android");   
        } else {
            con = DriverManager.getConnection("jdbc:sqlite:."+ File.separator +"database.db");
        }
        return con;
    }
    public PreparedStatement consulta(String sql) {
        PreparedStatement consult=null;
        try {
            // basededatos conexion = new basededatos();
            consult = conexion().prepareStatement(sql);
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return consult;
    }    
    public ResultSet regresardatos(String sql){
        ResultSet rs=null;
        PreparedStatement consulta;
        try {
            //basededatos conexion = new basededatos();
            consulta = conexion().prepareStatement(sql);
            rs = consulta.executeQuery();
        }catch (SQLException e) {
            log.warning("Error al preparar una consulta con resultado: "+ e.getMessage());
            System.out.println(e.getMessage());
        }
        return rs;
    }
    public void ejecutarquery (String sql){
        try {
            //basededatos conexion = new basededatos();
            Connection connnn = conexion();
            //if (connnn != null){
            Statement stmt = connnn.createStatement();
            stmt.execute(sql);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al ejecutar un query: "+ e.getMessage());
        }
    }
}
