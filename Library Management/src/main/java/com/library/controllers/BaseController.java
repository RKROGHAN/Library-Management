package com.library.controllers;

import com.library.entities.User;

/**
 * Base controller class that provides common functionality
 * for all controllers in the application
 */
public abstract class BaseController {
    
    protected User currentUser;
    
    /**
     * Set the current user for this controller
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        onUserSet();
    }
    
    /**
     * Get the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Called when a user is set - override in subclasses
     * to perform initialization based on user data
     */
    protected void onUserSet() {
        // Override in subclasses if needed
    }
    
    /**
     * Check if current user is admin
     */
    protected boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Check if current user is regular user
     */
    protected boolean isUser() {
        return currentUser != null && !currentUser.isAdmin();
    }
}
