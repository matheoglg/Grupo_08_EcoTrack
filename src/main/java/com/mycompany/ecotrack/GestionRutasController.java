/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import estructuras.LinkedList;
import estructuras.PriorityQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
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
    private void handleAgregarVehiculo(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Vehículo");
        dialog.setHeaderText("Registro de Vehículo Recolector");

        ButtonType btnAgregar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAgregar, ButtonType.CANCEL);

        // Layout del diálogo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtId = new TextField();
        txtId.setPromptText("V-10X");
        TextField txtCap = new TextField();
        txtCap.setPromptText("Capacidad en kg");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Capacidad Máxima:"), 0, 1);
        grid.add(txtCap, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == btnAgregar) {
                try {
                    String id = txtId.getText().trim();
                    double cap = Double.parseDouble(txtCap.getText().trim());

                    if (!id.isEmpty() && cap > 0) {
                        // Evitar duplicados antes de agregar
                        VehiculoRecolector nuevo = new VehiculoRecolector(id, cap);
                        sistema.agregarVehiculo(nuevo);
                        sistema.registrarCambio(); // Marcar para guardar en archivo

                        actualizarTablas(); // Refrescar la UI
                        lblInfoDespacho.setText("Vehículo " + id + " añadido a la flota.");
                    }
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "La capacidad debe ser un número válido.");
                }
            }
        });
    }

    
        
    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MenuDashboard.fxml"));
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
