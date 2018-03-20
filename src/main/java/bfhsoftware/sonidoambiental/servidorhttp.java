/*
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

import bfhsoftware.sonidoambiental.comunicacion.canciones;
import bfhsoftware.sonidoambiental.comunicacion.matriz;
import bfhsoftware.sonidoambiental.comunicacion.reproducciones;

//import bfhsoftware.sonidoambiental.comunicacion.;
import com.google.gson.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 * @author Usuario
 */
public class servidorhttp implements Runnable {
    private static final Logger log = Logger.getLogger(susesos.class.getName());
    static String carpetademusica;
    static String albumalqueguardar = "";
    static tareasprogramadas registrarmusic = tareasprogramadas.getInstance();
    //static comunicacion com = new comunicacion();
    // utilizar archivos en vez de internos
    static boolean archiv = true;

    servidorhttp(String directoriodemusica) {
        this.carpetademusica = directoriodemusica;
    }

    /* Clase privada necesaria para generar un mensaje de respuesta en JSON */
    private class RespuestaMensaje {
        private String mensaje;
        // private List<List<String>> mensajes;
        private boolean error = false;

        /*  public void cambiarmensaje(String mensaje){
        this.mensaje = mensaje;
        }*/
        public RespuestaMensaje(String mensaje, boolean error) {
            String caso = "";
            comunicacion logueo = new comunicacion();
            reproductor repo = new reproductor();
            if (mensaje.startsWith("LoGuEo:") && mensaje.substring(7).length() != 0) {
                //System.out.println((mensaje.substring(7, 7 + mensaje.substring(7).indexOf(":"))) + " " + mensaje.substring(8 + mensaje.substring(7).indexOf(":")));
                mensaje = logueo.loguear((mensaje.substring(7, 7 + mensaje.substring(7).indexOf(":"))), mensaje.substring(8 + mensaje.substring(7).indexOf(":")));
                //System.out.println(mensaje);
            }
            if (mensaje.startsWith("CrEaR:") && mensaje.substring(6).length() > 1) {
                mensaje = logueo.crearusuario((mensaje.substring(6, 6 + mensaje.substring(6).indexOf(":"))), mensaje.substring(7 + mensaje.substring(6).indexOf(":")));
            }
            if (mensaje.startsWith("anular:") && mensaje.substring(7).length() > 1) {
                logueo.anularcancion(mensaje.substring(7));
            }
            if (mensaje.startsWith("habilitar:") && mensaje.substring(10).length() > 1) {
                logueo.habilitarcancion(mensaje.substring(10));
            }
            if (mensaje.startsWith("borrar:") && mensaje.substring(7).length() > 1) {
                tareasprogramadas tarea = new tareasprogramadas();
                tarea.borrarmusica(mensaje.substring(7));
            }
            if (mensaje.startsWith("crearalbum:") && mensaje.substring(11).length() > 1) {
                mensaje = logueo.crearalbum(mensaje.substring(11));
            }
            if (mensaje.startsWith("eliminaralbum:") && mensaje.substring(14).length() > 1) {
                mensaje = logueo.eliminaralbum(mensaje.substring(14));
            }
            if (mensaje.startsWith("editaralbum:") && mensaje.substring(12).length() > 1) {
                mensaje = logueo.editaralbum((mensaje.substring(12, 12 + mensaje.substring(12).indexOf(":"))), mensaje.substring(13 + mensaje.substring(12).indexOf(":")));
                //System.out.println(mensaje);
            }
            if (mensaje.startsWith("cambiaralbum:") && mensaje.substring(13).length() > 1) {
                logueo.cambiardealbum((mensaje.substring(13, 13 + mensaje.substring(13).indexOf(":"))), mensaje.substring(14 + mensaje.substring(13).indexOf(":")));

            }
            if (mensaje.startsWith("subirarchivos:") && mensaje.substring(14).length() > 1) {
                albumalqueguardar = mensaje.substring(14).replace(" ", "");
                //System.out.println(albumalqueguardar);
            }
            caso = "nuevareproduccion:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.crearnuevareproduccion(mensaje.substring(caso.length()));
            }
            caso = "eliminarreproduccion:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.eliminarreproduccion(mensaje.substring(caso.length()));
            }
            caso = "deshabilitarreproduccion:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.deshabilitarreproduccion(mensaje.substring(caso.length()));
            }
            caso = "deshacerreproduccionmaestra:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.deshacerreproduccionmaestra(mensaje.substring(caso.length()));
            }
            caso = "hacerreproduccionmaestra:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.hacerreproduccionmaestra(mensaje.substring(caso.length()));
            }
            caso = "habilitarreproduccion:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.habilitarreproduccion(mensaje.substring(caso.length()));
            }
            caso = "quitaralbum:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.quitaralbumdereproduccion(mensaje.substring(caso.length(), caso.length() + mensaje.substring(caso.length()).indexOf(":")), mensaje.substring(caso.length() + 1 + mensaje.substring(caso.length()).indexOf(":")));
            }
            caso = "agregaralbum:";
            if (mensaje.startsWith(caso) && mensaje.substring(caso.length()).length() > 1) {
                logueo.agregaralbumdereproduccion(mensaje.substring(caso.length(), caso.length() + mensaje.substring(caso.length()).indexOf(":")), mensaje.substring(caso.length() + 1 + mensaje.substring(caso.length()).indexOf(":")));
            }
            switch (mensaje) {
                case "sinusuarios":
                    comunicacion comunicacion = new comunicacion();
                    if (comunicacion.sinusuarios())
                        mensaje = " ";
                    else
                        mensaje = "";
                    break;
                case "musica":
                    mensaje = repo.cancion();
                    break;
                case "estado":
                    mensaje = repo.estado();
                    break;

            }

            this.mensaje = mensaje;
            this.error = error;
        }
    }

    private class reproduccionmensaje {
        public Boolean lunes=false, martes=false, miercoles=false, jueves=false, viernes=false, sabado=false, domingo=false, eliminar=false, modificar=false;
        public String nombre="";
        public int diadesemana=0;
        public Long horahasta, horadesde;
        public String mensaje="", strhorahasta="", strhoradesde="";
    }

    /* Clase privada necesaria para obtener una consulta en JSON */
    private class ConsultaMensaje {
        private String mensaje;

        public String getMensaje() {

            return mensaje;
        }
    }


    private matriz[] delistadoamatriz(ArrayList<String> listado) {
        matriz[] retorna = new matriz[listado.size()];
        for (int x = 0; x < listado.size(); x++) {
            retorna[x] = new matriz(false, listado.get(x));
        }
        return retorna;
    }

    private class RespuestaMatriz {
        private matriz[] mensajes;

        private boolean error = false;

        public RespuestaMatriz(String mensaje, boolean error) {
            comunicacion com = new comunicacion();
            switch (mensaje) {
                case "album":
                    this.mensajes = delistadoamatriz(com.albumnes()).clone();
                    break;
            }
            this.error = error;
        }
        /*public RespuestaMatriz(String mensaje, boolean error) {
            //this.mensajes = new ArrayList<List<String>>();
            //mensajes.add(new ArrayList<String>());
            switch (mensaje) {
                case "album":
                    comunicacion com = new comunicacion();
                    mensajes.addAll(com.albumnes());
                    break;
                case "listado":
                    mensajes.add(mensaje);
                    mensajes.add("Primero");
                    mensajes.add("Segundo");
                    mensajes.add("Tercero");
                    break;
            }
            this.error = error;
        }*/
    }

    private class Respuestalistadecanciones {
        private canciones[] listadecanciones;
        private boolean error = false;

        public Respuestalistadecanciones(String mensaje, boolean error) {
            comunicacion lis = new comunicacion();
            this.listadecanciones = lis.listadodecanciones(false).clone();
            this.error = error;
        }
    }

    private class Respuestareproducciones {
        private reproducciones[] datosdereproduccion;
        private boolean error = false;

        public Respuestareproducciones(String mensaje, boolean error) {
            comunicacion lis = new comunicacion();
            this.datosdereproduccion = lis.obtenerreproducciones().clone();
            this.error = error;
        }
    }

    class raiz implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            File file;
            String archivo = he.getRequestURI().getPath();
            InputStream fs = null;
            if (archiv) {
                file = new File("./src/main/resources", he.getRequestURI().getPath());
                /* Si es un directorio cargamos el index.html (dará "not found" si éste no existe) */
                if (file.isDirectory()) {
                    file = new File(file, "/index.html");
                    archivo = "/index.html";
                }
                fs = new FileInputStream(file);
            } else {

                //este es el codigo oficial
                if (archivo.equals("/")) {
                    //System.out.println("es raiz");
                    archivo = "/index.html";
                }
                fs = getClass().getResourceAsStream(archivo);
            }
            if (fs != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
                /* Obtenemos el tipo mime del archivo para enviarlo en la cabecera correspondiente */
                he.getResponseHeaders().set("Content-Type", Files.probeContentType(Paths.get(archivo)));
                /* Enviamos las cabeceras HTTP OK junto con la longitud del contenido */
                he.sendResponseHeaders(HttpURLConnection.HTTP_OK, fs.available());
                /* Para no saturar la memoria ni el recolector de basura enviamos el archivo en trozos de 64K */
                OutputStream output = he.getResponseBody();
                //FileInputStream fs = new FileInputStream(file);
                // InputStream fs = getClass().getResourceAsStream("archivo_en_jar");
                //BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    output.write(buffer, 0, count);
                }
                output.flush();
                output.close();
                fs.close();
            } else {
                /* Si el archivo no existe lo indicamos así en el código de respuesta y el mensaje */
                String response = "Estas conectado al servidor pero hay un error en la peticion del archivo \"" + he.getRequestURI().getPath() + "\".";
                he.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, response.length());
                try (OutputStream output = he.getResponseBody()) {
                    output.write(response.getBytes());
                    output.flush();


                } catch (IOException e) {
                    log.warning("Error de servicio de archivos en el servicio web: " + e.getMessage());
                    System.out.println(e.getLocalizedMessage() + e.getMessage());
                } finally {
                    he.close();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            ejecutame();
        } catch (IOException e) {
            log.warning("Error de inicialización en el servicio web: " + e.getMessage());
        }
    }

    class json implements HttpHandler {
        public void handle(HttpExchange he) {
            try {
                Gson gson = new Gson();
                final String responseBody;
                final byte[] rawResponseBody;
                RespuestaMensaje respuesta;
                RespuestaMatriz respuesta2;

                switch (he.getRequestMethod().toUpperCase()) {
                    case "GET":
                        /* Creamos una instancia de Respuesta para ser convertida en JSON */
                        // System.out.println(he.getRequestURI());
                        switch (he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length())) {
                            case "lista":
                                Respuestalistadecanciones respuesta4;
                                respuesta4 = new Respuestalistadecanciones(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                                responseBody = gson.toJson(respuesta4);
                                break;
                            case "album":
                                respuesta2 = new RespuestaMatriz(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                                responseBody = gson.toJson(respuesta2);
                                break;
                            case "reproduccion":
                                Respuestareproducciones respuesta3;
                                respuesta3 = new Respuestareproducciones(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                                responseBody = gson.toJson(respuesta3);
                                break;
                            default:
                                respuesta = new RespuestaMensaje(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                                /* Creamos un JSON usando GSON */
                                responseBody = gson.toJson(respuesta);
                                break;
                        }
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos JSON */
                        he.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena JSON en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "POST":
                        /* Obtenemos el mensaje enviado mediante POST */
                        Scanner s = new Scanner(he.getRequestBody(), StandardCharsets.UTF_8.toString()).useDelimiter("\\A");
                        /* Si no hay ningún problema */
                        if (s.hasNext()) {
                            reproduccionmensaje consulta = gson.fromJson(s.next(), reproduccionmensaje.class);
                            comunicacion com = new comunicacion();
                            if (!consulta.eliminar && !consulta.modificar) {
                                for (int i = 1; i < 8; i++) {
                                    if ((consulta.lunes && i == 1) || (consulta.martes && i == 2) || (consulta.miercoles && i == 3) || (consulta.jueves && i == 4) || (consulta.viernes && i == 5) || (consulta.sabado && i == 6) || (consulta.domingo && i == 7)) {
                                        com.agregarprogramaciondereproduccion(consulta.nombre, i, consulta.horadesde, consulta.horahasta);
                                    }
                                }
                            } else if (consulta.eliminar)
                                com.eliminarprogramaciondereproduccion(consulta.nombre, consulta.diadesemana, consulta.strhoradesde, consulta.strhorahasta);
                                else if (consulta.modificar)
                                com.modificarprogramaciondereproduccion(consulta.nombre, consulta.diadesemana, consulta.strhoradesde, consulta.strhorahasta, consulta.horadesde, consulta.horahasta);
                            respuesta = new RespuestaMensaje("", false);
                        } else {
                            respuesta = new RespuestaMensaje("Error recibiendo datos", true);
                        }
                        responseBody = gson.toJson(respuesta);
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos JSON */
                        he.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena JSON en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "DELETE":
                        /* Creamos una instancia de Respuesta para ser convertida en JSON */
                        respuesta2 = new RespuestaMatriz(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                        /* Creamos un JSON usando GSON */
                        responseBody = gson.toJson(respuesta2);
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos JSON */
                        he.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena JSON en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "OPTIONS":
                        he.getResponseHeaders().set("Allow", "GET,POST,DELETE,OPTIONS");
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                        break;
                    default:
                        /* Si no se selecciona un método correcto devolvemos un BAD METHOD */
                        he.getResponseHeaders().set("Allow", "GET,POST,DELETE,OPTIONS");
                        he.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, -1);
                        break;
                }
            } catch (Exception e) {
                log.warning("Error al responder a travez del servidor web: " + e.getMessage());
                e.printStackTrace();
            } finally {
                he.close();
            }
        }
    }

    public void ejecutame() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 10);
        // Controlamos el contexto general para descargar archivos estáticos en la ruta actual
        server.createContext("/", new raiz());
        // Controlamos el contexto que hará peticiones REST/JSON a nuestro servicio
        server.createContext("/json/", new json());
        // Controlamos el contexto que hará peticiones en texto a nuestro servicio
        server.createContext("/texto/", new texto());
        //Efectuamos el arranque del servidor, quedando la ejecución bloqueada a partir de aquí
        server.createContext("/upload/", new subirarchivos());
        server.start();
    }

    static class subirarchivos implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            for (Entry<String, List<String>> header : t.getRequestHeaders().entrySet()) {
                //   System.out.println(header.getKey() + ": " + header.getValue().get(0));
            }
            DiskFileItemFactory d = new DiskFileItemFactory();

            try {
                ServletFileUpload up = new ServletFileUpload(d);
                List<FileItem> result = up.parseRequest(new RequestContext() {

                    @Override
                    public String getCharacterEncoding() {
                        return "UTF-8";
                    }

                    @Override
                    public int getContentLength() {
                        return 0; //tested to work with 0 as return
                    }

                    @Override
                    public String getContentType() {
                        return t.getRequestHeaders().getFirst("Content-type");
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return t.getRequestBody();
                    }

                });
                t.getResponseHeaders().add("Content-type", "text/plain");
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                File file;
                for (FileItem fi : result) {
                    String nombre = carpetademusica + File.separator + (Paths.get(fi.getName())).getFileName().toString();
                    file = new File(nombre);
                    OutputStream outputStream = new FileOutputStream(file);
                    IOUtils.copy(fi.getInputStream(), outputStream);
                    outputStream.close();
                    os.write(fi.getName().getBytes());
                    os.write("\r\n".getBytes());
                    System.out.println("Archivo que se subio: " + fi.getName());
                    if (!albumalqueguardar.equals(""))
                        registrarmusic.agregarmusica(file.getAbsolutePath(), albumalqueguardar);
                    else
                        registrarmusic.agregarmusica(file.getAbsolutePath());
                }
                os.close();
                reproductor.getInstance().iniciarreproduccion = true;
            } catch (Exception e) {
                e.printStackTrace();
                log.warning("Error de subida de archivos en el servicio web: " + e.getMessage());
            }
        }

    }

    class texto implements HttpHandler {
        public void handle(HttpExchange he) throws IOException {
            try {
                /* Definimos las variables de uso común */
                final String responseBody;
                final byte[] rawResponseBody;
                /* Agregamos un mínimo de información de depuración */
                //System.out.println(he.getRequestMethod() + " \"" + he.getRequestURI().getPath() + "\"");
                /* Obtenemos el método usado (en mayúsculas, por si se recibe de otra forma) para saber qué hacer */
                switch (he.getRequestMethod().toUpperCase()) {
                    case "GET":
                        /* Obtenemos la URL restante */
                        responseBody = he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length());
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos en texto plano */
                        he.getResponseHeaders().set("Content-Type", String.format("text/plain; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena de texto en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "POST":
                        /* Obtenemos el mensaje enviado mediante POST */
                        Scanner s = new Scanner(he.getRequestBody(), StandardCharsets.UTF_8.toString()).useDelimiter("\\A");
                        /* Si no hay ningún problema */
                        if (s.hasNext()) {
                            responseBody = s.next();
                        } else {
                            responseBody = "Error recibiendo datos";
                        }
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos en texto plano */
                        he.getResponseHeaders().set("Content-Type", String.format("text/plain; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "DELETE":
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                        break;
                    case "OPTIONS":
                        he.getResponseHeaders().set("Allow", "GET,POST,DELETE,OPTIONS");
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                        break;
                    default:
                        /* Si no se selecciona un método correcto devolvemos un BAD METHOD */
                        he.getResponseHeaders().set("Allow", "GET,POST,DELETE,OPTIONS");
                        he.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, -1);
                        break;
                }
            } catch (Exception e) {
                log.warning("Error de respuesta textual en el servicio web: " + e.getMessage());
                System.out.println(e.getMessage());
            } finally {
                he.close();
            }
        }
    }
}