/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;
import bfhsoftware.sonidoambiental.comunicacion.canciones;
import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Usuario
 */
public class servidorhttp implements Runnable {
    static String carpetademusica;
    // utilizar archivos en vez de internos
    boolean archivos = true;

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
            reproductor repo  = new reproductor();
            //  System.out.println(mensaje);
            if (mensaje.startsWith("LoGuEo:") && mensaje.substring(7).length() != 0){
                comunicacion logueo = new comunicacion();
                System.out.println((mensaje.substring(7, 7 + mensaje.substring(7).indexOf(":"))) + " " + mensaje.substring(8 + mensaje.substring(7).indexOf(":")));
                mensaje = logueo.loguear((mensaje.substring(7, 7 + mensaje.substring(7).indexOf(":"))), mensaje.substring(8 + mensaje.substring(7).indexOf(":")));
                System.out.println(mensaje);
            }
            if (mensaje.startsWith("CrEaR:") && mensaje.substring(6).length() > 1){
                comunicacion logueo = new comunicacion();
                mensaje = logueo.crearusuario((mensaje.substring(6, 6 + mensaje.substring(6).indexOf(":"))), mensaje.substring(7 + mensaje.substring(6).indexOf(":")));
            }
            switch (mensaje){
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
                    /*case "pausar-reproducir":
                    repo.pausar();
                    System.out.println("pausa");
                    break;*/
                    /*     case "listado":
                    comunicacion lis = new comunicacion();
                    this.mensajes = lis.listadodecanciones(true);
                    /*      mensajes.get(0).add(mensaje);
                    mensajes.get(0).add("Primero");
                    mensajes.get(0).add("Segundo");
                    mensajes.get(0).add("Tercero");
                    break;*/
            }
            /*if (mensaje.equals("musica")){
            System.out.println("funciona puto");
            }*/
            this.mensaje = mensaje;
            this.error = error;
        }
    }
    
    /* Clase privada necesaria para obtener una consulta en JSON */
    private class ConsultaMensaje {
        private String mensaje;
        
        public String getMensaje() {
            
            return mensaje;
        }
    }
    private class RespuestaMatriz {
        // private canciones[] listadecanciones;
        private boolean error = false;
        private List<String> mensajes;
        public RespuestaMatriz(String mensaje, boolean error) {
            //this.mensajes = new ArrayList<List<String>>();
            //mensajes.add(new ArrayList<String>());
            switch (mensaje){
                case "listado":
                    mensajes.add(mensaje);
                    mensajes.add("Primero");
                    mensajes.add("Segundo");
                    mensajes.add("Tercero");
                    break;
            }
            this.error = error;
        }
    }
    private class Respuestalistadecanciones {
        private canciones[] listadecanciones;
        private boolean error = false;
        public Respuestalistadecanciones(String mensaje, boolean error) {
            //this.mensajes = new ArrayList<List<String>>();
            //mensajes.add(new ArrayList<String>());
            //switch (mensaje){
            //    case "listado":
            comunicacion lis = new comunicacion();
            this.listadecanciones = lis.listadodecanciones(false).clone();
            /*      mensajes.get(0).add(mensaje);
            mensajes.get(0).add("Primero");
            mensajes.get(0).add("Segundo");
            mensajes.get(0).add("Tercero");*/
            //        break;
            // }
            this.error = error;
        }
    }
    /* Instanciamos esta clase y la ejecutamos */
    /*public static void main(final String... args) throws IOException {
    servidorhttp http = new servidorhttp();
    http.ejecutame();
    }*/
    class raiz implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            try {
                String archivo =he.getRequestURI().getPath();
                InputStream fs = null;
                if (archivos) {
                    File file = new File("./src/main/resources", he.getRequestURI().getPath());
                    /* Si es un directorio cargamos el index.html (dará "not found" si éste no existe) */
                    if (file.isDirectory()) {
                        file = new File(file, "/index.html");
                        archivo = "/index.html";
                    }
                    fs = new FileInputStream(file);
                }
                else
                {
                    //este es el codigo oficial
                    if (archivo.equals("/") ) {
                        //System.out.println("es raiz");
                        archivo = "/index.html";
                    }
                    fs = getClass().getResourceAsStream(archivo);
                }
                if (fs!=null) {
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
                    }
                    
                }} catch (IOException e) {
                    System.out.println(e.getLocalizedMessage() +e.getMessage());
                } finally {
                he.close();
            }
        }}
    class json implements HttpHandler {
        public void handle(HttpExchange he) throws IOException {
            try {
                /* Definimos las variables de uso común */
                Gson gson = new Gson();
                final String responseBody;
                final byte[] rawResponseBody;
                RespuestaMensaje respuesta;
                RespuestaMatriz respuesta2;
                Respuestalistadecanciones respuesta3;
                /* Agregamos un mínimo de información de depuración */
                // System.out.println(he.getRequestMethod() + " \"" + he.getRequestURI().getPath() + "\"");
                /* Obtenemos el método usado (en mayúsculas, por si se recibe de otra forma) para saber qué hacer */
                switch (he.getRequestMethod().toUpperCase()) {
                    case "GET":
                        /* Creamos una instancia de Respuesta para ser convertida en JSON */
                        switch (he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length())){
                            case "lista":
                                respuesta3 = new Respuestalistadecanciones(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                                responseBody = gson.toJson(respuesta3);
                                System.out.println("pasando lista de musica ");
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
                            ConsultaMensaje consulta = gson.fromJson(s.next(), ConsultaMensaje.class);
                            respuesta = new RespuestaMensaje(consulta.getMensaje(), false);
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
                System.out.println(e.getMessage());
            } finally {
                he.close();
            }
        }
    }
    @Override
    public void run (){
        try {
            ejecutame();
        } catch (IOException ex) {
            Logger.getLogger(servidorhttp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void ejecutame() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 10);
        /* Controlamos el contexto general para descargar archivos estáticos en la ruta actual */
        server.createContext("/", new raiz());
        /* Controlamos el contexto que hará peticiones REST/JSON a nuestro servicio */
        server.createContext("/json/", new json());
        /* Controlamos el contexto que hará peticiones en texto a nuestro servicio */
        server.createContext("/texto/", new texto());
        /* Efectuamos el arranque del servidor, quedando la ejecución bloqueada a partir de aquí */
        server.createContext("/upload/", new subirarchivos());
        server.start();
    }
    static class subirarchivos implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            for(Entry<String, List<String>> header : t.getRequestHeaders().entrySet()) {
                System.out.println(header.getKey() + ": " + header.getValue().get(0));
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
                //t.getResponseHeaders();
                
                
                //       if (directory.exists(carpetamusica))     
                t.getResponseHeaders().add("Content-type", "text/plain");
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                //FileOutputStream fop = null;
                File file;
               //comunicacion com = new comunicacion ();
                for(FileItem fi : result) {                    
                    file = new File(carpetademusica + File.separator + (Paths.get(fi.getName())).getFileName().toString());
                    OutputStream outputStream = new FileOutputStream(file);
                    IOUtils.copy(fi.getInputStream(), outputStream);                    
                    outputStream.close();
                    os.write(fi.getName().getBytes());
                    os.write("\r\n".getBytes());
                    System.out.println("Archivo que se subio: " + fi.getName());
                    /*com.registrarmusica(carpetademusica + File.separator + (Paths.get(fi.getName())).getFileName().toString());                    
                    System.out.println("Registrado");*/
                }
                
                os.close();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error");
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
                System.out.println(e.getMessage());
            } finally {
                he.close();
            }
        }
    }
}