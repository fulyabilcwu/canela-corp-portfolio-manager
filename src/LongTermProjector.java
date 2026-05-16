import java.util.List;
import java.util.ArrayList;

/**
 * Computes deterministic compound-growth projections for a portfolio.
 * Unlike Monte Carlo, this has no randomness - it shows the expected
 * value year by year if the average return holds steady.
 *
 * Used to plot the Long-Term Potential growth curve in the GUI.
 *
 * TODO (Phase 3): Replace hardcoded constants with a MARKET_ASSUMPTIONS
 * database table lookup via DatabaseManager.
 */
public class LongTermProjector {

    // Asset-type expected returns - mirror MonteCarloSimulator for consistency.
    private static double getExpectedReturn(String assetType) {
        switch (assetType.toUpperCase()) {
            case "STOCK":       return 0.10;
            case "BOND":        return 0.04;
            case "CASH":        return 0.02;
            case "REAL_ESTATE": return 0.07;
            case "GOLD":        return 0.05;
            case "ETF":         return 0.09;
            default:            return 0.05;
        }
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