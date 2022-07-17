package ru.javaprojects.onlinetesting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javaprojects.onlinetesting.service.QuestionService;
import ru.javaprojects.onlinetesting.service.TopicService;

@Controller
public class OnlineTestingController {
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
}