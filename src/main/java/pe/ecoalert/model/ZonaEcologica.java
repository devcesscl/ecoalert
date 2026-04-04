package pe.ecoalert.model;

/**
 * Modelo que encapsula los datos geográficos y ambientales de una zona de monitoreo.
 * Fuente de datos: imágenes satelitales (GLAD / Global Forest Watch).
 *
 * Principio aplicado: Responsabilidad Única — solo almacena datos, no calcula (SRP).
 */
public class ZonaEcologica {

    private final String         nombre;
    private final double         hectTotales;
    private final double         hectPerdidas;
    private final TipoEcosistema ecosistema;
    private final boolean        incendioActivo;

    public ZonaEcologica(String nombre, double hectTotales, double hectPerdidas,
                         TipoEcosistema ecosistema, boolean incendioActivo) {
        this.nombre         = nombre;
        this.hectTotales    = hectTotales;
        this.hectPerdidas   = hectPerdidas;
        this.ecosistema     = ecosistema;
        this.incendioActivo = incendioActivo;
    }

    public String getNombre()             { return nombre; }
    public double getHectTotales()        { return hectTotales; }
    public double getHectPerdidas()       { return hectPerdidas; }
    public TipoEcosistema getEcosistema() { return ecosistema; }
    public boolean tieneIncendioActivo()  { return incendioActivo; }
}
