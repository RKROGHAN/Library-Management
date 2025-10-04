package com.library.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.library.entities.Book;
import com.library.entities.User;
import com.library.services.BookService;
import com.library.utils.AlertUtils;
import com.library.utils.ValidationUtils;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Books management
 */
public class BooksController extends BaseController implements Initializable {
    
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, Integer> availableColumn;
    @FXML private TableColumn<Book, Integer> totalColumn;
    
    @FXML private JFXTextField searchField;
    @FXML private JFXComboBox<String> categoryFilter;
    @FXML private JFXButton addBookButton;
    @FXML private JFXButton editBookButton;
    @FXML private JFXButton deleteBookButton;
    @FXML private JFXButton refreshButton;
    
    private BookService bookService;
    private ObservableList<Book> booksList;
    private FilteredList<Book> filteredBooks;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookService = new BookService();
        setupTable();
        setupEventHandlers();
        loadBooks();
        setupAnimations();
    }
    
    @Override
    protected void onUserSet() {
        // Hide admin-only features for regular users
        if (!isAdmin()) {
            addBookButton.setVisible(false);
            editBookButton.setVisible(false);
            deleteBookButton.setVisible(false);
        }
    }
    
    private void setupTable() {
        // Configure table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        
        // Initialize data
        booksList = FXCollections.observableArrayList();
        filteredBooks = new FilteredList<>(booksList);
        booksTable.setItems(filteredBooks);
        
        // Setup search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredBooks.setPredicate(book -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newVal.toLowerCase();
                return book.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                       book.getAuthor().toLowerCase().contains(lowerCaseFilter) ||
                       (book.getCategory() != null && book.getCategory().toLowerCase().contains(lowerCaseFilter));
            });
        });
        
        // Setup category filter
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            filteredBooks.setPredicate(book -> {
                if (newVal == null || newVal.isEmpty() || "All Categories".equals(newVal)) {
                    return true;
                }
                return newVal.equals(book.getCategory());
            });
        });
    }
    
    private void setupEventHandlers() {
        addBookButton.setOnAction(e -> showAddBookDialog());
        editBookButton.setOnAction(e -> showEditBookDialog());
        deleteBookButton.setOnAction(e -> deleteSelectedBook());
        refreshButton.setOnAction(e -> loadBooks());
    }
    
    private void setupAnimations() {
        // Fade in animation for the table
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), booksTable);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void loadBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            booksList.clear();
            booksList.addAll(books);
            
            // Load categories for filter
            loadCategories();
            
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load books: " + e.getMessage());
        }
    }
    
    private void loadCategories() {
        try {
            List<String> categories = bookService.getAllCategories();
            categories.add(0, "All Categories");
            categoryFilter.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            System.err.println("Failed to load categories: " + e.getMessage());
        }
    }
    
    private void showAddBookDialog() {
        // TODO: Implement add book dialog
        AlertUtils.showInfo("Add Book", "Add book functionality will be implemented here");
    }
    
    private void showEditBookDialog() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertUtils.showWarning("No Selection", "Please select a book to edit");
            return;
        }
        
        // TODO: Implement edit book dialog
        AlertUtils.showInfo("Edit Book", "Edit book functionality will be implemented here");
    }
    
    private void deleteSelectedBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertUtils.showWarning("No Selection", "Please select a book to delete");
            return;
        }
        
        if (AlertUtils.showConfirmation("Delete Book", 
                "Are you sure you want to delete '" + selectedBook.getTitle() + "'?")) {
            
            try {
                if (bookService.deleteBook(selectedBook.getId())) {
                    AlertUtils.showSuccess("Book deleted successfully");
                    loadBooks();
                } else {
                    AlertUtils.showError("Error", "Failed to delete book");
                }
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to delete book: " + e.getMessage());
            }
        }
    }
}
