# Patron Decorator — Sistema de Mejoras de Habilidades

## Diagrama UML
![Decorator](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS7/Decorator.puml)

## ¿Que es?

El patron Decorator permite **agregar responsabilidades adicionales a un objeto de forma dinamica**, sin modificar su clase original. Funciona como una cadena de "envolturas": cada decorador envuelve al objeto anterior y anade su propio comportamiento.

En este proyecto, los personajes tienen habilidades base (Golpe Fisico, Disparo Magico, Hechizo Curativo). Durante la partida, el jugador puede **apilar mejoras** sobre esas habilidades: fuego, veneno, area de efecto, robo de vida. Cada mejora envuelve la habilidad existente y anade dano o efectos adicionales, sin modificar la habilidad original.

## Fragmento de codigo del proyecto

### Component — `src/SistemaMejoras.java`

```java
// COMPONENT: interfaz base que define el contrato
// de toda habilidad del personaje.
interface HabilidadPersonaje {
    void ejecutar();
    String getDescripcion();
    int getDano();
}
```

### Concrete Component — `src/SistemaMejoras.java`

```java
// CONCRETE COMPONENT: habilidad base sin mejoras.
class GolpeFisico implements HabilidadPersonaje {
    @Override
    public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }

    @Override
    public String getDescripcion() { return "Golpe Fisico"; }

    @Override
    public int getDano() { return 25; }
}
```

### Decorator (abstracto) — `src/SistemaMejoras.java`

```java
// DECORATOR: envuelve una HabilidadPersonaje
// y delega las llamadas al componente interno.
abstract class MejoraHabilidad implements HabilidadPersonaje {
    protected HabilidadPersonaje habilidadBase;

    public MejoraHabilidad(HabilidadPersonaje habilidadBase) {
        this.habilidadBase = habilidadBase;
    }

    @Override
    public void ejecutar() { habilidadBase.ejecutar(); }

    @Override
    public String getDescripcion() { return habilidadBase.getDescripcion(); }

    @Override
    public int getDano() { return habilidadBase.getDano(); }
}
```

### Concrete Decorator — `src/SistemaMejoras.java`

```java
// CONCRETE DECORATOR: anade dano de fuego.
class MejoraDanoFuego extends MejoraHabilidad {
    private static final int DANO_FUEGO = 15;

    public MejoraDanoFuego(HabilidadPersonaje habilidadBase) {
        super(habilidadBase);
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Fuego: +" + DANO_FUEGO + " dano adicional");
    }

    @Override
    public String getDescripcion() {
        return habilidadBase.getDescripcion() + " + Fuego";
    }

    @Override
    public int getDano() {
        return habilidadBase.getDano() + DANO_FUEGO;
    }
}
```

### ¿Como se usa en el proyecto?

En `src/Main.java` el jugador selecciona una habilidad base y luego apila mejoras. El codigo cliente solo trabaja con la interfaz `HabilidadPersonaje`, sin importar cuantas capas de mejoras se hayan aplicado:

```java
// Habilidad base
HabilidadPersonaje habilidad = new GolpeFisico();

// El jugador aplica mejoras (se apilan como envolturas)
habilidad = new MejoraDanoFuego(habilidad);   // +15 fuego
habilidad = new MejoraVeneno(habilidad);       // +10 veneno
habilidad = new MejoraRoboVida(habilidad);     // +5 robo de vida

// Al ejecutar, se ejecutan TODOS los efectos en cadena
habilidad.ejecutar();
// Dano total: 25 + 15 + 10 + 5 = 55
System.out.println("Dano total: " + habilidad.getDano());
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patron |
|---|---|
| `src/SistemaMejoras.java` | Component, Concrete Components, Decorator y Concrete Decorators |
| `src/Main.java` | Cliente que usa las habilidades decoradas |

## Comparacion con patrones anteriores

| Aspecto | Builder (TS5) | Prototype (TS6) | Adapter (TS6) | **Decorator (TS7)** |
|---|---|---|---|---|
| **Proposito** | Construir objeto complejo **paso a paso** | Crear objetos **clonando** un prototipo | Hacer compatibles **interfaces incompatibles** | Agregar **responsabilidades dinamicas** a un objeto |
| **Tipo** | Creacional | Creacional | Estructural | **Estructural** |
| **Problema que resuelve** | Objeto con muchos parametros | Crear copias eficientes | Integrar servicios externos | Extender comportamiento sin modificar la clase |
| **Mecanismo** | Composicion (builder + director) | Clonacion | Composicion (adapter envuelve adaptee) | Composicion (decorator envuelve componente) |
| **Ejemplo en el proyecto** | `PersonajeBuilder` → construye `Personaje` | `RegistroConfiguraciones` → clona configs | `AdaptadorDiscord` → adapta Discord | `MejoraDanoFuego` → anade fuego a habilidad |

## Diferencia clave: Adapter vs Decorator

| Aspecto | Adapter | Decorator |
|---|---|---|
| **Objetivo** | Hacer compatible una interfaz existente | Agregar funcionalidad nueva |
| **Modifica la interfaz?** | Si (traduce de una a otra) | No (mantiene la misma interfaz) |
| **Cantidad de envolturas** | Normalmente una | Se pueden apilar multiples |
| **En el proyecto** | Traduce Discord/Correo → NotificacionJuego | Apila Fuego + Veneno + Area sobre una habilidad |
