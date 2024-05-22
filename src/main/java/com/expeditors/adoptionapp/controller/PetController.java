package com.expeditors.adoptionapp.controller;

import com.expeditors.adoptionapp.domain.Adopter;
import com.expeditors.adoptionapp.domain.AdoptionRegister;
import com.expeditors.adoptionapp.domain.Pet;
import com.expeditors.adoptionapp.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("Pet")
@Profile("jpa")
public class PetController {

    @Autowired
    private PetService service;

    @GetMapping()
    public List<Pet> getPets() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        Optional<Pet> pet = service.getById(id);
        if (pet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found pet with id:" + id);
        }
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Pet pet) {
        Pet newPet = service.insert(pet);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pet.getPetId())
                .toUri();

        return ResponseEntity.created(newResource).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        boolean result = service.deleteById(id);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found pet with id: " + id);
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Pet pet) {
        boolean result = service.update(pet);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found pet with id: " + pet.getPetId());
        }

        return ResponseEntity.ok(result);
    }
}
