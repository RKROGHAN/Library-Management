package com.library.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.library.entities.User;
import com.library.services.BookService;
import com.library.services.TransactionService;
import com.library.services.UserService;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main dashboard controller
 */
public class DashboardController implements Initializable {
    
    @FXML private AnchorPane dashboardPane;
    @FXML private JFXHamburger hamburger;
    @FXML private JFXDrawer drawer;
    @FXML private AnchorPane contentPane;
    @FXML private Label welcomeLabel;
    @FXML private Label statsLabel;
    
    // Navigation buttons
    @FXML private JFXButton booksButton;
    @FXML private JFXButton usersButton;
    @FXML private JFXButton transactionsButton;
    @FXML private JFXButton reportsButton;
    @FXML private JFXButton settingsButton;
    @FXML private JFXButton logoutButton;
    
    private User currentUser;
    private BookService bookService;
    private UserService userService;
    private TransactionService transactionService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookService = new BookService();
        userService = new UserService();
        transactionService = new TransactionService();
        
        setupDrawer();
        setupAnimations();
        setupEventHandlers();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUI();
    }
    
    private void updateUI() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            updateStats();
            
            // Hide admin-only features for regular users
            if (!currentUser.isAdmin()) {
                usersButton.setVisible(false);
                reportsButton.setVisible(false);
            }
        }
    }
    
    private void updateStats() {
        try {
            int totalBooks = bookService.getAllBooks().size();
            int availableBooks = bookService.getAvailableBooks().size();
            int totalUsers = userService.getAllUsers().size();
            int activeTransactions = transactionService.getActiveTransactions().size();
            int overdueTransactions = transactionService.getOverdueTransactions().size();
            
            String stats = String.format(
                "📚 Books: %d total, %d available | 👥 Users: %d | 📖 Active Issues: %d | ⚠️ Overdue: %d",
                totalBooks, availableBooks, totalUsers, activeTransactions, overdueTransactions
            );
            
            statsLabel.setText(stats);
        } catch (Exception e) {
            statsLabel.setText("Unable to load statistics");
        }
    }
    
    private void setupDrawer() {
        try {
            VBox drawerContent = FXMLLoader.load(getClass().getResource("/fxml/DrawerContent.fxml"));
            drawer.setSidePane(drawerContent);
        } catch (IOException e) {
            System.err.println("Failed to load drawer content: " + e.getMessage());
        }
    }
    
    private void setupAnimations() {
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), dashboardPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        // Hamburger animation
        HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburger);
        burgerTask.setRate(-1);
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, (e) -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();
            
            if (drawer.isOpened()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }
    
    private void setupEventHandlers() {
        booksButton.setOnAction(e -> loadBooksView());
        usersButton.setOnAction(e -> loadUsersView());
        transactionsButton.setOnAction(e -> loadTransactionsView());
        reportsButton.setOnAction(e -> loadReportsView());
        settingsButton.setOnAction(e -> loadSettingsView());
        logoutButton.setOnAction(e -> handleLogout());
    }
    
    @FXML
    private void loadBooksView() {
        loadContent("/fxml/BooksView.fxml");
        drawer.close();
    }
    
    @FXML
    private void loadUsersView() {
        if (currentUser != null && currentUser.isAdmin()) {
            loadContent("/fxml/UsersView.fxml");
            drawer.close();
        }
    }
    
    @FXML
    private void loadTransactionsView() {
        loadContent("/fxml/TransactionsView.fxml");
        drawer.close();
    }
    
    @FXML
    private void loadReportsView() {
        if (currentUser != null && currentUser.isAdmin()) {
            loadContent("/fxml/ReportsView.fxml");
            drawer.close();
        }
    }
    
    @FXML
    private void loadSettingsView() {
        loadContent("/fxml/SettingsView.fxml");
        drawer.close();
    }
    
    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane content = loader.load();
            
            // Pass current user to the loaded controller
            if (loader.getController() instanceof BaseController) {
                ((BaseController) loader.getController()).setCurrentUser(currentUser);
            }
            
            // Clear content pane and add new content
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
            
            // Set anchor constraints
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            
            // Fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), content);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            
        } catch (IOException e) {
            System.err.println("Failed to load content: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            javafx.stage.Stage stage = (javafx.stage.Stage) dashboardPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Library Management System - Login");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            System.err.println("Failed to logout: " + e.getMessage());
        }
    }
}
