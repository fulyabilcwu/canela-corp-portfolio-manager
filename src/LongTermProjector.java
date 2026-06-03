import java.util.List;
import java.util.ArrayList;

/**
 * Computes deterministic compound-growth projections for a portfolio.
 * Unlike Monte Carlo, this has no randomness - it shows the expected
 * value year by year if the average return holds steady.
 *
 * Used to plot the Long-Term Potential growth curve in the GUI.
 *
 * Phase 3: expected returns are now pulled from REAL market data via
 * MarketDataService (Alpha Vantage), which fetches historical prices for a
 * representative fund per asset class and computes the annualized return.
 * No more hardcoded constants.
 */
public class LongTermProjector {

    // Asset-type expected returns now come from real market data
    // (historical prices via the Alpha Vantage API), not hardcoded values.
    private static double getExpectedReturn(String assetType) {
        return MarketDataService.getExpectedReturn(assetType);
    }

    /**
     * Calculates the weighted average expected annual return for the
     * portfolio based on its asset allocation.
     */
    public double calculateWeightedReturn(List<Asset> assets) {
        double weightedReturn = 0.0;
        for (Asset asset : assets) {
            double weight = asset.getAllocationPercentage() / 100.0;
            weightedReturn += weight * getExpectedReturn(asset.getAssetType());
        }
        return weightedReturn;
    }

    /**
     * Projects portfolio value for each year from 1 up to maxYears.
     * Returns a list where index i = projected value after year (i+1).
     * Feed this to the GUI to draw the long-term growth chart.
     */
    public List<Double> projectGrowth(Portfolio portfolio, List<Asset> assets, int maxYears) {
        double rate = calculateWeightedReturn(assets);
        double startingValue = portfolio.getTotalValue();
        List<Double> projection = new ArrayList<>();

        for (int year = 1; year <= maxYears; year++) {
            // Compound growth formula: FV = PV * (1 + r)^t
            double value = startingValue * Math.pow(1 + rate, year);
            projection.add(value);
        }
        return projection;
    }

    /**
     * Convenience method: just gives the final value after N years
     * without producing the full year-by-year list.
     */
    public double projectFinalValue(Portfolio portfolio, List<Asset> assets, int years) {
        double rate = calculateWeightedReturn(assets);
        return portfolio.getTotalValue() * Math.pow(1 + rate, years);
    }
}
