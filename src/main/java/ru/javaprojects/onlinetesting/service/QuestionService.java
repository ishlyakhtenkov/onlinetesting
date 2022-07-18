package ru.javaprojects.onlinetesting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javaprojects.onlinetesting.model.Question;
import ru.javaprojects.onlinetesting.repository.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {
    private QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getAll(String topicName) {
        Assert.notNull(topicName, "topicName must not be null");
        return repository.findAllByTopicName(topicName);
    }
}