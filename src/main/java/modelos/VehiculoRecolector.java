/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author User
 */
public class VehiculoRecolector implements Comparable<VehiculoRecolector>{
    private String id;
    private double capMax;
    private double cargaActual;
    private Zona zonaRecoleccion;

    public VehiculoRecolector(String id, double capMax, double cargaActual, Zona zonaRecoleccion) {
        this.id = id;
        this.capMax = capMax;
        this.cargaActual = cargaActual;
        this.zonaRecoleccion = zonaRecoleccion;
    }
    
    public VehiculoRecolector(String id, double capMax) {
        this.id = id;
        this.capMax = capMax;
        this.cargaActual = 0;
    }
    
    
    public void agregarResiduo(Residuo r){
        if (r != null && (this.cargaActual + r.getPeso()) <= this.capMax) {
            this.cargaActual += r.getPeso();

            this.zonaRecoleccion.setpPendiente(this.zonaRecoleccion.getpPendiente() - 1);
            this.zonaRecoleccion.setpRecolectado(this.zonaRecoleccion.getpRecolectado() + 1);

            System.out.println("Residuo " + r.getId() + " recolectado. Total en zona: " + 
                               this.zonaRecoleccion.getpRecolectado() + " objetos.");
        } else {
            System.out.println("El vehículo está lleno por peso, no puede cargar más unidades.");
        }
    }
    
    public void asignarZona(Zona zonaRecoleccion){
        this.zonaRecoleccion = zonaRecoleccion;
    }
    
    public int getPrioridad(){
        // Calcula la prioridad ambiental u operativa para la cola de prioridad
        // se basa en las zonas más críticas o en la capacidad que le quede al vehículo
        // para recolectar basura.
        if (zonaRecoleccion == null) return 0;
        
        // Ejemplo: Prioridad basada en el pPendiente de la zona
        return zonaRecoleccion.getpPendiente();
    }

    public String getId() {
        return id;
    }

    public double getCapMax() {
        return capMax;
    }

    public double getCargaActual() {
        return cargaActual;
    }
    public void setCargaActual(double cargaActual){
        this.cargaActual = cargaActual;        
    }

    public Zona getZonaRecoleccion() {
        return zonaRecoleccion;
    }
    
    @Override
    public int compareTo(VehiculoRecolector otro) {
        return Integer.compare(otro.getPrioridad(), this.getPrioridad());
    }
    
}
