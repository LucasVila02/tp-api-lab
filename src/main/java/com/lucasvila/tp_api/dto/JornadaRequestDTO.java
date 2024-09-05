package com.lucasvila.tp_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
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

    private Integer hsTrabajadas;

}