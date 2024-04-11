CREATE TABLE Procedimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    anno INT,
    fecha_creacion DATE,
    usuario_creacion VARCHAR(255),
    fecha_modificacion DATE,
    usuario_modificacion VARCHAR(255)
);