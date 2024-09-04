package com.lucasvila.tp_api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptoLaboralDTO {

    private Long id;

    private String nombre;

    private boolean laborable;

    @JsonProperty("hsMaximo")
    private int hsMaximo;

    @JsonProperty("hsMinimo")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptoLaboralDTO that = (ConceptoLaboralDTO) o;
        return laborable == that.laborable && hsMaximo == that.hsMaximo && hsMinimo == that.hsMinimo && Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, laborable, hsMaximo, hsMinimo);
    }
}
