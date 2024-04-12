package com.juansa.msvcintervinientes.controller;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class IntervinienteController {
    private IntervinienteService servicio;


    @Autowired
    public IntervinienteController(IntervinienteService servicio, IntervinienteRepository repo){
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Interviniente>> listar(){
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviniente> porId(@PathVariable Long id){
        Optional<Interviniente> opt = servicio.porId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Interviniente> crear(@Valid @RequestBody IntervinienteDTO intervinienteDTO, BindingResult result) {
        if(result.hasErrors()){
           throw new ValidationException(result.toString());
        }
        Interviniente intervinienteDb = servicio.guardarNuevo(intervinienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(intervinienteDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interviniente> editar(@Valid @RequestBody IntervinienteDTO intervinienteDTO, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            throw new ValidationException(result.toString());
        }
        Optional<Interviniente> opt = servicio.porId(id);
        if(opt.isPresent()) {
            Interviniente intervinienteDb = opt.get();
            intervinienteDb.setNombre(intervinienteDTO.getNombre());
            intervinienteDb.setTipoIntervencion(intervinienteDTO.getTipoIntervencion());
            servicio.guardarEditar(intervinienteDb);
            return ResponseEntity.status(HttpStatus.CREATED).body(intervinienteDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Interviniente> eliminar(@PathVariable Long id) {
        Optional<Interviniente> opt = servicio.porId(id);
        if(opt.isPresent()) {
            servicio.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
    }
}
