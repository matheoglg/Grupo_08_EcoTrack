/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import estructuras.DoublyCircularLinkedList;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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

    private DoublyCircularLinkedList<Residuo>.ListIterator iteradorUI;
    private SistemaEcoTrack sistema = SistemaEcoTrack.getInstancia();

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList("Plástico", "Papel", "Orgánico", "Vidrio", "Metal"));
        
        cmbZona.setItems(FXCollections.observableArrayList(sistema.getMapaZonas().values()));
        
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
        Residuo actual = iteradorUI.getCurrentContent();
        if (actual != null) {
            lblResumenId.setText("ID: " + actual.getId());
            lblResumenNombre.setText("Nombre: " + actual.getNombre());
            lblResumenTipo.setText("Tipo: " + actual.getTipo());
            lblResumenPrioridad.setText("Prioridad: " + actual.getPrioridadAmbiental());
            
            int total = sistema.getResiduos().size();
            lblContador.setText("Explorando lista circular (" + total + " registros)");
        }
    }

    @FXML
    private void handleGuardar() {
        sistema.guardarDatos();
        mostrarAlerta("Guardado", "Los datos se han persistido en los archivos .txt");
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
