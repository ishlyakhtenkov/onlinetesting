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
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;
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
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/question"))
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
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/question"))
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
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/question"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(1, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(1, ((Map<Question, String>) session.getAttribute(WRONG_ANSWERS)).size());
        assertEquals(mathQuestion2, session.getAttribute(CURRENT_QUESTION));
        assertEquals(List.of(mathQuestion3), session.getAttribute(QUESTIONS));
    }

    @Test
    void checkAnswerWhenQuestionIsNotCurrent() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion1.getContent())
                .param("answer", mathQuestion1.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 1, List.of(mathQuestion3), mathQuestion2, new HashMap<>())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/question"))
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
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void checkAnswerWhenQuestionIsBlank() throws Exception {
        mockMvc.perform(post("/checkAnswer")
                .param("answer", mathQuestion2.getContent())
                .session(createMockSession(MATH_TOPIC_NAME, 1, List.of(mathQuestion3), mathQuestion2, new HashMap<>())))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void checkAnswerWhenLastQuestionAndNoWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion3.getContent())
                .param("answer", mathQuestion3.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 2, emptyList(), mathQuestion3, new HashMap<>())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(emptyList(), session.getAttribute(QUESTIONS));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(3, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(emptyMap(), session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void checkAnswerWhenLastQuestionAndHasWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion3.getContent())
                .param("answer", mathQuestion3.getCorrectAnswer())
                .session(createMockSession(MATH_TOPIC_NAME, 2, emptyList(), mathQuestion3, WRONG_ANSWERS_MAP)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(emptyList(), session.getAttribute(QUESTIONS));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(3, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(WRONG_ANSWERS_MAP, session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void getQuestionWhenNoCurrentQuestion() throws Exception {
        MockHttpSession mockSession = createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, new HashMap<>());
        mockSession.removeAttribute(CURRENT_QUESTION);
        mockMvc.perform(get("/question")
                .session(mockSession))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void checkAnswerWhenNoCurrentQuestion() throws Exception {
        MockHttpSession mockSession = createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, new HashMap<>());
        mockSession.removeAttribute(CURRENT_QUESTION);
        mockMvc.perform(post("/checkAnswer")
                .param("question", mathQuestion3.getContent())
                .param("answer", mathQuestion3.getCorrectAnswer())
                .session(mockSession))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void interruptTestWhenNoWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(post("/interruptTest")
                .session(createMockSession(MATH_TOPIC_NAME, 2, emptyList(), mathQuestion3, new HashMap<>())))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(emptyList(), session.getAttribute(QUESTIONS));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(2, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(emptyMap(), session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void interruptTestWhenHasWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(post("/interruptTest")
                .session(createMockSession(MATH_TOPIC_NAME, 2, emptyList(), mathQuestion3, WRONG_ANSWERS_MAP)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertEquals(emptyList(), session.getAttribute(QUESTIONS));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(2, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(WRONG_ANSWERS_MAP, session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void interruptTestWhenNoCurrentQuestion() throws Exception {
        MockHttpSession mockSession = createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, new HashMap<>());
        mockSession.removeAttribute(CURRENT_QUESTION);
        mockMvc.perform(post("/interruptTest")
                .session(mockSession))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void showResultWhenNoWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(get("/result")
                .session(createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, new HashMap<>())))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertNull(session.getAttribute(QUESTIONS));
        assertNull(session.getAttribute(CURRENT_QUESTION));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(3, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(emptyMap(), session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void showResultWhenHasWrongAnswers() throws Exception {
        HttpSession session = mockMvc.perform(get("/result")
                .session(createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, WRONG_ANSWERS_MAP)))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andReturn()
                .getRequest()
                .getSession();

        assertNull(session.getAttribute(QUESTIONS));
        assertNull(session.getAttribute(CURRENT_QUESTION));
        assertEquals(MATH_TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        assertEquals(3, session.getAttribute(ANSWERS_AMOUNT));
        assertEquals(WRONG_ANSWERS_MAP, session.getAttribute(WRONG_ANSWERS));
    }

    @Test
    void showResultWhenNoAnswersAmount() throws Exception {
        MockHttpSession mockSession = createMockSession(MATH_TOPIC_NAME, 3, emptyList(), mathQuestion3, new HashMap<>());
        mockSession.removeAttribute(ANSWERS_AMOUNT);
        mockMvc.perform(get("/result")
                .session(mockSession))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
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