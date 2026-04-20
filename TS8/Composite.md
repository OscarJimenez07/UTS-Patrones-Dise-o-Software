# Patron Composite — Sistema de Clanes y Escuadrones

## Diagrama UML
![Composite](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS8/Composite.puml)

## ¿Que es?

El patron Composite **permite tratar de forma uniforme a objetos individuales y a composiciones de objetos** que forman una estructura de arbol. Los clientes usan la misma interfaz para una hoja (un objeto simple) y para un nodo que contiene otros nodos.

En este proyecto, los clanes del juego tienen una estructura jerarquica: un **clan** contiene **escuadrones**, y cada escuadron contiene **jugadores** (o incluso otros escuadrones). Sin Composite, el lider tendria que recorrer manualmente cada nivel para enviar un mensaje, sumar el poder total o contar miembros. Con Composite, el clan y los escuadrones implementan la misma interfaz que un jugador, y basta con llamar al metodo una sola vez sobre el nodo raiz: la operacion se propaga automaticamente a toda la jerarquia.

## Roles del patron

| Rol | Clase en el proyecto |
|---|---|
| **Component** | `MiembroClan` (interfaz) |
| **Leaf** | `JugadorClan` |
| **Composite** | `Escuadron` |
| **Cliente** | `Main.menuClanes()` |

## Fragmento de codigo del proyecto

### Component — `src/SistemaClanes.java`

```java
// COMPONENT: contrato comun para hojas (jugadores)
// y composites (escuadrones).
interface MiembroClan {
    String getNombre();
    int getPoder();
    int contarMiembros();
    void recibirMensaje(String mensaje);
    void mostrar(int nivel);
}
```

### Leaf — `src/SistemaClanes.java`

```java
// LEAF: jugador individual. No tiene hijos.
class JugadorClan implements MiembroClan {
    private String nombre;
    private int poder;
    private String rol;

    @Override
    public int getPoder() { return poder; }

    @Override
    public int contarMiembros() { return 1; }

    @Override
    public void recibirMensaje(String mensaje) {
        System.out.println("    [" + rol + "] " + nombre + " recibe: \"" + mensaje + "\"");
    }
}
```

### Composite — `src/SistemaClanes.java`

```java
// COMPOSITE: escuadron que contiene miembros.
// Puede contener jugadores y/u otros escuadrones.
class Escuadron implements MiembroClan {
    private String nombre;
    private List<MiembroClan> miembros = new ArrayList<>();

    public void agregar(MiembroClan miembro) { miembros.add(miembro); }

    @Override
    public int getPoder() {
        int total = 0;
        for (MiembroClan m : miembros) {
            total += m.getPoder();   // <-- delega al hijo (sea leaf o composite)
        }
        return total;
    }

    @Override
    public void recibirMensaje(String mensaje) {
        System.out.println("  >> Difundiendo a \"" + nombre + "\": " + mensaje);
        for (MiembroClan m : miembros) {
            m.recibirMensaje(mensaje);   // <-- recursion uniforme
        }
    }
}
```

### ¿Como se usa en el proyecto?

En `src/Main.java` se construye un clan con estructura anidada y el cliente lo trata como si fuera un solo objeto:

```java
// Construccion de la jerarquia
Escuadron clan = new Escuadron("Dragones de Acero");

Escuadron asalto = new Escuadron("Escuadron Asalto");
asalto.agregar(new JugadorClan("oscar", 1250, "Lider"));
asalto.agregar(new JugadorClan("anderson", 1180, "Oficial"));

Escuadron soporte = new Escuadron("Escuadron Soporte");
soporte.agregar(new JugadorClan("shadowKnight", 1980, "Miembro"));

clan.agregar(asalto);
clan.agregar(soporte);
clan.agregar(new JugadorClan("recluta", 1000, "Recluta"));

// El cliente llama UNA vez sobre el nodo raiz:
clan.recibirMensaje("Nos vemos en el lobby!");
// Se propaga a los 2 escuadrones y a los 5 jugadores automaticamente.

System.out.println("Poder total: " + clan.getPoder());
// 1250 + 1180 + 1980 + 1000 = 5410
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patron |
|---|---|
| `src/SistemaClanes.java` | Component, Leaf y Composite |
| `src/Main.java` | Cliente: construye la jerarquia y la usa uniformemente |

## Puntos clave para sustentar

1. **Transparencia**: el cliente (`Main`) no distingue si esta hablando con un `JugadorClan` o con un `Escuadron`. Ambos responden a `recibirMensaje()`, `getPoder()` y `mostrar()`.
2. **Recursion implicita**: el `Escuadron` no tiene logica especial para aplanar la jerarquia. Simplemente delega a sus hijos, que a su vez pueden ser otros `Escuadron`.
3. **Extensibilidad**: agregar un nuevo tipo de nodo (por ejemplo una `Alianza` que contiene varios clanes) no requiere tocar el codigo cliente. Solo hay que implementar `MiembroClan`.
4. **Relacion con el dominio**: el README del proyecto describe "Comunidades y Clanes con roles (lider, oficial, miembro)" y "rankings entre comunidades" — Composite modela naturalmente esa jerarquia.

## Comparacion con patrones anteriores

| Aspecto | Adapter (TS6) | Decorator (TS7) | Bridge (TS7) | **Composite (TS8)** |
|---|---|---|---|---|
| **Proposito** | Hacer compatibles **interfaces incompatibles** | Agregar **responsabilidades dinamicas** | Separar **abstraccion de implementacion** | Tratar objetos y grupos de objetos de forma **uniforme** |
| **Tipo** | Estructural | Estructural | Estructural | **Estructural** |
| **Estructura** | 1 a 1 (adapter envuelve adaptee) | Cadena (A envuelve B envuelve C) | 2 jerarquias conectadas | **Arbol (N hijos por nodo)** |
| **Mecanismo** | Composicion | Composicion | Composicion | **Composicion recursiva** |
| **Ejemplo** | `AdaptadorDiscord` | `MejoraDanoFuego` apila sobre habilidad | `AtaqueCuerpoACuerpo` + `ElementoFuego` | `Escuadron` contiene `JugadorClan` y otros `Escuadron` |

## Diferencia clave: Composite vs Decorator

Ambos usan composicion recursiva, pero resuelven problemas distintos:

| Aspecto | Decorator | Composite |
|---|---|---|
| **Objetivo** | Agregar comportamiento a un objeto | Representar jerarquias todo-parte |
| **¿Cuantos hijos?** | Exactamente **uno** (envuelve a otro) | **Muchos** (lista de hijos) |
| **¿Cambia el comportamiento?** | Si, anade responsabilidades | No, delega y agrega resultados |
| **En el proyecto** | Habilidad + mejora + mejora | Clan → Escuadrones → Jugadores |
