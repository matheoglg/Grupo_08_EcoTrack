/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema;

import estructuras.DoublyCircularLinkedList;
import estructuras.Stack;
import java.io.*;
import java.time.LocalDate;
import estructuras.PriorityQueue;
import java.util.ArrayList; 
import java.util.List;
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
    private boolean cambiosP = false;
    
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
        if (colaVehiculos.isEmpty()) {
            inicializarDatos();
        }
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
    
    public PriorityQueue<VehiculoRecolector> getVehiculos(){
        return colaVehiculos;
    }
    
    public Map<String,Zona> getMapaZonas(){
        return mapaZonas;
    }
    
    public CentroReciclaje getCentroReciclaje(){
        return cReciclaje;
    }
    
    public void registrarResiduo(Residuo r){
        if (r != null&& r.getZona() != null) {
            listaResiduos.addLast(r);
            int actualPendiente = r.getZona().getpPendiente();
            r.getZona().setpPendiente(actualPendiente + 1);
            cambiosP=true;
        }
    }
    
    public boolean hayCambios() {
        return cambiosP;
    }

    public void resetearCambios() {
        this.cambiosP = false;
    }
    
    public void registrarCambio() {
        this.cambiosP = true;
    }
    
    public void agregarVehiculo(VehiculoRecolector v) {
        if (v != null) {
            colaVehiculos.enqueue(v);
        }
    }
    
    public void despacharVehiculo(){
        if (colaVehiculos.isEmpty()) {
            System.out.println("No hay vehículos disponibles para despacho.");
            return; 
        }

        // Zona con menor utilidad y mas basura
        Zona zonaCritica = null;
        double menorUtilidad = Double.MAX_VALUE;
        int maxBasura = -1;

        for (Zona z : mapaZonas.values()) {
            double utilidadAct = z.calcularUtilidad();
            int basuraPendiente = z.getpPendiente();

            // Menor utilidad, sino, con mas basura
            if (utilidadAct < menorUtilidad || (utilidadAct == menorUtilidad && basuraPendiente > maxBasura)) {
                menorUtilidad = utilidadAct;
                maxBasura = basuraPendiente;
                zonaCritica = z;
            }
        }

        // Despacho si necesita recoleccion
        if (zonaCritica != null && zonaCritica.getpPendiente() > 0) {
            // Vehiculo con mayor prioridad
            VehiculoRecolector v = colaVehiculos.dequeue();
            v.asignarZona(zonaCritica);

            // Recolección
            Residuo recolectado = null;
            int totalResiduos = listaResiduos.size();

            for (int i = 0; i < totalResiduos; i++) {
                Residuo r = listaResiduos.get(i); 

                if (r.getZona().getNombre().equals(zonaCritica.getNombre())) {
                    double cargaPrevia = v.getCargaActual();
                    v.agregarResiduo(r); 

                    if (v.getCargaActual() > cargaPrevia) {
                        recolectado = r;
                        recolectado.setVehiculoTransportador(v); 

                        // Se elimina de la calle
                        listaResiduos.remove(i); 
                        break; 
                    }
                }
            }

            // Traslado a central
            if (recolectado != null) {
                cReciclaje.apilarResiduo(recolectado);
                System.out.println("Vehículo " + v.getId() + " cargó residuo " + recolectado.getId() + " y lo llevó al centro.");
            } else {
                System.out.println("El vehículo " + v.getId() + " llegó a la zona pero no pudo cargar residuos (exceso de peso).");
            }

            // El vehiculo vuelve a encolarse
            colaVehiculos.enqueue(v);
            registrarCambio();

        } else {
            System.out.println("No se requiere despacho en este momento: zonas estables.");
        }
    }
  
    
    public String obtenerZonaMasCritica() {
        if (mapaZonas.isEmpty()) return "No hay zonas registradas";

        Zona critica = null;
        double menorUtilidad = Double.MAX_VALUE;

        // Recorremos el mapa de zonas para encontrar la de mayor impacto (menor utilidad)
        for (Zona z : mapaZonas.values()) {
            double utilidadActual = z.calcularUtilidad(); 
            if (utilidadActual < menorUtilidad) {
                menorUtilidad = utilidadActual;
                critica = z;
            }
        }
        if(critica != null){
            return critica.getNombre();
        } else {
            return "N/A";
        }
        
    }
    
    public void cargarDatos(){
        //primero las zonas
        cargarZonas(); 
        //estadisticas
        cargarEstadisticas();
        //los que dependen de las zonas
        cargarResiduos();
        cargarVehiculos();
        if (listaResiduos.isEmpty()) {
            inicializarDatos();
        }
        this.cambiosP = false;
    }
    
    public void guardarDatos(){
        guardarResiduos();
        guardarEstadisticas();
        guardarVehiculos();
        guardarZonas();
    }
    
    private void guardarResiduos() {
        File f = new File(ARCHIVO_RESIDUOS);
        try (PrintWriter out = new PrintWriter(new FileWriter(f, false))) {
            if (listaResiduos.isEmpty()){
                out.print(""); //lista vacía, limpia el archivo
                return;
            }            
            for (Residuo r : listaResiduos) {
                if (r == null) continue;
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
            if (cReciclaje.getEstadisticasPorTipo() != null) {
                for (Map.Entry<String, Double> entry : cReciclaje.getEstadisticasPorTipo().entrySet()) {
                out.println("T:" + entry.getKey() + ":" + entry.getValue());
                }
            }
            // Guardamos el mapa de zonas
            if (cReciclaje.getEstadisticasPorZona() != null) {
                for (Map.Entry<String, Double> entry : cReciclaje.getEstadisticasPorZona().entrySet()) {
                out.println("Z:" + entry.getKey() + ":" + entry.getValue());
                }
            }
        } catch (IOException e) {
            System.err.println("Error guardando estadísticas: " + e.getMessage());
        }
    }
    
    
    private void guardarVehiculos() {
        PriorityQueue<VehiculoRecolector> copy = obtenerCopiaVehiculos();
        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_VEHICULOS))) {
            while(!copy.isEmpty()){
                VehiculoRecolector v = copy.dequeue();
                String nombreZona = (v.getZonaRecoleccion() != null) ? v.getZonaRecoleccion().getNombre() : "Sin Zona";
                out.println(v.getId() + ";" +  v.getCapMax() + ";" +v.getCargaActual() + ";" + nombreZona);
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
                String linea = sc.nextLine();
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(",");
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
                String linea = sc.nextLine();
            if (linea.trim().isEmpty()) continue;
            String[] d = linea.split(";");
          
            if (d.length >= 7) { //validar que tenga las columnas necesarias
                Zona z = mapaZonas.get(d[5]);
                if (z != null) {
                    Residuo r = new Residuo(d[0],d[1],d[2],Double.parseDouble(d[3]),LocalDate.parse(d[4]), z,Integer.parseInt(d[6]));
                    listaResiduos.addLast(r); //se agrega a circular doble
      
                }
            }
        }
        } catch (Exception e) { System.err.println("Error cargando residuos"); }
    }
    
    
    private void cargarEstadisticas() {
        File f = new File(ARCHIVO_ESTADISTICAS);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(":");                
                if (d.length >= 3) {
                if (d[0].equals("T")) {
                    //carga estadísticas por tipo de residuo
                    cReciclaje.getEstadisticasPorTipo().put(d[1], Double.parseDouble(d[2]));
                } else if (d[0].equals("Z")) {
                    //carga estadísticas por zona 
                    cReciclaje.getEstadisticasPorZona().put(d[1], Double.parseDouble(d[2]));
                    }
                }
            }
        } catch (Exception e) { 
            System.err.println("Error al cargar estadísticas."); 
        }
    }
    
    
    private void cargarVehiculos() {
        File f = new File(ARCHIVO_VEHICULOS);
        if (!f.exists()){
            System.out.println("Archivo vehiculos.txt no existe.");
            return;
        }
        while(!colaVehiculos.isEmpty()){
            colaVehiculos.dequeue();
        }

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");    
                if (d.length >= 3) {
                    Zona z = (d.length >= 4) ? mapaZonas.get(d[3]) : null;

                    VehiculoRecolector v = new VehiculoRecolector(d[0], Double.parseDouble(d[1]), Double.parseDouble(d[2]), z);
                    v.setCargaActual(Double.parseDouble(d[2]));

                    colaVehiculos.enqueue(v);
                }
            }   
        } catch (Exception e) { System.err.println("Error cargando vehiculos: " + e.getMessage()); }
    }
    
    
    //metodo para id automatico, facilidad al usuario
    public String generarSiguienteId() {
        int maxId = 0;
        for (Residuo r : listaResiduos) {
            try {
                String parteNumerica = r.getId().split("-")[1];
                int actual = Integer.parseInt(parteNumerica);
                if (actual > maxId) maxId = actual;
            } catch (Exception e) {
                //si el id no tiene el formato esperado se ignora
            }
        }
        //retorna el siguiente numero con formato de 3 dígitos )
        return String.format("R-%03d", maxId + 1);
    }
    
    
    //copia de la cola para la interfaz
    public PriorityQueue<VehiculoRecolector> obtenerCopiaVehiculos() {
        PriorityQueue<VehiculoRecolector> copia = new PriorityQueue<>();
        List<VehiculoRecolector> listaTemporal = new ArrayList<>();

        //vaciamos la original a una lista temporal
        while (!this.colaVehiculos.isEmpty()) {
            listaTemporal.add(this.colaVehiculos.dequeue());
        }
        //se llenan ambas
        for (VehiculoRecolector v : listaTemporal) {
            this.colaVehiculos.enqueue(v); 
            copia.enqueue(v);             
        }

        return copia;
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
        colaVehiculos.enqueue(v1);
        colaVehiculos.enqueue(v2);

        // Crear Residuos Iniciales (7 atributos)
        registrarResiduo(new Residuo("R-001", "Envases Plásticos", "Plástico", 12.5, 
                         LocalDate.now(), norte, 2));

        registrarResiduo(new Residuo("R-002", "Papel Periódico", "Papel", 5.0, 
                         LocalDate.now().minusDays(1), norte, 1));

        registrarResiduo(new Residuo("R-003", "Restos de Comida", "Orgánico", 25.0, 
                         LocalDate.now(), sur, 3));

        // Inicializar algunas estadísticas en el Centro de Reciclaje
        // Esto simula que ya se ha procesado algo antes
        cReciclaje.getEstadisticasPorTipo().put("Plástico", 150.0);
        cReciclaje.getEstadisticasPorTipo().put("Papel", 80.0);
        cReciclaje.getEstadisticasPorZona().put("Sector Norte", 230.0);
    }
}
