package com.lucasvila.tp_api.services;

import com.lucasvila.tp_api.entities.ConceptoLaboral;

import java.util.List;

public interface ConceptoLaborableServices {

    List<ConceptoLaboral> obtenerConceptos(Long id, String nombre);


}
