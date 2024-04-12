package com.juansa.msvcprocedimientos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcedimientoDTO {
    private Long id;

    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private String nombre;

    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private int numeroProcedimiento;

    @NotBlank(message = "no puede quedar vacío ni con espacios en blanco")
    private int anno;
}
