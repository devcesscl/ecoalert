package pe.ecoalert.exception;

/**
 * Excepción específica para datos provenientes de fuentes satelitales
 * que presentan valores incoherentes (ej: pérdidas > área total).
 */
public class DatoSatelitalInvalidoException extends EcoAlertException {

    public DatoSatelitalInvalidoException(String mensaje, String zona) {
        super("[DATO SATELITAL INVÁLIDO] " + mensaje, zona);
    }
}
