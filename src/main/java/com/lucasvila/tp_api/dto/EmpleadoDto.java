package com.lucasvila.tp_api.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDto {

    @JsonProperty("numero_documento")
    @NotNull(message = "'numero_documento' es obligatorio.")
    private Integer numeroDocumento;

    @NotBlank(message = "'nombre' es obligatorio.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Solo se permiten letras en el campo ‘nombre’.")
    private String nombre;

    @NotBlank(message = "'apellido' es obligatorio.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Solo se permiten letras en el campo ‘apellido’.")
    private String apellido;

    @NotBlank(message = "'email' es obligatorio.")
    @Email(message = "El email ingresado no es correcto.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$", message = "El email debe terminar con '.com'.")
    private String email;

    @JsonProperty("fecha_nacimiento")
    @NotNull(message = "'fecha_nacimiento' es obligatorio.")
    private LocalDate fechaNacimiento;

    @JsonProperty("fecha_ingreso")
    @NotNull(message = "'fecha_ingreso' es obligatorio.")
    private LocalDate fechaIngreso;

    @JsonProperty("fecha_creacion")
    private LocalDate fechaCreacion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpleadoDto that = (EmpleadoDto) o;
        return Objects.equals(numeroDocumento, that.numeroDocumento) && Objects.equals(nombre, that.nombre) && Objects.equals(apellido, that.apellido) && Objects.equals(email, that.email) && Objects.equals(fechaNacimiento, that.fechaNacimiento) && Objects.equals(fechaIngreso, that.fechaIngreso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroDocumento, nombre, apellido, email, fechaNacimiento, fechaIngreso);
    }

    @Override
    public String toString() {
        return "EmpleadoDto{" +
                "numeroDocumento=" + numeroDocumento +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
