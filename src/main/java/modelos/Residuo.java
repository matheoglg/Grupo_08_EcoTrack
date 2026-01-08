/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.time.LocalDate;

/**
 *
 * @author User
 */
public class Residuo {
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
    
    
}
