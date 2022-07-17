package ru.javaprojects.onlinetesting.service;

import org.springframework.stereotype.Service;
import ru.javaprojects.onlinetesting.model.Topic;
import ru.javaprojects.onlinetesting.repository.TopicRepository;

import java.util.List;

@Service
public class TopicService {
    private TopicRepository repository;

    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public List<Topic> getAll() {
        return repository.findAllByOrderByName();
    }
}