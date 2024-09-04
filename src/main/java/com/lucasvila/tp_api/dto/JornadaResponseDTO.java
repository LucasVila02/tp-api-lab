package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JornadaResponseDTO that = (JornadaResponseDTO) o;
        return Objects.equals(nroDocumento, that.nroDocumento) && Objects.equals(nombreCompleto, that.nombreCompleto) && Objects.equals(fecha, that.fecha) && Objects.equals(concepto, that.concepto) && Objects.equals(hsTrabajadas, that.hsTrabajadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroDocumento, nombreCompleto, fecha, concepto, hsTrabajadas);
    }
}
