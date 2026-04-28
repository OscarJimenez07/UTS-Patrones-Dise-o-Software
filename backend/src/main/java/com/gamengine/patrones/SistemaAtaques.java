package com.gamengine.patrones;

/**
 * Patron Bridge — Sistema de Ataques por Elemento.
 *
 * Bridge separa DOS dimensiones independientes:
 *   - TIPO de ataque (cuerpo a cuerpo, distancia, magico)
 *   - ELEMENTO del ataque (fuego, hielo, electrico)
 */

// ══════════════════════════════════════════════
// IMPLEMENTOR
// ══════════════════════════════════════════════
interface ElementoAtaque {
    String getNombre();
    int getDanoElemento();
    String getEfecto();
}

// ══════════════════════════════════════════════
// CONCRETE IMPLEMENTORS
// ══════════════════════════════════════════════

class ElementoFuego implements ElementoAtaque {
    @Override public String getNombre() { return "Fuego"; }
    @Override public int getDanoElemento() { return 20; }
    @Override public String getEfecto() { return "Quemadura: dano continuo por 3 turnos"; }
}

class ElementoHielo implements ElementoAtaque {
    @Override public String getNombre() { return "Hielo"; }
    @Override public int getDanoElemento() { return 12; }
    @Override public String getEfecto() { return "Congelacion: reduce velocidad del enemigo 50%"; }
}

class ElementoElectrico implements ElementoAtaque {
    @Override public String getNombre() { return "Electrico"; }
    @Override public int getDanoElemento() { return 18; }
    @Override public String getEfecto() { return "Paralisis: 30% chance de saltar turno enemigo"; }
}

// ══════════════════════════════════════════════
// ABSTRACTION
// ══════════════════════════════════════════════
abstract class AtaquePersonaje {
    protected ElementoAtaque elemento;

    public AtaquePersonaje(ElementoAtaque elemento) {
        this.elemento = elemento;
    }

    public abstract String getTipoAtaque();
    public abstract int getDanoBase();

    public void ejecutarAtaque() {
        int danoTotal = getDanoBase() + elemento.getDanoElemento();
        System.out.println("  Ejecutando: " + getTipoAtaque() + " de " + elemento.getNombre());
        System.out.println("    Dano base:     " + getDanoBase());
        System.out.println("    Dano elemento: +" + elemento.getDanoElemento() + " (" + elemento.getNombre() + ")");
        System.out.println("    Dano total:    " + danoTotal);
        System.out.println("    Efecto: " + elemento.getEfecto());
    }

    public String getDescripcion() { return getTipoAtaque() + " de " + elemento.getNombre(); }
    public int getDanoTotal() { return getDanoBase() + elemento.getDanoElemento(); }
    public ElementoAtaque getElemento() { return elemento; }
}

// ══════════════════════════════════════════════
// REFINED ABSTRACTIONS
// ══════════════════════════════════════════════

class AtaqueCuerpoACuerpo extends AtaquePersonaje {
    public AtaqueCuerpoACuerpo(ElementoAtaque elemento) { super(elemento); }
    @Override public String getTipoAtaque() { return "Golpe Cuerpo a Cuerpo"; }
    @Override public int getDanoBase() { return 30; }
    @Override public void ejecutarAtaque() {
        super.ejecutarAtaque();
        System.out.println("    [Cuerpo a cuerpo: ignora 10% de armadura]");
    }
}

class AtaqueDistancia extends AtaquePersonaje {
    public AtaqueDistancia(ElementoAtaque elemento) { super(elemento); }
    @Override public String getTipoAtaque() { return "Disparo a Distancia"; }
    @Override public int getDanoBase() { return 22; }
    @Override public void ejecutarAtaque() {
        super.ejecutarAtaque();
        System.out.println("    [Distancia: alcance de 5 casillas]");
    }
}

class AtaqueMagico extends AtaquePersonaje {
    public AtaqueMagico(ElementoAtaque elemento) { super(elemento); }
    @Override public String getTipoAtaque() { return "Hechizo Magico"; }
    @Override public int getDanoBase() { return 35; }
    @Override public void ejecutarAtaque() {
        super.ejecutarAtaque();
        System.out.println("    [Magico: consume 20 de mana]");
    }
}
