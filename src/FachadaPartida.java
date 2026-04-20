/**
 * Patron Facade — Fachada del flujo de "Iniciar Partida".
 *
 * Contiene:
 * - Facade (FachadaPartida): expone metodos simples que unifican
 *   varios subsistemas del motor de juegos.
 *
 * Subsistemas ocultados por la fachada:
 *   1. SistemaAutenticacion (Singleton) — valida que el usuario tenga sesion.
 *   2. RegistroConfiguraciones (Prototype) — clona la configuracion del modo.
 *   3. Matchmaking (simulado dentro de la fachada) — busca jugadores.
 *   4. NotificacionJuego (Adapter) — avisa al jugador que la partida empezo.
 *
 * El cliente (Main) ya NO necesita conocer Prototype, Singleton, Adapter ni
 * la logica de matchmaking: llama a un solo metodo y la fachada orquesta todo.
 */
class FachadaPartida {

    private SistemaAutenticacion auth;
    private RegistroConfiguraciones registroConfigs;
    private NotificacionJuego notificador;

    public FachadaPartida() {
        // Subsistema 1: autenticacion (Singleton existente).
        this.auth = SistemaAutenticacion.getInstance();

        // Subsistema 2: registro de prototipos de configuracion (Prototype).
        this.registroConfigs = new RegistroConfiguraciones();
        this.registroConfigs.registrar("Duelo", new ConfiguracionDuelo());
        this.registroConfigs.registrar("Equipos", new ConfiguracionEquipos());
        this.registroConfigs.registrar("BattleRoyale", new ConfiguracionBattleRoyale());

        // Subsistema 3: notificador por defecto (Adapter).
        this.notificador = new AdaptadorDiscord(new ServicioDiscord());
    }

    // Permite cambiar el canal de notificacion (Discord, Correo, ...).
    public void usarNotificador(NotificacionJuego notificador) {
        this.notificador = notificador;
    }

    // ══════════════════════════════════════════════
    // METODO FACHADA: unifica todo el flujo de iniciar
    // una partida rapida en una sola llamada.
    // ══════════════════════════════════════════════
    public void iniciarPartidaRapida(String usuario, String claveModo) {
        System.out.println("\n  ═══ FACHADA: INICIAR PARTIDA RAPIDA ═══");

        // Paso 1: validar sesion del usuario contra el Singleton.
        if (!verificarSesion(usuario)) {
            return;
        }

        // Paso 2: obtener una configuracion clonada del registro (Prototype).
        ConfiguracionPartidaPrototype config = registroConfigs.obtener(claveModo);
        if (config == null) {
            System.out.println("  [Fachada] Modo '" + claveModo + "' no disponible.");
            return;
        }
        System.out.println("  [Fachada] Configuracion obtenida (clon del prototipo):");
        config.mostrarInfo();

        // Paso 3: buscar jugadores (matchmaking simulado).
        buscarJugadores(config.getModo());

        // Paso 4: avisar al jugador via el canal configurado (Adapter).
        notificador.enviarAlerta(usuario, "Tu partida de " + config.getModo() + " ha comenzado!");

        System.out.println("  [Fachada] Flujo completado.");
        System.out.println("  ════════════════════════════════════════");
    }

    // ══════════════════════════════════════════════
    // METODO FACHADA: reporta el resultado de una
    // partida reutilizando el subsistema de notificacion.
    // ══════════════════════════════════════════════
    public void reportarResultado(String usuario, String modo, boolean victoria) {
        System.out.println("\n  ═══ FACHADA: REPORTAR RESULTADO ═══");
        if (!verificarSesion(usuario)) {
            return;
        }
        notificador.enviarResultadoPartida(usuario, modo, victoria);
        System.out.println("  ═══════════════════════════════════");
    }

    // --- Metodos internos (privados) que coordinan los subsistemas ---

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
