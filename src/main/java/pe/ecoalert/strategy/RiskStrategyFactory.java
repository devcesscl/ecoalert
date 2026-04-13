package pe.ecoalert.strategy;

import pe.ecoalert.model.TipoEcosistema;

/**
 * Fábrica que devuelve la estrategia correcta según el tipo de ecosistema.
 * Patrón Factory Method — centraliza la creación de estrategias.
 */
public class RiskStrategyFactory {

    public static EcosystemRiskStrategy forEcosistema(TipoEcosistema tipo) {
        return switch (tipo) {
            case AMAZONIA -> new AmazoniaRiskStrategy();
            case ANDES    -> new AndesRiskStrategy();
            case COSTA    -> new CostaRiskStrategy();
        };
    }
}
