package com.mycompany.skilltracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection;

    private DatabaseConnection() {

        try {

            String url = "jdbc:mysql://localhost:3306/skilltracker";
            String user = "root";
            String password = "Sss@12345";

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected Successfully");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {

        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}