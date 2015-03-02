/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 0.0.1
 * @author Rocchi
 */
public class DatabaseConnection {
    private final GUI gui;
    private final String driver;
    private String[] columnTitle;
    private String[][] table;
    private String dbAddress;
    private Connection connection;
    private Statement stat;
    private ResultSet resultSet;

    public DatabaseConnection() {
        this.driver = "com.mysql.jdbc.Driver";
        this.gui = new GUI(this);
    }

    public String[] getColumnTitle() {
        return columnTitle;
    }

    public String[][] getTable() {
        return table;
    }

    public void setDbAddress(String address, String port, String databaseName, String userName, String psw) {
        this.dbAddress = "jdbc:mysql://" + address + ":" + port + "/" + databaseName + "?user=" + userName + "&password=" + psw;
    }

    public int createConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(this.dbAddress);
            stat = connection.createStatement();
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver JDBC non trovato...");
            System.exit(1);
        } catch (SQLException ex) {
            return ex.getErrorCode();
        }
        return -1;
    }
    
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int execute(String query) {
        try {
            resultSet = stat.executeQuery(query);
            
            columnTitle = new String[resultSet.getMetaData().getColumnCount()];
            for(int i = 1; i <= columnTitle.length; i++)
                columnTitle[i-1] = resultSet.getMetaData().getColumnLabel(i);
            
            resultSet.last();
            table = new String[resultSet.getRow()][columnTitle.length];
            resultSet.beforeFirst();
            
            int j = 0;
            while(resultSet.next())
            {
                for(int i = 1; i <= columnTitle.length; i++)
                    table[j][i-1] = resultSet.getString(i);
                j++;
            }
        } catch (SQLException ex) {
            return ex.getErrorCode();
        }
        return -1;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseConnection dbConn = new DatabaseConnection();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI(dbConn).setVisible(true);
            }
        });
    }
    
}
