package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.OwnerDto;
import com.example.Pet.Clinic.Spring.Project.service.OwnerService;
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

@WebMvcTest(OwnerController.class)
class OwnerControllerTest extends BaseControllerTest {

    @MockBean
    private OwnerService ownerService;

    @Test
    public void findAll() throws Exception {
        when(ownerService.findAll()).thenReturn(new HashSet<OwnerDto>(Collections.singletonList(ownerDto)));

        mvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }


    @Test
    public void findAllWhenEmpty() throws Exception {
        when(ownerService.findAll()).thenReturn(new HashSet<>(Collections.emptyList()));

        mvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", is(empty())));
    }

    @Test
    public void findById() throws Exception {
        when(ownerService.findById(1L)).thenReturn(ownerDto);

        mvc.perform(get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Pana")));
    }

    @Test
    public void saveHappy() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestOwner);

        when(ownerService.save(requestOwner)).thenReturn(responseOwner);

        String responseJson = objectMapper.writeValueAsString(responseOwner);

        mvc.perform(post("/owners")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Pana")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().json(responseJson, true));
    }

    @Test
    public void saveUnhappyWhenHaveNullPropertyInObject() throws Exception {
        requestOwner.setFirstName(null);
        String requestJson = objectMapper.writeValueAsString(requestOwner);

        mvc.perform(post("/owners")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveWhenNull() throws Exception {
        mvc.perform(post("/owners")
                .content(EMPTY_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
