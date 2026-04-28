package com.gamengine.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Reenvía cualquier ruta no /api/** al index.html para que react-router en modo
 * BrowserRouter funcione tras un refresh de página.
 */
@Controller
public class WebController {

    @GetMapping(value = {
            "/",
            "/login",
            "/jugador",
            "/admin",
            "/mod",
            "/tienda",
            "/partida",
            "/notificaciones",
            "/mejoras",
            "/ataques",
            "/clan"
    })
    public String spa() {
        return "forward:/index.html";
    }
}
