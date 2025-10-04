package com.library.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager for application settings
 */
public class ConfigManager {
    
    private static final String CONFIG_FILE = "library.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    /**
     * Load properties from file
     */
    private static void loadProperties() {
        properties = new Properties();
        
        // Default properties
        properties.setProperty("app.name", "Library Management System");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("app.theme", "light");
        properties.setProperty("app.language", "en");
        
        // Database properties
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/library_management");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "password");
        
        // Library settings
        properties.setProperty("library.max_issue_days", "14");
        properties.setProperty("library.fine_per_day", "1.00");
        properties.setProperty("library.max_books_per_user", "5");
        
        // Notification settings
        properties.setProperty("notifications.email_enabled", "false");
        properties.setProperty("notifications.overdue_alerts", "true");
        properties.setProperty("notifications.system_updates", "true");
        
        // Try to load from file
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            // File doesn't exist or can't be read, use defaults
            System.out.println("Using default configuration");
        }
    }
    
    /**
     * Save properties to file
     */
    public static void saveProperties() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Library Management System Configuration");
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    /**
     * Get property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set property value
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Get integer property
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get double property
     */
    public static double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }
    
    /**
     * Set integer property
     */
    public static void setIntProperty(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Set double property
     */
    public static void setDoubleProperty(String key, double value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Set boolean property
     */
    public static void setBooleanProperty(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }
    
    /**
     * Get application name
     */
    public static String getAppName() {
        return getProperty("app.name");
    }
    
    /**
     * Get application version
     */
    public static String getAppVersion() {
        return getProperty("app.version");
    }
    
    /**
     * Get current theme
     */
    public static String getTheme() {
        return getProperty("app.theme", "light");
    }
    
    /**
     * Set theme
     */
    public static void setTheme(String theme) {
        setProperty("app.theme", theme);
    }
    
    /**
     * Get maximum issue days
     */
    public static int getMaxIssueDays() {
        return getIntProperty("library.max_issue_days", 14);
    }
    
    /**
     * Get fine per day
     */
    public static double getFinePerDay() {
        return getDoubleProperty("library.fine_per_day", 1.00);
    }
    
    /**
     * Get maximum books per user
     */
    public static int getMaxBooksPerUser() {
        return getIntProperty("library.max_books_per_user", 5);
    }
    
    /**
     * Check if email notifications are enabled
     */
    public static boolean isEmailNotificationsEnabled() {
        return getBooleanProperty("notifications.email_enabled", false);
    }
    
    /**
     * Check if overdue alerts are enabled
     */
    public static boolean isOverdueAlertsEnabled() {
        return getBooleanProperty("notifications.overdue_alerts", true);
    }
    
    /**
     * Check if system updates are enabled
     */
    public static boolean isSystemUpdatesEnabled() {
        return getBooleanProperty("notifications.system_updates", true);
    }
}
