package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Pet;
import com.expeditors.adoptionapp.domain.Pet;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        MockHttpServletRequestBuilder builder = get("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    public void getPetById() throws Exception {
        ResultActions actions = mockMvc.perform( get("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Pet> pets = mapper.readValue(jsonResult, new TypeReference<List<Pet>>() {});
        Pet pet = pets.getLast();

        MockHttpServletRequestBuilder builder = get("/Pet/{id}",pet.getPetId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void insert() throws Exception {
        Pet pet = new Pet.PetBuilder(Pet.PetType.Dog).setName("Mardog").setBreed("Daness").build();

        String jsonString = mapper.writeValueAsString(pet);

        ResultActions actions = mockMvc.perform(post("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));

//        doReturn(pet).when(adoptionService).insert(pet);

        actions = actions.andExpect(status().isCreated());

        MvcResult result = actions.andReturn();
        String locHeader = result.getResponse().getHeader("Location");
        assertNotNull(locHeader);
        System.out.println("locHeader: " + locHeader);
    }

    @Test
    @Transactional
    public void deletePet() throws Exception {
        ResultActions actions = mockMvc.perform(delete("/Pet/{id}", 1000)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isNotFound());

        Pet pet = new Pet.PetBuilder(Pet.PetType.Dog).setName("Mardog").setBreed("Daness").build();

        String jsonString = mapper.writeValueAsString(pet);

        actions = mockMvc.perform(post("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
//        doReturn(pet).when(adoptionService).insert(pet);
        actions.andExpect(status().isCreated());

//        doReturn(pet).when(adoptionService).getById(234);
//        doReturn(true).when(adoptionService).deleteById(234);

        actions = mockMvc.perform( get("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Pet> pets = mapper.readValue(jsonResult, new TypeReference<List<Pet>>() {});
        Pet petToDelete = pets.getLast();

        actions = mockMvc.perform(delete("/Pet/{id}", petToDelete.getPetId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void updatePet() throws Exception {

        Pet newPet = new Pet.PetBuilder(Pet.PetType.Dog).setName("Mardog").setBreed("Daness").build();

        String jsonString = mapper.writeValueAsString(newPet);

        ResultActions actions = mockMvc.perform(post("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));
//        doReturn(pet).when(adoptionService).insert(pet);
        actions.andExpect(status().isCreated());

//        doReturn(pet).when(adoptionService).getById(234);
//        doReturn(true).when(adoptionService).deleteById(234);

        actions = mockMvc.perform( get("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<Pet> pets = mapper.readValue(jsonResult, new TypeReference<List<Pet>>() {});
        Pet pet = pets.getLast();

        pet.setName("Rawler");
//
        String updateEntity = mapper.writeValueAsString(pet);


        actions = mockMvc.perform(put("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isOk());


        MockHttpServletRequestBuilder builder = get("/Pet/{id}", pet.getPetId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        result = actions.andReturn();

        jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        pet = mapper.treeToValue(node, Pet.class);

        assertEquals("Rawler", pet.getName());

        pet.setPetId(100);
        updateEntity = mapper.writeValueAsString(pet);
        //doReturn(null).when(adoptionService).update(pet);
        actions = mockMvc.perform(put("/Pet")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isNotFound());

    }
}
