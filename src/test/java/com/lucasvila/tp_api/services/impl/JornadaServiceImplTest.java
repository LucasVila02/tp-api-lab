package com.lucasvila.tp_api.services.impl;

import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.Impl.JornadaServiceImpl;
import com.lucasvila.tp_api.validation.ValidacionJornada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JornadaServiceImplTest {

    private Jornada jornada;

    private Empleado empleado;

    private ConceptoLaboral conceptoLaboral;

    @Mock
    private JornadaRepository jornadaRepository;

    @Mock
    private ValidacionJornada validacionJornada;

    @InjectMocks
    private JornadaServiceImpl jornadaService;

    @BeforeEach
    void setUp() {
        jornada = new Jornada();
        empleado = new Empleado(1L, 1234, "Lucas", "Vila","lucas@gmail.com", LocalDate.of(2004,03,02) , LocalDate.of(2022,03,02), LocalDate.now() );
        conceptoLaboral = new ConceptoLaboral(1L, "Turno Normal", true, 8, 6);

        jornada.setEmpleado(empleado); // Asignar el empleado
        jornada.setFecha(LocalDate.of(2023, 1, 4));
        jornada.setConceptoLaboral(conceptoLaboral); // Asignar el concepto
        jornada.setHorasTrabajadas(7); // Asignar las horas trabajadas
    }

    @Test
    void testFindJornadasByFechaDesdeYFechaHastaYDocumento() {
        // Simulación de valores de parámetros
        LocalDate fechaDesde = LocalDate.of(2023, 1, 1);
        LocalDate fechaHasta = LocalDate.of(2023, 1, 31);
        String nroDocumento = "1234";

        // Simular validación de fechas y documento
        when(validacionJornada.validarFormatoFecha("2023-01-01")).thenReturn(fechaDesde);
        when(validacionJornada.validarFormatoFecha("2023-01-31")).thenReturn(fechaHasta);
        doNothing().when(validacionJornada).validarFechas(fechaDesde, fechaHasta);
        doNothing().when(validacionJornada).validarNroDocumento(nroDocumento);

        // Simular llamada al repositorio
        List<Jornada> jornadasSimuladas = List.of(jornada); // Lista simulada con una jornada
        when(jornadaRepository.findByFechaBetweenAndEmpleadoNroDocumento(fechaDesde, fechaHasta, 1234))
                .thenReturn(jornadasSimuladas);

        // Ejecutar el método
        List<Jornada> result = jornadaService.findJornadas("2023-01-01", "2023-01-31", nroDocumento);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(1, result.size()); // Verificar que la lista tenga una jornada
        assertEquals(jornada, result.get(0)); // Verificar que la jornada devuelta sea la correcta
    }
    @Test
    void testFindJornadasByNroDocumento() {

        String nroDocumento = empleado.getNroDocumento().toString(); // Obtener el número de documento del empleado
        Integer documento = Integer.valueOf(nroDocumento);

        doNothing().when(validacionJornada).validarNroDocumento(nroDocumento);
        // Simular la respuesta del repositorio
        when(jornadaRepository.findByEmpleadoNroDocumento(documento)).thenReturn(List.of(jornada));

        // Ejecutar el método
        List<Jornada> result = jornadaService.findJornadas(null, null, nroDocumento);

        // Verificar el resultado
        assertNotNull(result);
        assertFalse(result.isEmpty()); // Verificar que la lista no esté vacía
        assertEquals(1, result.size()); // Verificar que la lista contenga exactamente una jornada
    }
    @Test
    void testFindJornadasByFechaDesde() {
        LocalDate fechaDesde = LocalDate.of(2023, 1, 1);

        // Simular validación de la fecha
        when(validacionJornada.validarFormatoFecha("2023-01-01")).thenReturn(fechaDesde);

        // Simular que el repositorio devuelve la lista con la jornada creada en setUp(
        when(jornadaRepository.findByFechaAfter(fechaDesde)).thenReturn(Collections.singletonList(jornada));

        // Ejecutar el método
        List<Jornada> result = jornadaService.findJornadas("2023-01-01", null, null);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(jornada, result.get(0));
    }

    @Test
    void testFindJornadasDefault() {
        when(jornadaRepository.findAll()).thenReturn(Collections.singletonList(jornada));

        List<Jornada> result = jornadaService.findJornadas(null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(jornada, result.get(0));
    }

    }



