package com.gamengine.patrones;

/**
 * Patron Strategy — Ranking Global.
 *
 * Encapsula distintos algoritmos de ordenamiento del ranking como objetos
 * intercambiables. El contexto recibe la estrategia y produce la lista
 * ordenada sin conocer detalles del algoritmo.
 *
 * Roles:
 *   - Strategy:          EstrategiaRanking
 *   - ConcreteStrategy:  PorPuntos, PorELO, PorVictorias, PorRatio
 *   - Context:           ServicioRanking
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// ══════════════════════════════════════════════
// ENTIDAD: dato puntuable
// ══════════════════════════════════════════════
class JugadorRanking {
    private final String usuario;
    private int puntos;
    private int elo;
    private int victorias;
    private int derrotas;

    public JugadorRanking(String usuario, int puntos, int elo, int victorias, int derrotas) {
        this.usuario = usuario;
        this.puntos = puntos;
        this.elo = elo;
        this.victorias = victorias;
        this.derrotas = derrotas;
    }

    public String getUsuario() { return usuario; }
    public int getPuntos() { return puntos; }
    public int getElo() { return elo; }
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }
    public double getRatio() {
        int total = victorias + derrotas;
        return total == 0 ? 0 : (double) victorias / total;
    }

    public void registrarPartida(boolean victoria, int puntosGanados, int eloDelta) {
        this.puntos += puntosGanados;
        this.elo += eloDelta;
        if (victoria) victorias++; else derrotas++;
    }
}

// ══════════════════════════════════════════════
// STRATEGY
// ══════════════════════════════════════════════
interface EstrategiaRanking {
    String getCriterio();
    String getDescripcion();
    Comparator<JugadorRanking> comparator();
    String valorMostrado(JugadorRanking j);
}

// ══════════════════════════════════════════════
// CONCRETE STRATEGIES
// ══════════════════════════════════════════════
class RankingPorPuntos implements EstrategiaRanking {
    @Override public String getCriterio() { return "puntos"; }
    @Override public String getDescripcion() { return "Por puntos totales acumulados"; }
    @Override public Comparator<JugadorRanking> comparator() {
        return Comparator.comparingInt(JugadorRanking::getPuntos).reversed();
    }
    @Override public String valorMostrado(JugadorRanking j) { return j.getPuntos() + " pts"; }
}

class RankingPorELO implements EstrategiaRanking {
    @Override public String getCriterio() { return "elo"; }
    @Override public String getDescripcion() { return "Por ELO competitivo"; }
    @Override public Comparator<JugadorRanking> comparator() {
        return Comparator.comparingInt(JugadorRanking::getElo).reversed();
    }
    @Override public String valorMostrado(JugadorRanking j) { return "ELO " + j.getElo(); }
}

class RankingPorVictorias implements EstrategiaRanking {
    @Override public String getCriterio() { return "victorias"; }
    @Override public String getDescripcion() { return "Por victorias absolutas"; }
    @Override public Comparator<JugadorRanking> comparator() {
        return Comparator.comparingInt(JugadorRanking::getVictorias).reversed();
    }
    @Override public String valorMostrado(JugadorRanking j) { return j.getVictorias() + " W"; }
}

class RankingPorRatio implements EstrategiaRanking {
    @Override public String getCriterio() { return "ratio"; }
    @Override public String getDescripcion() { return "Por ratio victoria/derrota"; }
    @Override public Comparator<JugadorRanking> comparator() {
        return Comparator.comparingDouble(JugadorRanking::getRatio).reversed();
    }
    @Override public String valorMostrado(JugadorRanking j) {
        return String.format("%.2f W/L", j.getRatio());
    }
}

// ══════════════════════════════════════════════
// CONTEXT
// ══════════════════════════════════════════════
class ServicioRanking {
    private final List<JugadorRanking> jugadores = new ArrayList<>();

    public void registrar(JugadorRanking j) { jugadores.add(j); }
    public List<JugadorRanking> jugadores() { return jugadores; }

    public List<JugadorRanking> top(EstrategiaRanking estrategia, int n) {
        System.out.println("  [Strategy] Ordenando con criterio '" + estrategia.getCriterio()
                + "' (" + estrategia.getDescripcion() + ")");
        List<JugadorRanking> copia = new ArrayList<>(jugadores);
        copia.sort(estrategia.comparator());
        if (n > 0 && copia.size() > n) {
            return copia.subList(0, n);
        }
        return copia;
    }

    public int posicion(String usuario, EstrategiaRanking estrategia) {
        List<JugadorRanking> ord = top(estrategia, 0);
        for (int i = 0; i < ord.size(); i++) {
            if (ord.get(i).getUsuario().equalsIgnoreCase(usuario)) return i + 1;
        }
        return -1;
    }
}
