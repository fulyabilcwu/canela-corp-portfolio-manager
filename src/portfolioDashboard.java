import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class portfolioDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel lblPortfolioName;
    private JLabel lblAge;
    private JLabel lblIncome;
    private JLabel lblRisk;
    private JLabel lblNetWorth;

    private JComboBox<String> assetComboBox;
    private JTextField pctField;
    private JTextArea assetArea;

    private JButton btnViewAllPortfolios;
    private JButton btnViewAllUsers;

    private int currentPortfolioID;
    private ArrayList<Asset> assets;
    private PiechartPanel piechart;

    // Shared style constants — mirrors portfolioBuilder
    private static final Color BODY_COLOR   = Color.DARK_GRAY;
    private static final Color HEADER_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR   = Color.WHITE;
    private static final Font  LABEL_FONT   = new Font("Tahoma", Font.BOLD, 50);
    private static final Font  FIELD_FONT   = new Font("Tahoma", Font.PLAIN, 25);
    private static final Font  BTN_FONT     = new Font("Tahoma", Font.PLAIN, 20);

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    portfolioDashboard frame = new portfolioDashboard();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public portfolioDashboard() {
        assets = new ArrayList<>();

        setTitle("Portfolio Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setMinimumSize(new Dimension(700, 500));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(BODY_COLOR);
        setContentPane(contentPane);

        contentPane.add(buildHeader(), BorderLayout.NORTH);
        contentPane.add(buildBody(),   BorderLayout.CENTER);
    }

    // -------------------------------------------------------------------------
    // Header — mirrors portfolioBuilder's lblNewLabel style
    // -------------------------------------------------------------------------

    private JLabel buildHeader() {
        lblPortfolioName = new JLabel("PORTFOLIO DASHBOARD");
        lblPortfolioName.setOpaque(true);
        lblPortfolioName.setHorizontalAlignment(SwingConstants.CENTER);
        lblPortfolioName.setForeground(TEXT_COLOR);
        lblPortfolioName.setBackground(HEADER_COLOR);
        lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 40));
        lblPortfolioName.setPreferredSize(new Dimension(0, 100));
        return lblPortfolioName;
    }

    // -------------------------------------------------------------------------
    // Body — GridBagLayout centered wrapper, same as portfolioBuilder
    // -------------------------------------------------------------------------

    private JPanel buildBody() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BODY_COLOR);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BODY_COLOR);
        centerWrapper.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 150, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.NONE;

        // Row 0 — Age | Income
        lblAge = makeLabel("Age:");
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblAge, gbc);

        lblIncome = makeLabel("Income:");
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(lblIncome, gbc);

        // Row 1 — Risk | Net Worth
        lblRisk = makeLabel("Risk Tolerance:");
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblRisk, gbc);

        lblNetWorth = makeLabel("Net Worth:");
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(lblNetWorth, gbc);

        
        piechart = new PiechartPanel(assets);
        piechart.setBackground(Color.WHITE);
        piechart.setPreferredSize(new Dimension(400, 400));

        JPanel chartWrapper = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        chartWrapper.setBackground(BODY_COLOR);
        chartWrapper.add(piechart);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(chartWrapper, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 3 — Add Asset controls
        JPanel addRow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        addRow.setBackground(BODY_COLOR);

        JLabel addLabel = new JLabel("Add Asset:");
        addLabel.setForeground(TEXT_COLOR);
        addLabel.setFont(LABEL_FONT);
        addRow.add(addLabel);

        assetComboBox = new JComboBox<String>();
        assetComboBox.addItem("Stocks");
        assetComboBox.addItem("Bonds");
        assetComboBox.addItem("Cash");
        assetComboBox.addItem("ETF");
        assetComboBox.addItem("Gold");
        assetComboBox.setFont(FIELD_FONT);
        assetComboBox.setPreferredSize(new Dimension(160, 45));
        addRow.add(assetComboBox);

        pctField = new JTextField(4);
        pctField.setFont(FIELD_FONT);
        pctField.setPreferredSize(new Dimension(80, 45));
        addRow.add(pctField);

        JLabel pctLabel = new JLabel("%");
        pctLabel.setForeground(TEXT_COLOR);
        pctLabel.setFont(LABEL_FONT);
        addRow.add(pctLabel);

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(BTN_FONT);
        btnAdd.setPreferredSize(new Dimension(100, 45));
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String assetType = assetComboBox.getSelectedItem().toString();
                    int pct = Integer.parseInt(pctField.getText());
                    if (pct <= 0) {
                        JOptionPane.showMessageDialog(null, "Percentage must be greater than 0.");
                        return;
                    }
                    if (getTotalPct() + pct > 100) {
                        JOptionPane.showMessageDialog(null, "Total allocation cannot exceed 100%.");
                        return;
                    }
                    Asset asset = new Asset(0, currentPortfolioID, assetType, assetType, pct, 0);
                    assets.add(asset);
                    piechart.repaint();
                    assetArea.append(assetType + " - " + pct + "%\n");
                    pctField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid percentage.");
                }
            }
        });
        addRow.add(btnAdd);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addRow, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 4 — Asset list scroll area
        assetArea = new JTextArea(5, 30);
        assetArea.setEditable(false);
        assetArea.setFont(FIELD_FONT);
        JScrollPane scrollPane = new JScrollPane(assetArea);
        scrollPane.setPreferredSize(new Dimension(500, 120));

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(scrollPane, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 5 — action buttons
        JPanel btnRow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 0));
        btnRow.setBackground(BODY_COLOR);

        JButton btnSaveAssets = new JButton("Save Assets");
        btnSaveAssets.setFont(BTN_FONT);
        btnSaveAssets.setBackground(Color.WHITE);
        btnSaveAssets.setPreferredSize(new Dimension(180, 50));
        btnSaveAssets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Asset asset : assets) {
                    DatabaseManager.addAsset(
                        currentPortfolioID,
                        asset.getAssetType(),
                        (double) asset.getAllocationPercentage(),
                        (double) asset.getAmount());
                }
                JOptionPane.showMessageDialog(null, "Assets saved successfully!");
                piechart.repaint();
            }
        });
        btnRow.add(btnSaveAssets);

        JButton btnLongTerm = new JButton("Long Term Potential");
        btnLongTerm.setFont(BTN_FONT);
        btnLongTerm.setBackground(Color.WHITE);
        btnLongTerm.setPreferredSize(new Dimension(220, 50));
        btnLongTerm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame longTermFrame = new JFrame("Long Term Potential");
                longTermFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                longTermFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                LongTermPotentialPanel panel = new LongTermPotentialPanel();
                panel.setOnBack(new Runnable() {
                    public void run() { longTermFrame.dispose(); }
                });
                longTermFrame.add(panel);
                longTermFrame.setVisible(true);
            }
        });
        btnRow.add(btnLongTerm);

        JButton btnMonteCarlo = new JButton("Monte Carlo Simulation");
        btnMonteCarlo.setFont(BTN_FONT);
        btnMonteCarlo.setBackground(Color.WHITE);
        btnMonteCarlo.setPreferredSize(new Dimension(260, 50));
        btnMonteCarlo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame monteCarloFrame = new JFrame("Monte Carlo Simulation");
                monteCarloFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                monteCarloFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                MonteCarloPanel panel = new MonteCarloPanel();
                panel.setOnBack(new Runnable() {
                    public void run() { monteCarloFrame.dispose(); }
                });
                monteCarloFrame.add(panel);
                monteCarloFrame.setVisible(true);
            }
        });
        btnRow.add(btnMonteCarlo);

        // Admin button in same row
        btnViewAllPortfolios = new JButton("View All Portfolios");
        btnViewAllPortfolios.setFont(BTN_FONT);
        btnViewAllPortfolios.setVisible(false);
        btnViewAllPortfolios.setPreferredSize(new Dimension(220, 50));
        btnViewAllPortfolios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Portfolio> portfolios = DatabaseManager.getAllPortfolios();
                StringBuilder sb = new StringBuilder();
                for (Portfolio p : portfolios) {
                    sb.append("[" + p.getPortfolio_ID() + "] " + p.getPortfolioName()
                        + " | Value: $" + p.getTotalValue()
                        + " | Risk: " + p.getRiskLevel() + "\n");
                }
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea),
                    "All Portfolios", JOptionPane.PLAIN_MESSAGE);
            }
        });
        btnRow.add(btnViewAllPortfolios);

        btnViewAllUsers = new JButton("View All Users");
        btnViewAllUsers.setFont(BTN_FONT);
        btnViewAllUsers.setVisible(false);
        btnViewAllUsers.setPreferredSize(new Dimension(200, 50));
        btnViewAllUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<User> users = DatabaseManager.getAllUsers();
                StringBuilder sb = new StringBuilder();
                for (User u : users) {
                    sb.append("[" + u.getUser_ID() + "] " + u.getName()
                        + " | Email: " + u.getEmail() + "\n");
                }
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea),
                    "All Users", JOptionPane.PLAIN_MESSAGE);
            }
        });
        btnRow.add(btnViewAllUsers);

        JButton btnAdmin = new JButton("Admin");
        btnAdmin.setFont(BTN_FONT);
        btnAdmin.setBackground(Color.WHITE);
        btnAdmin.setPreferredSize(new Dimension(120, 50));
        btnAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "Enter admin password:");
                if (input != null && input.equals("CANELA")) {
                    btnViewAllPortfolios.setVisible(true);
                    btnViewAllUsers.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Admin access granted!");
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect password.");
                }
            }
        });
        btnRow.add(btnAdmin);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 15, 20, 15);
        formPanel.add(btnRow, gbc);

        return centerWrapper;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_COLOR);
        l.setFont(LABEL_FONT);
        return l;
    }

    private int getTotalPct() {
        int total = 0;
        for (Asset asset : assets) {
            total += asset.getAllocationPercentage();
        }
        return total;
    }

    // -------------------------------------------------------------------------
    // Public setters
    // -------------------------------------------------------------------------

    public void setPortfolioText(String text) { lblPortfolioName.setText(text.toUpperCase()); }
    public void setAgeText(String text)        { lblAge.setText("Age: " + text); }
    public void setIncomeText(String text)     { lblIncome.setText("Income: $" + text); }
    public void setRiskText(String text)       { lblRisk.setText("Risk Tolerance: " + text); }
    public void setNetWorthText(String text)   { lblNetWorth.setText("Net Worth: " + text); }
    public void setPortfolioId(int id)         { currentPortfolioID = id; }
}