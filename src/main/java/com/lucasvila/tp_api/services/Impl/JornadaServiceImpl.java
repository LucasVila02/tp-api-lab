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
import com.lucasvila.tp_api.validation.ValidacionJornada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;


@Service
public class JornadaServiceImpl implements JornadaServices {

    @Autowired
    private JornadaRepository jornadaRepository;
    @Autowired
    private ConceptoLaborableRepository conceptoLaboralRepository;
    @Autowired
    private EmpleadosRepository empleadoRepository;

    @Autowired
    private ValidacionJornada validacionJornada;

    @Transactional(readOnly = true)
    @Override
    public List<Jornada> findJornadas(String fechaDesdeStr, String fechaHastaStr, String nroDocumento) {
        LocalDate fechaDesde = validacionJornada.validarFormatoFecha(fechaDesdeStr);
        LocalDate fechaHasta = validacionJornada.validarFormatoFecha(fechaHastaStr);

        validacionJornada.validarFechas(fechaDesde, fechaHasta);

        validacionJornada.validarNroDocumento(nroDocumento);

        // Convertir nroDocumento a Integer si es válido
        Integer documento = nroDocumento != null ? Integer.valueOf(nroDocumento) : null;

        // Mapa para asociar las combinaciones de parámetros con los métodos del repositorio
        Map<String, Supplier<List<Jornada>>> criterios = new LinkedHashMap<>();

        // Agrega las combinaciones posibles y sus métodos correspondientes al mapa
        criterios.put("fechaDesdeYFechaHastaYDocumento", () -> jornadaRepository.findByFechaBetweenAndEmpleadoNroDocumento(fechaDesde, fechaHasta, documento));
        criterios.put("fechaDesdeYFechaHasta", () -> jornadaRepository.findByFechaBetween(fechaDesde, fechaHasta));
        criterios.put("nroDocumento", () -> jornadaRepository.findByEmpleadoNroDocumento(documento));
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


    @Transactional
    @Override
    public JornadaResponseDTO addJornada(JornadaRequestDTO jornadaDto) {
        boolean esTurnoExtra = jornadaDto.getConcepto() == 2L; // ID 2L es turno extra
        boolean esDiaLibre = jornadaDto.getConcepto() ==3L;

        ConceptoLaboral concepto = conceptoLaboralRepository.findById(jornadaDto.getConcepto())
                .orElseThrow(() -> new NoEncontradoException(jornadaDto.getConcepto(), "concepto"));

        validacionJornada.validarHorasTrabajadas(concepto, jornadaDto.getHsTrabajadas());

        Empleado empleado = empleadoRepository.findById(jornadaDto.getEmpleado())
                .orElseThrow(() -> new NoEncontradoException(jornadaDto.getEmpleado(), "empleado"));

        validacionJornada.validarRangoHoras(concepto, jornadaDto.getHsTrabajadas()); //ok
        validacionJornada.validarDiaLibre(empleado, jornadaDto.getFecha());//ok
        validacionJornada.validarTurnoJornada(jornadaDto.getFecha(), concepto, empleado);
        validacionJornada.validarNumeroDeEmpleadosPorConcepto(concepto, jornadaDto.getFecha());//ok
        validacionJornada.validarJornadaDuplicada(empleado, concepto, jornadaDto.getFecha());//ok
        validacionJornada.validarTurnosNormalesSemanales(empleado, jornadaDto.getFecha());//ok

        if (!esDiaLibre) {
            validacionJornada.validarHorasDiarias(empleado, jornadaDto.getFecha(), jornadaDto.getHsTrabajadas());//ok
            validacionJornada.validarHorasSemanales(empleado, jornadaDto.getHsTrabajadas(), jornadaDto.getFecha());//ok
            validacionJornada.validarHorasMensuales(empleado, jornadaDto.getHsTrabajadas(), jornadaDto.getFecha());//ok
        }
        if (esTurnoExtra) {
            validacionJornada.validarTurnosExtraSemanales(empleado, jornadaDto.getFecha(), true);
        }

        if (esDiaLibre) {
            validacionJornada.validarDiasLibresSemanales(empleado, jornadaDto.getFecha());//funciona
            validacionJornada.validarDiasLibresMensuales(empleado, jornadaDto.getFecha());//funciona
        };


        // Crear la entidad Jornada
        Jornada jornada = new Jornada();
        jornada.setEmpleado(empleado);
        jornada.setConceptoLaboral(concepto);
        jornada.setFecha(jornadaDto.getFecha());
        jornada.setHsTrabajadas(jornadaDto.getHsTrabajadas());

        // Guardar la jornada y mapear a DTO de respuesta
        jornada = jornadaRepository.save(jornada);

        System.out.println(jornada);

        return jornada.toResponseDTO();
    }
}
