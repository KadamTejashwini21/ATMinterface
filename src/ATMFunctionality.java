import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ATMFunctionality {
    private static int attemptCount = 0;
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ATM Menu");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel pinLabel = new JLabel("Set a 4-digit PIN:");
        JPasswordField pinField = new JPasswordField(4);
        JButton setPinButton = new JButton("Set PIN");

        pinField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (pinField.getPassword().length >= 4) {
                    e.consume();
                }
            }
        });

        frame.getContentPane().setBackground(new Color(69, 176, 234, 255)); // Light multicolor (Soft Blue)
        frame.add(pinLabel, gbc);
        gbc.gridy++;
        frame.add(pinField, gbc);
        gbc.gridy++;
        frame.add(setPinButton, gbc);

        setPinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pinInput = new String(pinField.getPassword());

                if (!pinInput.matches("\\d{4}")) { // Ensure exactly 4-digit PIN
                    JOptionPane.showMessageDialog(frame, "PIN must be exactly 4 digits.");
                    pinField.setText("");
                    return;
                }

                try {
                    int pin = Integer.parseInt(pinInput);
                    Account account = new Account(1000.0, pin);
                    JOptionPane.showMessageDialog(frame, "PIN set successfully! Now enter your PIN to proceed.");
                    openATMInterface(frame, account);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error setting PIN. Please try again.");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void openATMInterface(JFrame frame, Account account) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel enterPinLabel = new JLabel("Please enter your PIN:");
        JPasswordField enterPinField = new JPasswordField(4);
        JButton enterPinButton = new JButton("Submit PIN");

        enterPinField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (enterPinField.getPassword().length >= 4) {
                    e.consume();
                }
            }
        });

        frame.add(enterPinLabel, gbc);
        gbc.gridy++;
        frame.add(enterPinField, gbc);
        gbc.gridy++;
        frame.add(enterPinButton, gbc);

        enterPinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredPin = new String(enterPinField.getPassword());

                if (!enteredPin.matches("\\d{4}")) { // Ensure exactly 4-digit PIN
                    JOptionPane.showMessageDialog(frame, "PIN must be exactly 4 digits.");
                    enterPinField.setText("");
                    return;
                }

                try {
                    int pin = Integer.parseInt(enteredPin);
                    if (account.authenticate(pin)) {
                        JOptionPane.showMessageDialog(frame, "Authentication successful!");
                        attemptCount = 0;
                        loadATMMenu(frame, account);
                    } else {
                        attemptCount++;
                        if (attemptCount >= MAX_ATTEMPTS) {
                            JOptionPane.showMessageDialog(frame, "You have reached the daily limit of tries. Please contact your nearest bank.");
                            System.exit(0);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect PIN. Try again.");
                            enterPinField.setText("");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred. Please try again.");
                    enterPinField.setText("");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void loadATMMenu(JFrame frame, Account account) {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(7, 1)); // 7 rows to include the label

        JLabel label = new JLabel("Choose an option:");
        radioPanel.add(label); // Add label inside the panel for alignment

        JRadioButton checkBalance = new JRadioButton("Check Balance");
        JRadioButton deposit = new JRadioButton("Deposit");
        JRadioButton withdraw = new JRadioButton("Withdraw");
        JRadioButton transactionHistory = new JRadioButton("Transaction History");
        JRadioButton changePin = new JRadioButton("Change PIN");
        JRadioButton logout = new JRadioButton("Logout");

        ButtonGroup group = new ButtonGroup();
        group.add(checkBalance);
        group.add(deposit);
        group.add(withdraw);
        group.add(transactionHistory);
        group.add(changePin);
        group.add(logout);

        radioPanel.add(checkBalance);
        radioPanel.add(deposit);
        radioPanel.add(withdraw);
        radioPanel.add(transactionHistory);
        radioPanel.add(changePin);
        radioPanel.add(logout);

        frame.add(radioPanel, gbc);
        gbc.gridy++;

        JButton submitButton = new JButton("Submit");
        frame.add(submitButton, gbc);
        radioPanel.setBackground(new Color(255, 240, 230)); // Light pastel color

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!account.isAuthenticated()) {
                    JOptionPane.showMessageDialog(frame, "Please enter PIN first.");
                    return;
                }
                if (checkBalance.isSelected()) {
                    JOptionPane.showMessageDialog(frame, "Current Balance: $" + account.getBalance());
                } else if (deposit.isSelected()) {
                    String amount = JOptionPane.showInputDialog("Enter deposit amount:");
                    try {
                        account.deposit(Double.parseDouble(amount));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount.");
                    }
                } else if (withdraw.isSelected()) {
                    String amount = JOptionPane.showInputDialog("Enter withdrawal amount:");
                    try {
                        account.withdraw(Double.parseDouble(amount));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount.");
                    }
                } else if (transactionHistory.isSelected()) {
                    JOptionPane.showMessageDialog(frame, "Transaction History:\n" + account.getTransactionHistory());
                } else if (changePin.isSelected()) {
                    String newPin = JOptionPane.showInputDialog("Enter new 4-digit PIN:");
                    if (newPin.matches("\\d{4}")) {
                        account.setPin(Integer.parseInt(newPin));
                        JOptionPane.showMessageDialog(frame, "PIN changed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "New PIN must be exactly 4 digits.");
                    }
                } else if (logout.isSelected()) {
                    JOptionPane.showMessageDialog(frame, "Logging out...");
                    attemptCount = 0; // Reset the attempt count
                    openATMInterface(frame, account); // Return to the PIN entry screen
                }
            }
        });

        frame.setVisible(true);
    }
}
