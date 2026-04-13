package pe.ecoalert;

import pe.ecoalert.exception.EcoAlertException;
import pe.ecoalert.model.EcoAlertResult;
import pe.ecoalert.model.TipoEcosistema;
import pe.ecoalert.model.ZonaEcologica;
import pe.ecoalert.service.EcoAlertService;
import pe.ecoalert.strategy.EcosystemRiskStrategy;
import pe.ecoalert.strategy.RiskStrategyFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║       EcoAlert Perú — Sistema de Monitoreo Ecológico            ║
 * ║       Producto Académico N° 1 — Desarrollo Ágil con IA          ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Aplicación de consola interactiva que permite:
 *   1. Evaluar zonas ecológicas ingresadas por el usuario
 *   2. Ver un reporte con datos reales del Perú (modo demo)
 *   3. Salir del sistema
 */
public class EcoAlertApp {

    // ─── Colores ANSI para la consola ─────────────────────────────────────
    private static final String RESET  = "\u001B[0m";
    private static final String VERDE  = "\u001B[32m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String ROJO   = "\u001B[31m";
    private static final String ROJO_F = "\u001B[1;31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String BOLD   = "\u001B[1m";

    public static void main(String[] args) {
        mostrarBanner();

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> evaluarZonaInteractiva(scanner);
                case "2" -> ejecutarDemo();
                case "3" -> { salir = true; System.out.println("\n" + CYAN + "✓ Sistema EcoAlert cerrado. ¡Cuida el planeta!" + RESET); }
                default  -> System.out.println(AMARILLO + "  Opción inválida. Ingrese 1, 2 o 3." + RESET);
            }
        }
        scanner.close();
    }

    // ─────────────────────────────────────────────────────────────────────
    // MODO INTERACTIVO — el usuario ingresa los datos de una zona
    // ─────────────────────────────────────────────────────────────────────
    private static void evaluarZonaInteractiva(Scanner sc) {
        System.out.println("\n" + BOLD + "  ── Evaluación de Zona Ecológica ──" + RESET);
        try {
            System.out.print("  Nombre de la zona: ");
            String nombre = sc.nextLine().trim();

            System.out.print("  Hectáreas totales de la zona: ");
            double hectTotales = Double.parseDouble(sc.nextLine().trim());

            System.out.print("  Hectáreas perdidas por deforestación: ");
            double hectPerdidas = Double.parseDouble(sc.nextLine().trim());

            System.out.println("  Tipo de ecosistema:");
            System.out.println("    1) Amazonía   2) Andes   3) Costa");
            System.out.print("  Seleccione (1-3): ");
            int tipoNum = Integer.parseInt(sc.nextLine().trim());
            TipoEcosistema tipo = switch (tipoNum) {
                case 1 -> TipoEcosistema.AMAZONIA;
                case 2 -> TipoEcosistema.ANDES;
                case 3 -> TipoEcosistema.COSTA;
                default -> throw new IllegalArgumentException("Opción de ecosistema inválida.");
            };

            System.out.print("  ¿Hay incendio activo en la zona? (s/n): ");
            boolean incendio = sc.nextLine().trim().equalsIgnoreCase("s");

            ZonaEcologica zona = new ZonaEcologica(nombre, hectTotales, hectPerdidas, tipo, incendio);
            EcosystemRiskStrategy estrategia = RiskStrategyFactory.forEcosistema(tipo);
            EcoAlertService servicio = new EcoAlertService(estrategia);

            EcoAlertResult resultado = servicio.evaluarZona(zona);
            mostrarResultado(resultado);

        } catch (NumberFormatException e) {
            System.out.println(ROJO + "  ✗ Error: ingrese solo números donde se requiere." + RESET);
        } catch (EcoAlertException e) {
            System.out.println(ROJO + "  ✗ Error ecológico: " + e.getMessage() + RESET);
            if (!e.getZonaAfectada().equals("No especificada"))
                System.out.println(ROJO + "    Zona afectada: " + e.getZonaAfectada() + RESET);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // MODO DEMO — datos reales de zonas peruanas (2023)
    // ─────────────────────────────────────────────────────────────────────
    private static void ejecutarDemo() {
        System.out.println("\n" + BOLD + CYAN + "  ══════════════════════════════════════════════════════" + RESET);
        System.out.println(BOLD + CYAN + "   REPORTE: Zonas de Monitoreo — Perú 2023" + RESET);
        System.out.println(CYAN + "   Fuente: Global Forest Watch / MINAM" + RESET);
        System.out.println(CYAN + "  ══════════════════════════════════════════════════════" + RESET);

        List<ZonaEcologica> zonas = List.of(
            new ZonaEcologica("Reserva Comunal El Sira",          616567, 38400, TipoEcosistema.AMAZONIA, true),
            new ZonaEcologica("Parque Nacional del Manu",         1716295, 12500, TipoEcosistema.AMAZONIA, false),
            new ZonaEcologica("Bosque de Protección Alto Mayo",   182000, 45600, TipoEcosistema.AMAZONIA, true),
            new ZonaEcologica("Santuario Nacional Tabaconas",     29500,  8200,  TipoEcosistema.ANDES,    false),
            new ZonaEcologica("Reserva Paisajística Nor Yauyos",  221268, 5400,  TipoEcosistema.ANDES,    false),
            new ZonaEcologica("Reserva Nacional de Paracas",      335000, 1200,  TipoEcosistema.COSTA,    false)
        );

        List<EcoAlertResult> resultados = new ArrayList<>();

        for (ZonaEcologica zona : zonas) {
            EcosystemRiskStrategy estrategia = RiskStrategyFactory.forEcosistema(zona.getEcosistema());
            EcoAlertService servicio = new EcoAlertService(estrategia);
            resultados.add(servicio.evaluarZona(zona));
        }

        // Ordenar por IRE descendente (mayor riesgo primero)
        resultados.sort(Comparator.comparingDouble(EcoAlertResult::getIre).reversed());

        System.out.println();
        for (EcoAlertResult r : resultados) {
            mostrarResultado(r);
        }

        // Estadísticas del reporte
        long criticas = resultados.stream().filter(r -> r.getNivelAlerta().equals("CRÍTICO")).count();
        long altas    = resultados.stream().filter(r -> r.getNivelAlerta().equals("ALTO")).count();
        double ireMax = resultados.get(0).getIre();

        System.out.println("\n" + BOLD + "  ── Resumen del Reporte ──" + RESET);
        System.out.printf("  Zonas evaluadas: %d  |  Zonas CRÍTICAS: %s%d%s  |  Zonas ALTAS: %s%d%s  |  IRE máximo: %.2f%n",
            resultados.size(), ROJO_F, criticas, RESET, ROJO, altas, RESET, ireMax);
    }

    // ─────────────────────────────────────────────────────────────────────
    // UI HELPERS
    // ─────────────────────────────────────────────────────────────────────
    private static void mostrarResultado(EcoAlertResult r) {
        String color = switch (r.getNivelAlerta()) {
            case "BAJO"    -> VERDE;
            case "MEDIO"   -> AMARILLO;
            case "ALTO"    -> ROJO;
            case "CRÍTICO" -> ROJO_F;
            default        -> RESET;
        };
        System.out.printf("  %s[%-8s]%s  IRE: %6.2f  |  Deforest.: %5.2f%%  |  %s%n",
            color, r.getNivelAlerta(), RESET,
            r.getIre(), r.getTasaDeforestacion(), r.getNombreZona());
    }

    private static void mostrarBanner() {
        System.out.println(VERDE + BOLD);
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║       🌿 EcoAlert Perú v1.0                         ║");
        System.out.println("  ║       Sistema de Monitoreo Ecológico                 ║");
        System.out.println("  ║       Producto Académico N°1 — Sprint 1              ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private static void mostrarMenu() {
        System.out.println("\n" + BOLD + "  ── Menú Principal ──────────────────────" + RESET);
        System.out.println("  1. Evaluar una zona ecológica (datos propios)");
        System.out.println("  2. Ver reporte demo con zonas reales del Perú");
        System.out.println("  3. Salir");
        System.out.print("\n  Seleccione una opción: ");
    }
}
