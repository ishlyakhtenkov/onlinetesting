package ru.javaprojects.onlinetesting.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javaprojects.onlinetesting.model.Question;

import java.util.List;

import static ru.javaprojects.onlinetesting.testdata.QuestionTestData.*;
import static ru.javaprojects.onlinetesting.testdata.TopicTestData.MATH_TOPIC_NAME;

@SpringBootTest
@ActiveProfiles("dev")
@Sql(scripts = "classpath:data-dev.sql", config = @SqlConfig(encoding = "UTF-8"))
class QuestionServiceTest {

    @Autowired
    private QuestionService service;

    @Test
    void getAll() {
        List<Question> questions = service.getAll(MATH_TOPIC_NAME);
        Assertions.assertThat(questions).usingRecursiveComparison().ignoringFields("topic")
                .isEqualTo(List.of(mathQuestion1, mathQuestion2, mathQuestion3));
    }
}