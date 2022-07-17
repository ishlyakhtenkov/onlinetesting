package ru.javaprojects.onlinetesting.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions", uniqueConstraints = {@UniqueConstraint(columnNames = {"topic_id", "content"}, name = "questions_unique_topic_content_idx")})
public class Question extends AbstractBaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Topic topic;

    @CollectionTable(name = "answers", joinColumns = @JoinColumn(name = "question_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"question_id", "content"}, name = "answers_unique_question_content_idx")})
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Answer> answers;

    public Question() {
    }

    public Question(Integer id, String content, List<Answer> answers) {
        super(id);
        this.content = content;
        this.answers = answers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}