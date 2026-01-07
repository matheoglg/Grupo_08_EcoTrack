/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema;

import java.util.Map;
import modelos.CentroReciclaje;
import modelos.Residuo;
import modelos.Zona;

/**
 *
 * @author User
 */
public class SistemaEcoTrack {
    private DoublyCircularNodeList<Residuo> listaResiduos;
    private PriorityQueue<VehiculoRecolector> colaVehiculos;
    private Map<String,Zona> mapaZonas;
    private CentroReciclaje cReciclaje;
    
    public void registrarResiduuo(Residuo r){
        //Inserta el residuo en la lista circular de residuos (listaResiduos)
    }
    
    public void despacharVehiculo(){
        // Obtiene el vehículo con mayor prioridad de la cola de prioridad de vehículos 
        // y lo asigna a la zona más crítica, es decir, la que tenga menor utilidad.        
    }
    
    public void cargarDatos(){
        // Lee los archivos de texto serializados para cargarlos en el sistema al volver a entrar.
    }
    
    public void guardarDatos(){
        // Se encarga de guardar todo el estado actual del sistema en archivos
        // estos datos vendrían a ser la lista de residuos, las zonas, los vehículos y las estadísticas por categoría.
    }
}
