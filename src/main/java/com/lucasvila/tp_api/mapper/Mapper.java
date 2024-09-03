package com.lucasvila.tp_api.mapper;

import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;

public class Mapper {

    public ConceptoLaboralDTO toConceptoLaboralDTO(ConceptoLaboral conceptoLaboral) {
        ConceptoLaboralDTO dto = new ConceptoLaboralDTO();
        dto.setNombre(conceptoLaboral.getNombre());
        dto.setLaborable(conceptoLaboral.isLaborable());
        dto.setHsMinimo(conceptoLaboral.getHsMinimo() != null ? conceptoLaboral.getHsMinimo() : 0);
        dto.setHsMaximo(conceptoLaboral.getHsMaximo() != null ? conceptoLaboral.getHsMaximo() : 0);
        dto.setId(conceptoLaboral.getId());
        return dto;
    }

    // Mapper para Jornada
    public JornadaResponseDTO toJornadaResponseDTO(Jornada jornada) {
        JornadaResponseDTO dto = new JornadaResponseDTO();
        dto.setNroDocumento(jornada.getEmpleado().getNumeroDocumento());
        dto.setNombreCompleto(jornada.getEmpleado().getNombre() + " " + jornada.getEmpleado().getApellido());
        dto.setFecha(jornada.getFecha());
        dto.setConcepto(jornada.getConceptoLaboral().getNombre());
        dto.setHsTrabajadas(jornada.getHorasTrabajadas());
        return dto;
    }

    // Mapper para Empleado
    public EmpleadoDTO toEmpleadoDTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setEmail(empleado.getEmail());
        dto.setFechaIngreso(empleado.getFechaIngreso());
        dto.setNumeroDocumento(empleado.getNumeroDocumento());
        dto.setFechaNacimiento(empleado.getFechaNacimiento());
        dto.setFechaCreacion(empleado.getFechaCreacion());
        return dto;
    }

}
