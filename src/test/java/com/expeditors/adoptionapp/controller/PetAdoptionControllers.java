package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.services.AdoptionRepoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@DataJpaTest
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

    @Autowired
    private ObjectMapper mapper;

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

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

        doReturn(adopter).when(adoptionService).insert(adopter);

        actions = actions.andExpect(status().isCreated());

        MvcResult result = actions.andReturn();
        String locHeader = result.getResponse().getHeader("Location");
        assertNotNull(locHeader);
        System.out.println("locHeader: " + locHeader);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
        doReturn(adopter).when(adoptionService).insert(adopter);
        actions.andExpect(status().isCreated());

        doReturn(adopter).when(adoptionService).getById(234);
        doReturn(true).when(adoptionService).deleteById(234);

        actions = mockMvc.perform(delete("/Adopter/{id}", 234)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateAdopter() throws Exception {
        Adopter adopter = new Adopter(
                1,
                "Francisco",
                "8677566545");
        doReturn(adopter).when(adoptionService).getById(1);

//        MockHttpServletRequestBuilder builder = get("/Adopter/{id}", 1)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        ResultActions actions = mockMvc.perform(builder)
//                .andExpect(status().isOk());
//        MvcResult result = actions.andReturn();
//
//
////        String jsonResult = result.getResponse().getContentAsString();
////        JsonNode node = mapper.readTree(jsonResult);
////        Adopter adopter = mapper.treeToValue(node.get("entity"), Adopter.class);
//        adopter.setName("Mariana Rios");
//
        String updateEntity = mapper.writeValueAsString(adopter);


        doReturn(adopter).when(adoptionService).insert(adopter);
        doReturn(true).when(adoptionService).update(adopter);

        ResultActions actions = mockMvc.perform(put("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isOk());

        //doReturn(adopter).when(adoptionService).getById(1);

        MockHttpServletRequestBuilder builder = get("/Adopter/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        //result = actions.andReturn();

//        jsonResult = result.getResponse().getContentAsString();
//        node = mapper.readTree(jsonResult);
//        adopter = mapper.treeToValue(node.get("entity"), Adopter.class);

        assertEquals("Marina Rios", adopter.getName());

        adopter.setAdopterId(100);
        updateEntity = mapper.writeValueAsString(adopter);
        doReturn(null).when(adoptionService).update(adopter);
        actions = mockMvc.perform(put("/Adopter")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEntity));

        actions.andExpect(status().isNotFound());

    }
}
