package com.gamengine.patrones;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio público que expone Decorator para los controllers.
 * Construye una habilidad base y apila las mejoras en el orden indicado.
 */
public class MejorasService {

    public Map<String, Object> aplicar(String base, List<String> mejoras) {
        HabilidadPersonaje habilidad = crearBase(base);
        if (habilidad == null) {
            return null;
        }
        if (mejoras != null) {
            for (String m : mejoras) {
                habilidad = aplicarMejora(habilidad, m);
            }
        }
        // Disparar ejecución para capturar log
        habilidad.ejecutar();

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("descripcion", habilidad.getDescripcion());
        dto.put("danoTotal", habilidad.getDano());
        dto.put("base", base);
        dto.put("mejoras", mejoras);
        return dto;
    }

    private HabilidadPersonaje crearBase(String base) {
        switch (base == null ? "" : base.toLowerCase()) {
            case "golpe": return new GolpeFisico();
            case "disparo": return new DisparoMagico();
            case "hechizo": return new HechizoCurativo();
            default: return null;
        }
    }

    private HabilidadPersonaje aplicarMejora(HabilidadPersonaje h, String tipo) {
        switch (tipo == null ? "" : tipo.toLowerCase()) {
            case "fuego": return new MejoraDanoFuego(h);
            case "veneno": return new MejoraVeneno(h);
            case "area": return new MejoraAreaEfecto(h);
            case "robo": return new MejoraRoboVida(h);
            default: return h;
        }
    }
}
