package com.mycompany.skilltracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// SINGLETON design pattern (Database connection)
public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection;

    // private constructor
    private DatabaseConnection() {

        try {

            String url = "jdbc:mysql://localhost:3306/SkillTracker";
            String user = "root";
            String password = "Mm112233";

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected Successfully");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // can only be called by getInstance() method
    public static DatabaseConnection getInstance() {

        // creates only one instance
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        // and uses only this single instance
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}