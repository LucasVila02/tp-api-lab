package com.lucasvila.tp_api.repositories;

import com.lucasvila.tp_api.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadosRepository extends JpaRepository<Empleado, Long> {

    boolean existsByNroDocumento(Integer nro);

    boolean existsByEmail(String email);

    boolean existsByNroDocumentoAndIdNot(Integer nroDocumento, Long id);

    boolean existsByEmailAndIdNot(String nroDocumento, Long id);


}
