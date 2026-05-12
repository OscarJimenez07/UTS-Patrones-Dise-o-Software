package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio publico que expone Observer para los controllers.
 * Mantiene un Subject unico con los logros suscritos.
 */
public class LogrosService {

    private final EventosJuego eventos;

    public LogrosService() {
        this.eventos = new EventosJuego();
        eventos.suscribir(new LogroPorPartidas());
        eventos.suscribir(new LogroPorVictorias());
        eventos.suscribir(new LogroPorClan());
        eventos.suscribir(new LogroPorCompras());

        sembrarEventosIniciales("oscar");
    }

    public List<Map<String, Object>> catalogo(String usuario) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (LogroObserver o : eventos.getObservers()) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("clave", o.getClave());
            dto.put("nombre", o.getNombre());
            dto.put("descripcion", o.getDescripcion());
            dto.put("recompensa", o.getRecompensa());
            dto.put("meta", o.getMeta());
            dto.put("progreso", usuario == null ? 0 : o.getProgreso(usuario));
            dto.put("desbloqueado", usuario != null && o.estaDesbloqueado(usuario));
            out.add(dto);
        }
        return out;
    }

    public Map<String, Object> publicar(String tipo, String usuario, Map<String, Object> datos) {
        EventoJuego ev = new EventoJuego(tipo, usuario, datos);
        eventos.publicar(ev);
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("evento", tipo);
        out.put("usuario", usuario);
        out.put("logros", catalogo(usuario));
        return out;
    }

    private void sembrarEventosIniciales(String usuario) {
        Map<String, Object> victoria = new LinkedHashMap<>();
        victoria.put("victoria", true);
        eventos.publicar(new EventoJuego("PARTIDA_FINALIZADA", usuario, victoria));
        eventos.publicar(new EventoJuego("PARTIDA_FINALIZADA", usuario, victoria));
        eventos.publicar(new EventoJuego("MENSAJE_CLAN", usuario, null));
    }
}
