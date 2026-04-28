package com.gamengine.api;

import com.gamengine.dto.Dtos.PartidaCrearRequest;
import com.gamengine.dto.Dtos.PartidaIniciarRequest;
import com.gamengine.patrones.FachadaPartidaService;
import com.gamengine.patrones.PartidasService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partida")
public class PartidaController {

    private final PartidasService partidas = new PartidasService();
    private final FachadaPartidaService fachada = new FachadaPartidaService();

    @GetMapping("/modos")
    public List<Map<String, Object>> modos() {
        return partidas.listarModos();
    }

    @PostMapping("/crear")
    public Map<String, Object> crear(@RequestBody PartidaCrearRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = partidas.crear(req.clave(), req.maxJugadores(), req.duracionMinutos());
        });
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Modo no encontrado: " + req.clave());
        } else {
            out.put("ok", true);
            out.put("config", holder[0]);
        }
        out.put("log", log);
        out.put("patron", "Prototype");
        return out;
    }

    @PostMapping("/iniciar")
    public Map<String, Object> iniciar(@RequestBody PartidaIniciarRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        String log = OutputCapture.capture(() -> {
            fachada.iniciar(req.usuario(), req.claveModo(), req.canal());
        });
        out.put("ok", true);
        out.put("log", log);
        out.put("patron", "Facade (orquesta Singleton + Prototype + Adapter)");
        return out;
    }
}
