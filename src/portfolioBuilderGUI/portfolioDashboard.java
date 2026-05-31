package portfolioBuilderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import DatabaseManager.DatabaseManager;


import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private int currentPortfolioID;
	private JTextArea assetArea;
	private ArrayList<Asset> assets;
	private PieChartPanel piechart;

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
		
		assets = new ArrayList<>();
		setTitle("Portfolio Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 688, 478);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		piechart =
		        new PieChartPanel(assets);

		piechart.setBounds(
		        330,
		        90,
		        300,
		        300);

		piechart.setBackground(Color.WHITE);

		contentPane.add(piechart);
		
		lblPortfolioName = new JLabel("New label");
		lblPortfolioName.setForeground(Color.WHITE);
		lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPortfolioName.setBackground(Color.BLACK);
		lblPortfolioName.setOpaque(true);
		lblPortfolioName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortfolioName.setBounds(0, 0, 672, 50);
		contentPane.add(lblPortfolioName);
		
		lblAge = new JLabel("New label");
		lblAge.setForeground(Color.WHITE);
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds(21, 61, 212, 14);
		contentPane.add(lblAge);
		
		lblIncome = new JLabel("New label");
		lblIncome.setForeground(Color.WHITE);
		lblIncome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIncome.setBounds(21, 96, 212, 14);
		contentPane.add(lblIncome);
		
		lblRisk = new JLabel("New label");
		lblRisk.setForeground(Color.WHITE);
		lblRisk.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisk.setBounds(21, 128, 212, 14);
		contentPane.add(lblRisk);
		
		lblNetWorth = new JLabel("New label");
		lblNetWorth.setForeground(Color.WHITE);
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
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds(21, 199, 101, 14);
		contentPane.add(lblNewLabel_4);
		
		pctField = new JTextField();
		pctField.setBounds(258, 198, 28, 20);
		contentPane.add(pctField);
		pctField.setColumns(10);
		
		JLabel lblPercent = new JLabel("%");
		lblPercent.setForeground(Color.WHITE);
		lblPercent.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPercent.setBounds(240, 199, 28, 14);
		contentPane.add(lblPercent);
		
		JButton btnAddAsset = new JButton("Add");
		btnAddAsset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 try
			        {
			            String assetType = assetComboBox.getSelectedItem().toString();

			            int pct = Integer.parseInt(pctField.getText());

			            if(pct <= 0)
			            {
			                JOptionPane.showMessageDialog(null, "Percentage must be greater than 0.");

			                return;
			            }

			            if(getTotalPct() + pct > 100)
			            {
			                JOptionPane.showMessageDialog(null,"Total allocation cannot exceed 100%.");

			                return;
			            }

			            Asset asset = new Asset(0, currentPortfolioID, assetType, assetType, pct, 0);

			            assets.add(asset);
			            piechart.repaint();

			            assetArea.append( assetType + " - " + pct + "%\n");

			            pctField.setText("");
			        }
			        catch(NumberFormatException ex)
			        {
			            JOptionPane.showMessageDialog(null, "Please enter a valid percentage.");
			        }
			}
		});
		btnAddAsset.setBounds(234, 229, 70, 23);
		contentPane.add(btnAddAsset);
		
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds( 26, 243,  207, 161);

		contentPane.add(scrollPane);

		assetArea = new JTextArea();

		assetArea.setEditable(false);

		scrollPane.setViewportView(assetArea);
		
		JButton btnSaveAssets = new JButton("Save Assets");
		btnSaveAssets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for(Asset asset : assets)
		        {
		            DatabaseManager.addAsset(
		                    currentPortfolioID,
		                    asset.getAssetType(),
		                    (double)asset.getAllocationPercentage(),
		                    (double)asset.getAmount());
		        }

		        JOptionPane.showMessageDialog(null, "Assets saved successfully!");
		        assets.clear();
		        assetArea.setText("");
		        
		        piechart.repaint();
		    }
		});
		btnSaveAssets.setBounds(66, 405, 124, 23);
		contentPane.add(btnSaveAssets);

	}
	
	private int getTotalPct()
	{
	    int total = 0;

	    for(Asset asset : assets)
	    {
	        total += asset.getAllocationPercentage();
	    }

	    return total;
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
	
	public void setPortfolioId(int id)
	{
	    currentPortfolioID = id;
	}
}
