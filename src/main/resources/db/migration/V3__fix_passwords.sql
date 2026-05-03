-- V3__fix_passwords.sql : Correction des hash BCrypt (mot de passe : "admin123")
UPDATE utilisateurs
SET password = '$2a$10$HbeyJAx6rDSyttPLJ1WjE.sw29u.jeRzMuKKnn/t0UF5xXHJ9gVxy'
WHERE username IN ('admin', 'medecin1', 'secretaire');
