package com.juansa.msvcprocedimientos.services;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.entities.Procedimiento;

import java.util.List;
import java.util.Optional;

public interface ProcedimientoService {
    List<Procedimiento> listar();
    Optional<Procedimiento> porId(Long id);
    Procedimiento guardarNuevo(ProcedimientoDTO procedimientoDTO);
    Procedimiento guardarEditar(Procedimiento procedimiento);
    void eliminar(Long id);
}
