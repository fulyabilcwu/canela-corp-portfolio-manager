package portfolioBuilderGUI;

public class User {
    private int user_ID;
    private String name;
    private String email;
    private String password;
    private int age;
    private double income;
    private String riskTolerance;
    private double netWorth;

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
        String riskTolerance,
        double netWorth){
            this.user_ID = user_ID;
            this.name = name;
            this.email = email;
            this.password = password;
            this.age = age;
            this.income = income;
            this.riskTolerance = riskTolerance;
            this.netWorth = netWorth;
    }

    /**
     * user constructor for sign-up page
     * where the user first creates their account
     * @param user_ID
     * @param name
     * @param email
     * @param password
     */
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getRiskTolerance() {
        return riskTolerance;
    }

    public void setRiskTolerance(String riskTolerance) {
        this.riskTolerance = riskTolerance;
    }

 }

