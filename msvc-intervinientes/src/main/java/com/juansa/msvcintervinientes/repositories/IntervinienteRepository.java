package com.juansa.msvcintervinientes.repositories;

import com.juansa.msvcintervinientes.entities.Interviniente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface IntervinienteRepository extends CrudRepository<Interviniente, Long>{
    @Query(value = "SELECT USER()", nativeQuery = true)
    String getUsuario();

    @Query(value = "SELECT CURRENT_DATE()", nativeQuery = true)
    LocalDate getFechaActual();
}
