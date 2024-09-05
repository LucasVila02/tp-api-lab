package com.lucasvila.tp_api.services;


import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.Jornada;

import java.util.List;

public interface JornadaServices {

    JornadaResponseDTO addJornada(JornadaRequestDTO jornada);

    List<Jornada> findJornadas(String fechaDesde, String fechaHasta, String numeroDocumento);

}
