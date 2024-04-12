package com.juansa.msvcprocedimientos.repositories;

import com.juansa.msvcprocedimientos.entities.Procedimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface ProcedimientoRepository extends CrudRepository<Procedimiento, Long> {
    @Query(value = "SELECT USER()", nativeQuery = true)
    String getUsuario();

    @Query(value = "SELECT CURRENT_DATE()", nativeQuery = true)
    LocalDate getFechaActual();
}
