package com.lucasvila.tp_api.controllers;


import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
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
    public ResponseEntity<List<ConceptoLaboralDTO>> obtenerConceptos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre) {

        List<ConceptoLaboralDTO> conceptos = services.getConceptos(id, nombre);
        return ResponseEntity.ok(conceptos);

    }
}
