package com.gamengine.api;

import com.gamengine.dto.Dtos.ChatMensajeRequest;
import com.gamengine.patrones.ChatService;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService service = new ChatService();

    @GetMapping("/canales")
    public Map<String, Object> canales() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("canales", service.canales());
        out.put("patron", "Mediator");
        return out;
    }

    @GetMapping("/{canal}")
    public Map<String, Object> historial(@PathVariable String canal) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.putAll(service.historial(canal));
        out.put("patron", "Mediator");
        return out;
    }

    @PostMapping("/enviar")
    public Map<String, Object> enviar(@RequestBody ChatMensajeRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        Map<String, Object>[] holder = new Map[1];
        String log = OutputCapture.capture(() -> {
            holder[0] = service.enviar(req.usuario(), req.canal(), req.mensaje());
        });
        out.put("ok", true);
        out.putAll(holder[0]);
        out.put("log", log);
        out.put("patron", "Mediator");
        return out;
    }
}
