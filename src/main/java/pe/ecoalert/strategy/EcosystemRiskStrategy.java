package pe.ecoalert.strategy;

/**
 * Interfaz para el Patrón Strategy de cálculo de riesgo ecológico.
 *
 * Permite agregar nuevos ecosistemas (ej: humedales, glaciares) sin modificar
 * el código existente — cumple el Principio Open/Closed (OCP).
 *
 * Principio aplicado: Patrón Strategy + Inversión de Dependencias (DIP).
 */
public interface EcosystemRiskStrategy {

    /**
     * Aplica el factor de riesgo del ecosistema a la tasa de deforestación.
     *
     * @param tasaDeforestacion porcentaje de área perdida (0-100)
     * @param hayIncendio       true si hay focos de calor activos en la zona
     * @return Índice de Riesgo Ecológico (IRE) calculado
     */
    double applyRisk(double tasaDeforestacion, boolean hayIncendio);

    /**
     * Determina el nivel de alerta en función del IRE calculado.
     *
     * @param ire Índice de Riesgo Ecológico
     * @return "BAJO", "MEDIO", "ALTO" o "CRÍTICO"
     */
    String getAlertLevel(double ire);
}
