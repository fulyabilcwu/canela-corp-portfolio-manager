package DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;

public class portfolioMan {

    public int insertUser(
            String name,
            int age,
            double income,
            String risk,
            double netWorth)
    {

        int userId = -1;

        String sql = "INSERT INTO Users " + "(name, email, password, age, income, risk_tolerance, net_worth) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try 
        (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS);
        )
        
        {

            stmt.setString(1, name);

            stmt.setString(2, name.toLowerCase() + "@example.com");

            stmt.setString(3, "pass123");

            stmt.setInt(4, age);

            stmt.setDouble(5, income);

            stmt.setString(6, risk.toUpperCase());

            stmt.setDouble(7, netWorth);

            stmt.executeUpdate();

            ResultSet res =stmt.getGeneratedKeys();

            if(res.next())
            {
                userId = res.getInt(1);
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return userId;
    }

    public int insertPortfolio(int userId,String portfolioName,String risk,double totalValue)
    {

        int portfolioId = -1;

        String sql = "INSERT INTO Portfolios " +"(user_id, portfolio_name, total_value, risk_level) " + "VALUES (?, ?, ?, ?)";

        try 
        (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt =
                    conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        )
        
        {

            stmt.setInt(1, userId);

            stmt.setString(2, portfolioName);

            stmt.setDouble(3, totalValue);

            stmt.setString(4, risk.toUpperCase());

            stmt.executeUpdate();

            ResultSet res =stmt.getGeneratedKeys();

            if(res.next())
            {
                portfolioId = res.getInt(1);
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return portfolioId;
        
        
    }
    public void addAsset(int portfolioId, String assetType, double allocation)
    {
    	String sql = "INSERT INTO ASSETS " + "(portfolio_id, asset_type, allocation_percentage, amount) " + "VALUES (?, ?, ?, ?)";
    	
    	try(Connection conn = DBConnection.getConnection();
    			PreparedStatement stmt = conn.prepareStatement(sql);)
    	{
    		stmt.setInt(1,  portfolioId);
    		
    		stmt.setString(2, assetType.toUpperCase());
    		
    		stmt.setDouble(3, allocation);
    		
    		stmt.setDouble(4,0);
    		
    		stmt.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		e.printStackTrace();
  
    	}
    }
}