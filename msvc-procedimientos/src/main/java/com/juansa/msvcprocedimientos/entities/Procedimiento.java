package com.juansa.msvcprocedimientos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "procedimientos")
public class Procedimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private String nombre;

    @Column (name = "numero_procedimiento")
    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private String numeroProcedimiento;

    @Column(name = "anno")
    @NotNull(message = "no puede quedar vacío ni con espacios en blanco")
    private int anno;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
}
