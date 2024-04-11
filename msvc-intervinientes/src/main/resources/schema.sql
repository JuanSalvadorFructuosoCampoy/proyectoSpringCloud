CREATE TABLE Intervinientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    tipo_intervencion VARCHAR(50),
    fecha_creacion DATE,
    usuario_creacion VARCHAR(255),
    fecha_modificacion DATE,
    usuario_modificacion VARCHAR(255)
);