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
import org.mockito.Spy;
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
        //given
        when(empleadosRepository.findAll()).thenReturn(Arrays.asList(empleado, empleadoDos));

        //when
        List<EmpleadoDTO> result = empleadosServicesImplUnderTest.findAll();

        //then
        assertEquals(2, result.size());
        assertEquals("Lucas", result.get(0).getNombre());

    }

    @Test
    void findByIdShouldReturnEmpleadoDTO() {

        // Mockear el comportamiento del repositorio
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // Ejecutar el método
        Optional<EmpleadoDTO> result = empleadosServicesImplUnderTest.findById(1L);

        // Validar que el resultado no sea vacío
        assertTrue(result.isPresent());
        assertEquals("Lucas", result.get().getNombre());

        // Verificar que el repositorio fue llamado
        verify(empleadosRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowNoEncontradoException() {

        // Mockear el comportamiento del repositorio para devolver un Optional vacío
        when(empleadosRepository.findById(3L)).thenReturn(Optional.empty());

        // Ejecutar el método y verificar que se lance la excepción
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.findById(3L);
        });

        // Validar que la excepción tiene el mensaje correcto
        assertEquals("No se existe el empleado con Id: 3", exception.getMessage());
    }

    @Test
    void shouldAddEmpleado() {

        // Simular las validaciones (sin hacer nada en este caso)
        doNothing().when(validacionEmpleado).validarEmailAndDocumentoEmpleadoCreate(empleadoDTO);
        doNothing().when(validacionEmpleado).validarFechasEmpleado(empleadoDTO);

        // Simular que el repositorio guarda el empleado y devuelve la entidad guardada
        when(empleadosRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Ejecutar el método
        EmpleadoDTO result = empleadosServicesImplUnderTest.create(empleadoDTO);

        // Validar que el resultado no sea nulo y contenga los datos correctos
        assertNotNull(result);
        assertEquals("Lucas", result.getNombre());

    }

    @Test
    void ShouldThrowExceptionWhenEmailIsInvalid() {

        doThrow(new EmpleadoDuplicadoException("Ya existe un empleado con el email ingresado."))
                .when(validacionEmpleado).validarEmailAndDocumentoEmpleadoCreate(any(EmpleadoDTO.class));


        Exception exception = assertThrows(EmpleadoDuplicadoException.class, () -> {
            empleadosServicesImplUnderTest.create(empleadoDTO);
        });

        assertEquals("Ya existe un empleado con el email ingresado.", exception.getMessage());

    }

    @Test
    void testUpdateEmpleadoSuccess() {
        // Mock de encontrar al empleado
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // Mock de validación (simula que no lanzan excepciones)
        doNothing().when(validacionEmpleado).validarEmailAndDocumentoEmpleadoUpdate(any(EmpleadoDTO.class), eq(1L));
        doNothing().when(validacionEmpleado).validarFechasEmpleado(any(EmpleadoDTO.class));

        // Mock de guardar el empleado actualizado
        when(empleadosRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Ejecutar el método de actualización
        Optional<EmpleadoDTO> result = empleadosServicesImplUnderTest.update(1L, empleadoDTO);

        // Validar que los datos fueron actualizados correctamente
        assertTrue(result.isPresent());
        assertEquals("Lucas", result.get().getNombre());
        assertEquals("Vila", result.get().getApellido());

    }

    @Test
    void testUpdateEmpleadoNotFound() {
        // Mock para simular que el empleado no existe
        when(empleadosRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y validar la excepción
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.update(1L, empleadoDTO);
        });

        assertEquals("No se existe el empleado con Id: 1", exception.getMessage());

    }
    @Test
    void testDeleteEmpleadoSuccess() {
        // Simular que el empleado existe y no tiene jornadas asociadas
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(jornadaRepository.existsByEmpleadoId(1L)).thenReturn(false);

        // Ejecutar el método de eliminación
        empleadosServicesImplUnderTest.delete(1L);
    }

    @Test
    void testDeleteEmpleadoWithJornadas() {
        // Simular que el empleado existe y tiene jornadas asociadas
        when(empleadosRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(jornadaRepository.existsByEmpleadoId(1L)).thenReturn(true);

        // Ejecutar y verificar que se lanza la excepción BadRequestException
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            empleadosServicesImplUnderTest.delete(1L);
        });

        assertEquals("No es posible eliminar un empleado con jornadas asociadas.", exception.getMessage());
    }

    @Test
    void testDeleteEmpleadoNotFound() {
        // Simular que el empleado no existe
        when(empleadosRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecutar y verificar que se lanza la excepción NoEncontradoException
        NoEncontradoException exception = assertThrows(NoEncontradoException.class, () -> {
            empleadosServicesImplUnderTest.delete(1L);
        });
        assertEquals("No se existe el empleado con Id: 1", exception.getMessage());
    }
}
