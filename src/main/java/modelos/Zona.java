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
    private int pRecolectado;
    private int pPendiente;

    public Zona(String nombre, int pRecolectado, int pPendiente) {
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

    public int getpRecolectado() {
        return pRecolectado;
    }

    public void setpRecolectado(int pRecolectado) {
        this.pRecolectado = pRecolectado;
    }

    public int getpPendiente() {
        return pPendiente;
    }

    public void setpPendiente(int pPendiente) {
        this.pPendiente = pPendiente;
    }
    
    public int calcularUtilidad(){
        return pRecolectado - pPendiente;
    }
    
    
    @Override
    public String toString() {
        return this.nombre; 
    }
}
