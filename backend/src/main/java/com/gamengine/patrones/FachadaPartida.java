package com.gamengine.patrones;

/**
 * Patron Facade — Fachada del flujo de "Iniciar Partida".
 *
 * Subsistemas ocultados:
 *   1. SistemaAutenticacion (Singleton)
 *   2. RegistroConfiguraciones (Prototype)
 *   3. Matchmaking (simulado)
 *   4. NotificacionJuego (Adapter)
 */
public class FachadaPartida {

    private SistemaAutenticacion auth;
    private RegistroConfiguraciones registroConfigs;
    private NotificacionJuego notificador;

    public FachadaPartida() {
        this.auth = SistemaAutenticacion.getInstance();

        this.registroConfigs = new RegistroConfiguraciones();
        this.registroConfigs.registrar("Duelo", new ConfiguracionDuelo());
        this.registroConfigs.registrar("Equipos", new ConfiguracionEquipos());
        this.registroConfigs.registrar("BattleRoyale", new ConfiguracionBattleRoyale());

        this.notificador = new AdaptadorDiscord(new ServicioDiscord());
    }

    public void usarNotificador(NotificacionJuego notificador) {
        this.notificador = notificador;
    }

    public void iniciarPartidaRapida(String usuario, String claveModo) {
        System.out.println("\n  ═══ FACHADA: INICIAR PARTIDA RAPIDA ═══");

        if (!verificarSesion(usuario)) {
            return;
        }

        ConfiguracionPartidaPrototype config = registroConfigs.obtener(claveModo);
        if (config == null) {
            System.out.println("  [Fachada] Modo '" + claveModo + "' no disponible.");
            return;
        }
        System.out.println("  [Fachada] Configuracion obtenida (clon del prototipo):");
        config.mostrarInfo();

        buscarJugadores(config.getModo());

        notificador.enviarAlerta(usuario, "Tu partida de " + config.getModo() + " ha comenzado!");

        System.out.println("  [Fachada] Flujo completado.");
        System.out.println("  ════════════════════════════════════════");
    }

    public void reportarResultado(String usuario, String modo, boolean victoria) {
        System.out.println("\n  ═══ FACHADA: REPORTAR RESULTADO ═══");
        if (!verificarSesion(usuario)) {
            return;
        }
        notificador.enviarResultadoPartida(usuario, modo, victoria);
        System.out.println("  ═══════════════════════════════════");
    }

    private boolean verificarSesion(String usuario) {
        if (usuario == null || auth.obtenerRol(usuario) == null) {
            System.out.println("  [Fachada] Usuario sin sesion activa. Operacion cancelada.");
            return false;
        }
        System.out.println("  [Fachada] Sesion verificada para " + usuario
                + " (" + auth.obtenerRol(usuario) + ")");
        return true;
    }

    private void buscarJugadores(String modo) {
        System.out.println("  [Fachada] Buscando jugadores para " + modo + "...");
        System.out.println("  [Fachada] Emparejamiento exitoso. Lobby creado.");
    }
}
