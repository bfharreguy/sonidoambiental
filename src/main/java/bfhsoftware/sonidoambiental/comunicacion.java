/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;

import java.io.File;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class comunicacion {
    String ultimotema = "";
    public String directoriodemusica = "";
    public String sSistemaOperativo = System.getProperty("os.name");
    public class canciones {
        public Boolean seleccionado = false;
        public int id;
        public String nombre="", ruta="", ultimareproduccion = "", album = "";
        public boolean anulado = false;
        public canciones(int id, String nombre, String ruta, String album, String ultimareproduccion, Boolean anulado, Boolean seleccionado){
            this.id = id;
            this.nombre = nombre;
            this.ruta = ruta;
            this.ultimareproduccion = ultimareproduccion;
            this.anulado = anulado;
            this.album = album;
            this.seleccionado = seleccionado;
        }
        
    }
    public boolean sinusuarios(){
        boolean respuesta = false;
        try{
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios;");
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) == 0)){
                    respuesta = true;
                }
            }
            rs.close();
            consulta.close();
        } catch (SQLException ex) {
            //System.out.println("error aca");
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    public String loguear(String usuario, String sha1){
        String retorna="";
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios WHERE sha1 = ? AND usuario = ?;");
            consulta.setString(1, sha1);
            consulta.setString(2, usuario);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) == 0)){
                    return "inexistente";
                }
            }
            rs.close();
            consulta.close();/*
            consulta = regre.consulta("SELECT usuario, sha1  FROM usuarios WHERE sha1 = ?;");
            consulta.setString(1, sha1);
            rs = consulta.executeQuery();
            if (rs.next()) {
            if (usuario == rs.getString(1) && sha1 == rs.getString(2)) {
            return "loguea";
            }
            }
            rs.close();
            consulta.close();*/
            return "loguea";
        } catch (SQLException ex) {
            //System.out.println("error aca");
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    public String crearusuario(String usuario, String sha1){
        try{
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios WHERE usuario = ?;");
            consulta.setString(1, usuario);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) > 0)){
                    return "existente";
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("INSERT INTO usuarios (usuario, sha1) VALUES (?, ?);");
            consulta.setString(1, usuario);
            consulta.setString(2, sha1);
            consulta.execute();
            consulta.close();
            System.out.println("Se crea el usuario:"+ usuario);
            return "creado";
        } catch (SQLException ex) {
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public canciones[] listadodecanciones(boolean anulados) {
        try{
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            int rows =0;
            String sql= "SELECT count(id) FROM musica";
            if (anulados)
                sql += " WHERE anulado = 0;";
            else
                sql += ";";
            consulta = regre.consulta(sql);
            rs = consulta.executeQuery();
            if(rs.next()) {
                rows=rs.getInt(1);
            }
            rs.close();
            sql= "SELECT id, nombreyruta, album, anulado, ultimareproduccion FROM musica";
            if (anulados)
                sql += " WHERE anulado = 0;";
            else
                sql += ";";
            consulta = regre.consulta(sql);
            rs = consulta.executeQuery();
            canciones[] regresar= new canciones[rows];
            rows = 0;
            while (rs.next()) {
                regresar[rows]=new canciones(rs.getInt("id"), (Paths.get(rs.getString("nombreyruta"))).getFileName().toString(), (Paths.get(rs.getString("nombreyruta"))).getParent().toString(), rs.getString("album"), rs.getString("ultimareproduccion"), rs.getBoolean("anulado"), false);
                rows ++;
            }
            rs.close();
            consulta.close();
            return regresar;
        } catch (SQLException ex) {
            //System.out.println("error aca");
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        canciones[] regresar= new canciones[0];
        return regresar;
    }
    public void verificaralbum() {
        basededatos regre = new basededatos();
        regre.ejecutarquery("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
        regre.ejecutarquery("INSERT INTO reproduccion (idalbum) SELECT (SELECT id FROM albums LIMIT 1) WHERE (SELECT Count(id) FROM reproduccion)= 0;");
        regre.ejecutarquery("UPDATE reproduccion SET habilitado = 1 WHERE (SELECT Count(id) FROM reproduccion)= 0 ;");
//      System.out.println("fin");
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
    public Object opcion (String nombre) {
        try{
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND texto IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)){
                    return opcioncadena(nombre, "");
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND numero IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)){
                    return opcionnumero(nombre, 0);
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND binario IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)){
                    return opcionboolean(nombre, false);
                }
            }
            rs.close();
            consulta.close();
        } catch (SQLException ex) {
            //System.out.println("error aca");
            Logger.getLogger(comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public Object opcion (String nombre, Object predeterminado) {
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
        }
        return null;
    }
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
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT binario FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                entro = true;
                preparar = rs.getInt("binario");
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
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT binario FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                preparar = rs.getInt("binario");
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
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT numero FROM opciones WHERE nombre = ?;");
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
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT numero FROM opciones WHERE nombre = ?;");
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
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT texto FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                entro =true;
                preparar = rs.getString("texto");
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
        try {basededatos regre = new basededatos();
        consulta = regre.consulta("SELECT texto FROM opciones WHERE nombre = ?;");
        consulta.setString(1, nombre);
        ResultSet rs = consulta.executeQuery();
        while (rs.next()) {
            preparar = rs.getString("texto");
        }
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  preparar;
    }
    public void guardaropcion(String nombre, String texto ) {
        PreparedStatement consulta;
        try {basededatos regre = new basededatos();
        consulta = regre.consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.executeUpdate();
        consulta.close();
        consulta = regre.consulta("UPDATE opciones SET texto = ? WHERE nombre = ?;");
        consulta.setString(1, texto);
        consulta.setString(2, nombre);
        consulta.executeUpdate();
        consulta.close();
        
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void guardaropcion(String nombre, int numero ) {
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; " );
            consulta.setString(1, nombre);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
            consulta = regre.consulta("UPDATE opciones SET numero = ? WHERE nombre = ?;");
            consulta.setInt(1, numero);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void marcarreproducido(String cancion){
        
        try {basededatos regre = new basededatos();
        PreparedStatement consult=regre.consulta("UPDATE musica SET reproducido = 1, ultimareproduccion = datetime('now','localtime') WHERE nombreyruta = ?;");
        consult.setString(1, cancion);
        consult.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void desmarcarreproducido(){
        basededatos regre = new basededatos();
        regre.ejecutarquery("UPDATE musica SET reproducido = 0;");
    }
    void registrarmusica (String nombre){
        
        try {basededatos regre = new basededatos();
        PreparedStatement consulta = regre.consulta("INSERT INTO musica (nombreyruta, album) SELECT ?, (SELECT id FROM albums LIMIT 1) WHERE (SELECT count(id) FROM musica WHERE nombreyruta = ?) = 0;");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.execute();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    void eliminarmusica (String nombre){
        
        try {basededatos regre = new basededatos();
        PreparedStatement consulta = regre.consulta("DELETE FROM musica WHERE nombreyruta = ?;");
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
        try {basededatos regre = new basededatos();
        PreparedStatement consulta=regre.consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; ");
        consulta.setString(1, nombre);
        consulta.setString(2, nombre);
        consulta.executeUpdate();
        consulta.close();
        consulta = regre.consulta("UPDATE opciones SET binario = ? WHERE nombre = ?;");
        consulta.setInt(1, numero);
        consulta.setString(2, nombre);
        consulta.executeUpdate();
        consulta.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String proximotema () {
        String temaelegido ="";
        try {
            basededatos regre = new basededatos();
            boolean sinarchivos = false;
            boolean archivoencontrado = false;
            comprobardirectorios();
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
                
                ResultSet rs2 = regre.regresardatos("SELECT COUNT(musica.id) from musica inner join reproduccion on musica.album = reproduccion.idalbum  WHERE reproduccion.habilitado = 1;");
                if (rs2.next()) {
                    if ( rs2.getInt(1) != 0) {
                        
                        ResultSet rs = regre.regresardatos("SELECT COUNT(musica.id) from musica inner join reproduccion on musica.album = reproduccion.idalbum  WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1;");
                        while (rs.next()) {
                            if ( rs.getInt(1) ==0) {
                                desmarcarreproducido();
                            }
                        }
                        ResultSet rs1 = regre.regresardatos("SELECT c.nombreyruta FROM (SELECT m.nombreyruta FROM musica m inner join reproduccion on m.album = reproduccion.idalbum WHERE m.reproducido = 0 AND reproduccion.habilitado = 1 ORDER BY datetime(ultimareproduccion, 'localtime') LIMIT (SELECT CAST(COUNT(musica.id) * 0.4 AS int) + 1 FROM musica inner join reproduccion on musica.album = reproduccion.idalbum WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1)) c ORDER BY RANDOM() LIMIT 1;");
                        while (rs1.next()) {
                            temaelegido = rs1.getString("nombreyruta");
                        }
                        if  (temaelegido.equals("")) {
                            sinarchivos = true;
                        }else{
//System.err.println("Verificando tema :"+ temaelegido);
if (verificararchivo(temaelegido)) {
    archivoencontrado = true ;
    marcarreproducido(temaelegido);
}else{
    System.err.println("temaeliminado");
    eliminarmusica(temaelegido);
}
                        }
                    } else { sinarchivos = true;
                    System.err.println("buscandocancion");
                    this.ultimotema="";
// System.err.println(sinarchivos);
                    }
                } else { sinarchivos = true;
                this.ultimotema="";
                }
                
            } while (!archivoencontrado && !sinarchivos) ;
            
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
        comprobardirectorios();
        System.out.println("se verificará el directorio "+ directoriodemusica);
        verificaralbumdesdedirectorio(directoriodemusica);
        System.out.println("Fin");
    }
    void comprobardirectorios (){
        String predeterminado ="";
        switch (sSistemaOperativo) {
            case "Linux":
                predeterminado = "./musica";                
                break;
            case "Windows":
                predeterminado = ".\\musica";
                break;
            case "Android":
                predeterminado="/./sdcard/Download/";
                break;
        }
        directoriodemusica = (String) opcion("carpetademusica", predeterminado);
        try {
            File f = new File(directoriodemusica);
            if (!f.exists()){
                f.mkdirs();
            }
        } catch (Exception e) {
            directoriodemusica=predeterminado;            
            guardaropcion("carpetademusica", predeterminado);            
            File f = new File(directoriodemusica);
            if (!f.exists())
                f.mkdirs();
        }
    }
    public String obtenernombredecancion(String direccioncompleta){
        
        return (Paths.get(direccioncompleta)).getFileName().toString();
    }
    void verificaralbumdesdedirectorio (String directorioaverificar){
        //  System.out.println("Verificando directorio");
//verificar los archivos que estan en el directorio y faltan en la base de datos
String files;
//        String directorio;

File folder = new File(directorioaverificar);
File[] listOfFiles = folder.listFiles();
if (!(listOfFiles == null)){
    
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
}
//verificar los que estan en la base de datos y no en el directorio y eliminarlos
PreparedStatement consulta;
try {basededatos regre = new basededatos();
consulta = regre.consulta("SELECT nombreyruta FROM musica ;");
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
        try {  basededatos regre = new basededatos();
        consulta = regre.consulta("SELECT COUNT(musica.id) FROM musica INNER JOIN reproduccion ON musica.album = reproduccion.idalbum WHERE reproduccion.habilitado =1;");
        ResultSet rs = consulta.executeQuery();
        if (rs.next()) {
            if ( rs.getInt(1)==0);{
            
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
