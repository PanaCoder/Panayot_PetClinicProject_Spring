package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.AddressDto;
import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.exception.BadRequestException;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Address;
import com.example.Pet.Clinic.Spring.Project.model.Owner;
import com.example.Pet.Clinic.Spring.Project.reposirtory.AddressRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final OwnerService ownerService;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, OwnerService ownerService, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.ownerService = ownerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public AddressDto findById(@NonNull Long id) {
        Address address = addressRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Address with id: " + id + " is not found."));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public AddressDto save(@NonNull AddressDto addressDto) {
        try {
            addressDto.setId(null);
            Address address = modelMapper.map(addressDto, Address.class);
            Address savedAddress = addressRepository.save(address);
            OwnerDto ownerDto = ownerService.findById(addressDto.getOwnerId());
            Owner owner = modelMapper.map(ownerDto, Owner.class);
            owner.setAddress(savedAddress);
            ownerDto = modelMapper.map(owner, OwnerDto.class);
            ownerService.update(ownerDto);
            return modelMapper.map(address, AddressDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Duplicate record for: " + addressDto);
        }
    }

    @Override
    public AddressDto update(@NonNull AddressDto addressDto) {
        if (addressDto.getId() == null) {
            throw new BadRequestException("The id must not be null.");
        }
        Optional<Address> maybeAddress = addressRepository.findById(addressDto.getId());
        if (maybeAddress.isPresent()) {
            Address address = modelMapper.map(addressDto, Address.class);
            addressRepository.save(address);
            return modelMapper.map(address, AddressDto.class);
        }
        throw new BadRequestException("Address: " + addressDto + " does not exist.");
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            addressRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("Address with id: " + id + " is not found");
        }
    }
}
