package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Pet;
import com.example.Pet.Clinic.Spring.Project.model.PetType;
import com.example.Pet.Clinic.Spring.Project.reposirtory.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private OwnerServiceImpl ownerService;
    private PetServiceImpl petService;
    private final Pet pet = Pet.builder().id(1L).name("Robi").petType(PetType.DOG).build();
    private final PetDto petDto = PetDto.builder().id(1L).name("Robi").petType(PetType.DOG).build();
    private final OwnerDto ownerDtoOfPet = OwnerDto.builder().id(1L).firstName("Pana").lastName("Atanasov").phone("1234567890").build();

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        petService = new PetServiceImpl(petRepository, ownerService, modelMapper);
    }

    @Test
    void findAll() {
        when(petRepository.findAll())
                .thenReturn(Collections.singletonList(pet));

        Set<PetDto> petDtoSet = petService.findAll();

        assertThat(petDtoSet, hasItem(petDto));
        assertThat(petDtoSet, hasSize(1));
    }

    @Test
    void findByIdHappy() {
        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        PetDto foundPetDto = petService.findById(1L);

        assertThat(foundPetDto, is(notNullValue()));
        assertEquals("Robi", foundPetDto.getName());
        assertEquals(1, foundPetDto.getId());
    }

    @Test
    public void findByIdExpectRecordNotFoundException() {
        when(petRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> petService.findById(1L));
    }

    @Test
    public void saveExpectNPE() {
        assertThrows(NullPointerException.class, () -> petService.save(null));
    }

    @Test
    public void saveVerifyDuplicateRecordException() {
        pet.setId(null);
        when(ownerService.findById(any()))
                .thenReturn(ownerDtoOfPet);
        when(petRepository.save(eq(pet)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> petService.save(petDto));
        verify(petRepository, times(1)).save(any());
    }

    @Test
    public void saveHappy() {
        pet.setId(null);

        when(ownerService.findById(any()))
                .thenReturn(ownerDtoOfPet);
        when(petRepository.save(eq(pet)))
                .thenReturn(pet);

        PetDto actual = petService.save(petDto);

        assertThat(actual, is(notNullValue()));
        assertEquals("Robi", actual.getName());
    }

    @Test
    public void deleteByIdHappy() {
        Long id = 1L;

        petService.deleteById(id);

        verify(petRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> petService.deleteById(null));
    }

    @Test
    public void updateExpectNPE() {
        assertThrows(NullPointerException.class, () -> petService.update(null));
    }
}
