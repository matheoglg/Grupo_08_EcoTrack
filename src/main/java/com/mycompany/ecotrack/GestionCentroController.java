/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import modelos.CentroReciclaje;
import modelos.Residuo;
import sistema.SistemaEcoTrack;
/**
 *
 * @author Hogar
 */
public class GestionCentroController {
    @FXML private ListView<String> lstPilaResiduos;
    @FXML private Label lblEstadoPila;
    
    // Tablas de Estadísticas
    @FXML private TableView<Map.Entry<String, Double>> tblEstadisticasTipo;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colTipo;
    @FXML private TableColumn<Map.Entry<String, Double>, Double> colPesoTipo;

    @FXML private TableView<Map.Entry<String, Double>> tblEstadisticasZona;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colZona;
    @FXML private TableColumn<Map.Entry<String, Double>, Double> colPesoZona;
    
    private SistemaEcoTrack sistema = SistemaEcoTrack.getInstancia();
    private CentroReciclaje centro = sistema.getCentroReciclaje();
    
    @FXML
    public void initialize() {
        configurarTablas();
        actualizarUI();
    }

    private void configurarTablas() {
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        colPesoTipo.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValue()).asObject());

        colZona.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        colPesoZona.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValue()).asObject());
    }

    private void actualizarUI() {
        // Visualizar la pila
        List<String> vistaPila = new ArrayList<>();
        
        for (Residuo r : centro.getPilaResiduos()) {
            vistaPila.add(0, "ID: " + r.getId() + " - " + r.getNombre() + " (" + r.getPeso() + "kg)");
        }
        lstPilaResiduos.setItems(FXCollections.observableArrayList(vistaPila));
        lblEstadoPila.setText("Residuos en espera: " + vistaPila.size());

        // Cargar estadisticas
        ObservableList<Map.Entry<String, Double>> itemsTipo = 
            FXCollections.observableArrayList(centro.getEstadisticasPorTipo().entrySet());
        tblEstadisticasTipo.setItems(itemsTipo);

        ObservableList<Map.Entry<String, Double>> itemsZona = 
            FXCollections.observableArrayList(centro.getEstadisticasPorZona().entrySet());
        tblEstadisticasZona.setItems(itemsZona);
        
        // Refrescar tablas por si hubo cambios de peso
        tblEstadisticasTipo.refresh();
        tblEstadisticasZona.refresh();
    }

    @FXML
    private void handleProcesar(ActionEvent event) {
        if (centro.getPilaResiduos().isEmpty()) {
            mostrarAlerta("Información", "No hay residuos en la pila para procesar.");
            return;
        }

        // Simula la extracción de la pila y actualización de Mapas
        centro.procesarResiduo();
        actualizarUI();
    }

    @FXML
    private void handleImprimir(ActionEvent event) {
        sistema.guardarEstadisticas();
    }
    
    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
