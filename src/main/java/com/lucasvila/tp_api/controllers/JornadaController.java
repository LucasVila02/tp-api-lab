package com.lucasvila.tp_api.controllers;


import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.services.JornadaServices;
import com.lucasvila.tp_api.validation.ValidacionJornada;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class JornadaController {

    @Autowired
    private JornadaServices services;

    @Autowired
    private ValidacionJornada validacionJornada;


    @GetMapping("/jornadas")
    public ResponseEntity<?> getJornadas(
            @RequestParam(required = false)  String fechaDesde,
            @RequestParam(required = false)  String fechaHasta,
            @RequestParam(required = false) String nroDocumento) {

            // Llamar al método unificado
            List<Jornada> jornadas = services.findJornadas(fechaDesde, fechaHasta, nroDocumento);

            List<JornadaResponseDTO> jornadaResponseDTOs = jornadas.stream()
                    .map(Jornada::toResponseDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(jornadaResponseDTOs);
    }

    @PostMapping("/jornada")
    public ResponseEntity<JornadaResponseDTO> crearJornada(@Valid @RequestBody JornadaRequestDTO jornadaRequestDTO) {

        // Llama al servicio para agregar la jornada y obtener el DTO de respuesta
        JornadaResponseDTO jornadaResponseDTO = services.addJornada(jornadaRequestDTO);

        // Retorna el DTO de respuesta con un código de estado 201 (CREATED)
        return new ResponseEntity<>(jornadaResponseDTO, HttpStatus.CREATED);
    }

}
