package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


public interface EmpleadosServices {

    List<Empleado> findAll();

    Optional<Empleado> findById(Long id);

    //DTO????

    Empleado create(EmpleadoDto empleado);

    Optional<Empleado> update(Long id, Empleado empleado);

    Optional<Empleado> delete(Long id);
}
