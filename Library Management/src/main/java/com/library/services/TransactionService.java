package com.library.services;

import com.library.db.DatabaseConnection;
import com.library.entities.Transaction;
import com.library.entities.Transaction.TransactionStatus;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Transaction operations
 * Handles all database operations related to book transactions
 */
public class TransactionService {
    private final DatabaseConnection dbConnection;
    private final BookService bookService;
    
    public TransactionService() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.bookService = new BookService();
    }
    
    /**
     * Issue a book to a user
     */
    public boolean issueBook(int userId, int bookId, LocalDate dueDate) {
        // Check if book is available
        if (!bookService.isBookAvailable(bookId)) {
            return false;
        }
        
        String sql = "INSERT INTO transactions (user_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(dueDate));
            stmt.setString(5, TransactionStatus.ISSUED.name());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update available copies
                bookService.updateAvailableCopies(bookId, -1);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error issuing book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Return a book
     */
    public boolean returnBook(int transactionId) {
        String sql = "UPDATE transactions SET return_date = ?, status = ? WHERE id = ? AND status = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setString(2, TransactionStatus.RETURNED.name());
            stmt.setInt(3, transactionId);
            stmt.setString(4, TransactionStatus.ISSUED.name());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get book ID and update available copies
                Optional<Transaction> transaction = getTransactionById(transactionId);
                if (transaction.isPresent()) {
                    bookService.updateAvailableCopies(transaction.get().getBookId(), 1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get transaction by ID
     */
    public Optional<Transaction> getTransactionById(int id) {
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE t.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Get transactions by user ID
     */
    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE t.user_id = ? ORDER BY t.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions by user ID: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Get transactions by book ID
     */
    public List<Transaction> getTransactionsByBookId(int bookId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE t.book_id = ? ORDER BY t.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions by book ID: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Get overdue transactions
     */
    public List<Transaction> getOverdueTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE t.status = ? AND t.due_date < ? ORDER BY t.due_date";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, TransactionStatus.ISSUED.name());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting overdue transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Get active transactions (issued but not returned)
     */
    public List<Transaction> getActiveTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE t.status = ? ORDER BY t.due_date";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, TransactionStatus.ISSUED.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting active transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Update overdue transactions
     */
    public void updateOverdueTransactions() {
        String sql = "UPDATE transactions SET status = ? WHERE status = ? AND due_date < ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, TransactionStatus.OVERDUE.name());
            stmt.setString(2, TransactionStatus.ISSUED.name());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating overdue transactions: " + e.getMessage());
        }
    }
    
    /**
     * Calculate fine for overdue transaction
     */
    public double calculateFine(int transactionId, double finePerDay) {
        Optional<Transaction> transaction = getTransactionById(transactionId);
        if (transaction.isPresent() && transaction.get().isOverdue()) {
            long daysOverdue = transaction.get().getDaysOverdue();
            return daysOverdue * finePerDay;
        }
        return 0.0;
    }
    
    /**
     * Search transactions
     */
    public List<Transaction> searchTransactions(String searchTerm) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u.username, b.title, b.author FROM transactions t " +
                    "LEFT JOIN users u ON t.user_id = u.id " +
                    "LEFT JOIN books b ON t.book_id = b.id " +
                    "WHERE u.username LIKE ? OR b.title LIKE ? OR b.author LIKE ? " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Map ResultSet to Transaction object
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setUserId(rs.getInt("user_id"));
        transaction.setBookId(rs.getInt("book_id"));
        
        Date issueDate = rs.getDate("issue_date");
        if (issueDate != null) {
            transaction.setIssueDate(issueDate.toLocalDate());
        }
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            transaction.setDueDate(dueDate.toLocalDate());
        }
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toLocalDate());
        }
        
        transaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
        transaction.setFineAmount(rs.getDouble("fine_amount"));
        
        // Additional fields from joins
        transaction.setUserName(rs.getString("username"));
        transaction.setBookTitle(rs.getString("title"));
        transaction.setBookAuthor(rs.getString("author"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            transaction.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            transaction.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return transaction;
    }
}
