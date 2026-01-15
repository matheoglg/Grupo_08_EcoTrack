/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import estructuras.PriorityQueue;
import modelos.VehiculoRecolector;

/**
 *
 * @author User
 */
public class GestorRutasRecoleccion {

    private PriorityQueue<VehiculoRecolector> colaVehiculos;

    // Constructor usando prioridad natural (compareTo)
    public GestorRutasRecoleccion() {
        this.colaVehiculos = new PriorityQueue<>();
    }

    // Registra vehiculo en la cola de prioridad
    public void registrarVehiculo(VehiculoRecolector v) {
        if (v == null){
            return;
        }
        colaVehiculos.enqueue(v);
    }

    // Despacho automatico del vehículo con mayor prioridad
    public VehiculoRecolector despacharVehiculo() {
        if (colaVehiculos.isEmpty()) {
            return null;
        }
        return colaVehiculos.dequeue();
    }

    // Consulta el siguiente vehiculo sin retirarlo
    public VehiculoRecolector verSiguienteVehiculo() {
        if (colaVehiculos.isEmpty()) {
            return null;
        }
        return colaVehiculos.peek();
    }

    
}