package com.juansa.msvcprocedimientos.services;

import com.juansa.msvcprocedimientos.clients.IntervinienteClientRest;
import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.models.Interviniente;
import com.juansa.msvcprocedimientos.models.entity.Procedimiento;
import com.juansa.msvcprocedimientos.exception.NumeroDuplicadoException;
import com.juansa.msvcprocedimientos.exception.ProcedimientoNoEncontradoException;
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

    private final IntervinienteClientRest cliente;

    @Autowired
    public ProcedimientoServiceImpl(ProcedimientoRepository repositorio, ModelMapper modelMapper, IntervinienteClientRest cliente) {
        this.repositorio = repositorio;
        this.modelMapper = modelMapper;
        this.cliente = cliente;
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
    @Transactional(readOnly = true)
    public Optional<Procedimiento> porIdConIntervinientes(Long id) {
        Optional<Procedimiento> o = repositorio.findById(id);
        if (o.isPresent()) {
            Procedimiento procedimiento = o.get();
                List<Interviniente> intervinientes = cliente.obtenerIntervinientesPorProcedimiento(procedimiento.getId());
                procedimiento.setIntervinientes(intervinientes);
            return Optional.of(procedimiento);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Procedimiento> porNumero(String numero) {
        return repositorio.getProcedimientoPorNumero(numero);
    }


    @Override
    @Transactional
    public Optional<Interviniente> asignarInterviniente(Interviniente interviniente, Long procedimientoId) {
        Optional<Procedimiento> opt = repositorio.findById(procedimientoId);
        if(opt.isPresent()) {
                Optional<Interviniente> intervinienteDb = cliente.porId(interviniente.getId());
                if (intervinienteDb.isEmpty()) {
                    return Optional.empty();
                }
                Procedimiento procedimiento = opt.get();
                procedimiento.getIntervinientes().add(intervinienteDb.get());
                procedimiento.setFechaModificacion(LocalDate.now());
                procedimiento.setUsuarioModificacion(repositorio.getUsuario());
                intervinienteDb.get().setProcedimientoId(procedimientoId);
                repositorio.save(procedimiento);
                cliente.actualizacion(intervinienteDb.get());
                return Optional.of(intervinienteDb.get());
       }
       return Optional.empty();
    }

    @Override
    public Optional<Interviniente> aniadirInterviniente(Interviniente interviniente, Long procedimientoId) {
        Optional<Procedimiento> opt = repositorio.findById(procedimientoId);
        if(opt.isPresent()) {
            Procedimiento procedimiento = opt.get();
            Optional<Interviniente> intervinienteDb = cliente.porId(interviniente.getId());
            if(intervinienteDb.isPresent()) {
                procedimiento.getIntervinientes().add(intervinienteDb.get());
                procedimiento.setFechaModificacion(LocalDate.now());
                procedimiento.setUsuarioModificacion(repositorio.getUsuario());
                interviniente.setProcedimientoId(procedimientoId);
                repositorio.save(procedimiento);
                cliente.aniadirInterviniente(interviniente, procedimientoId);
                return Optional.of(interviniente);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Procedimiento guardarNuevo(ProcedimientoDTO procedimientoDTO) {
            Procedimiento procedimiento = modelMapper.map(procedimientoDTO, Procedimiento.class);
            Optional<Procedimiento> numeroProc = repositorio.getProcedimientoPorNumero(procedimientoDTO.getNumeroProcedimiento());
            if (numeroProc.isPresent()) {
                throw new NumeroDuplicadoException();
            }
            procedimiento.setUsuarioCreacion(repositorio.getUsuario());
            procedimiento.setFechaCreacion(LocalDate.now());
            return repositorio.save(procedimiento);
    }

    @Override
    @Transactional
    public Procedimiento guardarEditar(Procedimiento procedimiento) {
        Optional<Procedimiento> optionalProcedimiento = repositorio.findById(procedimiento.getId());
        if(optionalProcedimiento.isEmpty()){
            throw new ProcedimientoNoEncontradoException();
        }
        Procedimiento procedimientoDb = optionalProcedimiento.get();
        Optional<Procedimiento> numeroProc = repositorio.getProcedimientoPorNumero(procedimiento.getNumeroProcedimiento());
        if(numeroProc.isPresent() && !numeroProc.get().getId().equals(procedimiento.getId())){
            throw new NumeroDuplicadoException();
        }
        procedimientoDb.setFechaModificacion(LocalDate.now());
        procedimientoDb.setUsuarioModificacion(repositorio.getUsuario());
        return repositorio.save(procedimientoDb);
    }

    @Override
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

    @Override
    public Optional<Interviniente> obtenerInterviniente(Long id) {
        return cliente.porId(id);
    }
}
