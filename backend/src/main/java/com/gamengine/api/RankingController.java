package com.gamengine.api;

import com.gamengine.patrones.RankingService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService service = new RankingService();

    @GetMapping("")
    public Map<String, Object> top(
            @RequestParam(defaultValue = "puntos") String criterio,
            @RequestParam(defaultValue = "20") int limite) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> holder[0] = service.top(criterio, limite));
        out.putAll(holder[0]);
        out.put("log", log);
        out.put("patron", "Strategy");
        return out;
    }

    @GetMapping("/criterios")
    public Map<String, Object> criterios() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("criterios", service.criterios());
        out.put("patron", "Strategy");
        return out;
    }

    @GetMapping("/jugador/{usuario}")
    public Map<String, Object> posicion(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("usuario", usuario);
        out.put("posicionPorCriterio", service.posicionDe(usuario));
        out.put("patron", "Strategy");
        return out;
    }
}
