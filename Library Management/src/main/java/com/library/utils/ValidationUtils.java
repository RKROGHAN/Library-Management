package com.library.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern ISBN_PATTERN = Pattern.compile(
        "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    );
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.replaceAll("[\\s-()]", "")).matches();
    }
    
    /**
     * Validate ISBN format
     */
    public static boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return true; // ISBN is optional
        }
        return ISBN_PATTERN.matcher(isbn.trim()).matches();
    }
    
    /**
     * Validate username (alphanumeric and underscore only, 3-20 characters)
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        String trimmed = username.trim();
        return trimmed.length() >= 3 && trimmed.length() <= 20 && 
               trimmed.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Validate password (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Validate required field
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(String value) {
        try {
            int num = Integer.parseInt(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }
    
    /**
     * Validate year (1900 to current year + 1)
     */
    public static boolean isValidYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year >= 1900 && year <= currentYear + 1;
    }
    
    /**
     * Validate title (non-empty, reasonable length)
     */
    public static boolean isValidTitle(String title) {
        return isNotEmpty(title) && title.trim().length() <= 255;
    }
    
    /**
     * Validate author name (non-empty, reasonable length)
     */
    public static boolean isValidAuthor(String author) {
        return isNotEmpty(author) && author.trim().length() <= 255;
    }
    
    /**
     * Validate category (non-empty, reasonable length)
     */
    public static boolean isValidCategory(String category) {
        return isNotEmpty(category) && category.trim().length() <= 100;
    }
    
    /**
     * Sanitize string (trim and remove extra spaces)
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Validate date range (issue date should be before due date)
     */
    public static boolean isValidDateRange(java.time.LocalDate issueDate, java.time.LocalDate dueDate) {
        if (issueDate == null || dueDate == null) {
            return false;
        }
        return !issueDate.isAfter(dueDate);
    }
    
    /**
     * Validate due date (should be in the future)
     */
    public static boolean isValidDueDate(java.time.LocalDate dueDate) {
        if (dueDate == null) {
            return false;
        }
        return !dueDate.isBefore(java.time.LocalDate.now());
    }
}
