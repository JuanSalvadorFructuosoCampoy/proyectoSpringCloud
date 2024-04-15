package com.juansa.msvcintervinientes.services;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.models.entity.Interviniente;
import com.juansa.msvcintervinientes.exception.IntervinienteNoEncontradoException;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public List<Interviniente> listarPorProc(Long procedimientoId) {
        return repositorio.buscarPorProcedimientoId(procedimientoId);
    }

    @Override
    @Transactional
    public Interviniente guardarNuevo(IntervinienteDTO intervinienteDTO) {
        Interviniente interviniente = modelMapper.map(intervinienteDTO, Interviniente.class);
        interviniente.setUsuarioCreacion(repositorio.getUsuario());
        interviniente.setFechaCreacion(LocalDate.now());
        return repositorio.save(interviniente);
    }

    @Override
    @Transactional
    public Interviniente guardarEditar(Interviniente interviniente) {
        Optional<Interviniente> optionalInterviniente = repositorio.findById(interviniente.getId());
        if(optionalInterviniente.isEmpty()) {
            throw new IntervinienteNoEncontradoException();
        }
        Interviniente intervinienteDb = optionalInterviniente.get();
        intervinienteDb.setFechaModificacion(LocalDate.now());
        intervinienteDb.setUsuarioModificacion(repositorio.getUsuario());
        return repositorio.save(intervinienteDb);
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
