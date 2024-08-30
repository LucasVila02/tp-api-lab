package com.lucasvila.tp_api.controllers;

import com.lucasvila.tp_api.dto.EmpleadoDto;
import com.lucasvila.tp_api.entities.Empleado;
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

    @GetMapping("/empleado")
    public List<Empleado> findAll(){
        return services.findAll();
    }

    @GetMapping("/empleado/{id}")
    public ResponseEntity<?> findById(/*@NotNull*/ @PathVariable Long id){
        Optional<Empleado> empleadosOptional = services.findById(id);

        if (empleadosOptional.isPresent()){
            return ResponseEntity.ok(empleadosOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/empleado")
    public ResponseEntity<?> create(@Valid @RequestBody  EmpleadoDto empleado /*BindingResult result*/){

        return ResponseEntity.status(HttpStatus.CREATED).body(services.create(empleado));
    }

    @PutMapping("/empleado/{id}")
    public ResponseEntity<Empleado> update(@Valid  @RequestBody Empleado empleado, @PathVariable Long id){
        Optional<Empleado> optionalEmpleado = services.update(id, empleado);

        return ResponseEntity.status(HttpStatus.CREATED).body(optionalEmpleado.orElseThrow());
    }

    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){ /*sin <?> para generico*/
        Optional<Empleado> optionalEmpleado = services.delete(id);
        if (optionalEmpleado.isPresent()){
            return ResponseEntity.ok(optionalEmpleado.orElseThrow());
//            return ResponseEntity.noContent().body() debe devolver noCOntent esto y update

        }
        return ResponseEntity.notFound().build();

    }

}
