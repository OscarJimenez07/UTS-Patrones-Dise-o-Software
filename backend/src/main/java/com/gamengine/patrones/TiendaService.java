package com.gamengine.patrones;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servicio público que expone Abstract Factory para los controllers.
 */
public class TiendaService {

    public Map<String, Object> oferta(String nivel) {
        TiendaItemFactory factory;
        String nivelNombre;
        if ("premium".equalsIgnoreCase(nivel)) {
            factory = new TiendaPremiumFactory();
            nivelNombre = "Premium";
        } else {
            factory = new TiendaEstandarFactory();
            nivelNombre = "Estándar";
        }

        Skin skin = factory.crearSkin("Dragon de Fuego");
        Potenciador pot = factory.crearPotenciador("Velocidad x2");
        Recompensa rec = factory.crearRecompensa("Mision diaria completada");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nivel", nivelNombre);

        Map<String, Object> skinDto = new LinkedHashMap<>();
        skinDto.put("nombre", skin.getNombre());
        skinDto.put("rareza", skin.getRareza());
        result.put("skin", skinDto);

        Map<String, Object> potDto = new LinkedHashMap<>();
        potDto.put("nombre", pot.getNombre());
        potDto.put("duracionMinutos", pot.getDuracion());
        result.put("potenciador", potDto);

        Map<String, Object> recDto = new LinkedHashMap<>();
        recDto.put("descripcion", rec.getDescripcion());
        recDto.put("monedas", rec.getMonedasOtorgadas());
        result.put("recompensa", recDto);

        // Disparar la "demostración" para capturar el log con el patrón en acción.
        Tienda tienda = new Tienda(factory, nivelNombre);
        tienda.mostrarOfertaDelDia();

        return result;
    }
}
