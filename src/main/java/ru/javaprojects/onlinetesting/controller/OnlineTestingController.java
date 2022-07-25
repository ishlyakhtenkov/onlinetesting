package ru.javaprojects.onlinetesting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javaprojects.onlinetesting.model.Question;
import ru.javaprojects.onlinetesting.service.QuestionService;
import ru.javaprojects.onlinetesting.service.TopicService;

import javax.servlet.http.HttpSession;
import java.util.*;

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
    public String showHomePage(HttpSession session, Model model) {
        log.info("get all topics for home page");
        session.invalidate();
        model.addAttribute("topics", topicService.getAll());
        return "index";
    }

    @PostMapping("/startTest")
    public String startTest(@RequestParam(value = TOPIC_NAME) String topicName, HttpSession session,
                            RedirectAttributes redirectAttributes) {
        log.info("start test on topic: {}", topicName);
        List<Question> questions = questionService.getAll(topicName);
        if (questions.isEmpty()) {
            redirectAttributes.addAttribute(TOPIC_NAME, topicName);
            return "redirect:/noQuestions";
        }
        Collections.shuffle(questions);
        Question currentQuestion = questions.remove(0);
        session.setAttribute(TOPIC_NAME, topicName);
        session.setAttribute(ANSWERS_AMOUNT, 0);
        session.setAttribute(QUESTIONS, questions);
        session.setAttribute(CURRENT_QUESTION, currentQuestion);
        session.setAttribute(WRONG_ANSWERS, new HashMap<Question, String>());
        return "redirect:/question";
    }

    @GetMapping("/noQuestions")
    public String showNoQuestionsPage(@RequestParam(value = TOPIC_NAME) String topicName) {
        return "noQuestions";
    }

    @GetMapping("/question")
    public String showQuestion(HttpSession session) {
        if (Objects.isNull(session.getAttribute(CURRENT_QUESTION))) {
            throw new BadRequestException("\"/question\" request error: CURRENT_QUESTION is null");
        }
        return "question";
    }

    @PostMapping("/checkAnswer")
    public String checkAnswer(@RequestParam(value = "question") String question,
                              @RequestParam(value = "answer") String answer, HttpSession session) {
        Question currentQuestion = (Question) session.getAttribute(CURRENT_QUESTION);
        if (Objects.isNull(currentQuestion)) {
            throw new BadRequestException("\"/checkAnswer\" request error: CURRENT_QUESTION is null");
        }
        if (questionNotCurrent(question, currentQuestion)) {
            return "redirect:/question";
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
                return "redirect:/question";
            } else {
                return "redirect:/result";
            }
        }
    }

    private boolean questionNotCurrent(String question, Question currentQuestion) {
        return !question.equals(currentQuestion.getContent());
    }

    @GetMapping("/result")
    public String showResultPage(HttpSession session) {
        session.removeAttribute(QUESTIONS);
        session.removeAttribute(CURRENT_QUESTION);
        if (Objects.isNull(session.getAttribute(ANSWERS_AMOUNT))
                || Objects.isNull(session.getAttribute(WRONG_ANSWERS))
                || Objects.isNull(session.getAttribute(TOPIC_NAME))) {
            throw new BadRequestException("\"/result\" request error: ANSWERS_AMOUNT or WRONG_ANSWERS or TOPIC_NAME is null");
        }
        return "result";
    }

    @PostMapping("/interruptTest")
    public String interruptTest(HttpSession session) {
        if (isTestFinished(session)) {
            throw new BadRequestException("\"/interruptTest\" request error: CURRENT_QUESTION is null");
        }
        return "redirect:/result";
    }

    private boolean isTestFinished(HttpSession session) {
        return Objects.isNull(session.getAttribute(CURRENT_QUESTION));
    }
}