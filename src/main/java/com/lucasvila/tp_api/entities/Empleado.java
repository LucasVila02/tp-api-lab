package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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

    public EmpleadoDto toDTO() {
        EmpleadoDto dto = new EmpleadoDto();
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
