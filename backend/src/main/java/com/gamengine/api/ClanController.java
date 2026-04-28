package com.gamengine.api;

import com.gamengine.dto.Dtos.ClanJugadorRequest;
import com.gamengine.dto.Dtos.ClanMensajeRequest;
import com.gamengine.patrones.ClanesService;
import com.gamengine.service.OutputCapture;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clan")
public class ClanController {

    private final ClanesService service;

    public ClanController(ClanesServiceBean bean) {
        this.service = bean.service();
    }

    @GetMapping
    public Map<String, Object> arbol() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("clan", service.arbol());
        out.put("escuadrones", service.escuadronesDisponibles());
        out.put("patron", "Composite");
        return out;
    }

    @PostMapping("/mensaje")
    public Map<String, Object> mensaje(@RequestBody ClanMensajeRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        boolean[] ok = {false};
        String log = OutputCapture.capture(() -> {
            if (req.escuadron() == null || req.escuadron().isBlank()) {
                ok[0] = service.enviarMensajeAClan(req.mensaje());
            } else {
                ok[0] = service.enviarMensajeAEscuadron(req.escuadron(), req.mensaje());
            }
        });
        out.put("ok", ok[0]);
        out.put("log", log);
        out.put("patron", "Composite");
        return out;
    }

    @PostMapping("/jugador")
    public Map<String, Object> agregar(@RequestBody ClanJugadorRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        int poder = req.poder() == null ? 1000 : req.poder();
        boolean ok = service.agregarJugador(req.escuadron(), req.nombre(), poder, req.rol());
        out.put("ok", ok);
        if (ok) {
            out.put("clan", service.arbol());
        }
        out.put("patron", "Composite");
        return out;
    }

    @Component
    public static class ClanesServiceBean {
        private final ClanesService service = new ClanesService();
        public ClanesService service() { return service; }
    }
}
