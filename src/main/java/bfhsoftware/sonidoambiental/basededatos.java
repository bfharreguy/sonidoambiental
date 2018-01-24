/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.sql.Connection;
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
  //  public PreparedStatement consulta;
    private static Connection con=null;
    Connection conexion() throws SQLException  {
        if (con != null){
            //System.out.println("conexion abierta");
            return con;
            
        }
        if (main.isandroid()){
            //con = DriverManager.getConnection("jdbc:sqlite:"+ getExternalFilesDir("./") +"database.db");
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
