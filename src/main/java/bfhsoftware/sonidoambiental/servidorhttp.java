/*
* Sistema creado por bfhsoftware para musica ambiental
* Bernardo harreguy, Derechos reservados
* and open the template in the editor.
*/
package bfhsoftware.sonidoambiental;
import java.nio.charset.*;
import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class servidorhttp {
    /* Clase privada necesaria para generar un mensaje de respuesta en JSON */
    private class RespuestaMensaje {
        private String mensaje;
        private boolean error = false;
        /*  public void cambiarmensaje(String mensaje){
        this.mensaje = mensaje;
        }*/
        public RespuestaMensaje(String mensaje, boolean error) {
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
        private List<String> mensajes;
        private boolean error = false;
        
        public RespuestaMatriz(String mensaje, boolean error) {
            this.mensajes = new ArrayList<String>();
            mensajes.add(mensaje);
            mensajes.add("Primero");
            mensajes.add("Segundo");
            mensajes.add("Tercero");
            this.error = error;
        }
    }
    
    /* Instanciamos esta clase y la ejecutamos */
    public static void main(final String... args) throws IOException {
        servidorhttp http = new servidorhttp();
        http.ejecutame();
    }
    
    public void ejecutame() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 80), 10);
        /* Controlamos el contexto general para descargar archivos estáticos en la ruta actual */
        server.createContext("/", he -> {
            try {
                String archivo =he.getRequestURI().getPath();
                ClassLoader classLoader = servidorhttp.class.getClassLoader();
                if (archivo.equals("/") ) {
                    archivo = "/index.html";
                } else if (archivo.startsWith("/")) {
                    //archivo = archivo.substring(1,archivo.length());
                }
                /*
                
                InputStream in = getClass().getResourceAsStream("archivo_en_jar");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                in.available();
                System.out.println(he.getRequestURI().getPath());
                
                File file;
                if (classLoader.getResource(he.getRequestURI().getPath().substring(1,he.getRequestURI().getPath().length()))==null) {
                System.out.println("archivo no existe");
                }
                
                if (he.getRequestURI().getPath().equals("/") ) {
                file = new File((classLoader.getResource("index.html")).getFile());
                //System.out.println("!");
                }else{
                //System.out.println("!!");
                // if (he.getRequestURI().getPath().startsWith("/")){
                file = new File((classLoader.getResource(he.getRequestURI().getPath().substring(1,he.getRequestURI().getPath().length()))).getFile());/*
                }else{
                file = new File((classLoader.getResource(he.getRequestURI().getPath())).getFile());
                }
                
                }
                System.out.println(file.length());
                
                /* Comprobamos la existencia del archivo a partir de la ruta www del directorio actual ("./www") */
                //File file = new File("./www", he.getRequestURI().getPath());
                /* Si es un directorio cargamos el index.html (dará "not found" si éste no existe) */
                
                /*if ((file.isDirectory()) || (he.getRequestURI().getPath().equals("/") )) {
                file = new File((classLoader.getResource("index.html")).getFile());
                //file = new File(file, "index.html");
                System.out.println("aqui");
                }*/
                InputStream fs = getClass().getResourceAsStream(archivo);
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
                    String response = "Error 404: El archivo \"" + he.getRequestURI().getPath() + "\" no existe.";
                    he.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, response.length());
                    OutputStream output = he.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                    output.close();
                    
                }} catch (Exception e) {
                    System.out.println(e.getLocalizedMessage() +e.getMessage());
                } finally {
                he.close();
            }
        });
        /* Controlamos el contexto que hará peticiones REST/JSON a nuestro servicio */
        server.createContext("/json/", he -> {
            try {
                /* Definimos las variables de uso común */
                Gson gson = new Gson();
                final String responseBody;
                final byte[] rawResponseBody;
                RespuestaMensaje respuesta;
                /* Agregamos un mínimo de información de depuración */
                //System.out.println(he.getRequestMethod() + " \"" + he.getRequestURI().getPath() + "\"");
                /* Obtenemos el método usado (en mayúsculas, por si se recibe de otra forma) para saber qué hacer */
                switch (he.getRequestMethod().toUpperCase()) {
                    case "GET":
                        /* Creamos una instancia de Respuesta para ser convertida en JSON */
                        respuesta = new RespuestaMensaje(he.getRequestURI().getPath().substring(he.getHttpContext().getPath().length()), false);
                         /* Creamos un JSON usando GSON */
                        responseBody = gson.toJson(respuesta);
                        /* Enviamos la cabecera HTTP para indicar que la respuesta serán datos JSON */
                        he.getResponseHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
                        /* Convertimos la cadena JSON en una matriz de bytes para ser entregados al navegador */
                        rawResponseBody = responseBody.getBytes(StandardCharsets.UTF_8);
                        he.sendResponseHeaders(HttpURLConnection.HTTP_OK, rawResponseBody.length);
                        he.getResponseBody().write(rawResponseBody);
                        break;
                    case "POST":
                        /* Obtenemos el mensaje enviado mediante POST */
                        java.util.Scanner s = new java.util.Scanner(he.getRequestBody(), StandardCharsets.UTF_8.toString()).useDelimiter("\\A");
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
        });
        /* Controlamos el contexto que hará peticiones en texto a nuestro servicio */
        server.createContext("/texto/", he -> {
            try {
                /* Definimos las variables de uso común */
                final String responseBody;
                final byte[] rawResponseBody;
                /* Agregamos un mínimo de información de depuración */
                System.out.println(he.getRequestMethod() + " \"" + he.getRequestURI().getPath() + "\"");
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
                        java.util.Scanner s = new java.util.Scanner(he.getRequestBody(), StandardCharsets.UTF_8.toString()).useDelimiter("\\A");
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
        });
        /* Efectuamos el arranque del servidor, quedando la ejecución bloqueada a partir de aquí */
        server.start();
    }
}