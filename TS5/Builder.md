# Patrón Builder — Construcción de Personajes del Juego

## Diagrama UML
![Builder](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS5/Builder.puml)

## ¿Qué es?

El patrón Builder permite **construir objetos complejos paso a paso**. Separa la construcción de un objeto de su representación, de modo que el mismo proceso de construcción pueda crear diferentes representaciones.

En este proyecto, un `Personaje` tiene muchos atributos (nombre, clase, arma, armadura, habilidad especial, vida, ataque, defensa). En lugar de usar un constructor con 8 parámetros o múltiples constructores sobrecargados, el Builder permite crear personajes paso a paso, y el Director garantiza que los pasos se ejecuten en el orden correcto.

## Fragmento de código del proyecto

### Product — `src/Personaje.java`

```java
// PRODUCT: el objeto complejo que se construye paso a paso.
class Personaje {
    private String nombre;
    private String clase;
    private String arma;
    private String armadura;
    private String habilidadEspecial;
    private int vida;
    private int ataque;
    private int defensa;

    // setters para cada atributo...

    public void mostrarInfo() {
        System.out.println("  PERSONAJE: " + nombre);
        System.out.println("  Clase:     " + clase);
        System.out.println("  Arma:      " + arma);
        System.out.println("  Armadura:  " + armadura);
        System.out.println("  Habilidad: " + habilidadEspecial);
        System.out.println("  Vida: " + vida + " | Ataque: " + ataque + " | Defensa: " + defensa);
    }
}
```

### Builder Interface — `src/Personaje.java`

```java
// BUILDER: interfaz que define los pasos de construcción.
interface PersonajeBuilder {
    void setNombre(String nombre);
    void construirEstadisticas();
    void equiparArma();
    void equiparArmadura();
    void asignarHabilidad();
    Personaje getPersonaje();
}
```

### Concrete Builder (Guerrero) — `src/Personaje.java`

```java
// CONCRETE BUILDER: construye un Guerrero.
// Alta vida y defensa, espada y armadura pesada.
class PersonajeGuerreroBuilder implements PersonajeBuilder {
    private Personaje personaje;

    public PersonajeGuerreroBuilder() {
        this.personaje = new Personaje();
    }

    @Override
    public void setNombre(String nombre) {
        personaje.setNombre(nombre);
        personaje.setClase("Guerrero");
    }

    @Override
    public void construirEstadisticas() {
        personaje.setVida(150);
        personaje.setAtaque(30);
        personaje.setDefensa(50);
    }

    @Override public void equiparArma() { personaje.setArma("Espada de Acero"); }
    @Override public void equiparArmadura() { personaje.setArmadura("Armadura Pesada de Hierro"); }
    @Override public void asignarHabilidad() { personaje.setHabilidadEspecial("Golpe Devastador"); }
    @Override public Personaje getPersonaje() { return personaje; }
}
```

### Director — `src/Personaje.java`

```java
// DIRECTOR: controla el proceso de construcción.
// Sabe en qué ORDEN llamar los pasos del builder.
class DirectorPersonaje {
    private PersonajeBuilder builder;

    public DirectorPersonaje(PersonajeBuilder builder) {
        this.builder = builder;
    }

    public Personaje construirPersonaje(String nombre) {
        builder.setNombre(nombre);
        builder.construirEstadisticas();
        builder.equiparArma();
        builder.equiparArmadura();
        builder.asignarHabilidad();
        return builder.getPersonaje();
    }
}
```

### ¿Cómo se usa en el proyecto?

En `src/Main.java` se demuestra el patrón:

```java
// Crear un Guerrero usando el Director + Builder
PersonajeBuilder builderGuerrero = new PersonajeGuerreroBuilder();
DirectorPersonaje director = new DirectorPersonaje(builderGuerrero);
Personaje guerrero = director.construirPersonaje("Arthas");
guerrero.mostrarInfo();

// Crear un Mago usando el Director + Builder
PersonajeBuilder builderMago = new PersonajeMagoBuilder();
director = new DirectorPersonaje(builderMago);
Personaje mago = director.construirPersonaje("Gandalf");
mago.mostrarInfo();
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patrón |
|---|---|
| `src/Personaje.java` | Product, Builder interface, Concrete Builders y Director |
| `src/Main.java` | Demostración del patrón |

## Comparación con patrones anteriores

| Aspecto | Factory Method (TS3) | Abstract Factory (TS4) | Builder (TS5) |
|---|---|---|---|
| **Propósito** | Delegar la creación de **un** producto a subclases | Crear **familias** de productos relacionados | Construir un objeto complejo **paso a paso** |
| **Problema que resuelve** | No saber qué subclase instanciar | Asegurar que los productos sean compatibles entre sí | Objeto con muchos parámetros y configuraciones opcionales |
| **Mecanismo** | Herencia (subclases sobrescriben método fábrica) | Composición (fábrica como objeto inyectado) | Composición (builder + director orquestan la construcción) |
| **Ejemplo en el proyecto** | `DashboardFactory` → crea un `Dashboard` según el rol | `TiendaItemFactory` → crea `Skin`, `Potenciador`, `Recompensa` | `PersonajeBuilder` → construye un `Personaje` con arma, armadura, stats |
