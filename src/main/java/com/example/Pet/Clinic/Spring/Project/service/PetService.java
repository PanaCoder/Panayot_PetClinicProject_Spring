package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import java.util.Set;

public interface PetService {

    Set<PetDto> findAll();

    PetDto findById(Long id);

    PetDto save(PetDto petDto);

    PetDto update(PetDto petDto);

    void deleteById(Long id);
}
