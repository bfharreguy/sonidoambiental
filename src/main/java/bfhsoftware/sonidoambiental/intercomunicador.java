/*
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
package bfhsoftware.sonidoambiental;

/**
 *
 * @author Usuario
 */
public final class intercomunicador {
 
    /**
     * Objeto thread local.
     */
    public static final ThreadLocal<String> THREAD  = new ThreadLocal<>();
 
    /**
     * Constructor privado para impedir instacias de la clase.
     */
    private intercomunicador () {}
 
    /**
     * Adiciona el valor.
     * @param valor
     *
     */
    public static void agregarValor(String valor) {
        THREAD.set(valor);
    }
 
    /**
     * Elimina el valor del hilo, esto se hace porque es posible estar
     * trabajando en un pool de hilos reutilizables.
     */
    public static void eliminarValor() {
        THREAD.remove();
    }
 
    /**
     * Obtiene el valor.
     * @return
     */
    public static String obtenerValor() {
        return THREAD.get();
    }
}