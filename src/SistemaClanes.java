/**
 * Patron Composite — Sistema de Clanes y Escuadrones.
 *
 * Contiene:
 * - Component (MiembroClan): interfaz comun para jugadores individuales y grupos
 * - Leaf (JugadorClan): miembro individual del clan (nodo hoja)
 * - Composite (Escuadron): grupo que contiene jugadores y/u otros escuadrones
 *
 * Permite tratar de forma uniforme a un jugador solo o a toda una jerarquia
 * de escuadrones. El lider del clan puede enviar un mensaje a "todo el clan"
 * sin saber si esta hablando con un jugador o con un grupo anidado.
 */

import java.util.ArrayList;
import java.util.List;

// ══════════════════════════════════════════════
// COMPONENT: contrato comun para hojas (jugadores)
// y composites (escuadrones). Permite tratar a
// ambos de la misma forma.
// ══════════════════════════════════════════════
interface MiembroClan {
    String getNombre();
    int getPoder();
    int contarMiembros();
    void recibirMensaje(String mensaje);
    void mostrar(int nivel);
}

// ══════════════════════════════════════════════
// LEAF: jugador individual. No tiene hijos.
// Es la unidad basica del clan.
// ══════════════════════════════════════════════
class JugadorClan implements MiembroClan {
    private String nombre;
    private int poder;
    private String rol;

    public JugadorClan(String nombre, int poder, String rol) {
        this.nombre = nombre;
        this.poder = poder;
        this.rol = rol;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getPoder() {
        return poder;
    }

    @Override
    public int contarMiembros() {
        return 1;
    }

    @Override
    public void recibirMensaje(String mensaje) {
        System.out.println("    [" + rol + "] " + nombre + " recibe: \"" + mensaje + "\"");
    }

    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.println(indent + "- " + nombre + " (" + rol + ", poder " + poder + ")");
    }
}

// ══════════════════════════════════════════════
// COMPOSITE: escuadron que contiene MiembroClan.
// Un escuadron puede tener jugadores y tambien
// otros escuadrones anidados (clan -> divisiones
// -> escuadrones -> jugadores).
//
// Las operaciones se delegan a todos los hijos,
// por lo que un mensaje al clan llega a todos.
// ══════════════════════════════════════════════
class Escuadron implements MiembroClan {
    private String nombre;
    private List<MiembroClan> miembros = new ArrayList<>();

    public Escuadron(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(MiembroClan miembro) {
        miembros.add(miembro);
    }

    public void remover(MiembroClan miembro) {
        miembros.remove(miembro);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    // El poder del escuadron es la suma del poder de todos
    // sus miembros (sean jugadores o subescuadrones).
    @Override
    public int getPoder() {
        int total = 0;
        for (MiembroClan m : miembros) {
            total += m.getPoder();
        }
        return total;
    }

    @Override
    public int contarMiembros() {
        int total = 0;
        for (MiembroClan m : miembros) {
            total += m.contarMiembros();
        }
        return total;
    }

    // Un mensaje al escuadron se reenvia a todos los hijos,
    // que pueden ser jugadores (leaf) u otros escuadrones.
    @Override
    public void recibirMensaje(String mensaje) {
        System.out.println("  >> Difundiendo a \"" + nombre + "\": " + mensaje);
        for (MiembroClan m : miembros) {
            m.recibirMensaje(mensaje);
        }
    }

    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.println(indent + "+ " + nombre
                + " [miembros: " + contarMiembros()
                + ", poder: " + getPoder() + "]");
        for (MiembroClan m : miembros) {
            m.mostrar(nivel + 1);
        }
    }
}
