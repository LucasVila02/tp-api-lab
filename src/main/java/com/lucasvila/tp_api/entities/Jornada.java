package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concepto_id")
    private ConceptoLaboral conceptoLaboral;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private LocalDate fecha;

    private Integer hsTrabajadas;

    // Método para mapear a DTO de respuesta
    public JornadaResponseDTO toResponseDTO() {
        JornadaResponseDTO dto = new JornadaResponseDTO();
        dto.setNroDocumento(this.empleado.getNroDocumento());
        dto.setNombreCompleto(this.empleado.getNombre() + " " + this.empleado.getApellido());
        dto.setFecha(this.fecha);
        dto.setConcepto(this.conceptoLaboral.getNombre());
        dto.setHsTrabajadas(this.hsTrabajadas);
        return dto;
    }

}
