package ru.javaprojects.onlinetesting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import ru.javaprojects.onlinetesting.model.Question;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaprojects.onlinetesting.controller.OnlineTestingController.*;
import static ru.javaprojects.onlinetesting.testdata.QuestionTestData.*;
import static ru.javaprojects.onlinetesting.testdata.TopicTestData.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Sql(scripts = "classpath:data-dev.sql", config = @SqlConfig(encoding = "UTF-8"))
class OnlineTestingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topics", List.of(historyTopic, mathTopic)))
                .andExpect(view().name("index"));
    }

    @Test
    void startTest() throws Exception {
        HttpSession session = mockMvc.perform(post("/startTest")
                .param(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attributeExists(CURRENT_QUESTION))
                .andExpect(view().name("question"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(0, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(2, ((List<Question>) session.getAttribute(QUESTIONS)).size());
        assertNotNull(session.getAttribute(CURRENT_QUESTION));
        assertEquals(0, ((Map<Question, String>) session.getAttribute(WRONG_ANSWERS)).size());
    }

    @Test
    void checkAnswerWhenAnswerCorrect() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion1.getContent())
                .param("answer", mathQuestion1.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 0, List.of(mathQuestion2, mathQuestion3), mathQuestion1, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(CURRENT_QUESTION, mathQuestion2))
                .andExpect(view().name("question"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(1, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(0, ((Map<Question, String>) session.getAttribute(WRONG_ANSWERS)).size());
        assertEquals(mathQuestion2, session.getAttribute(CURRENT_QUESTION));
        assertEquals(List.of(mathQuestion3), session.getAttribute(QUESTIONS));
    }

    @Test
    void checkAnswerWhenAnswerIncorrect() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion1.getContent())
                .param("answer", INCORRECT_ANSWER)
                .session(createMockSession(MATH_TOPIC_NAME, 0, List.of(mathQuestion2, mathQuestion3), mathQuestion1, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(CURRENT_QUESTION, mathQuestion2))
                .andExpect(view().name("question"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(1, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(1, ((Map<Question, String>) session.getAttribute(WRONG_ANSWERS)).size());
        assertEquals(mathQuestion2, session.getAttribute(CURRENT_QUESTION));
        assertEquals(List.of(mathQuestion3), session.getAttribute(QUESTIONS));
    }

    @Test
    void checkAnswerWhenQuestionNotCurrent() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion1.getContent())
                .param("answer", mathQuestion1.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 1, List.of(mathQuestion3), mathQuestion2, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(CURRENT_QUESTION, mathQuestion2))
                .andExpect(view().name("question"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(1, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(0, ((Map<Question, String>) session.getAttribute(WRONG_ANSWERS)).size());
        assertEquals(mathQuestion2, session.getAttribute(CURRENT_QUESTION));
        assertEquals(List.of(mathQuestion3), session.getAttribute(QUESTIONS));
    }

    @Test
    void checkAnswerWhenAnswerIsBlank() throws Exception {
        mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion2.getContent())
                .session(createMockSession(MATH_TOPIC_NAME, 1, List.of(mathQuestion3), mathQuestion2, new HashMap<>())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkAnswerWhenQuestionIsBlank() throws Exception {
        mockMvc.perform(post("/checkAnswer")
                .param("answer", mathQuestion2.getContent())
                .session(createMockSession(MATH_TOPIC_NAME, 1, List.of(mathQuestion3), mathQuestion2, new HashMap<>())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkAnswerAndShowResultWhenNoWrongAnswers() throws Exception {
        mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion3.getContent())
                .param("answer", mathQuestion3.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 2, List.of(), mathQuestion3, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(WRONG_ANSWERS, new HashMap<Question, String>()))
                .andExpect(model().attribute(ANSWERS_AMOUNT, 3))
                .andExpect(view().name("result"));
    }

    @Test
    void checkAnswerAndShowResultWhenHasWrongAnswers() throws Exception {
        mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion3.getContent())
                .param("answer", mathQuestion3.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 2, List.of(), mathQuestion3, WRONG_ANSWERS_MAP)))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(WRONG_ANSWERS, WRONG_ANSWERS_MAP))
                .andExpect(model().attribute(ANSWERS_AMOUNT, 3))
                .andExpect(view().name("result"));
    }

    @Test
    void interruptTestWhenNoWrongAnswers() throws Exception {
        mockMvc.perform(post("/interruptTest")
                .session(createMockSession(MATH_TOPIC_NAME, 2, List.of(), mathQuestion3, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(WRONG_ANSWERS, new HashMap<Question, String>()))
                .andExpect(model().attribute(ANSWERS_AMOUNT, 2))
                .andExpect(view().name("result"));
    }

    @Test
    void interruptTestWhenHasWrongAnswers() throws Exception {
        mockMvc.perform(post("/interruptTest")
                .session(createMockSession(MATH_TOPIC_NAME, 2, List.of(), mathQuestion3, WRONG_ANSWERS_MAP)))
                .andExpect(status().isOk())
                .andExpect(model().size(3))
                .andExpect(model().attribute(TOPIC_NAME, MATH_TOPIC_NAME))
                .andExpect(model().attribute(WRONG_ANSWERS, WRONG_ANSWERS_MAP))
                .andExpect(model().attribute(ANSWERS_AMOUNT, 2))
                .andExpect(view().name("result"));
    }

    @Test
    void notFoundRequest() throws Exception {
        mockMvc.perform(get("/xyz"))
                .andExpect(status().isNotFound());
    }

    private MockHttpSession createMockSession(String topicName, int answersAmount, List<Question> questions,
                                              Question currentQuestion, Map<Question, String> wrongAnswers) {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute(TOPIC_NAME, topicName);
        mockSession.setAttribute(ANSWERS_AMOUNT, answersAmount);
        mockSession.setAttribute(QUESTIONS, new ArrayList<>(questions));
        mockSession.setAttribute(CURRENT_QUESTION, currentQuestion);
        mockSession.setAttribute(WRONG_ANSWERS, wrongAnswers);
        return mockSession;
    }
}