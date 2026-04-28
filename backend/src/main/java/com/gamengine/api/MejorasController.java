package com.gamengine.api;

import com.gamengine.dto.Dtos.MejoraRequest;
import com.gamengine.patrones.MejorasService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mejoras")
public class MejorasController {

    private final MejorasService service = new MejorasService();

    @PostMapping("/aplicar")
    public Map<String, Object> aplicar(@RequestBody MejoraRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.aplicar(req.base(), req.mejoras());
        });
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Habilidad base no válida: " + req.base());
        } else {
            out.put("ok", true);
            out.putAll(holder[0]);
        }
        out.put("log", log);
        out.put("patron", "Decorator");
        return out;
    }
}
