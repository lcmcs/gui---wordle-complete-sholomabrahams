package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static edu.touro.cs.mcon364.WordleModel.NUM_COLS;
import static edu.touro.cs.mcon364.WordleModel.NUM_ROWS;

/**
 * GUI for a Wordle game
 */
public class WordleView extends JFrame {
    private final JPanel CANVAS_PANEL;
    private final WordleModel MODEL;

    /**
     * Runs the Wordle GUI based on the model
     * @param model the WordleModel where the game logic takes place
     */
    public WordleView(WordleModel model) {
        super("Wordle 2.0");
        MODEL = model;

        this.setSize(500, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        CANVAS_PANEL = new JPanel();
        CANVAS_PANEL.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 4, 4));
        this.add(CANVAS_PANEL, BorderLayout.CENTER);

        populateGameBoard();

        addEnterButton();

        this.setVisible(true);
    }

    /**
     * Adds an enter button to the frame
     */
    private void addEnterButton() {
        JButton enterButton = new JButton("Enter");
        JFrame frame = this;
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!MODEL.allFilled()) {
                    JOptionPane.showMessageDialog(frame, "You must fill out all the letters.");
                    return;
                }
                if (MODEL.checkInput()) {
                    JOptionPane.showMessageDialog(frame, "You guessed the correct answer!");
                    System.exit(0);
                }
                if (MODEL.getNumGuess() > NUM_ROWS) {
                    JOptionPane.showMessageDialog(frame, "You lost.");
                    System.exit(0);
                }
            }
        });
        this.add(enterButton, BorderLayout.SOUTH);
    }

    /**
     * Populates the GUI game board and sets the corresponding game board in the model
     */
    private void populateGameBoard() {
        // When applied, restricts the JTextField to a maximum text length of 1 character
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
                MODEL.setCell(i, j, tf);
                // Add the cell to the GUI game board
                CANVAS_PANEL.add(tf);
            }
        }
    }
}
