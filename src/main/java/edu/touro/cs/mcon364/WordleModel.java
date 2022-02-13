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

    // The package-private variables and method would be private, but need to be package-private for the tests to work
    String answer;
    List<String> words;
    Map<Character, Integer> answerCounts;

    private final Set<String> WORD_SET;

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
//        String word = "SAFER";
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
        Map<Character, Integer> guessCounts = getLettersCount(guess);
        Map<Character, Integer> ansCounts = new HashMap<>(answerCounts);

        // Add the appropriate WordleResponse to the response for each char in the guess
        for (int i = 0; i < guessLength; i++) {
            char currentChar = guess.charAt(i); // The char at this index of the guess
            if (currentChar == answer.charAt(i)) { // Char is in correct position
                response.add(CORRECT);
                guessCounts.put(currentChar, guessCounts.get(currentChar) - 1);
                ansCounts.put(currentChar, ansCounts.get(currentChar) - 1);
            } else if (answer.indexOf(currentChar) >= 0) { // Char is elsewhere in the answer
                if (ansCounts.getOrDefault(currentChar, 0) > 0 && guessCounts.getOrDefault(currentChar, 0) == 1) { // No duplicates of this char in the guess
                    response.add(DIFFERENT_POSITION);
                    guessCounts.put(currentChar, guessCounts.get(currentChar) - 1);
                    ansCounts.put(currentChar, ansCounts.get(currentChar) - 1);
                } else response.add(null); // Save duplicates for later
            } else { // Char is not in answer
                response.add(WRONG);
                guessCounts.put(currentChar, guessCounts.get(currentChar) - 1);
            }
        }

        // Deal with duplicates (max amount of duplicates is 3, max amount of contiguous duplicates is 2)
        // (We're dealing with them now because all the perfect matches have already been processed)
        for (int i = 0; i < guessLength; i++) {
            if (response.get(i) != null) continue;
            char currentChar = guess.charAt(i);
            if (ansCounts.getOrDefault(currentChar, 0) > 0) { // There are still unaccounted-for occurrences of this letter in the answer
                response.set(i, DIFFERENT_POSITION);
                guessCounts.put(currentChar, guessCounts.get(currentChar) - 1);
                ansCounts.put(currentChar, ansCounts.get(currentChar) - 1);
            } else { // No more of this letter unaccounted-for in the answer
                response.set(i, WRONG);
                guessCounts.put(currentChar, guessCounts.get(currentChar) - 1);
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
    static Map<Character, Integer> getLettersCount(String word) {
        char[] letters = word.toCharArray();
        Map<Character, Integer> result = new HashMap<>(LETTERS_IN_ALPHABET);
        for (char c : letters) {
            int current = result.getOrDefault(c, 0);
            result.put(c, current + 1);
        }
        return result;
    }
}
