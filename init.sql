-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS user_management;

-- Use the database
USE user_management;

-- Create roles table if it doesn't exist
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Insert default roles if they don't exist
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER');
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN');

-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create user_roles join table if it doesn't exist
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Create an admin user (password: admin123)
-- This will only insert if the admin user doesn't exist
INSERT IGNORE INTO users (username, email, password) 
VALUES (
    'admin', 
    'admin@example.com', 
    '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.'
);

-- Assign admin role to the admin user
SET @admin_id = (SELECT id FROM users WHERE username = 'admin' LIMIT 1);
SET @admin_role_id = (SELECT id FROM roles WHERE name = 'ROLE_ADMIN' LIMIT 1);

INSERT IGNORE INTO user_roles (user_id, role_id) 
SELECT @admin_id, @admin_role_id
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles 
    WHERE user_id = @admin_id AND role_id = @admin_role_id
) AND @admin_id IS NOT NULL AND @admin_role_id IS NOT NULL;
