/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import estructuras.DoublyCircularLinkedList;
import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelos.Residuo;
import modelos.Zona;
import sistema.SistemaEcoTrack;

/**
 *
 * @author Hogar
 */
public class GestionResiduosController {
    // Formulario para residuos
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtPeso;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Zona> cmbZona;
    @FXML private Slider sldPrioridad;

    // Visor de Residuos
    @FXML private Label lblResumenId;
    @FXML private Label lblResumenNombre;
    @FXML private Label lblResumenTipo;
    @FXML private Label lblResumenPrioridad;
    @FXML private Label lblContador;
    @FXML private ComboBox<String> cmbOrden;
    @FXML private Label lblResumenPeso, lblResumenFecha, lblResumenZona;

    private DoublyCircularLinkedList<Residuo>.ListIterator iteradorUI;
    private SistemaEcoTrack sistema = SistemaEcoTrack.getInstancia();

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList("Plástico", "Papel", "Orgánico", "Vidrio", "Metal"));
        
        cmbZona.setItems(FXCollections.observableArrayList(sistema.getMapaZonas().values()));
        
        cmbOrden.setItems(FXCollections.observableArrayList("Prioridad", "Peso", "Tipo"));
        cmbOrden.setValue("Prioridad");
        // Configurar el Iterador al iniciar
        configurarIterador();
    }

    private void configurarIterador() {
        iteradorUI = (DoublyCircularLinkedList<Residuo>.ListIterator) sistema.getResiduos().iterator();
        actualizarVisor();
    }

    // --- LÓGICA DE REGISTRO ---
    @FXML
    private void handleRegistrar() {
        try {
            // Extraer datos
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String tipo = cmbTipo.getValue();
            double peso = Double.parseDouble(txtPeso.getText());
            LocalDate fecha = dpFecha.getValue();
            Zona zona = cmbZona.getValue();
            int prioridad = (int) sldPrioridad.getValue();

            // Crear y registrar el residuo
            Residuo nuevo = new Residuo(id, nombre, tipo, peso, fecha, zona, prioridad);
            sistema.registrarResiduo(nuevo);

            // Limpiar campos y refrescar visor
            limpiarCampos();
            configurarIterador(); 
            
            mostrarAlerta("Éxito", "Residuo registrado correctamente.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Verifique que todos los campos sean correctos.");
        }
    }

    // NAVEGACIÓN
    @FXML
    private void handleSiguiente() {
        if (iteradorUI != null) {
            iteradorUI.forward(); 
            actualizarVisor();
        }
    }

    @FXML
    private void handleAnterior() {
        if (iteradorUI != null) {
            iteradorUI.backward(); 
            actualizarVisor();
        }
    }

    private void actualizarVisor() {
        Residuo r = iteradorUI.getCurrentContent();
        if (r != null) {
            lblResumenId.setText("ID: " + r.getId());
            lblResumenNombre.setText("Nombre: " + r.getNombre());
            lblResumenTipo.setText("Tipo: " + r.getTipo());
            lblResumenPeso.setText("Peso: " + r.getPeso() + " kg");
            lblResumenFecha.setText("Fecha: " + r.getFechaRecoleccion().toString());
            lblResumenZona.setText("Zona: " + r.getZona().getNombre());
            lblResumenPrioridad.setText("Prioridad: " + r.getPrioridadAmbiental());
        }
    }

    @FXML
    private void handleCambiarOrden() {
        String opcion = cmbOrden.getValue();
        if (opcion.equals("Peso")) {
            sistema.getResiduos().ordenar(Residuo.PorPeso);
        } else if (opcion.equals("Tipo")) {
            sistema.getResiduos().ordenar(Residuo.PorTipo);
        } else {
            sistema.getResiduos().ordenar(Residuo.PorPrioridad);
        }

        // IMPORTANTE: Después de ordenar, reinicia el iterador
        configurarIterador(); 
    }
    @FXML
    private void handleGuardar(ActionEvent event) {
        if (!sistema.hayCambios()) {
            mostrarAlerta("Sin cambios", "No se detectaron nuevos residuos para guardar.");
            return;
        }        
        sistema.guardarDatos();
        mostrarAlerta("Guardado", "Los datos se han guardado correctamente");
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la pantalla principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void irAlMenu(ActionEvent event) {
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

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtPeso.clear();
        dpFecha.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
