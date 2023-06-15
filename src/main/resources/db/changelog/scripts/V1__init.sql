CREATE TABLE shelter (id SERIAL PRIMARY KEY, name TEXT, address TEXT, description TEXT);
CREATE TABLE usersShelter (id SERIAL PRIMARY KEY, name TEXT, email TEXT, phone INTEGER, chatId BIGINT);