package com.juansa.msvcprocedimientos.services;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.entities.Procedimiento;
import com.juansa.msvcprocedimientos.repositories.ProcedimientoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedimientoServiceImpl implements ProcedimientoService{

    private final ProcedimientoRepository repositorio;

    private final ModelMapper modelMapper;

    @Autowired
    public ProcedimientoServiceImpl(ProcedimientoRepository repositorio, ModelMapper modelMapper) {
        this.repositorio = repositorio;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Procedimiento> listar() {
        return (List<Procedimiento>) repositorio.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Procedimiento> porId(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public Procedimiento guardarNuevo(ProcedimientoDTO procedimientoDTO) {
        Procedimiento procedimiento = modelMapper.map(procedimientoDTO, Procedimiento.class);
        procedimiento.setUsuarioCreacion(repositorio.getUsuario());
        procedimiento.setFechaCreacion(LocalDate.now());
        return repositorio.save(procedimiento);
    }

    @Override
    @Transactional
    public Procedimiento guardarEditar(Procedimiento procedimiento) {
        Optional<Procedimiento> optionalProcedimiento = repositorio.findById(procedimiento.getId());
        if(optionalProcedimiento.isEmpty()){
            throw new IllegalArgumentException("Procedimiento no encontrado");
        }
        Procedimiento procedimientoDb = optionalProcedimiento.get();
        procedimientoDb.setFechaModificacion(LocalDate.now());
        procedimientoDb.setUsuarioModificacion(repositorio.getUsuario());
        return repositorio.save(procedimientoDb);
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}
