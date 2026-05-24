import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * GUI panel for the Monte Carlo Simulation screen (Figure 15).
 *
 * Shows:
 *  - dark coral header with title and "Back to Portfolio" link
 *  - "Years to simulate" input + Run Simulation button
 *  - Summary Box with Estimated Value, Worst Case, Best Case
 *  - Line chart of the 3 trajectories (worst/estimated/best) year by year
 */
public class MonteCarloPanel extends JPanel {

    private static final Color HEADER_COLOR = new Color(204, 88, 80);
    private static final Color BODY_COLOR   = new Color(245, 210, 205);
    private static final Color TEXT_COLOR   = new Color(40, 30, 30);

    private JLabel estimatedLabel;
    private JLabel bestCaseLabel;
    private JLabel worstCaseLabel;
    private JTextField yearsField;
    private JPanel chartContainer;   // holds the chart, replaced on each run

    public MonteCarloPanel() {
        setLayout(new BorderLayout());
        setBackground(BODY_COLOR);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("MONTE CARLO SIMULATION");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(TEXT_COLOR);
        header.add(title, BorderLayout.WEST);

        JLabel backLink = new JLabel("← BACK TO PORTFOLIO");
        backLink.setFont(new Font("Arial", Font.BOLD, 12));
        backLink.setForeground(TEXT_COLOR);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(backLink, BorderLayout.EAST);

        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BODY_COLOR);
        body.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Input row ---
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputRow.setBackground(BODY_COLOR);
        JLabel yearsLabel = new JLabel("Years to simulate:");
        yearsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        yearsLabel.setForeground(TEXT_COLOR);
        inputRow.add(yearsLabel);
        yearsField = new JTextField("10", 5);
        inputRow.add(yearsField);

        JButton runButton = new JButton("Run Simulation");
        runButton.setFont(new Font("Arial", Font.BOLD, 12));
        runButton.setBackground(HEADER_COLOR);
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);
        inputRow.add(runButton);

        inputRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(inputRow);
        body.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Summary Box ---
        JPanel summaryBox = new JPanel();
        summaryBox.setLayout(new BoxLayout(summaryBox, BoxLayout.Y_AXIS));
        summaryBox.setBackground(BODY_COLOR);
        summaryBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        summaryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel summaryTitle = new JLabel("SUMMARY BOX");
        summaryTitle.setFont(new Font("Arial", Font.BOLD, 14));
        summaryTitle.setForeground(TEXT_COLOR);
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryBox.add(summaryTitle);
        summaryBox.add(Box.createRigidArea(new Dimension(0, 10)));

        estimatedLabel = makeResultLabel("ESTIMATED VALUE:  --");
        worstCaseLabel = makeResultLabel("WORST CASE:  --");
        bestCaseLabel  = makeResultLabel("BEST CASE:  --");
        summaryBox.add(estimatedLabel);
        summaryBox.add(worstCaseLabel);
        summaryBox.add(bestCaseLabel);

        summaryBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(summaryBox);
        body.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Chart container (gets filled in when user clicks Run) ---
        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(BODY_COLOR);
        chartContainer.setPreferredSize(new Dimension(600, 280));
        chartContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel placeholder = new JLabel("Chart will appear after running the simulation");
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setForeground(TEXT_COLOR);
        chartContainer.add(placeholder, BorderLayout.CENTER);
        body.add(chartContainer);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSimulation();
            }
        });

        return body;
    }

    private JLabel makeResultLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(TEXT_COLOR);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void runSimulation() {
        try {
            int years = Integer.parseInt(yearsField.getText());

            // Fake test data (TODO Phase 3: load real data from DatabaseManager)
            Portfolio fakePortfolio = new Portfolio(1, 1, "Test Portfolio", 10000.0, "MEDIUM");
            List<Asset> fakeAssets = new ArrayList<>();
            fakeAssets.add(new Asset(1, 1, "S&P 500 ETF",     "STOCK", 60.0, 6000.0));
            fakeAssets.add(new Asset(2, 1, "Treasury Bonds",  "BOND",  30.0, 3000.0));
            fakeAssets.add(new Asset(3, 1, "Savings Account", "CASH",  10.0, 1000.0));

            MonteCarloSimulator simulator = new MonteCarloSimulator();

            // Update Summary Box
            PortfolioAnalyzer result = simulator.runSimulation(fakePortfolio, fakeAssets, years);
            estimatedLabel.setText(String.format("ESTIMATED VALUE:  $%,.2f", result.getEstimatedValue()));
            worstCaseLabel.setText(String.format("WORST CASE:  $%,.2f", result.getWorstCase()));
            bestCaseLabel.setText(String.format("BEST CASE:  $%,.2f", result.getBestCase()));

            // Draw chart
            double[][] paths = simulator.runSimulationWithPaths(fakePortfolio, fakeAssets, years);
            updateChart(paths, years);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid number of years.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChart(double[][] paths, int years) {
        XYSeries worst = new XYSeries("Worst Case (5%)");
        XYSeries median = new XYSeries("Estimated (50%)");
        XYSeries best = new XYSeries("Best Case (95%)");

        for (int y = 0; y <= years; y++) {
            worst.add(y, paths[0][y]);
            median.add(y, paths[1][y]);
            best.add(y, paths[2][y]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(best);
        dataset.addSeries(median);
        dataset.addSeries(worst);

        JFreeChart chart = ChartFactory.createXYLineChart(
            "Projected Portfolio Value Over Time",
            "Year", "Value ($)",
            dataset, PlotOrientation.VERTICAL,
            true, true, false
        );
        chart.setBackgroundPaint(BODY_COLOR);

        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(new Dimension(600, 280));

        chartContainer.removeAll();
        chartContainer.add(cp, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Monte Carlo - Canela Portfolio Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.add(new MonteCarloPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}