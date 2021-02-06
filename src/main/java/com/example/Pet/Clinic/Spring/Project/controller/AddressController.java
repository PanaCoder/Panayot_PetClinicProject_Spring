package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.AddressDto;
import com.example.Pet.Clinic.Spring.Project.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<Set<AddressDto>> findAll() {
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AddressDto> save(@RequestBody @Valid AddressDto addressDto) {
        AddressDto savedDto = addressService.save(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping
    public ResponseEntity<AddressDto> update(@RequestBody @Valid AddressDto addressDto) {
        return ResponseEntity.ok(addressService.update(addressDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        addressService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
