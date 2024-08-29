package com.lucasvila.tp_api.entities;

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

}
