# Canela's Portfolio Manager

> A personalized investment portfolio construction system built with Java and JavaFX.

---

## Team — Canela Corp.

| Name | Role |
|------|------|
| Naya Kaba | Team Member |
| Fulya Bilgin | Team Member |
| Steve Buerstatte | Team Member |
| Eli Michel | Team Member |

---

## Project Overview

Canela's Portfolio Manager is a desktop application that helps users build personalized investment portfolios based on their financial profile. Users input key information such as age, income, risk tolerance, and net worth, and the system generates a recommended asset allocation tailored to their needs.

The application also provides long-term growth projections and Monte Carlo simulations to help users understand the potential risks and returns of their portfolio over time.

---

## Features

- **User Authentication** — Register and log in with email and password. Credentials are validated against the database.
- **Portfolio Builder** — Input age, income, risk tolerance, and net worth to generate a personalized portfolio.
- **Portfolio Generation** — Automatically recommends an asset allocation based on user inputs.
- **Portfolio Management** — Save, edit, and delete portfolios. Users can adjust allocations and add assets such as stocks, bonds, cash, real estate, gold, and ETFs.
- **Portfolio Dashboard** — View all portfolios with a pie chart showing asset allocation breakdown.
- **Long-Term Analysis** — Visual graph showing projected portfolio growth over 10, 25, and 50 years.
- **Monte Carlo Simulation** — Provides estimated, worst-case, and best-case portfolio value scenarios based on market variability.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| UI Framework | Swing & awt|
| Database | MySQL |
| IDE | VS Code |
| Version Control | Git / GitHub |

---

## Database Schema

### Users Table
| Field | Type | Key |
|-------|------|-----|
| user_id | int | PK (auto_increment) |
| name | varchar(255) | |
| email | varchar(255) | UNI |
| password | varchar(255) | |
| age | int | |
| income | decimal(12,2) | |
| net_worth | decimal(15,2) | |
| created | timestamp | |

### Portfolios Table
| Field | Type | Key |
|-------|------|-----|
| portfolio_id | int | PK (auto_increment) |
| user_id | int | FK → Users |
| portfolio_name | varchar(255) | |
| total_value | decimal(15,2) | |
| risk_level | enum('LOW','MEDIUM','HIGH') | |
| created_at | timestamp | |

### Assets Table
| Field | Type | Key |
|-------|------|-----|
| asset_id | int | PK (auto_increment) |
| user_id | int | FK → Users |
| portfolio_id | int | FK → Portfolios |
| asset_type | enum('STOCK','BOND','CASH','REAL_ESTATE','GOLD','ETF') | |
| allocation_percentage | decimal(5,2) | |
| amount | decimal(15,2) | |

---

## Class Structure

```
User.java               — Handles user data and authentication logic
Portfolio.java          — Manages portfolio data and generation logic
Asset.java              — Represents individual assets within a portfolio
PortfolioAnalyzer.java  — Runs Monte Carlo and long-term growth calculations
DatabaseManager.java    — Handles all MySQL database connections and queries
Main.java               — Entry point, ties all components together
Admin.java              _ Extends User and implements admin-related functions
```

---

## Application Screens

1. **Authentication Page** — Sign in with email/password or sign up for a new account
2. **Portfolio Builder** — Enter age, income, net worth
3. **Portfolio Dashboard** — View asset allocations with a pie chart, save or delete portfolios
4. **Long-Term Potential** — Line graph projecting portfolio value at 10, 25, and 50 years
5. **Monte Carlo Simulation** — Summary box showing estimated, worst-case, and best-case values

---

## Getting Started

### Prerequisites
- Java JDK 17+
- JavaFX SDK
- MySQL Server
- VS Code with the Java Extension Pack

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/fulyabilcwu/canela-corp-portfolio-manager.git
   ```
2. Open the project folder in VS Code
3. Set up the MySQL database and run the schema scripts
4. Configure your database credentials in `DatabaseManager.java`
5. Run `Main.java` to launch the application

---

## Project Status

> Phase 2 — Design (In Progress)
