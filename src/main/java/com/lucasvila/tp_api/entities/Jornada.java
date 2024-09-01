package com.lucasvila.tp_api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concepto_id")
//    @JsonProperty("idConcepto")
    private ConceptoLaboral idConcepto;

    @ManyToOne
//    @JsonProperty("idEmpleado")
    @JoinColumn(name = "empleado_id")
    private Empleado idEmpleado;

    private LocalDate fecha;

//    @Column(name = "horas_trabajadas")
    @JsonProperty("horas_trabajadas")
    private Integer horasTrabajadas;


}
