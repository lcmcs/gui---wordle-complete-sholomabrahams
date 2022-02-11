import edu.touro.cs.mcon364.WordleModel;
import edu.touro.cs.mcon364.WordleResponse;
import org.junit.Test;

import java.util.ArrayList;
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
    public void TestAnswerIntegrity() {
        boolean anyInvalid = false;
        for (char c : model.answer.toCharArray()) {
            if (c < 65 || c > 90) { // Not a capital letter
                anyInvalid = true;
                break;
            }
        }
        assertFalse(anyInvalid);
    }

    @Test
    public void TestNewGame() {
        var original = model.answer;
        model.newGame();
        var newAns = model.answer;
        assertNotEquals(original, newAns);
    }

    @Test
    public void InvalidGuess() {
        var res = model.checkGuess("ZVXYA");
        assertEquals(Collections.singletonList(ILLEGAL_WORD), res);
    }

    @Test
    public void DoubleLetterGuess() {
        model.answer = "SAFER";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EERIE");
        assertEquals(List.of(new WordleResponse[]{DIFFERENT_POSITION, WRONG, DIFFERENT_POSITION, WRONG, WRONG}), res);
    }

    @Test
    public void DoubleLetterAnswer() {
        model.answer = "EERIE";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EVERY");
        assertEquals(List.of(CORRECT, WRONG, DIFFERENT_POSITION, DIFFERENT_POSITION, WRONG), res);
    }

    @Test
    public void CorrectAnswer() {
        model.answer = "EERIE";
        model.answerCounts = WordleModel.getLettersCount(model.answer);
        var res = model.checkGuess("EERIE");
        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), res);
    }
}
