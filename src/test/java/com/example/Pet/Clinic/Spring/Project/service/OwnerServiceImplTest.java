package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.Owner;
import com.example.Pet.Clinic.Spring.Project.reposirtory.OwnerRepository;
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
class OwnerServiceImplTest {

    @Mock
    private OwnerRepository ownerRepository;
    private OwnerServiceImpl ownerService;
    private ModelMapper modelMapper;
    private final Owner owner = Owner.builder().id(1L).firstName("Pana").lastName("Atanasov").phone("1234567890").build();
    private final OwnerDto ownerDto = OwnerDto.builder().id(1L).firstName("Pana").lastName("Atanasov").phone("1234567890").build();

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();
        ownerService = new OwnerServiceImpl(ownerRepository, modelMapper);
    }

    @Test
    void findAll() {
        when(ownerRepository.findAll())
                .thenReturn(Collections.singletonList(owner));

        Set<OwnerDto> ownerSet = ownerService.findAll();

        assertThat(ownerSet, hasItem(ownerDto));
        assertThat(ownerSet, hasSize(1));
    }

    @Test
    void findByIdHappy() {
        when(ownerRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        OwnerDto foundOwnerDto = ownerService.findById(1L);

        assertThat(foundOwnerDto, is(notNullValue()));
        assertEquals("Pana", foundOwnerDto.getFirstName());
        assertEquals(1, foundOwnerDto.getId());
    }

    @Test
    void findByPhoneHappy() {
        when(ownerRepository.findByPhone("1234567890"))
                .thenReturn(Optional.of(owner));

        OwnerDto foundOwnerDto = ownerService.findByPhone("1234567890");

        assertThat(foundOwnerDto, is(notNullValue()));
        assertEquals("1234567890", foundOwnerDto.getPhone());
        assertEquals(1, foundOwnerDto.getId());
    }

    @Test
    public void findByIdExpectRecordNotFoundException() {
        when(ownerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> ownerService.findById(1L));
    }

    @Test
    public void saveExpectNPE() {
        assertThrows(NullPointerException.class, () -> ownerService.save(null));
    }

    @Test
    public void saveVerifyDuplicateRecordException() {
        owner.setId(null);
        when(ownerRepository.save(eq(owner)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> ownerService.save(ownerDto));
        verify(ownerRepository, times(1)).save(any());
    }

    @Test
    public void saveHappy() {
        owner.setId(null);
        when(ownerRepository.save(eq(owner)))
                .thenReturn(owner);

        OwnerDto actual = ownerService.save(ownerDto);

        assertThat(actual, is(notNullValue()));
        assertEquals("Pana", actual.getFirstName());
    }

    @Test
    public void deleteByIdHappy() {
        Long id = 1L;

        ownerService.deleteById(id);

        verify(ownerRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> ownerService.deleteById(null));
    }

    @Test
    public void updateExpectNPE() {
        assertThrows(NullPointerException.class, () -> ownerService.update(null));
    }

}
