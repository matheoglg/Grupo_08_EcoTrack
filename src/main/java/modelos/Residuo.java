/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.time.LocalDate;
import java.util.Comparator;

/**
 *
 * @author User
 */
public class Residuo implements Comparable<Residuo> {
    private String id;
    private String nombre;
    private String tipo;
    private double peso;
    private LocalDate fechaRecoleccion;
    private Zona zona;
    private int prioridadAmbiental;

    public Residuo(String id, String nombre, String tipo, double peso, LocalDate fechaRecoleccion, Zona zona, int prioridadAmbiental) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.fechaRecoleccion = fechaRecoleccion;
        this.zona = zona;
        this.prioridadAmbiental = prioridadAmbiental;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public LocalDate getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public void setFechaRecoleccion(LocalDate fechaRecoleccion) {
        this.fechaRecoleccion = fechaRecoleccion;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public int getPrioridadAmbiental() {
        return prioridadAmbiental;
    }

    public void setPrioridadAmbiental(int prioridadAmbiental) {
        this.prioridadAmbiental = prioridadAmbiental;
    }

    // Ordenar por prioridad ambiental de mayor a menor
    @Override
    public int compareTo(Residuo otroResiduo) {
        return Integer.compare(this.getPrioridadAmbiental(), otroResiduo.getPrioridadAmbiental());
    }
    // Ordenar por peso
    public static Comparator<Residuo> PorPeso = (r1, r2) -> {
        return Double.compare(r1.getPeso(), r2.getPeso());
    };

    // Ordenar por Tipo (Alfabético A-Z)
    public static Comparator<Residuo> PorTipo = (r1, r2) -> {
        return r1.getTipo().compareToIgnoreCase(r2.getTipo());
    };

    // Ordenar por Prioridad Ambiental (Mayor a Menor)
    public static Comparator<Residuo> PorPrioridad = (r1, r2) -> {
        return Integer.compare(r2.getPrioridadAmbiental(), r1.getPrioridadAmbiental());
    };
    
}
