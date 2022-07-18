package ru.javaprojects.onlinetesting.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javaprojects.onlinetesting.model.Topic;

import java.util.List;

import static ru.javaprojects.onlinetesting.testdata.TopicTestData.historyTopic;
import static ru.javaprojects.onlinetesting.testdata.TopicTestData.mathTopic;

@SpringBootTest
@ActiveProfiles("dev")
@Sql(scripts = "classpath:data-dev.sql", config = @SqlConfig(encoding = "UTF-8"))
class TopicServiceTest {

    @Autowired
    private TopicService service;

    @Test
    void getAll() {
        List<Topic> topics = service.getAll();
        Assertions.assertThat(topics).usingRecursiveComparison().isEqualTo(List.of(historyTopic, mathTopic));
    }
}