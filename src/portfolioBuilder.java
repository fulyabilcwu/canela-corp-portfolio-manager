import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class portfolioBuilder extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField ageField;
    private JTextField incomeField;
    private JTextField portfolioNameField;
    private JTextField totalValueField;
    private JComboBox<String> riskComboBox;

    private int currentUserID;
    private User user;

    private static final Color HEADER_COLOR = new Color(204, 88, 80);
    private static final Color BODY_COLOR   = new Color(245, 210, 205);
    private static final Color TEXT_COLOR   = new Color(40, 30, 30);

    public portfolioBuilder() {
        currentUserID = -1;
        buildUI();
    }

    public portfolioBuilder(int userID) {
        currentUserID = userID;
        user = DatabaseManager.getUserById(userID);
        buildUI();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void buildUI() {
        setTitle("Portfolio Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(BODY_COLOR);
        setContentPane(contentPane);

        JLabel title = new JLabel("BUILD YOUR PORTFOLIO");
        title.setOpaque(true);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Tahoma", Font.BOLD, 40));
        title.setBackground(HEADER_COLOR);
        title.setPreferredSize(new Dimension(0, 80));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BODY_COLOR);
        contentPane.add(centerWrapper, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BODY_COLOR);
        centerWrapper.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Tahoma", Font.BOLD, 20);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 20);
        Dimension fieldSize = new Dimension(220, 40);

        // =========================
        // PANEL 1: USER PROFILE
        // =========================
        JPanel userProfilePanel = new JPanel(new GridBagLayout());
        userProfilePanel.setBackground(BODY_COLOR);
        userProfilePanel.setBorder(BorderFactory.createTitledBorder("User Profile"));

        GridBagConstraints userGbc = new GridBagConstraints();
        userGbc.insets = new Insets(10, 10, 10, 10);
        userGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        ageLabel.setForeground(TEXT_COLOR);

        ageField = new JTextField();
        ageField.setFont(fieldFont);
        ageField.setPreferredSize(fieldSize);

        JLabel incomeLabel = new JLabel("Income:");
        incomeLabel.setFont(labelFont);
        incomeLabel.setForeground(TEXT_COLOR);

        incomeField = new JTextField();
        incomeField.setFont(fieldFont);
        incomeField.setPreferredSize(fieldSize);

        if (user != null) {
            if (user.getAge() > 0) {
                ageField.setText(String.valueOf(user.getAge()));
            }

            if (user.getIncome() > 0) {
                incomeField.setText(String.valueOf(user.getIncome()));
            }
        }

        userGbc.gridx = 0;
        userGbc.gridy = 0;
        userProfilePanel.add(ageLabel, userGbc);

        userGbc.gridx = 1;
        userProfilePanel.add(ageField, userGbc);

        userGbc.gridx = 0;
        userGbc.gridy = 1;
        userProfilePanel.add(incomeLabel, userGbc);

        userGbc.gridx = 1;
        userProfilePanel.add(incomeField, userGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(userProfilePanel, gbc);

        // =========================
        // PANEL 2: PORTFOLIO DATA
        // =========================
        JPanel portfolioPanel = new JPanel(new GridBagLayout());
        portfolioPanel.setBackground(BODY_COLOR);
        portfolioPanel.setBorder(BorderFactory.createTitledBorder("Portfolio Data"));

        GridBagConstraints portfolioGbc = new GridBagConstraints();
        portfolioGbc.insets = new Insets(10, 10, 10, 10);
        portfolioGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel portfolioNameLabel = new JLabel("Portfolio Name:");
        portfolioNameLabel.setFont(labelFont);
        portfolioNameLabel.setForeground(TEXT_COLOR);

        portfolioNameField = new JTextField();
        portfolioNameField.setFont(fieldFont);
        portfolioNameField.setPreferredSize(fieldSize);

        JLabel totalValueLabel = new JLabel("Total Value:");
        totalValueLabel.setFont(labelFont);
        totalValueLabel.setForeground(TEXT_COLOR);

        totalValueField = new JTextField();
        totalValueField.setFont(fieldFont);
        totalValueField.setPreferredSize(fieldSize);

        JLabel riskLabel = new JLabel("Risk Tolerance:");
        riskLabel.setFont(labelFont);
        riskLabel.setForeground(TEXT_COLOR);

        riskComboBox = new JComboBox<>();
        riskComboBox.addItem("Low");
        riskComboBox.addItem("Medium");
        riskComboBox.addItem("High");
        riskComboBox.setFont(fieldFont);
        riskComboBox.setPreferredSize(fieldSize);

        portfolioGbc.gridx = 0;
        portfolioGbc.gridy = 0;
        portfolioPanel.add(portfolioNameLabel, portfolioGbc);

        portfolioGbc.gridx = 1;
        portfolioPanel.add(portfolioNameField, portfolioGbc);

        portfolioGbc.gridx = 0;
        portfolioGbc.gridy = 1;
        portfolioPanel.add(totalValueLabel, portfolioGbc);

        portfolioGbc.gridx = 1;
        portfolioPanel.add(totalValueField, portfolioGbc);

        portfolioGbc.gridx = 0;
        portfolioGbc.gridy = 2;
        portfolioPanel.add(riskLabel, portfolioGbc);

        portfolioGbc.gridx = 1;
        portfolioPanel.add(riskComboBox, portfolioGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(portfolioPanel, gbc);

        // =========================
        // BUTTONS
        // =========================
        JButton generatePortfolioBtn = new JButton("Generate Portfolio");
        generatePortfolioBtn.setBackground(Color.WHITE);
        generatePortfolioBtn.setPreferredSize(new Dimension(250, 50));
        generatePortfolioBtn.setFont(new Font("Tahoma", Font.PLAIN, 22));

        generatePortfolioBtn.addActionListener(e -> generatePortfolio());

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 18));

        backButton.addActionListener(e -> {
            if (GUI.onBackToLogin != null) {
                GUI.onBackToLogin.run();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(generatePortfolioBtn);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 20, 15);
        formPanel.add(buttonPanel, gbc);
    }

    private void generatePortfolio() {
        String ageText = ageField.getText().trim();
        String incomeText = incomeField.getText().trim();
        String portfolioName = portfolioNameField.getText().trim();
        String totalValueText = totalValueField.getText().trim();
        String risk = riskComboBox.getSelectedItem().toString();

        if (ageText.isEmpty() || incomeText.isEmpty()
                || portfolioName.isEmpty() || totalValueText.isEmpty()
                || risk.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please fill in all required fields before generating a portfolio."
            );
            return;
        }

        int ageVal;
        try {
            ageVal = Integer.parseInt(ageText);

            if (ageVal <= 0 || ageVal > 120) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Age must be a whole number between 1 and 120.",
                    "Invalid Age",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double incomeVal;
        try {
            incomeVal = Double.parseDouble(incomeText);

            if (incomeVal < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Income must be a valid positive number.",
                    "Invalid Income",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double totalValueVal;
        try {
            totalValueVal = Double.parseDouble(totalValueText);

            if (totalValueVal <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Total Value must be a valid number greater than 0.",
                    "Invalid Total Value",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Save/update user profile data first
        DatabaseManager.updateUserInfo(
                currentUserID,
                "",
                "",
                "",
                ageVal,
                incomeVal,
                null,
                "",
                ""
        );

        // Create portfolio
        boolean portfolioCreated = DatabaseManager.generatePortfolio(
                currentUserID,
                portfolioName,
                totalValueVal,
                risk
        );

        if (!portfolioCreated) {
            JOptionPane.showMessageDialog(
                    null,
                    "Could not create portfolio.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int portfolioId = DatabaseManager.getLatestPortfolioId(currentUserID);

        GUI.latestPortfolioId = portfolioId;
        GUI.latestPortfolioName = portfolioName;
        GUI.latestAge = String.valueOf(ageVal);
        GUI.latestIncome = String.valueOf(incomeVal);
        GUI.latestNetWorth = String.valueOf(totalValueVal);
        GUI.latestRisk = risk;

        if (GUI.onPortfolioCreated != null) {
            GUI.onPortfolioCreated.run();
        } else {
            portfolioDashboard dash = new portfolioDashboard(currentUserID);
            dash.setPortfolioId(portfolioId);
            dash.setPortfolioText(portfolioName);
            dash.setVisible(true);
        }
    }
} // end of portfolioBuilder