package com.gamengine.api;

import com.gamengine.dto.Dtos.MisionAvanzarRequest;
import com.gamengine.patrones.MisionesService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/misiones")
public class MisionesController {

    private final MisionesService service = new MisionesService();

    @GetMapping("/{usuario}")
    public Map<String, Object> obtener(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.putAll(service.obtener(usuario));
        out.put("patron", "State");
        return out;
    }

    @PostMapping("/{usuario}/refrescar")
    public Map<String, Object> refrescar(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> holder[0] = service.refrescar(usuario));
        out.put("ok", true);
        out.putAll(holder[0]);
        out.put("log", log);
        out.put("patron", "State");
        return out;
    }

    @PostMapping("/{usuario}/{id}/progreso")
    public Map<String, Object> avanzar(@PathVariable String usuario,
                                       @PathVariable String id,
                                       @RequestBody(required = false) MisionAvanzarRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        int delta = (req == null || req.delta() == null) ? 1 : req.delta();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> holder[0] = service.avanzar(usuario, id, delta));
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Mision no encontrada");
        } else {
            out.put("ok", true);
            out.putAll(holder[0]);
        }
        out.put("log", log);
        out.put("patron", "State");
        return out;
    }

    @PostMapping("/{usuario}/{id}/reclamar")
    public Map<String, Object> reclamar(@PathVariable String usuario, @PathVariable String id) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> holder[0] = service.reclamar(usuario, id));
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Mision no encontrada");
        } else {
            out.put("ok", true);
            out.putAll(holder[0]);
        }
        out.put("log", log);
        out.put("patron", "State");
        return out;
    }
}
