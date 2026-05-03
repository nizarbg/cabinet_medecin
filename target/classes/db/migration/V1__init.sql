-- V1__init.sql : Création du schéma de la base de données cabinet médical

CREATE TABLE patients (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cin             VARCHAR(8)   NOT NULL UNIQUE,
    nom             VARCHAR(100) NOT NULL,
    prenom          VARCHAR(100) NOT NULL,
    date_naissance  DATE         NOT NULL,
    telephone       VARCHAR(8),
    email           VARCHAR(150),
    antecedents     TEXT,
    date_creation   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medecins (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom                     VARCHAR(100) NOT NULL,
    prenom                  VARCHAR(100) NOT NULL,
    specialite              VARCHAR(100) NOT NULL,
    numero_ordre            VARCHAR(50)  NOT NULL UNIQUE,
    telephone               VARCHAR(8),
    email                   VARCHAR(150),
    horaires_disponibilite  VARCHAR(255),
    actif                   BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE rendez_vous (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id      BIGINT       NOT NULL,
    medecin_id      BIGINT       NOT NULL,
    date_heure      DATETIME     NOT NULL,
    duree_minutes   INT          NOT NULL DEFAULT 30,
    statut          VARCHAR(20)  NOT NULL DEFAULT 'PLANIFIE',
    motif           VARCHAR(255),
    CONSTRAINT fk_rdv_patient  FOREIGN KEY (patient_id)  REFERENCES patients(id),
    CONSTRAINT fk_rdv_medecin  FOREIGN KEY (medecin_id)  REFERENCES medecins(id)
);

CREATE TABLE ordonnances (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    rendez_vous_id  BIGINT  NOT NULL UNIQUE,
    date_emission   DATE    NOT NULL,
    observations    TEXT,
    CONSTRAINT fk_ordonnance_rdv FOREIGN KEY (rendez_vous_id) REFERENCES rendez_vous(id)
);

CREATE TABLE lignes_medicament (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordonnance_id   BIGINT       NOT NULL,
    nom_medicament  VARCHAR(200) NOT NULL,
    posologie       VARCHAR(255),
    duree           VARCHAR(100),
    CONSTRAINT fk_ligne_ordonnance FOREIGN KEY (ordonnance_id) REFERENCES ordonnances(id)
);

CREATE TABLE utilisateurs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    nom_complet VARCHAR(200) NOT NULL,
    actif       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE utilisateur_roles (
    utilisateur_id  BIGINT      NOT NULL,
    role            VARCHAR(50) NOT NULL,
    PRIMARY KEY (utilisateur_id, role),
    CONSTRAINT fk_role_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);
