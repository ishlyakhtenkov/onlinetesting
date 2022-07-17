package ru.javaprojects.onlinetesting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.onlinetesting.testdata.QuestionTestData.*;

class QuestionTest {

    @Test
    void getCorrectAnswer() {
        assertEquals("4", mathQuestion1.getCorrectAnswer());
        assertEquals("9", mathQuestion2.getCorrectAnswer());
        assertEquals("5", mathQuestion3.getCorrectAnswer());
    }

    @Test
    void getCorrectAnswerWhenAllAnswersIncorrect() {
        assertThrows(IllegalStateException.class, noCorrectAnswerQuestion::getCorrectAnswer);
    }

    @Test
    void getCorrectAnswerWhenNoAnswers() {
        assertThrows(IllegalStateException.class, noAnswersQuestion::getCorrectAnswer);
    }
}