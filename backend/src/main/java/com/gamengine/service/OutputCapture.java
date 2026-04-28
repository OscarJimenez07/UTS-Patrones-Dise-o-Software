package com.gamengine.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputCapture {

    public static String capture(Runnable accion) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream nuevo = new PrintStream(buffer, true, java.nio.charset.StandardCharsets.UTF_8);
        try {
            System.setOut(nuevo);
            accion.run();
            System.out.flush();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
    }
}
