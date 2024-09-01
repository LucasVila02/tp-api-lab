package com.lucasvila.tp_api.controllers;


import com.lucasvila.tp_api.dto.ConceptoLaboralDto;
import com.lucasvila.tp_api.services.ConceptoLaborableServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ConceptoLaboralController {

    @Autowired
    private ConceptoLaborableServices services;

    @GetMapping({"/concepto"})
    public ResponseEntity<List<ConceptoLaboralDto>> obtenerConceptos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre) {

        List<ConceptoLaboralDto> conceptos = services.getConceptos(id, nombre);
        return ResponseEntity.ok(conceptos);

    }
}
