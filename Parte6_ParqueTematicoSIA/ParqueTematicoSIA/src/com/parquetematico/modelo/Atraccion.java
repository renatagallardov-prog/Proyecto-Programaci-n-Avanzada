package com.parquetematico.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Atraccion {

    private String codigo;
    private String nombre;
    private TipoAtraccion tipo;
    private int capacidadMaxima;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private boolean activa;
    private List<Reserva> reservas;

    public Atraccion(String codigo, String nombre, TipoAtraccion tipo, int capacidadMaxima,
                      LocalTime horaApertura, LocalTime horaCierre, boolean activa) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.activa = activa;
        this.reservas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoAtraccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtraccion tipo) {
        this.tipo = tipo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public boolean tieneCapacidadDisponible(int personasSolicitadas) {
        return personasSolicitadas > 0 && personasSolicitadas <= this.capacidadMaxima;
    }

    public boolean tieneCapacidadDisponible(LocalDate fecha, int personasSolicitadas) {
        int ocupadas = 0;
        for (Reserva r : reservas) {
            if (r.getFecha() != null && r.getFecha().equals(fecha)
                    && r.getEstado() != EstadoReserva.CANCELADA) {
                ocupadas += r.getCantidadPersonas();
            }
        }
        return (ocupadas + personasSolicitadas) <= this.capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Atraccion{codigo=" + codigo + ", nombre=" + nombre + ", tipo=" + tipo
                + ", capacidadMaxima=" + capacidadMaxima + ", horario=" + horaApertura + "-"
                + horaCierre + ", activa=" + activa + ", reservas=" + reservas.size() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atraccion)) return false;
        Atraccion atraccion = (Atraccion) o;
        return Objects.equals(codigo, atraccion.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
