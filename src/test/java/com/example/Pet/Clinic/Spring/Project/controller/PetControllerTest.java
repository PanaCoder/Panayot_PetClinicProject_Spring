package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.PetDto;
import com.example.Pet.Clinic.Spring.Project.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest extends BaseControllerTest {

    @MockBean
    private PetService petService;

    @Test
    public void findAll() throws Exception {
        when(petService.findAll()).thenReturn(new HashSet<PetDto>(Collections.singletonList(petDto)));

        mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    public void findAllWhenEmpty() throws Exception {
        when(petService.findAll()).thenReturn(new HashSet<>(Collections.emptyList()));

        mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", is(empty())));
    }

    @Test
    public void findById() throws Exception {
        when(petService.findById(1L)).thenReturn(petDto);

        mvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Robi")));
    }

    @Test
    public void saveHappy() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestPet);

        when(petService.save(requestPet)).thenReturn(responsePet);

        String responseJson = objectMapper.writeValueAsString(responsePet);

        mvc.perform(post("/pets")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Robi")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().json(responseJson, true));
    }

    @Test
    public void saveUnhappyWhenHaveNullPropertyInObject() throws Exception {
        requestPet.setName(null);
        String requestJson = objectMapper.writeValueAsString(requestPet);

        mvc.perform(post("/pets")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveWhenNull() throws Exception {
        mvc.perform(post("/pets")
                .content(EMPTY_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
