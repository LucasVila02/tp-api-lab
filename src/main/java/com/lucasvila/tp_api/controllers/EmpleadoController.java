package com.lucasvila.tp_api.controllers;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
import com.lucasvila.tp_api.exceptions.EmpleadoNoEncontradoException;
import com.lucasvila.tp_api.repositories.JornadaRepository;
import com.lucasvila.tp_api.services.EmpleadosServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class EmpleadoController {

    @Autowired
    private EmpleadosServices services;
//
//    @Autowired
//    private JornadaRepository jornadaRepository;

    @GetMapping("/empleado")
    public List<EmpleadoDto> findAll(){
        return services.findAll();
    }

    @GetMapping("/empleado/{id}")
    public ResponseEntity<EmpleadoDto> findById(/*@NotNull*/ @PathVariable Long id){
        Optional<Empleado> empleadosOptional = services.findById(id);

        if (empleadosOptional.isPresent()){
            EmpleadoDto empleadoDto = empleadosOptional.get().toDTO();
            return ResponseEntity.ok(empleadoDto);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/empleado")
    public ResponseEntity<EmpleadoDto> create(@Valid @RequestBody  EmpleadoDto empleado /*BindingResult result*/){
        EmpleadoDto creado =services.create(empleado) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/empleado/{id}")
    public ResponseEntity<EmpleadoDto> update(@Valid  @RequestBody EmpleadoDto empleadoDto, @PathVariable Long id){
        Optional<EmpleadoDto> updatedEmpleado  = services.update(id, empleadoDto);
//        EmpleadoDto empleadoUpdate =
        // Retorna el DTO actualizado con un código de estado 200 OK
        // Retorna un código de estado 404 Not Found si el empleado no se encuentra
        return updatedEmpleado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/empleado/{id}")
    public ResponseEntity delete(@PathVariable Long id){ /*sin <?> para generico*/

//        if (jornadaRepository.existsByEmpleado(empleado)) {
//            throw new BusinessException("No se puede eliminar el empleado porque tiene jornadas asociadas.");
//        }
        Optional<Empleado> optionalEmpleado = services.delete(id);
        if (optionalEmpleado.isPresent()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("message", "El empleado fue eliminado con éxito.").build();

        }
        return ResponseEntity.notFound().build();

    }

}
