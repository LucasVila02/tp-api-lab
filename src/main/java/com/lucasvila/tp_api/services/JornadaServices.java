package com.lucasvila.tp_api.services;


import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.Jornada;

import java.time.LocalDate;
import java.util.List;

public interface JornadaServices {

    JornadaResponseDTO addJornada(JornadaRequestDTO jornada);

    List<Jornada> findByFecha(LocalDate fechaDesde, LocalDate fechaHasta);

    List<Jornada> findByFechaDesde(LocalDate fechaDesde);

    List<Jornada> findByFechaHasta(LocalDate fechaHasta);

    List<Jornada> findByNroDocumento(Integer nroDocumento);

    List<Jornada> findByFechaAndNroDocumento(LocalDate fechaDesde, LocalDate fechaHasta, Integer nroDocumento);

    List<Jornada> findAll();
}
