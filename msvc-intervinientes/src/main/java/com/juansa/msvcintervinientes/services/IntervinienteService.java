package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.entities.Interviniente;

import java.util.List;
import java.util.Optional;

public interface IntervinienteService {
    List<Interviniente> listar();
    Optional<Interviniente> porId(Long id);
    Interviniente guardarNuevo(IntervinienteDTO intervinienteDTO);
    Interviniente guardarEditar(Interviniente interviniente);
    void eliminar(Long id);

}
