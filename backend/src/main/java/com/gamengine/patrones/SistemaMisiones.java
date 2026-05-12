package com.gamengine.patrones;

/**
 * Patron State — Misiones Diarias.
 *
 * Cada mision tiene un objeto State actual que decide su comportamiento.
 * Las transiciones validas las decide el propio estado, no la mision
 * (evita cadenas de if/else en el contexto).
 *
 * Roles:
 *   - Context: Mision
 *   - State:   EstadoMision
 *   - Concretos: Disponible, EnProgreso, Completada, Reclamada, Expirada
 */

// ══════════════════════════════════════════════
// STATE
// ══════════════════════════════════════════════
interface EstadoMision {
    String nombre();
    void avanzar(Mision m, int delta);
    void reclamar(Mision m);
    void expirar(Mision m);
}

// ══════════════════════════════════════════════
// CONTEXT
// ══════════════════════════════════════════════
class Mision {
    private final String id;
    private final String titulo;
    private final String descripcion;
    private final String recompensa;
    private final int meta;
    private int progreso;
    private EstadoMision estado;

    public Mision(String id, String titulo, String descripcion, String recompensa, int meta) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.recompensa = recompensa;
        this.meta = meta;
        this.progreso = 0;
        this.estado = new EstadoDisponible();
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getRecompensa() { return recompensa; }
    public int getMeta() { return meta; }
    public int getProgreso() { return progreso; }
    public String getEstadoNombre() { return estado.nombre(); }
    public boolean estaCompleta() { return progreso >= meta; }

    public void setEstado(EstadoMision nuevo) {
        System.out.println("  [State] '" + titulo + "' transiciona: "
                + estado.nombre() + " -> " + nuevo.nombre());
        this.estado = nuevo;
    }
    public void sumarProgreso(int delta) { this.progreso = Math.min(meta, progreso + delta); }

    public void avanzar(int delta) { estado.avanzar(this, delta); }
    public void reclamar() { estado.reclamar(this); }
    public void expirar() { estado.expirar(this); }
}

// ══════════════════════════════════════════════
// CONCRETE STATES
// ══════════════════════════════════════════════
class EstadoDisponible implements EstadoMision {
    @Override public String nombre() { return "Disponible"; }
    @Override public void avanzar(Mision m, int delta) {
        m.setEstado(new EstadoEnProgreso());
        m.avanzar(delta);
    }
    @Override public void reclamar(Mision m) {
        System.out.println("  [State] '" + m.getTitulo() + "' no se puede reclamar aun (Disponible).");
    }
    @Override public void expirar(Mision m) { m.setEstado(new EstadoExpirada()); }
}

class EstadoEnProgreso implements EstadoMision {
    @Override public String nombre() { return "EnProgreso"; }
    @Override public void avanzar(Mision m, int delta) {
        m.sumarProgreso(delta);
        System.out.println("  [State] '" + m.getTitulo() + "' progreso "
                + m.getProgreso() + "/" + m.getMeta());
        if (m.estaCompleta()) {
            m.setEstado(new EstadoCompletada());
        }
    }
    @Override public void reclamar(Mision m) {
        System.out.println("  [State] '" + m.getTitulo() + "' aun en progreso, no se puede reclamar.");
    }
    @Override public void expirar(Mision m) { m.setEstado(new EstadoExpirada()); }
}

class EstadoCompletada implements EstadoMision {
    @Override public String nombre() { return "Completada"; }
    @Override public void avanzar(Mision m, int delta) {
        System.out.println("  [State] '" + m.getTitulo() + "' ya esta completada, sin cambios.");
    }
    @Override public void reclamar(Mision m) {
        System.out.println("  [State] '" + m.getTitulo() + "' recompensa entregada: " + m.getRecompensa());
        m.setEstado(new EstadoReclamada());
    }
    @Override public void expirar(Mision m) { m.setEstado(new EstadoExpirada()); }
}

class EstadoReclamada implements EstadoMision {
    @Override public String nombre() { return "Reclamada"; }
    @Override public void avanzar(Mision m, int delta) {
        System.out.println("  [State] '" + m.getTitulo() + "' ya fue reclamada, sin cambios.");
    }
    @Override public void reclamar(Mision m) {
        System.out.println("  [State] '" + m.getTitulo() + "' ya fue reclamada antes.");
    }
    @Override public void expirar(Mision m) { /* terminal */ }
}

class EstadoExpirada implements EstadoMision {
    @Override public String nombre() { return "Expirada"; }
    @Override public void avanzar(Mision m, int delta) {
        System.out.println("  [State] '" + m.getTitulo() + "' expirada, no admite cambios.");
    }
    @Override public void reclamar(Mision m) {
        System.out.println("  [State] '" + m.getTitulo() + "' expiro y ya no se puede reclamar.");
    }
    @Override public void expirar(Mision m) { /* terminal */ }
}
