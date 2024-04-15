package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.models.entity.Interviniente;

import java.util.List;
import java.util.Optional;

public interface IntervinienteService {
    List<Interviniente> listar();
    Optional<Interviniente> porId(Long id);
    List<Interviniente> listarPorProc(Long procedimientoId);
    Interviniente guardarNuevo(IntervinienteDTO intervinienteDTO);
    Interviniente guardarEditar(Interviniente interviniente);
    void eliminar(Long id);

}
