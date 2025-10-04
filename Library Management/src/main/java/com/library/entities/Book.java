package com.library.entities;

import java.time.LocalDateTime;

/**
 * Book entity class
 * Represents a book in the library management system
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String publisher;
    private Integer publicationYear;
    private int availableCopies;
    private int totalCopies;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public Book() {}
    
    // Constructor with basic fields
    public Book(String title, String author, String category, int totalCopies) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    // Full constructor
    public Book(int id, String title, String author, String isbn, String category,
                String publisher, Integer publicationYear, int availableCopies,
                int totalCopies, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.description = description;
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public int getAvailableCopies() {
        return availableCopies;
    }
    
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public int getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public boolean isAvailable() {
        return availableCopies > 0;
    }
    
    public void incrementAvailableCopies() {
        this.availableCopies++;
    }
    
    public void decrementAvailableCopies() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
        }
    }
    
    public void updateTotalCopies(int newTotal) {
        int difference = newTotal - this.totalCopies;
        this.totalCopies = newTotal;
        this.availableCopies += difference;
        if (this.availableCopies < 0) {
            this.availableCopies = 0;
        }
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return id == book.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
