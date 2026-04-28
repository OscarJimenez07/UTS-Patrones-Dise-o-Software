package com.gamengine.patrones;

/**
 * Servicio público que expone Facade para los controllers.
 * Crea una FachadaPartida y permite seleccionar el canal de notificación.
 */
public class FachadaPartidaService {

    private final NotificacionesService notificaciones = new NotificacionesService();

    public boolean iniciar(String usuario, String claveModo, String canal) {
        FachadaPartida fachada = new FachadaPartida();
        fachada.usarNotificador(notificaciones.adaptadorPara(canal));
        fachada.iniciarPartidaRapida(usuario, claveModo);
        return true;
    }

    public boolean reportar(String usuario, String modo, boolean victoria, String canal) {
        FachadaPartida fachada = new FachadaPartida();
        fachada.usarNotificador(notificaciones.adaptadorPara(canal));
        fachada.reportarResultado(usuario, modo, victoria);
        return true;
    }
}
