package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;

import java.util.List;

public interface ConceptoLaborableServices {

    List<ConceptoLaboralDTO> getConceptos(Long id, String nombre);
}
