package com.lucasvila.tp_api.services.impl;

import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.services.Impl.ConceptoLaborableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ConceptoLaborableServiceImplTest {
    @Mock
    private ConceptoLaborableRepository conceptoLaborableRepository;

    @InjectMocks
    private ConceptoLaborableServiceImpl conceptoLaboralServiceImplTest;

    private ConceptoLaboral conceptoLaboral;

    private ConceptoLaboralDTO conceptoLaboralDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear una instancia de ConceptoLaboral para simular los datos
        conceptoLaboral = new ConceptoLaboral();
        conceptoLaboral.setId(1L);
        conceptoLaboral.setNombre("Turno Normal");
        conceptoLaboral.setHsMinimo(6);
        conceptoLaboral.setHsMaximo(8);
        conceptoLaboral.setLaborable(true);

        conceptoLaboralDTO = new ConceptoLaboralDTO();
        conceptoLaboralDTO.setId(1L);
        conceptoLaboralDTO.setNombre("Turno Normal");
        conceptoLaboralDTO.setHsMinimo(6);
        conceptoLaboralDTO.setHsMaximo(8);
        conceptoLaboralDTO.setLaborable(true);

        // Configura el mock para el método toDTO
        when(conceptoLaboral.toDTO()).thenReturn(conceptoLaboralDTO);
    }

    @Test
    void testGetConceptosByIdAndNombre() {
        Long id = 1L;

        // Simular la respuesta del repositorio
        when(conceptoLaborableRepository.findByIdAndNombreContainingIgnoreCase(id, nombre))
                .thenReturn(Collections.singletonList(conceptoLaboral));

        // Ejecutar el método
        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(id, nombre);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
        assertEquals(conceptoLaboral.getNombre(), result.get(0).getNombre());
    }

//    @Test
//    void testGetConceptosById() {
//        Long id = 1L;
//
//        // Simular la respuesta del repositorio
//        when(conceptoLaborableRepository.findById(id))
//                .thenReturn(Optional.of(conceptoLaboral));
//
//        // Ejecutar el método
//        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(id, null);
//
//        // Verificar el resultado
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
//        assertEquals(conceptoLaboral.getNombre(), result.get(0).getNombre());
//    }
//
//    @Test
//    void testGetConceptosByNombre() {
//        String nombre = "Concepto Test";
//
//        // Simular la respuesta del repositorio
//        when(conceptoLaborableRepository.findByNombreContainingIgnoreCase(nombre))
//                .thenReturn(Collections.singletonList(conceptoLaboral));
//
//        // Ejecutar el método
//        List<ConceptoLaboralDTO> result = conceptoLaboralServiceImplTest.getConceptos(null, nombre);
//
//        // Verificar el resultado
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
//        assertEquals(conceptoLaboral.getNombre(), result.get(0).getNombre());
//    }
//
//    @Test
//    void testGetAllConceptos() {
//        // Simular la respuesta del repositorio
//        when(conceptoLaborableRepository.findAll())
//                .thenReturn(Collections.singletonList(conceptoLaboral));
//
//        // Ejecutar el método
//        List<ConceptoLaboralDTO> result =  conceptoLaboralServiceImplTest
//        // Verificar el resultado
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(conceptoLaboral.getId(), result.get(0).getId());
//        assertEquals(conceptoLaboral.getNombre(), result.get(0).getNombre());
//    }

}
