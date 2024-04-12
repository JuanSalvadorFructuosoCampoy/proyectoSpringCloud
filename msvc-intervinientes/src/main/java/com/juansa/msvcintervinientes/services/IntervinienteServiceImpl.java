package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IntervinienteServiceImpl implements IntervinienteService{

    private final IntervinienteRepository repositorio;

    @Autowired
    public IntervinienteServiceImpl(IntervinienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interviniente> listar() {
        return (List<Interviniente>) repositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Interviniente> porId(Long id) {
        return repositorio.findById(id);
    }

    @Override
    @Transactional
    public Interviniente guardar(Interviniente interviniente) {
        return repositorio.save(interviniente);
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
