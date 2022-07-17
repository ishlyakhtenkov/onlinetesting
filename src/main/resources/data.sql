DELETE FROM answers;
DELETE FROM questions;
DELETE FROM topics;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO topics (name)
VALUES ('Math'),
       ('History');

INSERT INTO questions (topic_id, content)
VALUES (100000, 'Calculate the value of the expression (2 + 2)'),
       (100000, 'Calculate the value of the expression (3 * 3)'),
       (100000, 'Calculate the value of the expression (10 / 2)'),
       (100001, 'What year did world war 2 start?'),
       (100001, 'What year did world war 2 end?');

INSERT INTO answers (question_id, content, correct)
VALUES (100002, '5', false),
       (100002, '3', false),
       (100002, '4', true),
       (100003, '6', false),
       (100003, '9', true),
       (100003, '10', false),
       (100004, '8', false),
       (100004, '2', false),
       (100004, '5', true),
       (100005, '1941', true),
       (100005, '1940', false),
       (100005, '1942', false),
       (100005, '1945', false),
       (100006, '1941', false),
       (100006, '1945', true),
       (100006, '1950', false);