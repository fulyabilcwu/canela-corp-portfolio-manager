import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Runs a Monte Carlo simulation on a Portfolio to estimate future value
 * under market uncertainty. Produces estimated value, best-case (95th
 * percentile), and worst-case (5th percentile) scenarios.
 *
 * Expected return and volatility are calculated as a weighted average
 * of each asset's type, using the asset's allocation percentage.
 *
 * Phase 3: expected return AND volatility are now pulled from REAL market
 * data via MarketDataService (Alpha Vantage), which fetches historical prices
 * per asset class and computes the annualized return (CAGR) and the annualized
 * standard deviation of monthly returns. No more hardcoded constants.
 */
public class MonteCarloSimulator {

    // Number of simulated future scenarios. 10,000 gives stable results.
    private static final int NUM_SIMULATIONS = 10000;

    // Annual expected return now comes from REAL market data
    // (historical prices via the Alpha Vantage API), not hardcoded values.
    private static double getExpectedReturn(String assetType) {
        return MarketDataService.getExpectedReturn(assetType);
    }

    // Annual volatility now comes from REAL market data: the standard
    // deviation of the asset's historical monthly returns, annualized.
    private static double getVolatility(String assetType) {
        return MarketDataService.getVolatility(assetType);
    }

    /**
     * Runs the Monte Carlo simulation.
     *
     * @param portfolio the portfolio being analyzed
     * @param assets    the list of assets within that portfolio
     * @param years     how many years into the future to simulate
     * @return a PortfolioAnalyzer object holding the results
     */
    public PortfolioAnalyzer runSimulation(Portfolio portfolio, List<Asset> assets, int years) {

        // Step 1: Calculate weighted expected return and volatility
        // for the whole portfolio based on each asset's allocation %.
        double weightedReturn = 0.0;
        double weightedVolatility = 0.0;

        for (Asset asset : assets) {
            double weight = asset.getAllocationPercentage() / 100.0;
            weightedReturn     += weight * getExpectedReturn(asset.getAssetType());
            weightedVolatility += weight * getVolatility(asset.getAssetType());
        }

        // Step 2: Run NUM_SIMULATIONS independent simulations.
        // Each simulation rolls a random annual return for each year.
        double startingValue = portfolio.getTotalValue();
        Random random = new Random();
        List<Double> finalValues = new ArrayList<>();

        for (int sim = 0; sim < NUM_SIMULATIONS; sim++) {
            double value = startingValue;

            for (int y = 0; y < years; y++) {
                // Draw from a normal distribution: mean = weightedReturn,
                // stddev = weightedVolatility. nextGaussian() gives N(0,1).
                double randomReturn = weightedReturn + weightedVolatility * random.nextGaussian();
                value = value * (1 + randomReturn);
            }
            finalValues.add(value);
        }

        // Step 3: Sort final values so we can pull percentiles.
        Collections.sort(finalValues);

        double worstCase      = finalValues.get((int)(NUM_SIMULATIONS * 0.05)); // 5th percentile
        double estimatedValue = finalValues.get(NUM_SIMULATIONS / 2);           // median (50th)
        double bestCase       = finalValues.get((int)(NUM_SIMULATIONS * 0.95)); // 95th percentile

        // Step 4: Return results wrapped in a PortfolioAnalyzer object.
        // analysis_ID = 0 - the database will assign one via auto_increment when saved.
        return new PortfolioAnalyzer(
            0,
            portfolio.getPortfolio_ID(),
            bestCase,
            worstCase,
            estimatedValue,
            weightedReturn,
            years
        );
    }
    /**
     * Like runSimulation, but also returns the year-by-year paths
     * for the worst-case, median, and best-case scenarios.
     * Used by the Monte Carlo GUI panel to draw the 3 trajectory lines.
     *
     * @return a double[3][years+1] array where:
     *           result[0] = worst-case path
     *           result[1] = estimated/median path
     *           result[2] = best-case path
     *         Each path starts at startingValue at index 0.
     */
    public double[][] runSimulationWithPaths(Portfolio portfolio, List<Asset> assets, int years) {

        // Step 1: weighted return + volatility (same as runSimulation)
        double weightedReturn = 0.0;
        double weightedVolatility = 0.0;
        for (Asset asset : assets) {
            double weight = asset.getAllocationPercentage() / 100.0;
            weightedReturn     += weight * getExpectedReturn(asset.getAssetType());
            weightedVolatility += weight * getVolatility(asset.getAssetType());
        }

        // Step 2: run NUM_SIMULATIONS, but keep the FULL path of each one
        double startingValue = portfolio.getTotalValue();
        Random random = new Random();
        double[][] allPaths = new double[NUM_SIMULATIONS][years + 1];

        for (int sim = 0; sim < NUM_SIMULATIONS; sim++) {
            allPaths[sim][0] = startingValue;
            double value = startingValue;
            for (int y = 1; y <= years; y++) {
                double randomReturn = weightedReturn + weightedVolatility * random.nextGaussian();
                value = value * (1 + randomReturn);
                allPaths[sim][y] = value;
            }
        }

        // Step 3: for each YEAR, pick the worst/median/best across all sims
        double[][] result = new double[3][years + 1];
        for (int y = 0; y <= years; y++) {
            double[] yearValues = new double[NUM_SIMULATIONS];
            for (int sim = 0; sim < NUM_SIMULATIONS; sim++) {
                yearValues[sim] = allPaths[sim][y];
            }
            java.util.Arrays.sort(yearValues);
            result[0][y] = yearValues[(int)(NUM_SIMULATIONS * 0.05)];   // worst
            result[1][y] = yearValues[NUM_SIMULATIONS / 2];              // median
            result[2][y] = yearValues[(int)(NUM_SIMULATIONS * 0.95)];   // best
        }

        return result;
    }
}