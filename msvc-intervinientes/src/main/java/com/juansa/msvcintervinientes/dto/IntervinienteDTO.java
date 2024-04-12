package com.juansa.msvcintervinientes.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntervinienteDTO {

    private Long id;

    @NotEmpty(message = "no puede quedar vacío")
    private String nombre;

    @NotEmpty(message = "no puede quedar vacío")
    private String tipoIntervencion;
}
