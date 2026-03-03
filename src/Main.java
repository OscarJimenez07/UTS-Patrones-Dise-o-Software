/**
 * Clase principal de demostración.
 *
 * Muestra cómo funcionan los dos patrones de diseño:
 *
 * 1. SINGLETON (SistemaAutenticacion): Solo existe UNA instancia
 *    del sistema de login. Aunque pidamos getInstance() varias veces,
 *    siempre es el mismo objeto.
 *
 * 2. FACTORY METHOD (DashboardFactory): Después del login, se crea
 *    el dashboard correcto según el rol del usuario, sin que este
 *    código necesite saber los detalles de cada dashboard.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("============================================");
        System.out.println("   MOTOR DE JUEGOS MULTIJUGADOR");
        System.out.println("   Sistema de Login y Dashboard");
        System.out.println("============================================");

        // =============================================
        // DEMOSTRACIÓN 1: PATRÓN SINGLETON
        // =============================================
        System.out.println("\n--- PATRON SINGLETON ---");
        System.out.println("Obteniendo dos referencias al sistema de autenticacion...\n");

        SistemaAutenticacion auth1 = SistemaAutenticacion.getInstance();
        SistemaAutenticacion auth2 = SistemaAutenticacion.getInstance();

        // Verificamos que ambas referencias son el MISMO objeto
        System.out.println("\nauth1 == auth2? " + (auth1 == auth2));
        System.out.println("(Si es true, el Singleton funciona correctamente)");

        // =============================================
        // DEMOSTRACIÓN 2: LOGIN + FACTORY METHOD
        // =============================================
        System.out.println("\n\n--- PATRON FACTORY METHOD ---");
        System.out.println("Haciendo login con distintos usuarios...");

        // Login como Jugador
        System.out.println("\n>> Intento de login: carlos / 1234");
        if (auth1.login("carlos", "1234")) {
            auth1.abrirDashboard("carlos");
        }

        // Login como Administrador
        System.out.println("\n>> Intento de login: admin / admin123");
        if (auth1.login("admin", "admin123")) {
            auth1.abrirDashboard("admin");
        }

        // Login como Moderador
        System.out.println("\n>> Intento de login: maria / abcd");
        if (auth1.login("maria", "abcd")) {
            auth1.abrirDashboard("maria");
        }

        // Login fallido (para demostrar validación)
        System.out.println("\n>> Intento de login: hacker / 9999");
        if (!auth1.login("hacker", "9999")) {
            System.out.println("   Acceso denegado. No se muestra dashboard.");
        }

        System.out.println("\n============================================");
        System.out.println("   Demostracion finalizada exitosamente.");
        System.out.println("============================================");
    }
}
