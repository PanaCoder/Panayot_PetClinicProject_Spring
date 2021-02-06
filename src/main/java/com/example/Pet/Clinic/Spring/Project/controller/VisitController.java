package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;
import com.example.Pet.Clinic.Spring.Project.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/visits")
public class VisitController {
    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public ResponseEntity<Set<VisitDto>> findAll() {
        return ResponseEntity.ok(visitService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VisitDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.findById(id));
    }

    @PostMapping
    public ResponseEntity<VisitDto> save(@RequestBody @Valid VisitDto visitDto) {
        VisitDto savedDto = visitService.save(visitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping
    public ResponseEntity<VisitDto> update(@RequestBody @Valid VisitDto visitDto) {
        return ResponseEntity.ok(visitService.update(visitDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        visitService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
