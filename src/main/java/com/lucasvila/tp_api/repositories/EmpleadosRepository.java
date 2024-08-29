package com.lucasvila.tp_api.repositories;

import com.lucasvila.tp_api.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadosRepository extends JpaRepository<Empleado, Long> {

    boolean existsByNumeroDocumento(Integer nro);

    boolean existsByEmail(String email);

}
