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
 * TODO (Phase 3): Replace hardcoded constants with a MARKET_ASSUMPTIONS
 * database table lookup via DatabaseManager. Table schema:
 *   asset_type (varchar PK), expected_return (decimal), volatility (decimal)
 */
public class MonteCarloSimulator {

    // Number of simulated future scenarios. 10,000 gives stable results.
    private static final int NUM_SIMULATIONS = 10000;

    // Annual expected return for each asset type (decimal form).
    // Based on approximate long-run historical averages.
    private static double getExpectedReturn(String assetType) {
        switch (assetType.toUpperCase()) {
            case "STOCK":       return 0.10;   // 10% - equities long-run average
            case "BOND":        return 0.04;   // 4%  - investment-grade bonds
            case "CASH":        return 0.02;   // 2%  - money market / savings
            case "REAL_ESTATE": return 0.07;   // 7%  - REITs long-run average
            case "GOLD":        return 0.05;   // 5%  - gold long-run average
            case "ETF":         return 0.09;   // 9%  - mixed equity ETFs
            default:            return 0.05;   // fallback for unknown types
        }
    }

    // Annual volatility (standard deviation of returns) for each asset type.
    private static double getVolatility(String assetType) {
        switch (assetType.toUpperCase()) {
            case "STOCK":       return 0.18;   // High volatility
            case "BOND":        return 0.06;   // Low volatility
            case "CASH":        return 0.01;   // Very stable
            case "REAL_ESTATE": return 0.15;   // Moderate-high
            case "GOLD":        return 0.18;   // High - commodity swings
            case "ETF":         return 0.16;   // Slightly less than single stocks
            default:            return 0.15;
        }
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
}