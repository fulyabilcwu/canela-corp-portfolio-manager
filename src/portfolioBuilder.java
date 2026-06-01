import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
 
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
 
public class portfolioBuilder extends JFrame {
 
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField ageField;
	private JTextField incomeField;
	private JTextField portfolioNameField;
	private JTextField netWorthField;
	private JComboBox<String> riskComboBox;
	private JLabel lblIncome;
	private JLabel lblNetWorth;
	private JLabel lblAge;
	private JLabel lblPortfolioName;
	private JLabel lblRisk;
	private JLabel lblNewLabel;
	private int currentUserID;
 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					portfolioBuilder frame = new portfolioBuilder();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 
	public portfolioBuilder() {
		currentUserID = -1;
		buildUI();
	}
 
	public portfolioBuilder(int userID) {
		currentUserID = userID;
		buildUI();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
 

 
	private void buildUI() {
		setTitle("Portfolio Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
 
		
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
 
		
		lblNewLabel = new JLabel("BUILD YOUR PORTFOLIO");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setPreferredSize(new java.awt.Dimension(0, 100));
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
 
		
		JPanel centerWrapper = new JPanel(new GridBagLayout());
		centerWrapper.setBackground(Color.DARK_GRAY);
		contentPane.add(centerWrapper, BorderLayout.CENTER);
 
		
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.DARK_GRAY);
 
		
		centerWrapper.add(formPanel);
 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 15, 12, 15);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE; 
 
		Font labelFont = new Font("Tahoma", Font.BOLD, 75);
		java.awt.Dimension fieldSize = new java.awt.Dimension(180, 50);
 
		
		lblAge = new JLabel("Age:");
		lblAge.setForeground(Color.WHITE);
		lblAge.setFont(labelFont);
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(lblAge, gbc);
 
		ageField = new JTextField();
		ageField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		ageField.setColumns(10);
		ageField.setPreferredSize(fieldSize);
		gbc.gridx = 1; gbc.gridy = 0;
		formPanel.add(ageField, gbc);
 
		lblPortfolioName = new JLabel("Portfolio Name:");
		lblPortfolioName.setForeground(Color.WHITE);
		lblPortfolioName.setFont(labelFont);
		gbc.gridx = 2; gbc.gridy = 0;
		formPanel.add(lblPortfolioName, gbc);
 
		portfolioNameField = new JTextField();
		portfolioNameField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		portfolioNameField.setColumns(20);
		portfolioNameField.setPreferredSize(fieldSize);
		gbc.gridx = 3; gbc.gridy = 0;
		formPanel.add(portfolioNameField, gbc);
 
		
		lblIncome = new JLabel("Income:");
		lblIncome.setForeground(Color.WHITE);
		lblIncome.setFont(labelFont);
		gbc.gridx = 0; gbc.gridy = 1;
		formPanel.add(lblIncome, gbc);
 
		incomeField = new JTextField();
		incomeField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		incomeField.setColumns(20);
		incomeField.setPreferredSize(fieldSize);
		gbc.gridx = 1; gbc.gridy = 1;
		formPanel.add(incomeField, gbc);
 
		lblRisk = new JLabel("Risk Tolerance:");
		lblRisk.setForeground(Color.WHITE);
		lblRisk.setFont(labelFont);
		gbc.gridx = 2; gbc.gridy = 1;
		formPanel.add(lblRisk, gbc);
 
		riskComboBox = new JComboBox();
		riskComboBox.addItem("Low");
		riskComboBox.addItem("Medium");
		riskComboBox.addItem("High");
		riskComboBox.setMaximumRowCount(3);
		riskComboBox.setPreferredSize(fieldSize);
		riskComboBox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		gbc.gridx = 3; gbc.gridy = 1;
		formPanel.add(riskComboBox, gbc);
 
		
		lblNetWorth = new JLabel("Net Worth:");
		lblNetWorth.setForeground(Color.WHITE);
		lblNetWorth.setFont(labelFont);
		gbc.gridx = 0; gbc.gridy = 2;
		formPanel.add(lblNetWorth, gbc);
 
		netWorthField = new JTextField();
		netWorthField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		netWorthField.setColumns(20);
		netWorthField.setPreferredSize(fieldSize);
		gbc.gridx = 1; gbc.gridy = 2;
		formPanel.add(netWorthField, gbc);
 
		
		JButton generatePortfolioBtn = new JButton("Generate Portfoilio");
		generatePortfolioBtn.setBackground(new Color(255, 255, 255));
		generatePortfolioBtn.setPreferredSize(new java.awt.Dimension(250, 50));
		generatePortfolioBtn.setFont(new Font("Tahoma", Font.PLAIN, 25));
		generatePortfolioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 

				String age = ageField.getText();
				String income = incomeField.getText();
				String risk = riskComboBox.getSelectedItem().toString();
				String netWorth = netWorthField.getText();
				String portfolioName = portfolioNameField.getText();
 
				String res = "";
 
				if(risk.equals("High"))
				{
					res = portfolioName + "'s Aggressive Portfolio";
				}
				else if(risk.equals("Medium"))
				{
					res = portfolioName + "'s Balanced Portfolio";
				}
				else
				{
					res = portfolioName + "'s Conservative Portfolio";
				}
 
				
 
				DatabaseManager.generatePortfolio(
				        currentUserID,
				        res,
				        Double.parseDouble(netWorth),
				        risk);
 
				int portfolioId = DatabaseManager.getLatestPortfolioId(currentUserID);

				// Auto-populate assets based on the selected risk level so the
				// Monte Carlo and Long-Term Potential panels have data to work with.
				double totalValue = Double.parseDouble(netWorth);
				if (risk.equalsIgnoreCase("Low")) {
					DatabaseManager.addAsset(portfolioId, "STOCK", 20.0, totalValue * 0.20);
					DatabaseManager.addAsset(portfolioId, "BOND",  60.0, totalValue * 0.60);
					DatabaseManager.addAsset(portfolioId, "CASH",  20.0, totalValue * 0.20);
				} else if (risk.equalsIgnoreCase("Medium")) {
					DatabaseManager.addAsset(portfolioId, "STOCK", 60.0, totalValue * 0.60);
					DatabaseManager.addAsset(portfolioId, "BOND",  30.0, totalValue * 0.30);
					DatabaseManager.addAsset(portfolioId, "CASH",  10.0, totalValue * 0.10);
				} else { // High
					DatabaseManager.addAsset(portfolioId, "STOCK", 80.0, totalValue * 0.80);
					DatabaseManager.addAsset(portfolioId, "BOND",  15.0, totalValue * 0.15);
					DatabaseManager.addAsset(portfolioId, "CASH",   5.0, totalValue * 0.05);
				}
 
				portfolioDashboard dash = new portfolioDashboard();
				dash.setPortfolioId(portfolioId);
				dash.setPortfolioText(res);
				dash.setAgeText(age);
		        dash.setIncomeText(income);
		        dash.setNetWorthText(netWorth);
		        dash.setRiskText(risk);
				dash.setVisible(true);
			}
		});
 
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 15, 20, 15);
		formPanel.add(generatePortfolioBtn, gbc);
	}
}