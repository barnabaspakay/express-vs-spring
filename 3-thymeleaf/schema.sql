DROP TABLE IF EXISTS note;

CREATE TABLE note (
    id      SERIAL PRIMARY KEY,
    text    TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT now()
);

INSERT INTO note (text) VALUES ('First note from the database');
