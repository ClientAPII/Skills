// DatabaseManager.java
package de.clientapi.skills;

import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public boolean doesPlayerExist(String uuid) throws SQLException {
        String query = "SELECT COUNT(*) FROM skills WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void createPlayer(String uuid, String name, int bow, int sword) throws SQLException {
        String query = "INSERT INTO skills (uuid, name, bow, sword) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            statement.setString(2, name);
            statement.setInt(3, bow);
            statement.setInt(4, sword);
            statement.executeUpdate();
        }
    }

    public void updatePlayerName(String uuid, String name) throws SQLException {
        String query = "UPDATE skills SET name = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, uuid);
            statement.executeUpdate();
        }
    }

    public void connect(String url, String username, String password, String databaseName) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        // Connect to MySQL server without specifying the database
        connection = DriverManager.getConnection(url, username, password);

        // Check if the database exists and create it if it does not
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
        }

        // Switch to the specified database
        connection.setCatalog(databaseName);

        // Create the table if it does not exist
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS skills (uuid VARCHAR(36) NOT NULL, name VARCHAR(255), bow INT NOT NULL, sword INT NOT NULL, PRIMARY KEY (uuid))");
        }

        System.out.println("\u001B[32m" + "Connection with MySQL established." + "\u001B[0m");
    }

    public int getLevel(String uuid, String skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT " + skill + " FROM skills WHERE uuid = ?");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(skill);
        } else {
            return 1; // Default level
        }
    }

    public void setLevel(String uuid, String skill, int level) throws SQLException {
        String query = "UPDATE skills SET " + skill + " = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, level);
            statement.setString(2, uuid);
            statement.executeUpdate();
        }
    }
}