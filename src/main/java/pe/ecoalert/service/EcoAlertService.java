package pe.ecoalert.service;

import pe.ecoalert.exception.DatoSatelitalInvalidoException;
import pe.ecoalert.exception.EcoAlertException;
import pe.ecoalert.model.EcoAlertResult;
import pe.ecoalert.model.ZonaEcologica;
import pe.ecoalert.strategy.EcosystemRiskStrategy;

/**
 * Servicio principal de evaluación ecológica.
 *
 * Recibe la estrategia de riesgo por inyección de dependencias,
 * lo que desacopla completamente el cálculo del tipo de ecosistema
 * y permite pruebas unitarias con mocks (Mockito).
 *
 * Principios aplicados:
 *  - SRP: solo orquesta el flujo de evaluación
 *  - OCP: no conoce los factores de ningún ecosistema
 *  - DIP: depende de la interfaz, no de implementaciones concretas
 */
public class EcoAlertService {

    private final EcosystemRiskStrategy riskStrategy;

    public EcoAlertService(EcosystemRiskStrategy riskStrategy) {
        this.riskStrategy = riskStrategy;
    }

    /**
     * Evalúa el riesgo ecológico de una zona y retorna un resultado completo.
     *
     * @param zona datos de la zona geográfica a evaluar
     * @return EcoAlertResult con IRE, nivel de alerta y tasa de deforestación
     * @throws EcoAlertException              si la zona es nula o el área es inválida
     * @throws DatoSatelitalInvalidoException si los datos satelitales son inconsistentes
     */
    public EcoAlertResult evaluarZona(ZonaEcologica zona) {
        validarZona(zona);

        double tasa   = (zona.getHectPerdidas() / zona.getHectTotales()) * 100;
        double ire    = riskStrategy.applyRisk(tasa, zona.tieneIncendioActivo());
        String alerta = riskStrategy.getAlertLevel(ire);

        return new EcoAlertResult(zona.getNombre(), tasa, ire, alerta);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Validación por contrato — Fail Fast antes de cualquier cálculo
    // ─────────────────────────────────────────────────────────────────────
    private void validarZona(ZonaEcologica zona) {
        if (zona == null)
            throw new EcoAlertException("La zona no puede ser nula.");

        if (zona.getHectTotales() <= 0)
            throw new EcoAlertException(
                "El área total debe ser mayor a 0 ha.", zona.getNombre());

        if (zona.getHectPerdidas() < 0)
            throw new DatoSatelitalInvalidoException(
                "Las hectáreas perdidas no pueden ser negativas.", zona.getNombre());

        if (zona.getHectPerdidas() > zona.getHectTotales())
            throw new DatoSatelitalInvalidoException(
                "Las pérdidas (" + zona.getHectPerdidas() + " ha) superan el área total ("
                + zona.getHectTotales() + " ha).", zona.getNombre());
    }
}
