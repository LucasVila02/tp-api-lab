package com.lucasvila.tp_api.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasvila.tp_api.dto.ConceptoLaboralDto;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "hs_maximo")
    @JsonProperty("hs_maximo")
    private Integer hsMaximo;

    @Column(name = "hs_minimo")
    @JsonProperty("hs_minimo")
    private Integer hsMinimo;


    public ConceptoLaboralDto toDTO() {
        ConceptoLaboralDto dto = new ConceptoLaboralDto();
        dto.setNombre(this.nombre);
        dto.setLaborable(this.laborable);
        dto.setHsMinimo(this.hsMinimo);
        dto.setHsMaximo(this.hsMaximo);
        dto.setId(this.id);
        return dto;
    }

}
