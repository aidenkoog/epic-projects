package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static String url = "jdbc:mysql://localhost/taxi";
    private static String user = "root";
    private static String password = "1234";

    public static Connection getConnection(){
    	Connection conn = null;
    	try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return conn;
    }
}
