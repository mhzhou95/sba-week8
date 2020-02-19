package com.github.perscholas;

import java.sql.*;

/**
 * Created by leon on 2/18/2020.
 */
public enum DatabaseConnection {
    MARIADB;

    private Connection getConnection(String dbVendor) {
        String username = "root";
        String password = "";
        String databaseName = "SBA_week8_management_system";
        String url = "jdbc:" + dbVendor + "://127.0.0.1/";
        try {
            return DriverManager.getConnection(url + databaseName, username, password);
        } catch (SQLException e) {
            try{
                return DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public Connection getConnection(){
        return getConnection(name().toLowerCase());
    }

    public ResultSet executeQuery(String sqlStatement) throws SQLException {
        Connection connection = DatabaseConnection.MARIADB.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlStatement);
    }

    public Statement getScrollableStatement() {
        int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
        int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
        try {
            return getConnection().createStatement(resultSetType, resultSetConcurrency);
        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    public void executeStatement(String sqlStatement) {
        try {
            Statement statement = getScrollableStatement();
            statement.execute(sqlStatement);
        } catch (SQLException e) {
            throw new Error(e);
        }
    }
}
