package com.gamengine.patrones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio publico que expone Mediator para los controllers.
 * Mantiene una unica sala con varios canales (global, clan, dm-*).
 */
public class ChatService {

    private final SalaChatCentral sala;
    private final Map<String, UsuarioChat> conocidos = new LinkedHashMap<>();

    public ChatService() {
        this.sala = new SalaChatCentral();
        unirSi("oscar");
        unirSi("anderson");
        unirSi("proGamer99");
        unirSi("shadowKnight");

        // Mensajes iniciales para que la UI no se vea vacia
        conocidos.get("proGamer99").enviar("global", "Buenas a todos, alguien para Duelo?");
        conocidos.get("shadowKnight").enviar("global", "Voy! pero dame 5 minutos.");
        conocidos.get("oscar").enviar("clan", "Reunion del clan a las 8pm");
    }

    public Map<String, Object> enviar(String usuario, String canal, String mensaje) {
        unirSi(usuario);
        conocidos.get(usuario).enviar(canal == null ? "global" : canal, mensaje);
        return historial(canal);
    }

    public Map<String, Object> historial(String canal) {
        String c = canal == null ? "global" : canal;
        List<MensajeChat> hist = sala.historial(c);
        List<Map<String, Object>> filas = new ArrayList<>();
        for (MensajeChat m : hist) {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("canal", m.getCanal());
            dto.put("remitente", m.getRemitente());
            dto.put("contenido", m.getContenido());
            dto.put("timestamp", m.getTimestamp());
            filas.add(dto);
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("canal", c);
        out.put("mensajes", filas);
        out.put("usuariosConectados", sala.usuarios().size());
        return out;
    }

    public List<String> canales() {
        List<String> canales = new ArrayList<>();
        canales.add("global");
        canales.add("clan");
        for (String c : sala.canales()) {
            if (!canales.contains(c)) canales.add(c);
        }
        return canales;
    }

    private void unirSi(String usuario) {
        if (!conocidos.containsKey(usuario)) {
            UsuarioChat u = new UsuarioChat(usuario);
            u.unirse(sala);
            conocidos.put(usuario, u);
        }
    }
}
