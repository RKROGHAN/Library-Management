package com.library.utils;

import com.library.entities.Book;
import com.library.entities.Transaction;
import com.library.entities.User;
import com.library.services.BookService;
import com.library.services.TransactionService;
import com.library.services.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for generating reports
 */
public class ReportGenerator {
    
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;
    
    public ReportGenerator() {
        this.bookService = new BookService();
        this.userService = new UserService();
        this.transactionService = new TransactionService();
    }
    
    /**
     * Generate books inventory report
     */
    public String generateBooksReport() {
        try {
            List<Book> books = bookService.getAllBooks();
            StringBuilder report = new StringBuilder();
            
            report.append("LIBRARY BOOKS INVENTORY REPORT\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(80)).append("\n\n");
            
            report.append(String.format("%-5s %-30s %-25s %-15s %-10s %-10s\n", 
                "ID", "Title", "Author", "Category", "Available", "Total"));
            report.append("-".repeat(80)).append("\n");
            
            for (Book book : books) {
                report.append(String.format("%-5d %-30s %-25s %-15s %-10d %-10d\n",
                    book.getId(),
                    truncateString(book.getTitle(), 30),
                    truncateString(book.getAuthor(), 25),
                    truncateString(book.getCategory(), 15),
                    book.getAvailableCopies(),
                    book.getTotalCopies()
                ));
            }
            
            report.append("\n").append("=".repeat(80)).append("\n");
            report.append("Total Books: ").append(books.size()).append("\n");
            report.append("Total Available: ").append(books.stream().mapToInt(Book::getAvailableCopies).sum()).append("\n");
            report.append("Total Copies: ").append(books.stream().mapToInt(Book::getTotalCopies).sum()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            return "Error generating books report: " + e.getMessage();
        }
    }
    
    /**
     * Generate users report
     */
    public String generateUsersReport() {
        try {
            List<User> users = userService.getAllUsers();
            StringBuilder report = new StringBuilder();
            
            report.append("LIBRARY USERS REPORT\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(80)).append("\n\n");
            
            report.append(String.format("%-5s %-20s %-30s %-15s %-15s\n", 
                "ID", "Username", "Email", "Role", "Phone"));
            report.append("-".repeat(80)).append("\n");
            
            for (User user : users) {
                report.append(String.format("%-5d %-20s %-30s %-15s %-15s\n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail() != null ? user.getEmail() : "N/A",
                    user.getRole().name(),
                    user.getPhone() != null ? user.getPhone() : "N/A"
                ));
            }
            
            report.append("\n").append("=".repeat(80)).append("\n");
            report.append("Total Users: ").append(users.size()).append("\n");
            report.append("Admins: ").append(users.stream().mapToInt(u -> u.isAdmin() ? 1 : 0).sum()).append("\n");
            report.append("Regular Users: ").append(users.stream().mapToInt(u -> !u.isAdmin() ? 1 : 0).sum()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            return "Error generating users report: " + e.getMessage();
        }
    }
    
    /**
     * Generate transactions report
     */
    public String generateTransactionsReport() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            StringBuilder report = new StringBuilder();
            
            report.append("LIBRARY TRANSACTIONS REPORT\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(100)).append("\n\n");
            
            report.append(String.format("%-5s %-15s %-25s %-12s %-12s %-12s %-10s\n", 
                "ID", "User", "Book Title", "Issue Date", "Due Date", "Return Date", "Status"));
            report.append("-".repeat(100)).append("\n");
            
            for (Transaction transaction : transactions) {
                report.append(String.format("%-5d %-15s %-25s %-12s %-12s %-12s %-10s\n",
                    transaction.getId(),
                    truncateString(transaction.getUserName(), 15),
                    truncateString(transaction.getBookTitle(), 25),
                    transaction.getIssueDate() != null ? transaction.getIssueDate().toString() : "N/A",
                    transaction.getDueDate() != null ? transaction.getDueDate().toString() : "N/A",
                    transaction.getReturnDate() != null ? transaction.getReturnDate().toString() : "N/A",
                    transaction.getStatus().name()
                ));
            }
            
            report.append("\n").append("=".repeat(100)).append("\n");
            report.append("Total Transactions: ").append(transactions.size()).append("\n");
            report.append("Active Issues: ").append(transactions.stream().mapToInt(t -> t.isIssued() ? 1 : 0).sum()).append("\n");
            report.append("Returned: ").append(transactions.stream().mapToInt(t -> t.isReturned() ? 1 : 0).sum()).append("\n");
            report.append("Overdue: ").append(transactions.stream().mapToInt(t -> t.isOverdue() ? 1 : 0).sum()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            return "Error generating transactions report: " + e.getMessage();
        }
    }
    
    /**
     * Generate overdue books report
     */
    public String generateOverdueReport() {
        try {
            List<Transaction> overdueTransactions = transactionService.getOverdueTransactions();
            StringBuilder report = new StringBuilder();
            
            report.append("OVERDUE BOOKS REPORT\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(100)).append("\n\n");
            
            if (overdueTransactions.isEmpty()) {
                report.append("No overdue books found!\n");
            } else {
                report.append(String.format("%-5s %-15s %-25s %-12s %-12s %-10s\n", 
                    "ID", "User", "Book Title", "Issue Date", "Due Date", "Days Overdue"));
                report.append("-".repeat(100)).append("\n");
                
                for (Transaction transaction : overdueTransactions) {
                    long daysOverdue = transaction.getDaysOverdue();
                    report.append(String.format("%-5d %-15s %-25s %-12s %-12s %-10d\n",
                        transaction.getId(),
                        truncateString(transaction.getUserName(), 15),
                        truncateString(transaction.getBookTitle(), 25),
                        transaction.getIssueDate() != null ? transaction.getIssueDate().toString() : "N/A",
                        transaction.getDueDate() != null ? transaction.getDueDate().toString() : "N/A",
                        daysOverdue
                    ));
                }
            }
            
            report.append("\n").append("=".repeat(100)).append("\n");
            report.append("Total Overdue: ").append(overdueTransactions.size()).append("\n");
            
            return report.toString();
            
        } catch (Exception e) {
            return "Error generating overdue report: " + e.getMessage();
        }
    }
    
    /**
     * Save report to file
     */
    public boolean saveReportToFile(String report, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(report.getBytes());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving report to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Truncate string to specified length
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
