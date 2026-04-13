package pe.ecoalert.strategy;

/**
 * Estrategia de riesgo para la Selva Amazónica del Perú.
 * Factor 1.8 — mayor sensibilidad por biodiversidad y masa forestal densa.
 * Fuente: MINAM (2022), Indicadores de deforestación por bioma.
 */
public class AmazoniaRiskStrategy implements EcosystemRiskStrategy {

    private static final double FACTOR_BIOMA    = 1.8;
    private static final double FACTOR_INCENDIO = 1.3;

    @Override
    public double applyRisk(double tasaDeforestacion, boolean hayIncendio) {
        double ire = tasaDeforestacion * FACTOR_BIOMA;
        if (hayIncendio) ire *= FACTOR_INCENDIO;
        return Math.round(ire * 100.0) / 100.0;
    }

    @Override
    public String getAlertLevel(double ire) {
        if (ire < 10)  return "BAJO";
        if (ire < 30)  return "MEDIO";
        if (ire < 60)  return "ALTO";
        return "CRÍTICO";
    }
}
