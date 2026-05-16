package com.mycompany.skilltracker;

import java.sql.*;

// creation of Database
public class SkillDB {

    private static final String url      = "jdbc:mysql://localhost:3306";
    private static final String username = "root";
    private static final String password = "Mm112233";

    public static void setup(){
        try (Connection con = DriverManager.getConnection(url, username, password);
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS SkillTracker");
            st.executeUpdate("USE SkillTracker");

            // users table creation
            String Users =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "phone VARCHAR(15) UNIQUE NOT NULL)";
            st.executeUpdate(Users);

            // skills table creation
            String Skills =
                    "CREATE TABLE IF NOT EXISTS skills (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "category VARCHAR(100) NOT NULL, " +
                            "strategy VARCHAR(50) NOT NULL, " +
                            "last_used DATE NOT NULL, " +
                            "usage_count INT NOT NULL, " +
                            "state VARCHAR(20) NOT NULL, " +
                            "FOREIGN KEY (user_id) REFERENCES users(id))";
            st.executeUpdate(Skills);

            // demo
            st.executeUpdate(
                    "INSERT IGNORE INTO users (phone) VALUES ('0501234567')");
            st.executeUpdate(
                    "INSERT IGNORE INTO users (phone) VALUES ('0500000000')");

            // demo skills for user 1
            st.executeUpdate(
                    "INSERT IGNORE INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "SELECT 1, 'Java', 'Programming', 'Time-Based Decay', " +
                            "DATE_SUB(CURDATE(), INTERVAL 5 DAY), 10, 'Fresh' " +
                            "WHERE NOT EXISTS (" +
                            "SELECT 1 FROM skills WHERE user_id=1 AND name='Java')");

            st.executeUpdate(
                    "INSERT IGNORE INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "SELECT 1, 'SQL', 'Database', 'Frequency-Based Decay', " +
                            "DATE_SUB(CURDATE(), INTERVAL 40 DAY), 5, 'Fading' " +
                            "WHERE NOT EXISTS (" +
                            "SELECT 1 FROM skills WHERE user_id=1 AND name='SQL')");

            st.executeUpdate(
                    "INSERT IGNORE INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "SELECT 1, 'Swift', 'Mobile', 'Time-Based Decay', " +
                            "DATE_SUB(CURDATE(), INTERVAL 85 DAY), 2, 'Outdated' " +
                            "WHERE NOT EXISTS (" +
                            "SELECT 1 FROM skills WHERE user_id=1 AND name='Swift')");

            // demo skills for user 2
            st.executeUpdate(
                    "INSERT IGNORE INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "SELECT 2, 'Python', 'Programming', 'Time-Based Decay', " +
                            "DATE_SUB(CURDATE(), INTERVAL 10 DAY), 8, 'Fresh' " +
                            "WHERE NOT EXISTS (" +
                            "SELECT 1 FROM skills WHERE user_id=2 AND name='Python')");

            st.executeUpdate(
                    "INSERT IGNORE INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "SELECT 2, 'Excel', 'Productivity', 'Frequency-Based Decay', " +
                            "DATE_SUB(CURDATE(), INTERVAL 50 DAY), 3, 'Fading' " +
                            "WHERE NOT EXISTS (" +
                            "SELECT 1 FROM skills WHERE user_id=2 AND name='Excel')");

            System.out.println("Database setup complete!");

        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}