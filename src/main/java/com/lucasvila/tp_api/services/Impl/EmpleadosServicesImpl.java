package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.exceptions.*;

import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.EmpleadosServices;
import com.lucasvila.tp_api.validation.ValidacionEmpleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadosServicesImpl implements EmpleadosServices {

    @Autowired
    private EmpleadosRepository empleadosRepository;

    @Autowired
    private JornadaRepository jornadaRepository;

    @Autowired
    private ValidacionEmpleado validacionEmpleado;

    @Transactional(readOnly = true)
    @Override
    public List<EmpleadoDTO> findAll() {
        List<Empleado> empleados = empleadosRepository.findAll();
        return empleados.stream()
                .map(Empleado::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EmpleadoDTO> findById(Long id) {
        Optional<Empleado> optionalEmpleados = empleadosRepository.findById(id);

        if (optionalEmpleados.isEmpty()) {
            throw new NoEncontradoException(id, "empleado");
        }

        return optionalEmpleados.map(Empleado::toDTO);
    }

    @Transactional
    @Override
    public EmpleadoDTO create(EmpleadoDTO empleadoDto) {

        validacionEmpleado.validarEmailAndDocumentoEmpleadoCreate(empleadoDto);

        validacionEmpleado.validarFechasEmpleado(empleadoDto);

        Empleado empleado = empleadoDto.toEntity(); // Convierte DTO a entidad
        Empleado empleadoGuardado = empleadosRepository.save(empleado);

        return empleadoGuardado.toDTO();
    }

    @Transactional
    @Override
    public Optional<EmpleadoDTO> update(Long id, EmpleadoDTO empleadoDto) {
        Optional<Empleado> optionalEmpleados = empleadosRepository.findById(id);

        if (optionalEmpleados.isPresent()){

            validacionEmpleado.validarEmailAndDocumentoEmpleadoUpdate( empleadoDto,id);

            validacionEmpleado.validarFechasEmpleado(empleadoDto);

            Empleado empleado = optionalEmpleados.get();
            empleado.setNombre(empleadoDto.getNombre());
            empleado.setApellido(empleadoDto.getApellido());
            empleado.setEmail(empleadoDto.getEmail());
            empleado.setNroDocumento(empleadoDto.getNumeroDocumento());
            empleado.setFechaNacimiento(empleadoDto.getFechaNacimiento());
            empleado.setFechaIngreso(empleadoDto.getFechaIngreso());

            Empleado empleadoUpdate = empleadosRepository.save(empleado);

            return Optional.of(empleadoUpdate.toDTO());
        }else {
            throw new NoEncontradoException(id, "empleado");
        }

    }

    @Transactional
    @Override
    public Optional<Empleado> delete(Long id) {
        Optional<Empleado> optionalEmpleado = empleadosRepository.findById(id);

        if (optionalEmpleado.isEmpty()) {
            // Lanza la excepción si el empleado no se encuentra
            throw new NoEncontradoException(id, "empleado");
        }
        Empleado empleado = optionalEmpleado.get();

        // Verifica si el empleado tiene jornadas asociadas
        if (jornadaRepository.existsByEmpleadoId(id)) {
            throw new BadRequestException("No es posible eliminar un empleado con jornadas asociadas.");
        }
        // Elimina el empleado si no tiene jornadas asociadas
        empleadosRepository.delete(empleado);
        return optionalEmpleado; // Retorna el empleado eliminado
    }

}
