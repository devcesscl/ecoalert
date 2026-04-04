# 🌿 EcoAlert Perú
### Sistema de Monitoreo y Alerta Ecológica para los Biomas del Perú

**Producto Académico N° 1 — Desarrollo Ágil con Inteligencia Artificial Generativa**

---

## 📋 Descripción

EcoAlert Perú es una aplicación Java que calcula el **Índice de Riesgo Ecológico (IRE)** de zonas geográficas del Perú, generando niveles de alerta: `BAJO`, `MEDIO`, `ALTO` o `CRÍTICO`.

Desarrollada en un Sprint ágil de 2 semanas con apoyo de GitHub Copilot y ChatGPT-4o.

---

## 🏗 Arquitectura

```
ecoalert-peru/
├── src/main/java/pe/ecoalert/
│   ├── EcoAlertApp.java              ← Punto de entrada (menú interactivo)
│   ├── model/
│   │   ├── TipoEcosistema.java       ← Enum con factores de sensibilidad
│   │   ├── ZonaEcologica.java        ← Modelo de datos de zona
│   │   └── EcoAlertResult.java       ← Resultado de evaluación
│   ├── strategy/
│   │   ├── EcosystemRiskStrategy.java ← Interfaz (Patrón Strategy)
│   │   ├── AmazoniaRiskStrategy.java  ← Factor 1.8
│   │   ├── AndesRiskStrategy.java     ← Factor 1.5
│   │   ├── CostaRiskStrategy.java     ← Factor 1.2
│   │   └── RiskStrategyFactory.java   ← Fábrica de estrategias
│   ├── service/
│   │   └── EcoAlertService.java       ← Servicio principal
│   └── exception/
│       ├── EcoAlertException.java
│       └── DatoSatelitalInvalidoException.java
└── src/test/java/pe/ecoalert/
    └── EcoAlertServiceTest.java       ← 8 pruebas JUnit 5
```

---

## 🚀 Ejecución

### Requisitos
- Java 17+
- Maven 3.8+

### Compilar y ejecutar
```bash
# Compilar
mvn clean compile

# Ejecutar pruebas
mvn test

# Generar JAR y ejecutar
mvn package
java -jar target/ecoalert-peru.jar
```

---

## 📊 Fórmula del IRE

```
Tasa de deforestación = (hectáreas perdidas / hectáreas totales) × 100
IRE = tasa × factor_ecosistema [× 1.3 si hay incendio activo]
```

| Ecosistema | Factor | Fuente |
|------------|--------|--------|
| Amazonía   | 1.8    | MINAM (2022) |
| Andes      | 1.5    | MINAM (2022) |
| Costa      | 1.2    | MINAM (2022) |

| IRE     | Nivel de Alerta |
|---------|-----------------|
| < 10    | 🟢 BAJO          |
| 10–29   | 🟡 MEDIO         |
| 30–59   | 🔴 ALTO          |
| ≥ 60    | 🚨 CRÍTICO       |

---

## 🧪 Pruebas Unitarias

```bash
mvn test
```

8 casos de prueba cubriendo: casos exitosos (3 biomas), factor incendio, zona nula, área negativa, pérdidas negativas y pérdidas > área total.

---

## 📚 Referencias
- Martin, R. C. (2008). *Clean Code*. Prentice Hall.
- MINAM (2022). *Indicadores de deforestación por bioma en el Perú*.
- Schwaber, K. & Sutherland, J. (2020). *The Scrum Guide*.
