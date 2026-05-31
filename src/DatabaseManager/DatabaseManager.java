

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp";
    private static final String USER = "root";
    private static final String PASSWORD = "RootUser420";

    /**
     * 
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
            return null;
        }
    }

    // this is for the sign-up interface
    /**
     * 
     * @param name
     * @param email
     * @param password
     * @param question
     * @param answer
     * @return
     */
    public static boolean initiateUser(String name, String email, String password, String question, String answer) {
        String sql = "INSERT INTO Users (name, email, password, security_question, security_answer) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, question);
            statement.setString(5, answer);


            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not create user.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param email
     * @return
     */
    public String getSecurityQuestion(String email) {
        String query = "SELECT security_question FROM users WHERE email = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getString("security_question");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param email
     * @param answer
     * @return
     */
    public boolean verifySecurityQuestion(String email, String answer) {
        String query = "SELECT security_answer FROM users WHERE email = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String storedAnswer = result.getString("security_answer");
                return storedAnswer.equalsIgnoreCase(answer.trim());
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 
     * @param email
     * @param password
     * @return
     */
    public boolean resetPassword(String email, String password) {
        String query = "UPDATE users SET password = ? where email = ?;";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, password);
            statement.setString(2, email);

            statement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 
     * @param email
     * @param password
     * @return
     */
    public static boolean loginUser(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            System.out.println("Login failed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param user_id
     * @param portfolioName
     * @param totalValue
     * @param riskLevel
     * @return
     */
    public static boolean addPortfolio(int user_id, String portfolioName, Double totalValue, String riskLevel) {
        String sql = "INSERT INTO Portfolios (user_id, portfolio_name, total_value, risk_level) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);
            statement.setString(2, portfolioName);
            statement.setDouble(3, totalValue);
            statement.setString(4, riskLevel);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save portfolio.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param age
     * @param income
     * @param net_worth
     * @param email
     * @return
     */
    public static boolean generatePortfolio(int age, Double income, Double net_worth, String email){
        String query = "UPDATE users SET age = ?, income = ?, net_worth = ? where email = ?;";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, age);
            statement.setDouble(2, income);
            statement.setDouble(3, net_worth);
            statement.setString(4, email);

            statement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param portfolio_id
     * @param estimatedValue
     * @param projectedGrowth
     * @param simulationYear
     * @param bestCase
     * @param worstCase
     * @return
     */
    public static boolean saveAnalysis(int portfolio_id, Double estimatedValue, Double projectedGrowth, int simulationYear, Double bestCase, Double worstCase) {
        String sql = "INSERT INTO PortfolioAnalyzer "
                   + "(portfolio_id, estimated_value, projected_growth, simulation_year, best_case, worst_case) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, portfolio_id);
            statement.setDouble(2, estimatedValue);
            statement.setDouble(3, projectedGrowth);
            statement.setInt(4, simulationYear);
            statement.setDouble(5, bestCase);
            statement.setDouble(6, worstCase);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save portfolio analysis.");
            e.printStackTrace();
            return false;
        }
    }

	public static boolean updateUserInfo(
        int user_id,
        String name,
        String email,
        String password,
        Integer age,
        Double income,
        Double netWorth,
        String securityQ,
        String securityA) {

        StringBuilder sql = new StringBuilder("UPDATE Users SET ");
        ArrayList<Object> values = new ArrayList<>();

        if (!name.isEmpty()) {
            sql.append("name = ?, ");
            values.add(name);
        }

        if (!email.isEmpty()) {
            sql.append("email = ?, ");
            values.add(email);
        }

        if (!password.isEmpty()) {
            sql.append("password = ?, ");
            values.add(password);
        }

        if (age != null) {
            sql.append("age = ?, ");
            values.add(age);
        }

        if (income != null) {
            sql.append("income = ?, ");
            values.add(income);
        }

        if (netWorth != null) {
            sql.append("net_worth = ?, ");
            values.add(netWorth);
        }

        if (!securityQ.isEmpty()) {
            sql.append("security_question = ?, ");
            values.add(Double.parseDouble(securityQ));
        }
        if (!securityA.isEmpty()) {
            sql.append("security_answer = ?, ");
            values.add(Double.parseDouble(securityA));
        }

        if (values.isEmpty()) {
            System.out.println("No fields provided for update.");
            return false;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE user_id = ?");
        values.add(user_id);

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 1; i <= values.size(); i++) {
                statement.setObject(i, values.get(i));
            }

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not update user.");
            e.printStackTrace();
            return false;

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
            e.printStackTrace();
            return false;
        }
}

    public static boolean deleteUser(int user_id) {
        String query = "DELETE from users WHERE user_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user_id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not delete user.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePortfolio(int portfolio_id) {
        String query = "DELETE FROM portfolios WHERE portfolio_id = ?;";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, portfolio_id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not delete portfolio.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAsset(int asset_id) {
        String query = "DELETE from assets WHERE asset_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, asset_id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not delete asset.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateAsset(int asset_id, String asset_type, Double allocation_percentage, Double amount) {
        StringBuilder sql = new StringBuilder("UPDATE assets SET ");
        ArrayList<Object> values = new ArrayList<>();

        if (!asset_type.isEmpty()) {
            sql.append("asset_type = ?, ");
            values.add(asset_type);
        }

        if (allocation_percentage != null) {
            sql.append("allocation_percentage = ?, ");
            values.add(allocation_percentage);
        }

        if (amount != null) {
            sql.append("amount = ?, ");
            values.add(amount);
        }

        if (values.isEmpty()) {
            System.out.println("No fields were provided to update.");
            return false;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE asset_id = ?");
        values.add(asset_id);

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 1; i <= values.size(); i++) {
                statement.setObject(i, values.get(i));
            }

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not update asset.");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            e.printStackTrace();
            return false;
        }
}

    public static boolean addAsset(int portfolio_id, String asset_type, Double allocation_percentage, Double amount){
        String query = "INSERT INTO users (portfolio_id, asset_type, allocation_percentage, amount) VALUES (?, ?, ?, ?);";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setInt(1, portfolio_id);
            statement.setString(2, asset_type);
            statement.setDouble(3, allocation_percentage);
            statement.setDouble(4, amount);


            statement.executeQuery();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int getLatestPortfolioId(int userId)
    {
        String sql =
            "SELECT portfolio_id " +
            "FROM Portfolios " +
            "WHERE user_id = ? " +
            "ORDER BY portfolio_id DESC " +
            "LIMIT 1";

        try (
            Connection connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);

            ResultSet rs =
                    statement.executeQuery();

            if(rs.next())
            {
                return rs.getInt("portfolio_id");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }
    
    public static int getUserIdByEmail(String email)
    {
        String sql =
            "SELECT user_id FROM Users WHERE email = ?";

        try(
            Connection conn = getConnection();
            PreparedStatement stmt =
                conn.prepareStatement(sql))
        {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return rs.getInt("user_id");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }


}