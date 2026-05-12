package com.gamengine.api;

import com.gamengine.dto.Dtos.EventoLogroRequest;
import com.gamengine.patrones.LogrosService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logros")
public class LogrosController {

    private final LogrosService service = new LogrosService();

    @GetMapping("/{usuario}")
    public Map<String, Object> catalogo(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        List<Map<String, Object>> logros = service.catalogo(usuario);
        out.put("usuario", usuario);
        out.put("logros", logros);
        out.put("patron", "Observer");
        return out;
    }

    @PostMapping("/evento")
    public Map<String, Object> publicar(@RequestBody EventoLogroRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.publicar(req.tipo(), req.usuario(), req.datos());
        });
        out.put("ok", true);
        out.putAll(holder[0]);
        out.put("log", log);
        out.put("patron", "Observer");
        return out;
    }
}
