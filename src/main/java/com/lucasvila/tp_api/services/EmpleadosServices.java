package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.EmpleadoDTO;


import java.util.List;
import java.util.Optional;


public interface EmpleadosServices {

    List<EmpleadoDTO> findAll();

    Optional<EmpleadoDTO> findById(Long id);

    EmpleadoDTO create(EmpleadoDTO empleadoDto);

    Optional<EmpleadoDTO> update(Long id, EmpleadoDTO empleadoDto);

    void delete(Long id);
}
