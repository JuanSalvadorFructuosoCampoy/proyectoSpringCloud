package com.juansa.msvcprocedimientos.services;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.models.Interviniente;
import com.juansa.msvcprocedimientos.models.entity.Procedimiento;

import java.util.List;
import java.util.Optional;

public interface ProcedimientoService {
    List<Procedimiento> listar();
    Optional<Procedimiento> porId(Long id);
    Optional<Procedimiento> porIdConIntervinientes(Long id);
    Optional<Procedimiento> porNumero(String numero);

    Optional<Interviniente> asignarInterviniente(Interviniente interviniente, Long procedimientoId);
    Procedimiento guardarNuevo(ProcedimientoDTO procedimientoDTO);
    Procedimiento guardarEditar(Procedimiento procedimiento);
    void eliminar(Long id);
}
