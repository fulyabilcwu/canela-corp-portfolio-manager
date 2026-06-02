import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image logo = new ImageIcon(getClass().getResource("logo.png")).getImage();
    private String[] secQsList = {"What was the name of your first pet?",
            "What is your mom's name?",
            "What is your favorite hobby?",
            "What city are you from?",
            "What is your favorite food?",
            "What was the of your first partner/spouse?",
            "What was the name of your first bestfriend?",
            "What is your favorite sport?"
        };

    public GUI(){
        setTitle("Canela Corop. Portfolio Manager");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(logo);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "Login");
        mainPanel.add(signUpPanel(), "Sign Up");
        mainPanel.add(portfolioBuilderPanel(), "Portfolio Builder");
        mainPanel.add(dashboardPanel(), "Dashboard");
        mainPanel.add(analysisPanel(), "Analysis");
        mainPanel.add(moteCarloPanel(), "Monte Carlo");
        mainPanel.add(forgotPasswordPanel(), "Forgot Password");
        mainPanel.add(securityQuestionPanel(), "Security Question");
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

    /**
     * 
     * @return
     */
    private JPanel signUpPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(97, 128, 168));

        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Sign-Up");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        layout.gridx = 1;
        layout.gridy = 0;
        panel.add(title, layout);


        JLabel fName = new JLabel("First Name");
        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(fName, layout);

        JTextField fNameF = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 2;
        panel.add(fNameF, layout);

        JLabel mName = new JLabel("Middle Name");
        layout.gridx = 0;
        layout.gridy = 3;
        panel.add(mName, layout);

        JTextField mNameF = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 3;
        panel.add(mNameF, layout);

        JLabel lName = new JLabel("Last Name");
        layout.gridx = 0;
        layout.gridy = 4;
        panel.add(lName, layout);

        JTextField lNameF = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 4;
        panel.add(lNameF, layout);

        JLabel email = new JLabel("Email");
        layout.gridx = 0;
        layout.gridy = 5;
        panel.add(email, layout);

        JTextField emailField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 5;
        panel.add(emailField, layout);

        JLabel password = new JLabel("Password");
        layout.gridx = 0;
        layout.gridy = 6;
        panel.add(password, layout);

        JTextField passField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 6;
        panel.add(passField, layout);

        JLabel confirmationLabel = new JLabel("Confirm New Password");
        layout.gridx = 0;
        layout.gridy = 7;
        panel.add(confirmationLabel, layout);

        JTextField confirmedNewPasswd = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 7;
        panel.add(confirmedNewPasswd, layout);

        JButton createUserAccount = new JButton("Create Account");
            layout.gridx = 1;
            layout.gridy = 9;
            panel.add(createUserAccount, layout);
            String name = fNameF.getText() +" "+ mNameF.getText() +" "+ lNameF.getText();
            createUserAccount.addActionListener(e -> {
            User user = new User(0, name, emailField.getText(), passField.getText());
            // add user info to database
        });
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel loginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(97, 128, 168));

        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        layout.gridx = 1;
        layout.gridy = 0;
        panel.add(title, layout);

        JLabel email = new JLabel("Email");
        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(email, layout);

        JTextField emailField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 2;
        panel.add(emailField, layout);

        JLabel password = new JLabel("Password");
        layout.gridx = 0;
        layout.gridy = 4;
        panel.add(password, layout);  

        JTextField passField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 4;
        panel.add(passField, layout);

        JButton loginButton = new JButton("Login");
        layout.gridx = 1;
        layout.gridy = 7;
        panel.add(loginButton, layout);

        JButton signUpButton = new JButton("Sign Up");
        layout.gridx = 1;
        layout.gridy = 8;
        panel.add(signUpButton, layout);

        JLabel forgotPassword = new JLabel("<html><a href = ''>Forgot your password?</a></html>");
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                cardLayout.show(mainPanel, "Forgot Password");
            }
        });
        layout.gridx = 1;
        layout.gridy = 6;
        panel.add(forgotPassword, layout);

        signUpButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Sign Up");
        });
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel forgotPasswordPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(97, 128, 168));

        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Reset Password");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        layout.gridx = 1;
        layout.gridy = 0;
        panel.add(title, layout);

        JLabel email = new JLabel("Email");
        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(email, layout);

        JTextField emailField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 2;
        panel.add(emailField, layout);

        JButton continueButton = new JButton("Continue");
        layout.gridx = 1;
        layout.gridy = 7;
        panel.add(continueButton, layout);

        continueButton.addActionListener(e -> {
            String input = emailField.getText();
            // verify from database

            cardLayout.show(mainPanel, "Security Question");
        });

        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel securityQuestionPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(97, 128, 168));

        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("Security Verification");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        layout.gridx = 1;
        layout.gridy = 0;
        panel.add(title, layout);

        JLabel question = new JLabel("Question");
        layout.gridx = 0;
        layout.gridy = 1;
        panel.add(question, layout);

        /**EDIT LATER */
        // grab the chosen question associated with the user email from database and display here
        JTextField questionField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 1;
        panel.add(questionField, layout);

        JLabel answer = new JLabel("Enter Your Answer");
        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(answer, layout);

        JTextField answerField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 2;
        panel.add(answerField, layout);

        JButton verifyButton = new JButton("Verify");
        layout.gridx = 1;
        layout.gridy = 7;
        panel.add(verifyButton, layout);

        verifyButton.addActionListener(e -> {
            String chosenQuestion = questionField.getText();
            String input = answerField.getText();
            // verify from database

            cardLayout.show(mainPanel, "Reset Password");
        });
        
        return panel;
    }

    /**
     * 
     * @return
     */
    private JPanel resetPasswordPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(97, 128, 168));

        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("Create A New Password");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        layout.gridx = 1;
        layout.gridy = 0;
        panel.add(title, layout);

        JLabel newPasswdLabel = new JLabel("New Password");
        layout.gridx = 0;
        layout.gridy = 1;
        panel.add(newPasswdLabel, layout);

        JTextField newPassword = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 1;
        panel.add(newPassword, layout);

        JLabel confirmationLabel = new JLabel("Confirm New Password");
        layout.gridx = 0;
        layout.gridy = 2;
        panel.add(confirmationLabel, layout);

        JTextField confirmedNewPasswd = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 2;
        panel.add(confirmedNewPasswd, layout);

        JButton createPassword = new JButton("Apply New Password");
        layout.gridx = 1;
        layout.gridy = 7;
        panel.add(createPassword, layout);

        createPassword.addActionListener(e -> {
            String new_password = newPassword.getText();
            // verify from database

            cardLayout.show(mainPanel, "Reset Password");
        });

        return panel;
    }

}
