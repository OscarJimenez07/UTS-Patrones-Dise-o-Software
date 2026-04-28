package com.gamengine.api;

import com.gamengine.dto.Dtos.LoginRequest;
import com.gamengine.dto.Dtos.RegistroRequest;
import com.gamengine.patrones.SistemaAutenticacion;
import com.gamengine.service.OutputCapture;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        String log = OutputCapture.capture(() -> {
            SistemaAutenticacion auth = SistemaAutenticacion.getInstance();
            boolean ok = auth.login(req.usuario(), req.contrasena());
            out.put("ok", ok);
            if (ok) {
                out.put("usuario", req.usuario());
                out.put("rol", auth.obtenerRol(req.usuario()));
            }
        });
        out.put("log", log);
        out.put("patron", "Singleton");
        return out;
    }

    @PostMapping("/registro")
    public Map<String, Object> registro(@RequestBody RegistroRequest req) {
        Map<String, Object> out = new LinkedHashMap<>();
        String log = OutputCapture.capture(() -> {
            SistemaAutenticacion auth = SistemaAutenticacion.getInstance();
            String rol = req.rol() == null ? "jugador" : req.rol().toLowerCase();
            auth.registrarUsuario(req.usuario(), req.contrasena(), rol);
            System.out.println("[Auth] Usuario '" + req.usuario() + "' registrado como " + rol);
            out.put("ok", true);
            out.put("usuario", req.usuario());
            out.put("rol", rol);
        });
        out.put("log", log);
        out.put("patron", "Singleton");
        return out;
    }

    @GetMapping("/me")
    public Map<String, Object> me(@RequestParam("usuario") String usuario) {
        Map<String, Object> out = new LinkedHashMap<>();
        SistemaAutenticacion auth = SistemaAutenticacion.getInstance();
        String rol = auth.obtenerRol(usuario);
        out.put("usuario", usuario);
        out.put("rol", rol);
        out.put("ok", rol != null);
        return out;
    }
}
