ALTER TABLE shelter
ADD COLUMN type TEXT NOT NULL,
ADD COLUMN instruction TEXT;

ALTER TABLE shelter
DROP
COLUMN address;

INSERT INTO shelter(name, description, type, instruction)
VALUES ('4 лапы', 'песики', 'DOG', 'hurdur2'), ('усы и хвост', 'котики', 'CAT', 'hurdur1');

