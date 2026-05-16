package portfolioBuilderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

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

	/**
	 * Launch the application.S
	 */
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

	/**
	 * Create the frame.
	 */
	public portfolioDashboard() {
		setTitle("Portfolio Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 688, 478);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 182, 193));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPortfolioName = new JLabel("New label");
		lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPortfolioName.setBackground(new Color(178, 34, 34));
		lblPortfolioName.setOpaque(true);
		lblPortfolioName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortfolioName.setBounds(0, 0, 672, 50);
		contentPane.add(lblPortfolioName);
		
		lblAge = new JLabel("New label");
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds(21, 61, 212, 14);
		contentPane.add(lblAge);
		
		lblIncome = new JLabel("New label");
		lblIncome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIncome.setBounds(21, 96, 212, 14);
		contentPane.add(lblIncome);
		
		lblRisk = new JLabel("New label");
		lblRisk.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisk.setBounds(21, 128, 212, 14);
		contentPane.add(lblRisk);
		
		lblNetWorth = new JLabel("New label");
		lblNetWorth.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNetWorth.setBounds(21, 163, 212, 14);
		contentPane.add(lblNetWorth);
		
		assetComboBox = new JComboBox();
		assetComboBox.setBounds(132, 197, 101, 22);
		contentPane.add(assetComboBox);
		
		assetComboBox.addItem("Stocks");
		assetComboBox.addItem("Bonds");
		assetComboBox.addItem("Cash");
		assetComboBox.addItem("ETF");
		assetComboBox.addItem("Gold");
		
		JLabel lblNewLabel_4 = new JLabel("Add Assets: ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds(21, 199, 101, 14);
		contentPane.add(lblNewLabel_4);
		
		pctField = new JTextField();
		pctField.setBounds(258, 198, 28, 20);
		contentPane.add(pctField);
		pctField.setColumns(10);
		
		JLabel lblPercent = new JLabel("%");
		lblPercent.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPercent.setBounds(240, 199, 28, 14);
		contentPane.add(lblPercent);
		
		JButton btnAddAsset = new JButton("Add");
		btnAddAsset.setBounds(240, 239, 51, 23);
		contentPane.add(btnAddAsset);

	}
	
	public void setPortfolioText(String text)
	{
		lblPortfolioName.setText(text);
	}
	
	public void setAgeText(String text)
	{
		lblAge.setText("Age: " + text);
	}
	
	public void setIncomeText(String text)
	{
		lblIncome.setText("Income: $" + text);
	}
	
	public void setRiskText(String text)
	{
		lblRisk.setText("Risk Tolerance: " + text);
	}
	
	public void setNetWorthText(String text)
	{
		lblNetWorth.setText("Net Worth: " + text);
	}
}
