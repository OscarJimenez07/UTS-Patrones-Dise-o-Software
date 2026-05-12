package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio publico que expone Memento para los controllers.
 * Mantiene un caretaker por jugador en memoria.
 */
public class HistorialService {

    private final Map<String, HistorialJugador> caretakers = new LinkedHashMap<>();

    public HistorialService() {
        sembrarHistorialInicial("oscar");
        sembrarHistorialInicial("anderson");
    }

    public Map<String, Object> guardar(String usuario, String modo, boolean victoria,
                                       int puntuacion, int duracionSegundos,
                                       List<String> mejorasAplicadas) {
        PartidaEnCurso partida = new PartidaEnCurso(usuario, modo);
        if (mejorasAplicadas != null) {
            for (String m : mejorasAplicadas) partida.registrarMejora(m);
        }
        partida.marcarResultado(victoria, puntuacion, duracionSegundos);

        String id = "p-" + UUID.randomUUID().toString().substring(0, 8);
        SnapshotPartida s = partida.tomarSnapshot(id);
        caretakerDe(usuario).guardar(s);
        return snapshotToDto(s);
    }

    public List<Map<String, Object>> listar(String usuario) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (SnapshotPartida s : caretakerDe(usuario).listar()) {
            out.add(snapshotToDto(s));
        }
        return out;
    }

    public Map<String, Object> detalle(String usuario, String id) {
        SnapshotPartida s = caretakerDe(usuario).buscar(id);
        if (s == null) return null;
        // Probamos restauracion (demuestra el roundtrip Memento)
        PartidaEnCurso restaurada = new PartidaEnCurso("", "");
        restaurada.restaurarDesde(s);
        return snapshotToDto(s);
    }

    public int limpiar(String usuario) {
        return caretakerDe(usuario).limpiar();
    }

    private HistorialJugador caretakerDe(String usuario) {
        return caretakers.computeIfAbsent(usuario, k -> new HistorialJugador());
    }

    private void sembrarHistorialInicial(String usuario) {
        guardar(usuario, "Duelo", true, 1850, 240, List.of("Aumento de daño"));
        guardar(usuario, "Equipos", false, 720, 480, List.of("Escudo magico", "Aumento de daño"));
        guardar(usuario, "BattleRoyale", true, 3200, 1200, List.of());
    }

    private Map<String, Object> snapshotToDto(SnapshotPartida s) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", s.getId());
        dto.put("usuario", s.getUsuario());
        dto.put("modo", s.getModo());
        dto.put("victoria", s.isVictoria());
        dto.put("puntuacion", s.getPuntuacion());
        dto.put("duracionSegundos", s.getDuracionSegundos());
        dto.put("mejorasAplicadas", s.getMejorasAplicadas());
        dto.put("resumen", s.getResumen());
        dto.put("timestamp", s.getTimestamp());
        return dto;
    }
}
