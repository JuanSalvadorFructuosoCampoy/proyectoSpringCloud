package com.juansa.msvcintervinientes.controller;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.services.IntervinienteService;
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
public class IntervinienteController {
    private IntervinienteService servicio;

    private final static String ERROR_URL = "Error: URL de la solicitud incorrecta";
    @Autowired
    public IntervinienteController(IntervinienteService servicio){
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
    public ResponseEntity<Object> crear(@Valid @RequestBody IntervinienteDTO intervinienteDTO, BindingResult result) {
        if(result.hasErrors()){
            return validar(result);
        }
        Interviniente intervinienteDb = servicio.guardarNuevo(intervinienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(intervinienteDb);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> urlPostIncorrecta(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@Valid @RequestBody IntervinienteDTO intervinienteDTO, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            return validar(result);
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

    @PutMapping
    public ResponseEntity<Object> urlPutIncorrecta(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
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

    @DeleteMapping
    public ResponseEntity<Object> urlDeleteIncorrecta(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
    }

    private static ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->
                errores.put("error", "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }
}
