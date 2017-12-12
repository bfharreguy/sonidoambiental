/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
//Instalacion y configuracion de sistema de camaras

public class comunicacion {
    String ultimotema ="";
    String directoriodemusica = ".\\";
    static Connection conn;
    Connection conexion() {
        try {
            if (conn == null){
                conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    public void verificarbasededatos(){
        try {
            ResultSet rs = regresardatos("select DISTINCT tbl_name from sqlite_master where tbl_name = 'musica';");
            if (! rs.next()) {
                //InputStream fstream = this.getClass().getClassLoader().getResourceAsStream("abc.txt");
                //ejecutarquery(String.getClassLoader().getResourceAsStream("myfile.txt") );
            }
        } catch (SQLException ex) {
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public PreparedStatement consulta(String sql) {
        PreparedStatement consult=null;
        try {
            consult = conexion().prepareStatement(sql);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return consult;
    }
    public ResultSet regresardatos(String sql){
        ResultSet rs=null;
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement(sql);
        rs = consulta.executeQuery();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    public void ejecutarquery (String sql){
        try (Statement stmt = conexion().createStatement();){
            stmt.execute(sql);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void verificaralbum() {
        ejecutarquery("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
        ejecutarquery("INSERT INTO reproduccion (idalbum) SELECT (SELECT id FROM albums LIMIT 1) WHERE (SELECT Count(id) FROM reproduccion)= 0;");
        ejecutarquery("UPDATE reproduccion SET habilitado = 1 WHERE (SELECT Count(id) FROM reproduccion)= 0 LIMIT 1;");
        /*try (
        Statement stmt = conexion().createStatement()
        ){
        stmt.execute("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
        }catch (SQLException e) {
        System.out.println(e.getMessage());
        }*/
    }
    /*public void verificaralbum() {
    try (
    Statement stmt = conexion().createStatement()
    ){
    stmt.execute("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
    }catch (SQLException e) {
    System.out.println(e.getMessage());
    }
    }*/
    public Object opcion (String nombre, Object predeterminado, Integer tipo) {
        if (predeterminado != null ){
            if (predeterminado instanceof Integer) {
                return opcionnumero(nombre, (Integer) predeterminado);
            }
            if (predeterminado instanceof String) {
                return opcioncadena(nombre, (String) predeterminado);
            }
            if (predeterminado instanceof Boolean) {
                return opcionboolean(nombre, (Boolean) predeterminado);
            }
        } else{
            switch (tipo) {
                case 0:
                    return opcionboolean(nombre);
                case 1:
                    return opcionnumero(nombre);
                case 2:
                    return opcioncadena(nombre);
            }            
        }
        return null;
    }
    public boolean opcionboolean(String nombre, Boolean predeterminado) {
        int preparar = 0;
        boolean entro=false;
        PreparedStatement consulta;
        try {
            consulta = conexion().prepareStatement("SELECT numero FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                entro = true;
                preparar = rs.getInt("numero");
            }            
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (!entro){    
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return  !( preparar == 0 );
    }
    public boolean opcionboolean(String nombre) {
        int preparar = 0;
       PreparedStatement consulta;
        try {
            consulta = conexion().prepareStatement("SELECT numero FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                 preparar = rs.getInt("numero");
            }            
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return  !( preparar == 0 );
    }
     public int opcionnumero(String nombre, Integer predeterminado) {
         Boolean entro=false;
                int preparar = 0;
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("SELECT numero FROM opciones WHERE nombre = ?;");
        consulta.setString(1, nombre);
        ResultSet rs = consulta.executeQuery();
        while (rs.next()) {
            entro=Boolean.TRUE;
           preparar = rs.getInt("numero");
        }
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (!entro) {
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return  preparar;
    }
    public int opcionnumero(String nombre) {
                int preparar = 0;
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("SELECT numero FROM opciones WHERE nombre = ?;");
        consulta.setString(1, nombre);
        ResultSet rs = consulta.executeQuery();
        while (rs.next()) {
           preparar = rs.getInt("numero");
        }
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  preparar;
    }
    public String opcioncadena(String nombre, String predeterminado) {
        Boolean entro =false;
        String preparar = "";
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("SELECT texto FROM opciones WHERE nombre = ?;");
        consulta.setString(1, nombre);
        ResultSet rs = consulta.executeQuery();
        while (rs.next()) {
            entro =true;
            preparar = rs.getString("numero");
        }
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (!entro) {
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return  preparar;
    }
    public String opcioncadena(String nombre) {
                String preparar = "";
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("SELECT texto FROM opciones WHERE nombre = ?;");
        consulta.setString(1, nombre);
        ResultSet rs = consulta.executeQuery();
        while (rs.next()) {
            preparar = rs.getString("numero");
        }
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
         return  preparar;
    }
    public void guardaropcion(String nombre, String texto ) {
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; " +
                "UPDATE optiones SET texto = ? WHERE nombre = ?;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.setString(3, texto);
        consulta.setString(4, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void guardaropcion(String nombre, int numero ) {
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; " +
                "UPDATE optiones SET numero = ? WHERE nombre = ?;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.setInt(3, numero);
        consulta.setString(4, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void marcarreproducido(String cancion){
        
        try {
            PreparedStatement consult=consulta("UPDATE musica SET reproducido = 1, ultimareproduccion = datetime('now','localtime') WHERE nombreyruta = ?;");
            consult.setString(1, cancion);
            consult.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void desmarcarreproducido(){
        ejecutarquery("UPDATE musica SET reproducido = 0;");
    }
    void registrarmusica (String nombre){
        
        try {PreparedStatement consulta = consulta("INSERT INTO musica (nombreyruta, album) SELECT ?, (SELECT id FROM albums LIMIT 1) WHERE (SELECT count(id) FROM musica WHERE nombreyruta = ?) = 0;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void eliminarmusica (String nombre){
        
        try {PreparedStatement consulta = consulta("DELETE FROM musica WHERE nombreyruta = ?;");
        consulta.setString(1, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void guardaropcion(String nombre, boolean bol ) {
        int numero = 0;
        if ( bol ) {
            numero = 1;
        } else {
            numero = 0;
        }
        try {PreparedStatement consulta=consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; " +
                "UPDATE optiones SET numero = ? WHERE nombre = ?;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.setInt(3, numero);
        consulta.setString(4, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String proximotema () {
        String temaelegido ="";
        try {
            boolean archivoencontrado = false;
            comprobardirectorio();
            /*do {
            double cantidaddearchivosareproducir= 0;
            do {
            ResultSet rs = regresardatos("SELECT COUNT(musica.id) from musica inner join reproduccion on musica.album = reproduccion.idalbum  WHERE musica.reproducido = 0;");
            while (rs.next()) {
            cantidaddearchivosareproducir = rs.getInt(1) * .4;
            }
            System.out.println("CANTIDADDE TEMAS:" +  cantidaddearchivosareproducir +" Cantida");
            if (cantidaddearchivosareproducir == 0)
            desmarcarreproducido();
            } while (cantidaddearchivosareproducir == 0);
            int cancionelegida = ThreadLocalRandom.current().nextInt(1, (int) (cantidaddearchivosareproducir + 1));
            
            try {
            ResultSet rs1 = regresardatos("SELECT nombreyruta FROM musica WHERE reproducido = 0; ");
            int idelegida=0;
            while (rs1.next()) {
            idelegida ++;
            if (idelegida == cancionelegida) {
            temaelegido = rs1.getString("nombreyruta");
            System.out.println(temaelegido);
            }}
            }catch (SQLException e) {
            System.out.println(e.getMessage());
            }*/
            do{
                ResultSet rs = regresardatos("SELECT COUNT(musica.id) from musica inner join reproduccion on musica.album = reproduccion.idalbum  WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1;");
                while (rs.next()) {
                    if ( rs.getInt(1) ==0) {
                        desmarcarreproducido();
                    }
                }
                ResultSet rs1 = regresardatos("SELECT c.nombreyruta FROM (SELECT m.nombreyruta FROM musica m inner join reproduccion on m.album = reproduccion.idalbum WHERE m.reproducido = 0 AND reproduccion.habilitado = 1 ORDER BY datetime(ultimareproduccion, 'localtime') LIMIT (SELECT CAST(COUNT(musica.id) * 0.4 AS int) + 1 FROM musica inner join reproduccion on musica.album = reproduccion.idalbum WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1)) c ORDER BY RANDOM() LIMIT 1;");
                while (rs1.next()) {
                    temaelegido = rs1.getString("nombreyruta");
                }
                //System.err.println("Verificando tema :"+ temaelegido);
                if (verificararchivo(temaelegido)) {
                    archivoencontrado = true ;
                    marcarreproducido(temaelegido);
                }else{
                    System.err.println("temaeliminado");
                    eliminarmusica(temaelegido);
                }
            } while (!archivoencontrado) ;
            
            this.ultimotema = new File(temaelegido).getName();
            System.err.println(this.ultimotema);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return temaelegido;
    }
    public String Ultimotema(){
        return this.ultimotema;
    }
    public void verificarmusica () {
        comprobardirectorio();
        System.out.println("se verificará el directorio "+ directoriodemusica);
        verificaralbumdesdedirectorio(directoriodemusica);
        System.out.println("Fin");
    }
    void comprobardirectorio (){
        File f = new File(directoriodemusica);
        if (!f.exists()){
            System.out.println("No existe el directorio seleccionado previamente");
            directoriodemusica = ".\\";
        }
    }
    public String obtenernombredecancion(String direccioncompleta){
        return direccioncompleta.substring(direccioncompleta.lastIndexOf("\\")+1);
    }
    void verificaralbumdesdedirectorio (String directorioaverificar){
        //verificar los archivos que estan en el directorio y faltan en la base de datos
        String files;
//        String directorio;
File folder = new File(directorioaverificar);
File[] listOfFiles = folder.listFiles();
for (File listOfFile : listOfFiles) {
    if (listOfFile.isFile()) {
        files = listOfFile.getAbsolutePath();
        
        if (files.endsWith(".mp3") || files.endsWith(".MP3")|| files.endsWith(".mP3") || files.endsWith(".Mp3"))
        {
            System.out.println(" obtenido "+ obtenernombredecancion(listOfFile.getAbsolutePath()));
            registrarmusica(files);
        }
    } else if (listOfFile.isDirectory()) {
        verificaralbumdesdedirectorio(listOfFile.getPath());
        
    }
}
//verificar los que estan en la base de datos y no en el directorio y eliminarlos
PreparedStatement consulta;
try {consulta = conexion().prepareStatement("SELECT nombreyruta FROM musica ;");
ResultSet rs = consulta.executeQuery();
while (rs.next()) {
    if  ( ! verificararchivo(rs.getString(1)) ){
        eliminarmusica(rs.getString(1));
    }
}
}catch (SQLException e) {
    System.out.println(e.getMessage());
}

    }
    
    void verificarlistadereproduccion(){
        PreparedStatement consulta;
        try {consulta = conexion().prepareStatement("SELECT COUNT(musica.id) FROM musica INNER JOIN reproduccion ON musica.album = reproduccion.idalbum WHERE reproduccion.habilitado =1;");
        ResultSet rs = consulta.executeQuery();
        if (rs.next()) {
            if ( rs.getInt(1)==0 );{
        }
        }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //SELECT COUNT(musica.id) FROM musica INNER JOIN reproduccion ON musica.album = reproduccion.idalbum WHERE reproduccion.habilitado	=1;
    }
    public boolean verificararchivo ( String nombre){
        File file = new File(nombre);
        return file.exists();
    }
}
