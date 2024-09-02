package com.lucasvila.tp_api.repositories;

import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {
    List<Jornada> findByEmpleadoIdAndFecha(Long id, LocalDate fecha);

    List<Jornada> findByEmpleadoIdAndFechaBetween(Long id, LocalDate startOfWeek, LocalDate endOfWeek);

    List<Jornada> findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(Long id, LocalDate startOfWeek, LocalDate endOfWeek, String turnoExtra);

    long countByConceptoLaboralAndFecha(ConceptoLaboral conceptoLaborable, LocalDate fecha);

    boolean existsByEmpleadoIdAndConceptoLaboralAndFecha(Long id, ConceptoLaboral conceptoLaborable, LocalDate fecha);

//    boolean existsByEmpleado(Empleado empleado);

    boolean existsByEmpleadoId(Long id);

    List<Jornada> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta);

    List<Jornada> findByFechaGreaterThanEqual(LocalDate fechaDesde);

    List<Jornada> findByFechaLessThanEqual(LocalDate fechaHasta);

    List<Jornada> findByEmpleadoNumeroDocumento(Integer numeroDocumento);

    List<Jornada> findByFechaBetweenAndEmpleadoNumeroDocumento(LocalDate fechaDesde, LocalDate fechaHasta, Integer numeroDocumento);
}
