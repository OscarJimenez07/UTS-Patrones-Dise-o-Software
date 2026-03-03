# Patrón Factory Method — Dashboard y DashboardFactory

## Diagrama UML
![FactoryMethod](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS3/FactoryMethod.puml)

## ¿Qué es?

El patrón Factory Method define una interfaz para crear objetos, pero deja que las **subclases** decidan qué clase concreta instanciar. Así el código cliente trabaja con la clase abstracta sin conocer los tipos concretos.

En este proyecto, después del login se crea el dashboard correcto según el rol del usuario (Jugador, Administrador o Moderador) sin que el código cliente necesite saber qué clase concreta se instancia.

## Fragmento de código del proyecto

Extraído de `src/Dashboard.java`:

```java
// Clase abstracta que define el "método fábrica" para crear dashboards.
// Cada subclase decide QUÉ tipo de dashboard crear.
abstract class DashboardFactory {

    // FACTORY METHOD: las subclases deciden qué tipo de Dashboard crear.
    public abstract Dashboard crearDashboard(String nombreUsuario);

    // Método que usa el factory method internamente.
    // Crea el dashboard y lo muestra al usuario.
    public Dashboard prepararDashboard(String nombreUsuario) {
        Dashboard dashboard = crearDashboard(nombreUsuario);
        System.out.println("[Sistema] Cargando dashboard para rol: " + dashboard.getRol());
        dashboard.mostrar();
        return dashboard;
    }
}

// Fábrica concreta que crea dashboards de Jugador.
class FactoryDashboardJugador extends DashboardFactory {
    @Override
    public Dashboard crearDashboard(String nombreUsuario) {
        return new DashboardJugador(nombreUsuario);
    }
}
```

### ¿Cómo se usa en el proyecto?

En `SistemaAutenticacion.java`, el método `abrirDashboard` selecciona la fábrica según el rol:

```java
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
```
