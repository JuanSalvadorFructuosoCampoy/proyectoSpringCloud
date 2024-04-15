package com.juansa.msvcprocedimientos.models;

import lombok.Data;

import java.time.LocalDate;


@Data
public class Interviniente {
    private Long id;

    private String nombre;

    private String tipoIntervencion;

    private LocalDate fechaCreacion;

    private String usuarioCreacion;

    private LocalDate fechaModificacion;

    private String usuarioModificacion;
}
