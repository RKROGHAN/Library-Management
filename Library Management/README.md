# Library Management System 📚

A modern, feature-rich Library Management System built with JavaFX, MySQL, and JDBC. This application provides a comprehensive solution for managing library operations with a beautiful, animated user interface.

## ✨ Features

### 🔐 Authentication & Authorization
- Secure login system with role-based access control
- Admin and User roles with different permissions
- Session management and user validation

### 📚 Book Management
- Complete CRUD operations for books
- Advanced search and filtering capabilities
- Category-based organization
- Inventory tracking (available/total copies)
- ISBN validation and book details

### 👥 User Management
- User registration and profile management
- Role-based access control
- User search and filtering
- Account management features

### 📖 Issue/Return System
- Book issuing with due dates
- Return processing with automatic status updates
- Overdue tracking and fine calculation
- Transaction history and reporting

### 🎨 Modern UI/UX
- Beautiful, animated JavaFX interface
- JFoenix material design components
- Responsive design with smooth transitions
- Dark/Light theme support (ready for implementation)
- Intuitive navigation with drawer menu

### 📊 Reports & Analytics
- Comprehensive reporting system
- PDF and Excel export capabilities
- Statistical dashboards
- Overdue book alerts
- System analytics

### ⚙️ Advanced Features
- Real-time search and filtering
- Data validation and error handling
- Database connection pooling
- Modular architecture (MVC pattern)
- Clean, maintainable code structure

## 🛠️ Technology Stack

- **Frontend**: JavaFX 17.0.2 with JFoenix 9.0.10
- **Backend**: Java 11+ with JDBC
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.6+
- **Architecture**: MVC Pattern
- **UI Framework**: FXML with CSS styling

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Git (for cloning the repository)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd library-management-system
```

### 2. Database Setup
1. Install and start MySQL server
2. Create a database user (optional, can use root)
3. Update database credentials in `DatabaseConnection.java` if needed:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

### 3. Build the Project
```bash
mvn clean compile
```

### 4. Run the Application
```bash
mvn javafx:run
```

Or run the main class directly:
```bash
java -cp target/classes com.library.application.LibraryApp
```

## 🗄️ Database Schema

The application automatically creates the following tables:

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `password`
- `role` (ADMIN/USER)
- `email`
- `phone`
- `created_at`, `updated_at`

### Books Table
- `id` (Primary Key)
- `title`, `author`, `isbn`
- `category`, `publisher`
- `publication_year`
- `available_copies`, `total_copies`
- `description`
- `created_at`, `updated_at`

### Transactions Table
- `id` (Primary Key)
- `user_id` (Foreign Key)
- `book_id` (Foreign Key)
- `issue_date`, `due_date`, `return_date`
- `status` (ISSUED/RETURNED/OVERDUE)
- `fine_amount`
- `created_at`, `updated_at`

## 🎯 Default Login Credentials

- **Username**: `admin`
- **Password**: `admin123`
- **Role**: Administrator

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/library/
│   │   ├── application/          # Main application class
│   │   ├── controllers/          # JavaFX controllers
│   │   ├── entities/            # Data model classes
│   │   ├── services/            # Business logic layer
│   │   ├── db/                  # Database utilities
│   │   └── utils/               # Utility classes
│   └── resources/
│       ├── fxml/                # FXML UI files
│       ├── css/                 # Stylesheets
│       ├── icons/               # Application icons
│       └── sql/                  # Database schema
└── test/                        # Test files
```

## 🔧 Configuration

### Database Configuration
Update the database connection settings in `src/main/java/com/library/db/DatabaseConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### Application Settings
- Fine calculation per day
- Maximum issue duration
- Notification settings
- Theme preferences

## 🎨 UI Features

### Modern Design Elements
- Material Design components (JFoenix)
- Smooth animations and transitions
- Responsive layout design
- Professional color scheme
- Intuitive navigation

### Interactive Components
- Animated login screen
- Sliding navigation drawer
- Real-time search with filtering
- Data tables with sorting
- Modal dialogs for forms
- Status indicators and alerts

## 📊 Key Functionalities

### For Administrators
- Full system access
- User management
- Book inventory management
- Transaction oversight
- Report generation
- System configuration

### For Regular Users
- Book browsing and search
- Issue/return books
- View personal transactions
- Profile management

## 🔒 Security Features

- Password-based authentication
- Role-based access control
- Input validation and sanitization
- SQL injection prevention
- Session management

## 🚀 Performance Optimizations

- Database connection pooling
- Efficient query design
- Lazy loading for large datasets
- Optimized UI rendering
- Memory management

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## 🔮 Future Enhancements

- [ ] Mobile app integration
- [ ] Advanced analytics dashboard
- [ ] Email notification system
- [ ] Barcode scanning support
- [ ] Multi-language support
- [ ] Cloud deployment options
- [ ] API development
- [ ] Advanced reporting features

## 📸 Screenshots

*Screenshots of the application interface would be added here*

## 🏆 Acknowledgments

- JavaFX team for the excellent UI framework
- JFoenix for beautiful material design components
- MySQL team for the robust database system
- Open source community for inspiration and support

---

**Built with ❤️ using JavaFX, MySQL, and modern development practices**
