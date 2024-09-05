package com.lucasvila.tp_api.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class JornadaResponseDTO {

    private Integer nroDocumento;

    private String nombreCompleto;

    private LocalDate fecha;

    private String concepto;

    private Integer hsTrabajadas;

}
