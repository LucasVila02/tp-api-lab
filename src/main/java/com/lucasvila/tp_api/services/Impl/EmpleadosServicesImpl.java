package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.exceptions.*;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.EmpleadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpleadosServicesImpl implements EmpleadosServices {

    @Autowired
    private EmpleadosRepository repository;

    @Autowired
    private JornadaRepository jornadaRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EmpleadoDto> findAll() {
        List<Empleado> empleados = repository.findAll();
        return empleados.stream()
                .map(Empleado::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Empleado> findById(Long id) {
        Optional<Empleado> optionalEmpleados = repository.findById(id);

        if (optionalEmpleados.isEmpty()) {
            throw new NoEncontradoException(id, "empleado");
        }
        //        Empleado empleado = optionalEmpleados.get();
        //Revisar DTO
        return optionalEmpleados;
    }

    @Transactional
    @Override
    public EmpleadoDto create(EmpleadoDto empleadoDto) {

        // Validación de documento duplicado
        if (repository.existsByNumeroDocumento(empleadoDto.getNumeroDocumento())) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el documento ingresado.");
        }
        // Validación de email duplicado
        if (repository.existsByEmail(empleadoDto.getEmail())) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el email ingresado.");
        }

        validarFechasEmpleado(empleadoDto);

        Empleado empleado = empleadoDto.toEntity(); // Convierte DTO a entidad
        Empleado empleadoGuardado = repository.save(empleado);

        return empleadoGuardado.toDTO();
    }

    @Transactional
    @Override
    public Optional<EmpleadoDto> update(Long id, EmpleadoDto empleadoDto) {
        Optional<Empleado> optionalEmpleados = repository.findById(id);

        if (optionalEmpleados.isPresent()){

        // Validación de documento duplicado
        if (repository.existsByNumeroDocumentoAndIdNot(empleadoDto.getNumeroDocumento(), id) ) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el documento ingresado.");
        }
        // Validación de email duplicado
        if (repository.existsByEmailAndIdNot(empleadoDto.getEmail(), id)) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el email ingresado.");
        }

        validarFechasEmpleado(empleadoDto);

        Empleado empleado = optionalEmpleados.get();
        empleado.setNombre(empleadoDto.getNombre());
        empleado.setApellido(empleadoDto.getApellido());
        empleado.setEmail(empleadoDto.getEmail());
        empleado.setNumeroDocumento(empleadoDto.getNumeroDocumento());
        empleado.setFechaNacimiento(empleadoDto.getFechaNacimiento());
        empleado.setFechaIngreso(empleadoDto.getFechaIngreso());

        Empleado empleadoUpdate = repository.save(empleado);

        return Optional.of(empleadoUpdate.toDTO());
        }else {
            throw new NoEncontradoException(id, "empleado");
        }

    }


    @Transactional
    @Override
    public Optional<Empleado> delete(Long id) {
        Optional<Empleado> optionalEmpleado = repository.findById(id);

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
        repository.delete(empleado);
        return optionalEmpleado; // Retorna el empleado eliminado
    }


   private void validarFechasEmpleado(EmpleadoDto empleadoDto) {
        // Validación de fecha de ingreso
       if (empleadoDto.getFechaIngreso().isAfter(LocalDate.now())) {
           throw new FechaInvalidaException("La fecha de ingreso no puede ser posterior al día de la fecha.");
       }
       // Validación de edad
       int edad = Period.between(empleadoDto.getFechaNacimiento(), LocalDate.now()).getYears();
       if (edad < 18) {
           throw new EdadInvalidaException("La edad del empleado no puede ser menor a 18 años.");
       }
       // Validación de fecha de nacimiento
       if (empleadoDto.getFechaNacimiento().isAfter(LocalDate.now())) {
           throw new FechaInvalidaException("La fecha de nacimiento no puede ser posterior al día de la fecha.");
       }
   }
}
