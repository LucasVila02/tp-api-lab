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
public class EmpleadosServiceImpl implements EmpleadosServices {

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

        return empleadosRepository.findById(id)
                .map(Empleado::toDTO)
                .or(() -> {
                    throw new NoEncontradoException(id, "empleado");
                });
    }

    @Transactional
    @Override
    public EmpleadoDTO create(EmpleadoDTO empleadoDto) {

        validacionEmpleado.validarEmailAndDocumentoEmpleadoCreate(empleadoDto);

        validacionEmpleado.validarFechasEmpleado(empleadoDto);

        Empleado empleadoGuardado = empleadosRepository.save(empleadoDto.toEntity());

        return empleadoGuardado.toDTO();
    }

    @Transactional
    @Override
    public Optional<EmpleadoDTO> update(Long id, EmpleadoDTO empleadoDto) {
        Empleado empleado = empleadosRepository.findById(id)
                .orElseThrow(()-> new NoEncontradoException(id, "empleado"));

            validacionEmpleado.validarEmailAndDocumentoEmpleadoUpdate( empleadoDto,id);
            validacionEmpleado.validarFechasEmpleado(empleadoDto);

            empleado.setNombre(empleadoDto.getNombre());
            empleado.setApellido(empleadoDto.getApellido());
            empleado.setEmail(empleadoDto.getEmail());
            empleado.setNroDocumento(empleadoDto.getNroDocumento());
            empleado.setFechaNacimiento(empleadoDto.getFechaNacimiento());
            empleado.setFechaIngreso(empleadoDto.getFechaIngreso());

            Empleado empleadoUpdate = empleadosRepository.save(empleado);

            return Optional.of(empleadoUpdate.toDTO());
    }

    @Transactional
    @Override
    public void delete(Long id) {

        Empleado empleado = empleadosRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException(id, "empleado"));

        if (jornadaRepository.existsByEmpleadoId(id)) {
            throw new BadRequestException("No es posible eliminar un empleado con jornadas asociadas.");
        }
        empleadosRepository.delete(empleado);
    }

}
