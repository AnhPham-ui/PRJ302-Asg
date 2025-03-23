package com.leave.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-Q99KA6S\\MSSQLSERVER03:1433;databaseName=leave_management;trustServerCertificate=true;";
    private static final String DB_USER = "anhpt"; 
    private static final String DB_PASS = "123456789";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}