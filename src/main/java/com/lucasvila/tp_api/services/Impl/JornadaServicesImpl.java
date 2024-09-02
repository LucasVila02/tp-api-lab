package com.lucasvila.tp_api.services.Impl;

import com.lucasvila.tp_api.dto.JornadaRequestDTO;
import com.lucasvila.tp_api.dto.JornadaResponseDTO;
import com.lucasvila.tp_api.entities.ConceptoLaboral;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.entities.Jornada;
import com.lucasvila.tp_api.exceptions.BadRequestException;
import com.lucasvila.tp_api.exceptions.NoEncontradoException;
import com.lucasvila.tp_api.repositories.ConceptoLaborableRepository;
import com.lucasvila.tp_api.repositories.EmpleadosRepository;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.JornadaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
public class JornadaServicesImpl implements JornadaServices {

    @Autowired
    private JornadaRepository jornadaRepository;
    @Autowired
    private ConceptoLaborableRepository conceptoLaboralRepository;
    @Autowired
    private EmpleadosRepository empleadoRepository;


    @Override
    public List<Jornada> findByFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
        return jornadaRepository.findByFechaBetween(fechaDesde, fechaHasta);
    }

    @Override
    public List<Jornada> findByFechaDesde(LocalDate fechaDesde) {
        return jornadaRepository.findByFechaGreaterThanEqual(fechaDesde);
    }

    @Override
    public List<Jornada> findByFechaHasta(LocalDate fechaHasta) {
        return jornadaRepository.findByFechaLessThanEqual(fechaHasta);
    }

    @Override
    public List<Jornada> findByNroDocumento(Integer nroDocumento) {
        return jornadaRepository.findByEmpleadoNumeroDocumento(nroDocumento);
    }

    @Override
    public List<Jornada> findByFechaAndNroDocumento(LocalDate fechaDesde, LocalDate fechaHasta, Integer nroDocumento) {
        return jornadaRepository.findByFechaBetweenAndEmpleadoNumeroDocumento(fechaDesde, fechaHasta, nroDocumento);
    }

    @Override
    public List<Jornada> findAll() {
        return jornadaRepository.findAll();
    }



    @Transactional
    @Override
    public JornadaResponseDTO addJornada(JornadaRequestDTO jornadaDto) {


        ConceptoLaboral concepto = conceptoLaboralRepository.findById(jornadaDto.getConcepto())
                .orElseThrow(() -> new NoEncontradoException(jornadaDto.getConcepto(), "concepto"));

        // Validar si es 'Turno Normal' o 'Turno Extra' y verificar 'hsTrabajadas'
        if (isTurnoNormalOrExtra(concepto) && jornadaDto.getHorasTrabajadas() == null) {
            throw new BadRequestException("'hsTrabajadas' es obligatorio para el concepto ingresado.");
        }
        // Validar si el concepto es 'Día Libre' y verificar si se ingresó 'horasTrabajadas'
        if (!(isTurnoNormalOrExtra(concepto)) && jornadaDto.getHorasTrabajadas() != null) {
            throw new BadRequestException("El concepto ingresado no requiere el ingreso de 'hsTrabajadas'.");
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
                throw new BadRequestException("Las horas trabajadas no pueden ser nulas para el concepto: " + concepto.getNombre());
            }
        }

        // Aquí puedes continuar con la lógica de validación si horasTrabajadas no es null
        if (horasTrabajadas < concepto.getHsMinimo() || horasTrabajadas > concepto.getHsMaximo()) {
            throw new BadRequestException(String.format("El rango de horas que se puede cargar para este concepto es de %d - %d",
                    concepto.getHsMinimo(), concepto.getHsMaximo()));
        }
    }


    private void validarHorasDiarias(Empleado empleado, LocalDate fecha, Integer horasTrabajadas) {
        List<Jornada> jornadasDelDia = jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha);

        int horasTotalesDelDia = horasTrabajadas + jornadasDelDia.stream()
                .mapToInt(Jornada::getHorasTrabajadas)
                .sum();

        if (horasTotalesDelDia > 14) {
            throw new BadRequestException("Un empleado no puede cargar más de 14 horas trabajadas en un día.");
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
            throw new BadRequestException("El empleado ingresado supera las 52 horas semanales.");
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
            throw new BadRequestException("El empleado ingresado supera las 190 horas mensuales.");
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
            throw new BadRequestException("El empleado ingresado ya cuenta con 3 turnos extra esta semana.");
        }
    }

    private void validarDiaLibre(Empleado empleado, LocalDate fecha) {
        List<Jornada> jornadasDelDia = jornadaRepository.findByEmpleadoIdAndFecha(empleado.getId(), fecha);
        boolean tieneDiaLibre = jornadasDelDia.stream()
                .anyMatch(j -> "Día Libre".equals(j.getConceptoLaboral().getNombre()));

        if (tieneDiaLibre) {
            throw new BadRequestException("El empleado ingresado cuenta con un día libre en esa fecha.");
        }
    }


    private void validarTurnosNormalesSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        List<Jornada> turnosNormales = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfWeek, endOfWeek, "Turno Normal");

        if (turnosNormales.size() >= 5) {
            throw new BadRequestException("El empleado ingresado ya cuenta con 5 turnos normales esta semana.");
        }
    }

    private void validarDiasLibresSemanales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfWeek = fecha.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = fecha.with(DayOfWeek.SUNDAY);

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfWeek, endOfWeek, "Día Libre");

        if (diasLibres.size() >= 2) {
            throw new BadRequestException("El empleado no cuenta con más días libres esta semana.");
        }
    }

    private void validarDiasLibresMensuales(Empleado empleado, LocalDate fecha) {
        LocalDate startOfMonth = fecha.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = fecha.with(TemporalAdjusters.lastDayOfMonth());

        List<Jornada> diasLibres = jornadaRepository.findByEmpleadoIdAndFechaBetweenAndConceptoLaboralNombre(
                empleado.getId(), startOfMonth, endOfMonth, "Día Libre");

        if (diasLibres.size() >= 5) {
            throw new BadRequestException("El empleado no cuenta con más días libres este mes.");
        }
    }
    private void validarNumeroDeEmpleadosPorConcepto(ConceptoLaboral concepto, LocalDate fecha) {
        long empleadosRegistrados = jornadaRepository.countByConceptoLaboralAndFecha(concepto, fecha);

        if (empleadosRegistrados >= 2) {
            throw new BadRequestException("Ya existen 2 empleados registrados para este concepto en la fecha ingresada.");
        }
    }

    private void validarJornadaDuplicada(Empleado empleado, ConceptoLaboral concepto, LocalDate fecha) {
        boolean jornadaExistente = jornadaRepository.existsByEmpleadoIdAndConceptoLaboralAndFecha(empleado.getId(), concepto, fecha);

        if (jornadaExistente) {
            throw new BadRequestException("El empleado ya tiene registrado una jornada con este concepto en la fecha ingresada.");
        }
    }

}
