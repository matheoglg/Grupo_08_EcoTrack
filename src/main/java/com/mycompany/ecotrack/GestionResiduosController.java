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
import javafx.scene.control.ButtonType;
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
     
        configurarIterador();
        
        if (sistema.getResiduos().isEmpty()) { //lista vacia
        actualizarVisor();
        }
        
        txtPeso.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPeso.setText(oldValue); 
            }
        });
        
        txtId.setEditable(false); //bloquea el campo para que el usuario no escriba
        txtId.setText(sistema.generarSiguienteId()); //primer id disponible
        txtId.setStyle("-fx-background-color: #eeeeee;");
    }
        

    private void configurarIterador() {
        iteradorUI = (DoublyCircularLinkedList<Residuo>.ListIterator) sistema.getResiduos().iterator();
        actualizarVisor();
    }

    // --- LÓGICA DE REGISTRO ---
    @FXML
    private void handleRegistrar() {
    
    //validacion de campos vacios
    if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || 
        txtPeso.getText().isEmpty() || cmbTipo.getValue() == null || 
        dpFecha.getValue() == null || cmbZona.getValue() == null) {
        
        mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos antes de registrar.");
        return; 
    }
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
        } catch (NumberFormatException e) { //si el peso no es un numero valido
        mostrarAlerta("Error de formato", "El peso debe ser un valor numérico válido.");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo registrar: " + e.getMessage());
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
        //valida si la lista tiene algo
        if (sistema.getResiduos().isEmpty()) {
            lblResumenId.setText("ID: ---");
            lblResumenNombre.setText("Lista vacía");
            lblResumenTipo.setText("---");
            lblResumenPeso.setText("---");
            lblResumenFecha.setText("---");
            lblResumenZona.setText("---");
            lblResumenPrioridad.setText("---");
            lblContador.setText("Residuos: 0");
            return;
        }

        Residuo r = iteradorUI.getCurrentContent();

        if (r != null) {
            lblResumenId.setText("ID: " + r.getId());
            lblResumenNombre.setText(r.getNombre());
            lblResumenTipo.setText(r.getTipo());
            lblResumenPeso.setText(r.getPeso() + " kg");

            if (r.getFechaRecoleccion() != null) {
                lblResumenFecha.setText(r.getFechaRecoleccion().toString());
            }
            if (r.getZona() != null) {
                lblResumenZona.setText(r.getZona().getNombre()); 
            } else {
                lblResumenZona.setText("Sin zona");
            }
            lblResumenPrioridad.setText("Nivel: " + r.getPrioridadAmbiental());
            lblContador.setText("Residuos: " + sistema.getResiduos().size());
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
        
    }
    
    
    @FXML
    private void handleEliminar() {
        //obtener el residuo actual
        Residuo residuoAVer = iteradorUI.getCurrentContent();

        if (residuoAVer == null) {
            mostrarAlerta("Aviso", "No hay nada para eliminar.");
            return;
        }

        //confirmacion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Eliminación");
        confirm.setHeaderText("¿Estás seguro de eliminar este registro?");
        confirm.setContentText("Se eliminará: " + residuoAVer.getNombre() + " (ID: " + residuoAVer.getId() + ")");

        //eliminar si confirma
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //borrar de la lista en memoria
                boolean exito = sistema.getResiduos().eliminar(residuoAVer);
                if (exito) {
                    if (residuoAVer.getZona() != null) {
                        int actuales = residuoAVer.getZona().getpPendiente();
                        residuoAVer.getZona().setpPendiente(Math.max(0, actuales - 1));
                    }
                    sistema.registrarCambio();
                    configurarIterador(); 
                    actualizarVisor();
                    txtId.setText(sistema.generarSiguienteId());
                    mostrarAlerta("Eliminado", "Se ha eliminado de la lista. Recuerde guardar los cambios para actualizar el archivo.");
                }
            }
        });
    }
   @FXML
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
        cmbTipo.setValue(null);
        cmbZona.setValue(null);
        sldPrioridad.setValue(1);
        txtId.setText(sistema.generarSiguienteId());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
