package com.gamengine.dto;

import java.util.List;

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
}
