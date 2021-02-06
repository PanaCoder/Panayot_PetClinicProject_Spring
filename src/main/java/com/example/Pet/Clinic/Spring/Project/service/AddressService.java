package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.AddressDto;

import java.util.Set;

public interface AddressService {

    Set<AddressDto> findAll();

    AddressDto findById(Long id);

    AddressDto save(AddressDto addressDto);

    AddressDto update(AddressDto addressDto);

    void deleteById(Long id);

}
