CREATE TABLE "User"
(
    id           VARCHAR(255) PRIMARY KEY,
    passwordHash VARCHAR(255) NOT NULL,
    name         VARCHAR(255)
);

CREATE TABLE Performer
(
    id VARCHAR(255) PRIMARY KEY REFERENCES "User" (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Album
(
    name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE Track
(
    id               INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    performer        VARCHAR(255) NOT NULL REFERENCES Performer (id) ON DELETE CASCADE ON UPDATE CASCADE,
    duration         INT          NOT NULL,
    offlineAvailable BOOLEAN      NOT NULL,
    album            VARCHAR(255) REFERENCES Album (name) ON DELETE CASCADE ON UPDATE CASCADE,
    playCount        INT,
    publicationDate  TIMESTAMP WITH TIME ZONE,
    description      VARCHAR(1024)
);

CREATE TABLE Playlist
(
    id    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    owner VARCHAR(255) REFERENCES "User" (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PlaylistTrack
(
    playlist INT NOT NULL REFERENCES Playlist (id) ON DELETE CASCADE ON UPDATE CASCADE,
    track    INT NOT NULL REFERENCES Track (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (playlist, track)
);

INSERT INTO "User" (id, passwordHash, name)
-- passwordHash is 'password'
VALUES ('john',
        '$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk',
        'John Doe'),
       ('smallpools',
        '$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk',
        'Smallpools'),
       ('kurzgesagt',
        '$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk',
        'Kurzgesagt - In a Nutshell'),
       ('exurb1a',
        '$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk',
        'Exurb1a');

INSERT INTO Performer (id)
VALUES ('smallpools'),
       ('kurzgesagt'),
       ('exurb1a');

INSERT INTO Album (name)
VALUES ('Lovetap!');

INSERT INTO Track (title, performer, duration, offlineAvailable, album, playCount, publicationDate, description)
VALUES ('American Love', 'smallpools', 179, true, 'Lovetap!', null, null, null),
       ('The Egg - A Short Story', 'kurzgesagt', 474, false, null, 28613533, '2019-09-01',
        'The Egg. Story by Andy Weir, Animated by Kurzgesagt');

INSERT INTO Playlist (name, owner)
VALUES ('Empty', 'john'),
       ('Favorites', 'john');

INSERT INTO PlaylistTrack (playlist, track)
-- Playlist 'Favorites' has the 'id' 2
VALUES (2, 1),
       (2, 2);
