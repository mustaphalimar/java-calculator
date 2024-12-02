import java.awt.event.*;
import java.awt.*;
import java.util.concurrent.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener {
    private double a, b;
    private int operator;
    private final ExecutorService executor;
    private final JButton[] numberButtons = new JButton[10];
    private final JButton bAdd, bSub, bMul, bDiv, bEq, bDec, bClear;
    private final JTextField tf;

    public Main() {
        executor = Executors.newSingleThreadExecutor();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CALCULATOR WITH THREADS");
        setSize(400, 500);
        setLayout(null);

        tf = new JTextField("0");
        tf.setFont(new Font("Arial", Font.BOLD, 30));
        tf.setBounds(20, 20, 270, 50);
        tf.setEditable(false);
        add(tf);

        // Number Buttons
        int x = 20, y = 80;
        for (int i = 1; i <= 9; i++) {
            numberButtons[i] = createButton(String.valueOf(i), x, y, 60, 60);
            add(numberButtons[i]);
            x += 70;
            if (i % 3 == 0) {
                x = 20;
                y += 70;
            }
        }
        numberButtons[0] = createButton("0", 20, y, 60, 60);
        add(numberButtons[0]);

        // Decimal Button
        bDec = createButton(".", 90, y, 60, 60);
        add(bDec);

        // Operator Buttons
        bAdd = createButton("+", 230, 80, 60, 60);
        bSub = createButton("-", 230, 150, 60, 60);
        bMul = createButton("x", 230, 220, 60, 60);
        bDiv = createButton("/", 230, 290, 60, 60);
        bEq = createButton("=", 160, 290, 60, 60);
        bClear = createButton("C", 20, 360, 270, 60);

        add(bAdd);
        add(bSub);
        add(bMul);
        add(bDiv);
        add(bEq);
        add(bClear);

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                if(tf.getText().equals("0")){
                    tf.setText("");
                }
                tf.setText(tf.getText() + i);
                return;
            }
        }

        if (e.getSource() == bDec) {
            tf.setText(tf.getText() + ".");
        } else if (e.getSource() == bAdd) {
            prepareOperation(1);
        } else if (e.getSource() == bSub) {
            prepareOperation(2);
        } else if (e.getSource() == bMul) {
            prepareOperation(3);
        } else if (e.getSource() == bDiv) {
            prepareOperation(4);
        } else if (e.getSource() == bEq) {
            calculateResult();
        } else if (e.getSource() == bClear) {
            tf.setText("0");
            a = b = 0;
            operator = 0;
        }
    }

    private void prepareOperation(int op) {
        a = Double.parseDouble(tf.getText());
        operator = op;
        tf.setText("");
    }

    private void calculateResult() {
        try {
            b = Double.parseDouble(tf.getText());
            executor.submit(() -> {
                double result = 0;
                switch (operator) {
                    case 1: result = a + b; break;
                    case 2: result = a - b; break;
                    case 3: result = a * b; break;
                    case 4: result = b != 0 ? a / b : Double.NaN; break;
                }
                final double finalResult = result;
                SwingUtilities.invokeLater(() -> tf.setText(String.valueOf(finalResult)));
            });
        } catch (NumberFormatException ex) {
            tf.setText("Error");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
