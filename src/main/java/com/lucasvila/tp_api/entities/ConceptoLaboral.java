package com.lucasvila.tp_api.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "concepto_laboral")
public class ConceptoLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private boolean laborable;

    private Integer hsMaximo;

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

}
