-- Crear la tabla de empleados si no existe
CREATE TABLE IF NOT EXISTS empleados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    fecha_vinculacion DATE NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    creado_por BIGINT,
    FOREIGN KEY (creado_por) REFERENCES users(id),
    UNIQUE KEY uk_numero_documento (numero_documento, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Agregar índice para búsquedas por número de documento
CREATE INDEX idx_empleado_numero_documento ON empleados(numero_documento) WHERE deleted = FALSE;

-- Agregar índice para búsquedas por fechas
CREATE INDEX idx_empleado_fechas ON empleados(fecha_nacimiento, fecha_vinculacion) WHERE deleted = FALSE;
