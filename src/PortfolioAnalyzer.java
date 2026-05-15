public class PortfolioAnalyzer {
    private int analysis_ID;
    private int portfolio_ID;
    private double bestCase;
    private double worstCase;
    private double estimatedValue;
    private double projectedGrowth;
    private int simulationYear;

    public PortfolioAnalyzer(
        int analysis_ID,
        int portfolio_ID,
        double bestCase,
        double worstCase,
        double estimatedValue,
        double projectedGrowth,
        int simulationYear
        ){
            this.analysis_ID = analysis_ID;
            this.portfolio_ID = portfolio_ID;
            this.bestCase = bestCase;
            this.worstCase = worstCase;
            this.estimatedValue = estimatedValue;
            this.projectedGrowth = projectedGrowth;
            this.simulationYear = simulationYear;
        }

    public int getAnalysis_ID() {
        return analysis_ID;
    }

    public void setAnalysis_ID(int analysis_ID) {
        this.analysis_ID = analysis_ID;
    }

    public int getPortfolio_ID() {
        return portfolio_ID;
    }

    public void setPortfolio_ID(int portfolio_ID) {
        this.portfolio_ID = portfolio_ID;
    }

    public double getBestCase() {
        return bestCase;
    }

    public void setBestCase(double bestCase) {
        this.bestCase = bestCase;
    }

    public double getWorstCase() {
        return worstCase;
    }

    public void setWorstCase(double worstCase) {
        this.worstCase = worstCase;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public double getProjectedGrowth() {
        return projectedGrowth;
    }

    public void setProjectedGrowth(double projectedGrowth) {
        this.projectedGrowth = projectedGrowth;
    }

    public int getSimulationYear() {
        return simulationYear;
    }

    public void setSimulationYear(int simulationYear) {
        this.simulationYear = simulationYear;
    }

 }
