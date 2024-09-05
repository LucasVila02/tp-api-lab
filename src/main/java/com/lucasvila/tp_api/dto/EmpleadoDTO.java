package com.lucasvila.tp_api.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.validation.anotation.ValidName;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {

    private long id;

    @JsonProperty("nroDocumento")
    @NotNull(message = "'nroDocumento' es obligatorio.")
    private Integer nroDocumento;

    @ValidName(message = "Solo se permiten letras en el campo ‘nombre’.")
    @NotNull(message = "'nombre' es obligatorio.")
    private String nombre;

    @ValidName(message = "Solo se permiten letras en el campo ‘apellido’.")
    @NotNull(message = "'apellido' es obligatorio")
    private String apellido;

    @NotBlank(message = "'email' es obligatorio.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$", message = "El email debe  contener '@',  y terminar con '.com'.")
    private String email;

    @JsonProperty("fechaNacimiento")
    @NotNull(message = "'fechaNacimiento' es obligatorio.")
    private LocalDate fechaNacimiento;

    @JsonProperty("fechaIngreso")
    @NotNull(message = "'fechaIngreso' es obligatorio.")
    private LocalDate fechaIngreso;

    @JsonProperty("fechaCreacion")
    private LocalDate fechaCreacion;


    public Empleado toEntity() {
        Empleado empleado = new Empleado();
        empleado.setId(this.id);
        empleado.setNombre(this.nombre);
        empleado.setApellido(this.apellido);
        empleado.setEmail(this.email);
        empleado.setFechaIngreso(this.fechaIngreso);
        empleado.setNroDocumento(this.nroDocumento);
        empleado.setFechaNacimiento(this.fechaNacimiento);
        empleado.setFechaCreacion(this.fechaCreacion);
        return empleado;

    }

    @Override
    public String toString() {
        return "EmpleadoDto{" +
                "numeroDocumento=" + nroDocumento +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpleadoDTO that = (EmpleadoDTO) o;
        return id == that.id && Objects.equals(nroDocumento, that.nroDocumento) && Objects.equals(nombre, that.nombre) && Objects.equals(apellido, that.apellido) && Objects.equals(email, that.email) && Objects.equals(fechaNacimiento, that.fechaNacimiento) && Objects.equals(fechaIngreso, that.fechaIngreso) && Objects.equals(fechaCreacion, that.fechaCreacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroDocumento, nombre, apellido, email, fechaNacimiento, fechaIngreso, fechaCreacion);
    }
}
