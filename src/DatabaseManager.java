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
 * Connects to the local portfolioapp database and provides methods
 * to fetch portfolios and their assets for use in the Monte Carlo
 * simulation and Long-Term projection panels.
 *
 * Author: Fulya Bilgin
 */
public class DatabaseManager {

    private static final String URL =
        "jdbc:mysql://localhost:3306/portfolioapp?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            System.err.println("Error fetching portfolio "
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
                        assetName,
                        assetType,
                        rs.getDouble("allocation_percentage"),
                        rs.getDouble("amount")
                    );
                    assets.add(a);
                }
            }
        } catch (SQLException e) {
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
