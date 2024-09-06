package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JornadaResponseDTO {

    private Integer nroDocumento;

    private String nombreCompleto;

    private LocalDate fecha;

    private String concepto;

    private Integer hsTrabajadas;

}
