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
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistema.SistemaEcoTrack;

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
    
    @FXML private Label lblTitulo; 
    @FXML private Hyperlink linkRegistro;
    
    private boolean modoRegistro = false;

    @FXML
    private void validarLogin(ActionEvent event) {
        String user = campoUsuario.getText().trim();
        String password = campoContrasena.getText().trim();

        // Validación simple para testear si funciona
        /*if (user.equals("admin") && password.equals("1234")) {
            irAlDashboard();
            
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Inicio de Sesión", 
                          "Usuario o contraseña incorrectos.");
        }*/
        
        if (user.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor completa todos los campos.");
            return;
        }
        if (modoRegistro) {
            boolean exito = SistemaEcoTrack.getInstancia().registrarUsuario(user, password);
            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "Usuario creado. Ahora puedes iniciar sesión.");
                alternarModo(); 
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "El nombre de usuario ya está en uso.");
            }
        } else {
            if (SistemaEcoTrack.getInstancia().validarLogin(user, password)) {
                irAlDashboard();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Inicio de Sesión", 
                              "Usuario o contraseña incorrectos.");
            }
        }
    }
    
    @FXML
    private void alternarModo() {
        modoRegistro = !modoRegistro;
        if (modoRegistro) {
            lblTitulo.setText("Crear Cuenta Nueva");
            botonLogin.setText("Registrarse");
            linkRegistro.setText("¿Ya tienes cuenta? Inicia sesión aquí");
        } else {
            lblTitulo.setText("Iniciar Sesión");
            botonLogin.setText("Ingresar");
            linkRegistro.setText("¿No tienes cuenta? Regístrate aquí");
        }
        campoUsuario.clear();
        campoContrasena.clear();
    }
    
    
    private void irAlDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuDashboard.fxml")); 
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) botonLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EcoTrack");
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
