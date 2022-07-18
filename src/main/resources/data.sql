DELETE FROM answers;
DELETE FROM questions;
DELETE FROM topics;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO topics (name)
VALUES ('Math'),
       ('Geography'),
       ('History');

INSERT INTO questions (topic_id, content)
VALUES (100000, 'Find the missing number: 82 + ___ = 90'),
       (100000, 'Ed had saved $16. Then grandma gave him $10. Now how much more does he need in order to buy a toolset for $32?'),
       (100000, 'Calculate mentally: 59 + 8 = ___'),
       (100000, 'Calculate mentally: 145 + 74 = ___'),
       (100000, 'Mary picked 5 apples and Bill picked 9. The children shared all of their apples evenly. How many did each child get?'),
       (100000, 'Find the difference of 75 and 90'),
       (100000, 'Jennifer bought two vacuum cleaners for $152 each. What was the total cost?'),
       (100000, 'A box contains 450 disks in all. Of them, 126 are music CDs and the rest are DVDs. How many DVDs are in the box?'),
       (100000, 'Find the change, if you buy a meal for $3.35 and you pay with $4.'),
       (100000, 'Calculate mentally: 2 + 2 * 2 = ___'),
       (100001, 'How many continents are on Earth?'),
       (100001, 'Which continents are completely surrounded by water?'),
       (100001, 'North America is _______ of the equator.'),
       (100001, 'California is a __________.'),
       (100001, 'What ocean borders California?'),
       (100001, 'The state capitol of CA is located in what city?'),
       (100001, 'Sacramento is a ___________.'),
       (100002, 'The leader for equal rights for all people and who volunteered for many organizations was _____________.'),
       (100002, '_________________ led peaceful marches and demonstrations to gain equal rights and treatment for African Americans.'),
       (100002, '___________________ was a Mexican American man who worked to improve conditions for farm workers.'),
       (100002, 'Differences among people which can include language, race, religion, or customs is called:'),
       (100002, 'This holiday is observed in November and honors all men and women who have served in the military.');

INSERT INTO answers (question_id, content, correct)
VALUES (100003, '7', false),
       (100003, '6', false),
       (100003, '8', true),

       (100004, '4', false),
       (100004, '6', true),
       (100004, '8', false),

       (100005, '65', false),
       (100005, '66', false),
       (100005, '67', true),

       (100006, '219', true),
       (100006, '217', false),
       (100006, '209', false),

       (100007, '9', false),
       (100007, '8', false),
       (100007, '7', true),

       (100008, '10', false ),
       (100008, '15', true),
       (100008, '25', false ),

       (100009, '299', false ),
       (100009, '314', false ),
       (100009, '304', true),

       (100010, '324', true),
       (100010, '316', false ),
       (100010, '246', false ),

       (100011, '55 cents', false),
       (100011, '65 cents', true),
       (100011, '45 cents', false),

       (100012, '8', false),
       (100012, '6', true),
       (100012, '4', false),

       (100013, '7', true),
       (100013, '6', false),
       (100013, '5', false),

       (100014, 'Europe and Asia', false),
       (100014, 'Australia and Antarctica', true),
       (100014, 'Africa and Asia', false),

       (100015, 'east', false),
       (100015, 'south', false),
       (100015, 'north', true),

       (100016, 'state', true),
       (100016, 'country', false),
       (100016, 'city', false),

       (100017, 'Atlantic Ocean', false),
       (100017, 'Pacific Ocean', true),
       (100017, 'Southern Ocean', false),

       (100018, 'Los angeles', false),
       (100018, 'Sacramento', true),
       (100018, 'San Francisco', false),

       (100019, 'city', true),
       (100019, 'state', false),
       (100019, 'neighborhood', false),

       (100020, 'Eleanor Roosevelt', true),
       (100020, 'Susan B. Anthony', false),
       (100020, 'Helen Keller', false),

       (100021, 'Eleanor Roosevelt', false),
       (100021, 'Martin Luther King, Jr.', true),
       (100021, 'Thurgood Marshall', false),

       (100022, 'Jackie Robinson', false),
       (100022, 'Cesar Chavez', true),
       (100022, 'Powhatan', false),

       (100023, 'traditions', false),
       (100023, 'customs', false),
       (100023, 'diversity', true),

       (100024, 'Veteran''s Day', true),
       (100024, 'Thanksgiving Day', false),
       (100024, 'Memorial Day', false);