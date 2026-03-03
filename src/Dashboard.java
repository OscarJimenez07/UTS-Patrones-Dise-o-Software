/**
 * Clase abstracta que representa un Dashboard genérico.
 *
 * Cada tipo de usuario (Jugador, Admin, Moderador) tiene su propio
 * dashboard con opciones diferentes. Las subclases implementan mostrar().
 */
public abstract class Dashboard {

    protected String nombreUsuario;
    protected String rol;

    protected Dashboard(String nombreUsuario, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }

    /**
     * Muestra el contenido del dashboard.
     * Cada tipo de dashboard muestra opciones diferentes.
     */
    public abstract void mostrar();

    public String getNombreUsuario() { return nombreUsuario; }
    public String getRol() { return rol; }
}

/**
 * Dashboard para usuarios con rol Jugador.
 * Muestra opciones típicas de un jugador: jugar, ver estadísticas, tienda.
 */
class DashboardJugador extends Dashboard {

    public DashboardJugador(String nombreUsuario) {
        super(nombreUsuario, "Jugador");
    }

    @Override
    public void mostrar() {
        System.out.println("\n========================================");
        System.out.println("   DASHBOARD DEL JUGADOR");
        System.out.println("   Bienvenido, " + nombreUsuario + "!");
        System.out.println("========================================");
        System.out.println("  1. Buscar partida");
        System.out.println("  2. Ver mis estadisticas");
        System.out.println("  3. Tienda de objetos");
        System.out.println("  4. Ver ranking global");
        System.out.println("  5. Cerrar sesion");
        System.out.println("========================================");
    }
}

/**
 * Dashboard para usuarios con rol Administrador.
 * Muestra opciones de gestión del servidor y usuarios.
 */
class DashboardAdmin extends Dashboard {

    public DashboardAdmin(String nombreUsuario) {
        super(nombreUsuario, "Administrador");
    }

    @Override
    public void mostrar() {
        System.out.println("\n========================================");
        System.out.println("   DASHBOARD DEL ADMINISTRADOR");
        System.out.println("   Bienvenido, " + nombreUsuario + "!");
        System.out.println("========================================");
        System.out.println("  1. Gestionar usuarios");
        System.out.println("  2. Configurar servidor");
        System.out.println("  3. Ver reportes del sistema");
        System.out.println("  4. Gestionar partidas activas");
        System.out.println("  5. Cerrar sesion");
        System.out.println("========================================");
    }
}

/**
 * Dashboard para usuarios con rol Moderador.
 * Muestra opciones de moderación: reportes, sanciones, chat.
 */
class DashboardModerador extends Dashboard {

    public DashboardModerador(String nombreUsuario) {
        super(nombreUsuario, "Moderador");
    }

    @Override
    public void mostrar() {
        System.out.println("\n========================================");
        System.out.println("   DASHBOARD DEL MODERADOR");
        System.out.println("   Bienvenido, " + nombreUsuario + "!");
        System.out.println("========================================");
        System.out.println("  1. Ver reportes de jugadores");
        System.out.println("  2. Sancionar jugador");
        System.out.println("  3. Revisar chat global");
        System.out.println("  4. Ver historial de sanciones");
        System.out.println("  5. Cerrar sesion");
        System.out.println("========================================");
    }
}

/**
 * PATRON FACTORY METHOD
 *
 * Clase abstracta que define el "método fábrica" para crear dashboards.
 *
 * Cada subclase (FactoryDashboardJugador, FactoryDashboardAdmin, etc.)
 * decide QUÉ tipo de dashboard crear, sin que el código cliente lo sepa.
 *
 * Así, si mañana agregamos un nuevo rol (ej: "Streamer"), solo creamos
 * una nueva factory sin tocar el código existente.
 */
abstract class DashboardFactory {

    /**
     * FACTORY METHOD: las subclases deciden qué tipo de Dashboard crear.
     * Este es el corazón del patrón.
     */
    public abstract Dashboard crearDashboard(String nombreUsuario);

    /**
     * Método que usa el factory method internamente.
     * Crea el dashboard y lo muestra al usuario.
     *
     * Este código NO sabe qué tipo concreto de dashboard se crea.
     * Solo trabaja con la clase abstracta "Dashboard".
     */
    public Dashboard prepararDashboard(String nombreUsuario) {
        Dashboard dashboard = crearDashboard(nombreUsuario);
        System.out.println("\n[Sistema] Cargando dashboard para rol: " + dashboard.getRol());
        dashboard.mostrar();
        return dashboard;
    }
}

/**
 * Fábrica concreta que crea dashboards de Jugador.
 */
class FactoryDashboardJugador extends DashboardFactory {

    @Override
    public Dashboard crearDashboard(String nombreUsuario) {
        return new DashboardJugador(nombreUsuario);
    }
}

/**
 * Fábrica concreta que crea dashboards de Administrador.
 */
class FactoryDashboardAdmin extends DashboardFactory {

    @Override
    public Dashboard crearDashboard(String nombreUsuario) {
        return new DashboardAdmin(nombreUsuario);
    }
}

/**
 * Fábrica concreta que crea dashboards de Moderador.
 */
class FactoryDashboardModerador extends DashboardFactory {

    @Override
    public Dashboard crearDashboard(String nombreUsuario) {
        return new DashboardModerador(nombreUsuario);
    }
}
