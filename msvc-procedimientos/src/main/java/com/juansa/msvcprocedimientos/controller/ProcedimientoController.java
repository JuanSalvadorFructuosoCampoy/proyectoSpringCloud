package com.juansa.msvcprocedimientos.controller;

import com.juansa.msvcprocedimientos.dto.ProcedimientoDTO;
import com.juansa.msvcprocedimientos.models.Interviniente;
import com.juansa.msvcprocedimientos.models.entity.Procedimiento;
import com.juansa.msvcprocedimientos.exception.NumeroDuplicadoException;
import com.juansa.msvcprocedimientos.services.ProcedimientoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProcedimientoController {
    private ProcedimientoService servicio;
    private static final String ERROR_URL = "Error: URL de la solicitud incorrecta";
    private static final String ERROR_MESSAGE = "error";
    @Autowired
    public ProcedimientoController(ProcedimientoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<Procedimiento>> listar() {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procedimiento> porId(@PathVariable Long id) {
        Optional<Procedimiento> opt = servicio.porIdConIntervinientes(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody ProcedimientoDTO procedimientoDTO, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Procedimiento procedimientoDb = servicio.guardarNuevo(procedimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimientoDb);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> urlPostIncorrecta(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editar(@Valid @RequestBody ProcedimientoDTO procedimientoDTO, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<Procedimiento> opt = servicio.porId(id);
        if (opt.isPresent()) {
            Procedimiento procedimientoDb = opt.get();

            //En caso de que el número de procedimiento ya exista en la base de datos
            if (!procedimientoDTO.getNumeroProcedimiento().equalsIgnoreCase(procedimientoDb.getNumeroProcedimiento()) &&
                    servicio.porNumero(procedimientoDTO.getNumeroProcedimiento()).isPresent()) {
                throw new NumeroDuplicadoException();
            }
            procedimientoDb.setNumeroProcedimiento(procedimientoDTO.getNumeroProcedimiento());
            procedimientoDb.setAnno(procedimientoDTO.getAnno());
            servicio.guardarEditar(procedimientoDb);
            return ResponseEntity.status(HttpStatus.CREATED).body(procedimientoDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<Object> urlPutIncorrecta() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
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

    @DeleteMapping
    public ResponseEntity<Object> urlDeleteIncorrecta(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_URL);
    }

    @PutMapping("/asignar-int/{procedimientoId}")
    public ResponseEntity<Object> asignarInterviniente(@RequestParam Long interId, @PathVariable Long procedimientoId) {
        Optional<Interviniente> opt = servicio.obtenerInterviniente(interId);
        if(opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje","No existe el interviniente indicado."));
        }
        try{
            opt = servicio.asignarInterviniente(opt.get(), procedimientoId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje","No existe el interviniente por el id o error en la comunicación: " +
                            e.getMessage()));
        }
        if(opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-int/{procedimientoId}")
    public ResponseEntity<Object> crearInterviniente(@RequestBody Interviniente interviniente, @PathVariable Long procedimientoId) {
        Optional<Interviniente> opt;
        try{
            opt = servicio.crearInterviniente(interviniente, procedimientoId);
        }catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje","No se pudo crear el interviniente o error en la comunicación: "
                            + e.getMessage()));
        }
        if(opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Object> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->
                errores.put(ERROR_MESSAGE, "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(NumeroDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcion(NumeroDuplicadoException e) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionDeJsonIncorrecto(HttpMessageNotReadableException e) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_MESSAGE, "El JSON proporcionado es incorrecto: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
