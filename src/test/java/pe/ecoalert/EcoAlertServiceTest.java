package pe.ecoalert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.ecoalert.exception.DatoSatelitalInvalidoException;
import pe.ecoalert.exception.EcoAlertException;
import pe.ecoalert.model.EcoAlertResult;
import pe.ecoalert.model.TipoEcosistema;
import pe.ecoalert.model.ZonaEcologica;
import pe.ecoalert.service.EcoAlertService;
import pe.ecoalert.strategy.AmazoniaRiskStrategy;
import pe.ecoalert.strategy.AndesRiskStrategy;
import pe.ecoalert.strategy.CostaRiskStrategy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para EcoAlertService.
 * Generadas con GitHub Copilot y validadas manualmente con datos reales del MINAM.
 */
@DisplayName("EcoAlertService — Pruebas Unitarias")
class EcoAlertServiceTest {

    private EcoAlertService servicioAmazonia;
    private EcoAlertService servicioAndes;
    private EcoAlertService servicioCosta;

    @BeforeEach
    void setUp() {
        servicioAmazonia = new EcoAlertService(new AmazoniaRiskStrategy());
        servicioAndes    = new EcoAlertService(new AndesRiskStrategy());
        servicioCosta    = new EcoAlertService(new CostaRiskStrategy());
    }

    // ── CASOS EXITOSOS ────────────────────────────────────────────────────

    @Test
    @DisplayName("IRE Amazonía sin incendio → alerta MEDIO")
    void testIRE_Amazonia_SinIncendio() {
        // Área: 1000 ha | Perdidas: 100 ha → tasa = 10%
        // IRE = 10 * 1.8 = 18.0 → MEDIO
        ZonaEcologica zona = new ZonaEcologica("Test Amazonia", 1000, 100,
                TipoEcosistema.AMAZONIA, false);
        EcoAlertResult resultado = servicioAmazonia.evaluarZona(zona);

        assertEquals(18.0,   resultado.getIre(),         0.001);
        assertEquals("MEDIO", resultado.getNivelAlerta());
        assertEquals(10.0,   resultado.getTasaDeforestacion(), 0.001);
    }

    @Test
    @DisplayName("IRE Andes con incendio → alerta CRÍTICO")
    void testIRE_Andes_ConIncendio() {
        // Área: 500 ha | Perdidas: 200 ha → tasa = 40%
        // IRE = 40 * 1.5 * 1.3 = 78.0 → CRÍTICO
        ZonaEcologica zona = new ZonaEcologica("Test Andes", 500, 200,
                TipoEcosistema.ANDES, true);
        EcoAlertResult resultado = servicioAndes.evaluarZona(zona);

        assertEquals(78.0,     resultado.getIre(),         0.001);
        assertEquals("CRÍTICO", resultado.getNivelAlerta());
    }

    @Test
    @DisplayName("IRE Costa sin incendio → alerta BAJO")
    void testIRE_Costa_SinIncendio() {
        // Área: 10000 ha | Perdidas: 50 ha → tasa = 0.5%
        // IRE = 0.5 * 1.2 = 0.6 → BAJO
        ZonaEcologica zona = new ZonaEcologica("Test Costa", 10000, 50,
                TipoEcosistema.COSTA, false);
        EcoAlertResult resultado = servicioCosta.evaluarZona(zona);

        assertEquals(0.6,    resultado.getIre(),         0.001);
        assertEquals("BAJO", resultado.getNivelAlerta());
    }

    @Test
    @DisplayName("El incendio activo incrementa el IRE en factor 1.3")
    void testFactorIncendioAplicadoCorrectamente() {
        ZonaEcologica sinIncendio = new ZonaEcologica("Sin", 1000, 100, TipoEcosistema.AMAZONIA, false);
        ZonaEcologica conIncendio = new ZonaEcologica("Con", 1000, 100, TipoEcosistema.AMAZONIA, true);

        double ireBase      = servicioAmazonia.evaluarZona(sinIncendio).getIre();
        double ireConFuego  = servicioAmazonia.evaluarZona(conIncendio).getIre();

        assertEquals(ireBase * 1.3, ireConFuego, 0.01);
    }

    // ── CASOS DE EXCEPCIÓN ────────────────────────────────────────────────

    @Test
    @DisplayName("Zona nula → EcoAlertException")
    void testExcepcion_ZonaNula() {
        assertThrows(EcoAlertException.class,
                () -> servicioAmazonia.evaluarZona(null));
    }

    @Test
    @DisplayName("Área total <= 0 → EcoAlertException (evita división por cero)")
    void testExcepcion_AreaNegativa() {
        ZonaEcologica zona = new ZonaEcologica("Test", -500, 100,
                TipoEcosistema.AMAZONIA, false);
        assertThrows(EcoAlertException.class,
                () -> servicioAmazonia.evaluarZona(zona));
    }

    @Test
    @DisplayName("Hectáreas perdidas negativas → DatoSatelitalInvalidoException")
    void testExcepcion_HectareasPerdidasNegativas() {
        ZonaEcologica zona = new ZonaEcologica("Test", 1000, -50,
                TipoEcosistema.AMAZONIA, false);
        assertThrows(DatoSatelitalInvalidoException.class,
                () -> servicioAmazonia.evaluarZona(zona));
    }

    @Test
    @DisplayName("Pérdidas > área total → DatoSatelitalInvalidoException")
    void testExcepcion_PérdidasSuperanAreaTotal() {
        ZonaEcologica zona = new ZonaEcologica("Test", 100, 150,
                TipoEcosistema.AMAZONIA, false);
        assertThrows(DatoSatelitalInvalidoException.class,
                () -> servicioAmazonia.evaluarZona(zona));
    }

    @Test
    @DisplayName("100% de pérdida → IRE máximo y alerta CRÍTICO")
    void testIRE_PerdidaTotalDelArea() {
        ZonaEcologica zona = new ZonaEcologica("Zona Devastada", 1000, 1000,
                TipoEcosistema.AMAZONIA, true);
        EcoAlertResult resultado = servicioAmazonia.evaluarZona(zona);

        // IRE = 100 * 1.8 * 1.3 = 234.0 → CRÍTICO
        assertEquals("CRÍTICO", resultado.getNivelAlerta());
        assertTrue(resultado.getIre() > 60);
    }
}
