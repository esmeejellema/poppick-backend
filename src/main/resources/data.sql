-- Voeg rollen toe als ze nog niet bestaan
INSERT INTO roles (name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'QUIZTAKER'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'QUIZTAKER');
