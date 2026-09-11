package com.parquetematico.controlador;

import com.parquetematico.excepciones.CapacidadExcedidaException;
import com.parquetematico.excepciones.ElementoNoEncontradoException;
import com.parquetematico.gestion.DatosIniciales;
import com.parquetematico.gestion.GestorParque;
import com.parquetematico.modelo.Atraccion;
import com.parquetematico.modelo.EstadoReserva;
import com.parquetematico.modelo.Reserva;
import com.parquetematico.modelo.TipoAtraccion;
import com.parquetematico.persistencia.PersistenciaCSV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParqueController {

    private final GestorParque gestor;
    private final PersistenciaCSV persistencia;

    public ParqueController() {
        this.gestor = new GestorParque();
        this.persistencia = new PersistenciaCSV();
    }

    public void iniciar() {
        File archivo = new File("data/atracciones.csv");
        if (archivo.exists()) {
            persistencia.cargarDatos(gestor);
        } else {
            DatosIniciales.cargar(gestor);
        }
    }

    public void guardarTodo() {
        persistencia.guardarDatos(gestor);
    }

    public void agregarAtraccion(Atraccion a) {
        gestor.agregarAtraccion(a);
    }

    public List<Atraccion> listarAtracciones() {
        return gestor.listarAtracciones();
    }

    public Atraccion buscarAtraccion(String codigo) throws ElementoNoEncontradoException {
        return gestor.buscarAtraccion(codigo);
    }

    public List<Atraccion> buscarAtraccionPorTipo(TipoAtraccion tipo) {
        return gestor.buscarAtraccion(tipo);
    }

    public void modificarAtraccion(String codigo, String nombre, TipoAtraccion tipo, int capacidad)
            throws ElementoNoEncontradoException {
        gestor.modificarAtraccion(codigo, nombre, tipo, capacidad);
    }

    public void eliminarAtraccion(String codigo) throws ElementoNoEncontradoException {
        gestor.eliminarAtraccion(codigo);
    }

    public void agregarReserva(String codigoAtraccion, Reserva reserva)
            throws ElementoNoEncontradoException, CapacidadExcedidaException {
        gestor.agregarReserva(codigoAtraccion, reserva);
    }

    public List<Reserva> listarReservas(String codigoAtraccion) throws ElementoNoEncontradoException {
        return gestor.listarReservas(codigoAtraccion);
    }

    public Reserva buscarReserva(String codigoAtraccion, String idReserva)
            throws ElementoNoEncontradoException {
        return gestor.buscarReserva(codigoAtraccion, idReserva);
    }

    public List<Reserva> buscarReservasPorFecha(LocalDate fecha) {
        return gestor.buscarReservasPorFecha(fecha);
    }

    public void modificarReserva(String codigoAtraccion, String idReserva, int cantidadPersonas,
                                  EstadoReserva estado) throws ElementoNoEncontradoException {
        gestor.modificarReserva(codigoAtraccion, idReserva, cantidadPersonas, estado);
    }

    public void eliminarReserva(String codigoAtraccion, String idReserva)
            throws ElementoNoEncontradoException {
        gestor.eliminarReserva(codigoAtraccion, idReserva);
    }

    public String generarReporteOcupacion(LocalDate desde, LocalDate hasta) {
        return gestor.generarReporteOcupacion(desde, hasta);
    }

    public Map<String, Double> calcularOcupacionPorAtraccion(LocalDate desde, LocalDate hasta) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Atraccion a : gestor.listarAtracciones()) {
            int personas = 0;
            for (Reserva r : a.getReservas()) {
                if (r.getEstado() == EstadoReserva.CANCELADA) continue;
                if (r.getFecha() == null) continue;
                if (!r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta)) {
                    personas = personas + r.getCantidadPersonas();
                }
            }
            double porcentaje = 0;
            if (a.getCapacidadMaxima() > 0) {
                porcentaje = (personas * 100.0) / a.getCapacidadMaxima();
            }
            resultado.put(a.getNombre(), porcentaje);
        }
        return resultado;
    }

    public String exportarPlanillaOcupacion(LocalDate desde, LocalDate hasta) throws IOException {
        File carpeta = new File("reportes");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        String rutaArchivo = "reportes/planilla_ocupacion.csv";

        BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo));
        try {
            bw.write("Atraccion;Personas reservadas;Ocupacion (%)");
            bw.newLine();

            Map<String, Double> ocupacion = calcularOcupacionPorAtraccion(desde, hasta);
            for (Atraccion a : gestor.listarAtracciones()) {
                int personas = 0;
                for (Reserva r : a.getReservas()) {
                    if (r.getEstado() == EstadoReserva.CANCELADA) continue;
                    if (r.getFecha() == null) continue;
                    if (!r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta)) {
                        personas = personas + r.getCantidadPersonas();
                    }
                }
                double porcentaje = ocupacion.get(a.getNombre());
                bw.write(a.getNombre() + ";" + personas + ";" + String.format("%.1f", porcentaje));
                bw.newLine();
            }
        } finally {
            bw.close();
        }
        return rutaArchivo;
    }
}
