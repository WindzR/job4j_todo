CREATE TABLE IF NOT EXISTS itemList (
    id serial primary key,
    name text,
    description text,
    created timestamp,
    done boolean,
    user_id int NOT NULL REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    name text,
    email text UNIQUE,
    password text
);

CREATE TABLE IF NOT EXISTS categories (
    id serial primary key,
    name text
);