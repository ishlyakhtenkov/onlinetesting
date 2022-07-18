package ru.javaprojects.onlinetesting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javaprojects.onlinetesting.model.Question;
import ru.javaprojects.onlinetesting.service.QuestionService;
import ru.javaprojects.onlinetesting.service.TopicService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OnlineTestingController {
    public static final String TOPIC_NAME = "topicName";
    public static final String ANSWERS_AMOUNT = "answersAmount";
    public static final String QUESTIONS = "questions";
    public static final String CURRENT_QUESTION = "currentQuestion";
    public static final String WRONG_ANSWERS = "wrongAnswers";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private TopicService topicService;
    private QuestionService questionService;

    public OnlineTestingController(TopicService topicService, QuestionService questionService) {
        this.topicService = topicService;
        this.questionService = questionService;
    }

    @GetMapping
    public String showHomePage(Model model) {
        log.info("get all topics for home page");
        model.addAttribute("topics", topicService.getAll());
        return "index";
    }

    @PostMapping("/startTest")
    public String startTest(@RequestParam(value = TOPIC_NAME) String topicName, HttpSession session, Model model) {
        log.info("start test on topic: {}", topicName);
        List<Question> questions = questionService.getAll(topicName);
        if (questions.isEmpty()) {
            return noQuestionsPage(topicName, model);
        }
        Collections.shuffle(questions);
        Question currentQuestion = questions.remove(0);
        session.setAttribute(TOPIC_NAME, topicName);
        session.setAttribute(ANSWERS_AMOUNT, 0);
        session.setAttribute(QUESTIONS, questions);
        session.setAttribute(CURRENT_QUESTION, currentQuestion);
        session.setAttribute(WRONG_ANSWERS, new HashMap<Question, String>());
        return questionPage(topicName, currentQuestion, model);
    }

    private String noQuestionsPage(String topicName, Model model) {
        model.addAttribute(TOPIC_NAME, topicName);
        return "noQuestions";
    }

    private String questionPage(String topicName, Question currentQuestion, Model model) {
        model.addAttribute(TOPIC_NAME, topicName);
        model.addAttribute(CURRENT_QUESTION, currentQuestion);
        return "question";
    }

    @PostMapping("/checkAnswer")
    public String checkAnswer(@RequestParam(value = "question") String question,
                              @RequestParam(value = "answer") String answer, HttpSession session, Model model) {
        Question currentQuestion = (Question) session.getAttribute(CURRENT_QUESTION);
        if (questionNotCurrent(question, currentQuestion)) {
            return questionPage((String) session.getAttribute(TOPIC_NAME), currentQuestion, model);
        } else {
            int answersAmount = (int) session.getAttribute(ANSWERS_AMOUNT);
            session.setAttribute(ANSWERS_AMOUNT, ++answersAmount);
            if (!answer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                Map<Question, String> wrongAnswers = (Map<Question, String>) session.getAttribute(WRONG_ANSWERS);
                wrongAnswers.put(currentQuestion, answer);
            }
            List<Question> questions = (List<Question>) session.getAttribute("questions");
            if (!questions.isEmpty()) {
                currentQuestion = questions.remove(0);
                session.setAttribute(CURRENT_QUESTION, currentQuestion);
                return questionPage((String) session.getAttribute(TOPIC_NAME), currentQuestion, model);
            } else {
                return resultPage(session, model);
            }
        }
    }

    private boolean questionNotCurrent(String question, Question currentQuestion) {
        return !question.equals(currentQuestion.getContent());
    }

    private String resultPage(HttpSession session, Model model) {
        model.addAttribute(TOPIC_NAME, session.getAttribute(TOPIC_NAME));
        model.addAttribute(WRONG_ANSWERS, session.getAttribute(WRONG_ANSWERS));
        model.addAttribute(ANSWERS_AMOUNT, session.getAttribute(ANSWERS_AMOUNT));
        session.invalidate();
        return "result";
    }

    @PostMapping("/interruptTest")
    public String interruptTest(HttpSession session, Model model) {
        return resultPage(session, model);
    }
}