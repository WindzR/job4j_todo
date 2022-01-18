INSERT INTO itemList (id, name, description, created, done, user_id) VALUES (1, 'Почистить зубы',  'Почистить зубы', '2021-12-22 08:05:00', true, 2);
INSERT INTO itemList (id, name, description, created, done, user_id) VALUES (2, 'Погулять с собакой',  'Погулять с собакой', '2021-12-22 08:10:00', true, 1);
INSERT INTO itemList (id, name, description, created, done, user_id) VALUES (3, 'Дописать админку',  'Дописать админку к проекту dream_job', '2021-12-22 08:12:00', false, 2);
INSERT INTO itemList (id, name, description, created, done, user_id) VALUES (4, 'Сходить в тренажерку', 'Сходить в тренажерку', '2021-12-22 08:15:00', false, 1);

INSERT INTO users (id, name, email, password) VALUES (1, 'Admin', 'admin@yandex.ru', '12345');
INSERT INTO users (id, name, email, password) VALUES (2, 'Дмитрий', 'dima87@mail.ru', 'qwerty');

INSERT INTO categories (id, name) VALUES (1, 'Работа');
INSERT INTO categories (id, name) VALUES (2, 'Развитие');
INSERT INTO categories (id, name) VALUES (3, 'Быт');
INSERT INTO categories (id, name) VALUES (4, 'Родные');

INSERT INTO itemlist_categories (item_id, categories_id) VALUES (1, 3);
INSERT INTO itemlist_categories (item_id, categories_id) VALUES (2, 4);
INSERT INTO itemlist_categories (item_id, categories_id) VALUES (3, 1);
INSERT INTO itemlist_categories (item_id, categories_id) VALUES (3, 2);
INSERT INTO itemlist_categories (item_id, categories_id) VALUES (4, 2);

