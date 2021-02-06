package com.example.Pet.Clinic.Spring.Project.service;

import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;
import com.example.Pet.Clinic.Spring.Project.exception.DuplicateRecordException;
import com.example.Pet.Clinic.Spring.Project.exception.RecordNotFoundException;
import com.example.Pet.Clinic.Spring.Project.model.PetType;
import com.example.Pet.Clinic.Spring.Project.model.Visit;
import com.example.Pet.Clinic.Spring.Project.reposirtory.VisitRepository;
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
class VisitServiceImplTest {

    @Mock
    private VisitRepository visitRepository;
    @Mock
    private PetServiceImpl petService;
    private VisitServiceImpl visitService;
    private final Visit visit = Visit.builder().id(1L).treatment("treatmentTest").build();
    private final VisitDto visitDto = VisitDto.builder().id(1L).treatment("treatmentTest").build();
    private final PetDto petDtoOfVisit = PetDto.builder().id(1L).name("Robi").petType(PetType.DOG).build();

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        visitService = new VisitServiceImpl(visitRepository, petService, modelMapper);
    }


    @Test
    void findAll() {
        when(visitRepository.findAll())
                .thenReturn(Collections.singletonList(visit));

        Set<VisitDto> VisitDtoSet = visitService.findAll();

        assertThat(VisitDtoSet, hasItem(visitDto));
        assertThat(VisitDtoSet, hasSize(1));
    }

    @Test
    void findByIdHappy() {
        when(visitRepository.findById(1L))
                .thenReturn(Optional.of(visit));

        VisitDto foundVisitDto = visitService.findById(1L);

        assertThat(foundVisitDto, is(notNullValue()));
        assertEquals("treatmentTest", foundVisitDto.getTreatment());
        assertEquals(1, foundVisitDto.getId());
    }

    @Test
    public void findByIdExpectRecordNotFoundException() {
        when(visitRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> visitService.findById(1L));
    }

    @Test
    public void saveExpectNPE() {
        assertThrows(NullPointerException.class, () -> visitService.save(null));
    }

    @Test
    public void saveVerifyDuplicateRecordException() {
        visit.setId(null);
        when(petService.findById(any()))
                .thenReturn(petDtoOfVisit);
        when(visitRepository.save(eq(visit)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> visitService.save(visitDto));
        verify(visitRepository, times(1)).save(any());
    }

    @Test
    public void saveHappy() {
        visit.setId(null);

        when(petService.findById(any()))
                .thenReturn(petDtoOfVisit);
        when(visitRepository.save(eq(visit)))
                .thenReturn(visit);

        VisitDto actual = visitService.save(visitDto);

        assertThat(actual, is(notNullValue()));
        assertEquals("treatmentTest", actual.getTreatment());
    }

    @Test
    public void deleteByIdHappy() {
        Long id = 1L;

        visitService.deleteById(id);

        verify(visitRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> visitService.deleteById(null));
    }

    @Test
    public void updateExpectNPE() {
        assertThrows(NullPointerException.class, () -> visitService.update(null));
    }

}