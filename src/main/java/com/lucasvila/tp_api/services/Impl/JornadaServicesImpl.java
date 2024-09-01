package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.JornadaDto;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.JornadaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class JornadaServicesImpl implements JornadaServices {

    @Autowired
    private JornadaRepository repository;
    @Autowired
    private ConceptoLaborableRepository conceptoLaboralRepository;

    @Autowired
    private EmpleadosRepository empleadoRepository;


    public JornadaDto toDTO(Jornada jornada) {
        JornadaDto dto = new JornadaDto();
        dto.setId(jornada.getId());
        dto.setIdConcepto(jornada.getIdConcepto().getId());
        dto.setIdEmpleado(jornada.getIdEmpleado().getId());
        dto.setFecha(jornada.getFecha());
        dto.setHorasTrabajadas(jornada.getHorasTrabajadas());
        return dto;
    }

    @Transactional
    @Override
    public JornadaDto addJornada(JornadaDto jornadaDto) {

        ConceptoLaboral concepto = conceptoLaboralRepository.findById(jornadaDto.getIdConcepto())
                .orElseThrow(() -> new ExpressionException("Concepto no encontrado"));

        Empleado empleado = empleadoRepository.findById(jornadaDto.getIdEmpleado())
                .orElseThrow(() -> new ExpressionException("Empleado no encontrado"));

        Jornada jornada = new Jornada();
        jornada.setIdConcepto(concepto);
        jornada.setIdEmpleado(empleado);
        jornada.setFecha(jornadaDto.getFecha());
        jornada.setHorasTrabajadas(jornadaDto.getHorasTrabajadas());
        Jornada jornadaGuardada = repository.save(jornada);
        return toDTO(jornadaGuardada);
    }
}
