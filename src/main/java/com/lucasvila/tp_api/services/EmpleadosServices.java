package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.entities.Empleado;



import java.util.List;
import java.util.Optional;


public interface EmpleadosServices {

    List<EmpleadoDTO> findAll();

    Optional<EmpleadoDTO> findById(Long id);

    EmpleadoDTO create(EmpleadoDTO empleadoDto);

    Optional<EmpleadoDTO> update(Long id, EmpleadoDTO empleadoDto);

    Optional<Empleado> delete(Long id);
}
