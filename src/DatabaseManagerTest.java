// JUnit testing
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManagerTest {

    private String runId;
    private int fakeNameCounter;

    @Before
    public void setUp() throws Exception {
        runId = "dbtest_" + System.currentTimeMillis() + "_";
        fakeNameCounter = 1;
        cleanupTestData();
    }

    @After
    public void tearDown() throws Exception {
        cleanupTestData();
    }

    // CONNECTION

    @Test
    public void testGetConnection() throws Exception {
        Connection connection1 = DatabaseManager.getConnection();
        Connection connection2 = DatabaseManager.getConnection();
        Connection connection3 = DatabaseManager.getConnection();

        assertNotNull(connection1);
        assertFalse(connection1.isClosed());

        assertNotNull(connection2);
        assertFalse(connection2.isClosed());

        assertNotNull(connection3);
        assertFalse(connection3.isClosed());

        connection1.close();
        connection2.close();
        connection3.close();
    }

    // USER AUTH

    @Test
    public void testInitiateUser() {
        String email1 = fakeEmail("marta");
        String email2 = fakeEmail("leon");
        String email3 = fakeEmail("amina");
        String email4 = fakeEmail("caleb");
        String email5 = fakeEmail("sofia");

        assertTrue(DatabaseManager.initiateUser(fakeName("Marta Hale"), email1, "PassMarta123", "First pet?", "Maple"));
        assertTrue(DatabaseManager.initiateUser(fakeName("Leon Brooks"), email2, "PassLeon123", "Birth city?", "Tacoma"));
        assertTrue(DatabaseManager.initiateUser(fakeName("Amina Reyes"), email3, "PassAmina123", "Favorite food?", "Pasta"));
        assertTrue(DatabaseManager.initiateUser(fakeName("Caleb Stone"), email4, "PassCaleb123", "Favorite sport?", "Tennis"));
        assertTrue(DatabaseManager.initiateUser(fakeName("Sofia Reed"), email5, "PassSofia123", "Favorite color?", "Blue"));

        assertTrue(DatabaseManager.getUserIdByEmail(email1) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email2) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email3) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email4) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email5) > 0);
    }

    @Test
    public void testGetSecurityQuestion() {
        String email1 = createUser("Nora Finch", "FinchPass123", "Mother's maiden name?", "Bell");
        String email2 = createUser("Omar Lane", "LanePass123", "Favorite teacher?", "Mr. Shaw");
        String email3 = createUser("Iris Cole", "ColePass123", "First concert?", "Coldplay");

        assertEquals("Mother's maiden name?", DatabaseManager.getSecurityQuestion(email1));
        assertEquals("Favorite teacher?", DatabaseManager.getSecurityQuestion(email2));
        assertEquals("First concert?", DatabaseManager.getSecurityQuestion(email3));
        assertNull(DatabaseManager.getSecurityQuestion(fakeEmail("missing_question")));
    }

    @Test
    public void testVerifySecurityQuestion() {
        String email1 = createUser("Elena Cruz", "CruzPass123", "First pet?", "Luna");
        String email2 = createUser("Miles Grant", "GrantPass123", "Favorite city?", "Seattle");
        String email3 = createUser("Tara Mills", "MillsPass123", "Favorite snack?", "Chips");

        assertTrue(DatabaseManager.verifySecurityQuestion(email1, "Luna"));
        assertTrue(DatabaseManager.verifySecurityQuestion(email2, "  seattle  "));
        assertTrue(DatabaseManager.verifySecurityQuestion(email3, "CHIPS"));
        assertFalse(DatabaseManager.verifySecurityQuestion(email1, "Moon"));
        assertFalse(DatabaseManager.verifySecurityQuestion(fakeEmail("missing_verify"), "Anything"));
    }

    @Test
    public void testResetPassword() {
        String email1 = createUser("Jada North", "OldJada123", "First car?", "Civic");
        String email2 = createUser("Theo West", "OldTheo123", "Favorite movie?", "Up");
        String email3 = createUser("Mina Patel", "OldMina123", "Favorite animal?", "Cat");

        assertTrue(DatabaseManager.resetPassword(email1, "NewJada456"));
        assertTrue(DatabaseManager.loginUser(email1, "NewJada456"));
        assertFalse(DatabaseManager.loginUser(email1, "OldJada123"));

        assertTrue(DatabaseManager.resetPassword(email2, "NewTheo456"));
        assertTrue(DatabaseManager.loginUser(email2, "NewTheo456"));

        assertTrue(DatabaseManager.resetPassword(email3, "NewMina456"));
        assertTrue(DatabaseManager.loginUser(email3, "NewMina456"));

        // Current method returns true when SQL runs, even if no matching email exists.
        assertTrue(DatabaseManager.resetPassword(fakeEmail("missing_reset"), "NoUser123"));
    }

    @Test
    public void testLoginUser() {
        String email1 = createUser("Rafael King", "Rafael123", "Favorite app?", "Maps");
        String email2 = createUser("Lina Ross", "Lina123", "Favorite fruit?", "Mango");
        String email3 = createUser("Evan Moore", "Evan123", "Favorite book?", "Dune");

        assertTrue(DatabaseManager.loginUser(email1, "Rafael123"));
        assertTrue(DatabaseManager.loginUser(email2, "Lina123"));
        assertTrue(DatabaseManager.loginUser(email3, "Evan123"));
        assertFalse(DatabaseManager.loginUser(email1, "WrongPassword"));
        assertFalse(DatabaseManager.loginUser(fakeEmail("missing_login"), "Evan123"));
    }

    @Test
    public void testUpdateUserInfo() {
        String email1 = createUser("Paula Gray", "Paula123", "Favorite season?", "Fall");
        String email2 = createUser("Derek Blue", "Derek123", "Favorite drink?", "Tea");
        String email3 = createUser("Naomi Wells", "Naomi123", "Favorite place?", "Library");
        int user1 = DatabaseManager.getUserIdByEmail(email1);
        int user2 = DatabaseManager.getUserIdByEmail(email2);
        int user3 = DatabaseManager.getUserIdByEmail(email3);

        assertTrue(DatabaseManager.updateUserInfo(user1, fakeName("Paula Green"), "", "", null, null, null, "", ""));
        assertEquals("Paula Green", stripRunId(DatabaseManager.getUserById(user1).getName()));

        assertTrue(DatabaseManager.updateUserInfo(user2, "", "", "DerekNew123", 33, 82000.00, 120000.00, "", ""));
        User updated2 = DatabaseManager.getUserById(user2);
        assertEquals(33, updated2.getAge());
        assertEquals(82000.00, updated2.getIncome(), 0.01);
        assertEquals(120000.00, updated2.getNetWorth(), 0.01);
        assertTrue(DatabaseManager.loginUser(email2, "DerekNew123"));

        String newEmail3 = fakeEmail("naomi_updated");
        assertTrue(DatabaseManager.updateUserInfo(user3, "", newEmail3, "", null, null, null, "Updated question?", "Updated answer"));
        assertEquals(user3, DatabaseManager.getUserIdByEmail(newEmail3));
        assertEquals("Updated question?", DatabaseManager.getSecurityQuestion(newEmail3));
        assertTrue(DatabaseManager.verifySecurityQuestion(newEmail3, "updated answer"));

        assertFalse(DatabaseManager.updateUserInfo(user1, "", "", "", null, null, null, "", ""));
    }

    @Test
    public void testDeleteUser() {
        String email1 = createUser("Victor Stone", "Victor123", "Favorite team?", "Sounders");
        String email2 = createUser("Hazel Moon", "Hazel123", "Favorite flower?", "Rose");
        String email3 = createUser("Felix Wood", "Felix123", "Favorite game?", "Chess");
        int user1 = DatabaseManager.getUserIdByEmail(email1);
        int user2 = DatabaseManager.getUserIdByEmail(email2);
        int user3 = DatabaseManager.getUserIdByEmail(email3);

        assertTrue(DatabaseManager.deleteUser(user1));
        assertNull(DatabaseManager.getUserById(user1));

        assertTrue(DatabaseManager.deleteUser(user2));
        assertNull(DatabaseManager.getUserById(user2));

        assertTrue(DatabaseManager.deleteUser(user3));
        assertNull(DatabaseManager.getUserById(user3));

        // Current method returns true when SQL runs, even if no row is deleted.
        assertTrue(DatabaseManager.deleteUser(-999999));
    }

    @Test
    public void testCompleteAccount() {
        String email1 = createUser("Grace Park", "Grace123", "Favorite river?", "Nile");
        String email2 = createUser("Henry Blake", "Henry123", "Favorite mountain?", "Rainier");
        String email3 = createUser("Olive Scott", "Olive123", "Favorite planet?", "Mars");

        assertTrue(DatabaseManager.completeAccount(24, 56000.00, 21000.00, email1));
        assertTrue(DatabaseManager.completeAccount(41, 91000.00, 145000.00, email2));
        assertTrue(DatabaseManager.completeAccount(30, 72000.00, 68000.00, email3));
        assertTrue(DatabaseManager.completeAccount(99, 1.00, 2.00, fakeEmail("missing_complete")));

        User user1 = DatabaseManager.getUserById(DatabaseManager.getUserIdByEmail(email1));
        User user2 = DatabaseManager.getUserById(DatabaseManager.getUserIdByEmail(email2));
        User user3 = DatabaseManager.getUserById(DatabaseManager.getUserIdByEmail(email3));

        assertEquals(24, user1.getAge());
        assertEquals(56000.00, user1.getIncome(), 0.01);
        assertEquals(21000.00, user1.getNetWorth(), 0.01);

        assertEquals(41, user2.getAge());
        assertEquals(91000.00, user2.getIncome(), 0.01);
        assertEquals(145000.00, user2.getNetWorth(), 0.01);

        assertEquals(30, user3.getAge());
        assertEquals(72000.00, user3.getIncome(), 0.01);
        assertEquals(68000.00, user3.getNetWorth(), 0.01);
    }

    // PORTFOLIO METHODS

    @Test
    public void testGeneratePortfolio() {
        int userId = createUserAndGetId("Brianna Sharp", "Sharp123", "Favorite lake?", "Union");

        assertTrue(DatabaseManager.generatePortfolio(userId, fakePortfolioName("Growth Plan"), 15000.00, "HIGH"));
        assertTrue(DatabaseManager.generatePortfolio(userId, fakePortfolioName("Safety Plan"), 8000.00, "LOW"));
        assertTrue(DatabaseManager.generatePortfolio(userId, fakePortfolioName("Balanced Plan"), 12000.00, "MEDIUM"));
        assertTrue(DatabaseManager.generatePortfolio(userId, fakePortfolioName("ETF Plan"), 25000.00, "MEDIUM"));

        List<Portfolio> portfolios = DatabaseManager.getPortfoliosByUserId(userId);
        assertEquals(4, countRunPortfolios(portfolios));
    }

    @Test
    public void testUpdatePortfolio() {
        int user1 = createUserAndGetId("Oscar Young", "Oscar123", "Favorite show?", "Lost");
        int user2 = createUserAndGetId("Penny Wells", "Penny123", "Favorite dessert?", "Cake");
        int p1 = createPortfolio(user1, "Original Growth", 10000.00, "HIGH");
        int p2 = createPortfolio(user1, "Original Safety", 5000.00, "LOW");
        int p3 = createPortfolio(user1, "Original Balance", 7500.00, "MEDIUM");

        assertTrue(DatabaseManager.updatePortfolio(p1, null, fakePortfolioName("Updated Growth"), null, null));
        assertEquals("Updated Growth", stripRunId(DatabaseManager.getPortfolioById(p1).getPortfolioName()));

        assertTrue(DatabaseManager.updatePortfolio(p2, null, "", 9500.00, "MEDIUM"));
        Portfolio updated2 = DatabaseManager.getPortfolioById(p2);
        assertEquals(9500.00, updated2.getTotalValue(), 0.01);
        assertEquals("MEDIUM", updated2.getRiskLevel());

        assertTrue(DatabaseManager.updatePortfolio(p3, user2, fakePortfolioName("Transferred Portfolio"), 20000.00, "LOW"));
        Portfolio updated3 = DatabaseManager.getPortfolioById(p3);
        assertEquals(user2, updated3.getUser_ID());
        assertEquals("Transferred Portfolio", stripRunId(updated3.getPortfolioName()));
        assertEquals(20000.00, updated3.getTotalValue(), 0.01);
        assertEquals("LOW", updated3.getRiskLevel());

        assertFalse(DatabaseManager.updatePortfolio(p1, null, "", null, ""));
    }

    @Test
    public void testDeletePortfolio() {
        int userId = createUserAndGetId("Quinn Hart", "Quinn123", "Favorite beach?", "Alki");
        int p1 = createPortfolio(userId, "Delete One", 1000.00, "LOW");
        int p2 = createPortfolio(userId, "Delete Two", 2000.00, "MEDIUM");
        int p3 = createPortfolio(userId, "Delete Three", 3000.00, "HIGH");

        assertTrue(DatabaseManager.deletePortfolio(p1));
        assertNull(DatabaseManager.getPortfolioById(p1));

        assertTrue(DatabaseManager.deletePortfolio(p2));
        assertNull(DatabaseManager.getPortfolioById(p2));

        assertTrue(DatabaseManager.deletePortfolio(p3));
        assertNull(DatabaseManager.getPortfolioById(p3));

        // Current method returns true when SQL runs, even if no row is deleted.
        assertTrue(DatabaseManager.deletePortfolio(-999999));
    }

    @Test
    public void testGetAllPortfolios() {
        int userId = createUserAndGetId("Rose Ellis", "Rose123", "Favorite bird?", "Robin");
        int p1 = createPortfolio(userId, "All Portfolio One", 1111.00, "LOW");
        int p2 = createPortfolio(userId, "All Portfolio Two", 2222.00, "MEDIUM");
        int p3 = createPortfolio(userId, "All Portfolio Three", 3333.00, "HIGH");

        List<Portfolio> portfolios = DatabaseManager.getAllPortfolios();

        assertTrue(containsPortfolioId(portfolios, p1));
        assertTrue(containsPortfolioId(portfolios, p2));
        assertTrue(containsPortfolioId(portfolios, p3));
        assertTrue(portfolios.size() >= 3);
    }

    @Test
    public void testGetPortfolioById() {
        int userId = createUserAndGetId("Samira Lake", "Samira123", "Favorite tree?", "Cedar");
        int p1 = createPortfolio(userId, "Lookup Growth", 4444.00, "HIGH");
        int p2 = createPortfolio(userId, "Lookup Safe", 5555.00, "LOW");
        int p3 = createPortfolio(userId, "Lookup Balanced", 6666.00, "MEDIUM");

        assertEquals("Lookup Growth", stripRunId(DatabaseManager.getPortfolioById(p1).getPortfolioName()));
        assertEquals("Lookup Safe", stripRunId(DatabaseManager.getPortfolioById(p2).getPortfolioName()));
        assertEquals("Lookup Balanced", stripRunId(DatabaseManager.getPortfolioById(p3).getPortfolioName()));
        assertNull(DatabaseManager.getPortfolioById(-999999));
    }

    @Test
    public void testGetPortfoliosByUserId() {
        int user1 = createUserAndGetId("Tessa James", "Tessa123", "Favorite number?", "Seven");
        int user2 = createUserAndGetId("Uri Stone", "Uri123", "Favorite card?", "Ace");
        createPortfolio(user1, "User One Growth", 7000.00, "HIGH");
        createPortfolio(user1, "User One Safe", 3000.00, "LOW");
        createPortfolio(user1, "User One Balanced", 5000.00, "MEDIUM");
        createPortfolio(user2, "User Two Separate", 9000.00, "HIGH");

        List<Portfolio> user1Portfolios = DatabaseManager.getPortfoliosByUserId(user1);
        List<Portfolio> user2Portfolios = DatabaseManager.getPortfoliosByUserId(user2);
        List<Portfolio> missingPortfolios = DatabaseManager.getPortfoliosByUserId(-999999);

        assertEquals(3, countRunPortfolios(user1Portfolios));
        assertEquals(1, countRunPortfolios(user2Portfolios));
        assertEquals(0, missingPortfolios.size());
        assertTrue(allPortfoliosBelongToUser(user1Portfolios, user1));
    }

    @Test
    public void testGetLatestPortfolioId() {
        int user1 = createUserAndGetId("Vera Kim", "Vera123", "Favorite road?", "I5");
        int user2 = createUserAndGetId("Will Page", "Will123", "Favorite cloud?", "Cumulus");
        int first = createPortfolio(user1, "First Latest", 1000.00, "LOW");
        int second = createPortfolio(user1, "Second Latest", 2000.00, "MEDIUM");
        int third = createPortfolio(user1, "Third Latest", 3000.00, "HIGH");
        int otherUserLatest = createPortfolio(user2, "Other User Latest", 4000.00, "LOW");

        assertTrue(first > 0);
        assertTrue(second > first);
        assertEquals(third, DatabaseManager.getLatestPortfolioId(user1));
        assertEquals(otherUserLatest, DatabaseManager.getLatestPortfolioId(user2));
        assertEquals(-1, DatabaseManager.getLatestPortfolioId(-999999));
    }

    // ASSET METHODS

    @Test
    public void testAddAsset() {
        int userId = createUserAndGetId("Yara King", "Yara123", "Favorite store?", "Target");
        int portfolioId = createPortfolio(userId, "Asset Portfolio", 20000.00, "MEDIUM");

        assertTrue(DatabaseManager.addAsset(portfolioId, "STOCK", 40.00, 8000.00));
        assertTrue(DatabaseManager.addAsset(portfolioId, "BOND", 25.00, 5000.00));
        assertTrue(DatabaseManager.addAsset(portfolioId, "ETF", 20.00, 4000.00));
        assertTrue(DatabaseManager.addAsset(portfolioId, "CASH", 15.00, 3000.00));

        List<Asset> assets = DatabaseManager.getAssetsByPortfolioId(portfolioId);
        assertEquals(4, assets.size());
    }

    @Test
    public void testUpdateAsset() {
        int userId = createUserAndGetId("Zane Ward", "Zane123", "Favorite tool?", "Hammer");
        int portfolioId = createPortfolio(userId, "Update Asset Portfolio", 30000.00, "HIGH");
        int asset1 = createAsset(portfolioId, "STOCK", 50.00, 15000.00);
        int asset2 = createAsset(portfolioId, "BOND", 30.00, 9000.00);
        int asset3 = createAsset(portfolioId, "CASH", 20.00, 6000.00);

        assertTrue(DatabaseManager.updateAsset(asset1, "ETF", null, null));
        assertEquals("ETF", DatabaseManager.getAssetByID(asset1).getAssetType());

        assertTrue(DatabaseManager.updateAsset(asset2, "", 35.00, null));
        assertEquals(35.00, DatabaseManager.getAssetByID(asset2).getAllocationPercentage(), 0.01);

        assertTrue(DatabaseManager.updateAsset(asset3, "GOLD", 25.00, 7500.00));
        Asset updated3 = DatabaseManager.getAssetByID(asset3);
        assertEquals("GOLD", updated3.getAssetType());
        assertEquals(25.00, updated3.getAllocationPercentage(), 0.01);
        assertEquals(7500.00, updated3.getAmount(), 0.01);

        assertFalse(DatabaseManager.updateAsset(asset1, "", null, null));
    }

    @Test
    public void testDeleteAsset() {
        int userId = createUserAndGetId("Amber Shaw", "Amber123", "Favorite song?", "Imagine");
        int portfolioId = createPortfolio(userId, "Delete Asset Portfolio", 18000.00, "LOW");
        int asset1 = createAsset(portfolioId, "STOCK", 40.00, 7200.00);
        int asset2 = createAsset(portfolioId, "BOND", 40.00, 7200.00);
        int asset3 = createAsset(portfolioId, "CASH", 20.00, 3600.00);

        assertTrue(DatabaseManager.deleteAsset(asset1));
        assertNull(DatabaseManager.getAssetByID(asset1));

        assertTrue(DatabaseManager.deleteAsset(asset2));
        assertNull(DatabaseManager.getAssetByID(asset2));

        assertTrue(DatabaseManager.deleteAsset(asset3));
        assertNull(DatabaseManager.getAssetByID(asset3));

        // Current method returns true when SQL runs, even if no row is deleted.
        assertTrue(DatabaseManager.deleteAsset(-999999));
    }

    @Test
    public void testGetAllAssets() {
        int userId = createUserAndGetId("Ben Tyler", "Ben123", "Favorite class?", "CS");
        int portfolioId = createPortfolio(userId, "All Assets Portfolio", 50000.00, "MEDIUM");
        int asset1 = createAsset(portfolioId, "STOCK", 30.00, 15000.00);
        int asset2 = createAsset(portfolioId, "ETF", 30.00, 15000.00);
        int asset3 = createAsset(portfolioId, "REAL_ESTATE", 40.00, 20000.00);

        List<Asset> assets = DatabaseManager.getallAsets();

        assertTrue(containsAssetId(assets, asset1));
        assertTrue(containsAssetId(assets, asset2));
        assertTrue(containsAssetId(assets, asset3));
        assertTrue(assets.size() >= 3);
    }

    @Test
    public void testGetAssetByID() {
        int userId = createUserAndGetId("Cora Miles", "Cora123", "Favorite IDE?", "VS Code");
        int portfolioId = createPortfolio(userId, "Lookup Asset Portfolio", 40000.00, "HIGH");
        int asset1 = createAsset(portfolioId, "STOCK", 60.00, 24000.00);
        int asset2 = createAsset(portfolioId, "GOLD", 20.00, 8000.00);
        int asset3 = createAsset(portfolioId, "CASH", 20.00, 8000.00);

        assertEquals("STOCK", DatabaseManager.getAssetByID(asset1).getAssetType());
        assertEquals("GOLD", DatabaseManager.getAssetByID(asset2).getAssetType());
        assertEquals("CASH", DatabaseManager.getAssetByID(asset3).getAssetType());
        assertNull(DatabaseManager.getAssetByID(-999999));
    }

    @Test
    public void testGetAssetsByPortfolioId() {
        int userId = createUserAndGetId("Dina Clark", "Dina123", "Favorite editor?", "Vim");
        int portfolio1 = createPortfolio(userId, "Assets Portfolio One", 60000.00, "HIGH");
        int portfolio2 = createPortfolio(userId, "Assets Portfolio Two", 10000.00, "LOW");
        createAsset(portfolio1, "STOCK", 50.00, 30000.00);
        createAsset(portfolio1, "ETF", 30.00, 18000.00);
        createAsset(portfolio1, "BOND", 20.00, 12000.00);
        createAsset(portfolio2, "CASH", 100.00, 10000.00);

        List<Asset> portfolio1Assets = DatabaseManager.getAssetsByPortfolioId(portfolio1);
        List<Asset> portfolio2Assets = DatabaseManager.getAssetsByPortfolioId(portfolio2);
        List<Asset> missingAssets = DatabaseManager.getAssetsByPortfolioId(-999999);

        assertEquals(3, portfolio1Assets.size());
        assertEquals(1, portfolio2Assets.size());
        assertEquals(0, missingAssets.size());
        assertTrue(allAssetsBelongToPortfolio(portfolio1Assets, portfolio1));
    }

    // USER READ METHODS

    @Test
    public void testGetAllUsers() {
        int user1 = createUserAndGetId("Eli Ford", "Eli123", "Favorite browser?", "Firefox");
        int user2 = createUserAndGetId("Faye Hall", "Faye123", "Favorite shell?", "Bash");
        int user3 = createUserAndGetId("Gavin Lee", "Gavin123", "Favorite OS?", "Linux");

        List<User> users = DatabaseManager.getAllUsers();

        assertTrue(containsUserId(users, user1));
        assertTrue(containsUserId(users, user2));
        assertTrue(containsUserId(users, user3));
        assertTrue(users.size() >= 3);
    }

    @Test
    public void testGetUserIdByEmail() {
        String email1 = createUser("Hana Reed", "Hana123", "Favorite chip?", "Doritos");
        String email2 = createUser("Ivan Snow", "Ivan123", "Favorite laptop?", "ThinkPad");
        String email3 = createUser("Jules Ray", "Jules123", "Favorite phone?", "Pixel");

        assertTrue(DatabaseManager.getUserIdByEmail(email1) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email2) > 0);
        assertTrue(DatabaseManager.getUserIdByEmail(email3) > 0);
        assertEquals(-1, DatabaseManager.getUserIdByEmail(fakeEmail("missing_id")));
    }

    @Test
    public void testGetUserById() {
        int user1 = createUserAndGetId("Kira Bell", "Kira123", "Favorite database?", "MySQL");
        int user2 = createUserAndGetId("Luca Price", "Luca123", "Favorite language?", "Java");
        int user3 = createUserAndGetId("Maya Cross", "Maya123", "Favorite pattern?", "Facade");

        assertEquals("Kira Bell", stripRunId(DatabaseManager.getUserById(user1).getName()));
        assertEquals("Luca Price", stripRunId(DatabaseManager.getUserById(user2).getName()));
        assertEquals("Maya Cross", stripRunId(DatabaseManager.getUserById(user3).getName()));
        assertNull(DatabaseManager.getUserById(-999999));
    }

    // PRIVATE HELPER METHOD IN DatabaseManager

    @Test
    public void testFormatAssetNameUsingReflection() throws Exception {
        Method method = DatabaseManager.class.getDeclaredMethod("formatAssetName", String.class);
        method.setAccessible(true);

        assertEquals("Stock Holdings", (String) method.invoke(null, "STOCK"));
        assertEquals("Real Estate Holdings", (String) method.invoke(null, "REAL_ESTATE"));
        assertEquals("Cash Holdings", (String) method.invoke(null, "cash"));
        assertEquals("Unknown Asset", (String) method.invoke(null, new Object[] { null }));
    }

    // TEST HELPERS

    private String createUser(String baseName, String password, String question, String answer) {
        String email = fakeEmail(baseName.toLowerCase().replaceAll("[^a-z]", ""));
        assertTrue(DatabaseManager.initiateUser(fakeName(baseName), email, password, question, answer));
        assertTrue(DatabaseManager.getUserIdByEmail(email) > 0);
        return email;
    }

    private int createUserAndGetId(String baseName, String password, String question, String answer) {
        String email = createUser(baseName, password, question, answer);
        return DatabaseManager.getUserIdByEmail(email);
    }

    private int createPortfolio(int userId, String baseName, double totalValue, String riskLevel) {
        assertTrue(DatabaseManager.generatePortfolio(userId, fakePortfolioName(baseName), totalValue, riskLevel));
        int portfolioId = DatabaseManager.getLatestPortfolioId(userId);
        assertTrue(portfolioId > 0);
        return portfolioId;
    }

    private int createAsset(int portfolioId, String assetType, double allocation, double amount) {
        assertTrue(DatabaseManager.addAsset(portfolioId, assetType, allocation, amount));
        int assetId = getLatestAssetId(portfolioId);
        assertTrue(assetId > 0);
        return assetId;
    }

    private String fakeName(String baseName) {
        return runId + baseName + " " + (fakeNameCounter++);
    }

    private String fakePortfolioName(String baseName) {
        return runId + baseName;
    }

    private String fakeEmail(String label) {
        return runId + label.toLowerCase().replaceAll("[^a-z0-9]", "") + "@example.com";
    }

    private String stripRunId(String value) {
        if (value == null) {
            return null;
        }
        String stripped = value.replace(runId, "");
        return stripped.replaceAll("\\s+\\d+$", "");
    }

    private int getLatestAssetId(int portfolioId) {
        String sql = "SELECT asset_id FROM assets WHERE portfolio_id = ? ORDER BY asset_id DESC LIMIT 1";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("asset_id");
                }
            }
        } catch (SQLException e) {
            fail("Could not get latest asset id: " + e.getMessage());
        }
        return -1;
    }

    private int countAnalysesForPortfolio(int portfolioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PortfolioAnalyzer WHERE portfolio_id = ?";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, portfolioId);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private boolean containsUserId(List<User> users, int userId) {
        for (User user : users) {
            if (user.getUser_ID() == userId) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPortfolioId(List<Portfolio> portfolios, int portfolioId) {
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolio_ID() == portfolioId) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAssetId(List<Asset> assets, int assetId) {
        for (Asset asset : assets) {
            if (asset.getAsset_ID() == assetId) {
                return true;
            }
        }
        return false;
    }

    private boolean allPortfoliosBelongToUser(List<Portfolio> portfolios, int userId) {
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolioName() != null && portfolio.getPortfolioName().startsWith(runId)
                    && portfolio.getUser_ID() != userId) {
                return false;
            }
        }
        return true;
    }

    private boolean allAssetsBelongToPortfolio(List<Asset> assets, int portfolioId) {
        for (Asset asset : assets) {
            if (asset.getPortfolio_ID() != portfolioId) {
                return false;
            }
        }
        return true;
    }

    private int countRunPortfolios(List<Portfolio> portfolios) {
        int count = 0;
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolioName() != null && portfolio.getPortfolioName().startsWith(runId)) {
                count++;
            }
        }
        return count;
    }

    private Connection requireConnection() throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        assertNotNull("Database connection should not be null. Check MySQL, schema name, username, and password.", conn);
        return conn;
    }

    private void cleanupTestData() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn == null) {
                return;
            }
            executeSilently(conn,
                    "DELETE pa FROM PortfolioAnalyzer pa "
                            + "JOIN Portfolios p ON pa.portfolio_id = p.portfolio_id "
                            + "JOIN Users u ON p.user_id = u.user_id "
                            + "WHERE u.email LIKE 'dbtest_%@example.com'");

            executeSilently(conn,
                    "DELETE a FROM Assets a "
                            + "JOIN Portfolios p ON a.portfolio_id = p.portfolio_id "
                            + "JOIN Users u ON p.user_id = u.user_id "
                            + "WHERE u.email LIKE 'dbtest_%@example.com'");

            executeSilently(conn,
                    "DELETE p FROM Portfolios p "
                            + "JOIN Users u ON p.user_id = u.user_id "
                            + "WHERE u.email LIKE 'dbtest_%@example.com'");

            executeSilently(conn,
                    "DELETE FROM Users WHERE email LIKE 'dbtest_%@example.com'");
        }
    }

    private void executeSilently(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ignored) {
        }
    }
}
