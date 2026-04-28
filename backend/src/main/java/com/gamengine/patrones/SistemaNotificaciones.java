package com.gamengine.patrones;

/**
 * Patron Adapter — Sistema de Notificaciones.
 */

// ══════════════════════════════════════════════
// TARGET: interfaz que el motor de juegos utiliza.
// ══════════════════════════════════════════════
interface NotificacionJuego {
    void enviarAlerta(String jugador, String mensaje);
    void enviarResultadoPartida(String jugador, String modo, boolean victoria);
}

// ══════════════════════════════════════════════
// ADAPTEE 1: servicio externo de Discord.
// ══════════════════════════════════════════════
class ServicioDiscord {
    public void publicarMensaje(String canal, String contenido, boolean esMencion) {
        String prefijo = esMencion ? "@usuario " : "";
        System.out.println("  [Discord] #" + canal + ": " + prefijo + contenido);
    }
}

// ══════════════════════════════════════════════
// ADAPTEE 2: servicio externo de Correo.
// ══════════════════════════════════════════════
class ServicioCorreo {
    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        System.out.println("  [Email] Para: " + destinatario + "@gamezone.com");
        System.out.println("          Asunto: " + asunto);
        System.out.println("          Cuerpo: " + cuerpo);
    }
}

// ══════════════════════════════════════════════
// ADAPTER 1: adapta ServicioDiscord a NotificacionJuego.
// ══════════════════════════════════════════════
class AdaptadorDiscord implements NotificacionJuego {
    private ServicioDiscord discord;

    public AdaptadorDiscord(ServicioDiscord discord) {
        this.discord = discord;
    }

    @Override
    public void enviarAlerta(String jugador, String mensaje) {
        discord.publicarMensaje("alertas-juego", jugador + ": " + mensaje, true);
    }

    @Override
    public void enviarResultadoPartida(String jugador, String modo, boolean victoria) {
        String resultado = victoria ? "VICTORIA" : "DERROTA";
        String contenido = jugador + " ha terminado una partida de " + modo + " con " + resultado + "!";
        discord.publicarMensaje("resultados", contenido, false);
    }
}

// ══════════════════════════════════════════════
// ADAPTER 2: adapta ServicioCorreo a NotificacionJuego.
// ══════════════════════════════════════════════
class AdaptadorCorreo implements NotificacionJuego {
    private ServicioCorreo correo;

    public AdaptadorCorreo(ServicioCorreo correo) {
        this.correo = correo;
    }

    @Override
    public void enviarAlerta(String jugador, String mensaje) {
        correo.enviarEmail(jugador, "Alerta del juego", mensaje);
    }

    @Override
    public void enviarResultadoPartida(String jugador, String modo, boolean victoria) {
        String resultado = victoria ? "Victoria" : "Derrota";
        String asunto = "Resultado de partida: " + resultado;
        String cuerpo = "Hola " + jugador + ", tu partida de " + modo + " ha terminado. Resultado: " + resultado + ".";
        correo.enviarEmail(jugador, asunto, cuerpo);
    }
}
