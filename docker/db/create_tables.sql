CREATE TABLE "User"
(
    id           VARCHAR(255) PRIMARY KEY,
    passwordHash VARCHAR(255) NOT NULL,
    name         VARCHAR(255)
);

INSERT INTO "User"
-- passwordHash is 'password'
VALUES ('john', '$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk', 'John Doe');
