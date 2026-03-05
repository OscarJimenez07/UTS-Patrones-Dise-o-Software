/**
 * PATRON ABSTRACT FACTORY — Ítems de la Tienda Virtual
 *
 * Este archivo contiene:
 * - Las interfaces de productos (Skin, Potenciador, Recompensa)
 * - Las implementaciones concretas (Estándar y Premium)
 * - La interfaz Abstract Factory (TiendaItemFactory)
 * - Las fábricas concretas (TiendaEstandarFactory, TiendaPremiumFactory)
 */

// =============================================
// PRODUCTOS ABSTRACTOS (interfaces)
// =============================================

interface Skin {
    String getNombre();
    String getRareza();
    void aplicar();
}

interface Potenciador {
    String getNombre();
    int getDuracion();
    void activar();
}

interface Recompensa {
    String getDescripcion();
    int getMonedasOtorgadas();
    void reclamar();
}

// =============================================
// PRODUCTOS CONCRETOS — NIVEL ESTÁNDAR
// =============================================

class SkinEstandar implements Skin {
    private String nombre;

    public SkinEstandar(String nombre) { this.nombre = nombre; }

    @Override public String getNombre() { return nombre; }
    @Override public String getRareza() { return "Común"; }
    @Override public void aplicar() {
        System.out.println("  [Skin Estándar] Aplicando '" + nombre + "' (rareza: Común)");
    }
}

class PotenciadorEstandar implements Potenciador {
    private String nombre;

    public PotenciadorEstandar(String nombre) { this.nombre = nombre; }

    @Override public String getNombre() { return nombre; }
    @Override public int getDuracion() { return 5; }
    @Override public void activar() {
        System.out.println("  [Potenciador Estándar] Activando '" + nombre + "' por 5 min");
    }
}

class RecompensaEstandar implements Recompensa {
    private String descripcion;

    public RecompensaEstandar(String descripcion) { this.descripcion = descripcion; }

    @Override public String getDescripcion() { return descripcion; }
    @Override public int getMonedasOtorgadas() { return 50; }
    @Override public void reclamar() {
        System.out.println("  [Recompensa Estándar] '" + descripcion + "' -> +50 monedas");
    }
}

// =============================================
// PRODUCTOS CONCRETOS — NIVEL PREMIUM
// =============================================

class SkinPremium implements Skin {
    private String nombre;

    public SkinPremium(String nombre) { this.nombre = nombre; }

    @Override public String getNombre() { return nombre; }
    @Override public String getRareza() { return "Legendaria"; }
    @Override public void aplicar() {
        System.out.println("  [Skin Premium] Aplicando '" + nombre + "' (rareza: Legendaria) con efectos especiales");
    }
}

class PotenciadorPremium implements Potenciador {
    private String nombre;

    public PotenciadorPremium(String nombre) { this.nombre = nombre; }

    @Override public String getNombre() { return nombre; }
    @Override public int getDuracion() { return 30; }
    @Override public void activar() {
        System.out.println("  [Potenciador Premium] Activando '" + nombre + "' por 30 min (potencia x3)");
    }
}

class RecompensaPremium implements Recompensa {
    private String descripcion;

    public RecompensaPremium(String descripcion) { this.descripcion = descripcion; }

    @Override public String getDescripcion() { return descripcion; }
    @Override public int getMonedasOtorgadas() { return 500; }
    @Override public void reclamar() {
        System.out.println("  [Recompensa Premium] '" + descripcion + "' -> +500 monedas + item exclusivo");
    }
}

// =============================================
// ABSTRACT FACTORY + FÁBRICAS CONCRETAS
// =============================================

/**
 * ABSTRACT FACTORY: define la creación de una FAMILIA completa de ítems.
 * Cada fábrica concreta crea Skin + Potenciador + Recompensa de su nivel.
 */
interface TiendaItemFactory {
    Skin crearSkin(String nombre);
    Potenciador crearPotenciador(String nombre);
    Recompensa crearRecompensa(String descripcion);
}

/** Fábrica concreta: familia de ítems Estándar (moneda del juego). */
class TiendaEstandarFactory implements TiendaItemFactory {
    @Override public Skin crearSkin(String nombre) { return new SkinEstandar(nombre); }
    @Override public Potenciador crearPotenciador(String nombre) { return new PotenciadorEstandar(nombre); }
    @Override public Recompensa crearRecompensa(String descripcion) { return new RecompensaEstandar(descripcion); }
}

/** Fábrica concreta: familia de ítems Premium (moneda real). */
class TiendaPremiumFactory implements TiendaItemFactory {
    @Override public Skin crearSkin(String nombre) { return new SkinPremium(nombre); }
    @Override public Potenciador crearPotenciador(String nombre) { return new PotenciadorPremium(nombre); }
    @Override public Recompensa crearRecompensa(String descripcion) { return new RecompensaPremium(descripcion); }
}
