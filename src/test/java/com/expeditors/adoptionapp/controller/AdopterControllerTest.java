package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Adopter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AdopterControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        MockHttpServletRequestBuilder builder = get("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        //doReturn(adopters).when(adoptionService).getAll();

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    public void getAdopterById() throws Exception {
        ResultActions actions = mockMvc.perform( get("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Adopter> adopters = mapper.readValue(jsonResult, new TypeReference<List<Adopter>>() {});
        Adopter adopter = adopters.getLast();

        MockHttpServletRequestBuilder builder = get("/Adopter/{id}",adopter.getAdopterId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void insert() throws Exception {
        Adopter adopter = new Adopter(
                1,
                "Francisco",
                "8677566545");

        String jsonString = mapper.writeValueAsString(adopter);

        ResultActions actions = mockMvc.perform(post("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));

//        doReturn(adopter).when(adoptionService).insert(adopter);

        actions = actions.andExpect(status().isCreated());

        MvcResult result = actions.andReturn();
        String locHeader = result.getResponse().getHeader("Location");
        assertNotNull(locHeader);
        System.out.println("locHeader: " + locHeader);
    }

    @Test
    @Transactional
    public void deleteAdopter() throws Exception {
        ResultActions actions = mockMvc.perform(delete("/Adopter/{id}", 1000)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isNotFound());

        Adopter adopter = new Adopter(
                234,
                "Francisco",
                "8677566545");

        String jsonString = mapper.writeValueAsString(adopter);

        actions = mockMvc.perform(post("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
//        doReturn(adopter).when(adoptionService).insert(adopter);
        actions.andExpect(status().isCreated());

//        doReturn(adopter).when(adoptionService).getById(234);
//        doReturn(true).when(adoptionService).deleteById(234);

        actions = mockMvc.perform( get("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Adopter> adopters = mapper.readValue(jsonResult, new TypeReference<List<Adopter>>() {});
        Adopter adopterToDelete = adopters.getLast();

        actions = mockMvc.perform(delete("/Adopter/{id}", adopterToDelete.getAdopterId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void updateAdopter() throws Exception {

        Adopter newAdopter = new Adopter(
                234,
                "Francisco",
                "8677566545");

        String jsonString = mapper.writeValueAsString(newAdopter);

        ResultActions actions = mockMvc.perform(post("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
//        doReturn(adopter).when(adoptionService).insert(adopter);
        actions.andExpect(status().isCreated());

//        doReturn(adopter).when(adoptionService).getById(234);
//        doReturn(true).when(adoptionService).deleteById(234);

        actions = mockMvc.perform( get("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Adopter> adopters = mapper.readValue(jsonResult, new TypeReference<List<Adopter>>() {});
        Adopter adopter = adopters.getLast();

        adopter.setName("Mariana Rios");
//
        String updateEntity = mapper.writeValueAsString(adopter);


        actions = mockMvc.perform(put("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isOk());


        MockHttpServletRequestBuilder builder = get("/Adopter/{id}", adopter.getAdopterId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        result = actions.andReturn();

        jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        adopter = mapper.treeToValue(node, Adopter.class);

        assertEquals("Mariana Rios", adopter.getName());

        adopter.setAdopterId(100);
        updateEntity = mapper.writeValueAsString(adopter);
        //doReturn(null).when(adoptionService).update(adopter);
        actions = mockMvc.perform(put("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isNotFound());

    }
}
