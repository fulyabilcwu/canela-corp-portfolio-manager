import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fetches REAL market data from the Alpha Vantage API and computes both the
 * historical annualized return (CAGR) AND the annualized volatility (standard
 * deviation of monthly returns) for each asset class. This replaces the old
 * hardcoded return/volatility constants in LongTermProjector and
 * MonteCarloSimulator.
 *
 * Each asset type maps to a representative real-world fund (proxy ticker):
 *   STOCK       -> SPY  (S&P 500)
 *   BOND        -> AGG  (US aggregate bonds)
 *   REAL_ESTATE -> VNQ  (US real-estate REITs)
 *   GOLD        -> GLD  (gold)
 *   ETF         -> VTI  (total US market)
 *   CASH        -> fixed (cash has no traded price): return 2%, volatility 1%
 *
 * One API call returns the monthly adjusted closing prices (~100 months on the
 * free tier). From that single price series we compute:
 *   - return:     CAGR = (latest/oldest)^(1/years) - 1
 *   - volatility: stddev of monthly % returns, annualized by * sqrt(12)
 * Both are cached per asset type so repeated use doesn't burn the free-tier
 * daily request limit (25/day, 5/minute).
 */
public class MarketDataService {

    // ====== PASTE YOUR FREE ALPHA VANTAGE KEY BETWEEN THE QUOTES ======
    // Get one instantly at: https://www.alphavantage.co/support/#api-key
    private static final String API_KEY = "PMIKV4A67TYRUCLH";
    // ==================================================================

    private static final String BASE_URL =
        "https://www.alphavantage.co/query"
        + "?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=%s&apikey=%s";

    // Asset type -> proxy ticker
    private static final Map<String, String> TICKERS = new HashMap<>();
    static {
        TICKERS.put("STOCK",       "SPY");
        TICKERS.put("BOND",        "AGG");
        TICKERS.put("REAL_ESTATE", "VNQ");
        TICKERS.put("GOLD",        "GLD");
        TICKERS.put("ETF",         "VTI");
    }

    // Small holder for the two numbers we derive from one price series.
    private static class Stats {
        final double annualReturn;
        final double annualVolatility;
        Stats(double r, double v) { this.annualReturn = r; this.annualVolatility = v; }
    }

    // Cache so we don't re-hit the API for the same asset type repeatedly.
    private static final Map<String, Stats> cache = new HashMap<>();

    // Pulls "adjusted close" numbers out of the JSON without a JSON library.
    private static final Pattern ADJ_CLOSE =
        Pattern.compile("\"5\\. adjusted close\"\\s*:\\s*\"([0-9.]+)\"");

    private static final HttpClient HTTP = HttpClient.newHttpClient();

    /**
     * Expected annual return (decimal, e.g. 0.10) for an asset type,
     * computed from real historical prices. Used by LongTermProjector.
     */
    public static double getExpectedReturn(String assetType) {
        return getStats(assetType).annualReturn;
    }

    /**
     * Annualized volatility (decimal, e.g. 0.18 = 18%) for an asset type,
     * computed from real historical monthly returns. Used by
     * MonteCarloSimulator.
     */
    public static double getVolatility(String assetType) {
        return getStats(assetType).annualVolatility;
    }

    /**
     * Fetches (or returns cached) return + volatility for an asset type.
     * Makes at most ONE API call per asset type for the life of the program.
     */
    private static Stats getStats(String assetType) {
        if (assetType == null) {
            return new Stats(0.05, 0.15);
        }
        String type = assetType.toUpperCase();

        // Cash isn't a traded security: flat rate, near-zero volatility.
        if (type.equals("CASH")) {
            return new Stats(0.02, 0.01);
        }

        if (cache.containsKey(type)) {
            return cache.get(type);
        }

        String ticker = TICKERS.get(type);
        if (ticker == null) {
            return new Stats(0.05, 0.15);   // unknown asset type
        }

        try {
            String url = String.format(BASE_URL, ticker, API_KEY);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> resp =
                HTTP.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();

            List<Double> prices = extractPrices(body);

            if (prices.size() < 13) {
                // API limit hit, bad symbol, or no data -> fall back.
                // Cache the fallback too so we don't keep retrying a throttled
                // ticker and burning through the daily quota.
                System.err.println("Market data unavailable for " + ticker
                    + " (using fallback). Response start: "
                    + body.substring(0, Math.min(120, body.length())));
                Stats fb = fallback(type);
                cache.put(type, fb);
                return fb;
            }

            double annualReturn     = computeCagr(prices);
            double annualVolatility = computeAnnualVolatility(prices);

            Stats stats = new Stats(annualReturn, annualVolatility);
            cache.put(type, stats);

            System.out.println("[LIVE API] " + type + " (" + ticker
                + ") -> return = " + String.format("%.2f%%", annualReturn * 100)
                + ", volatility = " + String.format("%.2f%%", annualVolatility * 100)
                + "  [from " + ticker + " historical prices]");

            return stats;

        } catch (Exception e) {
            System.err.println("Error fetching market data for " + ticker
                + ": " + e.getMessage());
            Stats fb = fallback(type);
            cache.put(type, fb);
            return fb;
        }
    }

    /** Parse adjusted-close prices. JSON lists most-recent month first. */
    private static List<Double> extractPrices(String json) {
        List<Double> prices = new ArrayList<>();
        Matcher m = ADJ_CLOSE.matcher(json);
        while (m.find()) {
            prices.add(Double.parseDouble(m.group(1)));
        }
        return prices;
    }

    /**
     * Annualized return (CAGR) from the price series.
     * prices.get(0) = most recent, prices.get(size-1) = oldest.
     */
    private static double computeCagr(List<Double> prices) {
        double latest = prices.get(0);
        double oldest = prices.get(prices.size() - 1);
        double years  = (prices.size() - 1) / 12.0;
        return Math.pow(latest / oldest, 1.0 / years) - 1.0;
    }

    /**
     * Annualized volatility: standard deviation of the monthly percentage
     * returns, scaled to a yearly figure by multiplying by sqrt(12).
     */
    private static double computeAnnualVolatility(List<Double> prices) {
        // Build the list of month-over-month returns.
        List<Double> monthlyReturns = new ArrayList<>();
        // Walk from oldest to newest so each return is (newer/older - 1).
        for (int i = prices.size() - 1; i > 0; i--) {
            double older = prices.get(i);
            double newer = prices.get(i - 1);
            if (older != 0) {
                monthlyReturns.add((newer - older) / older);
            }
        }

        if (monthlyReturns.size() < 2) {
            return 0.15;   // not enough data; safe default
        }

        // Mean of monthly returns.
        double sum = 0.0;
        for (double r : monthlyReturns) {
            sum += r;
        }
        double mean = sum / monthlyReturns.size();

        // Variance (sample), then standard deviation.
        double sqDiff = 0.0;
        for (double r : monthlyReturns) {
            sqDiff += (r - mean) * (r - mean);
        }
        double monthlyVariance = sqDiff / (monthlyReturns.size() - 1);
        double monthlyStdDev = Math.sqrt(monthlyVariance);

        // Annualize: multiply monthly stddev by sqrt(12).
        return monthlyStdDev * Math.sqrt(12.0);
    }

    /** Reasonable long-run averages if the live call fails. */
    private static Stats fallback(String type) {
        switch (type) {
            case "STOCK":       return new Stats(0.10, 0.18);
            case "BOND":        return new Stats(0.04, 0.06);
            case "REAL_ESTATE": return new Stats(0.07, 0.15);
            case "GOLD":        return new Stats(0.05, 0.18);
            case "ETF":         return new Stats(0.09, 0.16);
            default:            return new Stats(0.05, 0.15);
        }
    }

    /** Quick manual test: prints live return + volatility for each asset type. */
    public static void main(String[] args) {
        String[] types = {"STOCK", "BOND", "REAL_ESTATE", "GOLD", "ETF", "CASH"};
        for (String t : types) {
            Stats s = getStats(t);
            System.out.printf("%-12s return = %6.2f%%   volatility = %6.2f%%%n",
                t, s.annualReturn * 100, s.annualVolatility * 100);
            // Free tier = 5 calls/minute. Space out the live calls.
            if (!t.equals("CASH")) {
                try { Thread.sleep(15000); } catch (InterruptedException ignored) {}
            }
        }
    }
}
