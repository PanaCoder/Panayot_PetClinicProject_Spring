package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<Set<PetDto>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PetDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PetDto> save(@RequestBody @Valid PetDto petDto) {
        PetDto savedDto = petService.save(petDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping
    public ResponseEntity<PetDto> update(@RequestBody @Valid PetDto petDto) {
        return ResponseEntity.ok(petService.update(petDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        petService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
