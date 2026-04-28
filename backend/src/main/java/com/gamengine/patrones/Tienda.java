package com.gamengine.patrones;

/**
 * Clase Tienda que usa el Abstract Factory.
 *
 * Recibe una fábrica y trabaja con las interfaces (Skin, Potenciador, Recompensa)
 * sin saber si los ítems son estándar o premium. Solo cambiando la fábrica,
 * toda la familia de productos cambia.
 */
public class Tienda {

    private TiendaItemFactory factory;
    private String nivel;

    public Tienda(TiendaItemFactory factory, String nivel) {
        this.factory = factory;
        this.nivel = nivel;
    }

    /** Crea y muestra una familia completa de ítems usando la fábrica. */
    public void mostrarOfertaDelDia() {
        Skin skin = factory.crearSkin("Dragon de Fuego");
        Potenciador pot = factory.crearPotenciador("Velocidad x2");
        Recompensa rec = factory.crearRecompensa("Mision diaria completada");

        System.out.println("\n  ======================================");
        System.out.println("    TIENDA - Oferta del Dia (" + nivel + ")");
        System.out.println("  ======================================");
        skin.aplicar();
        pot.activar();
        rec.reclamar();
        System.out.println("  ======================================");
    }

    public Skin crearSkin(String nombre) { return factory.crearSkin(nombre); }
    public Potenciador crearPotenciador(String nombre) { return factory.crearPotenciador(nombre); }
    public Recompensa crearRecompensa(String descripcion) { return factory.crearRecompensa(descripcion); }
    public String getNivel() { return nivel; }
}
