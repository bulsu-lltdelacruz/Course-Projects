package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UtilDB {
    /*private static final String URL = "jdbc:mysql://localhost:3306/db_tourismmanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "";*/
    private static final String URL = "jdbc:mysql://localhost:3306/db_tourismmanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try{
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        return null;
    }
}
