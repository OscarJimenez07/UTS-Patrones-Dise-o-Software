/**
 * Patron Decorator — Sistema de Mejoras de Habilidades.
 *
 * Contiene:
 * - Component (HabilidadPersonaje): interfaz base de toda habilidad
 * - ConcreteComponents (GolpeFisico, DisparoMagico, HechizoCurativo): habilidades base
 * - Decorator (MejoraHabilidad): clase abstracta que envuelve una habilidad
 * - ConcreteDecorators (MejoraDanoFuego, MejoraVeneno, MejoraAreaEfecto, MejoraRoboVida):
 *   mejoras que se apilan dinamicamente sobre cualquier habilidad
 */

// ══════════════════════════════════════════════
// COMPONENT: interfaz base que define el contrato
// de toda habilidad del personaje.
// ══════════════════════════════════════════════
interface HabilidadPersonaje {
    void ejecutar();
    String getDescripcion();
    int getDano();
}

// ══════════════════════════════════════════════
// CONCRETE COMPONENTS: habilidades base sin mejoras.
// ══════════════════════════════════════════════

class GolpeFisico implements HabilidadPersonaje {
    @Override
    public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }

    @Override
    public String getDescripcion() {
        return "Golpe Fisico";
    }

    @Override
    public int getDano() {
        return 25;
    }
}

class DisparoMagico implements HabilidadPersonaje {
    @Override
    public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }

    @Override
    public String getDescripcion() {
        return "Disparo Magico";
    }

    @Override
    public int getDano() {
        return 40;
    }
}

class HechizoCurativo implements HabilidadPersonaje {
    @Override
    public void ejecutar() {
        System.out.println("  Ejecutando: " + getDescripcion() + " [" + getDano() + " dano]");
    }

    @Override
    public String getDescripcion() {
        return "Hechizo Curativo";
    }

    @Override
    public int getDano() {
        return 10;
    }
}

// ══════════════════════════════════════════════
// DECORATOR (abstracto): envuelve una HabilidadPersonaje
// y delega las llamadas al componente interno.
// Las subclases anaden comportamiento adicional.
// ══════════════════════════════════════════════
abstract class MejoraHabilidad implements HabilidadPersonaje {
    protected HabilidadPersonaje habilidadBase;

    public MejoraHabilidad(HabilidadPersonaje habilidadBase) {
        this.habilidadBase = habilidadBase;
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
    }

    @Override
    public String getDescripcion() {
        return habilidadBase.getDescripcion();
    }

    @Override
    public int getDano() {
        return habilidadBase.getDano();
    }
}

// ══════════════════════════════════════════════
// CONCRETE DECORATOR 1: anade dano de fuego.
// ══════════════════════════════════════════════
class MejoraDanoFuego extends MejoraHabilidad {
    private static final int DANO_FUEGO = 15;

    public MejoraDanoFuego(HabilidadPersonaje habilidadBase) {
        super(habilidadBase);
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Fuego: +" + DANO_FUEGO + " dano adicional (quemadura)");
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

// ══════════════════════════════════════════════
// CONCRETE DECORATOR 2: anade dano de veneno.
// ══════════════════════════════════════════════
class MejoraVeneno extends MejoraHabilidad {
    private static final int DANO_VENENO = 10;

    public MejoraVeneno(HabilidadPersonaje habilidadBase) {
        super(habilidadBase);
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Veneno: +" + DANO_VENENO + " dano por turno (3 turnos)");
    }

    @Override
    public String getDescripcion() {
        return habilidadBase.getDescripcion() + " + Veneno";
    }

    @Override
    public int getDano() {
        return habilidadBase.getDano() + DANO_VENENO;
    }
}

// ══════════════════════════════════════════════
// CONCRETE DECORATOR 3: anade efecto de area.
// ══════════════════════════════════════════════
class MejoraAreaEfecto extends MejoraHabilidad {
    private static final int DANO_AREA = 8;

    public MejoraAreaEfecto(HabilidadPersonaje habilidadBase) {
        super(habilidadBase);
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
        System.out.println("    + Efecto Area: +" + DANO_AREA + " dano a enemigos cercanos");
    }

    @Override
    public String getDescripcion() {
        return habilidadBase.getDescripcion() + " + Area";
    }

    @Override
    public int getDano() {
        return habilidadBase.getDano() + DANO_AREA;
    }
}

// ══════════════════════════════════════════════
// CONCRETE DECORATOR 4: anade robo de vida.
// ══════════════════════════════════════════════
class MejoraRoboVida extends MejoraHabilidad {
    private static final int DANO_EXTRA = 5;

    public MejoraRoboVida(HabilidadPersonaje habilidadBase) {
        super(habilidadBase);
    }

    @Override
    public void ejecutar() {
        habilidadBase.ejecutar();
        int vidaRecuperada = (habilidadBase.getDano() + DANO_EXTRA) / 4;
        System.out.println("    + Robo de Vida: +" + DANO_EXTRA + " dano, recuperas " + vidaRecuperada + " HP");
    }

    @Override
    public String getDescripcion() {
        return habilidadBase.getDescripcion() + " + Robo de Vida";
    }

    @Override
    public int getDano() {
        return habilidadBase.getDano() + DANO_EXTRA;
    }
}
