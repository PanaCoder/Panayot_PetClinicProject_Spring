package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.exception.BadRequestException;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Owner;
import com.example.Pet.Clinic.Spring.Project.model.Pet;
import com.example.Pet.Clinic.Spring.Project.reposirtory.PetRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends Owner implements PetService {

    private final PetRepository petRepository;
    private final OwnerService ownerService;
    private final ModelMapper modelMapper;

    @Autowired
    public PetServiceImpl(PetRepository petRepository, OwnerService ownerService, ModelMapper modelMapper) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<PetDto> findAll() {
        return petRepository.findAll()
                .stream()
                .map(pet -> modelMapper.map(pet, PetDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public PetDto findById(@NonNull Long id) {
        Pet pet = petRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Pet with id: " + id + " is not found."));
        return modelMapper.map(pet, PetDto.class);
    }

    @Override
    public PetDto save(@NonNull PetDto petDto) {
        try {
            petDto.setId(null);
            OwnerDto ownerDtoOfPet = ownerService.findById(petDto.getOwnerId());
            Owner ownerOfPet = modelMapper.map(ownerDtoOfPet, Owner.class);
            Pet pet = modelMapper.map(petDto, Pet.class);
            pet.setOwner(ownerOfPet);
            petRepository.save(pet);
            return modelMapper.map(pet, PetDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Pet with name " + petDto.getName() + " already exist.");
        }
    }

    @Override
    public PetDto update(@NonNull PetDto petDto) {
        if (petDto.getId() == null) {
            throw new BadRequestException("The id must not be null.");
        }
        Optional<Pet> maybePet = petRepository.findById(petDto.getId());
        if (maybePet.isPresent()) {
            Pet pet = modelMapper.map(petDto, Pet.class);
            petRepository.save(pet);
            return modelMapper.map(pet, PetDto.class);
        }
        throw new BadRequestException("Pet with name: " + petDto.getName() + " does not exist.");
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            petRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("Pet with id: " + id + " is not found");
        }
    }
}
