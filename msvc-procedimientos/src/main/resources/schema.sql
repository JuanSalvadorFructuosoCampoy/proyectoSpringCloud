CREATE TABLE IF NOT EXISTS Procedimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_procedimiento VARCHAR(255),
    anno INT,
    fecha_creacion DATE,
    usuario_creacion VARCHAR(255),
    fecha_modificacion DATE,
    usuario_modificacion VARCHAR(255)
);