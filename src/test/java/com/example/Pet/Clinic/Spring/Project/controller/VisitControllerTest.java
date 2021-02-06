package com.example.Pet.Clinic.Spring.Project.controller;

import com.example.Pet.Clinic.Spring.Project.dto.VisitDto;
import com.example.Pet.Clinic.Spring.Project.service.VisitService;
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

@WebMvcTest(VisitController.class)
class VisitControllerTest extends BaseControllerTest {

    @MockBean
    private VisitService visitService;

    @Test
    public void findAll() throws Exception {
        when(visitService.findAll()).thenReturn(new HashSet<VisitDto>(Collections.singletonList(visitDto)));

        mvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    public void findAllWhenEmpty() throws Exception {
        when(visitService.findAll()).thenReturn(new HashSet<>(Collections.emptyList()));

        mvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", is(empty())));
    }

    @Test
    public void findById() throws Exception {
        when(visitService.findById(1L)).thenReturn(visitDto);

        mvc.perform(get("/visits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.treatment", is("treatmentTest")));
    }

    @Test
    public void saveHappy() throws Exception {
        String requestJson = objectMapper.writeValueAsString(requestVisit);

        when(visitService.save(requestVisit)).thenReturn(responseVisit);

        String responseJson = objectMapper.writeValueAsString(responseVisit);

        mvc.perform(post("/visits")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.treatment", is("treatmentTest")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(content().json(responseJson, true));
    }

    @Test
    public void saveUnhappyWhenHaveNullPropertyInObject() throws Exception {
        requestVisit.setTreatment(null);
        String requestJson = objectMapper.writeValueAsString(requestVisit);

        mvc.perform(post("/visits")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveWhenNull() throws Exception {
        mvc.perform(post("/visits")
                .content(EMPTY_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
