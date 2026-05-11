# Objetivos del Proyecto

> **Motor de Juegos Multijugador** — Aplicación de Patrones de Diseño de Software
>
> Materia: *Patrones de Software* · Parcial 2
> Integrantes: Oscar Javier Jiménez Celis · Anderson David Suárez Bernal

---

## Objetivo General

Construir un **Motor de Juegos Multijugador** que sirva como caso de estudio funcional para aplicar **patrones de diseño GoF** sobre un dominio real (partidas, clanes, tienda, combate y notificaciones), evidenciando cómo cada patrón resuelve un problema concreto del sistema y no como ejemplos aislados de libro.

La aplicación es **el vehículo**; los patrones son **el fin**.

---

## Objetivos Específicos

### 1. Objetivo Pedagógico — Aplicar y diferenciar 8 patrones de diseño GoF

Implementar de forma integrada los siguientes patrones, cada uno justificado por un problema real del motor:

| Patrón | Tipo | Problema que resuelve en la app |
|---|---|---|
| **Singleton** | Creacional | Sesión única del jugador autenticado |
| **Abstract Factory** | Creacional | Familias de productos para tienda estándar y premium |
| **Prototype** | Creacional | Clonación de modos de juego preconfigurados |
| **Facade** | Estructural | Orquestación del inicio de partida (un solo punto de entrada) |
| **Adapter** | Estructural | Integración con canales de notificación externos heterogéneos |
| **Decorator** | Estructural | Mejoras acumulables sobre personajes sin alterar su clase base |
| **Bridge** | Estructural | Combinación libre de tipos de ataque con tipos de arma |
| **Composite** | Estructural | Jerarquías de clanes con jugadores y subgrupos tratados uniformemente |

> **Meta:** que los 8 patrones convivan en un mismo sistema coherente, no como demos independientes.

---

### 2. Objetivo Funcional — Cubrir los módulos del enunciado del Parcial 2

Desarrollar los módulos solicitados de un motor de juegos multijugador, de modo que **cada módulo justifique naturalmente** el uso de uno o más patrones:

- **Gestión de partidas** — creación, modos de juego, ciclo de vida e inicio orquestado.
- **Comunidades y clanes** — jerarquía de miembros, subgrupos, mensajería interna.
- **Tienda y microtransacciones** — catálogos diferenciados por tipo de moneda (estándar / premium).
- **Sistema de combate y mejoras** — ataques combinables con armas y potenciadores acumulables.
- **Notificaciones** — envío de resultados y eventos a través de canales externos adaptados.
- **Autenticación y sesión** — login por rol con instancia única de sesión activa.

---

### 3. Objetivo de Evidencia — Entregar una app web ejecutable y verificable

Producir un entregable que el evaluador pueda **probar en vivo**, no solo leer en diagramas:

- **Backend** en Spring Boot que expone cada patrón como endpoint REST (`/api/auth`, `/api/tienda`, `/api/partida`, `/api/clan`, `/api/ataques`, `/api/mejoras`, `/api/notificaciones`).
- **Frontend** SPA en React + TypeScript + Tailwind con tema oscuro neón.
- **Distribución como un único JAR autocontenido** (`gameengine.jar`) corriendo en `http://localhost:8080`.
- **Login por roles** (`oscar / 1234` y `anderson / 1234`) con routing diferenciado por usuario.
- **Panel tipo terminal** en la UI que captura el `System.out` de cada patrón y lo muestra al instante, para evidenciar el comportamiento real del código en cada acción.

> **Meta:** que la sustentación se haga ejecutando la app, no leyendo el código.

---

## Resumen en una línea

> La app es un **motor de juegos multijugador** construido como **caso de estudio ejecutable** para demostrar la aplicación práctica de **8 patrones de diseño GoF** en un dominio real, verificables en vivo desde una interfaz web.

---

## Cómo verificar los objetivos

```bash
java -jar backend/target/gameengine.jar
```

Luego abrir <http://localhost:8080/> e iniciar sesión con cualquiera de los usuarios para recorrer cada patrón desde la interfaz.

Para detalles técnicos de ejecución y endpoints, ver [`FRONTEND.md`](./FRONTEND.md).
Para la descripción completa del dominio del motor, ver [`README.md`](./README.md).
