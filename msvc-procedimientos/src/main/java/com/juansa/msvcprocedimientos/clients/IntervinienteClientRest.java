package com.juansa.msvcprocedimientos.clients;

import com.juansa.msvcprocedimientos.models.Interviniente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-intervinientes", url = "localhost:8002")
public interface IntervinienteClientRest {
    @GetMapping("/{id}")
    Interviniente porId(@PathVariable Long id);

    @PostMapping("/")
    Interviniente crear(@RequestBody Interviniente interviniente);

    @GetMapping("/intervinientes-por-procedimiento/{id}")
    List<Interviniente> obtenerIntervinientesPorProcedimiento(@PathVariable Long id);

    @PutMapping("/aniadir-interviniente/{procedimientoId}")
    Interviniente aniadirInterviniente(@RequestBody Interviniente interviniente, @PathVariable Long procedimientoId);
}
