package pe.ecoalert.exception;

/**
 * Excepción base del dominio EcoAlert.
 * Extiende RuntimeException (unchecked) para no contaminar
 * las firmas de los métodos con throws declarativos.
 *
 * Principio aplicado: Clean Code Cap.7 — Error Handling (Martin, 2008)
 */
public class EcoAlertException extends RuntimeException {

    private final String zonaAfectada;

    public EcoAlertException(String mensaje) {
        super(mensaje);
        this.zonaAfectada = "No especificada";
    }

    public EcoAlertException(String mensaje, String zonaAfectada) {
        super(mensaje);
        this.zonaAfectada = zonaAfectada;
    }

    public String getZonaAfectada() {
        return zonaAfectada;
    }
}
