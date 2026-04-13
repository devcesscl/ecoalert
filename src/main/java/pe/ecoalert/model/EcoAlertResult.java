package pe.ecoalert.model;

/**
 * Objeto de resultado que encapsula la respuesta completa del servicio de evaluación.
 * Principio aplicado: Separación de responsabilidades — el resultado es inmutable (final).
 */
public class EcoAlertResult {

    private final String nombreZona;
    private final double ire;
    private final String nivelAlerta;
    private final double tasaDeforestacion;

    public EcoAlertResult(String nombreZona, double tasaDeforestacion,
                          double ire, String nivelAlerta) {
        this.nombreZona         = nombreZona;
        this.tasaDeforestacion  = tasaDeforestacion;
        this.ire                = ire;
        this.nivelAlerta        = nivelAlerta;
    }

    public String getNombreZona()        { return nombreZona; }
    public double getIre()               { return ire; }
    public String getNivelAlerta()       { return nivelAlerta; }
    public double getTasaDeforestacion() { return tasaDeforestacion; }

    @Override
    public String toString() {
        return String.format(
            "Zona: %-35s | Tasa deforestación: %5.2f%% | IRE: %6.2f | Alerta: %s",
            nombreZona, tasaDeforestacion, ire, nivelAlerta
        );
    }
}
