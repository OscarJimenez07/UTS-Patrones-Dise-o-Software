package com.gamengine.api;

import com.gamengine.dto.Dtos.AtaqueRequest;
import com.gamengine.patrones.AtaquesService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ataques")
public class AtaquesController {

    private final AtaquesService service = new AtaquesService();

    @PostMapping("/ejecutar")
    public Map<String, Object> ejecutar(@RequestBody AtaqueRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.ejecutar(req.tipo(), req.elemento());
        });
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Tipo o elemento no válido");
        } else {
            out.put("ok", true);
            out.putAll(holder[0]);
        }
        out.put("log", log);
        out.put("patron", "Bridge");
        return out;
    }

    @GetMapping("/combinaciones")
    public Map<String, Object> combinaciones() {
        Map<String, Object> out = new LinkedHashMap<>();
        List<Map<String, Object>> combos = service.combinaciones();
        out.put("total", combos.size());
        out.put("combinaciones", combos);
        out.put("patron", "Bridge");
        return out;
    }
}
