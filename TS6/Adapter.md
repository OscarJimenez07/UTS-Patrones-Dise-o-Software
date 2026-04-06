# Patron Adapter — Sistema de Notificaciones

## Diagrama UML
![Adapter](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS6/Adapter.puml)

## ¿Que es?

El patron Adapter permite que **dos interfaces incompatibles trabajen juntas**. Actua como un traductor entre el sistema que necesita un servicio (cliente) y un servicio externo que tiene una interfaz diferente a la esperada.

En este proyecto, el motor de juegos necesita enviar notificaciones a los jugadores (alertas, resultados de partida). Sin embargo, los servicios externos como **Discord** y **Correo Electronico** tienen APIs completamente distintas. Los adaptadores traducen las llamadas del juego al formato que cada servicio externo entiende.

## Fragmento de codigo del proyecto

### Target — `src/SistemaNotificaciones.java`

```java
// TARGET: interfaz que el motor de juegos utiliza
// para enviar notificaciones a los jugadores.
interface NotificacionJuego {
    void enviarAlerta(String jugador, String mensaje);
    void enviarResultadoPartida(String jugador, String modo, boolean victoria);
}
```

### Adaptee (Discord) — `src/SistemaNotificaciones.java`

```java
// ADAPTEE: servicio externo de Discord.
// Tiene su propia interfaz incompatible con NotificacionJuego.
class ServicioDiscord {
    public void publicarMensaje(String canal, String contenido, boolean esMencion) {
        String prefijo = esMencion ? "@usuario " : "";
        System.out.println("  [Discord] #" + canal + ": " + prefijo + contenido);
    }
}
```

### Adapter (Discord) — `src/SistemaNotificaciones.java`

```java
// ADAPTER: adapta ServicioDiscord a NotificacionJuego.
// Traduce las llamadas del juego al formato de Discord.
class AdaptadorDiscord implements NotificacionJuego {
    private ServicioDiscord discord;

    public AdaptadorDiscord(ServicioDiscord discord) {
        this.discord = discord;
    }

    @Override
    public void enviarAlerta(String jugador, String mensaje) {
        // Traduce: alerta del juego → mensaje en canal de Discord con mencion
        discord.publicarMensaje("alertas-juego", jugador + ": " + mensaje, true);
    }

    @Override
    public void enviarResultadoPartida(String jugador, String modo, boolean victoria) {
        // Traduce: resultado de partida → publicacion en canal de resultados
        String resultado = victoria ? "VICTORIA" : "DERROTA";
        String contenido = jugador + " ha terminado una partida de " + modo + " con " + resultado + "!";
        discord.publicarMensaje("resultados", contenido, false);
    }
}
```

### ¿Como se usa en el proyecto?

En `src/Main.java` el jugador elige por cual servicio recibir notificaciones. El codigo cliente solo trabaja con la interfaz `NotificacionJuego`, sin importar si detras hay Discord o Correo:

```java
// El cliente usa la interfaz Target, no conoce el servicio externo
NotificacionJuego notificacion;

// Opcion 1: notificar por Discord
notificacion = new AdaptadorDiscord(new ServicioDiscord());
notificacion.enviarAlerta("oscar", "Tu partida esta por comenzar!");

// Opcion 2: notificar por Correo
notificacion = new AdaptadorCorreo(new ServicioCorreo());
notificacion.enviarResultadoPartida("oscar", "Battle Royale", false);
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patron |
|---|---|
| `src/SistemaNotificaciones.java` | Target, Adaptees y Adapters |
| `src/Main.java` | Cliente que usa los adaptadores |

## Comparacion con patrones anteriores

| Aspecto | Factory Method (TS3) | Abstract Factory (TS4) | Builder (TS5) | Prototype (TS6) | Adapter (TS6) |
|---|---|---|---|---|---|
| **Proposito** | Delegar la creacion de **un** producto a subclases | Crear **familias** de productos relacionados | Construir un objeto complejo **paso a paso** | Crear objetos **clonando** un prototipo existente | Hacer compatibles **interfaces incompatibles** |
| **Tipo** | Creacional | Creacional | Creacional | Creacional | **Estructural** |
| **Problema que resuelve** | No saber que subclase instanciar | Asegurar que los productos sean compatibles entre si | Objeto con muchos parametros y configuraciones opcionales | Crear muchas copias similares de forma eficiente | Integrar servicios externos sin modificar su codigo |
| **Mecanismo** | Herencia (subclases sobrescriben metodo fabrica) | Composicion (fabrica como objeto inyectado) | Composicion (builder + director) | Clonacion (cada objeto sabe copiarse) | Composicion (adapter envuelve al adaptee) |
| **Ejemplo en el proyecto** | `DashboardFactory` → crea `Dashboard` segun el rol | `TiendaItemFactory` → crea `Skin`, `Potenciador`, `Recompensa` | `PersonajeBuilder` → construye un `Personaje` | `RegistroConfiguraciones` → clona configuraciones | `AdaptadorDiscord` / `AdaptadorCorreo` → adaptan servicios externos |
