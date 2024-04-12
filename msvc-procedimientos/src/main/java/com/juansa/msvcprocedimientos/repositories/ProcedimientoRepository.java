package com.juansa.msvcprocedimientos.repositories;

import com.juansa.msvcprocedimientos.entities.Procedimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProcedimientoRepository extends CrudRepository<Procedimiento, Long> {
    @Query(value = "SELECT USER()", nativeQuery = true)
    String getUsuario();

    @Query("SELECT p FROM Procedimiento p WHERE p.numeroProcedimiento = ?1")
    Optional<Procedimiento> getProcedimientoPorNumero(String numeroProcedimiento);
}
