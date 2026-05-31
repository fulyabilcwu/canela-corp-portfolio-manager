import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all MySQL database interactions for the
 * Canela Corp Portfolio Manager application.
 *
 * Combined version including BOTH:
 *  - User/auth methods (signup, login, security question, reset password)
 *  - Portfolio/asset write methods (save, add, generate, analysis)
 *  - Portfolio/asset read methods (used by Monte Carlo + Long-Term panels)
 *
 * Authors: Fulya Bilgin, Naya
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // ============================================================
    // CONNECTION
    // ============================================================

    /**
     * Opens a connection to the portfolioapp database.
     * Returns null on connection failure.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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

    // ============================================================
    // USER AUTH (sign-up, login, forgot password)
    // ============================================================

    /**
     * Creates a new user record during sign-up.
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
     * Returns the security question for a given email.
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
     * Verifies the security answer for a given email.
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
     * Resets the user's password.
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
     * Verifies login credentials.
     */
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

    // ============================================================
    // PORTFOLIO + ASSET WRITES (used by builder / dashboard)
    // ============================================================

    /**
     * Saves a new portfolio for a given user.
     */
    public static boolean savePortfolio(int userId, String portfolioName, double totalValue, String riskLevel) {
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

    /**
     * Adds an asset to a portfolio.
     */
    public static boolean addAsset(int portfolioId, String assetType, double allocationPercentage, double amount) {
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

    /**
     * Updates user profile info used for portfolio generation.
     */
    public static boolean generatePortfolio(int age, double income, double net_worth, String email) {
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

    public static boolean setAsset(/** */) {
        return false;
    }

    /**
     * Saves a portfolio analysis result.
     */
    public static boolean saveAnalysis(int portfolioId, double estimatedValue, double projectedGrowth, int simulationYear, double bestCase, double worstCase) {
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

    // ============================================================
    // PORTFOLIO + ASSET READS (used by Monte Carlo / Long-Term panels)
    // ============================================================

    /**
     * Returns all portfolios in the database, ordered by portfolio_id.
     */
    public static List<Portfolio> getAllPortfolios() {
        List<Portfolio> portfolios = new ArrayList<>();
        String sql = "SELECT portfolio_id, user_id, portfolio_name, "
                   + "total_value, risk_level "
                   + "FROM Portfolios ORDER BY portfolio_id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Portfolio p = new Portfolio(
                    rs.getInt("portfolio_id"),
                    rs.getInt("user_id"),
                    rs.getString("portfolio_name"),
                    rs.getDouble("total_value"),
                    rs.getString("risk_level")
                );
                portfolios.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error fetching portfolios: " + e.getMessage());
            e.printStackTrace();
        }

        return portfolios;
    }

    /**
     * Returns a single portfolio by its ID, or null if not found.
     */
    public static Portfolio getPortfolioById(int portfolioId) {
        String sql = "SELECT portfolio_id, user_id, portfolio_name, "
                   + "total_value, risk_level "
                   + "FROM Portfolios WHERE portfolio_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Portfolio(
                        rs.getInt("portfolio_id"),
                        rs.getInt("user_id"),
                        rs.getString("portfolio_name"),
                        rs.getDouble("total_value"),
                        rs.getString("risk_level")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching portfolio "
                + portfolioId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns all assets belonging to the given portfolio.
     * Synthesizes a display name from asset_type since the DB does
     * not store one.
     */
    public static List<Asset> getAssetsByPortfolioId(int portfolioId) {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT asset_id, portfolio_id, asset_type, "
                   + "allocation_percentage, amount "
                   + "FROM Assets WHERE portfolio_id = ? ORDER BY asset_id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String assetType = rs.getString("asset_type");
                    String assetName = formatAssetName(assetType);

                    Asset a = new Asset(
                        rs.getInt("asset_id"),
                        rs.getInt("portfolio_id"),
                        assetName,
                        assetType,
                        rs.getInt("allocation_percentage"),
                        rs.getInt("amount")
                    );
                    assets.add(a);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching assets for portfolio "
                + portfolioId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return assets;
    }

    /**
     * Converts a DB asset type like "REAL_ESTATE" to a friendly
     * display name like "Real Estate Holdings".
     */
    private static String formatAssetName(String assetType) {
        if (assetType == null) {
            return "Unknown Asset";
        }
        String[] parts = assetType.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(p.charAt(0)));
            sb.append(p.substring(1).toLowerCase());
            sb.append(" ");
        }
        return sb.toString().trim() + " Holdings";
    }

    // ============================================================
    // TEST
    // ============================================================

    /**
     * Quick smoke test to verify the database connection and data loading.
     * Run with: java -cp ".:../libs/*" DatabaseManager
     */
    public static void main(String[] args) {
        System.out.println("=== Testing DatabaseManager ===");
        System.out.println();

        List<Portfolio> portfolios = getAllPortfolios();
        System.out.println("Found " + portfolios.size() + " portfolios:");
        for (Portfolio p : portfolios) {
            System.out.println("  [" + p.getPortfolio_ID() + "] "
                + p.getPortfolioName()
                + " | total=" + p.getTotalValue()
                + " | risk=" + p.getRiskLevel());
        }

        if (!portfolios.isEmpty()) {
            int firstId = portfolios.get(0).getPortfolio_ID();
            System.out.println();
            System.out.println("Assets in portfolio " + firstId + ":");
            List<Asset> assets = getAssetsByPortfolioId(firstId);
            for (Asset a : assets) {
                System.out.println("  " + a.getAssetName()
                    + " (" + a.getAssetType() + "): "
                    + a.getAllocationPercentage() + "% = "
                    + a.getAmount());
            }
        }

        System.out.println();
        System.out.println("DatabaseManager working correctly!");
    }
}
