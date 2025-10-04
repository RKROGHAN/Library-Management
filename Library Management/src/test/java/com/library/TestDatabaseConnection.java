package com.library;

import com.library.db.DatabaseConnection;
import com.library.db.DatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for database connection and initialization
 */
public class TestDatabaseConnection {
    
    private DatabaseConnection dbConnection;
    
    @BeforeEach
    void setUp() {
        dbConnection = DatabaseConnection.getInstance();
    }
    
    @Test
    void testDatabaseConnection() {
        assertNotNull(dbConnection, "Database connection should not be null");
        assertTrue(dbConnection.testConnection(), "Database connection should be valid");
    }
    
    @Test
    void testDatabaseInitialization() {
        // This test assumes MySQL is running and accessible
        // In a real scenario, you might want to use an in-memory database for testing
        assertDoesNotThrow(() -> {
            DatabaseInitializer.initializeDatabase();
        }, "Database initialization should not throw exceptions");
    }
    
    @Test
    void testConnectionSingleton() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        assertEquals(instance1, instance2, "Database connection should be singleton");
    }
}
