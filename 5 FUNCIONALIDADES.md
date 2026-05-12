# Nuevas Funcionalidades (Implementadas)

> **Motor de Juegos Multijugador** — Extensión del proyecto
>
> Materia: *Patrones de Software*
> Integrantes: Oscar Javier Jiménez Celis · Anderson David Suárez Bernal
>
> **Estado: ✅ Todas implementadas y verificadas en el JAR `backend/target/gameengine.jar`.**
> Las nuevas páginas aparecen en la navegación lateral y en el dashboard del rol *jugador*.

Este documento describe **5 funcionalidades adicionales** integradas al motor de juegos multijugador. Cada funcionalidad:

- Resolver un problema real del dominio (partidas, jugadores, comunidad).
- **Justificar la incorporación de un nuevo patrón de diseño GoF**, ampliando el catálogo actual de 8 patrones.
- Ser implementable sobre la arquitectura existente (Spring Boot + React).

---

## Resumen de las 5 Funcionalidades

| # | Funcionalidad | Patrón GoF asociado | Tipo |
|---|---|---|---|
| 1 | Historial de Partidas | **Memento** | Comportamiento |
| 2 | Sistema de Logros y Recompensas | **Observer** | Comportamiento |
| 3 | Ranking Global y Tabla de Clasificación | **Strategy** | Comportamiento |
| 4 | Chat en Tiempo Real entre Jugadores | **Mediator** | Comportamiento |
| 5 | Sistema de Misiones Diarias | **State** | Comportamiento |

> Estas 5 funcionalidades incorporan **5 patrones de comportamiento** adicionales. El proyecto pasa de 8 a **13 patrones GoF aplicados**.

---

## 1. Historial de Partidas

### Descripción
Cada jugador puede consultar el historial detallado de sus partidas anteriores: fecha, modo de juego, resultado (victoria/derrota), duración, oponentes enfrentados, mejoras usadas y log de eventos relevantes.

### Justificación del patrón — **Memento**
El patrón **Memento** permite capturar y almacenar el estado interno de una partida (jugadores, puntuación, mejoras activas, ataques ejecutados) sin exponer su estructura interna, para luego poder **reconstruir** ese estado al revisar el historial.

- **Originator:** la partida actual.
- **Memento:** snapshot inmutable del estado al finalizar.
- **Caretaker:** servicio de historial que guarda los snapshots por jugador.

### Endpoints REST implementados
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/historial/{usuario}` | Lista los snapshots del jugador |
| `GET` | `/api/historial/{usuario}/{id}` | Detalle completo de un snapshot (restaurado) |
| `POST` | `/api/historial/guardar` | Guardar un snapshot nuevo (toma snapshot del originator) |
| `DELETE` | `/api/historial/{usuario}` | Vaciar el caretaker del jugador |

### Archivos del backend
- `backend/.../patrones/SistemaHistorial.java` — `SnapshotPartida` (Memento), `PartidaEnCurso` (Originator), `HistorialJugador` (Caretaker)
- `backend/.../patrones/HistorialService.java` — servicio expuesto a los controllers
- `backend/.../api/HistorialController.java` — endpoints REST

### Pantalla frontend
`frontend/src/pages/HistorialPage.tsx` — ruta `/historial`. Lista de snapshots, formulario para crear nuevos, vista de detalle restaurada y panel terminal con la salida del backend.

### Pantallas en el frontend
- **Vista "Mi Historial"** con tabla paginada de partidas (fecha, modo, resultado).
- **Modal de detalle** que muestra el snapshot completo (jugadores, mejoras, log de combate).

---

## 2. Sistema de Logros y Recompensas

### Descripción
Los jugadores desbloquean **logros** al cumplir condiciones específicas (jugar X partidas, ganar Y veces, alcanzar nivel Z, unirse a un clan, etc.). Cada logro otorga una recompensa (monedas, ítems cosméticos, insignias).

### Justificación del patrón — **Observer**
El patrón **Observer** desacopla la generación de eventos del sistema (partida terminada, jugador subió de nivel, compra realizada) del código que evalúa si esos eventos disparan un logro.

- **Subject:** eventos del juego (`PartidaFinalizada`, `CompraRealizada`, `ClanCreado`).
- **Observers:** evaluadores de logros que escuchan eventos y otorgan recompensas.
- Permite **añadir nuevos logros sin modificar el código de partidas o tienda**.

### Endpoints REST implementados
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/logros/{usuario}` | Catálogo de logros con progreso por jugador |
| `POST` | `/api/logros/evento` | Publica un evento al subject (los observers reaccionan) |

### Archivos del backend
- `backend/.../patrones/SistemaLogros.java` — `EventoJuego` (event), `LogroObserver` (observer), `EventosJuego` (subject) y 4 logros concretos
- `backend/.../patrones/LogrosService.java` — servicio con los logros suscritos
- `backend/.../api/LogrosController.java` — endpoints REST

### Pantalla frontend
`frontend/src/pages/LogrosPage.tsx` — ruta `/logros`. Galería de logros con barra de progreso, panel para disparar eventos, badge que indica desbloqueo.

### Pantallas en el frontend
- **Galería de Logros** con tarjetas desbloqueadas/bloqueadas y progreso (`3/10 partidas ganadas`).
- **Notificación tipo toast** cuando se desbloquea un logro nuevo.

---

## 3. Ranking Global y Tabla de Clasificación

### Descripción
Tabla pública de los mejores jugadores según distintos criterios: puntos totales, victorias, ratio victoria/derrota, ELO competitivo. El usuario puede cambiar el criterio de ordenamiento en vivo.

### Justificación del patrón — **Strategy**
El patrón **Strategy** permite encapsular distintos algoritmos de cálculo de ranking como objetos intercambiables, de modo que el frontend pueda **elegir el criterio sin que cambie el código del servicio de ranking**.

- **Strategy:** `EstrategiaRanking` (interfaz).
- **ConcreteStrategies:** `RankingPorPuntos`, `RankingPorELO`, `RankingPorVictorias`, `RankingPorRatio`.
- **Context:** servicio de ranking que recibe la estrategia y devuelve la lista ordenada.

### Endpoints REST implementados
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/ranking?criterio=puntos\|elo\|victorias\|ratio` | Top según estrategia |
| `GET` | `/api/ranking/criterios` | Catálogo de estrategias disponibles |
| `GET` | `/api/ranking/jugador/{usuario}` | Posición del jugador en cada ranking |

### Archivos del backend
- `backend/.../patrones/SistemaRanking.java` — `EstrategiaRanking` (strategy), 4 estrategias concretas, `ServicioRanking` (context), `JugadorRanking` (entidad)
- `backend/.../patrones/RankingService.java` — servicio para los controllers
- `backend/.../api/RankingController.java` — endpoints REST

### Pantalla frontend
`frontend/src/pages/RankingPage.tsx` — ruta `/ranking`. Tabla con podio y resaltado del jugador actual; selector de estrategia que cambia el orden sin recargar.

### Pantallas en el frontend
- **Tabla "Top 100"** con selector de criterio (pestañas o dropdown).
- **Resaltado** de la posición del jugador actual cuando aparece en la tabla.

---

## 4. Chat en Tiempo Real entre Jugadores

### Descripción
Chat global (lobby), chat de clan y mensajes directos entre jugadores conectados. Soporte para filtrado de palabras inapropiadas y notificación de menciones.

### Justificación del patrón — **Mediator**
El patrón **Mediator** centraliza la lógica de comunicación entre jugadores en un único objeto, evitando que cada cliente tenga referencias directas a los demás. Esto es **crítico** en chat multijugador para que la matriz de conexiones no explote.

- **Mediator:** `SalaDeChat` (gestiona canal global, canales de clan, DMs).
- **Colleagues:** jugadores conectados que solo conocen al mediador.
- Permite **agregar nuevos canales o reglas de moderación** sin tocar a los participantes.

### Endpoints REST implementados (polling sobre HTTP en lugar de WS por simplicidad)
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/chat/canales` | Lista de canales disponibles |
| `GET` | `/api/chat/{canal}` | Historial de mensajes del canal |
| `POST` | `/api/chat/enviar` | Envía un mensaje al mediador `{usuario, canal, mensaje}` |

### Archivos del backend
- `backend/.../patrones/SistemaChat.java` — `SalaDeChat` (mediator), `SalaChatCentral` (concrete mediator con filtrado), `UsuarioChat` (colleague), `MensajeChat`
- `backend/.../patrones/ChatService.java` — servicio para los controllers
- `backend/.../api/ChatController.java` — endpoints REST

### Pantalla frontend
`frontend/src/pages/ChatPage.tsx` — ruta `/chat`. Layout tipo mensajería, selector de canal, contador de conectados, mensajes propios alineados a la derecha.

### Pantallas en el frontend
- **Panel lateral de chat** persistente con pestañas (Global / Clan / DMs).
- **Indicador de jugadores conectados** en cada canal.
- **Filtro automático** de lenguaje inapropiado con asteriscos.

---

## 5. Sistema de Misiones Diarias

### Descripción
Cada jugador recibe **3 misiones diarias** al iniciar sesión (ej. "Gana 2 partidas", "Aplica 3 mejoras", "Envía un mensaje al clan"). Las misiones tienen un ciclo de vida: **Disponible → En Progreso → Completada → Reclamada → Expirada**.

### Justificación del patrón — **State**
El patrón **State** modela el ciclo de vida de cada misión como objetos de estado, evitando una cadena interminable de `if/else` y permitiendo que cada estado defina su propio comportamiento (puede avanzar, puede reclamarse, ya expiró).

- **Context:** `Misión` con un estado actual.
- **States:** `Disponible`, `EnProgreso`, `Completada`, `Reclamada`, `Expirada`.
- Cada estado **decide qué transiciones son válidas** desde sí mismo.

### Endpoints REST implementados
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/misiones/{usuario}` | Las 3 misiones del día del jugador |
| `POST` | `/api/misiones/{usuario}/{id}/progreso` | Avanza la misión (delegado al estado actual) |
| `POST` | `/api/misiones/{usuario}/{id}/reclamar` | Reclama la recompensa |
| `POST` | `/api/misiones/{usuario}/refrescar` | Expira las actuales y regenera 3 nuevas |

### Archivos del backend
- `backend/.../patrones/SistemaMisiones.java` — `Mision` (context), `EstadoMision` (state), 5 estados concretos
- `backend/.../patrones/MisionesService.java` — servicio para los controllers
- `backend/.../api/MisionesController.java` — endpoints REST

### Pantalla frontend
`frontend/src/pages/MisionesPage.tsx` — ruta `/misiones`. Tarjetas con badge del estado, barra de progreso y botones "+1 progreso" / "Reclamar" habilitados según el estado actual.

### Pantallas en el frontend
- **Widget "Misiones del Día"** en el dashboard, con barra de progreso por misión.
- **Botón "Reclamar"** habilitado solo cuando la misión está en estado `Completada`.
- **Contador regresivo** hasta que las misiones se renueven (medianoche).

---

## Beneficios incorporados al proyecto

1. **Mayor cobertura del enunciado del Parcial 2** — el `README.md` original ya mencionaba rankings, recompensas, chat e historial como módulos deseados.
2. **Catálogo de patrones más completo** — el proyecto pasa de 8 patrones (creacionales/estructurales) a **13 patrones**, incorporando 5 patrones de **comportamiento** que enriquecen la dimensión académica.
3. **Mayor profundidad de la sustentación** — cada funcionalidad nueva es una evidencia ejecutable adicional para mostrar al evaluador.
4. **Mejor experiencia para el jugador** — el motor deja de ser una demo de patrones para parecerse más a un juego online real.

---

## Cómo probar las nuevas funcionalidades

```bash
# Reconstruir todo (frontend + backend + JAR)
./rebuild.bat        # Windows
# ó
./rebuild.sh         # Unix

# Arrancar
java -jar backend/target/gameengine.jar
```

Luego abrir <http://localhost:8080/>, iniciar sesión con `oscar / 1234` o `anderson / 1234` y entrar a las nuevas rutas:

- `/historial` — Memento
- `/logros` — Observer
- `/ranking` — Strategy
- `/chat` — Mediator
- `/misiones` — State

También aparecen como tarjetas en el dashboard del jugador.

---

## Resumen visual de la ampliación

```
Antes (8 patrones):
  Creacionales:  Singleton · Abstract Factory · Prototype
  Estructurales: Facade · Adapter · Decorator · Bridge · Composite

Ahora (+5 patrones de comportamiento):
  Comportamiento: Memento · Observer · Strategy · Mediator · State
  ─────────────────────────────────────────────────────────────
  TOTAL: 13 patrones GoF aplicados en código ejecutable
```

---

Ver también:
- [`OBJETIVOS.md`](./OBJETIVOS.md) — objetivos del proyecto.
- [`README.md`](./README.md) — descripción general del motor.
- [`FRONTEND.md`](./FRONTEND.md) — cómo ejecutar y endpoints actuales.
