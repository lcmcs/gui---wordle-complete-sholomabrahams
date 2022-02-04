package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Wordle game
 */
public class Wordle extends JFrame {
    private static final String ANSWER = "SUPER";
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 5;

    /**
     * Runs the Wordle game
     */
    Wordle() {
        super("Wordle 1.2"); // must be first line

        this.setSize(500, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel canvasPanel = new JPanel();
        this.add(canvasPanel, BorderLayout.CENTER);

        canvasPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 4, 4));

        // Initialize virtual game board
        JTextField[][] cells = new JTextField[NUM_ROWS][NUM_COLS];

        KeyAdapter oneCharRestriction = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (((JTextField) e.getSource()).getText().length() >= 1) e.consume();
            }
        };

        // Populate the GUI and virtual game boards
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                JTextField tf = new JTextField();
                // Limit each cell to one character
                tf.addKeyListener(oneCharRestriction);
                // Add the cell to the virtual game board
                cells[i][j] = tf;
                // Add the cell to the GUI game board
                canvasPanel.add(tf);
            }
        }

        // Enter button
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder entry = new StringBuilder(NUM_COLS);
                for (JTextField cell : cells[0]) {
                    entry.append(cell.getText().charAt(0));
                }
                boolean correct = entry.toString().equalsIgnoreCase(ANSWER);
                for (JTextField cell : cells[0]) {
                    cell.setBackground(correct ? Color.BLUE : UIManager.getColor("Panel.background"));
                }
            }
        });
        this.add(enterButton, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
