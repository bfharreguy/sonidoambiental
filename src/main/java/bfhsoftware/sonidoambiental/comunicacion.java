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
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class comunicacion {
    private static final Logger log = Logger.getLogger(susesos.class.getName());
    String ultimotema = "";
    public String directoriodemusica = "";
    public String sSistemaOperativo = System.getProperty("os.name");
    private boolean estadoverificandomusica = false;

    public static class matriz {
        private boolean seleccionado = false;
        private String dato = "";
        private String dato2 = "";
        private boolean condicion1 = false;
        private boolean condicion2 = false;

        public matriz(boolean seleccionado, String dato) {
            this.seleccionado = seleccionado;
            this.dato = dato;
        }

        public matriz(boolean seleccionado, String dato, String dato2) {
            this.seleccionado = seleccionado;
            this.dato = dato;
            this.dato2 = dato2;
        }

        public matriz(boolean seleccionado, String dato, String dato2, boolean condicion1, boolean condicion2) {
            this.seleccionado = seleccionado;
            this.dato = dato;
            this.dato2 = dato2;
            this.condicion1 = condicion1;
            this.condicion2 = condicion2;
        }
    }

    public static class reproducciones {
        public String nombre = "";
        public boolean habilitado = true;
        public boolean albummaestro = false;
        public ArrayList<String> albums = null;
        public ArrayList<String> albumsaagregar = null;
        public ArrayList<Integer> diadesemana = null;
        public ArrayList<String> horadesde = null;
        public ArrayList<String> horahasta = null;

        public reproducciones reproducciones() {
            return this;
        }

        public reproducciones(String nombre, boolean habilitado, boolean albummaestro, ArrayList<String> albums) {
            this.nombre = nombre;
            this.habilitado = habilitado;
            this.albummaestro = albummaestro;
            this.albums = albums;
        }

        public reproducciones(String nombre, boolean habilitado, boolean albummaestro) {
            this.horadesde = new ArrayList<String>();
            this.horahasta = new ArrayList<String>();
            this.albumsaagregar = new ArrayList<String>();
            this.albums = new ArrayList<String>();
            this.nombre = nombre;
            this.habilitado = habilitado;
            this.albummaestro = albummaestro;
            this.diadesemana = new ArrayList<Integer>();
        }

        public void agregaralalbum(String album) {
            this.albums.add(album);
        }

        public void agregaralalbumquefaltan(String album) {
            this.albumsaagregar.add(album);
        }

        public void agregarhorario(Integer diadesemana, String horadesde, String horahasta) {
            this.diadesemana.add(diadesemana);
            this.horadesde.add(horadesde);
            this.horahasta.add(horahasta);
        }
    }

    public class canciones {
        public Boolean seleccionado = false;
        public int id;
        public String nombre = "", ruta = "", ultimareproduccion = "", album = "";
        public boolean anulado = false;

        public canciones(int id, String nombre, String ruta, String album, String ultimareproduccion, Boolean anulado, Boolean seleccionado) {
            this.id = id;
            this.nombre = nombre;
            this.ruta = ruta;
            this.ultimareproduccion = ultimareproduccion;
            this.anulado = anulado;
            this.album = album;
            this.seleccionado = seleccionado;
        }
    }

    public boolean sinusuarios() {
        boolean respuesta = false;
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios;");
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) == 0)) {
                    respuesta = true;
                }
            }
            rs.close();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al verificar si hay usuarios: " + e.getMessage());
        }
        return respuesta;
    }

    public String loguear(String usuario, String sha1) {
        String retorna = "";
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios WHERE sha1 = ? AND usuario = ?;");
            consulta.setString(1, sha1);
            consulta.setString(2, usuario);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) == 0)) {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al loguear usuario: " + e.getMessage());
        }
        return "";
    }

    public void cambiardealbum(String cancion, String album) {
        Integer idalbum = 0;
        System.out.println(cancion + " " + album);
        try {
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT id FROM albums WHERE nombre = ? ;");
            consulta.setString(1, album);
            rs = consulta.executeQuery();
            if (rs.next()) {
                idalbum = rs.getInt(1);
            } else
                return;
            rs.close();
            consulta.close();
            consulta = regre.consulta("UPDATE musica SET album = ? WHERE nombreyruta like ?;");
            consulta.setInt(1, idalbum);
            consulta.setString(2, "%" + cancion);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al cambiar de album: " + e.getMessage());
        }
    }

    public void equilibraralbumsenenreproductor() {
        basededatos regre = new basededatos();
        regre.ejecutarquery("INSERT INTO contenidodereproduccion (idalbum, idreproduccion) SELECT (SELECT id FROM albums LIMIT 1), (SELECT id FROM reproduccion WHERE habilitado = 1 AND esalbummaestro = 1 LIMIT 1) WHERE (SELECT Count(contenidodereproduccion.id) FROM contenidodereproduccion INNER JOIN(reproduccion, albums) ON reproduccion.id = contenidodereproduccion.idreproduccion AND albums.id = contenidodereproduccion.idalbum WHERE reproduccion.habilitado = 1 and reproduccion.esalbummaestro = 1)= 0;");
    }

    public String eliminaralbum(String nombre) {
        try {
            boolean acomodar = false;
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT COUNT(id) FROM albums ;");
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) == 1)) {
                    rs.close();
                    consulta.close();
                    return "noquedarianalbumes";
                }
            }
            rs.close();
            consulta = regre.consulta("UPDATE musica SET album = (SELECT id FROM albums WHERE nombre != ? LIMIT 1) WHERE album = (SELECT id FROM albums WHERE nombre = ?);");
            consulta.setString(1, nombre);
            consulta.setString(2, nombre);
            consulta.execute();
            consulta.close();
            consulta = regre.consulta("DELETE FROM albums WHERE nombre = ?;");
            consulta.setString(1, nombre);
            consulta.execute();
            consulta.close();
            equilibraralbumsenenreproductor();
            return "eliminado";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al eliminar album: " + e.getMessage());
        }
        return "";
    }

    public String editaralbum(String anteriornombre, String nuevonombre) {
        try {
            // System.out.println(anteriornombre +" " + nuevonombre);
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT COUNT(id) FROM albums WHERE nombre = ?;");
            consulta.setString(1, nuevonombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) > 0)) {
                    return "existente";
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("UPDATE albums SET nombre = ? WHERE nombre =?;");
            consulta.setString(1, nuevonombre);
            consulta.setString(2, anteriornombre);
            consulta.execute();
            consulta.close();
            return "editado";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al editar album: " + e.getMessage());
        }
        return "";
    }

    public void quitaralbumdereproduccion(String reproduccion, String album) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        ResultSet rs;
        try {
            consulta = regre.consulta("DELETE FROM contenidodereproduccion WHERE idreproduccion = (SELECT id FROM reproduccion WHERE nombre = ?) AND idalbum =(SELECT id FROM albums WHERE nombre = ?);");
            consulta.setString(1, reproduccion);
            consulta.setString(2, album);
            consulta.execute();
            consulta.close();
            equilibraralbumsenenreproductor();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener reproduccion: " + e.getMessage());
        }
    }

    public void agregaralbumdereproduccion(String reproduccion, String album) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("INSERT INTO contenidodereproduccion (idreproduccion, idalbum) SELECT (SELECT id FROM reproduccion WHERE nombre = ?), (SELECT id FROM albums WHERE nombre = ?) WHERE (SELECT COUNT(id) FROM contenidodereproduccion WHERE idreproduccion = (SELECT id FROM reproduccion WHERE nombre = ?) AND idalbum =(SELECT id FROM albums WHERE nombre = ?)) = 0;");
            consulta.setString(1, reproduccion);
            consulta.setString(2, album);
            consulta.setString(3, reproduccion);
            consulta.setString(4, album);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al agregar album a reproduccion: " + e.getMessage());
        }
    }

    public void eliminarreproduccion(String reproduccion) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("DELETE FROM reproduccion WHERE (SELECT count(id) FROM reproduccion WHERE nombre != ? AND esalbummaestro = 1 AND habilitado = 1) > 0 AND nombre = ? ;");
            consulta.setString(1, reproduccion);
            consulta.setString(2, reproduccion);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al eliminar reproduccion : " + e.getMessage());
        }
    }

    public void deshacerreproduccionmaestra(String reproduccion) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("UPDATE reproduccion SET esalbummaestro = 0 WHERE (SELECT count(id) FROM reproduccion WHERE nombre != ? AND esalbummaestro = 1 AND habilitado = 1) > 0 AND nombre = ? ;");
            consulta.setString(1, reproduccion);
            consulta.setString(2, reproduccion);
            consulta.execute();
            consulta.close();
            regre.ejecutarquery("INSERT INTO contenidodereproduccion (idalbum, idreproduccion) SELECT (SELECT id FROM albums LIMIT 1), (SELECT id FROM reproduccion WHERE habilitado = 1 AND esalbummaestro = 1 LIMIT 1) WHERE (SELECT Count(contenidodereproduccion.id) FROM contenidodereproduccion INNER JOIN(reproduccion, albums) ON reproduccion.id = contenidodereproduccion.idreproduccion AND albums.id = contenidodereproduccion.idalbum WHERE reproduccion.habilitado = 1 and reproduccion.esalbummaestro = 1)= 0;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al deshacer reproduccion maestra: " + e.getMessage());
        }
    }

    public void hacerreproduccionmaestra(String reproduccion) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("UPDATE reproduccion SET esalbummaestro = 1 WHERE nombre = ? ;");
            consulta.setString(1, reproduccion);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al hacer reproduccion maestra: " + e.getMessage());
        }
    }

    public void deshabilitarreproduccion(String reproduccion) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("UPDATE reproduccion SET habilitado = 0 WHERE (SELECT count(id) FROM reproduccion WHERE habilitado = 1) > 0 AND (SELECT count(id) FROM reproduccion WHERE nombre != ? AND esalbummaestro = 1 AND habilitado = 1) > 0 AND nombre = ? ;");
            consulta.setString(1, reproduccion);
            consulta.setString(2, reproduccion);
            consulta.execute();
            consulta.close();
            regre.ejecutarquery("INSERT INTO contenidodereproduccion (idalbum, idreproduccion) SELECT (SELECT id FROM albums LIMIT 1), (SELECT id FROM reproduccion WHERE habilitado = 1 AND esalbummaestro = 1 LIMIT 1) WHERE (SELECT Count(contenidodereproduccion.id) FROM contenidodereproduccion INNER JOIN(reproduccion, albums) ON reproduccion.id = contenidodereproduccion.idreproduccion AND albums.id = contenidodereproduccion.idalbum WHERE reproduccion.habilitado = 1 and reproduccion.esalbummaestro = 1)= 0;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al deshabilitar reproduccion: " + e.getMessage());
        }
    }

    public void habilitarreproduccion(String reproduccion) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("UPDATE reproduccion SET habilitado = 1 WHERE nombre = ? ;");
            consulta.setString(1, reproduccion);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al habilitar reproduccion: " + e.getMessage());
        }
    }

    public void agregarprogramaciondereproduccion(String nombre, Integer diadesemana, Long horadesde, Long horahasta) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        Date date1 = new Date(horadesde);
        Date date2 = new Date(horahasta);
        String horadesdestr = new SimpleDateFormat("HH:mm").format(date1);
        String horahastastr = new SimpleDateFormat("HH:mm").format(date2);
        //System.out.println(horadesdestr + " " + horahastastr);
        try {
            consulta = regre.consulta("INSERT INTO programareproduccion (idreproduccion, diadesemana, horadesde, horahasta) VALUES ((SELECT id FROM reproduccion WHERE nombre = ? LIMIT 1), ?, ?, ?);");
            consulta.setString(1, nombre);
            consulta.setInt(2, diadesemana);
            consulta.setString(3, horadesdestr);
            consulta.setString(4, horahastastr);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener reproduccion: " + e.getMessage());
        }
    }

    public void eliminarprogramaciondereproduccion(String nombre, Integer diadesemana, String horadesdestr, String horahastastr) {
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        try {
            consulta = regre.consulta("DELETE FROM programareproduccion WHERE idreproduccion = (SELECT id FROM reproduccion WHERE nombre = ? LIMIT 1) AND diadesemana = ? AND horadesde = ? AND horahasta = ?;");
            consulta.setString(1, nombre);
            consulta.setInt(2, diadesemana);
            consulta.setString(3, horadesdestr);
            consulta.setString(4, horahastastr);
            consulta.execute();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al eliminar programa de reproduccion: " + e.getMessage());
        }
    }

    public reproducciones[] obtenerreproducciones() {
        reproducciones respuesta[] = null;
        int tamaño = 0;
        int indice = 0;
        PreparedStatement consulta;
        basededatos regre = new basededatos();
        ResultSet rs;
        try {
            consulta = regre.consulta("SELECT count(reproduccion.id) FROM reproduccion;");
            rs = consulta.executeQuery();
            if (rs.next())
                tamaño = rs.getInt(1);
            rs.close();
            consulta.close();
            if (tamaño == 0)
                return respuesta;
            respuesta = new reproducciones[tamaño];
            consulta = regre.consulta("SELECT nombre, habilitado, esalbummaestro FROM reproduccion;");
            rs = consulta.executeQuery();
            while (rs.next()) {
                respuesta[indice] = new reproducciones(rs.getString(1), rs.getBoolean(2), rs.getBoolean(3));
                indice++;
            }
            rs.close();
            consulta.close();
            for (indice = 0; indice < tamaño; ++indice) {
                consulta = regre.consulta("SELECT albums.nombre FROM reproduccion INNER JOIN (albums, contenidodereproduccion) ON reproduccion.id = contenidodereproduccion.idreproduccion AND contenidodereproduccion.idalbum = albums.id WHERE reproduccion.nombre = ?;");
                consulta.setString(1, respuesta[indice].nombre);
                rs = consulta.executeQuery();
                while (rs.next()) {
                    respuesta[indice].agregaralalbum(rs.getString(1));
                }
                rs.close();
                consulta.close();
            }
            for (indice = 0; indice < tamaño; ++indice) {
                consulta = regre.consulta("SELECT programareproduccion.diadesemana, programareproduccion.horadesde, programareproduccion.horahasta FROM reproduccion INNER JOIN (programareproduccion) ON reproduccion.id = programareproduccion.idreproduccion WHERE reproduccion.nombre = ?;");
                consulta.setString(1, respuesta[indice].nombre);
                rs = consulta.executeQuery();
                while (rs.next()) {
                    respuesta[indice].agregarhorario(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
                rs.close();
                consulta.close();
            }
            for (indice = 0; indice < tamaño; ++indice) {
                consulta = regre.consulta("SELECT nombre FROM albums;");
                rs = consulta.executeQuery();
                while (rs.next()) {
                    //System.out.println(String.valueOf(!respuesta[indice].albums.contains(rs.getString(1))) + " " + rs.getString(1));
                    if (!(respuesta[indice].albums.contains(rs.getString(1))))
                        respuesta[indice].agregaralalbumquefaltan(rs.getString(1));
                }
                rs.close();
                consulta.close();
            }
            return respuesta;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener reproduccion: " + e.getMessage());
        }
        return respuesta;
    }

    public String crearnuevareproduccion(String nombre) {
        try {
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            consulta = regre.consulta("INSERT INTO reproduccion (nombre) SELECT ? WHERE (SELECT count(id) FROM reproduccion WHERE nombre = ?) = 0 ;");
            consulta.setString(1, nombre);
            consulta.setString(2, nombre);
            consulta.execute();
            consulta.close();
            return "creado";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al crear album: " + e.getMessage());
        }
        return "";
    }

    public String crearalbum(String nombre) {
        try {
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT COUNT(id) FROM albums WHERE nombre = ?;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) > 0)) {
                    return "existente";
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("INSERT INTO albums (nombre) VALUES (?);");
            consulta.setString(1, nombre);
            consulta.execute();
            consulta.close();
            return "creado";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al crear album: " + e.getMessage());
        }
        return "";
    }

    public String crearusuario(String usuario, String sha1) {
        try {
            PreparedStatement consulta;
            basededatos regre = new basededatos();
            ResultSet rs;
            consulta = regre.consulta("SELECT COUNT(id) FROM usuarios WHERE usuario = ?;");
            consulta.setString(1, usuario);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) > 0)) {
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
            System.out.println("Se crea el usuario:" + usuario);
            return "creado";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al crear usuario: " + e.getMessage());
        }
        return "";
    }

    public canciones[] listadodecanciones(boolean anulados) {
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            int rows = 0;
            String sql = "SELECT count(id) FROM musica";
            if (anulados)
                sql += " WHERE anulado = 0;";
            else
                sql += ";";
            consulta = regre.consulta(sql);
            rs = consulta.executeQuery();
            if (rs.next()) {
                rows = rs.getInt(1);
            }
            rs.close();
            sql = "SELECT musica.id, musica.nombreyruta, albums.nombre, musica.anulado, musica.ultimareproduccion FROM musica INNER JOIN albums ON albums.id = musica.album ;";
            if (anulados)
                sql += " WHERE anulado = 0;";
            else
                sql += ";";
            consulta = regre.consulta(sql);
            rs = consulta.executeQuery();
            canciones[] regresar = new canciones[rows];
            rows = 0;
            while (rs.next()) {
                regresar[rows] = new canciones(rs.getInt("id"), (Paths.get(rs.getString("nombreyruta"))).getFileName().toString(), (Paths.get(rs.getString("nombreyruta"))).getParent().toString(), rs.getString("nombre"), rs.getString("ultimareproduccion"), rs.getBoolean("anulado"), false);
                rows++;
            }
            rs.close();
            consulta.close();
            return regresar;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener listado de canciones: " + e.getMessage());
        }
        canciones[] regresar = new canciones[0];
        return regresar;
    }

    public void verificarbasededatos() {
        basededatos regre = new basededatos();
        regre.ejecutarquery("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
        regre.ejecutarquery("UPDATE musica SET album = (SELECT id FROM albums LIMIT 1) WHERE (SELECT COUNT(albums.id) FROM albums WHERE musica.album = albums.id) = 0;");
        regre.ejecutarquery("INSERT INTO reproduccion (nombre, esalbummaestro) SELECT 'reproduccion1', 1 WHERE (SELECT Count(id) FROM reproduccion)= 0;");
        regre.ejecutarquery("UPDATE reproduccion SET habilitado = 1 WHERE (SELECT Count(id) FROM reproduccion WHERE habilitado = 1)= 0 ;");
        regre.ejecutarquery("UPDATE reproduccion SET esalbummaestro = 1 WHERE (SELECT Count(id) FROM reproduccion WHERE esalbummaestro = 1 AND habilitado = 1)= 0 ;");
        regre.ejecutarquery("INSERT INTO contenidodereproduccion (idalbum, idreproduccion) SELECT (SELECT id FROM albums LIMIT 1), (SELECT id FROM reproduccion WHERE habilitado = 1 AND esalbummaestro = 1 LIMIT 1) WHERE (SELECT Count(contenidodereproduccion.id) FROM contenidodereproduccion INNER JOIN(reproduccion, albums) ON reproduccion.id = contenidodereproduccion.idreproduccion AND albums.id = contenidodereproduccion.idalbum WHERE reproduccion.habilitado = 1 and reproduccion.esalbummaestro = 1)= 0;");
        regre.ejecutarquery("UPDATE contenidodereproduccion SET idreproduccion = (SELECT id FROM reproduccion LIMIT 1) WHERE (SELECT COUNT(reproduccion.id) FROM reproduccion WHERE contenidodereproduccion.idreproduccion = reproduccion.id) = 0;");
        regre.ejecutarquery("UPDATE contenidodereproduccion SET idalbum = (SELECT id FROM albums LIMIT 1) WHERE (SELECT COUNT(albums.id) FROM albums WHERE contenidodereproduccion.idalbum = albums.id) = 0;");


//      System.out.println("fin");
/*try (
Statement stmt = conexion().createStatement()
){
stmt.execute("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
}catch (SQLException e) {
System.out.println(e.getMessage());
}*/
    }

    /*public void verificarbasededatos() {
    try (
    Statement stmt = conexion().createStatement()
    ){
    stmt.execute("INSERT INTO albums (nombre) SELECT 'album1' WHERE ( SELECT COUNT(id) FROM albums ) = 0;");
    }catch (SQLException e) {
    System.out.println(e.getMessage());
    }
    }*/
    public Object opcion(String nombre) {
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND texto IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)) {
                    return opcioncadena(nombre, "");
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND numero IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)) {
                    return opcionnumero(nombre, 0);
                }
            }
            rs.close();
            consulta.close();
            consulta = regre.consulta("SELECT COUNT(id) FROM opciones WHERE nombre = ? AND binario IS NOT NULL ;");
            consulta.setString(1, nombre);
            rs = consulta.executeQuery();
            if (rs.next()) {
                if ((rs.getInt(1) != 0)) {
                    return opcionboolean(nombre, false);
                }
            }
            rs.close();
            consulta.close();
        } catch (SQLException e) {
            log.warning("Error al obtener opcion generalizada: " + e.getMessage());
            System.out.println(e.getMessage());

        }
        return null;
    }

    public Object opcion(String nombre, Object predeterminado) {
        if (predeterminado != null) {
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

    public Object opcion(String nombre, Object predeterminado, Integer tipo) {
        if (predeterminado != null) {
            if (predeterminado instanceof Integer) {
                return opcionnumero(nombre, (Integer) predeterminado);
            }
            if (predeterminado instanceof String) {
                return opcioncadena(nombre, (String) predeterminado);
            }
            if (predeterminado instanceof Boolean) {
                return opcionboolean(nombre, (Boolean) predeterminado);
            }
        } else {
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
        boolean entro = false;
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener opcion cadena con predeterminado: " + e.getMessage());
        }
        if (!entro) {
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return !(preparar == 0);
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
        } catch (SQLException e) {
            log.warning("Error al obtener opcion boolean: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return !(preparar == 0);
    }

    public int opcionnumero(String nombre, Integer predeterminado) {
        Boolean entro = false;
        int preparar = 0;
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT numero FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                entro = Boolean.TRUE;
                preparar = rs.getInt("numero");
            }

        } catch (SQLException e) {
            log.warning("Error al obtener opcion numero con predeterminado: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        if (!entro) {
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return preparar;
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

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener opcion numero: " + e.getMessage());
        }
        return preparar;
    }

    public String opcioncadena(String nombre, String predeterminado) {
        Boolean entro = false;
        String preparar = "";
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT texto FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                entro = true;
                preparar = rs.getString("texto");
            }

        } catch (SQLException e) {
            log.warning("Error al obtener opcion cadena con predeterminado: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        if (!entro) {
            guardaropcion(nombre, predeterminado);
            return predeterminado;
        }
        return preparar;
    }

    public String opcioncadena(String nombre) {
        String preparar = "";
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT texto FROM opciones WHERE nombre = ?;");
            consulta.setString(1, nombre);
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                preparar = rs.getString("texto");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al obtener opcion cadena: " + e.getMessage());
        }
        return preparar;
    }

    public void guardaropcion(String nombre, String texto) {
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al guardar opcion texto: " + e.getMessage());
        }
    }

    public void guardaropcion(String nombre, int numero) {
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; ");
            consulta.setString(1, nombre);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
            consulta = regre.consulta("UPDATE opciones SET numero = ? WHERE nombre = ?;");
            consulta.setInt(1, numero);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al guardar opcion numero: " + e.getMessage());
        }
    }

    void marcarreproducido(String cancion) {

        try {
            basededatos regre = new basededatos();
            PreparedStatement consult = regre.consulta("UPDATE musica SET reproducido = 1, ultimareproduccion = datetime('now','localtime') WHERE nombreyruta = ?;");
            consult.setString(1, cancion);
            consult.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al marcar como reproducido: " + e.getMessage());
        }
    }

    void desmarcarreproducido() {
        basededatos regre = new basededatos();
        regre.ejecutarquery("UPDATE musica SET reproducido = 0;");
    }

    static void anularcancion(String nombre) {
        tareasprogramadas registrarmusic = tareasprogramadas.getInstance();
        registrarmusic.anularmusica(nombre);
    }

    static void habilitarcancion(String nombre) {
        tareasprogramadas registrarmusic = tareasprogramadas.getInstance();
        registrarmusic.habilitarmusica(nombre);
    }

    static void registrarmusica(String nombre) {
        tareasprogramadas registrarmusic = tareasprogramadas.getInstance();//= new tareasprogramadas();
        registrarmusic.agregarmusica(nombre);
    }

    void eliminarmusica(String nombre) {

        try {
            basededatos regre = new basededatos();
            PreparedStatement consulta = regre.consulta("DELETE FROM musica WHERE nombreyruta = ?;");
            consulta.setString(1, nombre);
            consulta.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al eliminar musica en comunicacion: " + e.getMessage());
        }
    }

    public void guardaropcion(String nombre, boolean bol) {
        int numero = 0;
        if (bol) {
            numero = 1;
        } else {
            numero = 0;
        }
        try {
            basededatos regre = new basededatos();
            PreparedStatement consulta = regre.consulta("INSERT INTO opciones (nombre) SELECT ? WHERE (SELECT count(id) FROM opciones WHERE nombre = ?) = 0; ");
            consulta.setString(1, nombre);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
            consulta = regre.consulta("UPDATE opciones SET binario = ? WHERE nombre = ?;");
            consulta.setInt(1, numero);
            consulta.setString(2, nombre);
            consulta.executeUpdate();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al guardar opcion boolean: " + e.getMessage());
        }
    }

    public String proximotema() {
        String temaelegido = "";
        try {
            basededatos regre = new basededatos();
            Boolean reproduccionmaestra = false;
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
            do {
                //verifico si hay musica para reproducir
                ResultSet rs = regre.regresardatos("SELECT COUNT(musica.id) FROM musica INNER JOIN (reproduccion, contenidodereproduccion, programareproduccion) ON  programareproduccion.idreproduccion = reproduccion.id AND musica.album = contenidodereproduccion.idalbum AND reproduccion.Id = contenidodereproduccion.idreproduccion  WHERE reproduccion.habilitado = 1 AND musica.anulado = 0 AND (diadesemana = cast(strftime('%w', date('now')) as integer) AND  time(strftime('%H:%M',datetime('now','localtime'))) > time(programareproduccion.horadesde) AND time(strftime('%H:%M',datetime('now','localtime'))) < time(programareproduccion.horahasta));");
                if (!rs.next()) {
                    sinarchivos = true;
                    this.ultimotema = "";
                } else {
                    if (rs.getInt(1) == 0) {
                        sinarchivos = true;
                       // System.err.println("buscandocancion");
                        this.ultimotema = "";
                    }
                }

                rs.close();
                if (sinarchivos) {
                    rs = regre.regresardatos("SELECT COUNT(musica.id) FROM musica INNER JOIN (reproduccion, contenidodereproduccion) ON  musica.album = contenidodereproduccion.idalbum AND reproduccion.Id = contenidodereproduccion.idreproduccion  WHERE reproduccion.habilitado = 1 AND musica.anulado = 0 AND reproduccion.esalbummaestro = 1;");
                    if (!rs.next()) {
                        sinarchivos = true;
                        this.ultimotema = "";
                    } else {
                        if (rs.getInt(1) == 0) {
                            sinarchivos = true;
                            System.err.println("buscandocancion");
                            this.ultimotema = "";
                        } else {
                            sinarchivos = false;
                            reproduccionmaestra = true;
                        }
                    }
                }
                rs.close();
                if (sinarchivos == false) {
                    if (reproduccionmaestra)
                        rs = regre.regresardatos("SELECT COUNT(musica.id) FROM musica INNER JOIN (reproduccion, contenidodereproduccion) ON musica.album = contenidodereproduccion.idalbum WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1 AND musica.anulado = 0 AND reproduccion.esalbummaestro = 1;");
                    else
                        rs = regre.regresardatos("SELECT COUNT(musica.id) FROM musica INNER JOIN (reproduccion, contenidodereproduccion, programareproduccion) ON programareproduccion.idreproduccion = reproduccion.id AND musica.album = contenidodereproduccion.idalbum AND reproduccion.Id = contenidodereproduccion.idreproduccion  WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1 AND musica.anulado = 0 AND ((diadesemana = cast(strftime('%w', date('now')) as integer) AND  time(strftime('%H:%M',datetime('now','localtime'))) > time(programareproduccion.horadesde) AND time(strftime('%H:%M',datetime('now','localtime'))) < time(programareproduccion.horahasta)));");
                    while (rs.next()) {
                        if (rs.getInt(1) == 0) {
                            desmarcarreproducido();
                        }
                    }
                    rs.close();
                    if (reproduccionmaestra)
                        rs = regre.regresardatos("SELECT c.nombreyruta FROM (SELECT m.nombreyruta FROM musica m INNER JOIN (reproduccion, contenidodereproduccion) ON m.album = contenidodereproduccion.idalbum AND reproduccion.id=contenidodereproduccion.idreproduccion WHERE m.reproducido = 0 AND reproduccion.habilitado = 1 AND m.anulado = 0 AND reproduccion.esalbummaestro = 1 ORDER BY datetime(ultimareproduccion, 'localtime') LIMIT (SELECT CAST(COUNT(musica.id) * 0.4 AS int) + 1 FROM musica INNER JOIN (reproduccion, contenidodereproduccion) ON musica.album = contenidodereproduccion.idalbum AND reproduccion.Id = contenidodereproduccion.idreproduccion WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1 AND musica.anulado = 0 AND reproduccion.esalbummaestro = 1)) c ORDER BY RANDOM() LIMIT 1;");
                        else
                    rs = regre.regresardatos("SELECT c.nombreyruta FROM (SELECT m.nombreyruta FROM musica m INNER JOIN (reproduccion, contenidodereproduccion, programareproduccion) ON programareproduccion.idreproduccion = reproduccion.id AND m.album = contenidodereproduccion.idalbum AND reproduccion.id=contenidodereproduccion.idreproduccion WHERE m.reproducido = 0 AND reproduccion.habilitado = 1 AND m.anulado = 0 AND (diadesemana = cast(strftime('%w', date('now')) as integer) AND  time(strftime('%H:%M',datetime('now','localtime'))) > time(programareproduccion.horadesde) AND time(strftime('%H:%M',datetime('now','localtime'))) < time(programareproduccion.horahasta)) ORDER BY datetime(ultimareproduccion, 'localtime') LIMIT (SELECT CAST(COUNT(musica.id) * 0.4 AS int) + 1 FROM musica INNER JOIN (reproduccion, contenidodereproduccion, programareproduccion) ON programareproduccion.idreproduccion = reproduccion.id AND musica.album = contenidodereproduccion.idalbum AND reproduccion.Id = contenidodereproduccion.idreproduccion WHERE musica.reproducido = 0 AND reproduccion.habilitado = 1 AND musica.anulado = 0 AND (diadesemana = cast(strftime('%w', date('now')) as integer) AND  time(strftime('%H:%M',datetime('now','localtime'))) > time(programareproduccion.horadesde) AND time(strftime('%H:%M',datetime('now','localtime'))) < time(programareproduccion.horahasta)) )) c ORDER BY RANDOM() LIMIT 1;");
                    while (rs.next()) {
                        temaelegido = rs.getString("nombreyruta");
                    }
                    rs.close();
                    if (temaelegido.equals("")) {
                        sinarchivos = true;
                    } else {
//System.err.println("Verificando tema :"+ temaelegido);
                        if (verificararchivo(temaelegido)) {
                            archivoencontrado = true;
                            marcarreproducido(temaelegido);
                        } else {
                            System.err.println("temaeliminado");
                            eliminarmusica(temaelegido);
                        }
                    }
                }

            } while (!archivoencontrado && !sinarchivos);

            this.ultimotema = new File(temaelegido).getName();
            //System.out.println("Reproduciendo: " + this.ultimotema);
            if (!this.ultimotema.equals(""))
                log.info("Reproduciendo: " + temaelegido);
        } catch (SQLException e) {
            log.warning("Error al seleccionar el proximo tema: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return temaelegido;
    }

    public String Ultimotema() {
        return this.ultimotema;
    }

    public void verificarmusica() {
        comprobardirectorios();
        System.out.println("se verificará el directorio " + directoriodemusica);
        verificaralbumdesdedirectorio(directoriodemusica);
        System.out.println("Fin");
    }

    void comprobardirectorios() {
        String predeterminado = "." + File.separator + "musica";
        if (main.isandroid())
            predeterminado = "/./sdcard/Download/";
        directoriodemusica = (String) opcion("carpetademusica", predeterminado);
        try {
            File f = new File(directoriodemusica);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            directoriodemusica = predeterminado;
            guardaropcion("carpetademusica", predeterminado);
            File f = new File(directoriodemusica);
            if (!f.exists())
                f.mkdirs();
        }
    }

    public String obtenernombredecancion(String direccioncompleta) {

        return (Paths.get(direccioncompleta)).getFileName().toString();
    }


    public ArrayList<String> albumnes() {
        ArrayList<String> retorna = new ArrayList<String>();
        try {
            PreparedStatement consulta;
            ResultSet rs;
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT nombre FROM albums;");
            rs = consulta.executeQuery();
            while (rs.next()) {
                retorna.add(rs.getString(1));
            }
            rs.close();
            consulta.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al loguear usuario: " + e.getMessage());
        }
        return retorna;
    }

    void verificaralbumdesdedirectorio(String directorioaverificar) {
        if (estadoverificandomusica)
            return;
        estadoverificandomusica = true;
        //  System.out.println("Verificando directorio");
//verificar los archivos que estan en el directorio y faltan en la base de datos
        String files;
//        String directorio;
        guardaropcion("ultimavezqueseverificaalbum", Objects.toString((System.currentTimeMillis() / 1000L), null));

        File folder = new File(directorioaverificar);
        File[] listOfFiles = folder.listFiles();
        if (!(listOfFiles == null)) {

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    files = listOfFile.getAbsolutePath();

                    if (files.endsWith(".mp3") || files.endsWith(".MP3") || files.endsWith(".mP3") || files.endsWith(".Mp3")) {
                        //System.out.println(" obtenido " + (Paths.get(listOfFile.getAbsolutePath())).getFileName().toString());
                        registrarmusica(files);
                    }
                } else if (listOfFile.isDirectory()) {
                    verificaralbumdesdedirectorio(listOfFile.getPath());

                }
            }
            estadoverificandomusica = false;
        }
//verificar los que estan en la base de datos y no en el directorio y eliminarlos
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT nombreyruta FROM musica ;");
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                if (!verificararchivo(rs.getString(1))) {
                    eliminarmusica(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            log.warning("Error al ejecutar el query de verificar si hay que eliminar musica, verificando directorio: " + e.getMessage());
        }

    }

    void verificarlistadereproduccion() {
        PreparedStatement consulta;
        try {
            basededatos regre = new basededatos();
            consulta = regre.consulta("SELECT COUNT(musica.id) FROM musica INNER JOIN reproduccion ON musica.album = reproduccion.idalbum WHERE reproduccion.habilitado =1;");
            ResultSet rs = consulta.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 0) ;
                {

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//SELECT COUNT(musica.id) FROM musica INNER JOIN reproduccion ON musica.album = reproduccion.idalbum WHERE reproduccion.habilitado	=1;
    }

    public boolean verificararchivo(String nombre) {
        File file = new File(nombre);
        return file.exists();
    }


}

