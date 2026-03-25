# Patrón Prototype — Configuraciones de Partida

## Diagrama UML
![Prototype](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS6/Prototype.puml)

## ¿Qué es?

El patrón Prototype permite **crear nuevos objetos clonando una instancia existente** en lugar de construirlos desde cero con `new`. Cada objeto sabe cómo copiarse a sí mismo, y un registro (registry) almacena los prototipos disponibles para entregarlos bajo demanda.

En este proyecto, cada modo de juego (Duelo 1v1, Equipos 3v3, Battle Royale) tiene una **configuración prototipo** con valores predefinidos (mapa, duración, máximo de jugadores, reglas). Cuando un jugador crea una partida, el sistema clona la configuración del modo elegido y opcionalmente la personaliza (más jugadores, distinta duración), sin modificar el prototipo original.

## Fragmento de código del proyecto

### Prototype Interface — `src/ConfiguracionPartida.java`

```java
// PROTOTYPE: interfaz que define el contrato de clonación.
// Cada configuración de partida sabe cómo copiarse a sí misma.
interface ConfiguracionPartidaPrototype {
    ConfiguracionPartidaPrototype clonar();
    void mostrarInfo();
    String getModo();
    void setMaxJugadores(int maxJugadores);
    void setDuracionMinutos(int duracionMinutos);
}
```

### Concrete Prototype (Duelo) — `src/ConfiguracionPartida.java`

```java
// CONCRETE PROTOTYPE: configuración para partidas 1 vs 1.
// Duelos rápidos en arena cerrada, sin reconexión.
class ConfiguracionDuelo implements ConfiguracionPartidaPrototype {
    private String modo;
    private int maxJugadores;
    private int duracionMinutos;
    private String mapa;
    private boolean rankedPermitido;
    private boolean reconexionPermitida;

    public ConfiguracionDuelo() {
        this.modo = "1 vs 1";
        this.maxJugadores = 2;
        this.duracionMinutos = 10;
        this.mapa = "Arena del Coliseo";
        this.rankedPermitido = true;
        this.reconexionPermitida = false;
    }

    @Override
    public ConfiguracionPartidaPrototype clonar() {
        ConfiguracionDuelo copia = new ConfiguracionDuelo();
        copia.modo = this.modo;
        copia.maxJugadores = this.maxJugadores;
        copia.duracionMinutos = this.duracionMinutos;
        copia.mapa = this.mapa;
        copia.rankedPermitido = this.rankedPermitido;
        copia.reconexionPermitida = this.reconexionPermitida;
        return copia;
    }

    // setters, getters y mostrarInfo()...
}
```

### Registry — `src/ConfiguracionPartida.java`

```java
// REGISTRY: almacena prototipos y entrega clones.
// El cliente nunca modifica el prototipo original.
class RegistroConfiguraciones {
    private Map<String, ConfiguracionPartidaPrototype> prototipos = new HashMap<>();

    public void registrar(String clave, ConfiguracionPartidaPrototype prototipo) {
        prototipos.put(clave, prototipo);
    }

    public ConfiguracionPartidaPrototype obtener(String clave) {
        ConfiguracionPartidaPrototype prototipo = prototipos.get(clave);
        if (prototipo == null) {
            System.out.println("  [Error] No existe configuracion con clave: " + clave);
            return null;
        }
        return prototipo.clonar();
    }

    public void listarConfiguraciones() {
        System.out.println("\n  ═══ MODOS DE JUEGO DISPONIBLES ═══");
        for (Map.Entry<String, ConfiguracionPartidaPrototype> entry : prototipos.entrySet()) {
            System.out.println("  - " + entry.getKey() + " (" + entry.getValue().getModo() + ")");
        }
    }
}
```

### ¿Cómo se usa en el proyecto?

En `src/Main.java` se demuestra el patrón:

```java
// Registrar prototipos de configuración
RegistroConfiguraciones registro = new RegistroConfiguraciones();
registro.registrar("Duelo", new ConfiguracionDuelo());
registro.registrar("Equipos", new ConfiguracionEquipos());
registro.registrar("BattleRoyale", new ConfiguracionBattleRoyale());

// Crear partida rápida (clon sin modificar)
ConfiguracionPartidaPrototype partidaRapida = registro.obtener("Duelo");
partidaRapida.mostrarInfo();

// Crear partida personalizada (clon modificado)
ConfiguracionPartidaPrototype partidaCustom = registro.obtener("Equipos");
partidaCustom.setMaxJugadores(10);
partidaCustom.setDuracionMinutos(30);
partidaCustom.mostrarInfo();
// El prototipo original de "Equipos" sigue intacto con 6 jugadores y 20 min
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patrón |
|---|---|
| `src/ConfiguracionPartida.java` | Prototype interface, Concrete Prototypes y Registry |
| `src/Main.java` | Demostración del patrón |

## Comparación con patrones anteriores

| Aspecto | Factory Method (TS3) | Abstract Factory (TS4) | Builder (TS5) | Prototype (TS6) |
|---|---|---|---|---|
| **Propósito** | Delegar la creación de **un** producto a subclases | Crear **familias** de productos relacionados | Construir un objeto complejo **paso a paso** | Crear objetos **clonando** un prototipo existente |
| **Problema que resuelve** | No saber qué subclase instanciar | Asegurar que los productos sean compatibles entre sí | Objeto con muchos parámetros y configuraciones opcionales | Crear muchas copias similares de forma eficiente |
| **Mecanismo** | Herencia (subclases sobrescriben método fábrica) | Composición (fábrica como objeto inyectado) | Composición (builder + director orquestan la construcción) | Clonación (cada objeto sabe copiarse a sí mismo) |
| **Ejemplo en el proyecto** | `DashboardFactory` → crea un `Dashboard` según el rol | `TiendaItemFactory` → crea `Skin`, `Potenciador`, `Recompensa` | `PersonajeBuilder` → construye un `Personaje` con arma, armadura, stats | `RegistroConfiguraciones` → clona `ConfiguracionDuelo`, `ConfiguracionEquipos`, `ConfiguracionBattleRoyale` |
