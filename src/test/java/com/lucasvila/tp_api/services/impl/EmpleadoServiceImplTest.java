package com.lucasvila.tp_api.services.impl;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.exceptions.BadRequestException;
import com.lucasvila.tp_api.exceptions.EmpleadoDuplicadoException;
import com.lucasvila.tp_api.exceptions.NoEncontradoException;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.Impl.EmpleadosServiceImpl;
import com.lucasvila.tp_api.validation.ValidacionEmpleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceImplTest {

    private Empleado empleado;
    private Empleado empleadoDos;
    private EmpleadoDTO empleadoDTO;

    @Mock
    private EmpleadosRepository empleadosRepository;

    @Mock
    private JornadaRepository jornadaRepository;

    @Mock
    private ValidacionEmpleado validacionEmpleado;

    @InjectMocks
    private EmpleadosServiceImpl empleadosServicesImplUnderTest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Lucas");
        empleado.setApellido("Vila");
        empleado.setEmail("lucas@gmail.com");
        empleado.setNroDocumento(1234);
        empleado.setFechaIngreso(LocalDate.of(2023,01,01));
        empleado.setFechaNacimiento(LocalDate.of(2001,02, 13));

        empleadoDos = new Empleado();
        empleadoDos.setId(2L);
        empleadoDos.setNombre("Jose");
        empleadoDos.setApellido("Perez");
        empleadoDos.setEmail("Jose@gmail.com");
        empleadoDos.setNroDocumento(123444);
        empleadoDos.setFechaIngreso(LocalDate.of(2023,01,01));
        empleadoDos.setFechaNacimiento(LocalDate.of(2001,02, 13));

        empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setNombre("Lucas");
        empleadoDTO.setApellido("Vila");
        empleadoDTO.setEmail("lucas@gmail.com");
        empleadoDTO.setNroDocumento(1234);
        empleadoDTO.setFechaIngreso(LocalDate.of(2023,01,01));
        empleadoDTO.setFechaNacimiento(LocalDate.of(2001,02, 13));

    }

    @Test
    void findAllShouldReturnListOfEmpleadoDTOs(){
        // Simular la respuesta del repositorio
        when(empleadosRepository.findAll()).thenReturn(Arrays.asList(empleado, empleadoDos));

        // Ejecutar el método
        List<EmpleadoDTO> result = empleadosServicesImplUnderTest.findAll();

        // Verificar el resultado
        assertEquals(2, result.size());
        assertEquals("Lucas", result.get(0).getNombre());

    }

    @Test
    void findByIdShouldReturnEmpleadoDTO() {

        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // Ejecutar el método
        Optional<EmpleadoDTO> result = empleadosServicesImplUnderTest.findById(1L);

        // Verificar el resultado
        assertTrue(result.isPresent());
        assertEquals("Lucas", result.get().getNombre());
    }

    @Test
    void shouldThrowNoEncontradoException() {

        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(3L)).thenReturn(Optional.empty());

        // Ejecutar el método
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.findById(3L);
        });

        // Verificar el resultado
        assertEquals("No se existe el empleado con Id: 3", exception.getMessage());
    }

    @Test
    void shouldAddEmpleado() {

        // Simular las validaciones (sin hacer nada en este caso)
        doNothing().when(validacionEmpleado).validarEmailAndDocumentoEmpleadoCreate(empleadoDTO);
        doNothing().when(validacionEmpleado).validarFechasEmpleado(empleadoDTO);

        // Simular la respuesta del repositorio
        when(empleadosRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Ejecutar el método
        EmpleadoDTO result = empleadosServicesImplUnderTest.create(empleadoDTO);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals("Lucas", result.getNombre());

    }

    @Test
    void ShouldThrowExceptionWhenEmailIsInvalid() {
        // Simular las validaciones (sin hacer nada en este caso)
        doThrow(new EmpleadoDuplicadoException("Ya existe un empleado con el email ingresado."))
                .when(validacionEmpleado).validarEmailAndDocumentoEmpleadoCreate(any(EmpleadoDTO.class));

        // Ejecutar el método
        Exception exception = assertThrows(EmpleadoDuplicadoException.class, () -> {
            empleadosServicesImplUnderTest.create(empleadoDTO);
        });
        // Verificar el resultado
        assertEquals("Ya existe un empleado con el email ingresado.", exception.getMessage());

    }

    @Test
    void testUpdateEmpleadoSuccess() {
        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // Simular las validaciones (sin hacer nada en este caso)
        doNothing().when(validacionEmpleado).validarEmailAndDocumentoEmpleadoUpdate(any(EmpleadoDTO.class), eq(1L));
        doNothing().when(validacionEmpleado).validarFechasEmpleado(any(EmpleadoDTO.class));

        // Simular la respuesta del repositorio
        when(empleadosRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Ejecutar el método
        Optional<EmpleadoDTO> result = empleadosServicesImplUnderTest.update(1L, empleadoDTO);

        // Verificar el resultado
        assertTrue(result.isPresent());
        assertEquals("Lucas", result.get().getNombre());
        assertEquals("Vila", result.get().getApellido());

    }

    @Test
    void testUpdateEmpleadoNotFound() {
        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar el método
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.update(1L, empleadoDTO);
        });
        // Verificar el resultado
        assertEquals("No se existe el empleado con Id: 1", exception.getMessage());

    }
    @Test
    void testDeleteEmpleadoSuccess() {
        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(jornadaRepository.existsByEmpleadoId(1L)).thenReturn(false);

        // Ejecutar el método de eliminación
        empleadosServicesImplUnderTest.delete(1L);
    }

    @Test
    void testDeleteEmpleadoWithJornadas() {
        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(jornadaRepository.existsByEmpleadoId(1L)).thenReturn(true);

        // Ejecutar el método
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            empleadosServicesImplUnderTest.delete(1L);
        });
        // Verificar el resultado
        assertEquals("No es posible eliminar un empleado con jornadas asociadas.", exception.getMessage());
    }

    @Test
    void testDeleteEmpleadoNotFound() {
        // Simular la respuesta del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar el método
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.delete(1L);
        });
        // Verificar el resultado
        assertEquals("No se existe el empleado con Id: 1", exception.getMessage());
    }
}
