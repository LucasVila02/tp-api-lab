package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.exceptions.EdadInvalidaException;
import com.lucasvila.tp_api.exceptions.EmpleadoDuplicadoException;
import com.lucasvila.tp_api.exceptions.EmpleadoNoEncontradoException;
import com.lucasvila.tp_api.exceptions.FechaInvalidaException;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.services.EmpleadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadosServicesImpl implements EmpleadosServices {

    @Autowired
    private EmpleadosRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Empleado> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Empleado> findById(Long id) {
        Optional<Empleado> optionalEmpleados = repository.findById(id);

        if (optionalEmpleados.isEmpty()) {
            throw new EmpleadoNoEncontradoException(id);
        }
        return optionalEmpleados;
    }

    @Transactional
    @Override
    public Empleado create(EmpleadoDto empleadoDto) {

        // Validación de documento duplicado
        if (repository.existsByNumeroDocumento(empleadoDto.getNumeroDocumento())) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el documento ingresado.");
        }
        // Validación de email duplicado
        if (repository.existsByEmail(empleadoDto.getEmail())) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el email ingresado.");
        }

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

        // Mapeo de DTO a entidad y guardado
        Empleado empleado = Empleado.builder()
                .nombre(empleadoDto.getNombre())
                .apellido(empleadoDto.getApellido())
                .numeroDocumento(empleadoDto.getNumeroDocumento())
                .email(empleadoDto.getEmail())
                .fechaNacimiento(empleadoDto.getFechaNacimiento())
                .fechaIngreso(empleadoDto.getFechaIngreso())
                .fechaCreacion(LocalDate.now())
                .build();

        return repository.save(empleado);
    }

    @Transactional
    @Override
    public Optional<Empleado> update(Long id, Empleado empleado) {
        Optional<Empleado> optionalEmpleados = repository.findById(id);

        if (optionalEmpleados.isPresent()){
            Empleado empleadoDb = optionalEmpleados.orElseThrow();

            empleadoDb.setNombre(empleado.getNombre());
            empleadoDb.setApellido(empleado.getApellido());
            empleadoDb.setEmail(empleado.getEmail());
            empleadoDb.setNumeroDocumento(empleado.getNumeroDocumento());
            empleadoDb.setFechaNacimiento(empleado.getFechaNacimiento());
            empleadoDb.setFechaIngreso(empleado.getFechaIngreso());

            return Optional.of(repository.save(empleadoDb));
        }
        return optionalEmpleados;
    }


    @Transactional
    @Override
    public Optional<Empleado> delete(Long id) {
        Optional<Empleado> optionalEmpleado = repository.findById(id);

        optionalEmpleado.ifPresent(empleado -> {
                repository.delete(empleado);
        });
        return optionalEmpleado;
    }
}
