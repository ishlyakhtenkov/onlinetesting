package ru.javaprojects.onlinetesting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "topics", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "topics_unique_name_idx")})
public class Topic extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    public Topic() {
    }

    public Topic(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}