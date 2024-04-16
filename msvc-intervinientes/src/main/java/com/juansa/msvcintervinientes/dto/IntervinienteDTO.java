package com.juansa.msvcintervinientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntervinienteDTO {

    private Long id;

    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private String nombre;

    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private String tipoIntervencion;

    private Long procedimientoId;
}
