package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer numeroDocumento;
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

    public EmpleadoDto toDTO() {
        EmpleadoDto dto = new EmpleadoDto();
        dto.setId(this.getId());
        dto.setNombre(this.nombre);
        dto.setApellido(this.apellido);
        dto.setEmail(this.email);
        dto.setFechaIngreso(this.fechaIngreso);
        dto.setNumeroDocumento(this.numeroDocumento);
        dto.setFechaNacimiento(this.fechaNacimiento);
        dto.setFechaCreacion(this.fechaCreacion);
        return dto;
    }

}
