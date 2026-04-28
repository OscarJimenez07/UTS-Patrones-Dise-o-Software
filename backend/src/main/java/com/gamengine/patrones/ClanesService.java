package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio público que expone Composite para los controllers.
 * Mantiene la jerarquía del clan en memoria (estado del proceso).
 */
public class ClanesService {

    private final Escuadron clan;
    private final Map<String, Escuadron> escuadrones = new LinkedHashMap<>();

    public ClanesService() {
        this.clan = new Escuadron("Dragones de Acero");

        Escuadron asalto = new Escuadron("Escuadron Asalto");
        asalto.agregar(new JugadorClan("oscar", 1250, "Lider"));
        asalto.agregar(new JugadorClan("anderson", 1180, "Oficial"));
        asalto.agregar(new JugadorClan("proGamer99", 2150, "Miembro"));

        Escuadron soporte = new Escuadron("Escuadron Soporte");
        soporte.agregar(new JugadorClan("shadowKnight", 1980, "Miembro"));
        soporte.agregar(new JugadorClan("dragonSlayer", 1875, "Miembro"));

        clan.agregar(asalto);
        clan.agregar(soporte);
        clan.agregar(new JugadorClan("recluta01", 1000, "Recluta"));

        escuadrones.put("Asalto", asalto);
        escuadrones.put("Soporte", soporte);
    }

    public Map<String, Object> arbol() {
        return nodoToDto(clan);
    }

    public boolean enviarMensajeAClan(String mensaje) {
        clan.recibirMensaje(mensaje);
        return true;
    }

    public boolean enviarMensajeAEscuadron(String escuadronClave, String mensaje) {
        Escuadron e = escuadrones.get(escuadronClave);
        if (e == null) return false;
        e.recibirMensaje(mensaje);
        return true;
    }

    public boolean agregarJugador(String escuadronClave, String nombre, int poder, String rol) {
        JugadorClan nuevo = new JugadorClan(nombre, poder, rol == null ? "Miembro" : rol);
        if (escuadronClave == null || escuadronClave.isBlank()) {
            clan.agregar(nuevo);
            return true;
        }
        Escuadron e = escuadrones.get(escuadronClave);
        if (e == null) return false;
        e.agregar(nuevo);
        return true;
    }

    public List<String> escuadronesDisponibles() {
        return new ArrayList<>(escuadrones.keySet());
    }

    private Map<String, Object> nodoToDto(MiembroClan m) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("nombre", m.getNombre());
        dto.put("poder", m.getPoder());
        dto.put("miembros", m.contarMiembros());
        if (m instanceof Escuadron e) {
            dto.put("tipo", "escuadron");
            List<Map<String, Object>> hijos = new ArrayList<>();
            for (MiembroClan hijo : e.getMiembros()) {
                hijos.add(nodoToDto(hijo));
            }
            dto.put("hijos", hijos);
        } else if (m instanceof JugadorClan j) {
            dto.put("tipo", "jugador");
            dto.put("rol", j.getRol());
        }
        return dto;
    }

    public void mostrarJerarquia() {
        clan.mostrar(0);
    }
}
