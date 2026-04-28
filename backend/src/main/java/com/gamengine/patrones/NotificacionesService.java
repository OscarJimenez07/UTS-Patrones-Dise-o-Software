package com.gamengine.patrones;

/**
 * Servicio público que expone Adapter para los controllers.
 */
public class NotificacionesService {

    public void enviarAlerta(String canal, String usuario, String mensaje) {
        NotificacionJuego notif = elegir(canal);
        notif.enviarAlerta(usuario, mensaje);
    }

    public void enviarResultado(String canal, String usuario, String modo, boolean victoria) {
        NotificacionJuego notif = elegir(canal);
        notif.enviarResultadoPartida(usuario, modo, victoria);
    }

    private NotificacionJuego elegir(String canal) {
        if ("correo".equalsIgnoreCase(canal) || "email".equalsIgnoreCase(canal)) {
            return new AdaptadorCorreo(new ServicioCorreo());
        }
        return new AdaptadorDiscord(new ServicioDiscord());
    }

    /** Para que la fachada también pueda elegir el adapter. */
    public NotificacionJuego adaptadorPara(String canal) {
        return elegir(canal);
    }
}
