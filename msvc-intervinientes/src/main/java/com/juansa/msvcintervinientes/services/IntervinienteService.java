package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.entities.Interviniente;

import java.util.List;
import java.util.Optional;

public interface IntervinienteService {
    List<Interviniente> listar();
    Optional<Interviniente> porId(Long id);

    Interviniente guardar(Interviniente interviniente);
    void eliminar(Long id);

}
