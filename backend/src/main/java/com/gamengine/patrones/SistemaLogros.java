package com.gamengine.patrones;

/**
 * Patron Observer — Sistema de Logros y Recompensas.
 *
 * El sistema desacopla quien produce eventos de quien los evalua. Cada logro
 * es un observer suscrito a eventos del juego. Al recibir un evento, decide
 * si se desbloquea y otorga recompensa.
 *
 * Roles:
 *   - Subject:   EventosJuego (notifica a sus observers)
 *   - Observer:  LogroObserver (interfaz)
 *   - Concretos: LogroPorPartidas, LogroPorVictorias, LogroPorClan, LogroPorCompras
 *   - Event:     EventoJuego (carga util del evento)
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ══════════════════════════════════════════════
// EVENT: datos del evento que circula por el sistema
// ══════════════════════════════════════════════
class EventoJuego {
    private final String tipo;
    private final String usuario;
    private final Map<String, Object> datos;

    public EventoJuego(String tipo, String usuario, Map<String, Object> datos) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.datos = datos == null ? new LinkedHashMap<>() : datos;
    }

    public String getTipo() { return tipo; }
    public String getUsuario() { return usuario; }
    public Map<String, Object> getDatos() { return datos; }
}

// ══════════════════════════════════════════════
// OBSERVER: contrato del logro
// ══════════════════════════════════════════════
interface LogroObserver {
    String getClave();
    String getNombre();
    String getDescripcion();
    String getRecompensa();
    int getProgreso(String usuario);
    int getMeta();
    boolean estaDesbloqueado(String usuario);
    void onEvento(EventoJuego ev);
}

// ══════════════════════════════════════════════
// CLASE BASE: contador con meta (usada por la mayoria de logros)
// ══════════════════════════════════════════════
abstract class LogroContador implements LogroObserver {
    protected final Map<String, Integer> progreso = new LinkedHashMap<>();
    protected final Map<String, Boolean> desbloqueado = new LinkedHashMap<>();

    @Override public int getProgreso(String usuario) { return progreso.getOrDefault(usuario, 0); }
    @Override public boolean estaDesbloqueado(String usuario) { return desbloqueado.getOrDefault(usuario, false); }

    protected void incrementar(String usuario, int delta) {
        int actual = progreso.getOrDefault(usuario, 0) + delta;
        progreso.put(usuario, actual);
        if (actual >= getMeta() && !desbloqueado.getOrDefault(usuario, false)) {
            desbloqueado.put(usuario, true);
            System.out.println("  [Logro DESBLOQUEADO] " + usuario + " -> " + getNombre()
                    + " (recompensa: " + getRecompensa() + ")");
        }
    }
}

// ══════════════════════════════════════════════
// CONCRETE OBSERVERS
// ══════════════════════════════════════════════
class LogroPorPartidas extends LogroContador {
    @Override public String getClave() { return "JUGADOR_VETERANO"; }
    @Override public String getNombre() { return "Veterano"; }
    @Override public String getDescripcion() { return "Juega 5 partidas"; }
    @Override public String getRecompensa() { return "100 monedas estandar"; }
    @Override public int getMeta() { return 5; }

    @Override
    public void onEvento(EventoJuego ev) {
        if ("PARTIDA_FINALIZADA".equals(ev.getTipo())) {
            incrementar(ev.getUsuario(), 1);
        }
    }
}

class LogroPorVictorias extends LogroContador {
    @Override public String getClave() { return "CAZADOR"; }
    @Override public String getNombre() { return "Cazador"; }
    @Override public String getDescripcion() { return "Gana 3 partidas"; }
    @Override public String getRecompensa() { return "Skin exclusiva (estandar)"; }
    @Override public int getMeta() { return 3; }

    @Override
    public void onEvento(EventoJuego ev) {
        if ("PARTIDA_FINALIZADA".equals(ev.getTipo())
                && Boolean.TRUE.equals(ev.getDatos().get("victoria"))) {
            incrementar(ev.getUsuario(), 1);
        }
    }
}

class LogroPorClan extends LogroContador {
    @Override public String getClave() { return "SOCIAL"; }
    @Override public String getNombre() { return "Social"; }
    @Override public String getDescripcion() { return "Envia 2 mensajes a tu clan"; }
    @Override public String getRecompensa() { return "Insignia de clan"; }
    @Override public int getMeta() { return 2; }

    @Override
    public void onEvento(EventoJuego ev) {
        if ("MENSAJE_CLAN".equals(ev.getTipo())) {
            incrementar(ev.getUsuario(), 1);
        }
    }
}

class LogroPorCompras extends LogroContador {
    @Override public String getClave() { return "COMPRADOR"; }
    @Override public String getNombre() { return "Comprador"; }
    @Override public String getDescripcion() { return "Realiza tu primera compra"; }
    @Override public String getRecompensa() { return "50 monedas premium"; }
    @Override public int getMeta() { return 1; }

    @Override
    public void onEvento(EventoJuego ev) {
        if ("COMPRA_REALIZADA".equals(ev.getTipo())) {
            incrementar(ev.getUsuario(), 1);
        }
    }
}

// ══════════════════════════════════════════════
// SUBJECT: registra y notifica observers
// ══════════════════════════════════════════════
class EventosJuego {
    private final List<LogroObserver> observers = new ArrayList<>();

    public void suscribir(LogroObserver o) { observers.add(o); }

    public void publicar(EventoJuego ev) {
        System.out.println("  [Subject] Evento '" + ev.getTipo() + "' de " + ev.getUsuario()
                + " -> notificando " + observers.size() + " observers");
        for (LogroObserver o : observers) {
            o.onEvento(ev);
        }
    }

    public List<LogroObserver> getObservers() { return observers; }
}
