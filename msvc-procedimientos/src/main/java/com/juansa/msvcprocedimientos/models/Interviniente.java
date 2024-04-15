package com.juansa.msvcprocedimientos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interviniente {
    private Long id;

    private String nombre;

    private String tipoIntervencion;

    private Long procedimientoId;
}
