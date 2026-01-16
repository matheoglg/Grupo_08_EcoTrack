/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import estructuras.LinkedList;
import estructuras.PriorityQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelos.VehiculoRecolector;
import modelos.Zona;
import sistema.SistemaEcoTrack;

/**
 *
 * @author Hogar
 */
public class GestionRutasController {
    @FXML private TableView<VehiculoRecolector> tblVehiculos;
    @FXML private TableColumn<VehiculoRecolector, String> colIdVehiculo;
    @FXML private TableColumn<VehiculoRecolector, Double> colCapacidad;
    @FXML private TableColumn<VehiculoRecolector, String> colEstado;
    @FXML private ListView<String> lstZonasCriticas;
    @FXML private Label lblInfoDespacho;

    private SistemaEcoTrack sistema = SistemaEcoTrack.getInstancia();

    @FXML
    public void initialize() {
        // Configurar cómo se mostrarán los datos en las columnas de la tabla
        colIdVehiculo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capMax"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("cargaActual"));
        colEstado.setText("Carga Actual (kg)");
        actualizarTablas();
    }

    private void actualizarTablas() {
        List<VehiculoRecolector> listaTemporal = new ArrayList<>();
        
        if (sistema.getVehiculos() != null) {
            PriorityQueue<VehiculoRecolector> copy = sistema.obtenerCopiaVehiculos(); 
            while (!copy.isEmpty()) {
                listaTemporal.add(copy.dequeue());
            }
        }
        tblVehiculos.setItems(FXCollections.observableArrayList(listaTemporal));

        //zonas criticas
        lstZonasCriticas.getItems().clear();
        for (Zona z : sistema.getMapaZonas().values()) {
            double impacto = z.calcularUtilidad(); // Menor utilidad = mayor impacto/prioridad
            lstZonasCriticas.getItems().add(z.getNombre() + " - Nivel de Urgencia: " + String.format("%.2f", impacto));
        }
    }

    @FXML
    private void handleAnalizar(ActionEvent event) {
        // Buscar la zona con menor utilidad (más llena de residuos)
        String zonaCritica = sistema.obtenerZonaMasCritica();
        lblInfoDespacho.setText("Zona crítica identificada: " + zonaCritica);
    }

    @FXML
    private void handleDespachar(ActionEvent event) {
        if (sistema.getVehiculos().isEmpty()) {
            lblInfoDespacho.setText("No hay vehículos en la cola de prioridad.");
            return;
        }

        // Ejecutar despacho automático basado en la prioridad del volumen/impacto
        sistema.despacharVehiculo();
        
        lblInfoDespacho.setText("Vehículo despachado exitosamente.");
        actualizarTablas();
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MenuDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblInfoDespacho.setText("Error al regresar: " + e.getMessage());
        }
    }}
