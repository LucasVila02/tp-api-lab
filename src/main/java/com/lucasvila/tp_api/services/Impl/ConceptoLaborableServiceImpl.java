package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.ConceptoLaboralDto;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.services.ConceptoLaborableServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConceptoLaborableServiceImpl implements ConceptoLaborableServices {

    @Autowired
    private ConceptoLaborableRepository repository;


    @Transactional(readOnly = true)
    public List<ConceptoLaboralDto> getConceptos(Long id, String nombre) {

        List<ConceptoLaboral> concepto;

        if (id != null && nombre != null) {
            concepto = repository.findByIdAndNombreContainingIgnoreCase(id, nombre);
        } else if (id != null) {
            concepto = repository.findById(id)
                    .map(Collections::singletonList)  // Si encuentra el concepto, lo envuelve en una lista
                    .orElseGet(Collections::emptyList);  // Si no lo encuentra, devuelve una lista vacía
        } else if (nombre != null) {
            concepto = repository.findByNombreContainingIgnoreCase(nombre);
        } else {
            concepto = repository.findAll();
        }
        return concepto.stream().map(ConceptoLaboral::toDTO).collect(Collectors.toList());
    }

}
