package pe.ecoalert.model;

/**
 * Enum que representa los biomas del Perú con su factor de sensibilidad ecológica.
 * Elimina los "strings mágicos" y centraliza los factores de riesgo.
 *
 * Factores basados en: MINAM (2022) — Indicadores de deforestación por bioma.
 *
 * Principio aplicado: Nombres descriptivos y eliminación de magic strings (Clean Code).
 */
public enum TipoEcosistema {

    AMAZONIA("Selva Amazónica", 1.8),
    ANDES("Bosques Andinos", 1.5),
    COSTA("Ecosistemas Costeros", 1.2);

    private final String descripcion;
    private final double factorSensibilidad;

    TipoEcosistema(String descripcion, double factorSensibilidad) {
        this.descripcion       = descripcion;
        this.factorSensibilidad = factorSensibilidad;
    }

    public String getDescripcion()       { return descripcion; }
    public double getFactorSensibilidad() { return factorSensibilidad; }
}
