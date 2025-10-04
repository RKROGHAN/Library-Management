-- Library Management System Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category VARCHAR(100),
    publisher VARCHAR(255),
    publication_year INT,
    available_copies INT NOT NULL DEFAULT 0,
    total_copies INT NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    status ENUM('ISSUED', 'RETURNED', 'OVERDUE') NOT NULL DEFAULT 'ISSUED',
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_category ON books(category);
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_book_id ON transactions(book_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_due_date ON transactions(due_date);

-- Insert default admin user
INSERT INTO users (username, password, role, email) VALUES 
('admin', 'admin123', 'ADMIN', 'admin@library.com');

-- Insert sample books
INSERT INTO books (title, author, isbn, category, publisher, publication_year, available_copies, total_copies, description) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 'Scribner', 1925, 3, 3, 'A classic American novel set in the Jazz Age.'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 'J.B. Lippincott & Co.', 1960, 2, 2, 'A gripping tale of racial injustice and childhood innocence.'),
('1984', 'George Orwell', '9780451524935', 'Dystopian Fiction', 'Secker & Warburg', 1949, 4, 4, 'A dystopian social science fiction novel.'),
('Pride and Prejudice', 'Jane Austen', '9780141439518', 'Romance', 'T. Egerton', 1813, 2, 2, 'A romantic novel of manners.'),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769174', 'Fiction', 'Little, Brown and Company', 1951, 1, 1, 'A coming-of-age story.'),
('Lord of the Flies', 'William Golding', '9780571056866', 'Fiction', 'Faber and Faber', 1954, 3, 3, 'A story about British boys stranded on an uninhabited island.'),
('The Hobbit', 'J.R.R. Tolkien', '9780547928227', 'Fantasy', 'George Allen & Unwin', 1937, 2, 2, 'A fantasy novel about a hobbit\'s unexpected journey.'),
('Harry Potter and the Philosopher\'s Stone', 'J.K. Rowling', '9780747532699', 'Fantasy', 'Bloomsbury', 1997, 5, 5, 'The first book in the Harry Potter series.'),
('The Chronicles of Narnia', 'C.S. Lewis', '9780007117116', 'Fantasy', 'Geoffrey Bles', 1950, 3, 3, 'A series of fantasy novels.'),
('The Alchemist', 'Paulo Coelho', '9780061122415', 'Fiction', 'HarperCollins', 1988, 2, 2, 'A philosophical novel about a young shepherd\'s journey.');
