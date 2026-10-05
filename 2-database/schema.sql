DROP TABLE IF EXISTS note;

CREATE TABLE note (
    id   SERIAL PRIMARY KEY,
    text TEXT NOT NULL
);

INSERT INTO note (text) VALUES ('First note from the database');
