package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


public interface EmpleadosServices {

    List<EmpleadoDto> findAll();

    Optional<Empleado> findById(Long id);

    EmpleadoDto create(EmpleadoDto empleadoDto);

    Optional<EmpleadoDto> update(Long id, EmpleadoDto empleadoDto);

    Optional<Empleado> delete(Long id);
}
