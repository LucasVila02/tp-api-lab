package com.lucasvila.tp_api.validation;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.exceptions.EdadInvalidaException;
import com.lucasvila.tp_api.exceptions.EmpleadoDuplicadoException;
import com.lucasvila.tp_api.exceptions.FechaInvalidaException;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;


@Service
public class ValidacionEmpleado {

    @Autowired
    private EmpleadosRepository empleadosRepository;

    public void validarEmailAndDocumentoEmpleadoCreate(EmpleadoDTO empleadoDto) {
        // Validación de documento duplicado
        if (empleadosRepository.existsByNumeroDocumento(empleadoDto.getNumeroDocumento())) {
            throw new EmpleadoDuplicadoException("Ya existe un empleado con el documento ingresado.");
        }
        // Validación de email duplicado
        if (empleadosRepository.existsByEmail(empleadoDto.getEmail())) {
            throw new EmpleadoDuplicadoException("Ya existe un empleado con el email ingresado.");
        }
    }

    public void validarEmailAndDocumentoEmpleadoUpdate(EmpleadoDTO empleadoDto, Long id){
        // Validación de documento duplicado
        if (empleadosRepository.existsByNumeroDocumentoAndIdNot(empleadoDto.getNumeroDocumento(), id) ) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el documento ingresado.");
        }
        // Validación de email duplicado
        if (empleadosRepository.existsByEmailAndIdNot(empleadoDto.getEmail(), id)) {
            throw new EmpleadoDuplicadoException( "Ya existe un empleado con el email ingresado.");
        }
    }


    public void validarFechasEmpleado(EmpleadoDTO empleadoDto) {
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
