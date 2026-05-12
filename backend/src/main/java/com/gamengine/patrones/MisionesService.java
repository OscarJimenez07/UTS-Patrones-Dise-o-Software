package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio publico que expone State para los controllers.
 * Genera 3 misiones diarias por jugador y permite avanzar/reclamar/expirar.
 */
public class MisionesService {

    private final Map<String, List<Mision>> misionesPorUsuario = new LinkedHashMap<>();

    public Map<String, Object> obtener(String usuario) {
        List<Mision> lista = misionesPorUsuario.computeIfAbsent(usuario, k -> generarDelDia());
        return toDto(usuario, lista);
    }

    public Map<String, Object> refrescar(String usuario) {
        List<Mision> previas = misionesPorUsuario.get(usuario);
        if (previas != null) {
            for (Mision m : previas) m.expirar();
        }
        List<Mision> nuevas = generarDelDia();
        misionesPorUsuario.put(usuario, nuevas);
        return toDto(usuario, nuevas);
    }

    public Map<String, Object> avanzar(String usuario, String id, int delta) {
        Mision m = buscar(usuario, id);
        if (m == null) return null;
        m.avanzar(delta);
        return toDto(usuario, misionesPorUsuario.get(usuario));
    }

    public Map<String, Object> reclamar(String usuario, String id) {
        Mision m = buscar(usuario, id);
        if (m == null) return null;
        m.reclamar();
        return toDto(usuario, misionesPorUsuario.get(usuario));
    }

    private Mision buscar(String usuario, String id) {
        List<Mision> lista = misionesPorUsuario.get(usuario);
        if (lista == null) return null;
        for (Mision m : lista) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    private List<Mision> generarDelDia() {
        List<Mision> base = new ArrayList<>();
        base.add(new Mision(nuevoId(), "Veterano del dia",
                "Juega 2 partidas hoy",
                "50 monedas estandar", 2));
        base.add(new Mision(nuevoId(), "Ataque magistral",
                "Ejecuta 3 ataques con cualquier elemento",
                "1 mejora gratis", 3));
        base.add(new Mision(nuevoId(), "Voz del clan",
                "Envia 1 mensaje al canal del clan",
                "Insignia diaria", 1));
        return base;
    }

    private String nuevoId() {
        return "m-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Map<String, Object> toDto(String usuario, List<Mision> lista) {
        List<Map<String, Object>> filas = new ArrayList<>();
        for (Mision m : lista) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", m.getId());
            dto.put("titulo", m.getTitulo());
            dto.put("descripcion", m.getDescripcion());
            dto.put("recompensa", m.getRecompensa());
            dto.put("progreso", m.getProgreso());
            dto.put("meta", m.getMeta());
            dto.put("estado", m.getEstadoNombre());
            filas.add(dto);
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("usuario", usuario);
        out.put("misiones", filas);
        return out;
    }
}
