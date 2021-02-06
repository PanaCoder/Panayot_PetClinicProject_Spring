package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;

import java.util.Set;

public interface OwnerService {

    Set<OwnerDto> findAll();

    OwnerDto findById(Long id);

    OwnerDto findByPhone(String phone);

    OwnerDto save(OwnerDto ownerDto);

    OwnerDto update(OwnerDto ownerDto);

    void deleteById(Long id);

    void deleteByPhone(String phone);

}
