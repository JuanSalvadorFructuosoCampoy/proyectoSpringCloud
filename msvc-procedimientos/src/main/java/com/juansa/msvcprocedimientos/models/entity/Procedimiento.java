package com.juansa.msvcprocedimientos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.juansa.msvcprocedimientos.models.Interviniente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "procedimientos")
public class Procedimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "numero_procedimiento", unique = true)
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

    @Transient
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "procedimiento_id")
    private List<Interviniente> intervinientes;

    public Procedimiento() {
        this.intervinientes = new ArrayList<>();
    }
}
