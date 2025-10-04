package com.library.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Database initialization utility
 * Creates tables and inserts sample data
 */
public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            if (connection == null) {
                System.err.println("Failed to get database connection");
                return;
            }
            
            // Read and execute schema.sql
            String schema = readResourceFile("/sql/schema.sql");
            if (schema != null && !schema.trim().isEmpty()) {
                executeSQLScript(connection, schema);
                System.out.println("Database initialized successfully!");
            } else {
                System.err.println("Failed to read schema.sql file");
            }
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
    
    private static String readResourceFile(String resourcePath) {
        try (InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(resourcePath);
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            return content.toString();
            
        } catch (Exception e) {
            System.err.println("Error reading resource file: " + e.getMessage());
            return null;
        }
    }
    
    private static void executeSQLScript(Connection connection, String sqlScript) throws SQLException {
        // Split script by semicolon and execute each statement
        String[] statements = sqlScript.split(";");
        
        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    try {
                        statement.execute(sql);
                    } catch (SQLException e) {
                        // Log error but continue with other statements
                        System.err.println("Error executing SQL: " + sql + " - " + e.getMessage());
                    }
                }
            }
        }
    }
    
    public static void resetDatabase() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            if (connection == null) {
                System.err.println("Failed to get database connection");
                return;
            }
            
            try (Statement statement = connection.createStatement()) {
                // Drop tables in reverse order to handle foreign key constraints
                statement.execute("DROP TABLE IF EXISTS transactions");
                statement.execute("DROP TABLE IF EXISTS books");
                statement.execute("DROP TABLE IF EXISTS users");
                statement.execute("DROP DATABASE IF EXISTS library_management");
                
                System.out.println("Database reset successfully!");
                
                // Reinitialize
                initializeDatabase();
            }
            
        } catch (SQLException e) {
            System.err.println("Database reset failed: " + e.getMessage());
        }
    }
}
