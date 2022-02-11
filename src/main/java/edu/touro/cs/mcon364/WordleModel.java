package edu.touro.cs.mcon364;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static edu.touro.cs.mcon364.WordleResponse.*;

/**
 * Logic for a GUI Wordle game
 */
public class WordleModel {
    private static final int LETTERS_IN_ALPHABET = 26;
    private static final int ASCII_OFFSET = 65;

    String answer;
    private List<String> words;
    private final Set<String> WORD_SET;
    private int[] answerCounts;

    public WordleModel() {
        try {
            words = Files.readAllLines(Paths.get("./answers.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        WORD_SET = new HashSet<>(words);
        newGame();
    }

    /**
     * Gets a new answer
     */
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
        answerCounts = getLettersCount(word);
        return word;
    }

    /**
     * Processes the input when the user hits enter
     *
     * @return true if the entry 100% matches, false otherwise
     */
    public List<WordleResponse> checkGuess(String guess) {
        int guessLength = guess.length();
        if (!WORD_SET.contains(guess.toLowerCase())) return Collections.singletonList(ILLEGAL_WORD);

        List<WordleResponse> response = new ArrayList<>(guessLength);
        // Arrays to keep track of how many unaccounted-for remaining of each letter of the alphabet
        int[] guessCounts = getLettersCount(guess);
        int[] ansCounts = answerCounts.clone();

        // Add the appropriate WordleResponse to the response for each char in the guess
        for (int i = 0; i < guessLength; i++) {
            char currentChar = guess.charAt(i); // The char at this index of the guess
            int posInAlphabet = currentChar - ASCII_OFFSET;
            if (currentChar == answer.charAt(i)) { // Char is in correct position
                response.add(CORRECT);
                guessCounts[posInAlphabet]--;
                ansCounts[posInAlphabet]--;
            } else if (answer.indexOf(currentChar) >= 0) { // Char is elsewhere in the answer
                if (ansCounts[posInAlphabet] > 0 && guessCounts[posInAlphabet] == 1) { // No duplicates of this char in the guess
                    response.add(DIFFERENT_POSITION);
                    guessCounts[posInAlphabet]--;
                    ansCounts[posInAlphabet]--;
                } else response.add(null); // Save duplicates for later
            } else { // Char is not in answer
                response.add(WRONG);
                guessCounts[posInAlphabet]--;
            }
        }

        // Deal with duplicates (max amount of duplicates is 3, max amount of contiguous duplicates is 2)
        // (We're dealing with them now because all the perfect matches have already been processed)
        for (int i = 0; i < guessLength; i++) {
            if (response.get(i) != null) continue;
            char currentChar = guess.charAt(i);
            int posInAlphabet = currentChar - ASCII_OFFSET;
            if (ansCounts[posInAlphabet] > 0) { // There are still unaccounted-for occurrences of this letter in the answer
                response.set(i, DIFFERENT_POSITION);
                guessCounts[posInAlphabet]--;
                ansCounts[posInAlphabet]--;
            } else { // No more of this letter unaccounted-for in the answer
                response.set(i, WRONG);
                guessCounts[posInAlphabet]--;
            }
        }

        return response;
    }

    /**
     * Counts how many times each letter of the alphabet appears in a word
     *
     * @param word the uppercase word to create a letter count for
     * @return an int[] the length of the alphabet with a count of how many times
     * the letter at that index of the alphabet appears in the word
     */
    private static int[] getLettersCount(String word) {
        char[] letters = word.toCharArray();
        int[] result = new int[LETTERS_IN_ALPHABET];
        for (char c : letters) {
            result[c - ASCII_OFFSET]++;
        }
        return result;
    }
}
