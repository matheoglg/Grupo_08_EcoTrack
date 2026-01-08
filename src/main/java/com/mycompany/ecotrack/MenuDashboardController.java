/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author Hogar
 */
public class MenuDashboardController {
    @FXML private Button btnResiduos;
    @FXML private Button btnLogistica;
    @FXML private Button btnEstadisticas;

    
    @FXML
    private void goToResiduos(ActionEvent event) {
        cambiarEscena("residuos_gestion.fxml", "EcoTrack - Gestión de Residuos");
    }

    @FXML
    private void goToLogistica(ActionEvent event) {
        cambiarEscena("logistica.fxml", "EcoTrack - Rutas de Recolección");
    }

    @FXML
    private void goToEstadisticas(ActionEvent event) {
        cambiarEscena("estadisticas.fxml", "EcoTrack - Centro de Reciclaje");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        cambiarEscena("login.fxml", "EcoTrack - Iniciar Sesión");
    }

    
    // Método para cambiar de pantalla sin repetir código en los otros métodos
     
    private void cambiarEscena(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            
            // Obtenemos el Stage actual desde cualquier botón
            Stage stage = (Stage) btnResiduos.getScene().getWindow();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Navegación", "No se pudo cargar la pantalla: " + fxml);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
