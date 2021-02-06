package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.exception.BadRequestException;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Owner;
import com.example.Pet.Clinic.Spring.Project.reposirtory.OwnerRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository, ModelMapper modelMapper) {
        this.ownerRepository = ownerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<OwnerDto> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(owner -> modelMapper.map(owner, OwnerDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public OwnerDto findById(@NonNull Long id) {
        Owner owner = ownerRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Owner with id: " + id + " is not found."));
        return modelMapper.map(owner, OwnerDto.class);
    }

    @Override
    public OwnerDto findByPhone(@NonNull String phone) {
        Owner owner = ownerRepository
                .findByPhone(phone)
                .orElseThrow(() -> new RecordNotFoundException("Owner with phone: " + phone + " is not found."));
        return modelMapper.map(owner, OwnerDto.class);
    }

    @Override
    public OwnerDto save(@NonNull OwnerDto ownerDto) {
        try {
            ownerDto.setId(null);
            Owner owner = modelMapper.map(ownerDto, Owner.class);
            ownerRepository.save(owner);
            return modelMapper.map(owner, OwnerDto.class);
        } catch (DataIntegrityViolationException e) {
            // test
            throw new DuplicateRecordException("Duplicate record for: " + ownerDto);
        }
    }

    @Override
    public OwnerDto update(@NonNull OwnerDto ownerDto) {
        if (ownerDto.getId() == null) {
            throw new BadRequestException("The id must not be null.");
        }
        Optional<Owner> maybeOwner = ownerRepository.findById(ownerDto.getId());
        if (maybeOwner.isPresent()) {
            Owner owner = modelMapper.map(ownerDto, Owner.class);
            ownerRepository.save(owner);
            return modelMapper.map(owner, OwnerDto.class);
        }
        throw new BadRequestException("Owner with id: " + ownerDto.getId() + " does not exist.");
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            ownerRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("Owner with id: " + id + " is not found");
        }
    }

    @Override
    public void deleteByPhone(@NonNull String phone) {
        try {
            ownerRepository.deleteByPhone(phone);
        } catch (Exception e) {
            throw new RecordNotFoundException("Owner with phone: " + phone + " is not found");
        }
    }
}
