/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author User
 */
public class VehiculoRecolector {
    private String id;
    private double capMax;
    private double cargaActual;
    private Zona zonaRecoleccion;
    
    public void agregarResiduo(Residuo r){
        // Añade el residuo y su peso al total de la carga actual del vehículo
        // mientras este no llegue ni supere su carga máxima.
    }
    
    public void asignarZona(Zona z){
        // Asignar una zona al vehículo para que realice la recolección dentro de la misma
    }
    
    public int getPrioridad(){
        // Calcula la prioridad ambiental u operativa para la cola de prioridad
        // se basa en las zonas más críticas o en la capacidad que le quede al vehículo
        // para recolectar basura.
        return 0;
    }
}
