package com.library.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

/**
 * Utility class for showing alerts and dialogs
 */
public class AlertUtils {
    
    /**
     * Show information alert
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    
    /**
     * Show error alert
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    
    /**
     * Show warning alert
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    
    /**
     * Show confirmation dialog
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UTILITY);
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    /**
     * Show success message
     */
    public static void showSuccess(String message) {
        showInfo("Success", message);
    }
    
    /**
     * Show validation error
     */
    public static void showValidationError(String message) {
        showError("Validation Error", message);
    }
    
    /**
     * Show database error
     */
    public static void showDatabaseError(String message) {
        showError("Database Error", message);
    }
}
