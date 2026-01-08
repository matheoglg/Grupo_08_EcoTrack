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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Hogar
 */
public class LoginController {
    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private Button botonLogin;

    @FXML
    private void validarLogin(ActionEvent event) {
        String user = campoUsuario.getText();
        String password = campoContrasena.getText();

        // Validación simple para testear si funciona
        if (user.equals("admin") && password.equals("1234")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Login Exitoso", 
                          "Bienvenido a EcoTrack");
            
            irAlDashboard();
            
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Inicio de Sesión", 
                          "Usuario o contraseña incorrectos.");
        }
    }
    
    private void irAlDashboard() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) botonLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EcoTrack - Panel Principal");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sistema", 
                          "No se pudo cargar la pantalla del Dashboard.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
