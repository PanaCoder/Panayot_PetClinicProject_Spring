package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public ResponseEntity<Set<OwnerDto>> findAll() {
        return ResponseEntity.ok(ownerService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OwnerDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.findById(id));
    }

    @GetMapping(value = "/phone/{phone}")
    public ResponseEntity<OwnerDto> findByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(ownerService.findByPhone(phone));
    }

    @PostMapping
    public ResponseEntity<OwnerDto> save(@RequestBody @Valid OwnerDto ownerDto) {
        OwnerDto savedDto = ownerService.save(ownerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping
    public ResponseEntity<OwnerDto> update(@RequestBody @Valid OwnerDto ownerDto) {
        return ResponseEntity.ok(ownerService.update(ownerDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        ownerService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/phone/{phone}")
    public ResponseEntity<HttpStatus> deleteByPhone(@PathVariable String phone) {
        ownerService.deleteByPhone(phone);
        return ResponseEntity.ok().build();
    }

}
