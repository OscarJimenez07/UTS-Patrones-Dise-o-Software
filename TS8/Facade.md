# Patron Facade — Fachada del flujo "Iniciar Partida"

## Diagrama UML
![Facade](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS8/Facade.puml)

## ¿Que es?

El patron Facade **proporciona una interfaz unificada y sencilla sobre un conjunto de interfaces de un subsistema**. Oculta la complejidad interna y expone un punto de entrada limpio al cliente.

En este proyecto, iniciar una partida implicaba **cuatro subsistemas independientes**:

1. **`SistemaAutenticacion`** (Singleton) — validar que el usuario tenga sesion activa.
2. **`RegistroConfiguraciones`** (Prototype) — obtener un clon de la configuracion del modo de juego.
3. **Matchmaking** — buscar jugadores y crear el lobby.
4. **`NotificacionJuego`** (Adapter) — enviar la alerta por Discord o Correo.

Sin Facade, `Main` tendria que conocer los cuatro subsistemas, instanciarlos en el orden correcto y coordinarlos. Con Facade, `Main` llama a **un solo metodo** (`iniciarPartidaRapida`) y la fachada orquesta todo internamente.

## Roles del patron

| Rol | Clase en el proyecto |
|---|---|
| **Facade** | `FachadaPartida` |
| **Subsistema 1** | `SistemaAutenticacion` |
| **Subsistema 2** | `RegistroConfiguraciones` + `ConfiguracionPartidaPrototype` |
| **Subsistema 3** | `NotificacionJuego` (`AdaptadorDiscord`, `AdaptadorCorreo`) |
| **Cliente** | `Main.buscarPartida()` |

## Fragmento de codigo del proyecto

### Facade — `src/FachadaPartida.java`

```java
class FachadaPartida {

    private SistemaAutenticacion auth;
    private RegistroConfiguraciones registroConfigs;
    private NotificacionJuego notificador;

    public FachadaPartida() {
        // Subsistema 1: Singleton ya existente.
        this.auth = SistemaAutenticacion.getInstance();

        // Subsistema 2: se precargan los prototipos de configuracion.
        this.registroConfigs = new RegistroConfiguraciones();
        this.registroConfigs.registrar("Duelo", new ConfiguracionDuelo());
        this.registroConfigs.registrar("Equipos", new ConfiguracionEquipos());
        this.registroConfigs.registrar("BattleRoyale", new ConfiguracionBattleRoyale());

        // Subsistema 3: notificador por defecto (Adapter).
        this.notificador = new AdaptadorDiscord(new ServicioDiscord());
    }

    // METODO FACHADA: unifica todo el flujo en una sola llamada.
    public void iniciarPartidaRapida(String usuario, String claveModo) {
        if (!verificarSesion(usuario)) return;

        ConfiguracionPartidaPrototype config = registroConfigs.obtener(claveModo);
        if (config == null) return;
        config.mostrarInfo();

        buscarJugadores(config.getModo());

        notificador.enviarAlerta(usuario,
                "Tu partida de " + config.getModo() + " ha comenzado!");
    }

    private boolean verificarSesion(String usuario) {
        if (usuario == null || auth.obtenerRol(usuario) == null) {
            System.out.println("  [Fachada] Sin sesion activa.");
            return false;
        }
        return true;
    }

    private void buscarJugadores(String modo) {
        System.out.println("  [Fachada] Buscando jugadores para " + modo + "...");
        System.out.println("  [Fachada] Lobby creado.");
    }
}
```

### ¿Como se usa en el proyecto?

En `src/Main.java`, el metodo `buscarPartida()` antes simulaba todo manualmente. Ahora **delega todo a la fachada**:

```java
// ANTES (sin Facade): el cliente orquestaba 4 subsistemas
SistemaAutenticacion auth = SistemaAutenticacion.getInstance();
if (auth.obtenerRol(usuarioActual) == null) return;

RegistroConfiguraciones registro = new RegistroConfiguraciones();
registro.registrar("Duelo", new ConfiguracionDuelo());
// ... registrar los demas
ConfiguracionPartidaPrototype config = registro.obtener("Duelo");
config.mostrarInfo();

// ... logica de matchmaking ...

NotificacionJuego notif = new AdaptadorDiscord(new ServicioDiscord());
notif.enviarAlerta(usuarioActual, "Partida lista");


// AHORA (con Facade): una sola llamada
FachadaPartida fachada = new FachadaPartida();
fachada.iniciarPartidaRapida(usuarioActual, "Duelo");
```

La fachada tambien permite **cambiar el canal de notificacion** sin exponer detalles:

```java
FachadaPartida fachada = new FachadaPartida();
fachada.usarNotificador(new AdaptadorCorreo(new ServicioCorreo()));
fachada.iniciarPartidaRapida(usuarioActual, "Equipos");
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patron |
|---|---|
| `src/FachadaPartida.java` | Facade — orquesta los subsistemas |
| `src/SistemaAutenticacion.java` | Subsistema 1 (Singleton) |
| `src/ConfiguracionPartida.java` | Subsistema 2 (Prototype) |
| `src/SistemaNotificaciones.java` | Subsistema 3 (Adapter) |
| `src/Main.java` | Cliente — llama solo a la fachada |

## Puntos clave para sustentar

1. **Desacoplamiento**: el cliente (`Main`) solo depende de `FachadaPartida`. Si maniana cambia el flujo interno (por ejemplo, se agrega un paso de anti-cheat), solo se modifica la fachada.
2. **No reemplaza**: los subsistemas siguen existiendo y se pueden usar directamente si se necesita control fino. La fachada es **opcional**, no obligatoria.
3. **Reutilizacion de patrones existentes**: la fachada aprovecha los patrones ya implementados (Singleton, Prototype, Adapter) en lugar de duplicar logica.
4. **Interfaz simple, implementacion compleja**: el metodo publico `iniciarPartidaRapida(usuario, modo)` parece trivial, pero internamente realiza verificacion de sesion, clonacion de prototipo, matchmaking y envio de notificacion.

## Comparacion con patrones anteriores

| Aspecto | Adapter (TS6) | Decorator (TS7) | Bridge (TS7) | Composite (TS8) | **Facade (TS8)** |
|---|---|---|---|---|---|
| **Proposito** | Hacer compatibles interfaces incompatibles | Agregar responsabilidades | Separar abstraccion de implementacion | Tratar objetos y grupos igual | **Simplificar el acceso a un subsistema complejo** |
| **Tipo** | Estructural | Estructural | Estructural | Estructural | **Estructural** |
| **¿Cuantos objetos involucra?** | 2 (target y adaptee) | N (cadena de decoradores) | 2 jerarquias | Arbol de N nodos | **N subsistemas independientes** |
| **¿Cambia la interfaz?** | Si, la traduce | No, la respeta | No | No | **Si, la simplifica** |
| **Ejemplo** | `AdaptadorDiscord` | `MejoraDanoFuego` | `AtaqueCuerpoACuerpo` + elemento | `Escuadron` contiene miembros | `FachadaPartida` coordina 4 sistemas |

## Diferencia clave: Facade vs Adapter

Ambos envuelven otros componentes, pero tienen intenciones distintas:

| Aspecto | Adapter | Facade |
|---|---|---|
| **¿Por que existe?** | La interfaz existente es **incompatible** | La interfaz existente es **complicada** |
| **¿Cuantas clases envuelve?** | Una (el adaptee) | Varias (un subsistema completo) |
| **¿Cambia la forma de usar?** | Solo traduce llamadas | Define un nuevo flujo mas simple |
| **En el proyecto** | `AdaptadorDiscord` traduce a `NotificacionJuego` | `FachadaPartida` orquesta auth + config + matchmaking + notificacion |
