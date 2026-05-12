package com.gamengine.api;

import com.gamengine.dto.Dtos.HistorialGuardarRequest;
import com.gamengine.patrones.HistorialService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private final HistorialService service = new HistorialService();

    @GetMapping("/{usuario}")
    public Map<String, Object> listar(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        List<Map<String, Object>> snapshots = service.listar(usuario);
        out.put("usuario", usuario);
        out.put("total", snapshots.size());
        out.put("snapshots", snapshots);
        out.put("patron", "Memento");
        return out;
    }

    @GetMapping("/{usuario}/{id}")
    public Map<String, Object> detalle(@PathVariable String usuario, @PathVariable String id) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> holder[0] = service.detalle(usuario, id));
        if (holder[0] == null) {
            out.put("ok", false);
            out.put("error", "Snapshot no encontrado");
        } else {
            out.put("ok", true);
            out.put("snapshot", holder[0]);
        }
        out.put("log", log);
        out.put("patron", "Memento");
        return out;
    }

    @PostMapping("/guardar")
    public Map<String, Object> guardar(@RequestBody HistorialGuardarRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.guardar(
                    req.usuario(), req.modo(),
                    Boolean.TRUE.equals(req.victoria()),
                    req.puntuacion() == null ? 0 : req.puntuacion(),
                    req.duracionSegundos() == null ? 0 : req.duracionSegundos(),
                    req.mejorasAplicadas());
        });
        out.put("ok", true);
        out.put("snapshot", holder[0]);
        out.put("log", log);
        out.put("patron", "Memento");
        return out;
    }

    @DeleteMapping("/{usuario}")
    public Map<String, Object> limpiar(@PathVariable String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        int[] eliminados = new int[1];
        String log = OutputCapture.capture(() -> eliminados[0] = service.limpiar(usuario));
        out.put("ok", true);
        out.put("eliminados", eliminados[0]);
        out.put("log", log);
        out.put("patron", "Memento");
        return out;
    }
}
