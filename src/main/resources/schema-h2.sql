DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS topics;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000 INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647;

CREATE TABLE topics
(
    id INTEGER DEFAULT global_seq.nextval PRIMARY KEY,
    name VARCHAR NOT NULL
);
CREATE UNIQUE INDEX topics_unique_name_idx ON topics (name);

CREATE TABLE questions
(
    id INTEGER DEFAULT global_seq.nextval PRIMARY KEY,
    topic_id INTEGER NOT NULL,
    content VARCHAR NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX questions_unique_topic_content_idx ON questions (topic_id, content);

CREATE TABLE answers
(
    question_id INTEGER NOT NULL,
    content VARCHAR NOT NULL,
    correct BOOL NOT NULL,
    h2_extra_column VARCHAR AS CASE WHEN correct = FALSE THEN NULL ELSE content END,
    FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX answers_unique_question_content_idx ON answers (question_id, content);
CREATE UNIQUE INDEX answers_unique_question_correct_idx ON answers (question_id, h2_extra_column);