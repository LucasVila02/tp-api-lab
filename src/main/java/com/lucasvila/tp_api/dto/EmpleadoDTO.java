package com.lucasvila.tp_api.dto;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.validation.anotation.ValidName;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmpleadoDTO {

    private long id;

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

    @NotNull(message = "'fechaNacimiento' es obligatorio.")
    private LocalDate fechaNacimiento;

    @NotNull(message = "'fechaIngreso' es obligatorio.")
    private LocalDate fechaIngreso;

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

}
