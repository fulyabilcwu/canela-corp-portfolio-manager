import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI panel for the Long-Term Potential screen.
 *
 * Shows a stylized pyramid chart with 3 tiers (10, 25, 50 years).
 * Each tier displays the projected portfolio value for that timeframe.
 * The pyramid widens at the bottom to visually emphasize long-term growth.
 *
 * Phase 3: portfolios are loaded live from the portfolioapp MySQL database
 * via DatabaseManager. Selecting a portfolio re-renders the pyramid.
 *
 * The "Back to Portfolio" link triggers the Runnable registered via
 * setOnBack(), letting the host app decide what navigation should happen.
 */
public class LongTermPotentialPanel extends JPanel {

    private static final Color HEADER_COLOR = new Color(204, 88, 80);
    private static final Color BODY_COLOR   = new Color(245, 210, 205);
    private static final Color TEXT_COLOR   = new Color(40, 30, 30);
    private static final Color TIER_COLOR   = new Color(180, 70, 65);

    // Phase 3: live portfolio + assets from MySQL
    private JComboBox<Portfolio> portfolioCombo;
    private Portfolio currentPortfolio;
    private List<Asset> currentAssets = new ArrayList<>();
    private int currentUser;

    private JLabel subtitle;
    private PyramidComponent pyramid;

    // Navigation callback — set by the host app via setOnBack()
    private Runnable onBack;

    public LongTermPotentialPanel(int currentUser) {
        this.currentUser = currentUser;

        setLayout(new BorderLayout());
        setBackground(BODY_COLOR);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        loadPortfoliosFromDatabase();
    }

    /**
     * Register what should happen when the user clicks "Back to Portfolio".
     */
    public void setOnBack(Runnable r) {
        this.onBack = r;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("LONG TERM POTENTIAL");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(TEXT_COLOR);
        header.add(title, BorderLayout.WEST);

        JLabel backLink = new JLabel("\u2190 BACK TO PORTFOLIO");
        backLink.setFont(new Font("Arial", Font.BOLD, 12));
        backLink.setForeground(TEXT_COLOR);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onBack != null) {
                    onBack.run();
                }
            }
        });
        header.add(backLink, BorderLayout.EAST);

        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BODY_COLOR);
        body.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BODY_COLOR);

        JPanel portfolioRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        portfolioRow.setBackground(BODY_COLOR);
        JLabel portfolioLabel = new JLabel("Portfolio:");
        portfolioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        portfolioLabel.setForeground(TEXT_COLOR);
        portfolioRow.add(portfolioLabel);

        portfolioCombo = new JComboBox<>();
        portfolioCombo.setPreferredSize(new Dimension(300, 28));
        portfolioCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        portfolioCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                l.setFont(new Font("Arial", Font.PLAIN, 13));
                if (value instanceof Portfolio) {
                    Portfolio p = (Portfolio) value;
                    l.setText(p.getPortfolioName()
                        + "  ($" + String.format("%,.2f", p.getTotalValue())
                        + ", " + p.getRiskLevel() + ")");
                }
                return l;
            }
        });
        portfolioCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPortfolioSelected();
            }
        });
        portfolioRow.add(portfolioCombo);
        portfolioRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(portfolioRow);
        topSection.add(Box.createRigidArea(new Dimension(0, 10)));

        subtitle = new JLabel("BASED OFF: --", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 13));
        subtitle.setForeground(TEXT_COLOR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(subtitle);

        body.add(topSection, BorderLayout.NORTH);

        pyramid = new PyramidComponent();
        body.add(pyramid, BorderLayout.CENTER);

        return body;
    }

    private void loadPortfoliosFromDatabase() {
        portfolioCombo.removeAllItems();

        List<Portfolio> portfolios =
                DatabaseManager.getPortfoliosByUserId(currentUser);

        if (portfolios.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No portfolios found for this user.",
                    "No Portfolios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Portfolio p : portfolios) {
            portfolioCombo.addItem(p);
        }

        portfolioCombo.setSelectedIndex(0);
        onPortfolioSelected();
    }

    private void onPortfolioSelected() {
        Portfolio selected = (Portfolio) portfolioCombo.getSelectedItem();
        if (selected == null) {
            return;
        }
        currentPortfolio = selected;
        currentAssets = DatabaseManager.getAssetsByPortfolioId(
            selected.getPortfolio_ID());

        subtitle.setText("BASED OFF: "
            + selected.getPortfolioName().toUpperCase());
        if (pyramid != null) {
            pyramid.repaint();
        }
    }

    private class PyramidComponent extends JPanel {

        public PyramidComponent() {
            setBackground(BODY_COLOR);
            setPreferredSize(new Dimension(800, 500));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (currentPortfolio == null
                    || currentAssets == null
                    || currentAssets.isEmpty()) {
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                String msg = "Select a portfolio to see projections";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
                return;
            }

            LongTermProjector projector = new LongTermProjector();
            double v10 = projector.projectFinalValue(currentPortfolio, currentAssets, 10);
            double v25 = projector.projectFinalValue(currentPortfolio, currentAssets, 25);
            double v50 = projector.projectFinalValue(currentPortfolio, currentAssets, 50);
            double start = currentPortfolio.getTotalValue();

            int centerX  = w / 2;
            int topY     = 40;
            int bottomY  = h - 40;
            int tierH    = (bottomY - topY) / 3;
            int maxWidth = (int)(w * 0.55);
            int minWidth = (int)(w * 0.15);

            drawTier(g2, centerX, topY,             tierH, minWidth,
                     (minWidth + maxWidth) / 3,
                     "10 YEARS",  v10, start);

            drawTier(g2, centerX, topY + tierH,     tierH,
                     (minWidth + maxWidth) / 3,
                     (2 * (minWidth + maxWidth)) / 3,
                     "25 YEARS",  v25, start);

            drawTier(g2, centerX, topY + 2*tierH, tierH,
                     (2 * (minWidth + maxWidth)) / 3,
                     maxWidth,
                     "50 YEARS",  v50, start);
        }

        private void drawTier(Graphics2D g2, int centerX, int y, int height,
                              int topW, int botW,
                              String label, double value, double start) {
            int[] xs = {
                centerX - topW/2, centerX + topW/2,
                centerX + botW/2, centerX - botW/2
            };
            int[] ys = { y, y, y + height, y + height };

            g2.setColor(TIER_COLOR);
            g2.fillPolygon(xs, ys, 4);
            g2.setColor(TEXT_COLOR);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(xs, ys, 4);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(label);
            g2.drawString(label, centerX - textW/2, y + height/2 + fm.getAscent()/2 - 2);

            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            String valueStr = String.format("$%,.0f", value);
            int valueX = centerX + botW/2 + 20;
            int valueY = y + height/2 + 5;
            g2.drawString(valueStr, valueX, valueY);

            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            double growthPct = ((value - start) / start) * 100;
            String growthStr = String.format("(+%.0f%% from $%,.0f)", growthPct, start);
            g2.drawString(growthStr, valueX, valueY + 18);
        }
    }

    // public static void main(String[] args) {
    //     final JFrame frame = new JFrame("Long-Term Potential - Canela Portfolio Manager");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setSize(900, 600);
    //     LongTermPotentialPanel panel = new LongTermPotentialPanel();
    //     // Standalone test: clicking "Back to Portfolio" closes the window.
    //     // In the integrated app the host wires this to real navigation.
    //     panel.setOnBack(new Runnable() {
    //         @Override
    //         public void run() {
    //             frame.dispose();
    //         }
    //     });
    //     frame.add(panel);
    //     frame.setLocationRelativeTo(null);
    //     frame.setVisible(true);
    // }
}
