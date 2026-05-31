-- Canela Corp Portfolio Manager
-- basic project database setup

DROP DATABASE IF EXISTS portfolioapp;
CREATE DATABASE portfolioapp;
USE portfolioapp;

-- USERS table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT,
    income DECIMAL(15,2),
    risk_tolerance VARCHAR(20),
    net_worth DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PORTFOLIOS table
CREATE TABLE Portfolios (
    portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    portfolio_name VARCHAR(255) NOT NULL,
    total_value DECIMAL(15,2),
    risk_level VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_portfolios_users
        FOREIGN KEY (user_id)
        REFERENCES Users(user_id)
        ON DELETE CASCADE
);

-- ASSETS table
CREATE TABLE Assets (
    asset_id INT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id INT NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    allocation_percentage DECIMAL(5,2),
    amount DECIMAL(15,2),

    CONSTRAINT fk_assets_portfolios
        FOREIGN KEY (portfolio_id)
        REFERENCES Portfolios(portfolio_id)
        ON DELETE CASCADE
);

-- PORTFOLIO ANALYZER table
-- This table is one-to-one with Portfolios because portfolio_id is UNIQUE.
CREATE TABLE PortfolioAnalyzer (
    analysis_id INT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id INT NOT NULL UNIQUE,
    estimated_value DECIMAL(15,2),
    projected_growth DECIMAL(15,2),
    simulation_year INT,
    best_case DECIMAL(15,2),
    worst_case DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_analyzer_portfolios
        FOREIGN KEY (portfolio_id)
        REFERENCES Portfolios(portfolio_id)
        ON DELETE CASCADE
);

-- SAMPLE DATA

INSERT INTO Users (name, email, password, age, income, risk_tolerance, net_worth)
VALUES
('John Doe', 'john@example.com', 'pass123', 25, 55000.00, 'MEDIUM', 15000.00),
('Sarah Lee', 'sarah@example.com', 'pass123', 32, 85000.00, 'HIGH', 50000.00),
('Michael Smith', 'michael@example.com', 'pass123', 45, 120000.00, 'LOW', 200000.00),
('Emily Davis', 'emily@example.com', 'pass123', 29, 70000.00, 'MEDIUM', 30000.00),
('David Brown', 'david@example.com', 'pass123', 38, 95000.00, 'HIGH', 80000.00);

INSERT INTO Portfolios (user_id, portfolio_name, total_value, risk_level)
VALUES
(1, 'Growth Portfolio', 15000.00, 'MEDIUM'),
(2, 'Aggressive Portfolio', 50000.00, 'HIGH'),
(3, 'Conservative Portfolio', 200000.00, 'LOW'),
(4, 'Balanced Portfolio', 30000.00, 'MEDIUM'),
(5, 'High Risk Portfolio', 80000.00, 'HIGH');

INSERT INTO Assets (portfolio_id, asset_type, allocation_percentage, amount)
VALUES
(1, 'STOCK', 60.00, 9000.00),
(1, 'BOND', 30.00, 4500.00),
(1, 'CASH', 10.00, 1500.00),

(2, 'STOCK', 80.00, 40000.00),
(2, 'ETF', 20.00, 10000.00),

(3, 'BOND', 70.00, 140000.00),
(3, 'CASH', 30.00, 60000.00),

(4, 'STOCK', 50.00, 15000.00),
(4, 'REAL_ESTATE', 30.00, 9000.00),
(4, 'GOLD', 20.00, 6000.00),

(5, 'STOCK', 90.00, 72000.00),
(5, 'GOLD', 10.00, 8000.00);

INSERT INTO PortfolioAnalyzer
(portfolio_id, estimated_value, projected_growth, simulation_year, best_case, worst_case)
VALUES
(1, 18000.00, 3000.00, 10, 25000.00, 10000.00),
(2, 75000.00, 25000.00, 10, 120000.00, 40000.00),
(3, 210000.00, 10000.00, 10, 250000.00, 180000.00),
(4, 35000.00, 5000.00, 10, 60000.00, 20000.00),
(5, 90000.00, 10000.00, 10, 150000.00, 30000.00);

-- BASIC TEST QUERIES

SELECT * FROM Users;
SELECT * FROM Portfolios;
SELECT * FROM Assets;
SELECT * FROM PortfolioAnalyzer;

-- View each portfolio with its user
SELECT
    u.name,
    p.portfolio_name,
    p.total_value,
    p.risk_level
FROM Users u
JOIN Portfolios p
    ON u.user_id = p.user_id;

-- View assets inside each portfolio
SELECT
    p.portfolio_name,
    a.asset_type,
    a.allocation_percentage,
    a.amount
FROM Portfolios p
JOIN Assets a
    ON p.portfolio_id = a.portfolio_id;

-- View portfolio analysis results
SELECT
    p.portfolio_name,
    pa.estimated_value,
    pa.projected_growth,
    pa.simulation_year,
    pa.best_case,
    pa.worst_case
FROM Portfolios p
JOIN PortfolioAnalyzer pa
    ON p.portfolio_id = pa.portfolio_id;
    
-- Test queries to verify database setup
USE portfolioapp;

SHOW TABLES;

SELECT * FROM USERS;
SELECT * FROM PORTFOLIOS;
SELECT * FROM ASSETS;
SELECT * FROM PortfolioAnalyzer;

