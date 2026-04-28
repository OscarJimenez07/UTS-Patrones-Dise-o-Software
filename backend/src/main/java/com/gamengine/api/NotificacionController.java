package com.gamengine.api;

import com.gamengine.dto.Dtos.NotifAlertaRequest;
import com.gamengine.dto.Dtos.NotifResultadoRequest;
import com.gamengine.patrones.NotificacionesService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionesService service = new NotificacionesService();

    @PostMapping("/enviar")
    public Map<String, Object> enviar(@RequestBody NotifAlertaRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        String log = OutputCapture.capture(() -> {
            service.enviarAlerta(req.canal(), req.usuario(), req.mensaje());
        });
        out.put("ok", true);
        out.put("canal", req.canal());
        out.put("log", log);
        out.put("patron", "Adapter");
        return out;
    }

    @PostMapping("/resultado")
    public Map<String, Object> resultado(@RequestBody NotifResultadoRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        boolean victoria = req.victoria() != null && req.victoria();
        String log = OutputCapture.capture(() -> {
            service.enviarResultado(req.canal(), req.usuario(), req.modo(), victoria);
        });
        out.put("ok", true);
        out.put("canal", req.canal());
        out.put("log", log);
        out.put("patron", "Adapter");
        return out;
    }
}
