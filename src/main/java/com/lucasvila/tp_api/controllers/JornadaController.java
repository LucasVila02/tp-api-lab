package com.lucasvila.tp_api.controllers;


import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.services.JornadaServices;
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

    @GetMapping("/jornadas")
    public ResponseEntity<?> getJornadas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String nroDocumento) {

        // Validaciones
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            return ResponseEntity.badRequest().body("El campo ‘fechaDesde’ no puede ser mayor que ‘fechaHasta’.");
        }

        // Validación del formato del número de documento
        if (nroDocumento != null && !nroDocumento.matches("\\d+")) {
            return ResponseEntity.badRequest().body("El campo ‘nroDocumento’ solo puede contener números enteros.");
        }

        List<Jornada> jornadas= getJornadasBasedOnParameters( fechaDesde,  fechaHasta,  nroDocumento);

        //        if (fechaDesde != null && fechaHasta != null && nroDocumento != null) {
//            jornadas = services.findByFechaAndNroDocumento(fechaDesde, fechaHasta, Integer.valueOf(nroDocumento));
//        } else if (fechaDesde != null && fechaHasta != null) {
//            jornadas = services.findByFecha(fechaDesde, fechaHasta);
//        } else if (nroDocumento != null) {
//            jornadas = services.findByNroDocumento(Integer.valueOf(nroDocumento));
//        } else if (fechaDesde != null) {
//            jornadas = services.findByFechaDesde(fechaDesde);
//        } else if (fechaHasta != null) {
//            jornadas = services.findByFechaHasta(fechaHasta);
//        } else {
//            jornadas = services.findAll();
//        }

        List<JornadaResponseDTO> jornadaResponseDTOs = jornadas.stream()
                .map(Jornada::toResponseDTO)  // Usa el método toResponseDTO
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



    private List<Jornada> getJornadasBasedOnParameters(LocalDate fechaDesde, LocalDate fechaHasta, String nroDocumento) {
        if (fechaDesde != null && fechaHasta != null && nroDocumento != null) {
            return services.findByFechaAndNroDocumento(fechaDesde, fechaHasta, Integer.valueOf(nroDocumento));
        } else if (fechaDesde != null && fechaHasta != null) {
            return services.findByFecha(fechaDesde, fechaHasta);
        } else if (nroDocumento != null) {
            return services.findByNroDocumento(Integer.valueOf(nroDocumento));
        } else if (fechaDesde != null) {
            return services.findByFechaDesde(fechaDesde);
        } else if (fechaHasta != null) {
            return services.findByFechaHasta(fechaHasta);
        } else {
            return services.findAll();
        }
    }
}
