/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ecotrack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        // Validación simple para pruebas
        if (user.equals("admin") && password.equals("1234")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Login Exitoso", 
                          "Bienvenido a EcoTrack");
            
            // Aquí iría la lógica para cambiar a la pantalla de Dashboard
            // irADashboard();
            
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Inicio de Sesión", 
                          "Usuario o contraseña incorrectos.");
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
