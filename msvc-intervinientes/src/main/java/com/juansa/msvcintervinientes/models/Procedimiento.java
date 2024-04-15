package com.juansa.msvcintervinientes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procedimiento {
    private Long id;
    private String numeroProcedimiento;
    private int anno;
}
