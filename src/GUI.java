import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GUI extends JFrame{
    private DatabaseManager database;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image logo = new ImageIcon(getClass().getResource("logo.png")).getImage();
    private String[] secQsList;
    private int loggedInUser = -1;
    private Admin privateAdmin = new Admin();

    // Shared portfolio context populated by portfolioBuilder
    public static int latestPortfolioId = -1;
    public static String latestPortfolioName = "";
    public static String latestAge = "";
    public static String latestIncome = "";
    public static String latestNetWorth = "";
    public static String latestRisk = "";

    // Callbacks set by GUI for other classes to trigger card navigation
    public static Runnable onPortfolioCreated = null;
    public static Runnable onBackToBuilder = null;
    public static Runnable onBackToLogin = null;
    public static Runnable onShowMonteCarlo = null;
    public static Runnable onShowLongTerm = null;
    public static Runnable onShowAdmin = null;

    public GUI(){
        database = new DatabaseManager();
        database.getConnection();
        setTitle("Canela Corop. Portfolio Manager");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setIconImage(logo);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "Sign In");
        mainPanel.add(signUpPanel(), "Sign Up");
        mainPanel.add(portfolioBuilderPanel(), "Portfolio Builder");
        mainPanel.add(dashboardPanel(), "Dashboard");
        mainPanel.add(analysisPanel(), "Analysis");
        mainPanel.add(monteCarloPanel(), "Monte Carlo");
        mainPanel.add(forgotPasswordPanel(), "Forgot Password");
        mainPanel.add(adminPanel(), "Admin");

        // Wire up cross-panel navigation callbacks
        onPortfolioCreated = () -> {
            cardLayout.show(mainPanel, "Dashboard");
            refreshPanel("Dashboard");
        };
        onBackToBuilder = () -> {
            cardLayout.show(mainPanel, "Portfolio Builder");
        };
        onBackToLogin = () -> {
            loggedInUser = -1;
            latestPortfolioId = -1;
            cardLayout.show(mainPanel, "Sign In");
        };
        onShowMonteCarlo = () -> {
            cardLayout.show(mainPanel, "Monte Carlo");
            refreshPanel("Monte Carlo");
        };
        onShowLongTerm = () -> {
            cardLayout.show(mainPanel, "Analysis");
            refreshPanel("Analysis");
        };

        onShowAdmin = () -> {
            refreshPanel("Admin");
            cardLayout.show(mainPanel, "Admin");
        };

        add(mainPanel);
        setVisible(true);

    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(204, 88, 80));

        JPanel insidePanel = new JPanel(new GridBagLayout());
        insidePanel.setPreferredSize(new Dimension(1500, 800));
        insidePanel.setMinimumSize(new Dimension(1500, 800));
        insidePanel.setBackground(new Color(245, 210, 205));
        insidePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(30, 45, 30, 45)
        ));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 10, 10, 10);
        layout.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("account.png"));
        Image i = newAccountIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(i));
        icon.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        insidePanel.add(icon, layout);

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.black);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);

        JTextArea display = new JTextArea();
        display.setFocusable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 14));
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(display, layout);

        JButton viewUsersButton = new JButton("View all users");
        viewUsersButton.setBackground(new Color(60, 60, 60));
        viewUsersButton.setForeground(Color.WHITE);
        viewUsersButton.setFocusable(false);
        viewUsersButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        viewUsersButton.addActionListener(e -> {
            List<User> users = DatabaseManager.getAllUsers();
            display.setText("");
            display.append(String.format(
            "%-8s %-20s %-30s %-15s %-20s %-20s %-5s %-12s %-12s %-10s%n",
            "User_ID",
                    "Name",
                    "Email",
                    "Password",
                    "Security_Q",
                    "Security_A",
                    "Age",
                    "Income",
                    "NetWorth",
                    "Role"));
            display.append("________________________________________________________________________________________________________________________"
                +"_____________________________________\n\n");
            for(User user: users){
                display.append(user.toString() + "\n");
            }
        });

        JButton viewPortfoliButton = new JButton("View all Portfolios");
        viewPortfoliButton.setBackground(new Color(60, 60, 60));
        viewPortfoliButton.setForeground(Color.WHITE);
        viewPortfoliButton.setFocusable(false);
        viewPortfoliButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        viewPortfoliButton.addActionListener(e -> {
            List<Portfolio> portfolios = DatabaseManager.getAllPortfolios();
            display.setText("");
            display.append(String.format(
        "%-12s %-10s %-25s %-15s %-10s%n",
        "Portfolio_ID",
                "User_ID",
                "Portfolio_Name",
                "Total_Value",
                "Risk_Level"));

            display.append("_____________________________________________________________________________\n");
            for(Portfolio portfolio: portfolios){
                display.append(portfolio.toString() + "\n");
            }
        });

        JButton viewAssetButton = new JButton("View all assets");
        viewAssetButton.setBackground(new Color(60, 60, 60));
        viewAssetButton.setForeground(Color.WHITE);
        viewAssetButton.setFocusable(false);
        viewAssetButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        viewAssetButton.addActionListener(e -> {
            List<Asset> assets = DatabaseManager.getallAsets();
            display.setText("");
            display.append(String.format(
            "%-12s %-15s %-25s %-18s %-15s%n",
            "Asset_ID",
                    "Portfolio_ID",
                    "Asset_Type",
                    "Allocation_%",
                    "Amount"));
            display.append("________________________________________________________________________________\n\n");
            for(Asset asset: assets){
                display.append(asset.toString() + "\n");
            }
        });

        JPanel viewButtons = new JPanel(new GridLayout(0, 3, 10, 0));
        viewButtons.setOpaque(false);
        viewButtons.add(viewUsersButton);
        viewButtons.add(viewPortfoliButton);
        viewButtons.add(viewAssetButton);

        layout.gridx = 0;
        layout.gridy = 2;
        insidePanel.add(viewButtons, layout);

        // search by user panel

        JPanel searchUserPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchUserPanel.setOpaque(false);

        JTextField search = new JTextField("Search by user ID");
        search.setForeground(Color.GRAY);
        search.setPreferredSize(new Dimension(300, 30));

        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals("Search by user ID")) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().trim().isEmpty()) {
                    search.setText("Search by user ID");
                    search.setForeground(Color.GRAY);
                }
            }
        });

        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setBackground(new Color(60, 60, 60));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusable(false);
        searchButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        

        searchUserPanel.add(search);
        searchUserPanel.add(searchButton);

        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(searchUserPanel, layout);

        // search by portfolio panel

        JPanel searchPortfolioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchUserPanel.setOpaque(false);

        JTextField searchP = new JTextField("Search by portfolio ID");
        searchP.setForeground(Color.GRAY);
        searchP.setPreferredSize(new Dimension(300, 30));

        searchP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchP.getText().equals("Search by portfolio ID")) {
                    searchP.setText("");
                    searchP.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchP.getText().trim().isEmpty()) {
                    searchP.setText("Search by portfolio ID");
                    searchP.setForeground(Color.GRAY);
                }
            }
        });

        JButton searchPButton = new JButton("Search");
        searchPButton.setPreferredSize(new Dimension(100, 30));
        searchPButton.setBackground(new Color(60, 60, 60));
        searchPButton.setForeground(Color.WHITE);
        searchPButton.setFocusable(false);
        searchPButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        searchUserPanel.add(searchP);
        searchUserPanel.add(searchPButton);

        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(searchPortfolioPanel, layout);

        // search by asset panel

        JPanel searchAssetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchUserPanel.setOpaque(false);

        JTextField searchA = new JTextField("Search by asset ID");
        searchA.setForeground(Color.GRAY);
        searchA.setPreferredSize(new Dimension(300, 30));

        searchA.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchA.getText().equals("Search by asset ID")) {
                    searchA.setText("");
                    searchA.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchA.getText().trim().isEmpty()) {
                    searchA.setText("Search by asset ID");
                    searchA.setForeground(Color.GRAY);
                }
            }
        });

        JButton searchAButton = new JButton("Search");
        searchAButton.setPreferredSize(new Dimension(100, 30));
        searchAButton.setBackground(new Color(60, 60, 60));
        searchAButton.setForeground(Color.WHITE);
        searchAButton.setFocusable(false);
        searchAButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        searchUserPanel.add(searchA);
        searchUserPanel.add(searchAButton);

        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(searchPortfolioPanel, layout);


        searchButton.addActionListener(e -> {
            display.setText("");
            searchP.setText("Search by portfolio ID");
            searchP.setForeground(Color.gray);
            searchA.setText("Search by asset ID");
            searchA.setForeground(Color.gray);
            insidePanel.requestFocusInWindow();
            User user = DatabaseManager.getUserById(Integer.parseInt(search.getText()));
            if(user != null){
                display.append(String.format(
            "%-8s %-20s %-30s %-15s %-20s %-20s %-5s %-12s %-12s %-10s%n",
            "User_ID",
                    "Name",
                    "Email",
                    "Password",
                    "Security_Q",
                    "Security_A",
                    "Age",
                    "Income",
                    "NetWorth",
                    "Role"));
                display.append("________________________________________________________________________________________________________________________"
                    +"_____________________________________\n\n");
                display.append(user.toString());
            }else JOptionPane.showMessageDialog(null, "User doesn't exist. Check your input!", "Error!", JOptionPane.INFORMATION_MESSAGE);
        });

        searchPButton.addActionListener(e -> {
            display.setText("");
            search.setText("Search by user ID");
            search.setForeground(Color.gray);
            searchA.setText("Search by asset ID");
            searchA.setForeground(Color.gray);
            insidePanel.requestFocusInWindow();
            Portfolio portfolio = DatabaseManager.getPortfolioById(Integer.parseInt(searchP.getText()));
            if(portfolio != null){
                display.setText("");
                display.append(String.format(
            "%-12s %-10s %-25s %-15s %-10s%n",
            "Portfolio_ID",
                    "User_ID",
                    "Portfolio_Name",
                    "Total_Value",
                    "Risk_Level"));

                display.append("_____________________________________________________________________________\n");
                display.append(portfolio.toString());
            }else JOptionPane.showMessageDialog(null, "Portoflio doesn't exist. Check your input!", "Error!", JOptionPane.INFORMATION_MESSAGE);
        });

        searchAButton.addActionListener(e -> {
            display.setText("");
            search.setText("Search by user ID");
            search.setForeground(Color.gray);
            searchP.setText("Search by portfolio ID");
            searchP.setForeground(Color.gray);
            insidePanel.requestFocusInWindow();
            Asset asset = DatabaseManager.getAssetByID(Integer.parseInt(searchP.getText()));
            if(asset != null){
                display.append(String.format(
            "%-12s %-15s %-25s %-18s %-15s%n",
            "Asset_ID",
                    "Portfolio_ID",
                    "Asset_Type",
                    "Allocation_%",
                    "Amount"));
                display.append("________________________________________________________________________________\n\n");
                display.append(asset.toString());
            }else JOptionPane.showMessageDialog(null, "Sddry doesn't exist. Check your input!", "Error!", JOptionPane.INFORMATION_MESSAGE);
        });


        // delete buttons
        JButton deleteUser = new JButton("Delete selected user");
        deleteUser.setBackground(new Color(60, 60, 60));
        deleteUser.setForeground(Color.WHITE);
        deleteUser.setFocusable(false);
        deleteUser.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        deleteUser.addActionListener(e -> {
            if(search.getText().equals("Search by user ID"))JOptionPane.showMessageDialog(null, "You must search for the user_id first!", "Error", JOptionPane.INFORMATION_MESSAGE);
            else{
                boolean result = privateAdmin.deleteUser(Integer.parseInt(search.getText()));
                if(result) JOptionPane.showMessageDialog(null, "Selected user deleted successfully!", "Status", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            
        });

        JButton deltePortfolio = new JButton("Delete selected portfolio");
        deltePortfolio.setBackground(new Color(60, 60, 60));
        deltePortfolio.setForeground(Color.WHITE);
        deltePortfolio.setFocusable(false);
        deltePortfolio.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        deltePortfolio.addActionListener(e -> {
            if(searchP.getText().equals("Search by portfolio ID"))JOptionPane.showMessageDialog(null, "You must search for the portoflio_id first!", "Error", JOptionPane.INFORMATION_MESSAGE);
            else{
                boolean result = privateAdmin.deletePortfolio(Integer.parseInt(searchP.getText()));
                if(result) JOptionPane.showMessageDialog(null, "Selected portfolio deleted successfully!", "Status", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            
        });

        JButton deleteAsset = new JButton("Delete selected asset");
        deleteAsset.setBackground(new Color(60, 60, 60));
        deleteAsset.setForeground(Color.WHITE);
        deleteAsset.setFocusable(false);
        deleteAsset.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        deleteAsset.addActionListener(e -> {
            if(searchA.getText().equals("Search by asset ID"))JOptionPane.showMessageDialog(null, "You must search for the asset_id first!", "Error", JOptionPane.INFORMATION_MESSAGE);
            else{
                boolean result = privateAdmin.deleteAsset(Integer.parseInt(searchA.getText()));
                if(result) JOptionPane.showMessageDialog(null, "Selected asset deleted successfully!", "Status", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        });
            

        JPanel deleteButtons = new JPanel(new GridLayout(0, 3, 10, 0));
        deleteButtons.setOpaque(false);
        deleteButtons.add(deleteUser);
        deleteButtons.add(deltePortfolio);
        deleteButtons.add(deleteAsset);


        layout.gridx = 0;
        layout.gridy = 6;
        insidePanel.add(deleteButtons, layout);

        //update buttons
        JButton updateUser = new JButton("Update selected users");
        updateUser.setBackground(new Color(60, 60, 60));
        updateUser.setForeground(Color.WHITE);
        updateUser.setFocusable(false);
        updateUser.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        updateUser.addActionListener(e -> {
            String input = search.getText().trim();

            if(input.equals("Search by user ID")){
                JOptionPane.showMessageDialog(
                    null,
                    "Please search for a user first."
                );
                return;
            }

            int userId = Integer.parseInt(input);

            JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(panel),
                "Update User",
                true
            );

            dialog.setContentPane(updateUserPanel(userId));
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        JButton updatePortfolio = new JButton("Update selected Portfolios");
        updatePortfolio.setBackground(new Color(60, 60, 60));
        updatePortfolio.setForeground(Color.WHITE);
        updatePortfolio.setFocusable(false);
        updatePortfolio.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        updatePortfolio.addActionListener(e -> {
            
            String input = searchP.getText().trim();

            if (input.isEmpty() || input.equals("Search by portfolio ID")) {

                JOptionPane.showMessageDialog(
                        null,
                        "You must search for the portfolio_id first!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            int portfolioId = Integer.parseInt(input);

            JDialog dialog = new JDialog(
                    (Frame) SwingUtilities.getWindowAncestor(panel),
                    "Update Portfolio",
                    true
            );

            dialog.setContentPane(updatePortfolioPanel(portfolioId));

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        JButton updateAsset = new JButton("Update selected asset");
        updateAsset.setBackground(new Color(60, 60, 60));
        updateAsset.setForeground(Color.WHITE);
        updateAsset.setFocusable(false);
        updateAsset.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        updateAsset.addActionListener(e -> {
            
            String input = searchA.getText().trim();

            if (input.isEmpty() || input.equals("Search by asset ID")) {

                JOptionPane.showMessageDialog(
                        null,
                        "You must search for the asset_id first!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return;
            }

            int assetId = Integer.parseInt(input);

            JDialog dialog = new JDialog(
                    (Frame) SwingUtilities.getWindowAncestor(panel),
                    "Update Asset",
                    true
            );

            dialog.setContentPane(updateAssetPanel(assetId));

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            
        });

        JPanel updateButtons = new JPanel(new GridLayout(0, 3, 10, 0));
        updateButtons.setOpaque(false);
        updateButtons.add(updateUser);
        updateButtons.add(updatePortfolio);
        updateButtons.add(updateAsset);

        layout.gridx = 0;
        layout.gridy = 7;
        insidePanel.add(updateButtons, layout);


        panel.add(insidePanel);

        insidePanel.setFocusable(true);
        SwingUtilities.invokeLater(() -> {
            insidePanel.requestFocusInWindow();
        });
        return panel;
    }

    private JPanel updateAssetPanel(int assetId) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 210, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(8, 8, 8, 8);
        layout.fill = GridBagConstraints.HORIZONTAL;

        Asset asset = DatabaseManager.getAssetByID(assetId);

        if (asset == null) {
            panel.add(new JLabel("Asset not found."));
            return panel;
        }

        JLabel title = new JLabel("Update Asset", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JTextField assetTypeField =
                new JTextField(asset.getAssetType(), 20);

        JTextField allocationField =
                new JTextField(String.valueOf(asset.getAllocationPercentage()), 20);

        JTextField amountField =
                new JTextField(String.valueOf(asset.getAmount()), 20);

        JButton saveButton = new JButton("Save Changes");

        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        panel.add(title, layout);

        layout.gridwidth = 1;

        layout.gridx = 0;
        layout.gridy = 1;
        panel.add(new JLabel("Asset Type:"), layout);

        layout.gridx = 1;
        panel.add(assetTypeField, layout);

        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(new JLabel("Allocation %:"), layout);

        layout.gridx = 1;
        panel.add(allocationField, layout);

        layout.gridx = 0;
        layout.gridy = 3;
        panel.add(new JLabel("Amount:"), layout);

        layout.gridx = 1;
        panel.add(amountField, layout);

        layout.gridx = 0;
        layout.gridy = 4;
        layout.gridwidth = 2;

        panel.add(saveButton, layout);

        saveButton.addActionListener(e -> {

            try {

                String assetType =
                        assetTypeField.getText().trim();

                Double allocationPercentage =
                        Double.parseDouble(allocationField.getText().trim());

                Double amount =
                        Double.parseDouble(amountField.getText().trim());

                boolean result =
                        privateAdmin.updateAsset(
                                assetId,
                                assetType,
                                allocationPercentage,
                                amount
                        );

                if (result) {

                    JOptionPane.showMessageDialog(
                            panel,
                            "Asset updated successfully!",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    SwingUtilities.getWindowAncestor(panel).dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            panel,
                            "Update failed.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        panel,
                        "Allocation percentage and amount must be valid numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return panel;
    }

    private JPanel updateUserPanel(int userId) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 210, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(8, 8, 8, 8);
        layout.fill = GridBagConstraints.HORIZONTAL;

        User user = DatabaseManager.getUserById(userId);

        if (user == null) {
            panel.add(new JLabel("User not found."));
            return panel;
        }

        JLabel title = new JLabel("Update User Information", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JTextField nameField = new JTextField(user.getName(), 20);
        JTextField emailField = new JTextField(user.getEmail(), 20);
        JTextField passwordField = new JTextField(user.getPassword(), 20);
        JTextField ageField = new JTextField(String.valueOf(user.getAge()), 20);
        JTextField incomeField = new JTextField(String.valueOf(user.getIncome()), 20);
        JTextField netWorthField = new JTextField(String.valueOf(user.getNetWorth()), 20);
        JTextField securityQField = new JTextField(user.getSecurityQ(), 20);
        JTextField securityAField = new JTextField(user.getSecurityA(), 20);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(60, 60, 60));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusable(false);

        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        panel.add(title, layout);

        layout.gridwidth = 1;

        layout.gridx = 0;
        layout.gridy = 1;
        panel.add(new JLabel("Name:"), layout);
        layout.gridx = 1;
        panel.add(nameField, layout);

        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(new JLabel("Email:"), layout);
        layout.gridx = 1;
        panel.add(emailField, layout);

        layout.gridx = 0;
        layout.gridy = 3;
        panel.add(new JLabel("Password:"), layout);
        layout.gridx = 1;
        panel.add(passwordField, layout);

        layout.gridx = 0;
        layout.gridy = 4;
        panel.add(new JLabel("Age:"), layout);
        layout.gridx = 1;
        panel.add(ageField, layout);

        layout.gridx = 0;
        layout.gridy = 5;
        panel.add(new JLabel("Income:"), layout);
        layout.gridx = 1;
        panel.add(incomeField, layout);

        layout.gridx = 0;
        layout.gridy = 6;
        panel.add(new JLabel("Net Worth:"), layout);
        layout.gridx = 1;
        panel.add(netWorthField, layout);

        layout.gridx = 0;
        layout.gridy = 7;
        panel.add(new JLabel("Security Question:"), layout);
        layout.gridx = 1;
        panel.add(securityQField, layout);

        layout.gridx = 0;
        layout.gridy = 8;
        panel.add(new JLabel("Security Answer:"), layout);
        layout.gridx = 1;
        panel.add(securityAField, layout);

        layout.gridx = 0;
        layout.gridy = 9;
        layout.gridwidth = 2;
        panel.add(saveButton, layout);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();
                String securityQ = securityQField.getText().trim();
                String securityA = securityAField.getText().trim();

                Integer age = Integer.parseInt(ageField.getText().trim());
                Double income = Double.parseDouble(incomeField.getText().trim());
                Double netWorth = Double.parseDouble(netWorthField.getText().trim());

                boolean result = privateAdmin.updateUser(
                        userId,
                        name,
                        email,
                        password,
                        age,
                        income,
                        netWorth,
                        securityQ,
                        securityA
                );

                if (result) {
                    JOptionPane.showMessageDialog(
                            panel,
                            "User updated successfully!",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    SwingUtilities.getWindowAncestor(panel).dispose();

                } else {
                    JOptionPane.showMessageDialog(
                            panel,
                            "Update failed.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        panel,
                        "Age, income, and net worth must be valid numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return panel;
    }

        private JPanel updatePortfolioPanel(int portfolioId) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 210, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(8, 8, 8, 8);
        layout.fill = GridBagConstraints.HORIZONTAL;

        Portfolio portfolio =
                DatabaseManager.getPortfolioById(portfolioId);

        if (portfolio == null) {
            panel.add(new JLabel("Portfolio not found."));
            return panel;
        }

        JLabel title =
                new JLabel("Update Portfolio", SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JTextField userIdField =
                new JTextField(String.valueOf(portfolio.getUser_ID()), 20);

        JTextField portfolioNameField =
                new JTextField(portfolio.getPortfolioName(), 20);

        JTextField totalValueField =
                new JTextField(String.valueOf(portfolio.getTotalValue()), 20);

        JTextField riskLevelField =
                new JTextField(portfolio.getRiskLevel(), 20);

        JButton saveButton = new JButton("Save Changes");

        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        panel.add(title, layout);

        layout.gridwidth = 1;

        layout.gridx = 0;
        layout.gridy = 1;
        panel.add(new JLabel("User ID:"), layout);

        layout.gridx = 1;
        panel.add(userIdField, layout);

        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(new JLabel("Portfolio Name:"), layout);

        layout.gridx = 1;
        panel.add(portfolioNameField, layout);

        layout.gridx = 0;
        layout.gridy = 3;
        panel.add(new JLabel("Total Value:"), layout);

        layout.gridx = 1;
        panel.add(totalValueField, layout);

        layout.gridx = 0;
        layout.gridy = 4;
        panel.add(new JLabel("Risk Level:"), layout);

        layout.gridx = 1;
        panel.add(riskLevelField, layout);

        layout.gridx = 0;
        layout.gridy = 5;
        layout.gridwidth = 2;

        panel.add(saveButton, layout);

        saveButton.addActionListener(e -> {

            try {

                Integer userId =
                        Integer.parseInt(userIdField.getText().trim());

                String portfolioName =
                        portfolioNameField.getText().trim();

                Double totalValue =
                        Double.parseDouble(totalValueField.getText().trim());

                String riskLevel =
                        riskLevelField.getText().trim();

                boolean result =
                        privateAdmin.updatePortfolio(
                                portfolioId,
                                userId,
                                portfolioName,
                                totalValue,
                                riskLevel
                        );

                if (result) {

                    JOptionPane.showMessageDialog(
                            panel,
                            "Portfolio updated successfully!",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    SwingUtilities.getWindowAncestor(panel).dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            panel,
                            "Update failed.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        panel,
                        "User ID and Total Value must be valid numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel monteCarloPanel() {
        MonteCarloPanel panel = new MonteCarloPanel();
        panel.setOnBack(() -> {
            cardLayout.show(mainPanel, "Dashboard");
            refreshPanel("Monte Carlo");
        });
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel analysisPanel() {
        LongTermPotentialPanel panel = new LongTermPotentialPanel();
        panel.setOnBack(() -> {
            cardLayout.show(mainPanel, "Dashboard");
            refreshPanel("Analysis");
        });
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel dashboardPanel() {
        portfolioDashboard pd = new portfolioDashboard();
        if (latestPortfolioId != -1) {
            pd.setPortfolioId(latestPortfolioId);
            pd.setPortfolioText(latestPortfolioName);
            pd.setAgeText(latestAge);
            pd.setIncomeText(latestIncome);
            pd.setNetWorthText(latestNetWorth);
            pd.setRiskText(latestRisk);
        }
        return (JPanel) pd.getContentPane();
    }

    /**
     * 
     * @return
     */
    private JPanel portfolioBuilderPanel() {

		portfolioBuilder pb = new portfolioBuilder(loggedInUser);

        return (JPanel) pb.getContentPane();
    }

    private JPanel signUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);

        JPanel insidePanel = new JPanel(new GridBagLayout());
        insidePanel.setPreferredSize(new Dimension(600, 900));
        insidePanel.setBackground(new Color(45, 45, 42));
        insidePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(30, 45, 30, 45)
        ));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 10, 10, 10);
        layout.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("account.png"));
        Image i = newAccountIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(i));
        icon.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        insidePanel.add(icon, layout);

        JLabel title = new JLabel("Create an account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 30, 20));
        fieldsPanel.setOpaque(false);
        JLabel fName = new JLabel("First Name *");
        fName.setForeground(Color.WHITE);
        JLabel lName = new JLabel("Last Name *");
        lName.setForeground(Color.WHITE);

        JTextField fNameF = new JTextField();
        JTextField lNameF = new JTextField();

        fieldsPanel.add(fName);
        fieldsPanel.add(lName);
        fieldsPanel.add(fNameF);
        fieldsPanel.add(lNameF);

        layout.gridx = 0;
        layout.gridy = 3;
        layout.gridwidth = 2;
        insidePanel.add(fieldsPanel, layout);

        JLabel mName = new JLabel("Middle Name");
        mName.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 4;
        layout.gridwidth = 2;
        insidePanel.add(mName, layout);

        JTextField mNameF = new JTextField(20);
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(mNameF, layout);

        JLabel email = new JLabel("Email *");
        email.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 6;
        layout.gridwidth = 2;
        insidePanel.add(email, layout);

        JTextField emailField = new JTextField(20);
        layout.gridx = 0;
        layout.gridy = 7;
        insidePanel.add(emailField, layout);

        JPanel passwdFieldsPanel = new JPanel(new GridLayout(2, 2, 30, 20));
        passwdFieldsPanel.setOpaque(false);
        JLabel password = new JLabel("Password *");
        password.setForeground(Color.WHITE);

        JTextField passField = new JTextField(20);

        JLabel confirmationLabel = new JLabel("Confirm New Password *");
        confirmationLabel.setForeground(Color.WHITE);

        JTextField confirmedNewPasswd = new JTextField(20);

        passwdFieldsPanel.add(password, layout);
        passwdFieldsPanel.add(confirmationLabel, layout);
        passwdFieldsPanel.add(passField, layout);
        passwdFieldsPanel.add(confirmedNewPasswd, layout);

        layout.gridx = 0;
        layout.gridy = 8;
        layout.gridwidth = 2;
        insidePanel.add(passwdFieldsPanel, layout);

        // security question setup
        secQsList = new String[]{"", "What was the name of your first pet?",
            "What is your mom's name?",
            "What is your favorite hobby?",
            "What city are you from?",
            "What is your favorite food?",
            "What was the of your first partner/spouse?",
            "What was the name of your first bestfriend?",
            "What is your favorite sport?"
        };

        JLabel chooseQ = new JLabel("Choose a question");
        chooseQ.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 12;
        insidePanel.add(chooseQ, layout);

        JComboBox<String> questionList = new JComboBox<>(secQsList);
        questionList.setSelectedIndex(0);
        questionList.setFocusable(false);
        questionList.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 13;
        insidePanel.add(questionList, layout);

        JCheckBox createYourOwn = new JCheckBox("Create your own question");
        createYourOwn.setOpaque(false);
        createYourOwn.setForeground(Color.white);
        createYourOwn.setFocusable(false);
        layout.gridx = 0;
        layout.gridy = 14;
        insidePanel.add(createYourOwn, layout);

        JLabel questionLabel = new JLabel("Enter your question *");
        questionLabel.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 15;
        insidePanel.add(questionLabel, layout);
        questionLabel.setVisible(false);

        JTextField question = new JTextField();
        question.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 16;
        insidePanel.add(question, layout);
        question.setVisible(false);

        createYourOwn.addActionListener(e -> {
            if(createYourOwn.isSelected()){
                questionLabel.setVisible(true);
                question.setVisible(true);
                questionList.setSelectedIndex(0);
            }
        });

        JLabel answerField = new JLabel("Answer the question *");
        answerField.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 17;
        insidePanel.add(answerField, layout);

        JTextField answer = new JTextField();
        answer.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 18;
        insidePanel.add(answer, layout);

        JButton createUserAccount = new JButton("Create Account");
        createUserAccount.setBackground(new Color(60, 60, 60));
        createUserAccount.setForeground(Color.WHITE);
        createUserAccount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 19;
        layout.insets = new Insets(25, 10, 10, 10);
        insidePanel.add(createUserAccount, layout);

        JButton signUpBack = new JButton("← Back to Sign In");
        signUpBack.setBackground(new Color(60, 60, 60));
        signUpBack.setForeground(Color.WHITE);
        signUpBack.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 20;
        layout.insets = new Insets(8, 10, 10, 10);
        insidePanel.add(signUpBack, layout);
        signUpBack.addActionListener(e -> {
            cardLayout.show(mainPanel, "Sign In");
        });
        createUserAccount.addActionListener(e -> {
            String chosenQ = (String) questionList.getSelectedItem();
            if(!fNameF.getText().isEmpty() && !lNameF.getText().isEmpty() && !passField.getText().isEmpty() && !confirmedNewPasswd.getText().isEmpty() 
                && !emailField.getText().isEmpty() && (chosenQ != "" && !answer.getText().isEmpty() || chosenQ == "" && !answer.getText().isEmpty() && !question.getText().isEmpty())){
                if(passField.getText().equals(confirmedNewPasswd.getText())){
                    String passwd = passField.getText();
                    String name = fNameF.getText();
                    if(!mNameF.getText().trim().isEmpty()) name = name + " " + mNameF.getText();
                        name = name + " " + lNameF.getText();
                        String security_question = "";
                    if(createYourOwn.isSelected()) security_question = question.getText();
                    else security_question = (String) questionList.getSelectedItem();
                    boolean accountCreationSuccess = database.initiateUser(name, emailField.getText(), passwd, security_question, answer.getText());
                    if(accountCreationSuccess){
                        JOptionPane.showMessageDialog(null, "Account creation successful!", "Account Status", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "Sign In");;
                        refreshPanel("Sign Up");
                    }else JOptionPane.showMessageDialog(null, "Account creation Failed!", "Account Status", JOptionPane.INFORMATION_MESSAGE);
                }else JOptionPane.showMessageDialog(null, "Passwords do not match\nPlease try again", "Account Status", JOptionPane.INFORMATION_MESSAGE);
            }else JOptionPane.showMessageDialog(null, "All required fields must be filled\nPlease try again", "Account Status", JOptionPane.INFORMATION_MESSAGE);       
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        bottom.setOpaque(false);

        JLabel alreadyHaveOne = new JLabel("Already have an account?");
        alreadyHaveOne.setForeground(Color.WHITE);
        bottom.add(alreadyHaveOne);

        JLabel signIn = new JLabel("<html><u>Sign in</u></html>");
        signIn.setForeground(new Color(120, 140, 255));
        signIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                cardLayout.show(mainPanel, "Sign In");
                refreshPanel("Sign Up");
            }
        });
        bottom.add(signIn);

        layout.gridx = 0;
        layout.gridy = 20;
        layout.gridwidth = 2;
        insidePanel.add(bottom, layout);

        panel.add(insidePanel);
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel loginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);

        JPanel insidePanel = new JPanel(new GridBagLayout());
        insidePanel.setPreferredSize(new Dimension(600, 700));
        insidePanel.setBackground(new Color(45, 45, 42));
        insidePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(30, 45, 30, 45)
        ));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 10, 10, 10);
        layout.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("account.png"));
        Image i = newAccountIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(i));
        icon.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        insidePanel.add(icon, layout);

        JLabel title = new JLabel("Sign In", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);

        JLabel email = new JLabel("Email");
        email.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 2;
        insidePanel.add(email, layout);

        JTextField emailField = new JTextField(30);
        emailField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 3;
        layout.gridwidth = 2;
        insidePanel.add(emailField, layout);

        JLabel password = new JLabel("Password");
        password.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 4;
        insidePanel.add(password, layout);  

        JTextField passField = new JTextField(30);
        passField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(passField, layout);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 60, 60));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 6;
        layout.insets = new Insets(25, 10, 10, 10);
        insidePanel.add(loginButton, layout);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(60, 60, 60));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 7;
        insidePanel.add(signUpButton, layout);

        JLabel forgotPassword = new JLabel("<html><u>Forgot your password?<u></html>");
        forgotPassword.setForeground(new Color(120, 140, 255));
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                cardLayout.show(mainPanel, "Forgot Password");
                refreshPanel("Sign In");
            }
        });
        layout.gridx = 0;
        layout.gridy = 8;
        layout.anchor = GridBagConstraints.CENTER;
        layout.fill = GridBagConstraints.NONE;
        insidePanel.add(forgotPassword, layout);

        signUpButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Sign Up");
            refreshPanel("Sign In");
        });

        loginButton.addActionListener(e -> {
            String emailInput = emailField.getText();
            String passInput = passField.getText();
            boolean correctCredentials = database.loginUser(emailInput, passInput);
            if(correctCredentials){
            	loggedInUser = DatabaseManager.getUserIdByEmail(emailInput.trim());
                mainPanel.remove(getPanel("Portfolio Builder"));
                mainPanel.add(portfolioBuilderPanel(), "Portfolio Builder");
                mainPanel.revalidate();
                mainPanel.repaint();
                cardLayout.show(mainPanel, "Portfolio Builder");
            } 
            else{
                JOptionPane.showMessageDialog(null, "The credentials were not recognized\nPlease try again!", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(insidePanel);
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel forgotPasswordPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);

        JPanel insidePanel = new JPanel(new GridBagLayout());
        insidePanel.setPreferredSize(new Dimension(600, 700));
        insidePanel.setBackground(new Color(45, 45, 42));
        insidePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(30, 45, 30, 45)
        ));

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 10, 10, 10);
        layout.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("secure.png"));
        Image i = newAccountIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(i));
        icon.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        insidePanel.add(icon, layout);

        JLabel title = new JLabel("Reset Password", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);


        JLabel email = new JLabel("Email *");
        email.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 2;
        insidePanel.add(email, layout);

        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(emailField, layout);

        JLabel question = new JLabel("Answer the following question:");
        question.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 4;
        insidePanel.add(question, layout);
        question.setVisible(false);

        JTextField questionField = new JTextField();
        questionField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(questionField, layout);
        questionField.setVisible(false);

        JLabel answer = new JLabel("Enter Your Answer *");
        answer.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 6;
        insidePanel.add(answer, layout);
        answer.setVisible(false);

        JTextField answerField = new JTextField();
        answerField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 7;
        insidePanel.add(answerField, layout);
        answerField.setVisible(false);

        JLabel passwordLabel = new JLabel("New Password *");
        passwordLabel.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 8;
        insidePanel.add(passwordLabel, layout);
        passwordLabel.setVisible(false);

        JTextField newPassword = new JTextField();
        newPassword.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 9;
        layout.gridwidth = 2;
        insidePanel.add(newPassword, layout);
        newPassword.setVisible(false);

        JLabel confirmationLabel = new JLabel("Confirm New Password *");
        confirmationLabel.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 10;
        insidePanel.add(confirmationLabel, layout);
        confirmationLabel.setVisible(false);

        JTextField confirmedNewPasswd = new JTextField();
        confirmedNewPasswd.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 11;
        insidePanel.add(confirmedNewPasswd, layout);
        confirmedNewPasswd.setVisible(false);

        JButton back = new JButton("Back");
        back.setBackground(new Color(60, 60, 60));
        back.setForeground(Color.WHITE);
        back.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JButton step1 = new JButton("Continue");
        step1.setBackground(new Color(60, 60, 60));
        step1.setForeground(Color.WHITE);
        step1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 4;
        insidePanel.add(step1, layout);

        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(back, layout);

        JButton step2 = new JButton("Continue");
        step2.setBackground(new Color(60, 60, 60));
        step2.setForeground(Color.WHITE);
        step2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 8;
        insidePanel.add(step2, layout);
        step2.setVisible(false);

        JButton step3 = new JButton("Continue");
        step3.setBackground(new Color(60, 60, 60));
        step3.setForeground(Color.WHITE);
        step3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 12;
        insidePanel.add(step3, layout);
        step3.setVisible(false);

        step1.addActionListener(e -> {
            if(!emailField.getText().isEmpty()){
                questionField.setText(database.getSecurityQuestion(emailField.getText()));
                question.setVisible(true);
                questionField.setVisible(true);
                answer.setVisible(true);
                answerField.setVisible(true);
                step1.setVisible(false);
                step2.setVisible(true);

                layout.gridx = 0;
                layout.gridy = 9;
                insidePanel.add(back, layout);
            }else JOptionPane.showMessageDialog(null, "You must enter an email address", "Error", JOptionPane.INFORMATION_MESSAGE);
        });

        step2.addActionListener(e -> {
            if(!answerField.getText().isEmpty()){
                boolean verify = database.verifySecurityQuestion(emailField.getText(), answerField.getText());
                if(verify){
                    JOptionPane.showMessageDialog(null, "Verification successful", "Verification Status", JOptionPane.INFORMATION_MESSAGE);
                    step2.setVisible(false);
                    passwordLabel.setVisible(true);
                    newPassword.setVisible(true);
                    confirmationLabel.setVisible(true);
                    confirmedNewPasswd.setVisible(true);
                    step3.setVisible(true);

                    layout.gridx = 0;
                    layout.gridy = 13;
                    insidePanel.add(back, layout);
                }else{
                JOptionPane.showMessageDialog(null, "Answer isn't correct", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }else JOptionPane.showMessageDialog(null, "You must answer the security question", "Error", JOptionPane.INFORMATION_MESSAGE);
        });

        step3.addActionListener(e -> {
            if(!newPassword.getText().isEmpty() && !confirmedNewPasswd.getText().isEmpty()){
                if(newPassword.getText().equals(confirmedNewPasswd.getText())){
                    boolean successful = database.resetPassword(emailField.getText(), newPassword.getText());
                    if(successful){
                        JOptionPane.showMessageDialog(null, "Password was changed successfully", "Password Status", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "Sign In");
                        refreshPanel("Forgot Password");
                    }else JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }else JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.INFORMATION_MESSAGE);
            }else JOptionPane.showMessageDialog(null, "You must create a new password", "Error", JOptionPane.INFORMATION_MESSAGE);
        });

        back.addActionListener(e -> {
            cardLayout.show(mainPanel, "Sign In");
            refreshPanel("Forgot Password");
        });

        panel.add(insidePanel);
        return panel;
    }

    private void refreshPanel(String panel) {
        mainPanel.remove(getPanel(panel));

        mainPanel.add(getPanel(panel), panel);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel getPanel(String panelName) {

        switch (panelName) {
            case "Sign In":
                return loginPanel();

            case "Sign Up":
                return signUpPanel();

            case "Forgot Password":
                return forgotPasswordPanel();

            case "Portfolio Builder":
                return portfolioBuilderPanel();

            case "Dashboard":
                return dashboardPanel();
            
            case "Analysis":
                return analysisPanel();

            case "Monte Carlo":
                return monteCarloPanel();

            case "Admin":
                return adminPanel();

            default:
                throw new IllegalArgumentException(
                    "Unknown panel: " + panelName
                );
        }
    }
}