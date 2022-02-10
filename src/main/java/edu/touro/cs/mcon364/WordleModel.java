package edu.touro.cs.mcon364;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static edu.touro.cs.mcon364.WordleResponse.*;

/**
 * Logic for a GUI Wordle game
 */
public class WordleModel {
    private static final int LETTERS_IN_ALPHABET = 26;
    private static final int ASCII_OFFSET = 65;

    private String answer;
    private List<String> words;
    private int[] answerCounts;

    public WordleModel() {
        try {
            words = Files.readAllLines(Paths.get("./answers.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newGame();
    }

    public void newGame() {
        answer = getAnswer();
    }

    /**
     * Randomly chooses an answer from the answer bank
     *
     * @return the answer
     */
    private String getAnswer() {
        int rand = new Random().nextInt(words.size());
        String word = words.get(rand).toUpperCase();
        // Initialize and populate array with count of how many of each letter in the answer
        answerCounts = getLettersCount(word.toCharArray());
        return word;
    }

    /**
     * Processes the input when the user hits enter
     *
     * @return true if the entry 100% matches, false otherwise
     */
    public List<WordleResponse> checkGuess(String guess) {
        int size = guess.length();
        if (!isLegalWord(guess)) return Collections.singletonList(ILLEGAL_WORD);

        List<WordleResponse> response = new ArrayList<>(size);
        char[] guessChars = guess.toUpperCase().toCharArray();
        int[] guessCounts = getLettersCount(guessChars);
        for (int i = 0; i < size; i++) {
            char currentChar = guessChars[i];
            if (currentChar == answer.charAt(i)) {
                response.add(CORRECT);
            } else if (answer.indexOf(currentChar) >= 0) {
                response.add(DIFFERENT_POSITION);
            } else {
                response.add(WRONG);
            }
        }

        return response;
    }

    private static int[] getLettersCount(char[] word) {
        int[] result = new int[LETTERS_IN_ALPHABET];
        for (char c : word) {
            result[c - ASCII_OFFSET]++;
        }
        return result;
    }

    private boolean isLegalWord(String guess) {
        return words.contains(guess.toLowerCase());
    }
}
