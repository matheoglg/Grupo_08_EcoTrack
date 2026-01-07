/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.Map;

/**
 *
 * @author User
 */
public class CentroReciclaje {
    private Stack<Residuo> pilaResiduos;
    private Map<String,Double> estadisticasPorTipo;
    private Map<String,Double> estadisticasPorZona;

    public CentroReciclaje(Stack pilaResiduos, Map estadisticasPorZona) {
        this.pilaResiduos = pilaResiduos;
        this.estadisticasPorZona = estadisticasPorZona;
    }
    
    public void apilarResiduo(Residuo r){
        // Agrega el residuo dado al tope de la pila de nombre pilaResiduos
        // simulando el ingreso de residuos al centro.
    }
    
    public void procesarResiduo(){
        // Saca el residuo que está en el tope de la pila
        // para simular su procesamiento y así llama a actualizarEstadisticas().
    }
    
    public void actualizarEstadisticas(){
        // Actualiza los mapas de estadísticas por tipo de basura y por zona.
    }
}
