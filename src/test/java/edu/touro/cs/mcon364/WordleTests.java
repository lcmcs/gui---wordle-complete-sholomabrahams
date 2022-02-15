package edu.touro.cs.mcon364;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static edu.touro.cs.mcon364.WordleResponse.*;
import static org.junit.Assert.*;

public class WordleTests {
    WordleModel model;

    public WordleTests() {
        model = new WordleModel();
    }

    @Test
    public void answerIntegrity() {
        boolean anyInvalid = false;
        for (char c : model.answer.toCharArray()) {
            if (c < 'A' || c > 'Z') { // Not a capital letter
                anyInvalid = true;
                break;
            }
        }
        assertFalse(anyInvalid);
    }

    @Test
    public void answerSelection() {
        var localModel = new WordleModel();
        localModel.words = Collections.singletonList("HELLO");
        for (int i  = 0; i < 2; i++) {
            localModel.newGame();
            assertEquals("HELLO", localModel.answer);
        }
    }

    @Test
    public void newGame() {
        var original = model.answer;
        model.newGame();
        var newAns = model.answer;
        assertNotEquals(original, newAns);
    }

    @Test
    public void invalidGuess() {
        var res = model.checkGuess("ZVXYA");
        assertEquals(Collections.singletonList(ILLEGAL_WORD), res);
    }

    @Test
    public void doubleLetterGuess() {
        model.answer = "SAFER";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EERIE");
        assertEquals(List.of(new WordleResponse[]{DIFFERENT_POSITION, WRONG, DIFFERENT_POSITION, WRONG, WRONG}), res);
    }

    @Test
    public void doubleLetterAnswer() {
        model.answer = "EERIE";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EVERY");
        assertEquals(List.of(CORRECT, WRONG, DIFFERENT_POSITION, DIFFERENT_POSITION, WRONG), res);
    }

    @Test
    public void correctAnswer() {
        model.answer = "EERIE";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EERIE");
        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), res);
    }
}
