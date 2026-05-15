public class Portfolio {
    private int portfolio_ID;
    private int user_ID;
    private String portfolioName;
    private double totalValue;
    private String riskLevel;


    public Portfolio(
        int portfolio_ID,
        int user_ID,
        String portfolioName,
        double totalValue,
        String riskLevel
    ){
        this.portfolio_ID = portfolio_ID;
        this.user_ID = user_ID;
        this.portfolioName = portfolioName;
        this.totalValue = totalValue;
        this.riskLevel = riskLevel;
    }


    public int getPortfolio_ID() {
        return portfolio_ID;
    }


    public void setPortfolio_ID(int portfolio_ID) {
        this.portfolio_ID = portfolio_ID;
    }


    public int getUser_ID() {
        return user_ID;
    }


    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }


    public String getPortfolioName() {
        return portfolioName;
    }


    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }


    public double getTotalValue() {
        return totalValue;
    }


    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }


    public String getRiskLevel() {
        return riskLevel;
    }


    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

 }
