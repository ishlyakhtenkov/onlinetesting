package ru.javaprojects.onlinetesting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.onlinetesting.model.Question;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findAllByTopicName(String name);
}