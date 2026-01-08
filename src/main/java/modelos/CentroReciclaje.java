/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import estructuras.Stack;
import java.util.Map;

/**
 *
 * @author User
 */
public class CentroReciclaje {
    private Stack<Residuo> pilaResiduos;
    private Map<String,Double> estadisticasPorTipo;
    private Map<String,Double> estadisticasPorZona;

    public CentroReciclaje(Stack<Residuo> pilaResiduos, Map<String, Double> estadisticasPorTipo, Map<String, Double> estadisticasPorZona) {
        this.pilaResiduos = pilaResiduos;
        this.estadisticasPorTipo = estadisticasPorTipo;
        this.estadisticasPorZona = estadisticasPorZona;
    }
    
    // Agrega el residuo dado al tope de la pila de nombre pilaResiduos
    // simulando el ingreso de residuos al centro.
    public void apilarResiduo(Residuo r){
        if (r == null){                        
            return;                            
        }
        pilaResiduos.push(r);
    }
    
    // Saca el residuo que está en el tope de la pila
    // para simular su procesamiento y así llama a actualizarEstadisticas().
    public void procesarResiduo(){
        if(pilaResiduos.isEmpty()){
            return;
        }
        Residuo r = pilaResiduos.pop();
        actualizarEstadisticas();
    }
    
    public void actualizarEstadisticas(){
        // Actualiza los mapas de estadísticas por tipo de basura y por zona.
    }
}
