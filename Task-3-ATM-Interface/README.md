# OASIS INFOBYTE — ATM Interface

**Internship:** Java Development · **Task:** 3 — ATM Interface  
**Application:** Java Swing desktop application with object-oriented business logic.

This is the graphical desktop edition requested as a usability upgrade. It retains the five core OOP classes and the banking operations from the console task.

## UI direction
Inspired by the supplied N26 banking reference:
- Warm off-white workspace, clean white cards and restrained borders
- Deep teal accent `#088177` and dark ink typography
- Sidebar navigation and compact account overview
- Clear balance panel, quick-action cards and transaction table

## Features
- User ID + PIN login with lockout after three failed attempts
- Account overview with current balance and account number
- Withdraw cash, deposit funds and transfer between demo accounts
- Confirmation before transactions
- Insufficient-funds and invalid-amount validation
- Transaction history with type, description, date and amount
- Recent activity preview
- Logout and return to sign-in

## Required OOP classes
- `Main` — application entry point
- `ATM` — transaction business rules
- `Account` — account data, balance and transaction list
- `Bank` — account registry, authentication and transfers
- `Transaction` — individual transaction record

Additional UI classes (`LoginFrame`, `DashboardFrame`, `Ui`) keep the presentation separate from business logic.

## Requirements
- JDK 17 or newer
- No third-party libraries
- No Maven installation required

## Run in IntelliJ IDEA
1. Extract the ZIP.
2. Open the `Java Development-Task 3-ATM Interface` folder in IntelliJ IDEA.
3. Set Project SDK to JDK 17 or newer.
4. Open `src/main/java/com/oasis/atm/Main.java`.
5. Click the green Run icon beside `main()`.

## Demo accounts
| User ID | PIN | Account ID | Opening balance |
|---|---|---|---:|
| `ram` | `1234` | `10010001` | ₹25,000.00 |
| `sita` | `4321` | `10010002` | ₹18,000.00 |
| `arjun` | `2468` | `10010003` | ₹12,000.00 |

Use another demo account's account ID when testing transfers.

## Important
This is an educational simulation only. It is not connected to a real bank or payment network. Demo balances and transactions are stored in memory and reset when the application closes. Never enter real banking credentials.

## Submission checklist
Keep this folder under your public GitHub repository named `OIBSIP`, using the folder name `Java Development-Task 3-ATM Interface`. Capture genuine screenshots of login, dashboard, deposit, withdrawal, transfer and transaction history before submitting.


## Latest dashboard refinements
- Indian currency formatting with grouped thousands and a show/hide balance control
- White, centered empty state for a new account's activity panel
- Tinted icon badges, hover border and hand cursor on quick-action cards
- Soft teal active navigation state
- PIN re-verification before withdrawals and transfers
- Quick amount chips on deposit and withdrawal screens
- Green credit / red debit amount rendering in transaction tables
- Receipt export in TXT or PDF format after a completed transaction
- Daily withdrawal limit indicator (₹20,000 per day) with remaining amount and progress bar
