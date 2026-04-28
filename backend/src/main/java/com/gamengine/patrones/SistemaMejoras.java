package com.gamengine.patrones;

/**
 * Patron Decorator — Sistema de Mejoras de Habilidades.
 */

// ══════════════════════════════════════════════
// COMPONENT: contrato base de toda habilidad.
// ══════════════════════════════════════════════
interface HabilidadPersonaje {
    void ejecutar();
    String getDescripcion();
    int getDano();
}

// ══════════════════════════════════════════════
// CONCRETE COMPONENTS
// ══════════════════════════════════════════════

class GolpeFisico implements HabilidadPersonaje {
    @Override public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }
    @Override public String getDescripcion() { return "Golpe Fisico"; }
    @Override public int getDano() { return 25; }
}

class DisparoMagico implements HabilidadPersonaje {
    @Override public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }
    @Override public String getDescripcion() { return "Disparo Magico"; }
    @Override public int getDano() { return 40; }
}

class HechizoCurativo implements HabilidadPersonaje {
    @Override public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }
    @Override public String getDescripcion() { return "Hechizo Curativo"; }
    @Override public int getDano() { return 10; }
}

// ══════════════════════════════════════════════
// DECORATOR (abstracto)
// ══════════════════════════════════════════════
abstract class MejoraHabilidad implements HabilidadPersonaje {
    protected HabilidadPersonaje habilidadBase;

    public MejoraHabilidad(HabilidadPersonaje habilidadBase) {
        this.habilidadBase = habilidadBase;
    }

    @Override public void ejecutar() { habilidadBase.ejecutar(); }
    @Override public String getDescripcion() { return habilidadBase.getDescripcion(); }
    @Override public int getDano() { return habilidadBase.getDano(); }
}

// ══════════════════════════════════════════════
// CONCRETE DECORATORS
// ══════════════════════════════════════════════

class MejoraDanoFuego extends MejoraHabilidad {
    private static final int DANO_FUEGO = 15;
    public MejoraDanoFuego(HabilidadPersonaje base) { super(base); }
    @Override public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Fuego: +" + DANO_FUEGO + " dano adicional (quemadura)");
    }
    @Override public String getDescripcion() { return habilidadBase.getDescripcion() + " + Fuego"; }
    @Override public int getDano() { return habilidadBase.getDano() + DANO_FUEGO; }
}

class MejoraVeneno extends MejoraHabilidad {
    private static final int DANO_VENENO = 10;
    public MejoraVeneno(HabilidadPersonaje base) { super(base); }
    @Override public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Veneno: +" + DANO_VENENO + " dano por turno (3 turnos)");
    }
    @Override public String getDescripcion() { return habilidadBase.getDescripcion() + " + Veneno"; }
    @Override public int getDano() { return habilidadBase.getDano() + DANO_VENENO; }
}

class MejoraAreaEfecto extends MejoraHabilidad {
    private static final int DANO_AREA = 8;
    public MejoraAreaEfecto(HabilidadPersonaje base) { super(base); }
    @Override public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Area: +" + DANO_AREA + " dano a enemigos cercanos");
    }
    @Override public String getDescripcion() { return habilidadBase.getDescripcion() + " + Area"; }
    @Override public int getDano() { return habilidadBase.getDano() + DANO_AREA; }
}

class MejoraRoboVida extends MejoraHabilidad {
    private static final int DANO_EXTRA = 5;
    public MejoraRoboVida(HabilidadPersonaje base) { super(base); }
    @Override public void ejecutar() {
        habilidadBase.ejecutar();
        int vidaRecuperada = (habilidadBase.getDano() + DANO_EXTRA) / 4;
        System.out.println("    + Robo de Vida: +" + DANO_EXTRA + " dano, recuperas " + vidaRecuperada + " HP");
    }
    @Override public String getDescripcion() { return habilidadBase.getDescripcion() + " + Robo de Vida"; }
    @Override public int getDano() { return habilidadBase.getDano() + DANO_EXTRA; }
}
