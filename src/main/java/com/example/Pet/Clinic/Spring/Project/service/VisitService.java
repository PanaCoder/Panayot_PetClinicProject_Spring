package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;

import java.util.Set;

public interface VisitService {

    Set<VisitDto> findAll();

    VisitDto findById(Long id);

    VisitDto save(VisitDto visitDto);

    VisitDto update(VisitDto visitDto);

    void deleteById(Long id);
}
