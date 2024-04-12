package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IntervinienteServiceImpl implements IntervinienteService{

    private final IntervinienteRepository repositorio;

    private final ModelMapper modelMapper;

    @Autowired
    public IntervinienteServiceImpl(IntervinienteRepository repositorio, ModelMapper modelMapper) {
        this.repositorio = repositorio;
        this.modelMapper = modelMapper;
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
    public Interviniente guardarNuevo(IntervinienteDTO intervinienteDTO) {
        Interviniente interviniente = modelMapper.map(intervinienteDTO, Interviniente.class);
        interviniente.setUsuarioCreacion(repositorio.getUsuario());
        interviniente.setFechaCreacion(repositorio.getFechaActual());
        return repositorio.save(interviniente);
    }

    @Override
    @Transactional
    public Interviniente guardarEditar(IntervinienteDTO intervinienteDTO) {
        Interviniente interviniente = modelMapper.map(intervinienteDTO, Interviniente.class);
        interviniente.setFechaModificacion(repositorio.getFechaActual());
        interviniente.setUsuarioModificacion(repositorio.getUsuario());
        return repositorio.save(interviniente);
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
