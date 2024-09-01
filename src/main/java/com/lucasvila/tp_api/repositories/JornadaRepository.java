package com.lucasvila.tp_api.repositories;

import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

//    boolean existsByEmpleado(Empleado empleado);
}
