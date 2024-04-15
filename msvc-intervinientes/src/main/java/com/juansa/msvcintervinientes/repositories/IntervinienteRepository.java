package com.juansa.msvcintervinientes.repositories;

import com.juansa.msvcintervinientes.models.entity.Interviniente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IntervinienteRepository extends CrudRepository<Interviniente, Long>{
    @Query(value = "SELECT USER()", nativeQuery = true)
    String getUsuario();

    @Query("SELECT i FROM Interviniente i WHERE i.procedimientoId = ?1")
    List<Interviniente> buscarPorProcedimientoId(Long procedimientoId);
}
