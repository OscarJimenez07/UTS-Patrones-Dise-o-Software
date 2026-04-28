package com.gamengine.patrones;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRON SINGLETON
 *
 * El sistema de autenticación es UNICO en todo el juego.
 * No tiene sentido tener dos sistemas de login, porque los usuarios
 * se registrarían en uno y no podrían entrar en el otro.
 *
 * Cómo funciona:
 * 1. El constructor es PRIVADO (nadie puede hacer "new SistemaAutenticacion()")
 * 2. La única forma de obtenerlo es con getInstance()
 * 3. getInstance() crea la instancia solo la PRIMERA vez
 */
public class SistemaAutenticacion {

    // La única instancia del sistema (empieza como null)
    private static SistemaAutenticacion instancia;

    // Base de datos simulada: usuario -> contraseña
    private Map<String, String> usuarios;

    // Base de datos simulada: usuario -> rol
    private Map<String, String> roles;

    /**
     * Constructor PRIVADO: nadie fuera de esta clase puede crear otro sistema.
     * Esta es la clave del patrón Singleton.
     */
    private SistemaAutenticacion() {
        this.usuarios = new HashMap<>();
        this.roles = new HashMap<>();

        // Usuarios registrados
        registrarUsuario("oscar", "1234", "jugador");
        registrarUsuario("anderson", "1234", "jugador");

        System.out.println("[Auth] Sistema de autenticacion iniciado.");
    }

    /**
     * Punto de acceso GLOBAL al sistema de autenticación.
     * Si no existe, lo crea. Si ya existe, devuelve el mismo.
     */
    public static SistemaAutenticacion getInstance() {
        if (instancia == null) {
            instancia = new SistemaAutenticacion();
        }
        return instancia;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public void registrarUsuario(String usuario, String contrasena, String rol) {
        usuarios.put(usuario, contrasena);
        roles.put(usuario, rol);
    }

    /**
     * Intenta hacer login. Devuelve true si las credenciales son correctas.
     */
    public boolean login(String usuario, String contrasena) {
        if (!usuarios.containsKey(usuario)) {
            System.out.println("[Auth] Usuario '" + usuario + "' no encontrado.");
            return false;
        }

        if (!usuarios.get(usuario).equals(contrasena)) {
            System.out.println("[Auth] Contrasena incorrecta para '" + usuario + "'.");
            return false;
        }

        System.out.println("[Auth] Login exitoso para '" + usuario + "' (Rol: " + roles.get(usuario) + ")");
        return true;
    }

    /**
     * Devuelve el rol de un usuario.
     */
    public String obtenerRol(String usuario) {
        return roles.get(usuario);
    }

    /**
     * Después del login, abre el dashboard correcto usando Factory Method.
     * Selecciona la fábrica según el rol del usuario.
     */
    public Dashboard abrirDashboard(String usuario) {
        String rol = obtenerRol(usuario);
        DashboardFactory factory;

        switch (rol.toLowerCase()) {
            case "jugador":
                factory = new FactoryDashboardJugador();
                break;
            case "administrador":
                factory = new FactoryDashboardAdmin();
                break;
            case "moderador":
                factory = new FactoryDashboardModerador();
                break;
            default:
                System.out.println("[Auth] Rol no reconocido: " + rol);
                return null;
        }

        return factory.prepararDashboard(usuario);
    }
}
