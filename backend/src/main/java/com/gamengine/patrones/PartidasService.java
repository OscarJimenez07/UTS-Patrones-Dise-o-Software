package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio público que expone Prototype para los controllers.
 * Mantiene un único registro de prototipos a nivel de aplicación.
 */
public class PartidasService {

    private final RegistroConfiguraciones registro;

    public PartidasService() {
        this.registro = new RegistroConfiguraciones();
        this.registro.registrar("Duelo", new ConfiguracionDuelo());
        this.registro.registrar("Equipos", new ConfiguracionEquipos());
        this.registro.registrar("BattleRoyale", new ConfiguracionBattleRoyale());
    }

    public List<Map<String, Object>> listarModos() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (String clave : registro.claves()) {
            ConfiguracionPartidaPrototype proto = registro.obtener(clave);
            out.add(toDto(clave, proto));
        }
        return out;
    }

    public Map<String, Object> crear(String clave, Integer maxJugadores, Integer duracionMinutos) {
        ConfiguracionPartidaPrototype clon = registro.obtener(clave);
        if (clon == null) {
            return null;
        }
        if (maxJugadores != null) clon.setMaxJugadores(maxJugadores);
        if (duracionMinutos != null) clon.setDuracionMinutos(duracionMinutos);
        // Disparar mostrarInfo() para capturar log
        clon.mostrarInfo();
        return toDto(clave, clon);
    }

    private Map<String, Object> toDto(String clave, ConfiguracionPartidaPrototype c) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("clave", clave);
        dto.put("modo", c.getModo());
        dto.put("maxJugadores", c.getMaxJugadores());
        dto.put("duracionMinutos", c.getDuracionMinutos());
        dto.put("mapa", c.getMapa());
        dto.put("rankedPermitido", c.isRankedPermitido());
        dto.put("reconexionPermitida", c.isReconexionPermitida());
        return dto;
    }
}
