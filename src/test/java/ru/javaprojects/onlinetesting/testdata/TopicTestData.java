package ru.javaprojects.onlinetesting.testdata;

import ru.javaprojects.onlinetesting.model.AbstractBaseEntity;
import ru.javaprojects.onlinetesting.model.Topic;

public class TopicTestData {
    public static final int mathTopicId = AbstractBaseEntity.START_SEQ;
    public static final int historyTopicId = AbstractBaseEntity.START_SEQ + 1;
    public static final Topic mathTopic = new Topic(mathTopicId, "Math");
    public static final Topic historyTopic = new Topic(historyTopicId, "History");
}
