package com.library.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transaction entity class
 * Represents a book transaction (issue/return) in the library management system
 */
public class Transaction {
    private int id;
    private int userId;
    private int bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
    private double fineAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for display purposes
    private String userName;
    private String bookTitle;
    private String bookAuthor;
    
    public enum TransactionStatus {
        ISSUED, RETURNED, OVERDUE
    }
    
    // Default constructor
    public Transaction() {}
    
    // Constructor for new transaction
    public Transaction(int userId, int bookId, LocalDate issueDate, LocalDate dueDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = TransactionStatus.ISSUED;
        this.fineAmount = 0.0;
    }
    
    // Full constructor
    public Transaction(int id, int userId, int bookId, LocalDate issueDate, LocalDate dueDate,
                      LocalDate returnDate, TransactionStatus status, double fineAmount,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getBookAuthor() {
        return bookAuthor;
    }
    
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    
    public boolean isOverdue() {
        return status == TransactionStatus.OVERDUE || 
               (status == TransactionStatus.ISSUED && LocalDate.now().isAfter(dueDate));
    }
    
    public boolean isReturned() {
        return status == TransactionStatus.RETURNED;
    }
    
    public boolean isIssued() {
        return status == TransactionStatus.ISSUED;
    }
    
    public void markAsReturned() {
        this.status = TransactionStatus.RETURNED;
        this.returnDate = LocalDate.now();
    }
    
    public void markAsOverdue() {
        this.status = TransactionStatus.OVERDUE;
    }
    
    public long getDaysOverdue() {
        if (isOverdue()) {
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }
    
    public void calculateFine(double finePerDay) {
        if (isOverdue()) {
            long daysOverdue = getDaysOverdue();
            this.fineAmount = daysOverdue * finePerDay;
        }
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
