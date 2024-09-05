package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jornada jornada = (Jornada) o;
        return Objects.equals(id, jornada.id) && Objects.equals(conceptoLaboral, jornada.conceptoLaboral) && Objects.equals(empleado, jornada.empleado) && Objects.equals(fecha, jornada.fecha) && Objects.equals(hsTrabajadas, jornada.hsTrabajadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conceptoLaboral, empleado, fecha, hsTrabajadas);
    }
}
