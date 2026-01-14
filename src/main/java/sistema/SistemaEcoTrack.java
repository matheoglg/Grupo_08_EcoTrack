/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema;

import estructuras.DoublyCircularLinkedList;
import estructuras.Stack;
import java.io.*;
import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import modelos.CentroReciclaje;
import modelos.Residuo;
import modelos.VehiculoRecolector;
import modelos.Zona;

/**
 *
 * @author User
 */
public class SistemaEcoTrack {
    private static SistemaEcoTrack instancia;
    private DoublyCircularLinkedList<Residuo> listaResiduos;
    private PriorityQueue<VehiculoRecolector> colaVehiculos;
    private CentroReciclaje cReciclaje;
    private Map<String, Zona> mapaZonas;
    
    private final String ARCHIVO_RESIDUOS = "residuos.txt";
    private final String ARCHIVO_ESTADISTICAS = "estadisticas.txt";
    private final String ARCHIVO_VEHICULOS = "vehiculos.txt";
    private final String ARCHIVO_ZONAS = "zonas.txt";
    
    private SistemaEcoTrack() {
        // Inicializar estructuras
        listaResiduos = new DoublyCircularLinkedList<>();
        colaVehiculos = new PriorityQueue<>();
        // Intentar cargar desde persistencia
        cReciclaje = new CentroReciclaje(
            new Stack<Residuo>(), 
            new HashMap<String, Double>(), 
            new HashMap<String, Double>()
        );
        cargarDatos();
    }
    
    public static SistemaEcoTrack getInstancia() {
        if (instancia == null) {
            instancia = new SistemaEcoTrack();
        }
        return instancia;
    }
    
    public DoublyCircularLinkedList<Residuo> getResiduos(){
        return listaResiduos;
    }
    
    public PriorityQueue<VehiculoRecolector> getVehiculo(){
        return colaVehiculos;
    }
    
    public Map<String,Zona> getMapaZonas(){
        return mapaZonas;
    }
    
    public CentroReciclaje getCentroReciclaje(){
        return cReciclaje;
    }
    
    public void registrarResiduo(Residuo r){
        if (r != null) {
            listaResiduos.addLast(r);
        }
    }
    
    public void agregarVehiculo(VehiculoRecolector v) {
        if (v != null) {
            colaVehiculos.add(v);
        }
    }
    
    public void despacharVehiculo(){
        // Obtiene el vehículo con mayor prioridad de la cola de prioridad de vehículos 
        // y lo asigna a la zona más crítica, es decir, la que tenga menor utilidad.        
    }
    
    public void cargarDatos(){
        cargarResiduos();
        cargarEstadisticas();
        cargarZonas();
        cargarVehiculos;
        if (listaResiduos.isEmpty()) {
            inicializarDatos();
        }
    }
    
    public void guardarDatos(){
        guardarResiduos();
        guardarEstadisticas();
        guardarVehiculos();
        guardarZonas();
    }
    
    private void guardarResiduos() {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_RESIDUOS))) {
            for (Residuo r : listaResiduos) {
                // Formato: id;nombre;tipo;peso;fecha;nombreZona;prioridad
                out.println(r.getId() + ";" + r.getNombre() + ";" + r.getTipo() + ";" + 
                            r.getPeso() + ";" + r.getFechaRecoleccion() + ";" + 
                            r.getZona().getNombre() + ";" + r.getPrioridadAmbiental());
            }
        } catch (IOException e) { System.err.println("Error residuos: " + e.getMessage()); }
    }
    private void guardarEstadisticas() {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_ESTADISTICAS))) {
            // Guardamos el mapa de tipos
            for (Map.Entry<String, Double> entry : cReciclaje.getEstadisticasPorTipo().entrySet()) {
                out.println("T:" + entry.getKey() + ":" + entry.getValue());
            }
            // Guardamos el mapa de zonas
            for (Map.Entry<String, Double> entry : cReciclaje.getEstadisticasPorZona().entrySet()) {
                out.println("Z:" + entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error guardando estadísticas: " + e.getMessage());
        }
    }
    private void guardarVehiculos() {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_VEHICULOS))) {
            // Nota: Aquí se asume que puedes iterar o vaciar la cola para guardar
            // Para persistencia simple, guardamos los datos básicos
            while(!colaVehiculos.isEmpty()){
                VehiculoRecolector v = colaVehiculos.poll();
                out.println(v.getId() + ";" + v.getCapMax());
            }
        } catch (IOException e) {
            System.err.println("Error guardando vehículos: " + e.getMessage());
        }
    }
    private void guardarZonas() {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_ZONAS))) {
            for (Zona z : mapaZonas.values()) {
                // Formato: nombre,pRecolectado,pPendiente
                out.println(z.getNombre() + "," + (int)z.getpRecolectado() + "," + (int)z.getpPendiente());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar zonas: " + e.getMessage());
        }
    }
    
    private void cargarZonas() {
        File f = new File(ARCHIVO_ZONAS);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] d = sc.nextLine().split(",");
                if (d.length == 3) {
                    Zona z = new Zona(d[0], Integer.parseInt(d[1]), Integer.parseInt(d[2]));
                    mapaZonas.put(z.getNombre(), z);
                }
            }
        } catch (Exception e) { System.err.println("Error cargando zonas."); }
    }
    private void cargarResiduos() {
        File f = new File(ARCHIVO_RESIDUOS);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] d = sc.nextLine().split(";");
                Zona z = mapaZonas.get(d[5]);
                if (z != null) {
                    Residuo r = new Residuo(d[0], d[1], d[2], Double.parseDouble(d[3]), 
                                            LocalDate.parse(d[4]), z, Integer.parseInt(d[6]));
                    listaResiduos.addLast(r);
                }
            }
        } catch (Exception e) { System.err.println("Error cargando residuos"); }
    }
    private void cargarEstadisticas() {
        File f = new File(ARCHIVO_ESTADISTICAS);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] d = sc.nextLine().split(":");
                if (d[0].equals("T")) 
                    cReciclaje.getEstadisticasPorTipo().put(d[1], Double.parseDouble(d[2]));
                else 
                    cReciclaje.getEstadisticasPorZona().put(d[1], Double.parseDouble(d[2]));
            }
        } catch (Exception e) { 
            System.err.println("Error al cargar estadísticas."); 
        }
    }
    private void cargarVehiculos() {
        File f = new File(ARCHIVO_VEHICULOS);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] d = sc.nextLine().split(";");
                VehiculoRecolector v = new VehiculoRecolector(d[0], Double.parseDouble(d[1]),Double.parseDouble(d[2]), mapaZonas.get(d[3]));
                v.setCargaActual(Double.parseDouble(d[2]));
                v.asignarZona(mapaZonas.get(d[3]));
                colaVehiculos.add(v);
            }
        } catch (Exception e) { System.err.println("Error cargando vehiculos"); }
    }
    
    public void inicializarDatos(){
        
    }
}
