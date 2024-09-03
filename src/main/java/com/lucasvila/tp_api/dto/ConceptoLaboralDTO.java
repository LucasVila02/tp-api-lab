package com.lucasvila.tp_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptoLaboralDTO {

    private Long id;

    private String nombre;

    private boolean laborable;

    @JsonProperty("hs_maximo")
    private int hsMaximo;

    @JsonProperty("hs_minimo")
    private int hsMinimo;


    public ConceptoLaboral toEntity() {
        ConceptoLaboral concepto = new ConceptoLaboral();
        concepto.setId(this.id);
        concepto.setNombre(this.nombre);
        concepto.setHsMaximo(this.hsMaximo);
        concepto.setHsMinimo(this.hsMinimo);
        concepto.setLaborable(this.laborable);
        return concepto;

    }

}
