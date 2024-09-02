package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.exceptions.*;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.JornadaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


@Service
public class JornadaServicesImpl implements JornadaServices {

    @Autowired
    private JornadaRepository jornadaRepository;
    @Autowired
    private ConceptoLaborableRepository conceptoLaboralRepository;
    @Autowired
    private EmpleadosRepository empleadoRepository;


    @Override
    public List<Jornada> findJornadas(String fechaDesdeStr, String fechaHastaStr, String nroDocumento) {
        LocalDate fechaDesde = parseFecha(fechaDesdeStr);
        LocalDate fechaHasta = parseFecha(fechaHastaStr);

        validarFechas(fechaDesde, fechaHasta);

        validarNroDocumento(nroDocumento);

        // Convertir nroDocumento a Integer si es válido
        Integer documento = nroDocumento != null ? Integer.valueOf(nroDocumento) : null;

        // Mapa para asociar las combinaciones de parámetros con los métodos del repositorio
        Map<String, Supplier<List<Jornada>>> criterios = new LinkedHashMap<>();

        // Agrega las combinaciones posibles y sus métodos correspondientes al mapa
        criterios.put("fechaDesdeYFechaHastaYDocumento", () -> jornadaRepository.findByFechaBetweenAndEmpleadoNumeroDocumento(fechaDesde, fechaHasta, documento));
        criterios.put("fechaDesdeYFechaHasta", () -> jornadaRepository.findByFechaBetween(fechaDesde, fechaHasta));
        criterios.put("nroDocumento", () -> jornadaRepository.findByEmpleadoNumeroDocumento(documento));
        criterios.put("fechaDesde", () -> jornadaRepository.findByFechaAfter(fechaDesde));
        criterios.put("fechaHasta", () -> jornadaRepository.findByFechaBefore(fechaHasta));
        criterios.put("default", jornadaRepository::findAll);

        // Determina qué combinación de parámetros se ha proporcionado y llama al método correspondiente
        return criterios.entrySet().stream()
                .filter(entry -> {
                    switch (entry.getKey()) {
                        case "fechaDesdeYFechaHastaYDocumento":
                            return fechaDesde != null && fechaHasta != null && nroDocumento != null;
                        case "fechaDesdeYFechaHasta":
                            return fechaDesde != null && fechaHasta != null;
                        case "nroDocumento":
                            return nroDocumento != null;
                        case "fechaDesde":
                            return fechaDesde != null && fechaHasta == null;
                        case "fechaHasta":
                            return fechaHasta != null && fechaDesde == null;
                        default:
                            return fechaDesde == null && fechaHasta == null && nroDocumento == null;
                    }
                })
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(criterios.get("default"))
                .get();
    }

    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        // Validación de las fechas
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new FechaInvalidaException("El campo ‘fechaDesde’ no puede ser mayor que ‘fechaHasta’.");
        }
    }

    private void validarNroDocumento(String nroDocumento) {
        if (nroDocumento != null) {
            try {
                Integer.parseInt(nroDocumento);
            } catch (NumberFormatException e) {
                throw new NroDocumentoInvalido("El campo ‘nroDocumento’ solo puede contener números enteros.");
            }
        }
    }
    private LocalDate parseFecha(String fechaStr) {
        if (fechaStr != null) {
            try {
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new FechaInvalidaException("Los campos ‘fechaDesde’ y ‘fechaHasta’ deben respetar el formato yyyy-mm-dd.");
            }
        }
        return null;
    }




    @Transactional
    @Override
    public JornadaResponseDTO addJornada(JornadaRequestDTO jornadaDto) {


        ConceptoLaboral concepto = conceptoLaboralRepository.findById(jornadaDto.getConcepto())
                .orElseThrow(() -> new NoEncontradoException(jornadaDto.getConcepto(), "concepto"));

        // Validar si es 'Turno Normal' o 'Turno Extra' y verificar 'hsTrabajadas'
        if (isTurnoNormalOrExtra(concepto) && jornadaDto.getHorasTrabajadas() == null) {
            throw new HorasTurnosInvalidException("'hsTrabajadas' es obligatorio para el concepto ingresado.");
        }
        // Validar si el concepto es 'Día Libre' y verificar si se ingresó 'horasTrabajadas'
        if (!(isTurnoNormalOrExtra(concepto)) && jornadaDto.getHorasTrabajadas() != null) {
            throw new HorasTurnosInvalidException("El concepto ingresado no requiere el ingreso de 'hsTrabajadas'.");
        }

        Empleado empleado = empleadoRepository.findById(jornadaDto.getEmpleado())
                .orElseThrow(() -> new NoEncontradoException(jornadaDto.getEmpleado(), "empleado"));

        if (concepto.getId() !=3){
            validarRangoHoras(concepto, jornadaDto.getHorasTrabajadas()); //ok
            validarHorasDiarias(empleado, jornadaDto.getFecha(), jornadaDto.getHorasTrabajadas());//ok
            validarHorasSemanales(empleado, jornadaDto.getHorasTrabajadas(), jornadaDto.getFecha());//ok
            validarHorasMensuales(empleado, jornadaDto.getHorasTrabajadas(), jornadaDto.getFecha());//ok
        }

        validarDiaLibre(empleado, jornadaDto.getFecha());//ok
        validarTurnosExtraSemanales( empleado,  jornadaDto.getFecha());
        validarTurnosExtraSemanales(empleado, jornadaDto.getFecha());//despues de 3 dia extra no deja agregar dias normales
        validarTurnosNormalesSemanales(empleado, jornadaDto.getFecha());//ok
        validarDiasLibresSemanales(empleado, jornadaDto.getFecha());//funciona
        validarDiasLibresMensuales(empleado, jornadaDto.getFecha());//funciona
        validarNumeroDeEmpleadosPorConcepto(concepto, jornadaDto.getFecha());//ok
        validarJornadaDuplicada(empleado, concepto, jornadaDto.getFecha());//ok

        // Crear la entidad Jornada
        Jornada jornada = new Jornada();
        jornada.setEmpleado(empleado);
        jornada.setConceptoLaboral(concepto);
        jornada.setFecha(jornadaDto.getFecha());
        jornada.setHorasTrabajadas(jornadaDto.getHorasTrabajadas());

        // Guardar la jornada y mapear a DTO de respuesta
        jornada = jornadaRepository.save(jornada);

        return jornada.toResponseDTO();
    }



    private boolean isTurnoNormalOrExtra(ConceptoLaboral concepto) {
        return "Turno Normal".equals(concepto.getNombre()) || "Turno Extra".equals(concepto.getNombre());
    }
    private void validarRangoHoras(ConceptoLaboral concepto, Integer horasTrabajadas) {
        // Verificar si horasTrabajadas es null
        if (horasTrabajadas == null) {
            // Si horasTrabajadas es null y el concepto es "Día Libre", puedes simplemente retornar
            if ("Día Libre".equals(concepto.getNombre())) {
                return; // No hay necesidad de validar horas para "Día Libre"
            } else {
                throw new HorasTurnosInvalidException("Las horas trabajadas no pueden ser nulas para el concepto: " + concepto.getNombre());
            }
        }

        // Aquí puedes continuar con la lógica de validación si horasTrabajadas no es null
        if (horasTrabajadas < concepto.getHsMinimo() || horasTrabajadas > concepto.getHsMaximo()) {
            throw new HorasTurnosInvalidException(String.format("El rango de horas que se puede cargar para este concepto es de %d - %d",
                    concepto.getHsMinimo(), concepto.getHsMaximo()));
        }
    }


    private void validarHorasDiarias(Empleado empleado, LocalDate fecha, Integer horasTrabajadas) {
        List<Jornada> jornadasDelDia = jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha);

        int horasTotalesDelDia = horasTrabajadas + jornadasDelDia.stream()
                .mapToInt(Jornada::getHorasTrabajadas)
                .sum();

        if (horasTotalesDelDia > 14) {
            throw new HorasTurnosInvalidException("Un empleado no puede cargar más de 14 horas trabajadas en un día.");
        }
    }

    private void validarHorasSemanales(Empleado empleado, Integer horasTrabajadas, LocalDate fecha) {
        // Obtener el primer y último día de la semana
        LocalDate startOfWeek = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = fecha.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Obtener todas las jornadas de la semana
        List<Jornada> jornadasDeLaSemana = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfWeek, endOfWeek);

        // Sumar las horas trabajadas en la semana
        int horasTotalesSemanales = horasTrabajadas + jornadasDeLaSemana.stream()
                .mapToInt(Jornada::getHorasTrabajadas)
                .sum();

        System.out.println("Total de horas trabajadas en la semana: " + horasTotalesSemanales);

        // Verificar si supera las 52 horas
        if (horasTotalesSemanales > 52) {
            throw new HorasTurnosInvalidException("El empleado ingresado supera las 52 horas semanales.");
        }
    }
    private void validarHorasMensuales(Empleado empleado, Integer horasTrabajadas, LocalDate fecha) {
        LocalDate startOfMonth = fecha.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = fecha.with(TemporalAdjusters.lastDayOfMonth());

        List<Jornada> jornadasDelMes = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfMonth, endOfMonth);
        int horasTotalesMensuales = horasTrabajadas + jornadasDelMes.stream()
                .mapToInt(Jornada::getHorasTrabajadas)
                .sum();

        System.out.println("Total de horas trabajadas en la mensuales: " + horasTotalesMensuales);
        if (horasTotalesMensuales > 190) {
            throw new HorasTurnosInvalidException("El empleado ingresado supera las 190 horas mensuales.");
        }
    }
    private void validarTurnosExtraSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        // Obtener todas las jornadas de la semana
        List<Jornada> todasLasJornadas = jornadaRepository.findByEmpleadoIdAndFechaBetween(empleado.getId(), startOfWeek, endOfWeek);

        // Filtrar y contar los turnos extra
        long cantidadTurnosExtra = todasLasJornadas.stream()
                .filter(jornada -> "Turno Extra".equals(jornada.getConceptoLaboral().getNombre()))
                .count();

        // Verificar si ya se excedieron los 3 turnos extra
        if (cantidadTurnosExtra >= 3) {
            throw new HorasTurnosInvalidException("El empleado ingresado ya cuenta con 3 turnos extra esta semana.");
        }
    }

    private void validarDiaLibre(Empleado empleado, LocalDate fecha) {
        List<Jornada> jornadasDelDia = jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha);
        boolean tieneDiaLibre = jornadasDelDia.stream()
                .anyMatch(j -> "Día Libre".equals(j.getConceptoLaboral().getNombre()));

        if (tieneDiaLibre) {
            throw new HorasTurnosInvalidException("El empleado ingresado cuenta con un día libre en esa fecha.");
        }
    }


    private void validarTurnosNormalesSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        List<Jornada> turnosNormales = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfWeek, endOfWeek, "Turno Normal");

        if (turnosNormales.size() >= 5) {
            throw new HorasTurnosInvalidException("El empleado ingresado ya cuenta con 5 turnos normales esta semana.");
        }
    }

    private void validarDiasLibresSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfWeek, endOfWeek, "Día Libre");

        if (diasLibres.size() >= 2) {
            throw new HorasTurnosInvalidException("El empleado no cuenta con más días libres esta semana.");
        }
    }

    private void validarDiasLibresMensuales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfMonth = fecha.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = fecha.with(TemporalAdjusters.lastDayOfMonth());

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfMonth, endOfMonth, "Día Libre");

        if (diasLibres.size() >= 5) {
            throw new HorasTurnosInvalidException("El empleado no cuenta con más días libres este mes.");
        }
    }
    private void validarNumeroDeEmpleadosPorConcepto(ConceptoLaboral concepto, LocalDate fecha) {
        long empleadosRegistrados = jornadaRepository.countByConceptoLaboralAndFecha(concepto, fecha);

        if (empleadosRegistrados >= 2) {
            throw new HorasTurnosInvalidException("Ya existen 2 empleados registrados para este concepto en la fecha ingresada.");
        }
    }

    private void validarJornadaDuplicada(Empleado empleado, ConceptoLaboral concepto, LocalDate fecha) {
        boolean jornadaExistente = jornadaRepository.existsByEmpleadoIdAndConceptoLaboralAndFecha(empleado.getId(), concepto, fecha);

        if (jornadaExistente) {
            throw new HorasTurnosInvalidException("El empleado ya tiene registrado una jornada con este concepto en la fecha ingresada.");
        }
    }



}
