package com.gamengine.patrones;

/**
 * Patron Composite — Sistema de Clanes y Escuadrones.
 */

import java.util.ArrayList;
import java.util.List;

// ══════════════════════════════════════════════
// COMPONENT
// ══════════════════════════════════════════════
interface MiembroClan {
    String getNombre();
    int getPoder();
    int contarMiembros();
    void recibirMensaje(String mensaje);
    void mostrar(int nivel);
}

// ══════════════════════════════════════════════
// LEAF: jugador individual
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

    @Override public String getNombre() { return nombre; }
    @Override public int getPoder() { return poder; }
    @Override public int contarMiembros() { return 1; }
    public String getRol() { return rol; }

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
// COMPOSITE: escuadron
// ══════════════════════════════════════════════
class Escuadron implements MiembroClan {
    private String nombre;
    private List<MiembroClan> miembros = new ArrayList<>();

    public Escuadron(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(MiembroClan miembro) { miembros.add(miembro); }
    public void remover(MiembroClan miembro) { miembros.remove(miembro); }
    public List<MiembroClan> getMiembros() { return miembros; }

    @Override public String getNombre() { return nombre; }

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
