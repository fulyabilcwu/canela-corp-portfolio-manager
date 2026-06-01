import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseManagerTest {

    private DatabaseManager database;

    @Before
    public void setUp() {
        database = new DatabaseManager();
    }

    @Test
    public void testDatabaseConnection() {
        Connection test1 = DatabaseManager.getConnection();

        assertNotNull(test1);
    }

    @Test
    public void testCreateUser() {
        boolean test1 = DatabaseManager.initiateUser(
                "John Carter",
                "johncar77@gmail.com",
                "SecurePass123",
                "What was the name of your first pet?",
                "Buddy"
        );

        boolean test2 = DatabaseManager.initiateUser(
                "Sarah Parker",
                "parsar77@gmail.com",
                "Finance2025",
                "What city are you from?",
                "Seattle"
        );

        boolean test3 = DatabaseManager.initiateUser(
                "Michael Lee",
                "miclee44@gmail.com",
                "InvestSmart",
                "What is your favorite food?",
                "Pizza"
        );

        boolean test4 = DatabaseManager.initiateUser(
                "Emily Johnson",
                "emily.johnson@gmail.com",
                "Portfolio456",
                "What is your favorite hobby?",
                "Reading"
        );

        boolean test5 = DatabaseManager.initiateUser(
                "David Wilson",
                "david.wilson@gmail.com",
                "MoneyPlan789",
                "What is your favorite sport?",
                "Soccer"
        );

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testLoginValidCredentials() {
        DatabaseManager.initiateUser(
                "John Carter",
                "johncar77@gmail.com",
                "SecurePass123",
                "What was the name of your first pet?",
                "Buddy"
        );

        DatabaseManager.initiateUser(
                "Sarah Parker",
                "parsar77@gmail.com",
                "Finance2025",
                "What city are you from?",
                "Seattle"
        );

        DatabaseManager.initiateUser(
                "Michael Lee",
                "miclee44@gmail.com",
                "InvestSmart",
                "What is your favorite food?",
                "Pizza"
        );

        DatabaseManager.initiateUser(
                "Emily Johnson",
                "emily.johnson@gmail.com",
                "Portfolio456",
                "What is your favorite hobby?",
                "Reading"
        );

        DatabaseManager.initiateUser(
                "David Wilson",
                "david.wilson@gmail.com",
                "MoneyPlan789",
                "What is your favorite sport?",
                "Soccer"
        );

        boolean test1 = DatabaseManager.loginUser("johncar77@gmail.com", "SecurePass123");
        boolean test2 = DatabaseManager.loginUser("parsar77@gmail.com", "Finance2025");
        boolean test3 = DatabaseManager.loginUser("miclee44@gmail.com", "InvestSmart");
        boolean test4 = DatabaseManager.loginUser("emily.johnson@gmail.com", "Portfolio456");
        boolean test5 = DatabaseManager.loginUser("david.wilson@gmail.com", "MoneyPlan789");

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testLoginInvalidCredentials() {
        DatabaseManager.initiateUser(
                "John Carter",
                "johncar77@gmail.com",
                "SecurePass123",
                "What was the name of your first pet?",
                "Buddy"
        );

        DatabaseManager.initiateUser(
                "Sarah Parker",
                "parsar77@gmail.com",
                "Finance2025",
                "What city are you from?",
                "Seattle"
        );

        DatabaseManager.initiateUser(
                "Michael Lee",
                "miclee44@gmail.com",
                "InvestSmart",
                "What is your favorite food?",
                "Pizza"
        );

        DatabaseManager.initiateUser(
                "Emily Johnson",
                "emily.johnson@gmail.com",
                "Portfolio456",
                "What is your favorite hobby?",
                "Reading"
        );

        DatabaseManager.initiateUser(
                "David Wilson",
                "david.wilson@gmail.com",
                "MoneyPlan789",
                "What is your favorite sport?",
                "Soccer"
        );

        boolean test1 = DatabaseManager.loginUser("johncar77@gmail.com", "WrongPassword");
        boolean test2 = DatabaseManager.loginUser("parsar77@gmail.com", "123456");
        boolean test3 = DatabaseManager.loginUser("miclee44@gmail.com", "IncorrectPass");
        boolean test4 = DatabaseManager.loginUser("emily.johnson@gmail.com", "");
        boolean test5 = DatabaseManager.loginUser("fakeuser@gmail.com", "MoneyPlan789");

        assertFalse(test1);
        assertFalse(test2);
        assertFalse(test3);
        assertFalse(test4);
        assertFalse(test5);
    }

    @Test
    public void testGeneratePortfolio() {
        DatabaseManager.initiateUser("John Carter", "johncar77@gmail.com", "SecurePass123", "Pet?", "Buddy");
        DatabaseManager.initiateUser("Sarah Parker", "parsar77@gmail.com", "Finance2025", "City?", "Seattle");
        DatabaseManager.initiateUser("Michael Lee", "miclee44@gmail.com", "InvestSmart", "Food?", "Pizza");
        DatabaseManager.initiateUser("Emily Johnson", "emily.johnson@gmail.com", "Portfolio456", "Hobby?", "Reading");
        DatabaseManager.initiateUser("David Wilson", "david.wilson@gmail.com", "MoneyPlan789", "Sport?", "Soccer");

        boolean test1 = DatabaseManager.completeAccount(25, 55000.0, 120000.0, "johncar77@gmail.com");
        boolean test2 = DatabaseManager.completeAccount(32, 85000.0, 250000.0, "parsar77@gmail.com");
        boolean test3 = DatabaseManager.completeAccount(40, 95000.0, 400000.0, "miclee44@gmail.com");
        boolean test4 = DatabaseManager.completeAccount(29, 62000.0, 150000.0, "emily.johnson@gmail.com");
        boolean test5 = DatabaseManager.completeAccount(50, 120000.0, 700000.0, "david.wilson@gmail.com");

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testgeneratePortfolio() {
        boolean test1 = DatabaseManager.generatePortfolio(
                6,
                "John Retirement Fund",
                150000.0,
                "LOW"
        );

        boolean test2 = DatabaseManager.generatePortfolio(
                7,
                "Sarah Balanced Portfolio",
                85000.0,
                "MEDIUM"
        );

        boolean test3 = DatabaseManager.generatePortfolio(
                8,
                "Michael Growth Portfolio",
                200000.0,
                "HIGH"
        );

        boolean test4 = DatabaseManager.generatePortfolio(
                9,
                "Emily Emergency Investment",
                45000.0,
                "LOW"
        );

        boolean test5 = DatabaseManager.generatePortfolio(
                10,
                "David Long Term Portfolio",
                300000.0,
                "MEDIUM"
        );

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testUpdateUserInfo() {
        boolean test1 = DatabaseManager.updateUserInfo(
                1,
                "John Carter Updated",
                "",
                "",
                null,
                null,
                null,
                "",
                ""
        );

        boolean test2 = DatabaseManager.updateUserInfo(
                2,
                "",
                "sarah.updated@gmail.com",
                "",
                null,
                null,
                null,
                "",
                ""
        );

        boolean test3 = DatabaseManager.updateUserInfo(
                3,
                "",
                "",
                "NewInvestPass123",
                null,
                null,
                null,
                "",
                ""
        );

        boolean test4 = DatabaseManager.updateUserInfo(
                4,
                "",
                "",
                "",
                30,
                70000.0,
                180000.0,
                "",
                ""
        );

        boolean test5 = DatabaseManager.updateUserInfo(
                5,
                "David Wilson Updated",
                "david.updated@gmail.com",
                "UpdatedPass789",
                52,
                130000.0,
                750000.0,
                "What is your favorite car?",
                "Toyota"
        );

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testUpdateAsset() {
        boolean test1 = DatabaseManager.updateAsset(
                1,
                "STOCK",
                40.0,
                25000.0
        );

        boolean test2 = DatabaseManager.updateAsset(
                2,
                "ETF",
                50.0,
                50000.0
        );

        boolean test3 = DatabaseManager.updateAsset(
                3,
                "BOND",
                20.0,
                15000.0
        );

        boolean test4 = DatabaseManager.updateAsset(
                4,
                "CASH",
                10.0,
                8000.0
        );

        boolean test5 = DatabaseManager.updateAsset(
                5,
                "REAL_ESTATE",
                60.0,
                200000.0
        );

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testDeleteUser() {
        boolean test1 = DatabaseManager.deleteUser(1);
        boolean test2 = DatabaseManager.deleteUser(2);
        boolean test3 = DatabaseManager.deleteUser(3);
        boolean test4 = DatabaseManager.deleteUser(4);
        boolean test5 = DatabaseManager.deleteUser(5);

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testDeletePortfolio() {
        boolean test1 = DatabaseManager.deletePortfolio(1);
        boolean test2 = DatabaseManager.deletePortfolio(2);
        boolean test3 = DatabaseManager.deletePortfolio(3);
        boolean test4 = DatabaseManager.deletePortfolio(4);
        boolean test5 = DatabaseManager.deletePortfolio(5);

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }

    @Test
    public void testDeleteAsset() {
        boolean test1 = DatabaseManager.deleteAsset(1);
        boolean test2 = DatabaseManager.deleteAsset(2);
        boolean test3 = DatabaseManager.deleteAsset(3);
        boolean test4 = DatabaseManager.deleteAsset(4);
        boolean test5 = DatabaseManager.deleteAsset(5);

        assertTrue(test1);
        assertTrue(test2);
        assertTrue(test3);
        assertTrue(test4);
        assertTrue(test5);
    }
}