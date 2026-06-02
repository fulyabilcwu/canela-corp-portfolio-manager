

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;



import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.util.List;
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
	private PiechartPanel piechart;
	
	private static final Color HEADER_COLOR = new Color(204, 88, 80);
    private static final Color BODY_COLOR   = new Color(245, 210, 205);
    private static final Color TEXT_COLOR   = new Color(40, 30, 30);

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setForeground(TEXT_COLOR);
		contentPane.setBackground(BODY_COLOR);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		piechart =
		        new PiechartPanel(assets);

		piechart.setBounds(
		        330,
		        90,
		        300,
		        300);

		piechart.setBackground(Color.WHITE);

		contentPane.add(piechart);
		
		lblPortfolioName = new JLabel("New label");
		lblPortfolioName.setForeground(TEXT_COLOR);
		lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPortfolioName.setBackground(HEADER_COLOR);
		lblPortfolioName.setOpaque(true);
		lblPortfolioName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortfolioName.setBounds(0, 0, 672, 50);
		contentPane.add(lblPortfolioName);
		
		lblAge = new JLabel("New label");
		lblAge.setForeground(TEXT_COLOR);
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds(21, 61, 212, 14);
		contentPane.add(lblAge);
		
		lblIncome = new JLabel("New label");
		lblIncome.setForeground(TEXT_COLOR);
		lblIncome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIncome.setBounds(21, 96, 212, 14);
		contentPane.add(lblIncome);
		
		lblRisk = new JLabel("New label");
		lblRisk.setForeground(TEXT_COLOR);
		lblRisk.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisk.setBounds(21, 128, 212, 14);
		contentPane.add(lblRisk);
		
		lblNetWorth = new JLabel("New label");
		lblNetWorth.setForeground(TEXT_COLOR);
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
		lblNewLabel_4.setForeground(TEXT_COLOR);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds(21, 199, 101, 14);
		contentPane.add(lblNewLabel_4);
		
		pctField = new JTextField();
		pctField.setBounds(258, 198, 28, 20);
		contentPane.add(pctField);
		pctField.setColumns(10);
		
		JLabel lblPercent = new JLabel("%");
		lblPercent.setForeground(TEXT_COLOR);
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

			            Asset asset = new Asset(0, currentPortfolioID, assetType, pct, 0);

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
		btnAddAsset.setBounds(240, 229, 70, 23);
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
		        
		        
		        piechart.repaint();
		    }
		});
		btnSaveAssets.setBounds(66, 405, 124, 23);
		contentPane.add(btnSaveAssets);

		JLabel backLink = new JLabel("\u2190 BACK TO PORTFOLIO");
		backLink.setFont(new Font("Arial", Font.BOLD, 12));
		backLink.setForeground(new Color(40, 30, 30));
		backLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		backLink.setBounds(15, 17, 200, 20);
		backLink.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        if (GUI.onBackToBuilder != null) {
		            GUI.onBackToBuilder.run();
		        } else {
		            dispose();
		        }
		    }
		});
		contentPane.add(backLink);
		contentPane.setComponentZOrder(backLink, 0);

		JButton btnLongTerm = new JButton("Long Term Potential");
		btnLongTerm.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (GUI.onShowLongTerm != null) {
		            GUI.onShowLongTerm.run();
		        } else {
		            // Fallback: open in new window
		            JFrame longTermFrame = new JFrame("Long Term Potential");
		            longTermFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            longTermFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		            LongTermPotentialPanel panel = new LongTermPotentialPanel();
		            panel.setOnBack(() -> longTermFrame.dispose());
		            longTermFrame.add(panel);
		            longTermFrame.setVisible(true);
		        }
		    }
		});
		btnLongTerm.setBounds(400, 405, 170, 23);
		contentPane.add(btnLongTerm);
		
		JButton btnMonteCarlo = new JButton("Monte Carlo Simulation");
		btnMonteCarlo.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (GUI.onShowMonteCarlo != null) {
		            GUI.onShowMonteCarlo.run();
		        } else {
		            // Fallback: open in new window
		            JFrame monteCarloFrame = new JFrame("Monte Carlo Simulation");
		            monteCarloFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            monteCarloFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		            MonteCarloPanel panel = new MonteCarloPanel();
		            panel.setOnBack(() -> monteCarloFrame.dispose());
		            monteCarloFrame.add(panel);
		            monteCarloFrame.setVisible(true);
		        }
		    }
		});
		btnMonteCarlo.setBounds(400, 435, 200, 23);
		contentPane.add(btnMonteCarlo);
		
		JButton btnViewAllPortfolios = new JButton("View All Portfolios");
		btnViewAllPortfolios.setVisible(false);
		btnViewAllPortfolios.setBounds(350, 50, 160, 23);
		btnViewAllPortfolios.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        List<Portfolio> portfolios = DatabaseManager.getAllPortfolios();
		        StringBuilder sb = new StringBuilder();
		        for (Portfolio p : portfolios) {
		            sb.append("[" + p.getPortfolio_ID() + "] " + p.getPortfolioName()
		                + " | Value: $" + p.getTotalValue()
		                + " | Risk: " + p.getRiskLevel() + "\n");
		        }
		        JTextArea textArea = new JTextArea(sb.toString());
		        textArea.setEditable(false);
		        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "All Portfolios", JOptionPane.PLAIN_MESSAGE);
		    }
		});
		contentPane.add(btnViewAllPortfolios);

		JButton btnViewAllUsers = new JButton("View All Users");
		btnViewAllUsers.setVisible(false);
		btnViewAllUsers.setBounds(520, 50, 140, 23);
		btnViewAllUsers.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        List<User> users = DatabaseManager.getAllUsers();
		        StringBuilder sb = new StringBuilder();
		        for (User u : users) {
		            sb.append("[" + u.getUser_ID() + "] " + u.getName()
		                + " | Email: " + u.getEmail() + "\n");
		        }
		        JTextArea textArea = new JTextArea(sb.toString());
		        textArea.setEditable(false);
		        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "All Users", JOptionPane.PLAIN_MESSAGE);
		    }
		});
		contentPane.add(btnViewAllUsers);
		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.setBackground(Color.WHITE);
		btnAdmin.setForeground(TEXT_COLOR);
		btnAdmin.setBounds(530, 15, 110, 23);
		btnAdmin.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String input = JOptionPane.showInputDialog(null, "Enter admin password:");
		        if (input != null && input.equals("CANELA")) {
		            btnViewAllPortfolios.setVisible(true);
		            btnViewAllUsers.setVisible(true);
		            JOptionPane.showMessageDialog(null, "Admin access granted!");
		        } else {
		            JOptionPane.showMessageDialog(null, "Incorrect password.");
		        }
		    }
		});
		contentPane.add(btnAdmin);

		
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
	    // Load existing assets from the database so pie chart and asset
	    // list reflect what was saved for this portfolio.
	    assets.clear();
	    if (assetArea != null) assetArea.setText("");
	    java.util.List<Asset> existing = DatabaseManager.getAssetsByPortfolioId(id);
	    if (existing != null) {
	        for (Asset a : existing) {
	            assets.add(a);
	            if (assetArea != null) {
	                assetArea.append(a.getAssetType() + " - " + a.getAllocationPercentage() + "%\n");
	            }
	        }
	    }
	    if (piechart != null) piechart.repaint();
	}
}


