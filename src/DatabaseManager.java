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
 * Combined version including:
 *  - User/auth methods (signup, login, security Q&A, reset password, updateUserInfo, deleteUser)
 *  - Portfolio write methods (addPortfolio, generatePortfolio, deletePortfolio, saveAnalysis)
 *  - Asset write methods (addAsset, updateAsset, deleteAsset)
 *  - Portfolio/asset read methods (used by Monte Carlo + Long-Term panels)
 *
 * Authors: Fulya Bilgin, Naya
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolioapp?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "RootUser420";

    // ============================================================
    // CONNECTION
    // ============================================================

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
    // USER AUTH
    // ============================================================

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

    public static String getSecurityQuestion(String email) {
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

    public static boolean verifySecurityQuestion(String email, String answer) {
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

    public static boolean resetPassword(String email, String password) {
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
            values.add(securityQ);
        }
        if (!securityA.isEmpty()) {
            sql.append("security_answer = ?, ");
            values.add(securityA);
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

            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
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

    // ============================================================
    // PORTFOLIO + ASSET WRITES
    // ============================================================

    public static boolean generatePortfolio(int user_id, String portfolioName, Double totalValue, String riskLevel) {
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

        public static boolean updatePortfolio(int portfolio_id, Integer user_id,
            String portfolioName, Double totalValue, String riskLevel) {

        StringBuilder sql = new StringBuilder("UPDATE portfolios SET ");
        ArrayList<Object> values = new ArrayList<>();

        if (user_id != null) {
            sql.append("user_id = ?, ");
            values.add(user_id);
        }

        if (portfolioName != null && !portfolioName.isEmpty()) {
            sql.append("portfolio_name = ?, ");
            values.add(portfolioName);
        }

        if (totalValue != null) {
            sql.append("total_value = ?, ");
            values.add(totalValue);
        }

        if (riskLevel != null && !riskLevel.isEmpty()) {
            sql.append("risk_level = ?, ");
            values.add(riskLevel);
        }

        if (values.isEmpty()) {
            System.out.println("No fields were provided to update.");
            return false;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE portfolio_id = ?");
        values.add(portfolio_id);

        try (Connection connection = getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not update portfolio.");
            e.printStackTrace();
            return false;

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean completeAccount(int age, Double income, Double net_worth, String email) {
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

    public static boolean generateAnalysis(int portfolio_id, Double estimatedValue, Double projectedGrowth, int simulationYear, Double bestCase, Double worstCase) {
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

    public static boolean addAsset(int portfolio_id, String asset_type, Double allocation_percentage, Double amount) {
        String query = "INSERT INTO assets (portfolio_id, asset_type, allocation_percentage, amount) VALUES (?, ?, ?, ?);";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, portfolio_id);
            statement.setString(2, asset_type);
            statement.setDouble(3, allocation_percentage);
            statement.setDouble(4, amount);

            statement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
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

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("age"),
                    rs.getDouble("income"),
                    rs.getDouble("net_worth"),
                    rs.getString("security_question"),
                    rs.getString("security_answer"),
                    rs.getString("role")
                );
                users.add(user);
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public static List<Asset> getallAsets() {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM assets ORDER BY asset_id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Asset asset = new Asset(
                    rs.getInt("asset_id"),
                    rs.getInt("portfolio_id"),
                    rs.getString("asset_type"),
                    rs.getDouble("allocation_percentage"),
                    rs.getDouble("amount")
                );
                assets.add(asset);

            } 
        }catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }

        return assets;
    }

    // ============================================================
    // PORTFOLIO + ASSET READS (used by Monte Carlo / Long-Term panels)
    // ============================================================

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

    public static Asset getAssetByID(int portfolioId) {
        String sql = "SELECT * FROM assets WHERE portfolio_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Asset(
                        rs.getInt("asset_id"),
                        rs.getInt("portfolio_id"),
                        rs.getString("asset_type"),
                        rs.getDouble("allocation_percentage"),
                        rs.getDouble("amount")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching Asset "
                + portfolioId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


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

    // // ============================================================
    // // TEST
    // // ============================================================

    // public static void main(String[] args) {
    //     System.out.println("=== Testing DatabaseManager ===");
    //     System.out.println();

    //     List<Portfolio> portfolios = getAllPortfolios();
    //     System.out.println("Found " + portfolios.size() + " portfolios:");
    //     for (Portfolio p : portfolios) {
    //         System.out.println("  [" + p.getPortfolio_ID() + "] "
    //             + p.getPortfolioName()
    //             + " | total=" + p.getTotalValue()
    //             + " | risk=" + p.getRiskLevel());
    //     }

    //     if (!portfolios.isEmpty()) {
    //         int firstId = portfolios.get(0).getPortfolio_ID();
    //         System.out.println();
    //         System.out.println("Assets in portfolio " + firstId + ":");
    //         List<Asset> assets = getAssetsByPortfolioId(firstId);
    //         for (Asset a : assets) {
    //             System.out.println("  " + a.getAssetName()
    //                 + " (" + a.getAssetType() + "): "
    //                 + a.getAllocationPercentage() + "% = "
    //                 + a.getAmount());
    //         }
    //     }

    //     System.out.println();
    //     System.out.println("DatabaseManager working correctly!");
    // }

    public static int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM Users WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching user_id for email " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static int getLatestPortfolioId(int userId) {
        String sql = "SELECT portfolio_id FROM Portfolios WHERE user_id = ? ORDER BY portfolio_id DESC LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("portfolio_id");
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching latest portfolio_id for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static User getUserById(int userId) {
    String sql = "SELECT * FROM users WHERE user_id = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);

        try (ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("age"),
                    rs.getDouble("income"),
                    rs.getDouble("net_worth"),
                    rs.getString("security_question"),
                    rs.getString("security_answer"),
                    rs.getString("role")
                );
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}
}
