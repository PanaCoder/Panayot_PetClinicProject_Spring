package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;
import com.example.Pet.Clinic.Spring.Project.model.PetType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BaseControllerTest {

    static final String EMPTY_JSON = "";
    final OwnerDto ownerDto = OwnerDto.builder()
            .id(1L)
            .firstName("Pana")
            .lastName("Atanasov")
            .phone("1234567890")
            .build();
    final OwnerDto requestOwner = OwnerDto.builder()
            .firstName("Pana")
            .lastName("Atanasov")
            .phone("1234567890")
            .build();
    final OwnerDto responseOwner = OwnerDto.builder()
            .id(1L)
            .firstName(requestOwner.getFirstName())
            .lastName(requestOwner.getLastName())
            .phone(requestOwner.getPhone())
            .build();
    final PetDto petDto = PetDto.builder()
            .id(1L)
            .name("Robi")
            .petType(PetType.DOG)
            .build();
    final PetDto requestPet = PetDto.builder()
            .name("Robi")
            .petType(PetType.DOG)
            .build();
    final PetDto responsePet = PetDto.builder()
            .id(1L)
            .name("Robi")
            .petType(PetType.DOG)
            .build();
    final VisitDto visitDto = VisitDto.builder()
            .id(1L)
            .treatment("treatmentTest")
            .build();
    final VisitDto requestVisit = VisitDto.builder()
            .treatment("treatmentTest")
            .build();
    final VisitDto responseVisit = VisitDto.builder()
            .id(1L)
            .treatment("treatmentTest")
            .build();

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

}
