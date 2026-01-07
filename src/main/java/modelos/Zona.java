/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author User
 */
public class Zona {
    private String nombre;
    private double pRecolectado;
    private double pPendiente;

    public Zona(String nombre, double pRecolectado, double pPendiente) {
        this.nombre = nombre;
        this.pRecolectado = pRecolectado;
        this.pPendiente = pPendiente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getpRecolectado() {
        return pRecolectado;
    }

    public void setpRecolectado(double pRecolectado) {
        this.pRecolectado = pRecolectado;
    }

    public double getpPendiente() {
        return pPendiente;
    }

    public void setpPendiente(double pPendiente) {
        this.pPendiente = pPendiente;
    }
    
    public void calcularUtilidad(){
        // Calcula la utilidad de la zona con los valores de pRecolectado y pPendiente 
        // para verificar si esta tiene acumulación de basura o si está limpia.
    }
}
