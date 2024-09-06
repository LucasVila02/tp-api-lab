package com.lucasvila.tp_api.services.impl;

import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.services.Impl.ConceptoLaborableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConceptoLaborableServiceImplTest {

    @Mock
    private ConceptoLaborableRepository conceptoLaborableRepository;

    @InjectMocks
    private ConceptoLaborableServiceImpl conceptoLaboralServiceImplTest;

    private ConceptoLaboral conceptoLaboral;
    private ConceptoLaboral conceptoLaboralDos;

    @BeforeEach
    void setUp() {
        conceptoLaboral = new ConceptoLaboral();
        conceptoLaboral.setId(1L);
        conceptoLaboral.setNombre("Turno Normal");
        conceptoLaboral.setHsMinimo(6);
        conceptoLaboral.setHsMaximo(8);
        conceptoLaboral.setLaborable(true);

        conceptoLaboralDos = new ConceptoLaboral();
        conceptoLaboralDos.setId(2L);
        conceptoLaboralDos.setNombre("Turno Extra");
        conceptoLaboralDos.setHsMinimo(2);
        conceptoLaboralDos.setHsMaximo(6);
        conceptoLaboralDos.setLaborable(true);

    }

    @Test
    void testGetConceptosByIdAndNombre() {
        Long id = 1L;
        String nombre = "Turno Normal";

        // Simular la respuesta del repositorio
        when(conceptoLaborableRepository.findByIdAndNombreContainingIgnoreCase(id, nombre))
                .thenReturn(Collections.singletonList(conceptoLaboral));

        // Ejecutar el método
        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(id, nombre);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
    }

    @Test
    void testGetConceptosById() {
        Long id = 1L;

        // Simular la respuesta del repositorio
        when(conceptoLaborableRepository.findById(id))
                .thenReturn(Optional.of(conceptoLaboral));

        // Ejecutar el método
        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(id, null);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
        assertEquals(conceptoLaboral.getNombre(), result.get(0).getNombre());
    }

    @Test
    void testGetConceptosByNombre() {
        String nombre = "Turno";

        // Simular la respuesta del repositorio
        when(conceptoLaborableRepository.findByNombreContainingIgnoreCase(nombre))
                .thenReturn(Arrays.asList(conceptoLaboral, conceptoLaboralDos));

        // Ejecutar el método
        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(null, nombre);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
        assertEquals(conceptoLaboralDos.getId(), result.get(1).getId());

    }

    @Test
    void testGetAllConceptos() {
        // Simular la respuesta del repositorio
        when(conceptoLaborableRepository.findAll())
                .thenReturn(Arrays.asList(conceptoLaboral, conceptoLaboralDos));

        // Ejecutar el método
        List<ConceptoLaboralDTO> result =  conceptoLaboralServiceImplTest.getConceptos(null, null);
        // Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
        assertEquals(conceptoLaboralDos.getId(), result.get(1).getId());
    }

}
