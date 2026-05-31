package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	
	private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp";
	
	private static final String USER = "root";
	
	private static final String PASSWORD = "ROOTPASSWORDS";
	
	public static Connection getConnection()
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			
			System.out.println("Database Connected");
			return conn;
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Driver not found");
			e.printStackTrace();
		}
		catch(SQLException e)
		{
			System.out.println("Connection failed");
			e.printStackTrace();
		}
		
		return null;
	}
}
