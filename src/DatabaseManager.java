import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

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

    /** 
     * thinking to change this and make it for portfolio builder rather than user creation 
     * Cause when a user signs up, only name, email, and password are gathered.
     * then, when building portfolio, the user is prompted to enter info such as
     * age, income, etc..
     * 
     * should the risk tolerance be an input or should it be generated later?
     * */
    // public static boolean createUser(String name, String email, String password, int age,
    //                                  double income, String riskTolerance, double netWorth) {

    //     String sql = "INSERT INTO Users (name, email, password, age, income, risk_tolerance, net_worth) "
    //                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    //     try (Connection connection = getConnection();
    //          PreparedStatement statement = connection.prepareStatement(sql)) {

    //         statement.setString(1, name);
    //         statement.setString(2, email);
    //         statement.setString(3, password);
    //         statement.setInt(4, age);
    //         statement.setDouble(5, income);
    //         statement.setString(6, riskTolerance);
    //         statement.setDouble(7, netWorth);

    //         statement.executeUpdate();
    //         return true;

    //     } catch (SQLException e) {
    //         System.out.println("Could not create user.");
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    // this is for the sign-up interface
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


    public static boolean loginUser(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

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

    public static boolean savePortfolio(int userId, String portfolioName,
                                        double totalValue, String riskLevel) {

        String sql = "INSERT INTO Portfolios (user_id, portfolio_name, total_value, risk_level) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
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

    public static boolean addAsset(int portfolioId, String assetType,
                                   double allocationPercentage, double amount) {

        String sql = "INSERT INTO Assets (portfolio_id, asset_type, allocation_percentage, amount) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, portfolioId);
            statement.setString(2, assetType);
            statement.setDouble(3, allocationPercentage);
            statement.setDouble(4, amount);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not add asset.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean generatePortfolio(int age, double income, double net_worth, String email){
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

    public static boolean setAsset(/** */){
        return false;
    }

    public static boolean saveAnalysis(int portfolioId, double estimatedValue,
                                       double projectedGrowth, int simulationYear,
                                       double bestCase, double worstCase) {

        String sql = "INSERT INTO PortfolioAnalyzer "
                   + "(portfolio_id, estimated_value, projected_growth, simulation_year, best_case, worst_case) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, portfolioId);
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
}