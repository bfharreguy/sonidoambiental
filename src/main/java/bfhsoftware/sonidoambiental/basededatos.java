/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bfhsoftware
 */
public class basededatos {
        public void verificarbasededatos(){
        //try {
            //basededatos regre = new basededatos();
            //ResultSet rs = regresardatos("select DISTINCT tbl_name from sqlite_master where tbl_name = 'musica';");
            //if (! rs.next()) {
                    //InputStream fstream = this.getClass().getClassLoader().getResourceAsStream("abc.txt");                    
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"albums\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`nombre` TEXT);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"musica\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombreyruta` TEXT NOT NULL, `anulado` NUMERIC NOT NULL DEFAULT 0, `album` INTEGER, `reproducido` INTEGER NOT NULL DEFAULT 0, `ultimareproduccion` TEXT);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"opciones\" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombre`	TEXT, `numero` INTEGER, `texto`	TEXT, `binario` INTEGER);");                
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"reproduccion\" (`Id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `idalbum` INTEGER NOT NULL DEFAULT 0, `habilitado` INTEGER NOT NULL DEFAULT 1);");
                    ejecutarquery("CREATE TABLE IF NOT EXISTS \"usuarios\" ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `usuario` TEXT, `sha1`	TEXT);");
                    //ejecutarquery("");
            //}
        /*} catch (SQLException ex) {
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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
            throw new RuntimeException("Failed to register SQLDroidDriver");
        }
        //String jdbcUrl = "jdbc:sqldroid:" + "/data/data/" + getPackageName() + "/my-database.db";
        String jdbcUrl = "jdbc:sqldroid:" + "/mnt/sdcard/database.db";
        try {
            con = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            //con = DriverManager.getConnection("jdbc:sqlite:"+ getExternalFilesDir("./") +"database.db");
            //con = DriverManager.getConnection("jdbc:sqlite:/mnt/sdcard/database.db");
            System.out.println("android");   
        } else {
            con = DriverManager.getConnection("jdbc:sqlite:./database.db");
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
        }
    }
}
