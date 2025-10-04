package com.library.application;

import com.library.db.DatabaseInitializer;
import com.library.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main application class for Library Management System
 */
public class LibraryApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseInitializer.initializeDatabase();
            
            // Load login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Apply CSS
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            // Set up stage
            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            
            // Set application icon
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/library-icon.png")));
            } catch (Exception e) {
                System.out.println("Icon not found, using default");
            }
            
            // Center the stage
            primaryStage.centerOnScreen();
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        // Clean up resources
        System.out.println("Application shutting down...");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
