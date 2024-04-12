package com.juansa.msvcprocedimientos.controller;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.entities.Procedimiento;
import com.juansa.msvcprocedimientos.services.ProcedimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProcedimientoController {
    private ProcedimientoService servicio;

    @Autowired
    public ProcedimientoController(ProcedimientoService servicio){
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Procedimiento>> listar() {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procedimiento> porId(@PathVariable Long id) {
        Optional<Procedimiento> opt = servicio.porId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody ProcedimientoDTO procedimientoDTO, BindingResult result) {
        if(result.hasErrors()){
            return validar(result);
        }
        Procedimiento procedimientoDb = servicio.guardarNuevo(procedimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimientoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar (@Valid @RequestBody ProcedimientoDTO procedimientoDTO, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Procedimiento> opt = servicio.porId(id);
        if(opt.isPresent()) {
            Procedimiento procedimientoDb = opt.get();
            procedimientoDb.setNombre(procedimientoDTO.getNombre());
            procedimientoDb.setNumeroProcedimiento(procedimientoDTO.getNumeroProcedimiento());
            procedimientoDb.setAnno(procedimientoDTO.getAnno());
            servicio.guardarEditar(procedimientoDb);
            return ResponseEntity.status(HttpStatus.CREATED).body(procedimientoDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Procedimiento> eliminar(@PathVariable Long id) {
        Optional<Procedimiento> opt = servicio.porId(id);
        if(opt.isPresent()) {
            servicio.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->
                errores.put("mensaje", "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

}
