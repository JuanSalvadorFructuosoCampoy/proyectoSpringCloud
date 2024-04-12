package com.juansa.msvcintervinientes.controller;

import com.juansa.msvcintervinientes.entities.Interviniente;
import com.juansa.msvcintervinientes.repositories.IntervinienteRepository;
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
        if(opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Interviniente interviniente, BindingResult result) {
        if(result.hasErrors()){
            return validar(result);
        }
        Interviniente intervinienteDb = servicio.guardar(interviniente);
        return ResponseEntity.status(HttpStatus.CREATED).body(intervinienteDb);
    }

    //Método de validación de errores
    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
