package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;
import com.example.Pet.Clinic.Spring.Project.exception.BadRequestException;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Pet;
import com.example.Pet.Clinic.Spring.Project.model.Visit;
import com.example.Pet.Clinic.Spring.Project.reposirtory.VisitRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetService petService;
    private final ModelMapper modelMapper;

    @Autowired
    public VisitServiceImpl(VisitRepository visitRepository, PetService petService, ModelMapper modelMapper) {
        this.visitRepository = visitRepository;
        this.petService = petService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<VisitDto> findAll() {
        return visitRepository.findAll()
                .stream()
                .map(visit -> modelMapper.map(visit, VisitDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public VisitDto findById(@NonNull Long id) {
        Visit visit = visitRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Visit with id: " + id + " is not found."));
        return modelMapper.map(visit, VisitDto.class);
    }

    @Override
    public VisitDto save(@NonNull VisitDto visitDto) {
        try {
            visitDto.setId(null);
            PetDto petDto = petService.findById(visitDto.getPetId());
            Pet pet = modelMapper.map(petDto, Pet.class);
            Visit visit = modelMapper.map(visitDto, Visit.class);
            visit.setPet(pet);
            visitRepository.save(visit);
            return modelMapper.map(visit, VisitDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Visit with id " + visitDto.getId() + " already exist.");
        }
    }

    @Override
    public VisitDto update(@NonNull VisitDto visitDto) {
        Optional<Visit> maybeVisit = visitRepository.findById(visitDto.getId());
        if (maybeVisit.isPresent()) {
            PetDto petDto = petService.findById(visitDto.getPetId());
            Pet pet = modelMapper.map(petDto, Pet.class);
            Visit visit = modelMapper.map(visitDto, Visit.class);
            visit.setPet(pet);
            visitRepository.save(visit);
            return modelMapper.map(visit, VisitDto.class);
        }
        throw new BadRequestException("Visit with id: " + visitDto.getId() + " does not exist.");
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            visitRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("Visit with id: " + id + " is not found");
        }
    }
}
