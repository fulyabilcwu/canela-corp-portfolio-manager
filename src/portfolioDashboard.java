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

	
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int W = screen.width;
		int H = screen.height;

		
		piechart = new PiechartPanel(assets);
		piechart.setBounds(
				(int)(W * 0.37),   // x
				(int)(H * 0.12),   // y
				(int)(W * 0.28),   // width
				(int)(H * 0.38)    // height
		);
		piechart.setBackground(Color.WHITE);
		contentPane.add(piechart);

		
		lblPortfolioName = new JLabel("New label");
		lblPortfolioName.setForeground(TEXT_COLOR);
		lblPortfolioName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPortfolioName.setBackground(HEADER_COLOR);
		lblPortfolioName.setOpaque(true);
		lblPortfolioName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortfolioName.setBounds(0, 0, (int)(W), (int)(H * 0.07));
		contentPane.add(lblPortfolioName);

		
		lblAge = new JLabel("New label");
		lblAge.setForeground(TEXT_COLOR);
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds((int)(W * 0.03), (int)(H * 0.10), (int)(W * 0.22), 20);
		contentPane.add(lblAge);

		lblIncome = new JLabel("New label");
		lblIncome.setForeground(TEXT_COLOR);
		lblIncome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIncome.setBounds((int)(W * 0.03), (int)(H * 0.16), (int)(W * 0.22), 20);
		contentPane.add(lblIncome);

		lblRisk = new JLabel("New label");
		lblRisk.setForeground(TEXT_COLOR);
		lblRisk.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRisk.setBounds((int)(W * 0.03), (int)(H * 0.22), (int)(W * 0.22), 20);
		contentPane.add(lblRisk);

		lblNetWorth = new JLabel("New label");
		lblNetWorth.setForeground(TEXT_COLOR);
		lblNetWorth.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNetWorth.setBounds((int)(W * 0.03), (int)(H * 0.28), (int)(W * 0.22), 20);
		contentPane.add(lblNetWorth);

		
		JLabel lblNewLabel_4 = new JLabel("Add Assets: ");
		lblNewLabel_4.setForeground(TEXT_COLOR);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds((int)(W * 0.03), (int)(H * 0.36), (int)(W * 0.09), 25);
		contentPane.add(lblNewLabel_4);

		assetComboBox = new JComboBox<>();
		assetComboBox.addItem("Stocks");
		assetComboBox.addItem("Bonds");
		assetComboBox.addItem("Cash");
		assetComboBox.addItem("ETF");
		assetComboBox.addItem("Gold");
		assetComboBox.setBounds((int)(W * 0.13), (int)(H * 0.36), (int)(W * 0.10), 25);
		contentPane.add(assetComboBox);

		JLabel lblPercent = new JLabel("%");
		lblPercent.setForeground(TEXT_COLOR);
		lblPercent.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPercent.setBounds((int)(W * 0.265), (int)(H * 0.36), 20, 25);
		contentPane.add(lblPercent);

		pctField = new JTextField();
		pctField.setColumns(10);
		pctField.setBounds((int)(W * 0.24), (int)(H * 0.36), (int)(W * 0.04), 25);
		contentPane.add(pctField);

		JButton btnAddAsset = new JButton("Add");
		btnAddAsset.setBounds((int)(W * 0.29), (int)(H * 0.36), (int)(W * 0.06), 25);
		btnAddAsset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String assetType = assetComboBox.getSelectedItem().toString();
					int pct = Integer.parseInt(pctField.getText().trim());

					if (pct <= 0) {
						JOptionPane.showMessageDialog(
								portfolioDashboard.this,
								"Percentage must be greater than 0.",
								"Invalid Percentage",
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (getTotalPct() + pct > 100) {
						JOptionPane.showMessageDialog(
								portfolioDashboard.this,
								"Total allocation cannot exceed 100%. You have " + (100 - getTotalPct()) + "% remaining.",
								"Allocation Exceeded",
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					Asset asset = new Asset(0, currentPortfolioID, assetType, assetType, pct, 0);
					assets.add(asset);
					piechart.repaint();
					assetArea.append(assetType + " - " + pct + "%\n");
					pctField.setText("");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(
							portfolioDashboard.this,
							"Please enter a valid whole number for the percentage.",
							"Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnAddAsset);

		// ── Asset scroll area ───────────────────────────────────────────────
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int)(W * 0.03), (int)(H * 0.44), (int)(W * 0.28), (int)(H * 0.20));
		contentPane.add(scrollPane);

		assetArea = new JTextArea();
		assetArea.setEditable(false);
		scrollPane.setViewportView(assetArea);

		// ── Save Assets button ──────────────────────────────────────────────
		JButton btnSaveAssets = new JButton("Save Assets");
		btnSaveAssets.setBounds((int)(W * 0.08), (int)(H * 0.67), (int)(W * 0.13), 30);
		btnSaveAssets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Asset asset : assets) {
					DatabaseManager.addAsset(
							currentPortfolioID,
							asset.getAssetType(),
							(double) asset.getAllocationPercentage(),
							(double) asset.getAmount());
				}
				JOptionPane.showMessageDialog(portfolioDashboard.this, "Assets saved successfully!");
				piechart.repaint();
			}
		});
		contentPane.add(btnSaveAssets);

		
		JButton btnLongTerm = new JButton("Long Term Potential");
		btnLongTerm.setBounds((int)(W * 0.55), (int)(H * 0.67), (int)(W * 0.18), 30);
		btnLongTerm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GUI.onShowLongTerm != null) {
					GUI.onShowLongTerm.run();
				} else {
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
		contentPane.add(btnLongTerm);

		
		JButton btnMonteCarlo = new JButton("Monte Carlo Simulation");
		btnMonteCarlo.setBounds((int)(W * 0.55), (int)(H * 0.74), (int)(W * 0.20), 30);
		btnMonteCarlo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GUI.onShowMonteCarlo != null) {
					GUI.onShowMonteCarlo.run();
				} else {
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
		contentPane.add(btnMonteCarlo);

		
		JButton btnViewAllPortfolios = new JButton("View All Portfolios");
		btnViewAllPortfolios.setVisible(false);
		btnViewAllPortfolios.setBounds((int)(W * 0.50), (int)(H * 0.08), (int)(W * 0.15), 25);
		btnViewAllPortfolios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Portfolio> portfolios = DatabaseManager.getAllPortfolios();
				StringBuilder sb = new StringBuilder();
				for (Portfolio p : portfolios) {
					sb.append("[").append(p.getPortfolio_ID()).append("] ")
					  .append(p.getPortfolioName())
					  .append(" | Value: $").append(p.getTotalValue())
					  .append(" | Risk: ").append(p.getRiskLevel()).append("\n");
				}
				JTextArea textArea = new JTextArea(sb.toString());
				textArea.setEditable(false);
				JOptionPane.showMessageDialog(portfolioDashboard.this,
						new JScrollPane(textArea), "All Portfolios", JOptionPane.PLAIN_MESSAGE);
			}
		});
		contentPane.add(btnViewAllPortfolios);

		JButton btnViewAllUsers = new JButton("View All Users");
		btnViewAllUsers.setVisible(false);
		btnViewAllUsers.setBounds((int)(W * 0.67), (int)(H * 0.08), (int)(W * 0.13), 25);
		btnViewAllUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<User> users = DatabaseManager.getAllUsers();
				StringBuilder sb = new StringBuilder();
				for (User u : users) {
					sb.append("[").append(u.getUser_ID()).append("] ")
					  .append(u.getName())
					  .append(" | Email: ").append(u.getEmail()).append("\n");
				}
				JTextArea textArea = new JTextArea(sb.toString());
				textArea.setEditable(false);
				JOptionPane.showMessageDialog(portfolioDashboard.this,
						new JScrollPane(textArea), "All Users", JOptionPane.PLAIN_MESSAGE);
			}
		});
		contentPane.add(btnViewAllUsers);

		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.setBackground(Color.WHITE);
		btnAdmin.setForeground(TEXT_COLOR);
		btnAdmin.setBounds((int)(W * 0.78), (int)(H * 0.02), (int)(W * 0.10), 30);
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(portfolioDashboard.this, "Enter admin password:");
				if (input != null && input.equals("CANELA")) {
					btnViewAllPortfolios.setVisible(true);
					btnViewAllUsers.setVisible(true);
					JOptionPane.showMessageDialog(portfolioDashboard.this, "Admin access granted!");
				} else if (input != null) {
					JOptionPane.showMessageDialog(portfolioDashboard.this, "Incorrect password.",
							"Access Denied", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnAdmin);

		
		JLabel backLink = new JLabel("\u2190 BACK TO PORTFOLIO");
		backLink.setFont(new Font("Arial", Font.BOLD, 12));
		backLink.setForeground(new Color(40, 30, 30));
		backLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		backLink.setBounds((int)(W * 0.02), (int)(H * 0.025), (int)(W * 0.18), 20);
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
	}

	

	private int getTotalPct() {
		int total = 0;
		for (Asset asset : assets) {
			total += asset.getAllocationPercentage();
		}
		return total;
	}

	public void setPortfolioText(String text) {
		lblPortfolioName.setText(text);
	}

	public void setAgeText(String text) {
		lblAge.setText("Age: " + text);
	}

	public void setIncomeText(String text) {
		lblIncome.setText("Income: $" + text);
	}

	public void setRiskText(String text) {
		lblRisk.setText("Risk Tolerance: " + text);
	}

	public void setNetWorthText(String text) {
		lblNetWorth.setText("Net Worth: " + text);
	}

	public void setPortfolioId(int id) {
		currentPortfolioID = id;
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