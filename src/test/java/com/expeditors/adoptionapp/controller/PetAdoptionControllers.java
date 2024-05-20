package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.services.AdoptionRepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PetAdoptionControllers {

    private List<Adopter> adopters = List.of(
            new Adopter(
                    1,
                    "Francisco",
                    "8677566545"),
            new Adopter(
                    2,
                    "Amador Hernandez",
                    "8677654512"));

    @MockBean
    private AdoptionRepoService adoptionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        MockHttpServletRequestBuilder builder = get("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        doReturn(adopters).when(adoptionService).getAll();

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    public void getAdopterById() throws Exception {
        MockHttpServletRequestBuilder builder = get("/Adopter/{id}",1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        doReturn(adopters.get(0)).when(adoptionService).getById(1);

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }
}
