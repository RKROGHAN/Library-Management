package com.library.services;

import com.library.db.DatabaseConnection;
import com.library.entities.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Book operations
 * Handles all database operations related to books
 */
public class BookService {
    private final DatabaseConnection dbConnection;
    
    public BookService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Create a new book
     */
    public boolean createBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, category, publisher, publication_year, " +
                    "available_copies, total_copies, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setString(5, book.getPublisher());
            stmt.setObject(6, book.getPublicationYear());
            stmt.setInt(7, book.getAvailableCopies());
            stmt.setInt(8, book.getTotalCopies());
            stmt.setString(9, book.getDescription());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get book by ID
     */
    public Optional<Book> getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Get available books (with available copies > 0)
     */
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE available_copies > 0 ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Update book
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, category = ?, " +
                    "publisher = ?, publication_year = ?, available_copies = ?, total_copies = ?, " +
                    "description = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setString(5, book.getPublisher());
            stmt.setObject(6, book.getPublicationYear());
            stmt.setInt(7, book.getAvailableCopies());
            stmt.setInt(8, book.getTotalCopies());
            stmt.setString(9, book.getDescription());
            stmt.setInt(10, book.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete book
     */
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Search books by title, author, or category
     */
    public List<Book> searchBooks(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR category LIKE ? ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Get books by category
     */
    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category = ? ORDER BY title";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by category: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Get all unique categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM books WHERE category IS NOT NULL ORDER BY category";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    /**
     * Update available copies when book is issued/returned
     */
    public boolean updateAvailableCopies(int bookId, int change) {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, change);
            stmt.setInt(2, bookId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available copies: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check if book is available
     */
    public boolean isBookAvailable(int bookId) {
        String sql = "SELECT available_copies FROM books WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_copies") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking book availability: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Book object
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setCategory(rs.getString("category"));
        book.setPublisher(rs.getString("publisher"));
        
        Integer pubYear = rs.getObject("publication_year", Integer.class);
        book.setPublicationYear(pubYear);
        
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setDescription(rs.getString("description"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            book.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            book.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return book;
    }
}
