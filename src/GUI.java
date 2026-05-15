import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GUI(){
        setTitle("Canela Corop. Portfolio Manager");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "Login");
        mainPanel.add(signUpPanel(), "Sign Up");
        mainPanel.add(portfolioBuilderPanel(), "Portfolio Builder");
        mainPanel.add(dashboardPanel(), "Dashboard");
        mainPanel.add(analysisPanel(), "Analysis");
        mainPanel.add(moteCarloPanel(), "Monte Carlo");

        add(mainPanel);
        setVisible(true);

    }

    private JPanel moteCarloPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel analysisPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel dashboardPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel portfolioBuilderPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel signUpPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel();

        JLabel title = new JLabel("Login");

        JLabel username = new JLabel("Username");
        JLabel password = new JLabel("Password");
        // placeholder for forgot_your_password Label/hyperlink
        


        return panel;
    }
}
