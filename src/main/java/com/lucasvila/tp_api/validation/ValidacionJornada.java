package com.lucasvila.tp_api.validation;

import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.exceptions.FechaInvalidaException;
import com.lucasvila.tp_api.exceptions.HorasTurnosInvalidoException;
import com.lucasvila.tp_api.exceptions.NroDocumentoInvalidoException;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ValidacionJornada {

    @Autowired
    private JornadaRepository jornadaRepository;

    // ===================
    // Métodos de Validación de Fechas
    // ===================

    public void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        // Validación de las fechas
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new FechaInvalidaException("El campo ‘fechaDesde’ no puede ser mayor que ‘fechaHasta’.");
        }
    }

    public LocalDate validarFormatoFecha(String fechaStr) {
        if (fechaStr != null) {
            try {
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new FechaInvalidaException("Los campos ‘fechaDesde’ y ‘fechaHasta’ deben respetar el formato yyyy-mm-dd.");
            }
        }
        return null;
    }

    // ===================
    // Métodos de Validación de Números
    // ===================

    public void validarNroDocumento(String nroDocumento) {
        if (nroDocumento != null) {
            try {
                Integer.parseInt(nroDocumento);
            } catch (NumberFormatException e) {
                throw new NroDocumentoInvalidoException("El campo ‘nroDocumento’ solo puede contener números enteros.");
            }
        }
    }
    // ===================
    // Métodos de Validación de Horas
    // ===================

    public void validarHorasTrabajadas(ConceptoLaboral concepto, Integer horasTrabajadas) {
        if (isTurnoNormalOrExtra(concepto) && horasTrabajadas == null) {
            throw new HorasTurnosInvalidoException("'hsTrabajadas' es obligatorio para el concepto ingresado.");
        }
        if (!isTurnoNormalOrExtra(concepto) && horasTrabajadas != null) {
            throw new HorasTurnosInvalidoException("El concepto ingresado no requiere el ingreso de 'hsTrabajadas'.");
        }
    }

    private boolean isTurnoNormalOrExtra(ConceptoLaboral concepto) {
        return "Turno Normal".equals(concepto.getNombre()) || "Turno Extra".equals(concepto.getNombre());
    }


    public void validarRangoHoras(ConceptoLaboral concepto, Integer horasTrabajadas) {
        // Verificar si horasTrabajadas es null
        if (horasTrabajadas == null) {
            // Si horasTrabajadas es null y el concepto es "Día Libre", puedes simplemente retornar
            if ("Día Libre".equals(concepto.getNombre())) {
                return; // No hay necesidad de validar horas para "Día Libre"
            } else {
                throw new HorasTurnosInvalidoException("Las horas trabajadas no pueden ser nulas para el concepto: " + concepto.getNombre());
            }
        }
        // Aquí puedes continuar con la lógica de validación si horasTrabajadas no es null
        if (horasTrabajadas < concepto.getHsMinimo() || horasTrabajadas > concepto.getHsMaximo()) {
            throw new HorasTurnosInvalidoException(String.format("El rango de horas que se puede cargar para este concepto es de %d - %d",
                    concepto.getHsMinimo(), concepto.getHsMaximo()));
        }
    }


    public void validarHorasDiarias(Empleado empleado, LocalDate fecha, Integer horasTrabajadas) {
//        List<Jornada> jornadasDelDia = jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha);
        List<Jornada> jornadasDelDia = Optional.ofNullable(
                jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha)
        ).orElse(Collections.emptyList());

        // Calcular el total de horas trabajadas en el día
        int horasTotalesDelDia = horasTrabajadas + jornadasDelDia.stream().filter(Objects::nonNull)
                .mapToInt(Jornada::getHsTrabajadas)
                .sum();

        // Verificar si las horas totales del día exceden el límite permitido
        if (horasTotalesDelDia > 14) {
            throw new HorasTurnosInvalidoException("Un empleado no puede cargar más de 14 horas trabajadas en un día.");
        }
        System.out.println("Total de horas trabajadas en el dia: " + horasTotalesDelDia);
    }

    public void validarHorasSemanales(Empleado empleado, Integer horasTrabajadas, LocalDate fecha) {
        // Obtener el primer y último día de la semana
        LocalDate startOfWeek = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = fecha.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));


        // Obtener todas las jornadas de la semana
        List<Jornada> jornadasDeLaSemana = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfWeek, endOfWeek);

        // Sumar las horas trabajadas en la semana
        int horasTotalesSemanales = horasTrabajadas + jornadasDeLaSemana.stream()
                .filter(Objects::nonNull) // Filtra cualquier elemento nulo en la lista
                .mapToInt(jornada -> jornada.getHsTrabajadas() != null ? jornada.getHsTrabajadas() : 0) // Maneja posibles valores null
                .sum();

        // Verificar si supera las 52 horas
        if (horasTotalesSemanales > 52) {
            throw new HorasTurnosInvalidoException("El empleado ingresado supera las 52 horas semanales.");
        }

        System.out.println("Total de horas trabajadas en la semana: " + horasTotalesSemanales);
    }
    public void validarHorasMensuales(Empleado empleado, Integer horasTrabajadas, LocalDate fecha) {
        LocalDate startOfMonth = fecha.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = fecha.with(TemporalAdjusters.lastDayOfMonth());

        List<Jornada> jornadasDelMes = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfMonth, endOfMonth);
        int horasTotalesMensuales = horasTrabajadas + jornadasDelMes.stream()
                .filter(Objects::nonNull) // Filtra cualquier elemento nulo en la lista
                .mapToInt(jornada -> jornada.getHsTrabajadas() != null ? jornada.getHsTrabajadas() : 0) // Maneja posibles valores null
                .sum();

        if (horasTotalesMensuales > 190) {
            throw new HorasTurnosInvalidoException("El empleado ingresado supera las 190 horas mensuales.");
        }
        System.out.println("Total de horas trabajadas en el mes: " + horasTotalesMensuales);
    }

    // ===================
    // Métodos de Validación de Turnos
    // ===================

    public void validarTurnosExtraSemanales(Empleado empleado, LocalDate fecha, boolean esTurnoExtra) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        // Obtener todas las jornadas de la semana
        List<Jornada> todasLasJornadas = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfWeek, endOfWeek);

        // Filtrar y contar los turnos extra
        long cantidadTurnosExtra = todasLasJornadas.stream()
                .filter(jornada -> jornada.getConceptoLaboral().getId() == 2L) // Asumiendo que ID 2L corresponde a turno extra
                .count();

        // Verificar si se intenta agregar otro turno extra cuando ya se alcanzaron los 3
        if (esTurnoExtra && cantidadTurnosExtra >= 3) {
            throw new HorasTurnosInvalidoException("El empleado ingresado ya cuenta con 3 turnos extra esta semana.");
        }
    }

    public void validarTurnoJornada(LocalDate fecha, ConceptoLaboral conceptoLaboral, Empleado empleado) {
        // Verifica si el concepto laboral es "Día libre" (ID 3 en este caso)
        if (conceptoLaboral.getId() == 3) {
            // Verifica si el empleado ya tiene una jornada laboral en esa fecha
            if (tieneHorasDeTrabajo(empleado, fecha)) {
                throw new HorasTurnosInvalidoException("El empleado ya tiene una jornada laboral registrada en esta fecha y no puede agregar un día libre.");
            }
        }
    }

    public void validarDiaLibre(Empleado empleado, LocalDate fecha) {
        // Obtener las jornadas del día para el empleado
        List<Jornada> jornadasDelDia = Optional.ofNullable(
                jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha)
        ).orElse(Collections.emptyList());

        // Verificar si ya tiene un día libre registrado en la fecha
        boolean tieneDiaLibre = jornadasDelDia.stream()
                .anyMatch(j -> j.getConceptoLaboral() != null
                        && j.getConceptoLaboral().getId() == 3L); // Día libre

        if (tieneDiaLibre) {
            throw new HorasTurnosInvalidoException("El empleado ingresado cuenta con un día libre en esa fecha");
        }
    }

    public void validarTurnosNormalesSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        // Obtener todas las jornadas dentro de la semana
        List<Jornada> todasLasJornadas = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfWeek, endOfWeek);

        // Filtrar solo los turnos normales, excluyendo los días libres y turnos extra
        long countTurnosNormales = todasLasJornadas.stream()
                .filter(j -> j.getConceptoLaboral().getId() == 1L)
                .count();

        if (countTurnosNormales >= 5) {
            throw new HorasTurnosInvalidoException("El empleado ingresado ya cuenta con 5 turnos normales esta semana.");
        }
    }

    public void validarDiasLibresSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfWeek, endOfWeek, "Día Libre");

        if ( diasLibres.size() >= 2) {
            throw new HorasTurnosInvalidoException("El empleado no cuenta con más días libres esta semana.");
        }
    }

    public void validarDiasLibresMensuales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfMonth = fecha.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = fecha.with(TemporalAdjusters.lastDayOfMonth());

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfMonth, endOfMonth, "Día Libre");

        if (diasLibres.size() >= 5) {
            throw new HorasTurnosInvalidoException("El empleado no cuenta con más días libres este mes.");
        }
    }


    // ===================
    // Métodos de Auxiliares
    // ===================

    public void validarNumeroDeEmpleadosPorConcepto(ConceptoLaboral concepto, LocalDate fecha) {
        long empleadosRegistrados = jornadaRepository.countByConceptoLaboralAndFecha(concepto, fecha);

        if (empleadosRegistrados >= 2) {
            throw new HorasTurnosInvalidoException("Ya existen 2 empleados registrados para este concepto en la fecha ingresada.");
        }
    }

    public void validarJornadaDuplicada(Empleado empleado, ConceptoLaboral concepto, LocalDate fecha) {
        boolean jornadaExistente = jornadaRepository.existsByEmpleadoIdAndConceptoLaboralAndFecha(empleado.getId(), concepto, fecha);

        if (jornadaExistente) {
            throw new HorasTurnosInvalidoException("El empleado ya tiene registrado una jornada con este concepto en la fecha ingresada.");
        }
    }

    // Método auxiliar que verifica si el empleado tiene horas de trabajo en una fecha específica
    private boolean tieneHorasDeTrabajo(Empleado empleado, LocalDate fecha) {
        // Obtener las jornadas del día para el empleado
        List<Jornada> jornadasDelDia = Optional.ofNullable(
                jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha)
        ).orElse(Collections.emptyList());

        // Verificar si alguna de las jornadas tiene horas trabajadas en esa fecha
        return jornadasDelDia.stream()
                .anyMatch(j -> j.getHsTrabajadas() != null && j.getHsTrabajadas() > 0);
    }

}
