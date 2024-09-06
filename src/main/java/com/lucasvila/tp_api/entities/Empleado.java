package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;



@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer nroDocumento;

    private String nombre;

    private String apellido;

    private String email;

    private LocalDate fechaNacimiento;

    private LocalDate fechaIngreso;

    private LocalDate fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now(); // Asigna la fecha actual al persistir
    }

    public EmpleadoDTO toDTO() {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(this.getId());
        dto.setNombre(this.nombre);
        dto.setApellido(this.apellido);
        dto.setEmail(this.email);
        dto.setFechaIngreso(this.fechaIngreso);
        dto.setNroDocumento(this.nroDocumento);
        dto.setFechaNacimiento(this.fechaNacimiento);
        dto.setFechaCreacion(this.fechaCreacion);
        return dto;
    }

}
