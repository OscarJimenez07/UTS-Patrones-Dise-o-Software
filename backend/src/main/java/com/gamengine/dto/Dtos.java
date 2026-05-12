package com.gamengine.dto;

import java.util.List;
import java.util.Map;

public class Dtos {

    public record LoginRequest(String usuario, String contrasena) {}
    public record RegistroRequest(String usuario, String contrasena, String rol) {}

    public record PartidaCrearRequest(String clave, Integer maxJugadores, Integer duracionMinutos) {}
    public record PartidaIniciarRequest(String usuario, String claveModo, String canal) {}

    public record NotifAlertaRequest(String usuario, String mensaje, String canal) {}
    public record NotifResultadoRequest(String usuario, String modo, Boolean victoria, String canal) {}

    public record MejoraRequest(String base, List<String> mejoras) {}

    public record AtaqueRequest(String tipo, String elemento) {}

    public record ClanMensajeRequest(String escuadron, String mensaje) {}
    public record ClanJugadorRequest(String escuadron, String nombre, Integer poder, String rol) {}

    // Funcionalidades adicionales (Memento, Observer, Mediator, State)
    public record HistorialGuardarRequest(String usuario, String modo, Boolean victoria,
                                          Integer puntuacion, Integer duracionSegundos,
                                          List<String> mejorasAplicadas) {}

    public record EventoLogroRequest(String tipo, String usuario, Map<String, Object> datos) {}

    public record ChatMensajeRequest(String usuario, String canal, String mensaje) {}

    public record MisionAvanzarRequest(Integer delta) {}
}
