package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connectivity using JDBC.
 * Implements centralized connection management for the MySQL database.
 */
public class DatabaseManager {

    // MySQL Database credentials - Modify here as needed
    private static final String URL = "jdbc:mysql://localhost:3306/campus_placement?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP MySQL password is empty string

    static {
        try {
            // Explicitly load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Not Found! Ensure mysql-connector-j-8.x.x.jar is added to project libraries.");
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a new active MySQL database connection.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Utility method to quickly verify database connectivity at system launch.
     * @return true if database responds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
