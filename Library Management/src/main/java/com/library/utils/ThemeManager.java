package com.library.utils;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Theme management utility class
 * Handles theme switching and styling
 */
public class ThemeManager {
    
    public enum Theme {
        LIGHT, DARK, AUTO
    }
    
    private static Theme currentTheme = Theme.LIGHT;
    
    /**
     * Set the current theme
     */
    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }
    
    /**
     * Get the current theme
     */
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
    
    /**
     * Apply theme to a scene
     */
    public static void applyTheme(Scene scene, Theme theme) {
        if (scene == null) return;
        
        // Remove existing theme classes
        scene.getRoot().getStyleClass().removeAll("light-theme", "dark-theme", "auto-theme");
        
        // Add new theme class
        switch (theme) {
            case LIGHT:
                scene.getRoot().getStyleClass().add("light-theme");
                break;
            case DARK:
                scene.getRoot().getStyleClass().add("dark-theme");
                break;
            case AUTO:
                scene.getRoot().getStyleClass().add("auto-theme");
                break;
        }
        
        currentTheme = theme;
    }
    
    /**
     * Toggle between light and dark themes
     */
    public static void toggleTheme(Scene scene) {
        if (currentTheme == Theme.LIGHT) {
            applyTheme(scene, Theme.DARK);
        } else {
            applyTheme(scene, Theme.LIGHT);
        }
    }
    
    /**
     * Apply dark theme styles
     */
    public static void applyDarkTheme(Scene scene) {
        if (scene == null) return;
        
        // Add dark theme styles
        scene.getStylesheets().add(ThemeManager.class.getResource("/css/dark-theme.css").toExternalForm());
    }
    
    /**
     * Remove dark theme styles
     */
    public static void removeDarkTheme(Scene scene) {
        if (scene == null) return;
        
        // Remove dark theme styles
        scene.getStylesheets().remove(ThemeManager.class.getResource("/css/dark-theme.css").toExternalForm());
    }
    
    /**
     * Check if dark theme is active
     */
    public static boolean isDarkTheme() {
        return currentTheme == Theme.DARK;
    }
    
    /**
     * Get theme-specific color
     */
    public static String getThemeColor(String colorName) {
        switch (currentTheme) {
            case DARK:
                return getDarkThemeColor(colorName);
            case LIGHT:
            default:
                return getLightThemeColor(colorName);
        }
    }
    
    private static String getLightThemeColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "primary":
                return "#3498db";
            case "secondary":
                return "#2c3e50";
            case "background":
                return "#ffffff";
            case "surface":
                return "#f8f9fa";
            case "text":
                return "#2c3e50";
            case "text-secondary":
                return "#7f8c8d";
            default:
                return "#000000";
        }
    }
    
    private static String getDarkThemeColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "primary":
                return "#3498db";
            case "secondary":
                return "#34495e";
            case "background":
                return "#2c3e50";
            case "surface":
                return "#34495e";
            case "text":
                return "#ecf0f1";
            case "text-secondary":
                return "#bdc3c7";
            default:
                return "#ffffff";
        }
    }
}
