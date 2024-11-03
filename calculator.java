import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class calculator extends JFrame implements ActionListener {
    private JTextField display;
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public calculator() {
        setTitle("Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Create display
        display = new JTextField();
        display.setFont(new Font("Times New Roman", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 10, 10)); // Reduced gaps between buttons
        buttonPanel.setBackground(Color.BLACK);

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                ".", "0", "=", "+",
                "√", "C", "←", "▲"
        };

        for (String label : buttonLabels) {
            JButton button = createButton(label);
            buttonPanel.add(button);
        }

        // Add components to frame
        setLayout(new BorderLayout());
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size for better visibility
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(80, 80)); // Increased button size for better usability

        // Create rounded button edges
        button.setBorder(new RoundedBorder(20)); // Increased border radius

        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("0123456789".contains(command)) {
            if (startNewNumber) {
                display.setText(command);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        } else if (command.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
                startNewNumber = false;
            }
        } else if (command.equals("C")) {
            display.setText("");
            firstNumber = 0;
            secondNumber = 0;
            operator = "";
            startNewNumber = true;
        } else if (command.equals("←")) { // Backspace functionality
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (command.equals("=")) {
            if (!operator.isEmpty()) {
                secondNumber = Double.parseDouble(display.getText());
                double result = calculate(firstNumber, secondNumber, operator);
                display.setText(String.valueOf(result));
                operator = "";
                startNewNumber = true;
            }
        } else if (command.equals("√")) {
            double number = Double.parseDouble(display.getText());
            if (number >= 0) {
                display.setText(String.valueOf(Math.sqrt(number)));
                startNewNumber = true;
            } else {
                JOptionPane.showMessageDialog(this, "Error: Cannot calculate square root of a negative number.");
            }
        } else if (command.equals("▲")) { // Shutdown functionality
            dispose(); // Close the application
        } else {
            firstNumber = Double.parseDouble(display.getText());
            operator = command;
            startNewNumber = true;
        }
    }

    private double calculate(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Division by zero is not allowed.");
                    return 0;
                }
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new calculator()); // Use invokeLater for thread safety
    }

    // Custom border class for rounded button edges
    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
