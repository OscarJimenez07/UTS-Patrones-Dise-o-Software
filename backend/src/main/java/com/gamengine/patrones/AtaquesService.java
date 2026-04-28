package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio público que expone Bridge para los controllers.
 */
public class AtaquesService {

    private static final String[] TIPOS = {"cuerpo", "distancia", "magico"};
    private static final String[] ELEMENTOS = {"fuego", "hielo", "electrico"};

    public Map<String, Object> ejecutar(String tipo, String elemento) {
        ElementoAtaque el = crearElemento(elemento);
        AtaquePersonaje at = crearAtaque(tipo, el);
        if (el == null || at == null) {
            return null;
        }
        // Capturable
        at.ejecutarAtaque();

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("tipo", at.getTipoAtaque());
        dto.put("elemento", el.getNombre());
        dto.put("danoBase", at.getDanoBase());
        dto.put("danoElemento", el.getDanoElemento());
        dto.put("danoTotal", at.getDanoTotal());
        dto.put("efecto", el.getEfecto());
        dto.put("descripcion", at.getDescripcion());
        return dto;
    }

    public List<Map<String, Object>> combinaciones() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (String t : TIPOS) {
            for (String e : ELEMENTOS) {
                ElementoAtaque el = crearElemento(e);
                AtaquePersonaje at = crearAtaque(t, el);
                Map<String, Object> dto = new LinkedHashMap<>();
                dto.put("tipoClave", t);
                dto.put("elementoClave", e);
                dto.put("tipo", at.getTipoAtaque());
                dto.put("elemento", el.getNombre());
                dto.put("danoTotal", at.getDanoTotal());
                dto.put("efecto", el.getEfecto());
                out.add(dto);
            }
        }
        return out;
    }

    private ElementoAtaque crearElemento(String e) {
        switch (e == null ? "" : e.toLowerCase()) {
            case "fuego": return new ElementoFuego();
            case "hielo": return new ElementoHielo();
            case "electrico": return new ElementoElectrico();
            default: return null;
        }
    }

    private AtaquePersonaje crearAtaque(String t, ElementoAtaque el) {
        if (el == null) return null;
        switch (t == null ? "" : t.toLowerCase()) {
            case "cuerpo": return new AtaqueCuerpoACuerpo(el);
            case "distancia": return new AtaqueDistancia(el);
            case "magico": return new AtaqueMagico(el);
            default: return null;
        }
    }
}
