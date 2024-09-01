package com.lucasvila.tp_api.controllers;


import com.lucasvila.tp_api.dto.JornadaDto;
import com.lucasvila.tp_api.services.JornadaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class JornadaController {

    @Autowired
    private JornadaServices services;


    @PostMapping("/jornada")
    public ResponseEntity<JornadaDto> crearJornada(@RequestBody JornadaDto jornadaDTO) {
        JornadaDto nuevaJornada = services.addJornada(jornadaDTO);
        return new ResponseEntity<>(nuevaJornada, HttpStatus.CREATED);
    }
}
