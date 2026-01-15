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
        mapaZonas = new HashMap<>();
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
            
            int actualPendiente = r.getZona().getpPendiente();
            r.getZona().setpPendiente(actualPendiente + 1);
        }
    }
    
    public void agregarVehiculo(VehiculoRecolector v) {
        if (v != null) {
            colaVehiculos.offer(v);
        }
    }
    
    public void despacharVehiculo(){
        if (colaVehiculos.isEmpty()) {
            return;
        }

        // Buscar la zona más crítica (la de menor utilidad ambiental)
        Zona zonaCritica = null;
        double menorUtilidad = Double.MAX_VALUE;

        for (Zona z : mapaZonas.values()) {
            int utilidadActual = z.calcularUtilidad();
            if (utilidadActual < menorUtilidad) {
                menorUtilidad = utilidadActual;
                zonaCritica = z;
            }
        }

        // Obtener el vehículo con mayor prioridad (el primero de la cola)
        // .poll() extrae y elimina el elemento de la cabeza de la cola
        if (zonaCritica != null) {
            VehiculoRecolector vehiculo = colaVehiculos.poll(); 

            // 4. Asignar el vehículo a la zona crítica
            vehiculo.asignarZona(zonaCritica);

            System.out.println("DESPACHO: El vehículo " + vehiculo.getId() + 
                               " ha sido enviado a la zona: " + zonaCritica.getNombre() + 
                               " (Utilidad: " + menorUtilidad + ")");

            // Nota: Una vez que el vehículo termine su ruta, 
            // deberías volver a meterlo a la cola con colaVehiculos.add(vehiculo)
        }        
    }
    
    public void cargarDatos(){
        cargarResiduos();
        cargarEstadisticas();
        cargarZonas();
        cargarVehiculos();
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
                colaVehiculos.offer(v);
            }
        } catch (Exception e) { System.err.println("Error cargando vehiculos"); }
    }
    
    public void inicializarDatos(){
        Zona norte = new Zona("Sector Norte", 0, 0);
        Zona sur = new Zona("Sector Sur", 0, 0);
        Zona centro = new Zona("Casco Central", 0, 0);

        // Guardarlas en el mapa para que sean accesibles
        mapaZonas.put(norte.getNombre(), norte);
        mapaZonas.put(sur.getNombre(), sur);
        mapaZonas.put(centro.getNombre(), centro);

        // Crear Vehículos y asignarlos a las zonas
        // Capacidad en kg (3000kg, 5000kg, etc.)
        VehiculoRecolector v1 = new VehiculoRecolector("V-101", 3000.0);
        v1.asignarZona(norte);

        VehiculoRecolector v2 = new VehiculoRecolector("V-102", 5000.0);
        v2.asignarZona(sur);

        // Agregarlos a la PriorityQueue (se ordenarán por la prioridad de sus zonas)
        colaVehiculos.offer(v1);
        colaVehiculos.offer(v2);

        // Crear Residuos Iniciales (7 atributos)
        registrarResiduo(new Residuo("R-001", "Envases Plásticos", "Plástico", 12.5, 
                         LocalDate.now(), norte, 2));

        registrarResiduo(new Residuo("R-002", "Papel Periódico", "Papel", 5.0, 
                         LocalDate.now().minusDays(1), norte, 1));

        registrarResiduo(new Residuo("R-003", "Restos de Comida", "Orgánico", 25.0, 
                         LocalDate.now(), sur, 3));

        // 4. Inicializar algunas estadísticas en el Centro de Reciclaje
        // Esto simula que ya se ha procesado algo antes
        cReciclaje.getEstadisticasPorTipo().put("Plástico", 150.0);
        cReciclaje.getEstadisticasPorTipo().put("Papel", 80.0);
        cReciclaje.getEstadisticasPorZona().put("Sector Norte", 230.0);
    }
}
