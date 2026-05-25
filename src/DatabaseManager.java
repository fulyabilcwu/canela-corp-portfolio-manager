import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp";
    private static final String USER = "root";
    private static final String PASSWORD = "your_my_sql_password";

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

    public static boolean createUser(String name, String email, String password, int age,
                                     double income, String riskTolerance, double netWorth) {

        String sql = "INSERT INTO Users (name, email, password, age, income, risk_tolerance, net_worth) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, age);
            statement.setDouble(5, income);
            statement.setString(6, riskTolerance);
            statement.setDouble(7, netWorth);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not create user.");
            e.printStackTrace();
            return false;
        }
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