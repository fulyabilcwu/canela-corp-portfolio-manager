public class Admin extends User{
    public Admin(int user_ID, String name, String email, String password,
                 int age, double income, double netWorth, String securityQ, String securityA) {
        super(user_ID, name, email, password, age, income, netWorth, securityQ, securityA);
    }

    public boolean deleteUser(int userId) {
        return DatabaseManager.deleteUser(userId);
    }

    public boolean updateUserInfo(int userId, String name, String email, String password, Integer age,
                                  Double income, Double netWorth, String securityQ, String securityA) {
        return DatabaseManager.updateUserInfo(
                userId,
                name,
                email,
                password,
                age,
                income,
                netWorth,
                securityQ, securityA
        );
    }

    public boolean deletePortfolio(int portfolioId) {
        return DatabaseManager.deletePortfolio(portfolioId);
    }

    public boolean deleteAsset(int assetId) {
        return DatabaseManager.deleteAsset(assetId);
    }

    public boolean updateAsset(int asset_id, String asset_type,
                               Double allocation_percentage, Double amount) {
        return DatabaseManager.updateAsset(
                asset_id,
                asset_type,
                allocation_percentage,
                amount
        );
    }

    public boolean adminLogin(String email, String password) {
        return DatabaseManager.loginUser(email, password);
    }
}