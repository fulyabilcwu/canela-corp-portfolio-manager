import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame{
    private DatabaseManager database;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image logo = new ImageIcon(getClass().getResource("logo.png")).getImage();
    private String[] secQsList;

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
        mainPanel.add(moteCarloPanel(), "Monte Carlo");
        mainPanel.add(forgotPasswordPanel(), "Forgot Password");
        mainPanel.add(securityQuestionVerificationPanel(), "Verify Security Question");
        mainPanel.add(resetPasswordPanel(), "Reset Password");




        add(mainPanel);
        setVisible(true);

    }
    /**
     * 
     * @return
     */
    private JPanel moteCarloPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel analysisPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel dashboardPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel portfolioBuilderPanel() {
        JPanel panel = new JPanel();
        return panel;
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
        layout.gridy = 20;
        layout.insets = new Insets(25, 10, 10, 10);
        insidePanel.add(createUserAccount, layout); 
        createUserAccount.addActionListener(e -> {
            String chosenQ = (String) questionList.getSelectedItem();
            if(!fNameF.getText().isEmpty() && !lNameF.getText().isEmpty() && !passField.getText().isEmpty() && !confirmedNewPasswd.getText().isEmpty() 
                && !emailField.getText().isEmpty() && (chosenQ != "" && !answer.getText().isEmpty() || chosenQ != "" && !answer.getText().isEmpty() && !question.getText().isEmpty())){
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
                        JOptionPane.showMessageDialog(null, "Account creation successfull!", "Account Status", JOptionPane.INFORMATION_MESSAGE);
                        fNameF.setText("");
                        mNameF.setText("");
                        lNameF.setText("");
                        emailField.setText("");
                        passField.setText("");
                        confirmedNewPasswd.setText("");

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
            }
        });
        bottom.add(signIn);

        layout.gridx = 0;
        layout.gridy = 19;
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
            }
        });
        layout.gridx = 0;
        layout.gridy = 8;
        layout.anchor = GridBagConstraints.CENTER;
        layout.fill = GridBagConstraints.NONE;
        insidePanel.add(forgotPassword, layout);

        signUpButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Sign Up");
        });

        loginButton.addActionListener(e -> {
            String emailInput = emailField.getText();
            String passInput = passField.getText();
            boolean correctCredentials = database.loginUser(emailInput, passInput);
            if(correctCredentials) cardLayout.show(mainPanel, "Dashboard");
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

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("account.png"));
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


        JLabel email = new JLabel("Email");
        email.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(email, layout);

        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 1;
        layout.gridy = 4;
        insidePanel.add(emailField, layout);

        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(60, 60, 60));
        continueButton.setForeground(Color.WHITE);
        continueButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 8;
        insidePanel.add(continueButton, layout);

        continueButton.addActionListener(e -> {
            String input = emailField.getText();
            // verify from database

            cardLayout.show(mainPanel, "Verify Security Question");
        });
        panel.add(insidePanel);
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel securityQuestionVerificationPanel() {
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

        JLabel title = new JLabel("Security Question", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);

        JLabel question = new JLabel("Question");
        question.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 3;
        layout.gridwidth = 2;
        insidePanel.add(question, layout);

        /**EDIT LATER */
        // grab the chosen question associated with the user email from database and display here
        JTextField questionField = new JTextField();
        questionField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 4;
        insidePanel.add(questionField, layout);

        JLabel answer = new JLabel("Enter Your Answer");
        answer.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(answer, layout);

        JTextField answerField = new JTextField();
        answerField.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 6;
        insidePanel.add(answerField, layout);

        JButton verifyButton = new JButton("Verify");
        verifyButton.setBackground(new Color(60, 60, 60));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 8;
        insidePanel.add(verifyButton, layout);

        verifyButton.addActionListener(e -> {
            String chosenQuestion = questionField.getText();
            String input = answerField.getText();
            // verify from database

            cardLayout.show(mainPanel, "Reset Password");
        });
        panel.add(insidePanel);
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel resetPasswordPanel() {
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

        ImageIcon newAccountIcon = new ImageIcon(getClass().getResource("passwd.png"));
        Image i = newAccountIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(i));
        icon.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 2;
        insidePanel.add(icon, layout);

        JLabel title = new JLabel("Create a new Password", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        layout.gridx = 0;
        layout.gridy = 1;
        insidePanel.add(title, layout);

        JLabel passwordLabel = new JLabel("New Password");
        passwordLabel.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 3;
        insidePanel.add(passwordLabel, layout);

        JTextField newPassword = new JTextField();
        newPassword.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 4;
        layout.gridwidth = 2;
        insidePanel.add(newPassword, layout);

        JLabel confirmationLabel = new JLabel("Confirm New Password");
        confirmationLabel.setForeground(Color.white);
        layout.gridx = 0;
        layout.gridy = 5;
        insidePanel.add(confirmationLabel, layout);

        JTextField confirmedNewPasswd = new JTextField();
        confirmedNewPasswd.setPreferredSize(new Dimension(20, 30));
        layout.gridx = 0;
        layout.gridy = 6;
        insidePanel.add(confirmedNewPasswd, layout);

        JButton createPassword = new JButton("Apply New Password");
        createPassword.setBackground(new Color(60, 60, 60));
        createPassword.setForeground(Color.WHITE);
        createPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        layout.gridx = 0;
        layout.gridy = 8;
        layout.anchor = GridBagConstraints.CENTER;
        layout.fill = GridBagConstraints.NONE;
        insidePanel.add(createPassword, layout);

        createPassword.addActionListener(e -> {
            String new_password = newPassword.getText();
            // update database
            JOptionPane.showMessageDialog(null, "Password was changed successfully", "Password Status", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Sign In");
        });
        panel.add(insidePanel);
        return panel;
    }

}
