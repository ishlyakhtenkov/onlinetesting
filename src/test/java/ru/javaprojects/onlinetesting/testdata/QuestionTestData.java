package ru.javaprojects.onlinetesting.testdata;

import ru.javaprojects.onlinetesting.model.Answer;
import ru.javaprojects.onlinetesting.model.Question;

import java.util.Collections;
import java.util.List;

import static ru.javaprojects.onlinetesting.model.AbstractBaseEntity.START_SEQ;

public class QuestionTestData {
    public static final int MATH_QUESTION_1_ID = START_SEQ + 2;
    public static final int MATH_QUESTION_2_ID = START_SEQ + 3;
    public static final int MATH_QUESTION_3_ID = START_SEQ + 4;
    public static final Question mathQuestion1;
    public static final Question mathQuestion2;
    public static final Question mathQuestion3;
    public static final Question noCorrectAnswerQuestion;
    public static final Question noAnswersQuestion;

    static {
        List<Answer> mathQuestion1Answers = List.of(new Answer("5", false), new Answer("3", false), new Answer("4", true));
        mathQuestion1 = new Question(MATH_QUESTION_1_ID, "Calculate the value of the expression (2 + 2)", mathQuestion1Answers);
        List<Answer> mathQuestion2Answers = List.of(new Answer("6", false), new Answer("9", true), new Answer("10", false));
        mathQuestion2 = new Question(MATH_QUESTION_2_ID, "Calculate the value of the expression (3 * 3)", mathQuestion2Answers);
        List<Answer> mathQuestion3Answers = List.of(new Answer("8", false), new Answer("2", false), new Answer("5", true));
        mathQuestion3 = new Question(MATH_QUESTION_3_ID, "Calculate the value of the expression (10 / 2)", mathQuestion3Answers);
        List<Answer> noCorrectAnswerQuestionAnswers = List.of(new Answer("ABC", false), new Answer("XYZ", false));
        noCorrectAnswerQuestion = new Question(null, "All answers are incorrect!", noCorrectAnswerQuestionAnswers);
        noAnswersQuestion = new Question(null, "There are no answers at all", Collections.emptyList());
    }
}