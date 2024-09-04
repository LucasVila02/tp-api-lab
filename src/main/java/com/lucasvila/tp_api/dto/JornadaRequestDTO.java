package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JornadaRequestDTO {


    private Long id;

    @NotNull(message = "'idConcepto' es obligatorio.")
    @JsonProperty("idConcepto")
    private Long concepto;

    @NotNull(message = "'IdEmpleado' es obligatorio.")
    @JsonProperty("idEmpleado")
    private Long empleado;

    @NotNull(message = "'fecha' es obligatorio.")
    private LocalDate fecha;

    @JsonProperty("hsTrabajadas")
    private Integer horasTrabajadas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JornadaRequestDTO that = (JornadaRequestDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(concepto, that.concepto) && Objects.equals(empleado, that.empleado) && Objects.equals(fecha, that.fecha) && Objects.equals(horasTrabajadas, that.horasTrabajadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concepto, empleado, fecha, horasTrabajadas);
    }
}