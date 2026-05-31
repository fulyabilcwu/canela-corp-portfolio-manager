

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DatabaseManager.DatabaseManager;

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
	private String currentUserEmail;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public portfolioBuilder() {
		
		currentUserEmail = "john@example.com";
		
		setTitle("Portfolio Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton generatePortfolioBtn = new JButton("Generate Portfoilio");
		generatePortfolioBtn.setBackground(new Color(255, 255, 255));
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
				
				int userId = DatabaseManager.getUserIdByEmail(currentUserEmail);

				DatabaseManager.addPortfolio(
				        userId,
				        res,
				        Double.parseDouble(netWorth),
				        risk);

				int portfolioId = DatabaseManager.getLatestPortfolioId(userId);


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
		generatePortfolioBtn.setBounds(142, 227, 147, 23);
		contentPane.add(generatePortfolioBtn);
		
		ageField = new JTextField();
		ageField.setBounds(94, 41, 86, 20);
		contentPane.add(ageField);
		ageField.setColumns(10);
		
		incomeField = new JTextField();
		incomeField.setBounds(94, 104, 86, 20);
		contentPane.add(incomeField);
		incomeField.setColumns(10);
		
		portfolioNameField = new JTextField();
		portfolioNameField.setBounds(322, 41, 86, 20);
		contentPane.add(portfolioNameField);
		portfolioNameField.setColumns(10);
		
		netWorthField = new JTextField();
		netWorthField.setBounds(94, 165, 86, 20);
		contentPane.add(netWorthField);
		netWorthField.setColumns(10);
		
		lblIncome = new JLabel("Income:");
		lblIncome.setForeground(Color.WHITE);
		lblIncome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIncome.setBounds(25, 93, 86, 38);
		contentPane.add(lblIncome);
		
		lblNetWorth = new JLabel("Net Worth:");
		lblNetWorth.setForeground(Color.WHITE);
		lblNetWorth.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNetWorth.setBounds(10, 154, 86, 38);
		contentPane.add(lblNetWorth);
		
		lblAge = new JLabel("Age:");
		lblAge.setForeground(Color.WHITE);
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds(45, 30, 86, 38);
		contentPane.add(lblAge);
		
		lblPortfolioName = new JLabel("Portfolio Name:");
		lblPortfolioName.setForeground(Color.WHITE);
		lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPortfolioName.setBounds(208, 30, 114, 38);
		contentPane.add(lblPortfolioName);
		
		lblRisk = new JLabel("Risk Tolerance:");
		lblRisk.setForeground(Color.WHITE);
		lblRisk.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisk.setBounds(208, 93, 104, 38);
		contentPane.add(lblRisk);
		
		lblNewLabel = new JLabel("BUILD YOUR PORTFOLIO");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setBounds(0, 0, 434, 38);
		contentPane.add(lblNewLabel);
		
		riskComboBox = new JComboBox();
		riskComboBox.addItem("Low");
		riskComboBox.addItem("Medium");
		riskComboBox.addItem("High");
		riskComboBox.setMaximumRowCount(3);
		riskComboBox.setBounds(322, 103, 86, 22);
		contentPane.add(riskComboBox);
	}
	
	public portfolioBuilder(String email)
	{
		this();
		
	    this.currentUserEmail = email;

	    
	}
}
