package com.gamengine.patrones;

/**
 * Patrón Builder — Construcción de Personajes del Juego.
 *
 * Contiene:
 * - Product (Personaje): el objeto complejo que se construye
 * - Builder (PersonajeBuilder): interfaz con los pasos de construcción
 * - Concrete Builders: PersonajeGuerreroBuilder, PersonajeMagoBuilder
 * - Director (DirectorPersonaje): controla el orden de construcción
 */

// ══════════════════════════════════════════════
// PRODUCT: el objeto complejo que se construye paso a paso.
// ══════════════════════════════════════════════
class Personaje {
    private String nombre;
    private String clase;
    private String arma;
    private String armadura;
    private String habilidadEspecial;
    private int vida;
    private int ataque;
    private int defensa;

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setClase(String clase) { this.clase = clase; }
    public void setArma(String arma) { this.arma = arma; }
    public void setArmadura(String armadura) { this.armadura = armadura; }
    public void setHabilidadEspecial(String habilidadEspecial) { this.habilidadEspecial = habilidadEspecial; }
    public void setVida(int vida) { this.vida = vida; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public void mostrarInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║  PERSONAJE: " + nombre);
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  Clase:     " + clase);
        System.out.println("  ║  Arma:      " + arma);
        System.out.println("  ║  Armadura:  " + armadura);
        System.out.println("  ║  Habilidad: " + habilidadEspecial);
        System.out.println("  ║  Vida:      " + vida);
        System.out.println("  ║  Ataque:    " + ataque);
        System.out.println("  ║  Defensa:   " + defensa);
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}

// ══════════════════════════════════════════════
// BUILDER: interfaz que define los pasos de construcción.
// ══════════════════════════════════════════════
interface PersonajeBuilder {
    void setNombre(String nombre);
    void construirEstadisticas();
    void equiparArma();
    void equiparArmadura();
    void asignarHabilidad();
    Personaje getPersonaje();
}

// ══════════════════════════════════════════════
// CONCRETE BUILDER: construye un Guerrero.
// ══════════════════════════════════════════════
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

    @Override
    public void equiparArma() {
        personaje.setArma("Espada de Acero");
    }

    @Override
    public void equiparArmadura() {
        personaje.setArmadura("Armadura Pesada de Hierro");
    }

    @Override
    public void asignarHabilidad() {
        personaje.setHabilidadEspecial("Golpe Devastador");
    }

    @Override
    public Personaje getPersonaje() {
        return personaje;
    }
}

// ══════════════════════════════════════════════
// CONCRETE BUILDER: construye un Mago.
// ══════════════════════════════════════════════
class PersonajeMagoBuilder implements PersonajeBuilder {
    private Personaje personaje;

    public PersonajeMagoBuilder() {
        this.personaje = new Personaje();
    }

    @Override
    public void setNombre(String nombre) {
        personaje.setNombre(nombre);
        personaje.setClase("Mago");
    }

    @Override
    public void construirEstadisticas() {
        personaje.setVida(80);
        personaje.setAtaque(60);
        personaje.setDefensa(20);
    }

    @Override
    public void equiparArma() {
        personaje.setArma("Bastón Arcano");
    }

    @Override
    public void equiparArmadura() {
        personaje.setArmadura("Túnica Mística");
    }

    @Override
    public void asignarHabilidad() {
        personaje.setHabilidadEspecial("Bola de Fuego");
    }

    @Override
    public Personaje getPersonaje() {
        return personaje;
    }
}

// ══════════════════════════════════════════════
// DIRECTOR: controla el proceso de construcción.
// ══════════════════════════════════════════════
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
