public class User {
    private int user_ID;
    private String name;
    private String email;
    private String password;
    private int age;
    private double income;
    private double netWorth;
    private String securityQ;
    private String securityA;
    private String role;

    /**
     * user constructor mainly for portfolio Builder
     * @param user_ID
     * @param name
     * @param email
     * @param password
     * @param age
     * @param income
     * @param netWorth
     */
    public User(
        int user_ID, 
        String name, 
        String email, 
        String password, 
        int age, 
        double income, 
        double netWorth,
        String securityQ,
        String securityA,
        String role){
            this.user_ID = user_ID;
            this.name = name;
            this.email = email;
            this.password = password;
            this.age = age;
            this.income = income;
            this.netWorth = netWorth;
            this.securityQ = securityQ;
            this.securityA = securityA;
            this.role = role;
    }

    /**
     * user constructor for sign-up page
     * where the user first creates their account
     * @param user_ID
     * @param name
     * @param email
     * @param password
     */
    public User(String name, String email, String password, String securityQ, String securityA){
        this.name = name;
        this.email = email;
        this.password = password;
        this.securityQ = securityQ;
        this.securityA = securityA;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getSecurityQ() {
        return securityQ;
    }

    public void setSecurityQ(String securityQ) {
        this.securityQ = securityQ;
    }

    public String getSecurityA() {
        return securityA;
    }

    public void setSecurityA(String securityA) {
        this.securityA = securityA;
    }

    public String getRole(){
        return this.role;
    }

    public boolean completeAccount(){
        return DatabaseManager.completeAccount(age, income, netWorth, email);
    }

    public boolean updateUser(int user_id, String name, String email, String password, Integer age, Double income, 
        Double netWorth, String securityQ, String securityA){
        return DatabaseManager.updateUserInfo(user_id, name, email, password, age, income, netWorth, securityQ, securityA);
    }

    public boolean deleteUser(int user_id){
        return DatabaseManager.deleteUser(user_id);
    }

    public boolean generatePortfolio(String portfolioName, double totalValue, String riskLevel) {
        return DatabaseManager.generatePortfolio(user_ID, portfolioName, totalValue, riskLevel);
    }

    public boolean deletePortfolio(int portfolio_id) {
        return DatabaseManager.deletePortfolio(portfolio_id);
    }

    public boolean addAsset(int portfolio_id, String asset_type, Double allocation_percentage, Double amount) {
        return DatabaseManager.addAsset(portfolio_id, asset_type, allocation_percentage, amount);
    }

    public boolean updateAsset(int asset_id, String asset_type, Double allocation_percentage, Double amount) {
        return DatabaseManager.updateAsset(asset_id, asset_type, allocation_percentage, amount);
    }

    public boolean deleteAsset(int asset_id) {
        return DatabaseManager.deleteAsset(asset_id);
    }

    public boolean generateAnalysis(int portfolioId, double estimatedValue, double projectedGrowth,
                                int simulationYear, double bestCase, double worstCase) {
        return DatabaseManager.generateAnalysis(portfolioId, estimatedValue, projectedGrowth, simulationYear, bestCase, worstCase);
    }

    public boolean login(String email, String password){
        return DatabaseManager.loginUser(email, password);
    }

    public boolean resetPassword(String email, String password){
        return DatabaseManager.resetPassword(email, password);
    }


 }
