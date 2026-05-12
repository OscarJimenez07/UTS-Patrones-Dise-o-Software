package com.gamengine.patrones;

/**
 * Patron Mediator — Chat en Tiempo Real.
 *
 * El mediator centraliza la comunicacion entre jugadores. Los participantes
 * (colleagues) solo conocen al mediador, evitando referencias directas entre
 * ellos. Se pueden anadir reglas (canales, moderacion) sin tocar a los
 * participantes.
 *
 * Roles:
 *   - Mediator:          SalaDeChat (interfaz)
 *   - ConcreteMediator:  SalaChatCentral
 *   - Colleague:         UsuarioChat
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ══════════════════════════════════════════════
// MENSAJE
// ══════════════════════════════════════════════
class MensajeChat {
    private final String canal;
    private final String remitente;
    private final String contenido;
    private final long timestamp;

    public MensajeChat(String canal, String remitente, String contenido) {
        this.canal = canal;
        this.remitente = remitente;
        this.contenido = contenido;
        this.timestamp = System.currentTimeMillis();
    }

    public String getCanal() { return canal; }
    public String getRemitente() { return remitente; }
    public String getContenido() { return contenido; }
    public long getTimestamp() { return timestamp; }
}

// ══════════════════════════════════════════════
// MEDIATOR
// ══════════════════════════════════════════════
interface SalaDeChat {
    void registrar(UsuarioChat usuario);
    void enviar(String canal, String remitente, String contenido);
    List<MensajeChat> historial(String canal);
}

// ══════════════════════════════════════════════
// COLLEAGUE
// ══════════════════════════════════════════════
class UsuarioChat {
    private final String nombre;
    private SalaDeChat sala;
    private final List<MensajeChat> recibidos = new ArrayList<>();

    public UsuarioChat(String nombre) { this.nombre = nombre; }

    public void unirse(SalaDeChat sala) {
        this.sala = sala;
        sala.registrar(this);
    }

    public void enviar(String canal, String contenido) {
        sala.enviar(canal, nombre, contenido);
    }

    public void recibir(MensajeChat m) {
        recibidos.add(m);
        System.out.println("  [" + nombre + "] recibio en #" + m.getCanal()
                + " de " + m.getRemitente() + ": " + m.getContenido());
    }

    public String getNombre() { return nombre; }
    public List<MensajeChat> getRecibidos() { return recibidos; }
}

// ══════════════════════════════════════════════
// CONCRETE MEDIATOR
// ══════════════════════════════════════════════
class SalaChatCentral implements SalaDeChat {

    private final List<UsuarioChat> usuarios = new ArrayList<>();
    private final Map<String, List<MensajeChat>> historiales = new LinkedHashMap<>();
    private final List<String> palabrasFiltradas = List.of("idiota", "estupido", "tonto");

    @Override
    public void registrar(UsuarioChat u) {
        if (usuarios.stream().noneMatch(x -> x.getNombre().equalsIgnoreCase(u.getNombre()))) {
            usuarios.add(u);
            System.out.println("  [Mediator] " + u.getNombre() + " se unio a la sala.");
        }
    }

    @Override
    public void enviar(String canal, String remitente, String contenido) {
        String limpio = filtrar(contenido);
        MensajeChat m = new MensajeChat(canal, remitente, limpio);
        historiales.computeIfAbsent(canal, k -> new ArrayList<>()).add(m);

        System.out.println("  [Mediator] " + remitente + " -> #" + canal + ": " + limpio);
        for (UsuarioChat u : usuarios) {
            if (!u.getNombre().equalsIgnoreCase(remitente)) {
                u.recibir(m);
            }
        }
    }

    @Override
    public List<MensajeChat> historial(String canal) {
        return new ArrayList<>(historiales.getOrDefault(canal, new ArrayList<>()));
    }

    public List<String> canales() { return new ArrayList<>(historiales.keySet()); }
    public List<UsuarioChat> usuarios() { return usuarios; }

    private String filtrar(String contenido) {
        if (contenido == null) return "";
        String out = contenido;
        for (String p : palabrasFiltradas) {
            out = out.replaceAll("(?i)" + p, "***");
        }
        return out;
    }
}
