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

    /**
     * user constructor mainly for portfolio Builder
     * @param user_ID
     * @param name
     * @param email
     * @param password
     * @param age
     * @param income
     * @param riskTolerance
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
        String securityA){
            this.user_ID = user_ID;
            this.name = name;
            this.email = email;
            this.password = password;
            this.age = age;
            this.income = income;
            this.netWorth = netWorth;
            this.securityQ = securityQ;
            this.securityA = securityA;
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

    public boolean generatePortfolio(){
        return DatabaseManager.generatePortfolio(age, income, netWorth, email);
    }

    public boolean savePortfolio(String portfolioName, double totalValue, String riskLevel) {
        return DatabaseManager.addPortfolio(user_ID, portfolioName, totalValue, riskLevel);
    }

    public boolean addAsset(int portfolioId, String assetType, double allocationPercentage, double amount) {
        return DatabaseManager.addAsset(portfolioId, assetType, allocationPercentage, amount);
    }

    public boolean saveAnalysis(int portfolioId, double estimatedValue, double projectedGrowth,
                                int simulationYear, double bestCase, double worstCase) {
        return DatabaseManager.saveAnalysis(portfolioId, estimatedValue, projectedGrowth, simulationYear, bestCase, worstCase);
    }
 }
