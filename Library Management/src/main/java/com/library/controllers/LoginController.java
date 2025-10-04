package com.library.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.library.entities.User;
import com.library.services.UserService;
import com.library.utils.AlertUtils;
import com.library.utils.ValidationUtils;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Login screen
 */
public class LoginController implements Initializable {
    
    @FXML private AnchorPane loginPane;
    @FXML private VBox loginForm;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXButton loginButton;
    @FXML private JFXButton exitButton;
    @FXML private Label errorLabel;
    
    private UserService userService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        setupAnimations();
        setupEventHandlers();
    }
    
    private void setupAnimations() {
        // Fade in animation for the login form
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), loginForm);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        // Slide up animation for the login form
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(1000), loginForm);
        slideUp.setFromY(50);
        slideUp.setToY(0);
        slideUp.play();
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.setOnAction(e -> handleLogin());
        
        // Exit button action
        exitButton.setOnAction(e -> System.exit(0));
        
        // Enter key handling
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());
        
        // Clear error label when typing
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Clear previous errors
        clearError();
        
        // Validate input
        if (!ValidationUtils.isNotEmpty(username)) {
            showError("Please enter a username");
            return;
        }
        
        if (!ValidationUtils.isNotEmpty(password)) {
            showError("Please enter a password");
            return;
        }
        
        // Authenticate user
        Optional<User> user = userService.authenticate(username, password);
        
        if (user.isPresent()) {
            // Login successful - navigate to dashboard
            navigateToDashboard(user.get());
        } else {
            showError("Invalid username or password");
        }
    }
    
    private void navigateToDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Pass user data to dashboard controller
            DashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(user);
            
            // Apply CSS
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Library Management System - Dashboard");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            AlertUtils.showError("Navigation Error", "Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        
        // Shake animation for error
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), loginForm);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }
    
    private void clearError() {
        errorLabel.setVisible(false);
    }
}
