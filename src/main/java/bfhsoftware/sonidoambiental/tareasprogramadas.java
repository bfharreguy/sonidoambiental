package bfhsoftware.sonidoambiental;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class tareasprogramadas implements Runnable {
    private static final Logger log = Logger.getLogger(susesos.class.getName());
    private static Boolean registrandomusica = false;
    private static Boolean ocupadoregistrandomusica = false;
    private static ArrayList<ArrayList<String>> registros = null;
    private static boolean transaccion = false;

    private ArrayList<ArrayList<String>> registros() {
        if (registros == null) {
            registros = new ArrayList<ArrayList<String>>();
        }
        return registros;
    }
    private static void registrarcancion(ArrayList<ArrayList<String>> registrodecanciones) {
        //ArrayList<String> registrodecanciones = cancionesaregistrar();
        if (registrodecanciones.size() > 0) {
            String nombre = registrodecanciones.get(0).get(1);
            basededatos regre = new basededatos();
            comunicacion com = new comunicacion();
            if (transaccion == false) {
                regre.ejecutarquery("BEGIN TRANSACTION;");
                transaccion = true;
            }
            //System.out.println(registrodecanciones.get(0).get(1));
            switch (registrodecanciones.get(0).get(0)) {
                case "agregarcancion":
                    try {
                        ResultSet rs;
                        PreparedStatement consulta;
                        consulta = regre.consulta("SELECT COUNT(id) FROM musica WHERE nombreyruta = ?;");
                        consulta.setString(1, nombre);
                        rs = consulta.executeQuery();
                        if (rs.next()) {
                            if (rs.getInt(1)>0){
                                rs.close();
                                consulta.close();
                                break;
                            }
                        }
                        rs.close();
                        consulta.close();
                        consulta = regre.consulta("INSERT INTO musica (nombreyruta, album) SELECT ?, (SELECT id FROM albums LIMIT 1) WHERE (SELECT count(id) FROM musica WHERE nombreyruta = ?) = 0;");
                        consulta.setString(1, nombre);
                        consulta.setString(2, nombre);
                        consulta.execute();
                        consulta.close();
                    } catch (SQLException e) {
                        log.warning("Error al intentar agregar cancion: "+ e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
                case "agregarcancionyalbum":
                    try {
                        //System.out.println("Registrando " + nombre);
                        int album=0;
                        ResultSet rs;
                        PreparedStatement consulta;
                        consulta = regre.consulta("SELECT COUNT(id) FROM musica WHERE nombreyruta = ?;");
                        consulta.setString(1, nombre);
                        rs = consulta.executeQuery();
                        if (rs.next()) {
                            if (rs.getInt(1)>0){
                                rs.close();
                                consulta.close();
                                break;
                            }
                        }
                        rs.close();
                        consulta.close();
                        consulta = regre.consulta("SELECT id FROM albums WHERE nombre = ?;");
                        consulta.setString(1, registrodecanciones.get(0).get(2));
                        rs = consulta.executeQuery();
                        if (rs.next()) {
                            album = rs.getInt(1);
                        }
                        rs.close();
                        consulta.close();
                        consulta = regre.consulta("INSERT INTO musica (nombreyruta, album) SELECT ?, ? WHERE (SELECT count(id) FROM musica WHERE nombreyruta = ?) = 0;");
                        consulta.setString(1, nombre);
                        consulta.setInt(2, album);
                        consulta.setString(3, nombre);
                        System.out.println("agregando cancion " + nombre );
                        consulta.execute();
                        consulta.close();
                    } catch (SQLException e) {
                        log.warning("Error al intentar agregar canción y album : "+ e.getMessage());
                        System.out.println(e.getMessage());

                    }
                    break;
                case "anularcancion":
                    try {
                        PreparedStatement consulta = regre.consulta("UPDATE musica SET anulado = 1 WHERE nombreyruta LIKE ?;");
                        consulta.setString(1, "%" + nombre );
                        consulta.execute();
                        consulta.close();
                    } catch (SQLException e) {
                        log.warning("Error al intentar anular canción: "+ e.getMessage());
                        System.out.println(e.getMessage());

                    }
                    break;
                case "habilitarcancion":
                    try {
                        PreparedStatement consulta = regre.consulta("UPDATE musica SET anulado = 0 WHERE nombreyruta LIKE ?;");
                        consulta.setString(1, "%" + nombre );
                        consulta.execute();
                        consulta.close();
                    } catch (SQLException e) {
                        log.warning("Error al intentar habilitar canción: "+ e.getMessage());
                        System.out.println(e.getMessage());

                    }
                    break;
                case "eliminarcancion":
                    try {
                        File borrar = new File((String) com.opcion("carpetademusica") + File.separator + nombre);
                        borrar.delete();
                        PreparedStatement consulta = regre.consulta("DELETE FROM musica WHERE nombreyruta LIKE ?;");
                        consulta.setString(1, "%" + nombre );
                        consulta.execute();
                        consulta.close();
                        System.out.println("archivo borrado");
                    } catch (SQLException e) {
                        log.warning("Error al intentar eliminar canción: "+ e.getMessage());
                        System.out.println(e.getMessage());
                    }
                    break;
            }
            registrodecanciones.remove(0);
            if (registrodecanciones.size()== 0 && transaccion) {
                transaccion = false;
                regre.ejecutarquery("END TRANSACTION;");
            }
        } else {
            registrandomusica = false;
        }
    }
    @Override
    public void run() {
        tareasprogramadas.getInstance().tareaviva();
    }


    private synchronized void tareaviva() {

        while (true) {
            try {
                this.wait(20);
            } catch (InterruptedException e) {
                log.warning("Error en la tarea de tareasprogramadas: "+ e.getMessage());
            }
            // System.out.println(cancionesaregistrar().size());
            while (registrandomusica && !ocupadoregistrandomusica) {
                ocupadoregistrandomusica = true;
                registrarcancion(registros());
                ocupadoregistrandomusica = false;
            }
        }
    }

    private static tareasprogramadas ourInstance = new tareasprogramadas();

    public static tareasprogramadas getInstance() {
        return ourInstance;
    }

    public tareasprogramadas() {

    }

    public void anularmusica(String lista) {
        /*ArrayList<ArrayList<String>> inner = new ArrayList<ArrayList<String>>();

        inner.get(0).add("anularcancion");
        inner.get(0).add( lista);
        registros().addAll(inner);
        registrandomusica = true;*/
        ArrayList<String> inner = new ArrayList<String>();
        inner.add("anularcancion");
        inner.add(lista);
        registros().add(inner);
        registrandomusica = true;
        //System.out.println(cancionesaregistrar().size());
    }
    public void habilitarmusica(String lista) {
        ArrayList<String> inner = new ArrayList<String>();
        inner.add("habilitarcancion");
        inner.add(lista);
        registros().add(inner);
        registrandomusica = true;
    }
    public void borrarmusica(String lista) {
        /*ArrayList<ArrayList<String>> inner = new ArrayList<ArrayList<String>>();
        //ArrayList<String> inner = new ArrayList<String>();
        inner.get(0).add("eliminarcancion");
        inner.get(0).add( lista);
        registros().addAll(inner);*/
        ArrayList<String> inner = new ArrayList<String>();
        inner.add("eliminarcancion");
        inner.add(lista);
        registros().add(inner);
        registrandomusica = true;
        //System.out.println(cancionesaregistrar().size());
    }

    public void agregarmusica(String lista) {
        ArrayList<String> inner = new ArrayList<String>();
        inner.add("agregarcancion");
        inner.add(lista);
        registros().add(inner);
        registrandomusica = true;
        //System.out.println(cancionesaregistrar().size());
    }
    public void agregarmusica(String lista, String album) {
        ArrayList<String> inner = new ArrayList<String>();
        inner.add("agregarcancionyalbum");
        inner.add(lista);
        inner.add(album);
        registros().add(inner);
        registrandomusica = true;

    }
    public void agregarmusicamasiva(ArrayList<ArrayList<String>> lista) {
        registros().addAll(lista);
        registrandomusica = true;
    }
    /*private static tarearegistrarmusica ourInstance = new tarearegistrarmusica();

    public static tarearegistrarmusica getInstance() {
        return ourInstance;
    }

    private tarearegistrarmusica() {


    }*/
}
