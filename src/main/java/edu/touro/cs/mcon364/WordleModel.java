package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * Logic for a GUI Wordle game
 */
public class WordleModel {
    private final JTextField[][] CELLS;
    private final String ANSWER;
    private int numGuess;

    static final int NUM_ROWS = 6;
    static final int NUM_COLS = 5;

    public WordleModel() {
        // Initialize virtual game board
        CELLS = new JTextField[NUM_ROWS][NUM_COLS];
        ANSWER = getAnswer();
        numGuess = 1;
    }

    /**
     * Allows the view to add JTextFields to the model
     *
     * @param row       Row where the JTextField belongs
     * @param col       Column where the JTextField belongs
     * @param textField The JTextField to be added
     */
    void setCell(int row, int col, JTextField textField) {
        CELLS[row][col] = textField;
    }

    /**
     * Randomly chooses an answer from the answer bank
     *
     * @return the answer
     */
    private String getAnswer() {
        try {
            List<String> words = Files.readAllLines(Paths.get("./src/main/java/edu/touro/cs/mcon364/answers.txt"));
            int rand = new Random().nextInt(words.size());
            System.out.println(words.get(rand));
            return words.get(rand).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "SUPER";
        }
    }

    /**
     * Processes the input when the user hits enter
     *
     * @return true if the entry 100% matches, false otherwise
     */
    boolean checkInput() {
        // Arrange the submission into a char[]
        boolean[] correct = new boolean[NUM_COLS];
        for (int i = 0; i < NUM_COLS; i++) {
            JTextField cell = CELLS[numGuess - 1][i];

            int positionInAnswer = ANSWER.indexOf(Character.toUpperCase(cell.getText().charAt(0)));
            if (positionInAnswer < 0) { // character is not in the answer
                cell.setBackground(UIManager.getColor("Panel.background"));
            } else {
                boolean isCorrectPosition = positionInAnswer == i;
                if (isCorrectPosition) correct[i] = true;
                cell.setBackground(isCorrectPosition ? Color.green : Color.orange);
            }
        }

        disableRow();
        numGuess++;

        for (boolean b : correct) {
            if (!b) return false;
        }
        return true;
    }

    int getNumGuess() {
        return numGuess;
    }

    /**
     * Disables the row associated with the current guess and enables the proceeding row.
     */
    private void disableRow() {
        for (JTextField cell : CELLS[numGuess - 1]) {
            cell.setEnabled(false);
        }
        if (numGuess >= NUM_ROWS) return;
        for (JTextField cell : CELLS[numGuess]) {
            cell.setEnabled(true);
        }
    }

    /**
     * Checks if all the JTextFields in the submitted row are empty.
     *
     * @return false if there is an empty cell, else true
     */
    public boolean allFilled() {
        for (JTextField cell : CELLS[numGuess - 1]) {
            if (cell.getText().isEmpty()) return false;
        }
        return true;
    }
}
