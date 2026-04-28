package com.gamengine.api;

import com.gamengine.patrones.TiendaService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tienda")
public class TiendaController {

    private final TiendaService service = new TiendaService();

    @GetMapping("/{nivel}")
    public Map<String, Object> oferta(@PathVariable("nivel") String nivel) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.oferta(nivel);
        });
        if (holder[0] != null) {
            out.putAll(holder[0]);
        }
        out.put("log", log);
        out.put("patron", "Abstract Factory");
        return out;
    }
}
