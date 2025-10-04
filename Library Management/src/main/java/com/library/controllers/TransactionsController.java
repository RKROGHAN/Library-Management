package com.library.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.library.entities.Transaction;
import com.library.services.TransactionService;
import com.library.utils.AlertUtils;
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
 * Controller for Transactions management
 */
public class TransactionsController extends BaseController implements Initializable {
    
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> userNameColumn;
    @FXML private TableColumn<Transaction, String> bookTitleColumn;
    @FXML private TableColumn<Transaction, String> issueDateColumn;
    @FXML private TableColumn<Transaction, String> dueDateColumn;
    @FXML private TableColumn<Transaction, String> statusColumn;
    @FXML private TableColumn<Transaction, String> returnDateColumn;
    
    @FXML private JFXTextField searchField;
    @FXML private JFXComboBox<String> statusFilter;
    @FXML private JFXButton issueBookButton;
    @FXML private JFXButton returnBookButton;
    @FXML private JFXButton refreshButton;
    
    private TransactionService transactionService;
    private ObservableList<Transaction> transactionsList;
    private FilteredList<Transaction> filteredTransactions;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionService = new TransactionService();
        setupTable();
        setupEventHandlers();
        loadTransactions();
        setupAnimations();
    }
    
    private void setupTable() {
        // Configure table columns
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        
        // Initialize data
        transactionsList = FXCollections.observableArrayList();
        filteredTransactions = new FilteredList<>(transactionsList);
        transactionsTable.setItems(filteredTransactions);
        
        // Setup search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredTransactions.setPredicate(transaction -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newVal.toLowerCase();
                return (transaction.getUserName() != null && transaction.getUserName().toLowerCase().contains(lowerCaseFilter)) ||
                       (transaction.getBookTitle() != null && transaction.getBookTitle().toLowerCase().contains(lowerCaseFilter));
            });
        });
        
        // Setup status filter
        statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            filteredTransactions.setPredicate(transaction -> {
                if (newVal == null || newVal.isEmpty() || "All Status".equals(newVal)) {
                    return true;
                }
                return newVal.equals(transaction.getStatus().name());
            });
        });
    }
    
    private void setupEventHandlers() {
        issueBookButton.setOnAction(e -> showIssueBookDialog());
        returnBookButton.setOnAction(e -> returnSelectedBook());
        refreshButton.setOnAction(e -> loadTransactions());
    }
    
    private void setupAnimations() {
        // Fade in animation for the table
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), transactionsTable);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            transactionsList.clear();
            transactionsList.addAll(transactions);
            
            // Load status options for filter
            loadStatusOptions();
            
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load transactions: " + e.getMessage());
        }
    }
    
    private void loadStatusOptions() {
        statusFilter.setItems(FXCollections.observableArrayList(
            "All Status", "ISSUED", "RETURNED", "OVERDUE"
        ));
    }
    
    private void showIssueBookDialog() {
        // TODO: Implement issue book dialog
        AlertUtils.showInfo("Issue Book", "Issue book functionality will be implemented here");
    }
    
    private void returnSelectedBook() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            AlertUtils.showWarning("No Selection", "Please select a transaction to return");
            return;
        }
        
        if (selectedTransaction.isReturned()) {
            AlertUtils.showWarning("Already Returned", "This book has already been returned");
            return;
        }
        
        if (AlertUtils.showConfirmation("Return Book", 
                "Are you sure you want to return '" + selectedTransaction.getBookTitle() + "'?")) {
            
            try {
                if (transactionService.returnBook(selectedTransaction.getId())) {
                    AlertUtils.showSuccess("Book returned successfully");
                    loadTransactions();
                } else {
                    AlertUtils.showError("Error", "Failed to return book");
                }
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to return book: " + e.getMessage());
            }
        }
    }
}
