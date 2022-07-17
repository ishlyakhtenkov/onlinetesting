package ru.javaprojects.onlinetesting.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Answer {

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "correct", nullable = false)
    private boolean correct;

    public Answer() {
    }

    public Answer(String content, boolean correct) {
        this.content = content;
        this.correct = correct;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return correct == answer.correct &&
                Objects.equals(content, answer.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, correct);
    }
}