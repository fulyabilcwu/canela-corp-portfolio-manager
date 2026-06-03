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
 * Fetches REAL market data from the Alpha Vantage API and computes the
 * historical annualized return (CAGR) for each asset class. This replaces
 * the old hardcoded return constants in LongTermProjector.
 *
 * Each asset type maps to a representative real-world fund (proxy ticker):
 *   STOCK       -> SPY  (S&P 500)
 *   BOND        -> AGG  (US aggregate bonds)
 *   REAL_ESTATE -> VNQ  (US real-estate REITs)
 *   GOLD        -> GLD  (gold)
 *   ETF         -> VTI  (total US market)
 *   CASH        -> fixed 2% (cash has no traded price)
 *
 * The class calls TIME_SERIES_MONTHLY_ADJUSTED, takes the monthly adjusted
 * closing prices the free tier returns (~100 months), and computes:
 *     CAGR = (latestPrice / oldestPrice)^(1 / years) - 1
 *
 * Results are cached per asset type for the life of the program so that
 * selecting portfolios repeatedly does not burn through the free-tier
 * daily request limit (25/day).
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

    // Cache so we don't re-hit the API for the same asset type repeatedly.
    private static final Map<String, Double> cache = new HashMap<>();

    // Pulls "adjusted close" numbers out of the JSON without a JSON library.
    private static final Pattern ADJ_CLOSE =
        Pattern.compile("\"5\\. adjusted close\"\\s*:\\s*\"([0-9.]+)\"");

    private static final HttpClient HTTP = HttpClient.newHttpClient();

    /**
     * Returns the expected annual return (as a decimal, e.g. 0.10) for the
     * given asset type, computed from real historical market prices.
     * Falls back to a sensible default if the API can't be reached.
     */
    public static double getExpectedReturn(String assetType) {
        if (assetType == null) {
            return 0.05;
        }
        String type = assetType.toUpperCase();

        // Cash isn't a traded security; use a flat money-market style rate.
        if (type.equals("CASH")) {
            return 0.02;
        }

        // Return cached value if we already fetched this asset type.
        if (cache.containsKey(type)) {
            return cache.get(type);
        }

        String ticker = TICKERS.get(type);
        if (ticker == null) {
            return 0.05;   // unknown asset type
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

            double cagr = computeCagrFromJson(body);

            if (cagr <= -1.0 || Double.isNaN(cagr)) {
                // API limit hit, bad symbol, or no data -> fall back.
                System.err.println("Market data unavailable for " + ticker
                    + " (using fallback). Response start: "
                    + body.substring(0, Math.min(120, body.length())));
                return fallback(type);
            }

            cache.put(type, cagr);
            System.out.println("[LIVE API] " + type + " (" + ticker
                + ") -> computed annual return = "
                + String.format("%.2f%%", cagr * 100)
                + "  [from " + ticker + " historical prices]");
            return cagr;

        } catch (Exception e) {
            System.err.println("Error fetching market data for " + ticker
                + ": " + e.getMessage());
            return fallback(type);
        }
    }

    /**
     * Computes the annualized return (CAGR) from the monthly adjusted-close
     * prices in an Alpha Vantage TIME_SERIES_MONTHLY_ADJUSTED response.
     * The JSON lists most-recent month first, so the first match is the
     * latest price and the last match is the oldest.
     */
    private static double computeCagrFromJson(String json) {
        List<Double> prices = new ArrayList<>();
        Matcher m = ADJ_CLOSE.matcher(json);
        while (m.find()) {
            prices.add(Double.parseDouble(m.group(1)));
        }

        if (prices.size() < 13) {
            // need at least ~1 year of data to annualize
            return Double.NaN;
        }

        double latest = prices.get(0);                 // most recent month
        double oldest = prices.get(prices.size() - 1); // earliest month
        double months = prices.size() - 1;
        double years  = months / 12.0;

        // CAGR = (latest/oldest)^(1/years) - 1
        return Math.pow(latest / oldest, 1.0 / years) - 1.0;
    }

    /** Reasonable long-run averages if the live call fails. */
    private static double fallback(String type) {
        switch (type) {
            case "STOCK":       return 0.10;
            case "BOND":        return 0.04;
            case "REAL_ESTATE": return 0.07;
            case "GOLD":        return 0.05;
            case "ETF":         return 0.09;
            default:            return 0.05;
        }
    }

    /** Quick manual test: prints the live computed return for each asset type. */
    public static void main(String[] args) {
        String[] types = {"STOCK", "BOND", "REAL_ESTATE", "GOLD", "ETF", "CASH"};
        for (String t : types) {
            System.out.printf("%-12s expected annual return = %.2f%%%n",
                t, getExpectedReturn(t) * 100);
            // Free tier allows 5 calls/minute. Wait between live calls so we
            // don't get throttled. (CASH makes no call, so skip the wait there.)
            if (!t.equals("CASH")) {
                try { Thread.sleep(15000); } catch (InterruptedException ignored) {}
            }
        }
    }
}
