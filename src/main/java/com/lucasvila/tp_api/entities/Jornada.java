package com.lucasvila.tp_api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
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
    private ConceptoLaboral conceptoLaboral;

    @ManyToOne
//    @JsonProperty("idEmpleado")
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private LocalDate fecha;

//    @Column(name = "horas_trabajadas")
    @JsonProperty("horas_trabajadas")
    private Integer horasTrabajadas;
    // Método para mapear a DTO de respuesta

    public JornadaResponseDTO toResponseDTO() {
        JornadaResponseDTO dto = new JornadaResponseDTO();
        dto.setNroDocumento(this.empleado.getNumeroDocumento());
        dto.setNombreCompleto(this.empleado.getNombre() + " " + this.empleado.getApellido());
        dto.setFecha(this.fecha);
        dto.setConcepto(this.conceptoLaboral.getNombre());
        dto.setHsTrabajadas(this.horasTrabajadas);
        return dto;
    }

}
