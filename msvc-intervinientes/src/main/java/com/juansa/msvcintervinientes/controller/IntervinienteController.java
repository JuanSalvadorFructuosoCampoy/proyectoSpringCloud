package com.juansa.msvcintervinientes.controller;

import com.juansa.msvcintervinientes.dto.IntervinienteDTO;
import com.juansa.msvcintervinientes.models.entity.Interviniente;
import com.juansa.msvcintervinientes.services.IntervinienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class IntervinienteController {
    private IntervinienteService servicio;

    private static final String ERROR_URL = "Error: URL de la solicitud incorrecta";
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
        return ResponseEntity.status(HttpStatus.OK).body(opt.orElse(null));
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

    @PutMapping("/asignar-int/{procedimientoId}")
    public ResponseEntity<Object> asignarInterviniente(@Valid @RequestBody IntervinienteDTO intervinienteDTO, BindingResult result, @PathVariable Long procedimientoId){
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Interviniente> opt = servicio.porId(intervinienteDTO.getId());
        if(opt.isPresent()) {
            Interviniente interviniente = opt.get();
            interviniente.setProcedimientoId(procedimientoId);
            servicio.guardarEditar(interviniente);
            return ResponseEntity.status(HttpStatus.CREATED).body(interviniente);
        }
        return ResponseEntity.notFound().build();
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

    @GetMapping("/intervinientes-por-procedimiento/{id}")
    public ResponseEntity<Object> obtenerIntervinientesPorProcedimiento(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.listarPorProc(id));
    }

    private static ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->
                errores.put("error", "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/actualizacion")
    Interviniente actualizacion(@RequestBody Interviniente interviniente) {
        return servicio.guardarEditar(interviniente);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionDeJsonIncorrecto(HttpMessageNotReadableException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "El JSON proporcionado es incorrecto: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
