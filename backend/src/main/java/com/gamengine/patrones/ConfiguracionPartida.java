package com.gamengine.patrones;

/**
 * Patrón Prototype — Configuraciones de Partida.
 */

import java.util.HashMap;
import java.util.Map;

// ══════════════════════════════════════════════
// PROTOTYPE: interfaz que define el contrato de clonación.
// ══════════════════════════════════════════════
interface ConfiguracionPartidaPrototype {
    ConfiguracionPartidaPrototype clonar();
    void mostrarInfo();
    String getModo();
    int getMaxJugadores();
    int getDuracionMinutos();
    String getMapa();
    boolean isRankedPermitido();
    boolean isReconexionPermitida();
    void setMaxJugadores(int maxJugadores);
    void setDuracionMinutos(int duracionMinutos);
}

// ══════════════════════════════════════════════
// CONCRETE PROTOTYPE: 1 vs 1
// ══════════════════════════════════════════════
class ConfiguracionDuelo implements ConfiguracionPartidaPrototype {
    private String modo;
    private int maxJugadores;
    private int duracionMinutos;
    private String mapa;
    private boolean rankedPermitido;
    private boolean reconexionPermitida;

    public ConfiguracionDuelo() {
        this.modo = "1 vs 1";
        this.maxJugadores = 2;
        this.duracionMinutos = 10;
        this.mapa = "Arena del Coliseo";
        this.rankedPermitido = true;
        this.reconexionPermitida = false;
    }

    @Override
    public ConfiguracionPartidaPrototype clonar() {
        ConfiguracionDuelo copia = new ConfiguracionDuelo();
        copia.modo = this.modo;
        copia.maxJugadores = this.maxJugadores;
        copia.duracionMinutos = this.duracionMinutos;
        copia.mapa = this.mapa;
        copia.rankedPermitido = this.rankedPermitido;
        copia.reconexionPermitida = this.reconexionPermitida;
        return copia;
    }

    @Override public String getModo() { return modo; }
    @Override public int getMaxJugadores() { return maxJugadores; }
    @Override public int getDuracionMinutos() { return duracionMinutos; }
    @Override public String getMapa() { return mapa; }
    @Override public boolean isRankedPermitido() { return rankedPermitido; }
    @Override public boolean isReconexionPermitida() { return reconexionPermitida; }
    @Override public void setMaxJugadores(int maxJugadores) { this.maxJugadores = maxJugadores; }
    @Override public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    @Override
    public void mostrarInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║  CONFIGURACION DE PARTIDA");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  Modo:          " + modo);
        System.out.println("  ║  Jugadores:     " + maxJugadores);
        System.out.println("  ║  Duracion:      " + duracionMinutos + " min");
        System.out.println("  ║  Mapa:          " + mapa);
        System.out.println("  ║  Ranked:        " + (rankedPermitido ? "Si" : "No"));
        System.out.println("  ║  Reconexion:    " + (reconexionPermitida ? "Si" : "No"));
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}

// ══════════════════════════════════════════════
// CONCRETE PROTOTYPE: Equipos 3v3
// ══════════════════════════════════════════════
class ConfiguracionEquipos implements ConfiguracionPartidaPrototype {
    private String modo;
    private int maxJugadores;
    private int duracionMinutos;
    private String mapa;
    private boolean rankedPermitido;
    private boolean reconexionPermitida;

    public ConfiguracionEquipos() {
        this.modo = "Equipos 3v3";
        this.maxJugadores = 6;
        this.duracionMinutos = 20;
        this.mapa = "Campo de Batalla";
        this.rankedPermitido = true;
        this.reconexionPermitida = true;
    }

    @Override
    public ConfiguracionPartidaPrototype clonar() {
        ConfiguracionEquipos copia = new ConfiguracionEquipos();
        copia.modo = this.modo;
        copia.maxJugadores = this.maxJugadores;
        copia.duracionMinutos = this.duracionMinutos;
        copia.mapa = this.mapa;
        copia.rankedPermitido = this.rankedPermitido;
        copia.reconexionPermitida = this.reconexionPermitida;
        return copia;
    }

    @Override public String getModo() { return modo; }
    @Override public int getMaxJugadores() { return maxJugadores; }
    @Override public int getDuracionMinutos() { return duracionMinutos; }
    @Override public String getMapa() { return mapa; }
    @Override public boolean isRankedPermitido() { return rankedPermitido; }
    @Override public boolean isReconexionPermitida() { return reconexionPermitida; }
    @Override public void setMaxJugadores(int maxJugadores) { this.maxJugadores = maxJugadores; }
    @Override public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    @Override
    public void mostrarInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║  CONFIGURACION DE PARTIDA");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  Modo:          " + modo);
        System.out.println("  ║  Jugadores:     " + maxJugadores);
        System.out.println("  ║  Duracion:      " + duracionMinutos + " min");
        System.out.println("  ║  Mapa:          " + mapa);
        System.out.println("  ║  Ranked:        " + (rankedPermitido ? "Si" : "No"));
        System.out.println("  ║  Reconexion:    " + (reconexionPermitida ? "Si" : "No"));
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}

// ══════════════════════════════════════════════
// CONCRETE PROTOTYPE: Battle Royale
// ══════════════════════════════════════════════
class ConfiguracionBattleRoyale implements ConfiguracionPartidaPrototype {
    private String modo;
    private int maxJugadores;
    private int duracionMinutos;
    private String mapa;
    private boolean rankedPermitido;
    private boolean reconexionPermitida;

    public ConfiguracionBattleRoyale() {
        this.modo = "Battle Royale";
        this.maxJugadores = 50;
        this.duracionMinutos = 30;
        this.mapa = "Isla Abandonada";
        this.rankedPermitido = false;
        this.reconexionPermitida = false;
    }

    @Override
    public ConfiguracionPartidaPrototype clonar() {
        ConfiguracionBattleRoyale copia = new ConfiguracionBattleRoyale();
        copia.modo = this.modo;
        copia.maxJugadores = this.maxJugadores;
        copia.duracionMinutos = this.duracionMinutos;
        copia.mapa = this.mapa;
        copia.rankedPermitido = this.rankedPermitido;
        copia.reconexionPermitida = this.reconexionPermitida;
        return copia;
    }

    @Override public String getModo() { return modo; }
    @Override public int getMaxJugadores() { return maxJugadores; }
    @Override public int getDuracionMinutos() { return duracionMinutos; }
    @Override public String getMapa() { return mapa; }
    @Override public boolean isRankedPermitido() { return rankedPermitido; }
    @Override public boolean isReconexionPermitida() { return reconexionPermitida; }
    @Override public void setMaxJugadores(int maxJugadores) { this.maxJugadores = maxJugadores; }
    @Override public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    @Override
    public void mostrarInfo() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║  CONFIGURACION DE PARTIDA");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  Modo:          " + modo);
        System.out.println("  ║  Jugadores:     " + maxJugadores);
        System.out.println("  ║  Duracion:      " + duracionMinutos + " min");
        System.out.println("  ║  Mapa:          " + mapa);
        System.out.println("  ║  Ranked:        " + (rankedPermitido ? "Si" : "No"));
        System.out.println("  ║  Reconexion:    " + (reconexionPermitida ? "Si" : "No"));
        System.out.println("  ╚══════════════════════════════════════╝");
    }
}

// ══════════════════════════════════════════════
// REGISTRY: almacena prototipos y entrega clones.
// ══════════════════════════════════════════════
class RegistroConfiguraciones {
    private Map<String, ConfiguracionPartidaPrototype> prototipos = new HashMap<>();

    public void registrar(String clave, ConfiguracionPartidaPrototype prototipo) {
        prototipos.put(clave, prototipo);
    }

    public ConfiguracionPartidaPrototype obtener(String clave) {
        ConfiguracionPartidaPrototype prototipo = prototipos.get(clave);
        if (prototipo == null) {
            System.out.println("  [Error] No existe configuracion con clave: " + clave);
            return null;
        }
        return prototipo.clonar();
    }

    public java.util.Set<String> claves() {
        return prototipos.keySet();
    }

    public void listarConfiguraciones() {
        System.out.println("\n  ═══ MODOS DE JUEGO DISPONIBLES ═══");
        for (Map.Entry<String, ConfiguracionPartidaPrototype> entry : prototipos.entrySet()) {
            System.out.println("  - " + entry.getKey() + " (" + entry.getValue().getModo() + ")");
        }
    }
}
