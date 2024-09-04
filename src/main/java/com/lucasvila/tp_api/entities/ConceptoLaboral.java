package com.lucasvila.tp_api.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "concepto_laboral")
public class ConceptoLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private boolean laborable;

    @JsonInclude
    @Column(name = "hsMaximo")
    private Integer hsMaximo;

    @JsonInclude
    @Column(name = "hsMinimo")
    private Integer hsMinimo;


    public ConceptoLaboralDTO toDTO() {
        ConceptoLaboralDTO dto = new ConceptoLaboralDTO();
        dto.setNombre(this.nombre);
        dto.setLaborable(this.laborable);
        dto.setHsMinimo(this.hsMinimo != null ? this.hsMinimo : 0);
        dto.setHsMaximo(this.hsMaximo != null ? this.hsMaximo: 0);
        dto.setId(this.id);
        return dto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptoLaboral that = (ConceptoLaboral) o;
        return laborable == that.laborable && Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(hsMaximo, that.hsMaximo) && Objects.equals(hsMinimo, that.hsMinimo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, laborable, hsMaximo, hsMinimo);
    }
}
