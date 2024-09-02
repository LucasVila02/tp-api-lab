package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class JornadaResponseDTO {

    @JsonProperty("nroDocumento")
    private Integer nroDocumento;

    @JsonProperty("nombreCompleto")
    private String nombreCompleto;

    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("concepto")
    private String concepto;

    @JsonProperty("hsTrabajadas")
    private Integer hsTrabajadas;



}
