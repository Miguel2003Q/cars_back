-- =============================================
-- Script de creación de tablas para Cars App
-- Base de datos: MySQL
-- =============================================

-- Crear base de datos (opcional)
-- CREATE DATABASE IF NOT EXISTS cars_db;
-- USE cars_db;

-- =============================================
-- Tabla: users
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =============================================
-- Tabla: cars
-- =============================================
CREATE TABLE IF NOT EXISTS cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    color VARCHAR(30) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Clave foránea
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Índices para mejor rendimiento
    INDEX idx_user_id (user_id),
    INDEX idx_license_plate (license_plate),
    INDEX idx_brand (brand),
    INDEX idx_year (year)
);

-- =============================================
-- Comentarios de las tablas
-- =============================================
ALTER TABLE users COMMENT = 'Tabla de usuarios del sistema';
ALTER TABLE cars COMMENT = 'Tabla de autos registrados por los usuarios';

-- =============================================
-- Insertar datos de prueba
-- =============================================

-- Insertar usuarios de prueba
INSERT INTO users (username, email, password) VALUES
('admin', 'admin@cars.com', '$2a$10$LCHfeqFR.qh2rMqt.kEsdOVj5Jo/7wsuut3XvQN/IGhOOZUvDEwV6'),
('juan', 'juan@email.com', '$2a$10$LCHfeqFR.qh2rMqt.kEsdOVj5Jo/7wsuut3XvQN/IGhOOZUvDEwV6'),
('maria', 'maria@email.com', '$2a$10$LCHfeqFR.qh2rMqt.kEsdOVj5Jo/7wsuut3XvQN/IGhOOZUvDEwV6'),
('carlos', 'carlos@email.com', '$2a$10$LCHfeqFR.qh2rMqt.kEsdOVj5Jo/7wsuut3XvQN/IGhOOZUvDEwV6');

-- Insertar autos de prueba
INSERT INTO cars (brand, model, year, license_plate, color, user_id) VALUES
-- Autos del usuario admin (id=1)
('Toyota', 'Corolla', 2023, 'ABC123', 'Blanco', 1),
('Honda', 'Civic', 2022, 'DEF456', 'Azul', 1),
('Ford', 'Focus', 2021, 'GHI789', 'Rojo', 1),

-- Autos del usuario juan (id=2)
('Nissan', 'Sentra', 2023, 'JKL012', 'Negro', 2),
('Chevrolet', 'Cruze', 2022, 'MNO345', 'Gris', 2),

-- Autos del usuario maria (id=3)
('Volkswagen', 'Jetta', 2024, 'PQR678', 'Blanco', 3),
('Hyundai', 'Elantra', 2023, 'STU901', 'Azul', 3),
('Kia', 'Forte', 2022, 'VWX234', 'Rojo', 3),

-- Autos del usuario carlos (id=4)
('Mazda', 'Mazda3', 2023, 'YZA567', 'Negro', 4),
('Subaru', 'Impreza', 2024, 'BCD890', 'Azul', 4);

-- =============================================
-- Verificar creación de tablas y datos
-- =============================================
SHOW TABLES;
DESCRIBE users;
DESCRIBE cars;


