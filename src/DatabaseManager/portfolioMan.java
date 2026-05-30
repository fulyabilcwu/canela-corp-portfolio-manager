package DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBConnection;
import portfolioBuilderGUI.Asset;
import portfolioBuilderGUI.Portfolio;

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
    
    public ArrayList<Portfolio> getPortfoliosByUser(int userId)
    {
        ArrayList<Portfolio> portfolios = new ArrayList<>();

        String sql = "SELECT * FROM Portfolios WHERE user_id = ?";

        try
        (
            Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        
        {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                Portfolio portfolio =
                        new Portfolio( rs.getInt("portfolio_id"), rs.getInt("user_id"), rs.getString("portfolio_name"),  rs.getDouble("total_value"), rs.getString("risk_level") );

                portfolios.add(portfolio);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return portfolios;
    }
    
    public ArrayList<Asset> getAssetsByPortfolio(int portfolioId)
    {
        ArrayList<Asset> assets = new ArrayList<>();

        String sql = "SELECT * FROM Assets WHERE portfolio_id = ?";

        try
        (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        
        {
            stmt.setInt(1, portfolioId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                Asset asset = new Asset( rs.getInt("asset_id"), rs.getInt("portfolio_id"),  rs.getString("asset_type"), rs.getString("asset_type"), (int)rs.getDouble("allocation_percentage"), (int)rs.getDouble("amount") );

                assets.add(asset);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return assets;
    }
}