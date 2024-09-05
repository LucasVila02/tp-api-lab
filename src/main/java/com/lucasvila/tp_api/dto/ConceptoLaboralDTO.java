package com.lucasvila.tp_api.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class ConceptoLaboralDTO {

    private Long id;

    private String nombre;

    private boolean laborable;

    private int hsMaximo;

    private int hsMinimo;

}
