package com.gamengine.patrones;

/**
 * Patron Memento — Historial de Partidas.
 *
 * Memento captura el estado interno de una partida en un objeto inmutable
 * (snapshot), permitiendo restaurarlo despues sin exponer la estructura interna.
 *
 * Roles:
 *   - Originator: PartidaEnCurso (genera y restaura snapshots)
 *   - Memento:    SnapshotPartida (estado inmutable)
 *   - Caretaker:  HistorialJugador (guarda los snapshots por jugador)
 */

import java.util.ArrayList;
import java.util.List;

// ══════════════════════════════════════════════
// MEMENTO: snapshot inmutable del estado de una partida
// ══════════════════════════════════════════════
class SnapshotPartida {
    private final String id;
    private final String usuario;
    private final String modo;
    private final boolean victoria;
    private final int puntuacion;
    private final int duracionSegundos;
    private final List<String> mejorasAplicadas;
    private final String resumen;
    private final long timestamp;

    public SnapshotPartida(String id, String usuario, String modo, boolean victoria,
                           int puntuacion, int duracionSegundos,
                           List<String> mejorasAplicadas, String resumen) {
        this.id = id;
        this.usuario = usuario;
        this.modo = modo;
        this.victoria = victoria;
        this.puntuacion = puntuacion;
        this.duracionSegundos = duracionSegundos;
        this.mejorasAplicadas = mejorasAplicadas == null
                ? new ArrayList<>() : new ArrayList<>(mejorasAplicadas);
        this.resumen = resumen;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getModo() { return modo; }
    public boolean isVictoria() { return victoria; }
    public int getPuntuacion() { return puntuacion; }
    public int getDuracionSegundos() { return duracionSegundos; }
    public List<String> getMejorasAplicadas() { return new ArrayList<>(mejorasAplicadas); }
    public String getResumen() { return resumen; }
    public long getTimestamp() { return timestamp; }
}

// ══════════════════════════════════════════════
// ORIGINATOR: partida activa que produce y restaura mementos
// ══════════════════════════════════════════════
class PartidaEnCurso {
    private String usuario;
    private String modo;
    private boolean victoria;
    private int puntuacion;
    private int duracionSegundos;
    private final List<String> mejorasAplicadas = new ArrayList<>();

    public PartidaEnCurso(String usuario, String modo) {
        this.usuario = usuario;
        this.modo = modo;
    }

    public void registrarMejora(String mejora) { mejorasAplicadas.add(mejora); }
    public void marcarResultado(boolean victoria, int puntuacion, int duracionSegundos) {
        this.victoria = victoria;
        this.puntuacion = puntuacion;
        this.duracionSegundos = duracionSegundos;
    }

    public SnapshotPartida tomarSnapshot(String id) {
        String resumen = (victoria ? "Victoria" : "Derrota") + " en " + modo
                + " con " + puntuacion + " pts en " + duracionSegundos + "s";
        System.out.println("  [Memento] Snapshot creado para partida " + id
                + " (usuario=" + usuario + ", modo=" + modo + ")");
        return new SnapshotPartida(id, usuario, modo, victoria,
                puntuacion, duracionSegundos, mejorasAplicadas, resumen);
    }

    public void restaurarDesde(SnapshotPartida s) {
        this.usuario = s.getUsuario();
        this.modo = s.getModo();
        this.victoria = s.isVictoria();
        this.puntuacion = s.getPuntuacion();
        this.duracionSegundos = s.getDuracionSegundos();
        this.mejorasAplicadas.clear();
        this.mejorasAplicadas.addAll(s.getMejorasAplicadas());
        System.out.println("  [Memento] Partida restaurada desde snapshot " + s.getId());
    }

    public String getUsuario() { return usuario; }
    public String getModo() { return modo; }
}

// ══════════════════════════════════════════════
// CARETAKER: guarda mementos por jugador sin acceder a su contenido
// ══════════════════════════════════════════════
class HistorialJugador {
    private final List<SnapshotPartida> snapshots = new ArrayList<>();

    public void guardar(SnapshotPartida s) {
        snapshots.add(0, s);
        System.out.println("  [Caretaker] Snapshot " + s.getId() + " agregado al historial.");
    }

    public List<SnapshotPartida> listar() {
        return new ArrayList<>(snapshots);
    }

    public SnapshotPartida buscar(String id) {
        for (SnapshotPartida s : snapshots) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }

    public int limpiar() {
        int n = snapshots.size();
        snapshots.clear();
        System.out.println("  [Caretaker] Historial limpiado (" + n + " entradas).");
        return n;
    }
}
