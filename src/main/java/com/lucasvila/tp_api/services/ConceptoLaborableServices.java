package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.dto.ConceptoLaboralDto;

import java.util.List;

public interface ConceptoLaborableServices {

    List<ConceptoLaboralDto> getConceptos(Long id, String nombre);


}
