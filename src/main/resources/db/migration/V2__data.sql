-- V2__data.sql : Données initiales — utilisateurs, médecins et patients de démonstration

-- Mot de passe : "admin123" (BCrypt)
INSERT INTO utilisateurs (username, password, nom_complet, actif) VALUES
('admin',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrateur', TRUE),
('medecin1',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Ben Ali Sami',   TRUE),
('secretaire', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hind Trabelsi',      TRUE);

INSERT INTO utilisateur_roles (utilisateur_id, role) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MEDECIN'),
(3, 'ROLE_SECRETAIRE');

-- Médecins
INSERT INTO medecins (nom, prenom, specialite, numero_ordre, telephone, email, horaires_disponibilite, actif) VALUES
('Ben Ali',   'Sami',   'Cardiologie',       'MED-001', '71234567', 'sami.benali@cabinet.tn',   'Lun-Ven 08:00-16:00', TRUE),
('Triki',     'Leila',  'Pédiatrie',         'MED-002', '72345678', 'leila.triki@cabinet.tn',   'Lun-Sam 09:00-17:00', TRUE),
('Mansouri',  'Karim',  'Médecine Générale', 'MED-003', '73456789', 'karim.mansouri@cabinet.tn','Lun-Ven 08:00-18:00', TRUE);

-- Patients
INSERT INTO patients (cin, nom, prenom, date_naissance, telephone, email, antecedents, date_creation) VALUES
('12345678', 'Hammami',  'Mohamed', '1980-05-15', '20123456', 'med.hammami@email.tn',  'Hypertension, Diabète type 2', NOW()),
('23456789', 'Chaabane', 'Fatma',   '1992-08-22', '21234567', 'fatma.ch@email.tn',     'Aucun', NOW()),
('34567890', 'Belhaj',   'Youssef', '1975-12-03', '22345678', 'youssef.b@email.tn',   'Asthme', NOW()),
('45678901', 'Rekik',    'Ines',    '2001-03-18', '23456789', 'ines.rekik@email.tn',  'Allergie pénicilline', NOW()),
('56789012', 'Jlassi',   'Tarek',   '1968-07-30', '24567890', 'tarek.j@email.tn',     'Antécédents cardiaques', NOW());
