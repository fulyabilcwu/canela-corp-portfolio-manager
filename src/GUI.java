import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame{
    private DatabaseManager database;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image logo = new ImageIcon(getClass().getResource("logo.png")).getImage();
    private String[] secQsList;
    private int loggedInUser = -1;

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

        add(mainPanel);
        setVisible(true);

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

            default:
                throw new IllegalArgumentException(
                    "Unknown panel: " + panelName
                );
        }
    }
}