package ru.javaprojects.onlinetesting.testdata;

import ru.javaprojects.onlinetesting.model.Topic;

import static ru.javaprojects.onlinetesting.model.AbstractBaseEntity.START_SEQ;

public class TopicTestData {
    public static final int mathTopicId = START_SEQ;
    public static final int historyTopicId = START_SEQ + 1;
    public static final String MATH_TOPIC_NAME = "Math";
    public static final String HISTORY_TOPIC_NAME = "History";
    public static final Topic mathTopic = new Topic(mathTopicId, MATH_TOPIC_NAME);
    public static final Topic historyTopic = new Topic(historyTopicId, HISTORY_TOPIC_NAME);
}
