package com.lucasvila.tp_api.entities;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;

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
        dto.setNumeroDocumento(this.nroDocumento);
        dto.setFechaNacimiento(this.fechaNacimiento);
        dto.setFechaCreacion(this.fechaCreacion);
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(id, empleado.id) && Objects.equals(nroDocumento, empleado.nroDocumento) && Objects.equals(nombre, empleado.nombre) && Objects.equals(apellido, empleado.apellido) && Objects.equals(email, empleado.email) && Objects.equals(fechaNacimiento, empleado.fechaNacimiento) && Objects.equals(fechaIngreso, empleado.fechaIngreso) && Objects.equals(fechaCreacion, empleado.fechaCreacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroDocumento, nombre, apellido, email, fechaNacimiento, fechaIngreso, fechaCreacion);
    }
}
