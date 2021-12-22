CREATE TABLE IF NOT EXISTS itemList (
    id serial primary key,
    name text,
    description text,
    created timestamp,
    done boolean
);