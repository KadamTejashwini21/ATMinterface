public class Account {
    private double balance;
    private int pin;
    private boolean authenticated = false;
    private String transactionHistory = "";

    // Constructor to initialize account with balance and pin
    public Account(double balance, int pin) {
        this.balance = balance;
        this.pin = pin;
    }

    // Method to authenticate the entered PIN
    public boolean authenticate(int enteredPin) {
        if (this.pin == enteredPin) {
            authenticated = true;
            return true;
        }
        return false;
    }

    // Method to get the current balance
    public double getBalance() {
        return balance;
    }

    // Method to deposit money into the account
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory += "Deposited: $" + amount + "\n";
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    // Method to withdraw money from the account
    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance -= amount;
                transactionHistory += "Withdrew: $" + amount + "\n";
            } else {
                throw new IllegalArgumentException("Insufficient funds.");
            }
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
    }

    // Method to get the transaction history
    public String getTransactionHistory() {
        return transactionHistory;
    }

    // Check if the account is authenticated
    public boolean isAuthenticated() {
        return authenticated;
    }

    // Method to change the PIN
    public void setPin(int newPin) {
        if (String.valueOf(newPin).length() == 4) { // Ensure it's a 4-digit PIN
            this.pin = newPin;
        } else {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
    }
}
