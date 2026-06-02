import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class portfolioDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblPortfolioName;
    private JLabel lblAge;
    private JLabel lblIncome;
    private JLabel lblRisk;
    private JLabel lblNetWorth;
    private JComboBox<String> assetComboBox;
    private JTextField pctField;
    private int currentPortfolioID;
    private JTextArea assetArea;
    private ArrayList<Asset> assets;
    private PiechartPanel piechart;
    private static final Color HEADER_COLOR = new Color(204, 88, 80);
    private static final Color BODY_COLOR = new Color(245, 210, 205);
    private static final Color TEXT_COLOR = new Color(40, 30, 30);
    private static final Color BUTTON_COLOR = new Color(60, 60, 60);
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                portfolioDashboard frame = new portfolioDashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public portfolioDashboard() {
        assets = new ArrayList<>();
        setTitle("Portfolio Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(BODY_COLOR);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        JPanel dashboardContainer = new JPanel(new GridBagLayout());
        dashboardContainer.setBackground(BODY_COLOR);
        dashboardContainer.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1;
        mainGbc.weighty = 1;
        mainGbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(dashboardContainer, mainGbc);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setPreferredSize(new Dimension(900, 60));
        JLabel backLink = new JLabel("← BACK TO PORTFOLIO");
        backLink.setFont(new Font("Arial", Font.BOLD, 12));
        backLink.setForeground(TEXT_COLOR);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setBorder(new EmptyBorder(0, 15, 0, 0));
        backLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GUI.onBackToBuilder != null) {
                    GUI.onBackToBuilder.run();
                } else {
                    dispose();
                }
            }
        });
        lblPortfolioName = new JLabel("Portfolio Dashboard", SwingConstants.CENTER);
        lblPortfolioName.setForeground(TEXT_COLOR);
        lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 20));
        JButton btnAdmin = new JButton("Admin");
        btnAdmin.setBackground(Color.WHITE);
        btnAdmin.setForeground(TEXT_COLOR);
        btnAdmin.setFocusable(false);
        btnAdmin.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "Enter admin password:");
            if (input != null && input.equals("CANELA")) {
                if (GUI.onShowAdmin != null) {
                    GUI.onShowAdmin.run();
                } else {
                    JOptionPane.showMessageDialog(null, "Admin panel is not connected.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect password.");
            }
        });
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightHeader.setOpaque(false);
        rightHeader.add(btnAdmin);
        headerPanel.add(backLink, BorderLayout.WEST);
        headerPanel.add(lblPortfolioName, BorderLayout.CENTER);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dashboardContainer.add(headerPanel, gbc);
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BODY_COLOR);
        infoPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(8, 8, 8, 8);
        infoGbc.fill = GridBagConstraints.HORIZONTAL;
        infoGbc.gridx = 0;
        lblAge = createInfoLabel("Age: ");
        lblIncome = createInfoLabel("Income: ");
        lblRisk = createInfoLabel("Risk Tolerance: ");
        lblNetWorth = createInfoLabel("Net Worth: ");
        infoGbc.gridy = 0;
        infoPanel.add(lblAge, infoGbc);
        infoGbc.gridy = 1;
        infoPanel.add(lblIncome, infoGbc);
        infoGbc.gridy = 2;
        infoPanel.add(lblRisk, infoGbc);
        infoGbc.gridy = 3;
        infoPanel.add(lblNetWorth, infoGbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        dashboardContainer.add(infoPanel, gbc);
        piechart = new PiechartPanel(assets);
        piechart.setPreferredSize(new Dimension(350, 350));
        piechart.setBackground(Color.WHITE);
        piechart.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        dashboardContainer.add(piechart, gbc);
        JPanel assetInputPanel = new JPanel(new GridBagLayout());
        assetInputPanel.setBackground(BODY_COLOR);
        assetInputPanel.setBorder(BorderFactory.createTitledBorder("Add Assets"));
        GridBagConstraints assetGbc = new GridBagConstraints();
        assetGbc.insets = new Insets(8, 8, 8, 8);
        assetGbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblAddAssets = new JLabel("Asset Type:");
        lblAddAssets.setForeground(TEXT_COLOR);
        lblAddAssets.setFont(new Font("Tahoma", Font.BOLD, 14));
        assetComboBox = new JComboBox<>();
        assetComboBox.addItem("Stocks");
        assetComboBox.addItem("Bonds");
        assetComboBox.addItem("Cash");
        assetComboBox.addItem("ETF");
        assetComboBox.addItem("Gold");
        JLabel lblPercent = new JLabel("Allocation %:");
        lblPercent.setForeground(TEXT_COLOR);
        lblPercent.setFont(new Font("Tahoma", Font.BOLD, 14));
        pctField = new JTextField(8);
        JButton btnAddAsset = createDarkButton("Add");
        btnAddAsset.addActionListener(e -> addAsset());
        JButton btnSaveAssets = createDarkButton("Save Assets");
        btnSaveAssets.addActionListener(e -> saveAssets());
        assetGbc.gridx = 0;
        assetGbc.gridy = 0;
        assetInputPanel.add(lblAddAssets, assetGbc);
        assetGbc.gridx = 1;
        assetInputPanel.add(assetComboBox, assetGbc);
        assetGbc.gridx = 0;
        assetGbc.gridy = 1;
        assetInputPanel.add(lblPercent, assetGbc);
        assetGbc.gridx = 1;
        assetInputPanel.add(pctField, assetGbc);
        assetGbc.gridx = 0;
        assetGbc.gridy = 2;
        assetInputPanel.add(btnAddAsset, assetGbc);
        assetGbc.gridx = 1;
        assetInputPanel.add(btnSaveAssets, assetGbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        dashboardContainer.add(assetInputPanel, gbc);
        assetArea = new JTextArea(10, 30);
        assetArea.setEditable(false);
        assetArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(assetArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Current Assets"));
        gbc.gridx = 1;
        gbc.gridy = 2;
        dashboardContainer.add(scrollPane, gbc);
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        navigationPanel.setOpaque(false);
        JButton btnLongTerm = createDarkButton("Long Term Potential");
        btnLongTerm.addActionListener(e -> {
            if (GUI.onShowLongTerm != null) {
                GUI.onShowLongTerm.run();
            } else {
                JFrame longTermFrame = new JFrame("Long Term Potential");
                longTermFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                longTermFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                LongTermPotentialPanel panel = new LongTermPotentialPanel();
                panel.setOnBack(() -> longTermFrame.dispose());
                longTermFrame.add(panel);
                longTermFrame.setVisible(true);
            }
        });
        JButton btnMonteCarlo = createDarkButton("Monte Carlo Simulation");
        btnMonteCarlo.addActionListener(e -> {
            if (GUI.onShowMonteCarlo != null) {
                GUI.onShowMonteCarlo.run();
            } else {
                JFrame monteCarloFrame = new JFrame("Monte Carlo Simulation");
                monteCarloFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                monteCarloFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                MonteCarloPanel panel = new MonteCarloPanel();
                panel.setOnBack(() -> monteCarloFrame.dispose());
                monteCarloFrame.add(panel);
                monteCarloFrame.setVisible(true);
            }
        });
        navigationPanel.add(btnLongTerm);
        navigationPanel.add(btnMonteCarlo);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dashboardContainer.add(navigationPanel, gbc);
    }
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        return label;
    }
    private JButton createDarkButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        return button;
    }
    private void addAsset() {
        try {
            String assetType = assetComboBox.getSelectedItem().toString();
            int pct = Integer.parseInt(pctField.getText().trim());
            if (pct <= 0) {
                JOptionPane.showMessageDialog(null, "Percentage must be greater than 0.");
                return;
            }
            if (getTotalPct() + pct > 100) {
                JOptionPane.showMessageDialog(null, "Total allocation cannot exceed 100%.");
                return;
            }
            Asset asset = new Asset(0, currentPortfolioID, assetType, pct, 0);
            assets.add(asset);
            piechart.repaint();
            assetArea.append(assetType + " - " + pct + "%\n");
            pctField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid percentage.");
        }
    }
    private void saveAssets() {
        for (Asset asset : assets) {
            DatabaseManager.addAsset(
                    currentPortfolioID,
                    asset.getAssetType(),
                    (double) asset.getAllocationPercentage(),
                    (double) asset.getAmount()
            );
        }
        JOptionPane.showMessageDialog(null, "Assets saved successfully!");
        piechart.repaint();
    }
    private int getTotalPct() {
        int total = 0;
        for (Asset asset : assets) {
            total += asset.getAllocationPercentage();
        }
        return total;
    }
    public void setPortfolioText(String text) {
        lblPortfolioName.setText(text);
    }
    public void setAgeText(String text) {
        lblAge.setText("Age: " + text);
    }
    public void setIncomeText(String text) {
        lblIncome.setText("Income: $" + text);
    }
    public void setRiskText(String text) {
        lblRisk.setText("Risk Tolerance: " + text);
    }
    public void setNetWorthText(String text) {
        lblNetWorth.setText("Net Worth: " + text);
    }
    public void setPortfolioId(int id) {
        currentPortfolioID = id;
        assets.clear();
        if (assetArea != null) {
            assetArea.setText("");
        }
        java.util.List<Asset> existing = DatabaseManager.getAssetsByPortfolioId(id);
        if (existing != null) {
            for (Asset a : existing) {
                assets.add(a);
                if (assetArea != null) {
                    assetArea.append(
                            a.getAssetType()
                                    + " - "
                                    + a.getAllocationPercentage()
                                    + "%\n"
                    );
                }
            }
        }
        if (piechart != null) {
            piechart.repaint();
        }
    }
}
