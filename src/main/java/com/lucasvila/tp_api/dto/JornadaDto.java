package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JornadaDto {

    private Long id;

    private Long idConcepto;
    private Long idEmpleado;
    private LocalDate fecha;

    @JsonProperty("horas_trabajadas")
    private int horasTrabajadas;

}