package com.lucasvila.tp_api.controllers;

import com.lucasvila.tp_api.dto.EmpleadoDTO;
import com.lucasvila.tp_api.services.EmpleadosServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class EmpleadoController {

    @Autowired
    private EmpleadosServices services;

    @GetMapping("/empleado")
    public List<EmpleadoDTO> findAll(){
        return services.findAll();
    }

    @GetMapping("/empleado/{id}")
    public ResponseEntity<EmpleadoDTO> findById(@PathVariable Long id) {
        Optional<EmpleadoDTO> empleadoDtoOptional = services.findById(id);
        return empleadoDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/empleado")
    public ResponseEntity<EmpleadoDTO> create(@Valid @RequestBody EmpleadoDTO empleado /*BindingResult result*/){
        EmpleadoDTO empleadoCreado =services.create(empleado) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoCreado);
    }

    @PutMapping("/empleado/{id}")
    public ResponseEntity<EmpleadoDTO> update(@Valid  @RequestBody EmpleadoDTO empleadoDto, @PathVariable Long id){
        Optional<EmpleadoDTO> updatedEmpleado  = services.update(id, empleadoDto);
        return updatedEmpleado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        this.services.delete(id);
        return ResponseEntity.noContent().header("message", "El empleado fue eliminado con éxito.").build();
    }

}
