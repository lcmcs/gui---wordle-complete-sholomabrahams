package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static edu.touro.cs.mcon364.WordleModel.NUM_COLS;
import static edu.touro.cs.mcon364.WordleModel.NUM_ROWS;

public class WordleView extends JFrame {

    public WordleView(WordleModel model) {
        super("Wordle 2.0");

        this.setSize(500, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel canvasPanel = new JPanel();
        this.add(canvasPanel, BorderLayout.CENTER);

        canvasPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 4, 4));

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
                if (i > 0) tf.setEnabled(false);
                tf.setDisabledTextColor(Color.black);
                // Add the cell to the virtual game board
                model.setCell(i, j, tf);
                // Add the cell to the GUI game board
                canvasPanel.add(tf);
            }
        }

        JButton enterButton = new JButton("Enter");
        JFrame frame = this;
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.checkInput()) {
                    JOptionPane.showMessageDialog(frame, "You guessed the correct answer!");
                    System.exit(0);
                }
                if (model.getNumGuess() > NUM_ROWS) {
                    JOptionPane.showMessageDialog(frame, "You lost.");
                    System.exit(0);
                }
            }
        });
        this.add(enterButton, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}
