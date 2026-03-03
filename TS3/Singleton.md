# Patrón Singleton — SistemaAutenticacion

## ¿Qué es?

El patrón Singleton garantiza que una clase tenga **una única instancia** en toda la aplicación y proporciona un punto de acceso global a ella.

En este proyecto, el sistema de autenticación (`SistemaAutenticacion`) es único: no tiene sentido tener dos sistemas de login, porque los usuarios se registrarían en uno y no podrían entrar en el otro.

## Fragmento de código del proyecto

Extraído de `src/SistemaAutenticacion.java`:

```java
public class SistemaAutenticacion {

    // La única instancia del sistema (empieza como null)
    private static SistemaAutenticacion instancia;

    // Constructor PRIVADO: nadie fuera de esta clase puede crear otro sistema.
    // Esta es la clave del patrón Singleton.
    private SistemaAutenticacion() {
        this.usuarios = new HashMap<>();
        this.roles = new HashMap<>();
        registrarUsuario("carlos", "1234", "jugador");
        registrarUsuario("admin", "admin123", "administrador");
        registrarUsuario("maria", "abcd", "moderador");
        System.out.println("[Auth] Sistema de autenticacion iniciado.");
    }

    // Punto de acceso GLOBAL al sistema de autenticación.
    // Si no existe, lo crea. Si ya existe, devuelve el mismo.
    public static SistemaAutenticacion getInstance() {
        if (instancia == null) {
            instancia = new SistemaAutenticacion();
        }
        return instancia;
    }
}
```

### ¿Cómo se usa en el proyecto?

En `Main.java` se demuestra que ambas referencias apuntan al mismo objeto:

```java
SistemaAutenticacion auth1 = SistemaAutenticacion.getInstance();
SistemaAutenticacion auth2 = SistemaAutenticacion.getInstance();

System.out.println("auth1 == auth2? " + (auth1 == auth2));
// Imprime: true (es la misma instancia)
```
