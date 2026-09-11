package com.parquetematico.ui;

import com.parquetematico.controlador.ParqueController;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class AplicacionParque {

    private final ParqueController controlador;

    public AplicacionParque() {
        controlador = new ParqueController();
    }

    public void iniciar() {
        controlador.iniciar();

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Administracion - Parque Tematico ===");
        System.out.print("Como desea usar el sistema? (1) Consola  (2) Ventana: ");
        String opcion = scanner.nextLine().trim();

        if ("2".equals(opcion)) {
            abrirVentana();
        } else {
            abrirConsola(scanner);
        }
    }

    private void abrirConsola(Scanner scanner) {
        MenuConsola menu = new MenuConsola(controlador, scanner);
        menu.iniciar();
        controlador.guardarTodo();
        System.out.println("Datos guardados. Hasta pronto!");
    }

    private void abrirVentana() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal(controlador).setVisible(true);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                controlador.guardarTodo();
            }
        }));
    }
}
