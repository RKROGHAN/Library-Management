package com.library.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.library.entities.User;
import com.library.services.UserService;
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
 * Controller for Users management
 */
public class UsersController extends BaseController implements Initializable {
    
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    
    @FXML private JFXTextField searchField;
    @FXML private JFXButton addUserButton;
    @FXML private JFXButton editUserButton;
    @FXML private JFXButton deleteUserButton;
    @FXML private JFXButton refreshButton;
    
    private UserService userService;
    private ObservableList<User> usersList;
    private FilteredList<User> filteredUsers;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        setupTable();
        setupEventHandlers();
        loadUsers();
        setupAnimations();
    }
    
    @Override
    protected void onUserSet() {
        // Only admin can manage users
        if (!isAdmin()) {
            addUserButton.setVisible(false);
            editUserButton.setVisible(false);
            deleteUserButton.setVisible(false);
        }
    }
    
    private void setupTable() {
        // Configure table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        // Initialize data
        usersList = FXCollections.observableArrayList();
        filteredUsers = new FilteredList<>(usersList);
        usersTable.setItems(filteredUsers);
        
        // Setup search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredUsers.setPredicate(user -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newVal.toLowerCase();
                return user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                       (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerCaseFilter));
            });
        });
    }
    
    private void setupEventHandlers() {
        addUserButton.setOnAction(e -> showAddUserDialog());
        editUserButton.setOnAction(e -> showEditUserDialog());
        deleteUserButton.setOnAction(e -> deleteSelectedUser());
        refreshButton.setOnAction(e -> loadUsers());
    }
    
    private void setupAnimations() {
        // Fade in animation for the table
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), usersTable);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();
            usersList.clear();
            usersList.addAll(users);
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load users: " + e.getMessage());
        }
    }
    
    private void showAddUserDialog() {
        // TODO: Implement add user dialog
        AlertUtils.showInfo("Add User", "Add user functionality will be implemented here");
    }
    
    private void showEditUserDialog() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showWarning("No Selection", "Please select a user to edit");
            return;
        }
        
        // TODO: Implement edit user dialog
        AlertUtils.showInfo("Edit User", "Edit user functionality will be implemented here");
    }
    
    private void deleteSelectedUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showWarning("No Selection", "Please select a user to delete");
            return;
        }
        
        // Prevent deleting the current user
        if (currentUser != null && currentUser.getId() == selectedUser.getId()) {
            AlertUtils.showWarning("Cannot Delete", "You cannot delete your own account");
            return;
        }
        
        if (AlertUtils.showConfirmation("Delete User", 
                "Are you sure you want to delete user '" + selectedUser.getUsername() + "'?")) {
            
            try {
                if (userService.deleteUser(selectedUser.getId())) {
                    AlertUtils.showSuccess("User deleted successfully");
                    loadUsers();
                } else {
                    AlertUtils.showError("Error", "Failed to delete user");
                }
            } catch (Exception e) {
                AlertUtils.showError("Error", "Failed to delete user: " + e.getMessage());
            }
        }
    }
}
