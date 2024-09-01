package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.services.ConceptoLaborableServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ConceptoLaborableServiceImpl implements ConceptoLaborableServices {

    @Autowired
    private ConceptoLaborableRepository repository;


    public List<ConceptoLaboral> obtenerConceptos(Long id, String nombre) {
        if (id != null && nombre != null) {
            return repository.findByIdAndNombreContainingIgnoreCase(id, nombre);
        } else if (id != null) {
            return repository.findById(id)
                    .map(Collections::singletonList)  // Si encuentra el concepto, lo envuelve en una lista
                    .orElseGet(Collections::emptyList);  // Si no lo encuentra, devuelve una lista vacía
        } else if (nombre != null) {
            return repository.findByNombreContainingIgnoreCase(nombre);
        } else {
            return repository.findAll();
        }
    }


}
