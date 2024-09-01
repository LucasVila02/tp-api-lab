package com.lucasvila.tp_api.repositories;

import com.lucasvila.tp_api.entities.ConceptoLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConceptoLaborableRepository extends JpaRepository<ConceptoLaboral, Long> {

    List<ConceptoLaboral> findByNombreContainingIgnoreCase(String nombre);

    List<ConceptoLaboral> findByIdAndNombreContainingIgnoreCase(Long id, String nombre);
}
