package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;

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

    void setCell(int row, int col, JTextField textField) {
        CELLS[row][col] = textField;
    }

    private String getAnswer() {
        return "SUPER";
    }

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
        for (JTextField cell : CELLS[numGuess]) {
            cell.setEnabled(true);
        }
    }
}
