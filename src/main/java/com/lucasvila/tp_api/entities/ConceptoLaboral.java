package com.lucasvila.tp_api.entities;


import com.lucasvila.tp_api.dto.ConceptoLaboralDTO;
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
    private Integer hsMaximo;

    @Column(name = "hs_minimo")
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
