import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static SistemaAutenticacion auth = SistemaAutenticacion.getInstance();
    private static String usuarioActual = null;

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n============================================");
            System.out.println("   MOTOR DE JUEGOS MULTIJUGADOR");
            System.out.println("============================================");
            System.out.println("  1. Iniciar sesion");
            System.out.println("  2. Registrarse");
            System.out.println("  0. Salir");
            System.out.println("============================================");
            System.out.print("  Opcion: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    iniciarSesion();
                    break;
                case "2":
                    registrarse();
                    break;
                case "0":
                    salir = true;
                    System.out.println("\n  Hasta pronto!");
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }
        }

        scanner.close();
    }

    private static void iniciarSesion() {
        System.out.print("\n  Usuario: ");
        String usuario = scanner.nextLine().trim();
        System.out.print("  Contrasena: ");
        String contrasena = scanner.nextLine().trim();

        if (auth.login(usuario, contrasena)) {
            usuarioActual = usuario;
            String rol = auth.obtenerRol(usuario);

            switch (rol.toLowerCase()) {
                case "jugador":
                    menuJugador();
                    break;
                case "administrador":
                    menuAdmin();
                    break;
                case "moderador":
                    menuModerador();
                    break;
            }
        }
    }

    private static void registrarse() {
        System.out.print("\n  Nuevo usuario: ");
        String usuario = scanner.nextLine().trim();
        System.out.print("  Contrasena: ");
        String contrasena = scanner.nextLine().trim();
        System.out.println("\n  Seleccione su rol:");
        System.out.println("  1. Jugador");
        System.out.println("  2. Moderador");
        System.out.println("  3. Administrador");
        System.out.print("  Opcion: ");
        String opRol = scanner.nextLine().trim();

        String rol;
        switch (opRol) {
            case "1": rol = "jugador"; break;
            case "2": rol = "moderador"; break;
            case "3": rol = "administrador"; break;
            default:
                System.out.println("  Rol no valido. Registro cancelado.");
                return;
        }

        auth.registrarUsuario(usuario, contrasena, rol);
        System.out.println("  Usuario '" + usuario + "' registrado como " + rol + "!");
    }

    // ========================================================
    //  MENU JUGADOR (usa Factory Method para el dashboard)
    // ========================================================
    private static void menuJugador() {
        Dashboard dashboard = auth.abrirDashboard(usuarioActual);
        boolean enSesion = true;

        while (enSesion) {
            System.out.print("\n  Opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1":
                    buscarPartida();
                    break;
                case "2":
                    verEstadisticas();
                    break;
                case "3":
                    menuTienda();
                    break;
                case "4":
                    verRanking();
                    break;
                case "5":
                    menuCrearPartida();
                    break;
                case "6":
                    enSesion = false;
                    usuarioActual = null;
                    System.out.println("\n  Sesion cerrada.");
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }

            if (enSesion) {
                dashboard.mostrar();
            }
        }
    }

    // ========================================================
    //  CREAR PARTIDA (usa patron Prototype)
    // ========================================================
    private static void menuCrearPartida() {
        RegistroConfiguraciones registro = new RegistroConfiguraciones();
        registro.registrar("Duelo", new ConfiguracionDuelo());
        registro.registrar("Equipos", new ConfiguracionEquipos());
        registro.registrar("BattleRoyale", new ConfiguracionBattleRoyale());

        boolean enMenu = true;

        while (enMenu) {
            System.out.println("\n  ╔══════════════════════════════════════╗");
            System.out.println("  ║      CREAR PARTIDA (Prototype)       ║");
            System.out.println("  ╠══════════════════════════════════════╣");
            System.out.println("  ║  1. Ver modos disponibles            ║");
            System.out.println("  ║  2. Crear partida rapida             ║");
            System.out.println("  ║  3. Crear partida personalizada      ║");
            System.out.println("  ║  0. Volver al menu                   ║");
            System.out.println("  ╚══════════════════════════════════════╝");
            System.out.print("  Opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1":
                    registro.listarConfiguraciones();
                    break;
                case "2":
                    System.out.println("\n  Selecciona el modo:");
                    System.out.println("  1. Duelo (1 vs 1)");
                    System.out.println("  2. Equipos (3v3)");
                    System.out.println("  3. Battle Royale");
                    System.out.print("  Opcion: ");
                    String modoRapido = scanner.nextLine().trim();

                    String claveRapida;
                    switch (modoRapido) {
                        case "1": claveRapida = "Duelo"; break;
                        case "2": claveRapida = "Equipos"; break;
                        case "3": claveRapida = "BattleRoyale"; break;
                        default:
                            System.out.println("  Modo no valido.");
                            continue;
                    }

                    ConfiguracionPartidaPrototype partidaRapida = registro.obtener(claveRapida);
                    System.out.println("\n  Partida creada (clon del prototipo):");
                    partidaRapida.mostrarInfo();
                    System.out.println("  Buscando jugadores...");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "3":
                    System.out.println("\n  Selecciona el modo base:");
                    System.out.println("  1. Duelo (1 vs 1)");
                    System.out.println("  2. Equipos (3v3)");
                    System.out.println("  3. Battle Royale");
                    System.out.print("  Opcion: ");
                    String modoBase = scanner.nextLine().trim();

                    String claveBase;
                    switch (modoBase) {
                        case "1": claveBase = "Duelo"; break;
                        case "2": claveBase = "Equipos"; break;
                        case "3": claveBase = "BattleRoyale"; break;
                        default:
                            System.out.println("  Modo no valido.");
                            continue;
                    }

                    ConfiguracionPartidaPrototype partidaCustom = registro.obtener(claveBase);

                    System.out.print("  Max jugadores (actual: entre para mantener): ");
                    String inputJugadores = scanner.nextLine().trim();
                    if (!inputJugadores.isEmpty()) {
                        try {
                            partidaCustom.setMaxJugadores(Integer.parseInt(inputJugadores));
                        } catch (NumberFormatException e) {
                            System.out.println("  Valor no valido, se mantiene el original.");
                        }
                    }

                    System.out.print("  Duracion en minutos (actual: entre para mantener): ");
                    String inputDuracion = scanner.nextLine().trim();
                    if (!inputDuracion.isEmpty()) {
                        try {
                            partidaCustom.setDuracionMinutos(Integer.parseInt(inputDuracion));
                        } catch (NumberFormatException e) {
                            System.out.println("  Valor no valido, se mantiene el original.");
                        }
                    }

                    System.out.println("\n  Partida personalizada creada (clon modificado):");
                    partidaCustom.mostrarInfo();
                    System.out.println("  Buscando jugadores...");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "0":
                    enMenu = false;
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }
        }
    }

    // ========================================================
    //  MENU ADMIN (usa Factory Method para el dashboard)
    // ========================================================
    private static void menuAdmin() {
        Dashboard dashboard = auth.abrirDashboard(usuarioActual);
        boolean enSesion = true;

        while (enSesion) {
            System.out.print("\n  Opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1":
                    System.out.println("\n  [Admin] Panel de gestion de usuarios");
                    System.out.println("  Usuarios registrados en el sistema.");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "2":
                    System.out.println("\n  [Admin] Configuracion del servidor");
                    System.out.println("  Servidores activos: 3 | Jugadores conectados: 127");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "3":
                    System.out.println("\n  [Admin] Reportes del sistema");
                    System.out.println("  Partidas hoy: 45 | Usuarios nuevos: 12 | Incidentes: 0");
                    break;
                case "4":
                    System.out.println("\n  [Admin] Partidas activas");
                    System.out.println("  No hay partidas en curso actualmente.");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "5":
                    enSesion = false;
                    usuarioActual = null;
                    System.out.println("\n  Sesion cerrada.");
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }

            if (enSesion) {
                dashboard.mostrar();
            }
        }
    }

    // ========================================================
    //  MENU MODERADOR (usa Factory Method para el dashboard)
    // ========================================================
    private static void menuModerador() {
        Dashboard dashboard = auth.abrirDashboard(usuarioActual);
        boolean enSesion = true;

        while (enSesion) {
            System.out.print("\n  Opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1":
                    System.out.println("\n  [Mod] Reportes de jugadores");
                    System.out.println("  No hay reportes pendientes.");
                    break;
                case "2":
                    System.out.print("\n  [Mod] Nombre del jugador a sancionar: ");
                    String jugador = scanner.nextLine().trim();
                    System.out.println("  Sancion aplicada a '" + jugador + "' (advertencia).");
                    break;
                case "3":
                    System.out.println("\n  [Mod] Chat global:");
                    System.out.println("  oscar: Alguien para una partida?");
                    System.out.println("  guest42: Hola a todos!");
                    System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
                    break;
                case "4":
                    System.out.println("\n  [Mod] Historial de sanciones");
                    System.out.println("  No hay sanciones registradas.");
                    break;
                case "5":
                    enSesion = false;
                    usuarioActual = null;
                    System.out.println("\n  Sesion cerrada.");
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }

            if (enSesion) {
                dashboard.mostrar();
            }
        }
    }

    // ========================================================
    //  CREAR PERSONAJE (usa patron Builder)
    // ========================================================
    private static void crearPersonaje() {
        System.out.println("\n  ═══ CREACION DE PERSONAJE ═══");
        System.out.print("  Nombre de tu personaje: ");
        String nombre = scanner.nextLine().trim();
        System.out.println("\n  Elige la clase:");
        System.out.println("  1. Guerrero (alta vida y defensa)");
        System.out.println("  2. Mago (alto ataque magico)");
        System.out.print("  Opcion: ");
        String clase = scanner.nextLine().trim();

        PersonajeBuilder builder;

        switch (clase) {
            case "1":
                builder = new PersonajeGuerreroBuilder();
                break;
            case "2":
                builder = new PersonajeMagoBuilder();
                break;
            default:
                System.out.println("  Clase no valida. Creacion cancelada.");
                return;
        }

        DirectorPersonaje director = new DirectorPersonaje(builder);
        Personaje personaje = director.construirPersonaje(nombre);

        System.out.println("\n  Personaje creado exitosamente!\n");
        personaje.mostrarInfo();
    }

    // ========================================================
    //  TIENDA (usa patron Abstract Factory)
    // ========================================================
    private static void menuTienda() {
        boolean enTienda = true;

        while (enTienda) {
            System.out.println("\n  ╔══════════════════════════════════════╗");
            System.out.println("  ║         TIENDA DE OBJETOS            ║");
            System.out.println("  ╠══════════════════════════════════════╣");
            System.out.println("  ║  1. Ver items Estandar (monedas)     ║");
            System.out.println("  ║  2. Ver items Premium  (diamantes)   ║");
            System.out.println("  ║  0. Volver al menu                   ║");
            System.out.println("  ╚══════════════════════════════════════╝");
            System.out.print("  Opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1":
                    Tienda tiendaEstandar = new Tienda(new TiendaEstandarFactory(), "Estándar");
                    tiendaEstandar.mostrarOfertaDelDia();
                    break;
                case "2":
                    Tienda tiendaPremium = new Tienda(new TiendaPremiumFactory(), "Premium");
                    tiendaPremium.mostrarOfertaDelDia();
                    break;
                case "0":
                    enTienda = false;
                    break;
                default:
                    System.out.println("  Opcion no valida.");
            }
        }
    }

    // ========================================================
    //  BUSCAR PARTIDA (simulado, se ampliara con Strategy)
    // ========================================================
    private static void buscarPartida() {
        System.out.println("\n  ═══ BUSCAR PARTIDA ═══");
        System.out.println("  Selecciona modo de juego:");
        System.out.println("  1. 1 vs 1");
        System.out.println("  2. Equipos (3v3)");
        System.out.println("  3. Battle Royale");
        System.out.print("  Opcion: ");
        String modo = scanner.nextLine().trim();

        String nombreModo;
        switch (modo) {
            case "1": nombreModo = "1 vs 1"; break;
            case "2": nombreModo = "Equipos 3v3"; break;
            case "3": nombreModo = "Battle Royale"; break;
            default:
                System.out.println("  Modo no valido.");
                return;
        }

        System.out.println("\n  Buscando partida de " + nombreModo + "...");
        System.out.println("  Jugadores encontrados! Entrando a la partida...");
        System.out.println("  (Modulo en desarrollo - se ampliara con nuevos patrones)");
    }

    // ========================================================
    //  ESTADISTICAS Y RANKING (simulado)
    // ========================================================
    private static void verEstadisticas() {
        System.out.println("\n  ═══ ESTADISTICAS DE " + usuarioActual.toUpperCase() + " ═══");
        System.out.println("  Partidas jugadas:  24");
        System.out.println("  Victorias:         15");
        System.out.println("  Derrotas:           9");
        System.out.println("  Win rate:          62%");
        System.out.println("  Puntuacion Elo:  1250");

        System.out.println("\n  Deseas crear un nuevo personaje? (s/n): ");
        String resp = scanner.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            crearPersonaje();
        }
    }

    private static void verRanking() {
        System.out.println("\n  ═══ RANKING GLOBAL ═══");
        System.out.println("  #1  ProGamer99     - 2150 Elo");
        System.out.println("  #2  ShadowKnight   - 1980 Elo");
        System.out.println("  #3  DragonSlayer    - 1875 Elo");
        System.out.println("  #4  " + usuarioActual + "          - 1250 Elo");
        System.out.println("  ...");
    }
}
